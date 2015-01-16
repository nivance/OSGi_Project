package nivance.jpa.redis;

import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.exception.JPAException;
import nivance.jpa.redis.bean.GameCodeInfo;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class Connection implements Delayed {

	public Connection(String host, int port, String password, int writeTimeoutMillis, int writeMaxIdle, int writeMaxActive, int readTimeoutMillis,
			int readMaxIdle, int readMaxActive) {
		this.host = host;
		this.port = port;
		this.password = password;
		this.readTimeoutMillis = readTimeoutMillis;
		this.readMaxIdle = readMaxIdle;
		this.readMaxActive = readMaxActive;
		this.writeTimeoutMillis = writeTimeoutMillis;
		this.writeMaxIdle = writeMaxIdle;
		this.writeMaxActive = writeMaxActive;
	}

	public boolean connect() throws JPAException {
		try {
			this.close();
			JedisPoolConfig rconfig = new JedisPoolConfig();
			rconfig.setMaxActive(this.readMaxActive);
			rconfig.setMaxIdle(this.readMaxIdle);
			this.rjcf = new JedisConnectionFactory(rconfig);
			this.rjcf.setHostName(this.host);
			this.rjcf.setPort(this.port);
			this.rjcf.setPassword(this.password);
			this.rjcf.setTimeout(this.readTimeoutMillis);

			JedisPoolConfig wconfig = new JedisPoolConfig();
			wconfig.setMaxActive(this.writeMaxActive);
			wconfig.setMaxIdle(this.writeMaxIdle);
			this.wjcf = new JedisConnectionFactory(wconfig);
			this.wjcf.setHostName(this.host);
			this.wjcf.setPort(this.port);
			this.wjcf.setPassword(this.password);
			this.wjcf.setTimeout(this.writeTimeoutMillis);

			this.gameTemplate = new RedisTemplate<>();
			this.gameTemplate.setConnectionFactory(this.rjcf);
			this.gameTemplate.setKeySerializer(this.gameTemplate.getStringSerializer());
			this.gameTemplate.setHashKeySerializer(this.gameTemplate.getStringSerializer());
			JacksonJsonRedisSerializer<GameCodeInfo> gameSerializer = new JacksonJsonRedisSerializer<GameCodeInfo>(GameCodeInfo.class);
			this.gameTemplate.setHashValueSerializer(gameSerializer);

			gameTemplate.afterPropertiesSet();

			boolean b = this.ping();
			if (b) {
				this.connectCount = 0;
			}
			return b;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	public void close() throws JPAException {
		try {
			this.connectCount = 0;
			if (rjcf != null) rjcf.destroy();
			if (wjcf != null) wjcf.destroy();
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	protected boolean ping() {
		return this.gameTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
				try {
					conn.ping();
				} catch (RedisConnectionFailureException e) {
					log.error(this.toString() + " make ping fail.");
					return false;
				}
				return true;
			}
		});
	}
	
	public RedisTemplate<String, Map<String, GameCodeInfo>> getGameTemplate() {
		return gameTemplate;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Connection other = (Connection) obj;
		if (host == null) {
			if (other.host != null) return false;
		} else if (!host.equals(other.host)) return false;
		if (port != other.port) return false;
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public String toString() {
		return "[Connection host=" + host + ", port=" + port + "]";
	}

	private @Getter final String host;
	private @Getter final int port;
	private JedisConnectionFactory wjcf;
	private JedisConnectionFactory rjcf;
	private @Getter volatile String password;
	private @Getter volatile int writeTimeoutMillis;
	private @Getter volatile int writeMaxIdle;
	private @Getter volatile int writeMaxActive;
	private @Getter volatile int readTimeoutMillis;
	private @Getter volatile int readMaxIdle;
	private @Getter volatile int readMaxActive;

	private RedisTemplate<String, Map<String, GameCodeInfo>> gameTemplate;

	protected void connetFail(long hangUpMillis) {
		this.hangUpMillis = hangUpMillis;
		this.connectCount++;
		this.abnormalTimestamp = System.currentTimeMillis();
	}

	@Override
	public int compareTo(Delayed o) {
		Connection that = (Connection) o;
		long d = this.getDelay(TimeUnit.MILLISECONDS) - that.getDelay(TimeUnit.MILLISECONDS);
		return d > 0 ? 1 : (d == 0 ? 0 : -1);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long elapsedMillis = System.currentTimeMillis() - this.abnormalTimestamp;
		return unit.convert(this.hangUpMillis - elapsedMillis, TimeUnit.NANOSECONDS);
	}

	private @Getter volatile int connectCount = 0;
	private volatile long abnormalTimestamp = 0;
	private volatile long hangUpMillis = 0;
}
