/**
 * 
 */
package demo.pojo;

import struts.stauts.form;

/**
 * @author msa12
 *2018
 */
public class demoPojo  extends  form{
     /**
	 * 
	 */
	public demoPojo() {
		super();
	}

	private String name;
     
     private String text;
     
     @Override
	public String toString() {
		return "demoPojo [name=" + name + ", text=" + text + ", path=" + path
				+ "]";
	}

	private String path;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
