package com.sinotrans.visualizationplatform.support.multidatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @Description
 * 		DynamicDataSource的类，继承AbstractRoutingDataSource并重写determineCurrentLookupKey方法
 *
 * com.sinotrans.visualizationplatform.support.multidatasource.DynamicDataSource.java
 *
 * @author chenj
 *
 * @date 2018年5月22日 下午9:50:39
 *
 * @version 1.0
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

	@Override
	protected Object determineCurrentLookupKey() {
		
		return DataSourceHolder.getDataSources();
	}
	
}
