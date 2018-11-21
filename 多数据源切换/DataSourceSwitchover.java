package com.sinotrans.visualizationplatform.support.multidatasource;

import org.aspectj.lang.JoinPoint;

/**
 * 
 * @Description
 * 		织入切面，自动切换数据源
 *
 * com.sinotrans.visualizationplatform.support.multidatasource.DataSourceSwitchover.java
 *
 * @author chenj
 *
 * @date 2018年5月22日 下午10:05:55
 *
 * @version 1.0
 *
 */
public class DataSourceSwitchover {

	public void before(JoinPoint point) {

		// 获取目标对象的类类型
		Class<?> aClass = point.getTarget().getClass();
		String c = aClass.getName();
		String[] ss = c.split("\\.");
		// 获取包名用于区分不同数据源
		String packageName = ss[4];

		if ("visual".equals(packageName)) {
			DataSourceHolder.setDataSources(DataSourceEnum.DSOURCE1.getKey());
		} else if("stg".equals(packageName)) {
			DataSourceHolder.setDataSources(DataSourceEnum.DSOURCE2.getKey());
		}
	}

	/**
	 * 执行后将数据源置为空
	 */
	public void after() {
		DataSourceHolder.setDataSources(null);
	}

}
