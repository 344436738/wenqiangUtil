package com.leimingtech.cms.controller.contents;

import com.leimingtech.base.entity.site.SiteEntity;
import com.leimingtech.base.entity.temp.*;
import com.leimingtech.cms.entity.contents.ContentClassify;
import com.leimingtech.cms.entity.video.VideoalburmEntity;
import com.leimingtech.cms.service.videoalburm.VideoalburmServiceI;
import com.leimingtech.common.Globals;
import com.leimingtech.common.hibernate.SortDirection;
import com.leimingtech.common.hibernate.qbc.CriteriaQuery;
import com.leimingtech.common.hibernate.qbc.PageList;
import com.leimingtech.common.hqlsearch.HqlGenerateUtil;
import com.leimingtech.core.common.CmsConstants;
import com.leimingtech.core.common.ContentMobileClassify;
import com.leimingtech.core.common.ContentStatus;
import com.leimingtech.core.service.*;
import com.leimingtech.core.service.depart.DepartServiceI;
import com.leimingtech.core.service.modelext.ModelExtServiceI;
import com.leimingtech.core.util.*;
import com.leimingtech.core.util.constant.PersonnelStatusConstant;
import com.leimingtech.member.entity.member.vo.MemberSimpleVOEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: Controller
 * @Description: 内容s
 * @author zhangdaihao modify by linjm 20140402
 * @date 2014-04-21 15:05:15
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/contentsController")
public class ContentsController extends ContentsbaseController {

