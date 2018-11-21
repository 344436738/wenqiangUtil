package com.leimingtech.cms.service.impl;

import com.leimingtech.base.entity.site.SiteEntity;
import com.leimingtech.base.entity.temp.*;
import com.leimingtech.cms.entity.contents.ContentClassify;
import com.leimingtech.cms.service.LuceneServiceI;
import com.leimingtech.core.service.ContentsServiceI;
import com.leimingtech.core.service.SiteServiceI;
import com.leimingtech.core.service.impl.CommonServiceImpl;
import com.leimingtech.core.util.FileUtil;
import com.leimingtech.core.util.HtmlParser;
import com.leimingtech.core.util.StringUtils;
import com.leimingtech.core.util.SystemPath;
import com.leimingtech.lucene.wltea.analyzer.lucene.IKAnalyzer;
import com.leimingtech.lucene.wltea.analyzer.lucene.SearchParams;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author  :linjm linjianmao@gmail.com
 * @version :2014-4-15下午03:05:00
 * 索引生成器
 **/
@Service("luceneServiceImpl")
@Transactional
public class LuceneServiceImpl extends CommonServiceImpl implements LuceneServiceI {
	
	private Analyzer analyzer = new IKAnalyzer();//分词器
	
	@Autowired
	private ContentsServiceI contentsService;
	@Autowired
	private SiteServiceI siteService;
	
	private static String indexPath=SystemPath.getSysPath()+"/WEB-INF/luceneIndex/";
	
