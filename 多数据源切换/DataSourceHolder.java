package com.sinotrans.visualizationplatform.support.multidatasource;

/**
 * 
 * @Description
 * 		DynamicDataSourceHolder用于持有当前线程中使用的数据源标识
 *
 * com.sinotrans.visualizationplatform.support.multidatasource.DataSourceHolder.java
 *
 * @author chenj
 *
 * @date 2018年5月22日 下午9:49:40
 *
 * @version 1.0
 *
 */
public class DataSourceHolder {

	public DataSourceHolder() {
				
	}
	
	private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    public static void setDataSources(String dataSource) {
        dataSources.set(dataSource);
    }

    public static String getDataSources() {
        return dataSources.get();
    }


}
