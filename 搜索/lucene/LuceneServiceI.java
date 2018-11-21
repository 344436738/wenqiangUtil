package com.leimingtech.cms.service;

import com.leimingtech.base.entity.site.SiteEntity;
import com.leimingtech.base.entity.temp.ContentsEntity;
import com.leimingtech.core.service.CommonService;

import java.util.List;
import java.util.Map;

/**
 * @author  :linjm linjianmao@gmail.com
 * @version :2014-4-15下午03:03:22
 *  description :
 **/
public interface LuceneServiceI extends CommonService {

	/**
	 * 生成LUCENE索引
	 * @param site
	 */
	public void creatIndex(SiteEntity site);
	
	/**
	 * 删除索引 多个
	 * @param list
	 */
	public void deleteIndex(SiteEntity site,List<ContentsEntity> list);
	
	/**
	 * 删除索引 单个
	 * @param contents
	 */
	public void deleteIndex(SiteEntity site, ContentsEntity contents);
	
	/**
	 * 更新索引 多个
	 */
	public void updateIndex(SiteEntity site,List<ContentsEntity> list);
	
	/**
	 * 更新索引  单个
	 * @param contentid
	 */
	public void updateIndex(SiteEntity site, ContentsEntity contents);
	
	/**
	 * 查询索引，有分页
	 * @param site
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> searchIndex(SiteEntity site, Map map);

	Map<String, Object> searchContent(SiteEntity site, Map params);
	
}
