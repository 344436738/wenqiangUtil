package com.leimingtech.core.annotation;

import java.lang.annotation.*;

/**
 * 
 * @author  
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Documented
public @interface Ehcache {
	// 缓存名称,默认为空
	String cacheName() default "";

	// 增加缓存还是删除缓存，默认为增加缓存
	boolean addOrdel() default true;
	
	//临时缓存还是永久缓存，默认为永久缓存
	boolean eternal() default true;
}
