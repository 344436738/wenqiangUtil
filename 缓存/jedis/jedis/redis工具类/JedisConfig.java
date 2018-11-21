package com.leimingtech.core.service.impl.jedis;

import com.leimingtech.common.utils.PropertiesUtil;
import com.leimingtech.core.util.NumberUtil;
import org.apache.commons.lang3.BooleanUtils;



/**
 * jedis配置信息
 * 
 * @author liuzhen
 * 
 */
public class JedisConfig {

	/**jedis开关*/
	public final static boolean JEDIS_STATUS;
	/**默认过期时间 如果redis.properties中不设置也是半小时*/
	public final static long JEDIS_DEFAULT_EXPIRE;

	static {
		PropertiesUtil p = new PropertiesUtil("redis.properties");
		JEDIS_STATUS = BooleanUtils.toBoolean(p.readProperty("redis.openflag"));
		JEDIS_DEFAULT_EXPIRE = NumberUtil.toLong(p.readProperty("redis.defaultExpire"), 1800);
	}
}
