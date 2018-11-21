package com.leimingtech.core.service.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis客户端
 */
public interface JedisClient {

	String set(String key, String value);

	/**
	 * 存储序列化的数据
	 * @param key
	 * @param value
	 * @return
	 */
	<V> String set(String key, V value);

	String get(String key);

	Long del(String key);

	/**
	 * 根据key获取序列化数据
	 * @param key
	 * @param classs 需要反序列出的对象
	 * @return
	 */
	<T> T get(String key, Class<T> classs);

	<T> List<T> getList(String key, Class<T> classs);

	Long hset(String key, String item, String value);

	<V> Long hset(String key, String item, V value);

	String hget(String key, String item);

	/**
	 * 根据key获取序列化数据
	 * @param key 缓存key
	 * @param item
	 * @param classs 需要反序列出的对象
	 * @param <T>
	 * @return
	 */
	<T> T hget(String key, String item, Class<T> classs);

	Long hdel(String key, String item);

	Long incr(String key);

	Long decr(String key);

	/**
	 *
	 * @Description: 设置存存活时间
	 * @param key
	 * @param second
	 * @return
	 *
	 * @author leechenxiang
	 * @date 2016年4月27日 下午4:34:35
	 */
	Long expire(String key, int second);

	/**
	 *
	 * @Description: 判断key多久过期
	 * @param key
	 * @return 秒 >= 0 剩余秒数 = -1 永久存活 = -2 已经消除
	 *
	 * @author leechenxiang
	 * @date 2016年4月27日 下午4:34:22
	 */
	Long ttl(String key);

	/**
	 *	加入到缓存并自动设置默认缓存时间
	 * @param key
	 * @param value
	 */
	<V> void setEX(String key, V value);

	/**
	 * 是否存在缓存
	 * @param key
	 * @return
	 */
	boolean exists(String key);

	/**
	 * 加入到集合头
	 * @param key
	 * @param value
	 * @return 返回整树，操作后集合中的数量
	 */
	long lpush(String key, String... value);

	/**
	 * 加入到集合尾
	 * @param key
	 * @param value
	 * @return 返回整树，操作后集合中的数量
	 */
	long rpush(String key, String... value);

	/**
	 * jedis.llen获取长度 -1表示取得所有
	 *
	 * @param key
	 * @param startIndex 起始位置，从0开始
	 * @param endIndex   结束位置，jedis.llen获取长度 -1表示取得所有
	 * @return
	 */
	List<String> lrange(String key, int startIndex, int endIndex);

	/**
	 * 加入到集合尾
	 * @param key
	 * @param value
	 * @return 返回整树，操作后集合中的数量
	 */
	long rpushEX(String key, String value);

	/**
	 * 向Set添加一条记录
	 * @param key
	 * @param value
	 * @return  如果value已存在返回0,否则返回1
	 */
	long sadd(String key, String value);

	/**
	 * 返回set集合中的所有成员
	 * @param key
	 * @return
	 */
	Set<String> smembers(String key);

	Map<String,String> hgetAll(String key);
}
