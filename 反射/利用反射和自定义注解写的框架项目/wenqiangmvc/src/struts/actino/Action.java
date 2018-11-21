package struts.actino;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import struts.stauts.form;

public interface Action {
     public String exec(HttpServletRequest request,form f,Map<String,String> map);
}
