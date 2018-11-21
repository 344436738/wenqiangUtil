package com.leimingtech.cms.core.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * freemarker工具类
 * 
 * @author liuzhen
 *  
 */
public class FreemarkerUtils {

	private static Configuration cfg = new Configuration();

	static {
		cfg.setLocale(Locale.CHINA);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setNumberFormat("#");
	}

	public static String renderString(String templateString,
			Map<String, ?> model) throws IOException, TemplateException {
		StringWriter result = new StringWriter();
		Template t = new Template("name", new StringReader(templateString), cfg);
		t.process(model, result);
		return result.toString();
	}

	/**
	 * 通过Freemarker解析模板生成
	 * @param templatePath 模板路径
	 * @param templateName 模板名称
	 * @param fileName 生成文件路径
     * @param root 传入数据
     */
	public static void analysisTemplate(String templatePath,
										String templateName, String fileName, Map<?, ?> root) {
		try {
			//初使化FreeMarker配置
			Configuration config = new Configuration();
			// 设置要解析的模板所在的目录，并加载模板文件
			config.setDirectoryForTemplateLoading(new File(templatePath));
			// 设置包装器，并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper());

			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			// 否则会出现乱码
			Template template = config.getTemplate(templateName, "UTF-8");
			// 合并数据模型与模板
			FileOutputStream fos = new FileOutputStream(fileName);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 根据freemarker模板内容，返回解析后数据
	 * @param templateDir 模板根路径
	 * @param templateFilepath 模板全路径
	 */
	public static String renderString(Map<String, ?> model, String templateDir, String templateFilepath) throws IOException, TemplateException {
		StringWriter result = new StringWriter();
		cfg.setDirectoryForTemplateLoading(new File(templateDir));
		Template template = cfg.getTemplate(templateFilepath, "utf-8");
		template.process(model, result);
		return result.toString();
	}
}
