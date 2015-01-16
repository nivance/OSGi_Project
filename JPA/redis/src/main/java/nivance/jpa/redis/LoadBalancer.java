package nivance.jpa.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.exception.JPAException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoadBalancer implements InitializingBean, DisposableBean {

	public interface Everyone {
		public boolean execute(Connection conn, String key, Object value);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.ha.start();
	}

	@Override
	public void destroy() throws Exception {
		lock.writeLock().lock();
		try {
			for (Connection conn : actives) {
				try {
					conn.close();
					log.warn("close " + conn + " success when shutdown app.");
				} catch (JPAException e) {
					log.error("close " + conn + " failed when shutdown app,", e);
				}
			}
			this.ha.shutdown();
			log.info(">>>>>>>>>>>>>>>>>>redis cluster HA has stoped.");
		} finally {
			lock.writeLock().unlock();
		}
	}

	public boolean useAll(Everyone executor, String key, Object value, boolean write) {
		boolean b = true;
		List<Connection> fails = new ArrayList<Connection>();
		lock.readLock().lock();
		try {
			for (Connection conn : actives) {
				if (!executor.execute(conn, key, value)) {
					fails.add(conn);
				}
			}
			if (fails.size() >= actives.size()) {// 全部写入失败
				b = false;
			}
		} finally {
			lock.readLock().unlock();
		}
		if (fails.size() > 0) {
			lock.writeLock().lock();
			try {
				if (write) {
					for (Connection conn : fails) {
						this.discard(conn);
					}
				} else {
					for (Connection conn : fails) {
						this.hangUp(conn);
					}
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
		return b;
	}

	public Connection nextConnection() {
		lock.readLock().lock();
		try {
			int idx = Math.abs(bulk.incrementAndGet() % actives.size());
			return actives.get(idx);
		} finally {
			lock.readLock().unlock();
		}
	}

	public int size() {
		lock.readLock().lock();
		try {
			return actives.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	public void discard(Connection conn) {
		lock.writeLock().lock();
		try {
			if (actives.size() > 1) {
				balance.delete(conn);
				actives.remove(conn);
				conn.close();
				log.warn("discard " + conn + " and close it success.");
				connectionCfg.turnoffAddress(conn.getHost(), conn.getPort());
			} else {
				log.error("discard " + conn + ", but it's the only one connection, so still keep it.");
			}
		} catch (JPAException e) {
			log.error(conn + " has retry connect [" + conn.getConnectCount() + "] times and stop retry, but close it error.", e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void hangUp(Connection one) {
		lock.writeLock().lock();
		Connection conn = null;
		try {
			if (actives.contains(one)) {
				int idx = actives.indexOf(one);
				conn = actives.get(idx);
				if (actives.size() > 1) {
					actives.remove(idx);
					ha.add(conn);
					log.warn(conn + " was hangup, and it will try reconnect.");
				} else {
					log.error(conn + " should be hangup, but it's the only one connection, so still keep it.");
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	protected void reBalance(List<Connection> conns) {
		lock.writeLock().lock();
		try {
			this.ha.clean();
			this.balance.add(conns);
			List<Connection> needRetries = new ArrayList<Connection>();
			// 建立新的连接
			List<Connection> added = new ArrayList<Connection>();
			for (Connection conn : balance.findNew()) {
				try {
					if (conn.connect()) {
						added.add(conn);
						log.warn("find new " + conn + ", and build connection success.");
					} else {
						needRetries.add(conn);
						log.error("find new " + conn + ", but connect it fail, it'll retry.");
					}
				} catch (JPAException e) {
					needRetries.add(conn);
					log.error("find new " + conn + ", but connect it fail, it'll retry.", e);
				}
			}
			if (added.size() > 0) {
				actives.addAll(added);
			}

			// 重建修改配置的连接
			List<Connection> modified = new ArrayList<Connection>();
			for (Connection conn : balance.findParamChanged()) {
				try {
					if (conn.connect()) {
						// 关闭旧连接
						int idx = actives.indexOf(conn);
						if (idx >= 0) {
							Connection old = actives.get(idx);
							old.close();
							actives.remove(idx);
						}
						modified.add(conn);
						log.warn("find " + conn + "'s parameter has changed, and rebuild connection success.");
					} else {
						needRetries.add(conn);
						log.error("find " + conn + "'s parameter has changed, but rebuild connection fail, it'll retry.");
					}
				} catch (JPAException e) {
					needRetries.add(conn);
					log.error("find " + conn + "'s parameter has changed, but rebuild connection fail, it'll retry.", e);
				}
			}
			if (modified.size() > 0) {
				actives.addAll(modified);
			}

			// 删除丢弃的连接
			for (Connection conn : balance.findRemoved()) {
				try {
					if (actives.size() > 1) {
						actives.remove(conn);
						conn.close();
						log.warn("find " + conn + " has deleted or turnoff, and close connection success.");
					} else {
						log.error("find " + conn + " has deleted or turnoff, but it's the only one connection, so still keep it.");
					}
				} catch (JPAException e) {
					log.error("find " + conn + " has deleted or turnoff, but close connection fail.", e);
				}
			}

			// 重试失败的连接
			for (Connection conn : needRetries) {
				ha.add(conn);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	private void revive(Connection one) {
		lock.writeLock().lock();
		try {
			if (balance.contains(one)) {
				actives.add(one);
				log.warn(one + " is back in loadbalance.");
			} else {
				try {
					one.close();
					log.warn("though " + one + " reconnect success, but cause cfg it doesn't back in loadbalance, and close it success.");
				} catch (JPAException e) {
					log.warn("though " + one + " reconnect success, but cause cfg it doesn't back in loadbalance, and close it failed.", e);
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	private @Resource(name = "connectionCfg") ConnectionCfg connectionCfg;
	private final HA ha = new HA();
	private final Balance balance = new Balance();
	private final List<Connection> actives = new ArrayList<Connection>();
	private final AtomicInteger bulk = new AtomicInteger(-1);
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	// NOT ThreadSafe
	private class Balance {
		public void add(List<Connection> conns) {
			for (Connection conn : conns) {
				if (allConns.contains(conn)) {
					this.tempExist.add(conn);
				} else {
					this.tempNew.add(conn);
				}
			}
			this.tempAllAdded.addAll(conns);
		}

		public List<Connection> findNew() {
			List<Connection> r = new ArrayList<Connection>();
			r.addAll(this.tempNew);
			this.allConns.addAll(this.tempNew);
			this.tempNew.clear();
			return r;
		}

		public List<Connection> findRemoved() {
			List<Connection> r = new ArrayList<Connection>();
			List<Connection> delete = new ArrayList<Connection>();

			this.allConns.addAll(this.tempNew);
			for (Connection conn : this.allConns) {
				if (!this.tempAllAdded.contains(conn)) {
					r.add(conn);
					delete.add(conn);
				}
			}
			this.tempAllAdded.clear();
			this.allConns.removeAll(delete);
			return r;
		}

		public List<Connection> findParamChanged() {
			List<Connection> r = new ArrayList<Connection>();
			for (Connection conn : this.tempExist) {
				for (Connection ccc : this.allConns) {
					if (ccc.equals(conn)) {
						if (ccc.getReadTimeoutMillis() != conn.getReadTimeoutMillis()) {
							r.add(conn);
						} else if (ccc.getReadMaxActive() != conn.getReadMaxActive()) {
							r.add(conn);
						} else if (ccc.getReadMaxIdle() != conn.getReadMaxIdle()) {
							r.add(conn);
						} else if (ccc.getWriteTimeoutMillis() != conn.getWriteTimeoutMillis()) {
							r.add(conn);
						} else if (ccc.getWriteMaxActive() != conn.getWriteMaxActive()) {
							r.add(conn);
						} else if (ccc.getWriteMaxIdle() != conn.getWriteMaxIdle()) {
							r.add(conn);
						}
					}
				}
			}
			for (Connection conn : r) {
				this.allConns.remove(conn);
				this.allConns.add(conn);
			}
			this.tempExist.clear();
			return r;
		}

		public boolean contains(Connection conn) {
			return allConns.contains(conn);
		}

		public void delete(Connection conn) {
			allConns.remove(conn);
		}

		private final Set<Connection> allConns = new HashSet<Connection>();
		private final List<Connection> tempNew = new ArrayList<Connection>();
		private final List<Connection> tempExist = new ArrayList<Connection>();
		private final List<Connection> tempAllAdded = new ArrayList<Connection>();
	}

	private class HA {
		public void start() {
			this.executor.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							Connection conn = abnormity.take();
							if (conn != null) {
								if (conn.getConnectCount() < maxRetryCount) {
									reConnect(conn);
								} else {
									discardConnection(conn);
								}
							}
						} catch (InterruptedException e) {
							return;
						}
					}
				}
			});
			this.heartBeat.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					useAll(ping, null, null, false);
				}
			}, 2, 1, TimeUnit.SECONDS);
			log.info(">>>>>>>>>>>>>>>>>>redis cluster HA has started.");
		}

		public void shutdown() {
			this.executor.shutdownNow();
			this.heartBeat.shutdownNow();
		}

		public void add(Connection conn) {
			this.waitAMoment(conn);
		}

		public void clean() {
			abnormity.clear();
		}

		private void discardConnection(Connection conn) {
			int time = conn.getConnectCount();
			try {
				conn.close();
				connectionCfg.turnoffAddress(conn.getHost(), conn.getPort());
				log.warn(conn + " has retry connect [" + time + "] times and stop retry.");
			} catch (JPAException e) {
				log.error(conn + " has retry connect [" + time + "] times and stop retry, but close it error.", e);
			}
		}

		private void reConnect(Connection conn) {
			int time = conn.getConnectCount();
			try {
				if (conn.connect()) {
					log.error(conn + " has retry connect [" + time + "] times and success.");
					revive(conn);
				} else {
					log.error(conn + " has retry connect [" + time + "] times and failed, try again after 10 secs.");
					waitAMoment(conn);
				}
			} catch (JPAException e) {
				log.error(conn + " has retry connect [" + time + "] times and failed, try again after 10 secs.", e);
				waitAMoment(conn);
			}
		}

		private void waitAMoment(Connection conn) {
			conn.connetFail(hangUpMillis);
			abnormity.offer(conn);
		}

		private final DelayQueue<Connection> abnormity = new DelayQueue<Connection>();
		private final ExecutorService executor = Executors.newSingleThreadExecutor();
		private final ScheduledExecutorService heartBeat = Executors.newSingleThreadScheduledExecutor();
		private final int maxRetryCount = 6;
		private final int hangUpMillis = 10000;
		private final Everyone ping = new Everyone() {
			@Override
			public boolean execute(Connection conn, String key, Object value) {
				if (!conn.ping()) {
					return false;
				}
				return true;
			}
		};
	}

}
