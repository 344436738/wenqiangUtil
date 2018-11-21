package com.leimingtech.core.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParamsUtils {

	/**
	 * 获取request中所有参数
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}

	/**
	 * Map中是数据转为URL参数
	 * 
	 * @param m
	 * @return
	 * @author liuzhen
	 * @date 2016-8-9 下午4:19:45
	 */
	public static String toUrlParam(Map<String, String> m) {
		StringBuilder sb = new StringBuilder();
		for (String key : m.keySet()) {
			sb.append(key);
			sb.append('=');
			sb.append(m.get(key));
			sb.append('&');
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获取url参数 以&连接
	 * 
	 * @param request
	 * @return
	 */
	public static String getUrlParams(HttpServletRequest request) {
		Map<String, String> params = getAllParams(request);
		String paramStr = toUrlParam(params);
		return paramStr;
	}
	
	
	/**
	 * 
	 * @Description:参数过滤方法
	 * @author Administrator 朱俊一
	 * @date 2018年5月30日
	 * @version 1.0
	 * @param param 空Map
	 * @param body  解析body放入Map中
	 * @throws IOException 
	 * @throws JSONException 
	 */
  	protected HashMap<String, Object>  paramConvert(HttpServletRequest request) throws IOException, JSONException{
  		HashMap<String, Object> param = new HashMap<String, Object>();
  		StringBuilder sb = new StringBuilder();
		Object obj = "";
  		BufferedReader reader = request.getReader();
    	String line;
    	while ((line = reader.readLine()) != null)  
    		sb.append(line);
    	String content = sb.toString();
		JSONObject jsonObject = new JSONObject(content);
		//解析请求的参数
    	JSONObject sinoRequest = (JSONObject)jsonObject.get(Constans.SINOREQUEST);
		JSONObject paramData = (JSONObject)sinoRequest.get(Constans.PARAMDATA);
  		try {
  			Iterator it = paramData.keys();
  			while(it.hasNext()){
  				Object key = it.next();
  				if(key instanceof String){
  					String k = key.toString();
  					param.put(k, paramData.get(k).toString());
  				}
  			}
  		} catch (JSONException e) {
  			logger.error("参数转换错误" + e.getMessage() + e.getMessage());
  		}
		return param;
  	}
	
	
	
	
	
}
