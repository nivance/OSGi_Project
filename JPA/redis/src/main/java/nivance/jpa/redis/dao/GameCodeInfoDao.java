package nivance.jpa.redis.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.exception.JPAException;
import nivance.jpa.redis.Connection;
import nivance.jpa.redis.LoadBalancer;
import nivance.jpa.redis.LoadBalancer.Everyone;
import nivance.jpa.redis.bean.GameCodeInfo;
import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerFactory.Type;
import nivance.serialize.SerializerUtil;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Component("gameCodeInfoDao")
public class GameCodeInfoDao implements RedisDataInfoDao {

	private static final String KEY = "GameCode";
	private Type type = SerializerFactory.Type.JSON;

	@Override
	public boolean batchInsert(Object data) {
		GameCodeInfo game = SerializerUtil.deserialize(type, data, GameCodeInfo.class);
		String field = game.getGamecode();
		return balancer.useAll(insert, field, game, true);
	}

	@Override
	public Object findOne(Object data) throws JPAException, IOException {
		Connection conn = null;
		GameCodeInfo game = SerializerUtil.deserialize(type, data, GameCodeInfo.class);
		try {
			String field = game.getGamecode();
			conn = balancer.nextConnection();
			game = (GameCodeInfo) conn.getGameTemplate().opsForHash().get(KEY, field);
			if(game != null) {
				return SerializerUtil.serialize(type, game);
			}
		} catch(DataAccessException e) {
			balancer.hangUp(conn);
			throw new JPAException(e);
		}
		return null;
	}

	@Override
	public List<Object> findAll(Object data) throws JPAException, IOException {
		Connection conn = null;
		List<Object> list = new ArrayList<Object>();
		try {
			conn = balancer.nextConnection();
			Map<Object, Object> all = conn.getGameTemplate().opsForHash().entries(KEY);
			if(all != null) {
				for(Map.Entry<Object, Object> en : all.entrySet()) {
					GameCodeInfo game = (GameCodeInfo)en.getValue();
					list.add(SerializerUtil.serialize(type, game));
				}
			}
		} catch(DataAccessException e) {
			balancer.hangUp(conn);
			throw new JPAException(e);
		}
		return list;
	}

	@Override
	public boolean deleteOne(Object data) {
		GameCodeInfo game = SerializerUtil.deserialize(type, data, GameCodeInfo.class);
		String field = game.getGamecode();
		return balancer.useAll(deleteone, field, game, true);
	}

	@Override
	public boolean deleteAll(Object data) {
		return balancer.useAll(deleteall, null, null, true);
	}

	@Override
	public long count(Object data) throws JPAException, IOException {
		Connection conn = null;
		try {
			conn = balancer.nextConnection();
			return conn.getGameTemplate().boundHashOps(KEY).size();
		} catch(Exception e) {
			balancer.hangUp(conn);
			throw new JPAException(e);
		}
	}

	private @Resource(name = "loadBalancer") LoadBalancer balancer;
	private final Everyone insert = new Everyone() {
		@Override
		public boolean execute(Connection conn, String field, Object value) {
			GameCodeInfo game = (GameCodeInfo)value;
			boolean b = true;
			try {
				conn.getGameTemplate().opsForHash().put(KEY, field, game);
			} catch (DataAccessException e) {
				b = false;
				log.error("redis地址" + conn + "写入方案代码" + game + "失败.", e);
			}
			return b;
		}
	};
	private final Everyone deleteone = new Everyone() {
		@Override
		public boolean execute(Connection conn, String field, Object value) {
			GameCodeInfo game = (GameCodeInfo)value;
			boolean b = true;
			try {
				conn.getGameTemplate().opsForHash().delete(KEY, field);
			} catch (DataAccessException e) {
				b = false;
				log.error("redis地址" + conn + "删除方案代码" + game + "失败.", e);
			}
			return b;
		}
	};
	private final Everyone deleteall = new Everyone() {
		@Override
		public boolean execute(Connection conn, String field, Object value) {
			boolean b = true;
			try {
				conn.getGameTemplate().delete(KEY);
			} catch (DataAccessException e) {
				b = false;
				log.error("redis地址" + conn + "删除方案代码" + KEY + "失败.", e);
			}
			return b;
		}
	};
}
