package com.leimingtech.core.service.impl.jedis;

import com.leimingtech.core.service.jedis.JedisClient;
import com.leimingtech.core.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集群版的jedis客户端操作
 * 
 * @author liuzhen
 * 
 */
public class JedisClientCluster implements JedisClient {

	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public String set(String key, String value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.set(key, value);
	}

	@Override
	public <V> String set(String key, V value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}

		SerializationUtil.checkSerializable(value);

		try {
			return jedisCluster.set(key.getBytes("UTF-8"), SerializationUtil.serialize(value));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String get(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.get(key);
	}

	@Override
	public <T> T get(String key, Class<T> classs) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}

		try {
			byte[] result = jedisCluster.get(key.getBytes("UTF-8"));

			if (result != null) {
				return (T) SerializationUtil.deserialize(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Long del(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}

		Long result = jedisCluster.del(key);
		return result;
	}

	@Override
	public <T> List<T> getList(String key, Class<T> classs) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}

		try {
			byte[] result = jedisCluster.get(key.getBytes("UTF-8"));

			if (result != null) {
				return (List<T>) SerializationUtil.deserialize(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Long hset(String key, String item, String value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.hset(key, item, value);
	}

	@Override
	public <V> Long hset(String key, String item, V value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}

		SerializationUtil.checkSerializable(value);
		try {
			return jedisCluster.hset(key.getBytes("UTF-8"), item.getBytes("UTF-8"),
					SerializationUtil.serialize(value));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String hget(String key, String item) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.hget(key, item);
	}

	/**
	 * 根据key获取序列化数据
	 *
	 * @param key    缓存key
	 * @param item
	 * @param classs 需要反序列出的对象
	 * @return
	 */
	@Override
	public <T> T hget(String key, String item, Class<T> classs) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}

		try {
			byte[] result = jedisCluster.hget(key.getBytes("UTF-8"), item.getBytes("UTF-8"));

			if (result != null) {
				return (T) SerializationUtil.deserialize(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Long hdel(String key, String item) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.hdel(key, item);
	}

	@Override
	public Long incr(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.incr(key);
	}

	@Override
	public Long decr(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.decr(key);
	}

	@Override
	public Long expire(String key, int second) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.expire(key, second);
	}

	@Override
	public Long ttl(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.ttl(key);
	}

	/**
	 * 加入到缓存并自动设置默认缓存时间
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public <V> void setEX(String key, V value) {
		if (!JedisConfig.JEDIS_STATUS || value == null) {
			return;
		}

		SerializationUtil.checkSerializable(value);
		jedisCluster.set(key.getBytes(), SerializationUtil.serialize(value));
		jedisCluster.expire(key, (int) JedisConfig.JEDIS_DEFAULT_EXPIRE);
	}

	/**
	 * 是否存在缓存
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean exists(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return Boolean.FALSE;
		}
		return jedisCluster.exists(key);
	}

	/**
	 * 加入到集合头
	 *
	 * @param key
	 * @param value
	 * @return 返回整树，操作后集合中的数量
	 */
	@Override
	public long lpush(String key, String... value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return 0;
		}
		return jedisCluster.lpush(key, value);
	}

	/**
	 * 加入到集合尾
	 *
	 * @param key
	 * @param value
	 * @return 返回整树，操作后集合中的数量
	 */
	@Override
	public long rpush(String key, String... value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return 0;
		}
		return jedisCluster.rpush(key, value);
	}

	/**
	 * jedis.llen获取长度 -1表示取得所有
	 *
	 * @param key
	 * @param startIndex 起始位置，从0开始
	 * @param endIndex   结束位置，jedis.llen获取长度 -1表示取得所有
	 * @return
	 */
	@Override
	public List<String> lrange(String key, int startIndex, int endIndex) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}

		return jedisCluster.lrange(key, startIndex, endIndex);
	}

	/**
	 * 加入到集合尾
	 *
	 * @param key
	 * @param value
	 * @return 返回整树，操作后集合中的数量
	 */
	@Override
	public long rpushEX(String key, String value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return 0;
		}
		long count = jedisCluster.rpush(key, value);
		jedisCluster.expire(key, (int) JedisConfig.JEDIS_DEFAULT_EXPIRE);
		return count;
	}

	/**
	 * 向Set添加一条记录
	 *
	 * @param key
	 * @param value
	 * @return 如果value已存在返回0, 否则返回1
	 */
	@Override
	public long sadd(String key, String value) {
		if (!JedisConfig.JEDIS_STATUS) {
			return 0;
		}
		return jedisCluster.sadd(key, value);
	}

	/**
	 * 返回set集合中的所有成员
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Set<String> smembers(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.smembers(key);
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		if (!JedisConfig.JEDIS_STATUS) {
			return null;
		}
		return jedisCluster.hgetAll(key);
	}
}
