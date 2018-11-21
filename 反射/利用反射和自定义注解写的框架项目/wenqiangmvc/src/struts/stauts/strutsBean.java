/**
 * 
 */
package struts.stauts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author msa12
 *2018
 */
public class strutsBean {
    private String actionName;
    
    private String actionType;
    
    private String actionPath;
    
    private String formName;
    
    @Override
	public String toString() {
		return "strutsBean [actionName=" + actionName + ", actionType="
				+ actionType + ", actionPath=" + actionPath + ", formName="
				+ formName + ", map1=" + map1 + "]";
	}

	/**
	 * 
	 */
	public strutsBean() {
		super();
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public Map<String, String> getMap1() {
		return map1;
	}

	public void setMap1(Map<String, String> map1) {
		this.map1 = map1;
	}

	private  Map<String,String> map1=new HashMap<String, String>();
}