	/**
	 * 初始化索引配置
	 * @param sitePath
	 * @return
	 */
	private IndexWriter getIndexWrite(String sitePath){
		FSDirectory directory = null;
		IndexWriterConfig indexWriterConfig = null;
		IndexWriter indexWriter = null;
		try {
			File file = new File(indexPath + sitePath);
			if(!file.exists()){
				file.mkdirs();
			}else{
				// 如果文件存在 先删除再创建新的索引
				FileUtil.deleteDir(file);
				file.mkdirs();
			}
			directory = FSDirectory.open(file);//打开文件
			// 初始化索引配置  版本 ,分词器
			indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexWriter;
	}
	
	/**
	 * 获取要索引的Document
	 * @param contents
	 * @param contentsMap
	 * @return
	 */
	private Document addDocument(ContentsEntity contents, Map<String, Object> contentsMap){
		Document doc = new Document();
		if(contents!=null){
			String contentid = contents.getId();
			doc.add(new StringField("indexID", contentid + "", Store.YES));                                                             // 不分词存储
			doc.add(new StringField("url", contents.getUrl()==null ? "" :contents.getUrl(), Store.YES));                                // 不分词存储
			//wap 端添加  -- 
			 
			doc.add(new StringField("wapurl", contents.getWapurl()  == null ? "" : contents.getWapurl().toString(), Store.YES));            // 不分词存储
			
			doc.add(new StringField("catid", contents.getCatid() == null ? "" : contents.getCatid().toString(), Store.YES));            // 不分词存储
			doc.add(new StringField("classify", contents.getClassify() == null ? "" : contents.getClassify().toString(), Store.YES));   // 不分词存储
			doc.add(new StringField("publishdate", contents.getPublished()==null ? "" :contents.getPublished().toString(), Store.YES)); // 不分词存储
			doc.add(new StringField("pathids", contents.getPathids(), Store.YES));                                                      // 不分词存储
			doc.add(new TextField("title", contents.getTitle()==null ? "" :contents.getTitle(), Store.YES));                            // 一元分词存储
			doc.add(new TextField("description", contents.getDigest()==null ? "" :contents.getDigest(), Store.YES));                    // 一元分词存储
			
			String classify = contents.getClassify();
			if(ContentClassify.CONTENT_ARTICLE.equals(classify)){
				ArticleEntity article = (ArticleEntity)contentsMap.get(contentid);
				if (article != null&&StringUtils.isNotEmpty(article.getContent())) {
					doc.add(new TextField("content", article.getContent(), Store.YES));                                                 // 一元分词存储
				}
			} else if(ContentClassify.CONTENT_VIDEO.equals(classify)) {
				ContentVideoEntity video = (ContentVideoEntity)contentsMap.get(contentid);
				if(null != video){
					String remark = video.getVideoremark() == null ? "" : video.getVideoremark();
					doc.add(new TextField("content", remark, Store.YES));                                                               // 一元分词存储
				}
			} else if(ContentClassify.CONTENT_PICTUREGROUP.equals(classify)){
				PictureAloneEntity picture = (PictureAloneEntity)contentsMap.get(contentid);
				if(null != picture){
					String msg = picture.getPictureMessage() == null ? "" : picture.getPictureMessage();
					doc.add(new TextField("content", msg, Store.YES));                                                                  // 一元分词存储
				}
			}
		}
		return doc;
	}
	
	/**
	 * 生成LUCENE索引
	 * @param site
	 */
	@SuppressWarnings("unchecked")
	public void creatIndex(SiteEntity site) {
		IndexWriter indexWriter = getIndexWrite(site.getSitePath());
		try {
			indexWriter.deleteAll();   // 删除创建的索引，重新生成
			// 获取内容
			Map<String, Object> m = contentsService.getAllOpenContent(site);
			if (m != null) {
				List<ContentsEntity> list = (List<ContentsEntity>) m.get("contentList");
				Map<String, Object> contentdataMap = (Map<String, Object>) m.get("contentDataMap");
				// 添加索引
				for (int i = 0; i < list.size(); i++) {
					Document doc = new Document();
					ContentsEntity contents = list.get(i);
					if (contents != null) {
						doc = addDocument(contents, contentdataMap);
						indexWriter.addDocument(doc); // 添加需要建立索引的Field
					}
				}
			}
			//indexWriter.optimize();//合并索引操作
			indexWriter.commit(); // 将改动持久化到本次索引中
			indexWriter.close();  // 关闭
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter!=null){
				try {
					indexWriter.close();//关闭
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	/**
	 * 查询索引，分页
	 * @param site
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> searchIndex(SiteEntity site, Map params) {
		
		String keyWord = params.get("keyword") + "";
		String catid = params.get("catid") + "";
		String classify = params.get("classify") + "";
		String istype = params.get("istype") + "";
		String pageno = params.get("pageNO") + "";
		int pageNO = Integer.parseInt(pageno);
		String pagesize = params.get("pageSize") + "";
		int pageSize = Integer.parseInt(pagesize);
		
		List<NewsIndexEntity> list = new ArrayList<NewsIndexEntity>();
		String indexPath = SystemPath.getSysPath()+"/WEB-INF/luceneIndex/";
		IndexReader reader = null;
		IndexSearcher searcher = null;
		 
		Integer totalSize = 0;
		try {
			Directory directory = FSDirectory.open(new File(indexPath+site.getSitePath())); // 打开索引库
			reader = DirectoryReader.open(directory);                                       // 流读取
			searcher = new IndexSearcher(reader);                                           // 搜索
			
			SearchParams searchParams = new SearchParams(analyzer);                                 //查询条件
			searchParams = getSearchParams(keyWord, istype,catid, classify, pageNO, pageSize);
			
			// 高亮
			//SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<B class=\"highlight\">", "</B>");//高亮标签格式
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color=OrangeRed   >", "</font>");//高亮标签格式  <font color=red>新闻</font>
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(searchParams.getBooleanQuery()));
			
			// 这个100是指定关键字字符串的context的长度，你可以自己设定，因为不可能返回整篇正文内容
			int abstractLength = 100;
			highlighter.setTextFragmenter(new SimpleFragmenter(abstractLength));
			
			int totalget = searchParams.getPageIndex() * searchParams.getPageSize();
			
			TopDocs topDocs = null; // 返回Ｎ条记录
			topDocs = searcher.search(searchParams.getBooleanQuery(), totalget + searchParams.getPageSize());
			totalSize = topDocs.totalHits;//总命中数
			
			for (int j = (pageNO - 1) * pageSize; j < topDocs.scoreDocs.length; j++) {
				ScoreDoc scoreDoc = topDocs.scoreDocs[j];
				NewsIndexEntity newsIndex = getNewIndex(searcher, scoreDoc, highlighter, abstractLength);
				list.add(newsIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(reader!=null){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("newslist", list);
		map.put("totalSize", totalSize);
		return map;
	}
	
	/**
	 * 查询结果列表
	 * @param searcher
	 * @param scoreDoc
	 * @param highlighter
	 * @param abstractLength
	 * @return
	 */
	@SuppressWarnings("resource")
	private NewsIndexEntity getNewIndex(IndexSearcher searcher, ScoreDoc scoreDoc, Highlighter highlighter, int abstractLength){
		NewsIndexEntity newsIndex = new NewsIndexEntity();
		try {
			String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
			Pattern p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
			Document doc = searcher.doc(scoreDoc.doc);
			String doctitle = doc.get("title");
			String docdescription = doc.get("description");
			String docpublishdate = doc.get("publishdate");
			String docurl = doc.get("url");
			String wapurl =doc.get("wapurl");
			//System.out.println("LuceneServiceImpl.getNewIndex()"+"---------"+wapurl+"------------");
			String content=doc.get("content");
			TokenStream tokenStream = new IKAnalyzer().tokenStream( "title", new StringReader(doctitle));
			String tmp = highlighter.getBestFragment(tokenStream, doctitle);
			if (StringUtils.isNotEmpty(tmp)) {
    
			}

			if (StringUtils.isNotEmpty(doctitle)) {
				tokenStream = new IKAnalyzer().tokenStream("title", new StringReader(doctitle));
				tmp = highlighter.getBestFragment(tokenStream, doctitle);
				if (StringUtils.isNotEmpty(tmp)) {
					doctitle = tmp.trim();
				} else {
					Matcher m_html = p_html.matcher(doctitle);
					doctitle = m_html.replaceAll(""); //过滤html标签 
					if (doctitle.length() > abstractLength) {
						doctitle = docdescription.substring(0, abstractLength);
					}
				}
			}
			if (StringUtils.isNotEmpty(docdescription)) {
				tokenStream = new IKAnalyzer().tokenStream("description", new StringReader(docdescription));
				tmp = highlighter.getBestFragment(tokenStream, docdescription);
				if (StringUtils.isNotEmpty(tmp)) {
					docdescription = tmp.trim();
				} else {
					Matcher m_html = p_html.matcher(docdescription);
					docdescription = m_html.replaceAll(""); //过滤html标签 
					if (docdescription.length() > abstractLength) {
						docdescription = docdescription.substring(0, abstractLength);
					}
				}
			}
			
			if (StringUtils.isNotEmpty(content)) {
				content=HtmlParser.htmlToText(content);
				tokenStream = new IKAnalyzer().tokenStream("content", new StringReader(content));
				tmp = highlighter.getBestFragment(tokenStream, content);
				if (StringUtils.isNotEmpty(tmp)) {
					content = tmp.trim();
				} else {
					Matcher m_html = p_html.matcher(content);
					content = m_html.replaceAll(""); //过滤html标签 
					if (content.length() > abstractLength) {
						content = content.substring(0, abstractLength);
					}
				}
			}

			newsIndex.setTitle(doctitle);
			newsIndex.setUrl(docurl);
			newsIndex.setWapurl (wapurl);
			newsIndex.setContent(content);
			newsIndex.setDescription(docdescription);
			newsIndex.setPublishdate(docpublishdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsIndex;
	}
	
	
	/**
	 * 获取查询条件
	 * @param keyWord
	 * @param catid
	 * @param classify
	 * @param pageNO
	 * @param pageSize
	 * @return
	 */
	private SearchParams getSearchParams(String keyWord, String istype,String catid, String classify, int pageNO, int pageSize){
		SearchParams searchParams = new SearchParams(analyzer);
		
		if(StringUtils.isNotEmpty(keyWord)){
			if(StringUtils.isNotEmpty(istype) && istype.equals("1")){
				searchParams.addTextField("title", keyWord, true);
			}else if(StringUtils.isNotEmpty(istype) && istype.equals("2")){
				searchParams.addTextField("content", keyWord, true);
			}else{
			searchParams.addTextField("title", keyWord, false);
			searchParams.addTextField("content", keyWord, false);
			searchParams.addTextField("description", keyWord, false);
			}
		}
		
		if(StringUtils.isNotEmpty(catid) && !"0".equals(catid)){
			searchParams.addLikeField("pathids", catid, true);
		}
		
		if(StringUtils.isNotEmpty(classify) && !"0".equals(classify)){
			searchParams.addEqualField("classify", classify, true);
		}
		
		
		if(searchParams.getBooleanQuery().clauses().size() == 0){
			MatchAllDocsQuery maq = new MatchAllDocsQuery();
			searchParams.addQuery(maq, false);
		}
		
		String pageno = String.valueOf(pageNO);
		if(StringUtils.isNotEmpty(pageno)){
			searchParams.setPageIndex(pageNO - 1);
		}
		
		String pagesize = String.valueOf(pageSize);
		if(StringUtils.isNotEmpty(pagesize)){
			searchParams.setPageSize(pageSize);
		}
		return searchParams;
	}

	/**
	 * 更新索引  单个
	 */
	public void updateIndex(SiteEntity site, ContentsEntity contents) {
		IndexWriter indexWriter = getIndexWrite(site.getSitePath());
		Map<String, Object> contentsMap = contentsService.getIndexContent(contents.getId());
		Document doc = addDocument(contents, contentsMap);
		
		try {
			indexWriter.addDocument(doc);
			indexWriter.commit(); // 将改动持久化到本次索引中
			indexWriter.close();  // 关闭
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter!=null){
				try {
					indexWriter.close();//关闭
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	/**
	 * 更新索引 多个
	 */
	public void updateIndex(SiteEntity site,List<ContentsEntity> list) {
		try {
			for (ContentsEntity contentsEntity : list) {
				updateIndex(site, contentsEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除索引 多个
	 * @param list
	 */
	public void deleteIndex(SiteEntity site,List<ContentsEntity> list) {
		try {
			for (ContentsEntity contentsEntity : list) {
				deleteIndex(site, contentsEntity);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除索引 单个
	 * @param contents
	 */
	public void deleteIndex(SiteEntity site, ContentsEntity contents) {
		IndexWriter indexWriter = getIndexWrite(site.getSitePath());
		try {
			//删除对应的索引
			Term term = new Term("indexID", contents.getId().toString());
			indexWriter.deleteDocuments(term);//删除已经建过的索引然后重新建
			indexWriter.close();//关闭
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter!=null){
				try {
					indexWriter.close();//关闭
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Map<String, Object> searchContent(SiteEntity site, Map params) {
		String keyWord = params.get("keyword") + "";

		String pageno = params.get("pageNo") + "";
		int pageNO = Integer.parseInt(pageno);
		String pagesize = params.get("pageSize") + "";
		int pageSize = Integer.parseInt(pagesize);

		List<NewsIndexEntity> list = new ArrayList<NewsIndexEntity>();
		String indexPath = SystemPath.getSysPath()+"/WEB-INF/luceneIndex/";
		IndexReader reader = null;
		IndexSearcher searcher = null;

		Integer totalSize = 0;
		try {
			Directory directory = FSDirectory.open(new File(indexPath+site.getSitePath())); // 打开索引库
			reader = DirectoryReader.open(directory);                                       // 流读取
			searcher = new IndexSearcher(reader);                                           // 搜索

			SearchParams searchParams = new SearchParams(analyzer);                                 //查询条件
			searchParams = getSearchParams(keyWord, pageNO, pageSize);

			// 高亮

			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter();//高亮标签格式  <font color=red>新闻</font>
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(searchParams.getBooleanQuery()));

			// 这个100是指定关键字字符串的context的长度，你可以自己设定，因为不可能返回整篇正文内容
			int abstractLength = 100;
			highlighter.setTextFragmenter(new SimpleFragmenter(abstractLength));

			int totalget = searchParams.getPageIndex() * searchParams.getPageSize();

			TopDocs topDocs = null; // 返回Ｎ条记录
			topDocs = searcher.search(searchParams.getBooleanQuery(), totalget + searchParams.getPageSize());
			totalSize = topDocs.totalHits;//总命中数

			for (int j = (pageNO - 1) * pageSize; j < topDocs.scoreDocs.length; j++) {
				ScoreDoc scoreDoc = topDocs.scoreDocs[j];
				NewsIndexEntity newsIndex = getNewIndex(searcher, scoreDoc, highlighter, abstractLength);
				list.add(newsIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(reader!=null){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("newslist", list);
		map.put("totalSize", totalSize);
		return map;
	}

	/**
	 * 根据标签查询
	 */
	private SearchParams getSearchParams(String keyWord, int pageNO, int pageSize){
		SearchParams searchParams = new SearchParams(analyzer);

		if(StringUtils.isNotEmpty(keyWord)){
			String tagArry[]=keyWord.split(",");
			for(int i= 0;i<tagArry.length;i++){
				searchParams.addLikeField("title",tagArry[i],false);
			}
		}
		if(searchParams.getBooleanQuery().clauses().size() == 0){
			MatchAllDocsQuery maq = new MatchAllDocsQuery();
			searchParams.addQuery(maq, false);
		}

		String pageno = String.valueOf(pageNO);
		if(StringUtils.isNotEmpty(pageno)){
			searchParams.setPageIndex(pageNO - 1);
		}

		String pagesize = String.valueOf(pageSize);
		if(StringUtils.isNotEmpty(pagesize)){
			searchParams.setPageSize(pageSize);
		}
		return searchParams;
	}



}
