package com.sinotrans.visualizationplatform.support.multidatasource;

/**
 * 
 * @Description
 * 		多数据源枚举
 *
 * com.sinotrans.visualizationplatform.support.multidatasource.DataSourceEnum.java
 *
 * @author chenj
 *
 * @date 2018年5月22日 下午9:47:23
 *
 * @version 1.0
 *
 */
public enum DataSourceEnum {
	
	DSOURCE1("ds1"), DSOURCE2("ds2");
    
    private String key;

    DataSourceEnum(String key) { this.key = key; }

    public String getKey() { return key; }
    
    public void setKey(String key) {  this.key = key; }

}