	@Autowired
	private ContentsServiceI contentsService;
	@Autowired
	private ArticleServiceI articleService;
	@Autowired
	private ModelManageServiceI modelManageService;
	@Autowired
	private PictureGroupServiceI pictureGroupService;
	@Autowired
	private ContentLinkServiceI contentLinkService;
	@Autowired
	private ContentVideoServiceI contentVideoService;
	@Autowired
	private VoteOptionServiceI voteOptionService;
	@Autowired
	private ActivityServiceI activityService;
	@Autowired
	private VoteServiceI voteService;
	@Autowired
	private SurveyServiceI surveyService;
	@Autowired
	private SpecialServiceI specialService;
	@Autowired
	private InterviewServiceI interviewService;
	@Autowired
	private ModelItemServiceI modelItemService;
	@Autowired
	private ModelExtServiceI modelExtService;
	@Autowired
	private ContentCatServiceI contentCatService;// 栏目管理接口
	@Autowired
	private VideoalburmServiceI videoalburmService;
	/**部门管理接口*/
	@Autowired
	private DepartServiceI departService;
	/**会员管理接口*/
	@Autowired
	private MemberServiceI memberService;
	/** 用户管理接口 */
	@Autowired
	private UserService userService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "contentCat")
	public ModelAndView function(HttpServletRequest request) {
		CriteriaQuery cq = new CriteriaQuery(ContentCatEntity.class);
		cq.eq("levels", 0);
		cq.add();
		List<ContentCatEntity> list = contentsService.getListByCriteriaQuery(
				cq, false);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("list", list);
		return new ModelAndView("cms/contents/contentEditList", m);
	}

	/**
	 * 加载下级
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "load")
	@ResponseBody
	public JSONArray loadMenu(HttpServletRequest request) {
		String id = request.getParameter("id");
		CriteriaQuery cq = new CriteriaQuery(ContentCatEntity.class);
		cq.eq("contentCat.id", String.valueOf(id));
		cq.add();
		List<ContentCatEntity> list = contentsService.getListByCriteriaQuery(
				cq, false);
		JSONArray jsonArray = new JSONArray();
		for (ContentCatEntity contentCat : list) {
			JSONObject json = new JSONObject();
			if (contentCat.getContentCats() != null
					&& contentCat.getContentCats().size() > 0) {
				json.put("text", contentCat.getName());
				json.put("value", contentCat.getId());
				json.put("leaf", false);
				json.put("expanded", false);
				json.put("cls", "folder");
				json.put("id", contentCat.getId());
				json.put("href",
						"contentsController.do?load&id=" + contentCat.getId());
				json.put("data-role", "branch");
				json.put("children", new JSONArray());
			} else {
				json.put("text", contentCat.getName());
				json.put("value", contentCat.getId());
				json.put("leaf", true);
				json.put("href", "javascript:void(0);");
				json.put("data-role", "leaf");
				json.put("id", contentCat.getId());
				json.put("checked", false);
			}
			jsonArray.add(json);
		}
		return jsonArray;
	}

	/**
	 * 加载下级地区
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "loads")
	@ResponseBody
	public String loadMenus(HttpServletRequest request) {
		String id = request.getParameter("id");
		CriteriaQuery cq = new CriteriaQuery(ContentCatEntity.class);
		cq.eq("contentCat.id", String.valueOf(id));
		cq.add();
		List<ContentCatEntity> list = contentsService.getListByCriteriaQuery(
				cq, false);
		JSONArray jsonArray = new JSONArray();
		for (ContentCatEntity contentCat : list) {
			JSONObject json = new JSONObject();
			if (contentCat.getContentCats() != null
					&& contentCat.getContentCats().size() > 0) {
				json.put("text", contentCat.getName());
				json.put("value", contentCat.getId());
				json.put("leaf", false);
				json.put("expanded", false);
				json.put("cls", "folder");
				json.put("id", contentCat.getId());
				json.put("href",
						"contentsController.do?load&id=" + contentCat.getId());
				json.put("data-role", "branch");
				json.put("children", new JSONArray());
			} else {
				json.put("text", contentCat.getName());
				json.put("value", contentCat.getId());
				json.put("leaf", true);
				json.put("href", "javascript:void(0);");
				json.put("data-role", "leaf");
				json.put("id", contentCat.getId());
				json.put("checked", false);
			}
			jsonArray.add(json);
		}
		return jsonArray.toString();
	}

	/**
	 * 加载下级
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "Table")
	public ModelAndView menuTable(HttpServletRequest request) {
		String id = request.getParameter("id");
		ContentCatEntity parent = null;
		if (id != null && "".equals(id)) {
			// 点击顶级菜单
			parent = new ContentCatEntity();
			parent.setId(null);
			parent.setName("顶级菜单");
		} else {
			parent = contentsService.getEntity(ContentCatEntity.class,
					String.valueOf(id));
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("parentFunction", parent);
		m.put("list", this.getContentsList(parent));// parent.getContentCats()
		m.put("selectId", id);
		// 三级级联菜单第一级
		CriteriaQuery cq = new CriteriaQuery(ContentCatEntity.class);
		cq.eq("levels", 1);
		cq.add();
		List<ContentCatEntity> list = contentsService.getListByCriteriaQuery(
				cq, false);
		m.put("list_test", list);
		return new ModelAndView("cms/contentcat/contentCat", m);

	}

	/**
	 * 递归获取栏目下的所有文章 包括自己和子节点
	 *
	 * @param contentCat
	 * @return
	 */
	public List<ContentsEntity> getContentsList(ContentCatEntity contentCat) {
		List<ContentsEntity> contentsAllList = new ArrayList<ContentsEntity>();

		List<ContentsEntity> contentsList = contentsService.findByProperty(
				ContentsEntity.class, "catid", contentCat.getId());
		if (contentCat.getContentCats().size() == 0) {
			return contentsList;
		}
		contentsAllList.addAll(contentsList);
		for (int i = 0; i < contentCat.getContentCats().size(); i++) {
			List<ContentsEntity> childContentsList = contentsService
					.findByProperty(ContentsEntity.class, "channelId",
							contentCat.getContentCat().getId());
			contentsAllList.addAll(childContentsList);
			getContentsList(contentCat.getContentCat());
		}
		return contentsAllList;
	}

	/**
	 * 内容列表页ftl
	 *
	 * @param contents
	 * @param modelid
	 * @param constants
	 * @param publishedStart
	 * @param publishedEnd
	 * @param request
	 */
	@RequestMapping(params = "table")
	public ModelAndView table(ContentsEntity contents, String modelid,
							  String constants, String publishedStart, String publishedEnd,
							  HttpServletRequest request) {
		String contentCatId = request.getParameter("contentCatId");
		String contribute = request.getParameter("contribute");
		String title = request.getParameter("title");
		// 是否显示全部分类内容(all)
		String tab = request.getParameter("tab");
		if (StringUtils.isEmpty(contentCatId)) {
			contentCatId = "-1";
		}

		// 获取内容列表
		int pageSize = StringUtils.isEmpty(request.getParameter("pageSize")) ? 15
				: Integer.valueOf(request.getParameter("pageSize"));
		int pageNo = StringUtils.isEmpty(request.getParameter("pageNo")) ? 1
				: Integer.valueOf(request.getParameter("pageNo"));

		CriteriaQuery cq = new CriteriaQuery(ContentsEntity.class, pageSize,
				pageNo, "", "");
		// 查询条件组装器
		if (StringUtils.isNotEmpty(contentCatId) && !"-1".equals(contentCatId)) {
			cq.eq("catid", contentCatId);
		}
		// tab=all/alone,分别表示显示全部/当前栏目下的所有内容
		if (tab.equals("all")) {
			if (!contentCatId.equals("-1")) {
				cq.eq("catid", contentCatId);
			}else{
				String[] roleList=contentsService.catRoleList();
				if(roleList!=null && roleList.length>0){
					cq.in("catid", roleList);
				}else{
					cq.eq("catid", contentCatId);
				}

			}
		}
		if(StringUtils.isNotEmpty(title)){
			cq.like("title","%"+title+"%");
		}

		// 查询模型分类
		if (StringUtils.isNotEmpty(modelid)) {
			cq.eq("modelid", modelid);
		}
		// 工作流状态
		if (StringUtils.isNotEmpty(constants)) {
			cq.eq("constants", constants);
		} else {
			cq.notEq("constants", ContentStatus.CONTENT_RECYCLE);
		}
		// 发布时间
		if (StringUtils.isNotEmpty(publishedStart)
				&& StringUtils.isNotEmpty(publishedEnd)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				cq.gt("published", sdf.parse(publishedStart + " 00:00:00"));
				cq.lt("published", sdf.parse(publishedEnd + " 23:59:59"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 是否为投稿
		if (StringUtils.isNotEmpty(contribute)) {
			cq.eq("iscontribute", contribute);
		}

		cq.eq("siteid", UserUtils.getSiteId());

		// 排序条件
		cq.addOrder("isTop", SortDirection.desc);
		cq.addOrder("isHeadline", SortDirection.desc);
		cq.addOrder("constants", SortDirection.asc);
		cq.addOrder("published", SortDirection.desc);
		cq.add();

		PageList pageList = this.contentsService.getPageList(cq, true);
		List<ContentsEntity> testList = pageList.getResultList();
		// 将templateIndex封装为jsonObject
		ContentCatEntity contentCat = contentsService.get(
				ContentCatEntity.class, String.valueOf(contentCatId));
		// 当前用户所拥有的权限
		List<TSRole> roleList = PlatFormUtil.getRoleUser();

		if (roleList != null && roleList.size() > 0) {

			String[] roles = new String[roleList.size()];
			for (int i = 0; i < roleList.size(); i++) {
				roles[i] = roleList.get(i).getId();
			}

			for (int i = 0; i < testList.size(); i++) {
				ContentsEntity con = testList.get(i);

				if (ArrayUtils.contains(roles, con.getWorkflowRoleid())) {
					con.setIsOperate(true);
				} else {
					con.setIsOperate(false);
				}
			}
		}

		JSONObject jsonobj = new JSONObject();
		if (contentCat != null
				&& StringUtils.isNotEmpty(contentCat.getTemplateIndex())) {
			jsonobj = jsonobj.fromObject(contentCat.getTemplateIndex());
		}
		int pageCount = (int) Math.ceil((double) pageList.getCount()
				/ (double) pageSize);
		if (pageCount <= 0) {
			pageCount = 1;
		}
		// 获取模型modelsList
		String sql = "select model.id,model.name,model.template_show from cms_model model where model.disabled!='1'";
		List<Map<String, Object>> modelsList = contentsService.findForJdbc(sql);

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("pageList", testList);
		m.put("pageNo", pageList.getCurPageNO());
		m.put("pageSize", pageSize);
		m.put("pageCount", pageCount);
		m.put("contentCat", contentCat);
		m.put("jsonobj", jsonobj);
		m.put("modelsList", modelsList);
		m.put("tab", tab);
		m.put("actionUrl", "contentsController.do?table?tab=all&contentCatId="
				+ contentCatId);
		SiteEntity site = UserUtils.getSite();

		m.put("domain", site.getDomain());
		m.put("conpath", Globals.CONTEXTPATH);
		m.put("title", title);
		m.put("contribute", contribute);
		m.put("modelid", modelid);
		m.put("constants", constants);
		m.put("publishedStart", publishedStart);
		m.put("publishedEnd", publishedEnd);
		m.put("netDomain", PlatFormUtil.getDomail());
		m.put("maprole", userService.mapByRoleId());
		return new ModelAndView("cms/contents/contentsList", m);
	}

	/**
	 * 内容添加
	 *
	 * @return
	 */
	@RequestMapping(params = "addPageModel")
	public ModelAndView addPageModel(HttpServletRequest reqeust) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", new ContentsEntity());
		return new ModelAndView("cms/contents/contents", m);
	}

	/**
	 * 内容更新
	 *
	 * @return
	 */
	@RequestMapping(params = "updatePageModel")
	public ModelAndView updatePageModel(HttpServletRequest reqeust) {
		String id = reqeust.getParameter("id");
		ContentsEntity contents = contentsService.getEntity(
				ContentsEntity.class, String.valueOf(id));
		// 获取栏目
		ContentCatEntity contentCat = contentsService.getEntity(
				ContentCatEntity.class, contents.getCatid());
		// 获取扩展字段
		ModelManageEntity modelManage = null;
		if (StringUtils.isNotEmpty(contentCat.getJsonid())) {
			modelManage = modelManageService.getEntity(contentCat.getJsonid());
		}
		// 获取自定义字段/值
		List<ModelExtEntity> modelExtList=modelExtService.getContentAllExt(id);

		List<ModelItemEntity> modelItemList=null;
		if (modelManage!=null) {
			modelItemList=modelItemService.findByModelId(modelManage.getId());
		}

		Map<String, Object> extMap = new HashMap<String, Object>();
		for (int i = 0; i < modelExtList.size(); i++) {
			ModelExtEntity ext = modelExtList.get(i);
			extMap.put(ext.getModelItemId(), ext);
		}

		// 获取当前毫秒数
		String temporary = String.valueOf(System.currentTimeMillis());
		List<ContentTagEntity> contentTagList = contentsService
				.loadAll(ContentTagEntity.class); // 获取Tags标签内容
		List<SourceEntity> sourceEntityList = contentsService
				.loadAll(SourceEntity.class); // 获取来源内容
		// 从数据字典中获取内容标记
		List<TSTypegroup> typeGroupList = contentsService.findByProperty(
				TSTypegroup.class, "typegroupcode", "content_mark");
		List<TSType> typeList = new ArrayList<TSType>();
		if (typeGroupList.size() != 0) {
			typeList = contentsService.findByProperty(TSType.class,
					"TSTypegroup.id", typeGroupList.get(0).getId());
		}
		// 来源
		String sourceStr = "";
		if (sourceEntityList.size() != 0) {
			for (int i = 0; i < sourceEntityList.size(); i++) {
				sourceStr += sourceEntityList.get(i).getName() + ",";
			}
			sourceStr = sourceStr.substring(0, sourceStr.length() - 1);
		}
		// 当前人
		String markpeople = "";
		TSUser user = UserUtils.getUser();
		if (user != null) {
			markpeople = user.getUserName();
			if (StringUtils.isEmpty(markpeople)) {
				markpeople = user.getRealName();
			}
		}
		// 区分添加/编辑页面
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String,String> departMap = departService.getAllToMap();//获取所有部门信息
		if(StringUtils.isNotBlank(contents.getMemberIds())){
			List<MemberSimpleVOEntity> memberList = memberService.findIdUsernameListByIds(contents.getMemberIds().split(","));
			m.put("memberList",memberList);
		}
		m.put("departMap",departMap);
		m.put("sourceStr", sourceStr);
		m.put("contents", contents);
		m.put("contentsId", contents.getId());
		m.put("contentCat", contentCat);
		m.put("modelItemList", modelItemList);
		m.put("extMap", extMap);
		m.put("typeList", typeList);
		m.put("markpeople", markpeople);
		m.put("modelManage", modelManage);
		m.put("sessionId", reqeust.getSession().getId());
		// 发布文章分类(1文章2组图3链接4视频5活动6投票7访谈8调查9专题)
		m.put("contentTagList", contentTagList); // Tags标签内容
		m.put("sourceEntityLists", sourceEntityList); // 来源内容
		m.put("temporary", temporary); // 随机数
		m.put("memberinfo", UserUtils.getUser());
		if (contents.getClassify().equals(ContentClassify.CONTENT_ARTICLE)) {
			ArticleEntity article = articleService.getArticleById(contents
					.getId());
			m.put("page", article);
			m.put("classify", ContentClassify.CONTENT_ARTICLE);
			return new ModelAndView("cms/article/article_open", m);
			// 组图分类
		} else if (contents.getClassify().equals(
				ContentClassify.CONTENT_PICTUREGROUP)) {
			PictureGroupEntity pictureGroup = pictureGroupService
					.findUniqueByProperty(PictureGroupEntity.class,
							"contentid", contents.getId());
			m.put("page", pictureGroup == null ? new PictureGroupEntity()
					: pictureGroup);
			m.put("classify", ContentClassify.CONTENT_PICTUREGROUP);
			return new ModelAndView("cms/picturegroup/pictureGroup_open", m);
			// 链接分类
		} else if (contents.getClassify().equals(ContentClassify.CONTENT_LINK)) {
			List<ContentLinkEntity> contentLinkList = contentLinkService
					.findByProperty(ContentLinkEntity.class, "contentid",
							contents.getId());
			for (ContentLinkEntity contentLink : contentLinkList) {
				m.put("page", contentLink);
			}
			m.put("classify", ContentClassify.CONTENT_LINK);
			return new ModelAndView("cms/link/contentLink_open", m);
			// 视频分类
		} else if (contents.getClassify().equals(ContentClassify.CONTENT_VIDEO)) {
			ContentVideoEntity contentVideo = contentVideoService
					.findUniqueByProperty(ContentVideoEntity.class,
							"contentid", contents.getId());
			if (contentVideo == null) {
				contentVideo = new ContentVideoEntity();
			}
			m.put("page", contentVideo);
			VideoalburmEntity videoalburm = new VideoalburmEntity();
			if (StringUtils.isNotEmpty(contentVideo.getSpecial())) {
				videoalburm = videoalburmService.getVideoAlburmById(String
						.valueOf(contentVideo.getSpecial()));
			}
			m.put("videoalburm", videoalburm);
			m.put("classify", ContentClassify.CONTENT_VIDEO);
			return new ModelAndView("cms/video/contentVideo_open", m);
			// 活动分类
		} else if (contents.getClassify().equals(
				ContentClassify.CONTENT_ACTIVITY)) {
			List<ActivityEntity> activityList = activityService.findByProperty(
					ActivityEntity.class, "contentid", contents.getId());
			for (ActivityEntity activity : activityList) {
				m.put("page", activity);
			}
			return new ModelAndView("cms/activity/activity_open", m);
			// 投票分类
		} else if (contents.getClassify().equals(ContentClassify.CONTENT_VOTE)) {
			List<VoteEntity> voteList = voteService.findByProperty(
					VoteEntity.class, "contentid", contents.getId());
			for (VoteEntity vote : voteList) {
				m.put("page", vote);
				List<VoteOptionEntity> voteOptionList = voteOptionService
						.findByProperty(VoteOptionEntity.class, "voteid",
								String.valueOf(vote.getId()));
				m.put("voteOptionList", voteOptionList);
			}
			m.put("classify", ContentClassify.CONTENT_VOTE);
			return new ModelAndView("cms/vote/vote_open", m);
			// 访谈分类
		} else if (contents.getClassify().equals(
				ContentClassify.CONTENT_INTERVIEW)) {
			List<InterviewEntity> interviewList = interviewService
					.findByProperty(InterviewEntity.class, "contentid",
							contents.getId());
			for (InterviewEntity interview : interviewList) {
				m.put("page", interview);
			}
			return new ModelAndView("cms/interview/interview_open", m);
			// 调查分类
		} else if (contents.getClassify()
				.equals(ContentClassify.CONTENT_SURVEY)) {
			List<SurveyEntity> surveyList = surveyService.findByProperty(
					SurveyEntity.class, "contentid", contents.getId());
			for (SurveyEntity survey : surveyList) {
				m.put("page", survey);
			}
			m.put("classify", ContentClassify.CONTENT_SURVEY);
			return new ModelAndView("cms/survey/survey_open", m);
			// 专题分类
		} else if (contents.getClassify().equals(
				ContentClassify.CONTENT_SPECIAL)) {
			SpecialEntity special=specialService.findByContentId(contents.getId());

			m.put("special", special);
			return new ModelAndView("cms/special/special_open", m);
			// 其他类别
		} else {
			return new ModelAndView("cms/contents/contents_open", m);
		}
	}

	/**
	 * 内容保存
	 *
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public String save(ContentsEntity contents, ContentLinkEntity contentLink,
					   ArticleEntity article, PictureGroupEntity pictureGroup,
					   ContentVideoEntity contentVideo, VoteEntity vote,
					   SurveyEntity survey, ActivityEntity activity,
					   HttpServletRequest request) {

		JSONObject j = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		// 内容id
		String contentsId = request.getParameter("contentsId");

		String articleEsc=StringEscapeUtils.unescapeHtml4(article.getContent());
		article.setContent(articleEsc);
		map.put("contents", contents);
		map.put("contentLink", contentLink);
		map.put("article", article);
		map.put("pictureGroup", pictureGroup);
		map.put("contentVideo", contentVideo);
		map.put("vote", vote);
		map.put("survey", survey);
		map.put("activity", activity);
		contentsService.saveContent(map);

		// 同时发布的栏目id
		String funVal0 = request.getParameter("funVal0");
		if(StringUtils.isNotEmpty(funVal0)){
			contentsService.saveOtherContent(contentsId, funVal0);
		}

		
		if (StringUtils.isNotEmpty(contentsId)) {
			contents = contentsService.getContensById(contentsId);
		}
		j.accumulate("contentsid", contents.getId());
		j.accumulate("isSuccess", true);
		Map<String, String> mapStr = ContentClassify.map;
		String classify = contents.getClassify();
		j.accumulate("msg", "保存" + mapStr.get(classify) + "成功");
		j.accumulate("toUrl",
				"contentsController.do?table&tab=alone&contentCatId="
						+ contents.getCatid());
		String str = j.toString();
		return str;

	}

	/**
	 * 内容删除
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public String del(HttpServletRequest request) {
		JSONObject j = new JSONObject();
		String contentsId = request.getParameter("id");
		String contentCatId = request.getParameter("contentCatId");
		String constants=request.getParameter("constants");
		// 判断是否为全部显示
		String tab = request.getParameter("tab");
		String[] ids = contentsId.split(",");
		try {
			contentsService.delContent(ids);
			message = "内容删除成功";
		} catch (Exception e) {
			message = "内容删除失败";
		}
		j.accumulate("isSuccess", true);
		j.accumulate("msg", message);
		j.accumulate("toUrl", "contentsController.do?table&tab=" + tab
				+ "&contentCatId=" + contentCatId+"&constants="+constants);
		String str = j.toString();
		return str;
	}

	/**
	 * 左边树展示
	 *
	 * @param requset
	 * @return
	 */
	@RequestMapping(params = "contentsTree")
	public ModelAndView contentsTree(HttpServletRequest requset) {
		List<ContentCatVOTreeEntity> list = contentCatService.getSimpleAllBySite(UserUtils.getSiteId());
		TSUser user = UserUtils.getUser();
		int flag = user.getAuthentication();
		if(flag== PersonnelStatusConstant.EDITORIAL_STAFF || flag==PersonnelStatusConstant.ORDINARY_PERSON){
			contentCatService.filterPCCatalogPermission(list);//过滤栏目权限
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("list", list);
		return new ModelAndView("cms/contents/leftcontentEditTree", m);
	}

	/**
	 * 获取选中的相关内容
	 *
	 * @return
	 */
	@RequestMapping(params = "correlations")
	@ResponseBody
	public String correlations(VoteOptionEntity voteOption,
							   HttpServletRequest request) {
		String checked = request.getParameter("checked");
		// 获取当前毫秒数
		String contentId = request.getParameter("contentId");
		String temporary = request.getParameter("temporary");

		JSONObject j = new JSONObject();
		j.accumulate("isSuccess", true);
		j.accumulate("checked", checked);
		j.accumulate("temporary", temporary);
		j.accumulate("contentId", contentId);
		j.accumulate("toUrl", "contentsController.do?showDiv");
		String str = j.toString();
		return str;
	}

	/**
	 * 相关div显示
	 *
	 * @return
	 */
	@RequestMapping(params = "showDiv")
	public ModelAndView showDiv(HttpServletRequest request) {
		// 获取当前毫秒数
		String temporary = request.getParameter("temporary");
		String contentId = request.getParameter("contentId");

		List<RelateContentEntity> relateContentList = new ArrayList<RelateContentEntity>();
		List<RelateContentEntity> relateContentListT = new ArrayList<RelateContentEntity>();
		String checked = request.getParameter("checked");
		// 有选中项时
		if (StringUtils.isNotEmpty(checked)) {
			String[] checkArray = checked.split(",");
			for (int i = 0; i < checkArray.length; i++) {
				if (checkArray[i] != "") {
					ContentsEntity content = contentsService
							.get(ContentsEntity.class,
									String.valueOf(checkArray[i]));
					// 所有选中的保存于相关内容表中
					RelateContentEntity relateContent = new RelateContentEntity();
					relateContent.setContentId(temporary);
					relateContent.setRelateTitle(content.getTitle());
					relateContent.setRelateUrl(content.getUrl());
					relateContent.setImages(content.getThumb());
					relateContent.setPart(checkArray[i]);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
					try {
						relateContent
								.setCreated(df.parse(df.format(new Date())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					contentsService.save(relateContent);
				}
			}
		}
		if (StringUtils.isNotEmpty(temporary)) {
			relateContentListT = contentsService.findByProperty(
					RelateContentEntity.class, "contentId", temporary);
			for (RelateContentEntity relateContent : relateContentListT) {
				relateContentList.add(relateContent);
			}
		}
		if (StringUtils.isNotEmpty(contentId) && !"-1".equals(contentId)) {
			relateContentListT = contentsService.findByProperty(
					RelateContentEntity.class, "contentId", contentId);
			for (RelateContentEntity relateContent : relateContentListT) {
				relateContentList.add(relateContent);
			}
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("relateContentList", relateContentList);
		m.put("temporary", temporary);
		m.put("contentId", contentId);
		return new ModelAndView("cms/article/dialog_attacharticleDiv", m);
	}

	/**
	 * 相关div显示(修改)
	 *
	 * @return
	 */
	@RequestMapping(params = "showEditDiv")
	public ModelAndView showEditDiv(HttpServletRequest request) {
		// 获取当前毫秒数
		String temporary = request.getParameter("temporary");
		String contentId = request.getParameter("contentId");
		List<RelateContentEntity> relateContentList = new ArrayList<RelateContentEntity>();
		if (!contentId.equals("") && !contentId.equals("-1")) {
			// 排序
			String hql = "from RelateContentEntity relate where relate.contentId='"
					+ contentId + "' order by relate.relateOrder asc";
			relateContentList = contentsService.findByQueryString(hql);
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("relateContentList", relateContentList);
		m.put("temporary", temporary);
		m.put("contentId", contentId);
		return new ModelAndView("cms/article/dialog_attacharticleDiv", m);
	}

	/**
	 * 相关搜索弹出框
	 *
	 * @return
	 * @author zhangxiaoqiang
	 */
	@RequestMapping(params = "correlationDialog")
	public ModelAndView correlationDialog(ContentsEntity contents,
										  String title, String classify1, HttpServletRequest request) {
		String content_id = request.getParameter("content_id");
		// 获取当前毫秒数
		String temporary = request.getParameter("temporary");
		// 相关搜索关键字
		String seek = request.getParameter("seek");
		String str = "";
		if (StringUtils.isNotEmpty(seek)) {
			try {
				str = new String(seek.getBytes("iso-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 获取内容列表
		int pageSize = StringUtils.isEmpty(request.getParameter("pageSize")) ? 10
				: Integer.valueOf(request.getParameter("pageSize"));
		int pageNo = StringUtils.isEmpty(request.getParameter("pageNo")) ? 1
				: Integer.valueOf(request.getParameter("pageNo"));

		CriteriaQuery cq = new CriteriaQuery(ContentsEntity.class, pageSize,
				pageNo, "", "");
		if (!"0".equals(classify1)) {
			cq.eq("classify", classify1);
		}
		cq.eq("constants", "50");
		cq.eq("siteid", UserUtils.getSiteId());
		cq.addOrder("created", SortDirection.desc);
		cq.addOrder("published", SortDirection.desc);
		cq.add();
		// 查询条件组装器
		Map param = request.getParameterMap();
		HqlGenerateUtil.installHql(cq, contents, param);
		// 排序条件
		PageList pageList = this.contentsService.getPageList(cq, true);
		List<ContentsEntity> testList = pageList.getResultList();
		for (ContentsEntity content : testList) {
			ContentCatEntity contentCat = contentsService.get(
					ContentCatEntity.class, content.getCatid());
			if (contentCat != null) {
				content.setCatName(contentCat.getName());
			}
		}
		int pageCount = (int) Math.ceil((double) pageList.getCount()
				/ (double) pageSize);
		if (pageCount <= 0) {
			pageCount = 1;
		}

		List<RelateContentEntity> relateList = new ArrayList<RelateContentEntity>();
		List<RelateContentEntity> relateListT = new ArrayList<RelateContentEntity>();
		if (!content_id.equals("-1") && !content_id.equals("")) {
			relateListT = contentsService.findByProperty(
					RelateContentEntity.class, "contentId", content_id); // 获取内容相关表数据
			for (RelateContentEntity relateContent : relateListT) {
				relateList.add(relateContent);
			}
		}
		if (!temporary.equals("")) {
			relateListT = contentsService.findByProperty(
					RelateContentEntity.class, "contentId", temporary); // 获取内容相关表数据
			for (RelateContentEntity relateContent : relateListT) {
				relateList.add(relateContent);
			}
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("pageList", testList);
		m.put("pageNo", pageList.getCurPageNO());
		m.put("pageSize", pageSize);
		m.put("pageCount", pageCount);
		m.put("temporary", temporary);
		m.put("contentId", content_id);
		m.put("seek", str);
		m.put("classify1", classify1);
		m.put("title", title);
		m.put("actionUrl", "contentsController.do?correlationDialog&temporary="
				+ temporary + "&content_id=" + content_id + "&seek=" + seek);
		return new ModelAndView("cms/article/attachArticleList", m);
	}

	/**
	 * 转发页面
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "pushView")
	public ModelAndView pushView(HttpServletRequest request) {
		String contentsId = request.getParameter("contentsId");
		String contentCatId = request.getParameter("contentCatId");
		String classify = request.getParameter("classify");
		// 添加/编辑操作区分
		String flag = request.getParameter("flag");
		// 链接、投票页面用于提示
		String tab = request.getParameter("tab");
		ContentsEntity content = new ContentsEntity();
		ContentCatEntity contentCat = new ContentCatEntity();
		if (StringUtils.isNotEmpty(contentsId)) {
			content = contentsService.getEntity(ContentsEntity.class,
					String.valueOf(contentsId));

		}
		if (StringUtils.isNotEmpty(contentCatId)) {
			contentCat = contentsService.get(ContentCatEntity.class,
					String.valueOf(contentCatId));

		}
		// 栏目选择的关联移动频道
		MobileChannelEntity mobileChannel = new MobileChannelEntity();
		if (StringUtils.isNotEmpty(contentCat.getMobileChannel())) {
			mobileChannel = contentsService.get(MobileChannelEntity.class,
					String.valueOf(contentCat.getMobileChannel()));
		}
		// 获取已选的专题
		String sql = "select s.special_name as name,s.id as id  from cm_simplespecial s inner join cm_simplespecial_content sc on s.id= sc.simplespecialid where sc.contentid = '"
				+ contentsId + "'";
		List<Map<String, Object>> mapList = contentsService.findForJdbc(sql,null);
		String specialId = "";
		String specialNames = "";
		for (Map<String, Object> mp : mapList) {
			specialId += mp.get("id") + ",,,";
			specialNames += mp.get("name") + ",";
		}
		if (!StringUtils.isEmpty(specialNames))
			specialNames = specialNames.substring(0, specialNames.length() - 1);

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("contentsId", contentsId);
		m.put("specialId", specialId);
		m.put("specialNames", specialNames);
		m.put("tab", tab);
		m.put("content", content);
		m.put("contentCat", contentCat);
		m.put("classify", classify);
		m.put("flag", flag);
		m.put("mobileChannel", mobileChannel);
		m.put("article_stat", ContentMobileClassify.CONTENT_ARTICLE);
		m.put("picture_stat", ContentMobileClassify.CONTENT_PICTUREGROUP);
		m.put("link_stat", ContentMobileClassify.CONTENT_LINK);
		m.put("video_stat", ContentMobileClassify.CONTENT_VIDEO);
		m.put("vote_stat", ContentMobileClassify.CONTENT_VOTE);
		m.put("survey_stat", ContentMobileClassify.CONTENT_SURVEY);
		return new ModelAndView("cms/contents/pushContent", m);
	}

	/**
	 * 获取专题
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getSpecialAllArray")
	@ResponseBody
	public JSONArray getSpecialAllArray(String contentid,
										HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
		String sql = "select sim.id as sid,sim.special_name as sSpecialName,sim.special_type as sSpecialType,t.scid,t.sccontentid from cm_simplespecial  sim left join (select s.id as sid,s.special_name as sSpecialName,s.special_type as sSpecialType ,sc.id as scid ,sc.contentid as sccontentid from cm_simplespecial s left join cm_simplespecial_content sc on s.id= sc.simplespecialid where sc.contentid = '"
				+ contentid + "')  t on sim.id = t.sid";
		JSONObject json = null;
		List<Map<String, Object>> mpList = contentsService.findForJdbc(sql,null);
		for (int i = 0; i < mpList.size(); i++) {
			json = new JSONObject();
			json.accumulate("id", mpList.get(i).get("sid"));
			json.accumulate("name", mpList.get(i).get("sSpecialName"));
			Object oscid = mpList.get(i).get("scid");
			Object osccontentid = mpList.get(i).get("sccontentid");
			String ssccontentid = osccontentid == null ? "" : osccontentid
					.toString();

			if (oscid != null && ssccontentid.equals(contentid)) {
				json.accumulate("checked", "true");
			} else {
				json.accumulate("checked", "false");
			}
			jsonArray.add(json);
		}
		return jsonArray;
	}

	/**
	 * 加载频道树
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "loadmobile")
	@ResponseBody
	public JSONArray loadmobile(HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
		// 频道tree
		CriteriaQuery cq = new CriteriaQuery(MobileChannelEntity.class);
		cq.eq("levels", 0);
		cq.addOrder("created", SortDirection.desc);
		cq.addOrder("sort", SortDirection.desc);
		// cq.addOrder("id", SortDirection.desc);
		cq.add();
		List<MobileChannelEntity> mobileChannelList = contentsService
				.getListByCriteriaQuery(cq, false);
		for (MobileChannelEntity mobileChannel : mobileChannelList) {
			JSONObject json = new JSONObject();
			// 父节点
			if (mobileChannel.getLevels() == 0) {
				json.put("id", mobileChannel.getId());
				json.put("name", mobileChannel.getName());
				json.put("open", false);
				json.put("children", getChildren(mobileChannel));
				jsonArray.add(json);
			}
		}
		return jsonArray;
	}

	/**
	 * 子节点
	 *
	 * @param mobileChannel
	 * @return
	 */
	public JSONArray getChildren(MobileChannelEntity mobileChannel) {
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();

		CriteriaQuery cq = new CriteriaQuery(MobileChannelEntity.class);
		cq.eq("mobileChannel.id", String.valueOf(mobileChannel.getId()));
		cq.addOrder("sort", SortDirection.desc);
		cq.addOrder("created", SortDirection.desc);
		cq.add();
		List<MobileChannelEntity> mobileChannelList = contentsService
				.getListByCriteriaQuery(cq, false);

		if (mobileChannelList == null || mobileChannelList.size() == 0) {
			return jsonArray;
		}

		for (MobileChannelEntity mobileChannel1 : mobileChannelList) {
			json.put("id", mobileChannel1.getId());
			json.put("name", mobileChannel1.getName());
			json.put("open", false);
			json.put("children", getChildren(mobileChannel1));
			jsonArray.add(json);
		}
		return jsonArray;
	}

	/**
	 * 移动到、引用到、复制到内容页面
	 *
	 * @param ids
	 *            操作的ids
	 * @param toType
	 *            操作类型（copy：复制）、（cite：移动）、（mobile：移动）（toOtherSite：站点）
	 * @return
	 */
	@RequestMapping(params = "operate")
	public ModelAndView operate(String ids,String toType) {
		Map<String, Object> m = new HashMap<String, Object>();
		SiteEntity site= UserUtils.getSite();
		//m.put("siteList", UserUtils.getSiteList());
		m.put("ids", ids);
		m.put("site", site);
		m.put("toType", toType);
		m.put("contentCattreeJsonData", contentCatService
				.getContentCatTreeData().toString());
		return new ModelAndView("cms/contents/contentCatTree", m);
	}

	/**
	 * 获取栏目树节点数据，可以根据站点获取，不传默认获取当前站点数据
	 *
	 * @param siteid
	 * @return
	 */
	@RequestMapping(params = "getContentCatTreeDataBySite")
	@ResponseBody
	public JSONArray getContentCatTreeDataBySite(String siteid) {
		return contentCatService.getContentCatTreeData(siteid);
	}

	/**
	 * 确认移动到、引用到、复制到内容
	 *
	 * @param ids
	 *            操作的ids
	 * @param toType
	 *            操作类型（copy：复制）、（cite：移动）、（mobile：移动）、（toOtherSite：站点）
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "toContents")
	@ResponseBody
	public String toContents(HttpServletRequest request,String ids,String toType) {
		String contentCatId = request.getParameter("funVal");
		String siteId=request.getParameter("siteId");
		String str = contentsService.mobiles(ids, contentCatId, toType,siteId);
		return str;
	}

	/**
	 * 增加浏览量
	 *
	 * @param contentId
	 */
	@RequestMapping(params = "addPv")
	@ResponseBody
	public void addPv(String  contentId) {
		contentsService.addPv(contentId);
	}


	/**
	 * 将新闻设置为头条
	 */
	@RequestMapping(params = "setHeadline")
	@ResponseBody
	public String setHeadline(HttpServletRequest request) {

		String contentId = request.getParameter("contentId");
		ContentsEntity c = contentsService.getEntity(ContentsEntity.class,
				contentId);
		String str = "";
		if (c.getIsHeadline() == 0) {
			c.setIsHeadline(1);
			str = "1";
		} else {
			c.setIsHeadline(0);
			str = "0";
		}
		contentsService.save(c);
		return str;
	}

	/**
	 * 将word文档转为html
	 * @throws Exception
	 */
	@RequestMapping(params = "wordToHtml")
	@ResponseBody
	public String wordToHtml(HttpServletRequest request) {
		String pathWord=request.getParameter("pathUrl");
		String typeWord=request.getParameter("fileType");
		String html=new String();
		String uploadFilesPath= CmsConstants.getUploadFilesPath(UserUtils.getSite());
		String pathDoc=uploadFilesPath+pathWord;
		String outPath = ResourceUtil.getCMSUploadFilesPath();
		String contents=null;
		try {
			if (typeWord.equals(".doc")) {
				html = DocToHtml.convertToHtml(pathDoc);

				contents = ReadHtml.getHtml(html).replace(outPath, "").replace("\\", "/").replace("src=\"", "src=\"/upload/image/htmlFile/" + PathFormat.parse("{yyyy}/{mm}/{dd}/",""));
			} else if (typeWord.equals(".docx")) {
				html = DocxToHtml.doGenerateSysOut(pathDoc);
				contents = ReadHtml.getHtml(html).replace("\\", "/").replace(outPath, "").replace("   ", "");
				contents = StringEscapeUtils.unescapeHtml4(contents);
			}
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
/*		FileUtil.delete(html);
		FileUtil.delete(pathDoc);*/
		String beginstr = "<head>";//目标字符串1
		String endstr = "</head>";//目标字符串2
		int first= contents.indexOf(beginstr);//第一个字符串的起始位置
		int sencond= contents.indexOf(endstr);//第二个字符串的起始位置	
		String result= contents.substring(0, first+beginstr.length())+contents.substring(sencond, contents.length());
		return result;
	}
}
