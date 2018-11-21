package com.sinotrans.visualizationplatform.util;



/**
 * 
 * 微信缓存管理器
 * 
 * @author chenj
 * 
 * @since 2016-6-7
 *
 */
public class CacheManager {

	private BaseCache newsCache;

	private static CacheManager instance;

	private static Object lock = new Object();
	

	
	private CacheManager() {

		newsCache = new BaseCache("LBS",960);

	}

	/**
	 * 
	 * 实例化缓存管理器实例
	 * 
	 * @return
	 */
	public static CacheManager getInstance() {

		if (instance == null) {

			synchronized (lock) {

				if (instance == null) {

					instance = new CacheManager();

				}

			}

		}

		return instance;

	}

	public void remove(String key) {

		newsCache.remove(key);

	}

	public void put(String key, Object value){
		
		newsCache.put(key, value);
	}
	
	public Object get(String key) {

		try {

			return newsCache.get(key);

		} catch (Exception e) {

			return null;

		}

	}
	
	/**
	 * 
	 * 根据有效期获取缓存数据
	 * 
	 * @param key
	 * 
	 * @param period 有效期
	 * 
	 * @return
	 */
	public Object get(String key,int period) {

		try {

			return newsCache.get(key,period);

		} catch (Exception e) {

			return null;

		}

	}

	public void removeAll() {

		newsCache.removeAll();

	}

}
