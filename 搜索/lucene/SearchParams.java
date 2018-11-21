package com.leimingtech.lucene.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.util.Version;

import java.util.ArrayList;

public class SearchParams {
	private BooleanQuery booleanQuery = new BooleanQuery();
	private ArrayList sortFieldList = new ArrayList();
	private int pageSize = 10;
	private int pageIndex = 0;
	private Analyzer analyzer;

	public SearchParams(Analyzer analyzer) {
		this.analyzer =analyzer;
	}

	/**
	 * 添加一个全文检索条件，在指定字段中执行全文检索
	 * @param field
	 * @param query
	 * @param isMust
	 */
	public void addTextField(String field, String query, boolean isMust) {
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, field, analyzer);
		try {
			booleanQuery.add(parser.parse(query), isMust ? Occur.MUST : Occur.SHOULD);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加一个查询条件，类假于SQL中的field like 'query%'
	 * @param field
	 * @param query
	 * @param isMust
	 */
	public void addLeftLikeField(String field, String query, boolean isMust) {
		Term term = new Term(field, query);
		PrefixQuery q = new PrefixQuery(term);
		booleanQuery.add(q, isMust ? Occur.MUST : Occur.SHOULD);
	}
	
	/**
	 * 添加一个查询条件，类假于SQL中的field like '%query%'
	 * @param field
	 * @param query
	 * @param isMust
	 */
	public void addLikeField(String field, String query, boolean isMust) {
		Term term = new Term(field, "*"+query+"*");
		WildcardQuery q = new WildcardQuery(term);
		booleanQuery.add(q, isMust ? Occur.MUST : Occur.SHOULD);
	}
	
	/**
	 * 添加一个自定义的查询条件
	 * @param q
	 * @param isMust
	 */
	public void addQuery(Query q, Boolean isMust) {
		booleanQuery.add(q, isMust ? Occur.MUST : Occur.SHOULD);
	}
	
	/**
	 * 添加一个查询条件，要求字段值完全等于指定值
	 * @param field
	 * @param query
	 * @param isMust
	 */
	public void addEqualField(String field, String query, boolean isMust) {
		Term term = new Term(field, query);
		TermQuery q = new TermQuery(term);
		booleanQuery.add(q, isMust ? Occur.MUST : Occur.SHOULD);
	}
	
	public void setSortField(String field, boolean desc){
		sortFieldList.add(new SortField(field, (Type) SortField.STRING_FIRST, desc));
	}
	
	public BooleanQuery getBooleanQuery() {
		return booleanQuery;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		if(pageIndex < 0){
			pageIndex = 0;
		}
		this.pageIndex = pageIndex;
	}
	
}
