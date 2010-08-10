/**
 * 
 */
package it.eng.spagobi.utilities.javascript;

/**
 * @author Gioia
 *
 */
public abstract class BaseJsTreeBuilder implements IJsTreeBuilder {
	String name = DEFAULT_NAME;
	StringBuffer buffer;
	
	public static final String DEFAULT_NAME = "dtree";
	
		
	/**
	 * @param id
	 * @param pid
	 * @param nname
	 * @param url
	 * @param title
	 * @param target
	 * @param icon
	 * @param iconOpen
	 * @param open
	 * @param onclick
	 * @param checkName
	 * @param checkValue
	 * @param checked
	 */
	void addNode(String id, 
				 String pid, 
				 String nname, 
				 String url, 
				 String title, 
				 String target, 
				 String icon, 
				 String iconOpen, 
				 String open, 
				 String onclick, 
				 String checkName, 
				 String checkValue, 
				 String checked) {
		
		buffer.append(name + ".add(" +
			"'" + id + "', " +
			"'" + pid + "', " +
			"'" + nname + "', " +
			"'" + url + "', " +
			"'" + title + "', " +
			"'" + target + "', " +
			"'" + icon + "', " + 
			"'" + iconOpen + "', " +
			"'" + open + "', " +  
			"'" + onclick + "', " +
			"'" + checkName + "', " +
			"'" + checkValue + "', " +
			"'" + checked + "');\n");				
	}

	public void addTree() {
		buffer.append(name +" = new dTree('" + name + "');");
	}
	
	public void addHeader() {
		buffer.append("<script type=\"text/javascript\">");
	}
	
	public void addFooter() {
		buffer.append("document.write("+ name +");");
		buffer.append("</script>");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
