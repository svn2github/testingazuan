/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.qbe.tree.presentation.tag;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DTree implements IQbeTree {

	private String name;
	private StringBuffer script;	
	
	private String classPrefix;
	
		
	public static final String DEFAULT_NAME = "dtree";
	
	
	public DTree() {
		this(DEFAULT_NAME);
	}
	
	public DTree(String treeName) {
		clearScript();
		setName(treeName);		
	}
		
	public void createTree() {		
		appendLineToScript(getName() + " = new dTree('" + getName() + "');");
	}
	
	public void createTree(String treeName) {
		setName(treeName);
		appendLineToScript(getName() + " = new dTree('" + getName() + "');");
	}
	
	public void addNode(String id, 
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
		
		appendLineToScript(getName() + ".add(" +
			"'" + id + "', " +
			"'" + pid + "', " +
			"'" + nname + "', " +
			"'" + url + "', " +
			"'" + title + "', " +
			//"'" + target + "', " +
			"'', " +
			"'" + icon + "', " + 
			"'" + iconOpen + "', " +
			"'" + open + "', " +  
			"'" + onclick + "', " +
			"'" + checkName + "', " +
			"'" + checkValue + "', " +
			"'" + checked + "');\n");				
	}

	
	
	protected String getHeader() {
		return "<script type=\"text/javascript\">";
	}
	
	protected String getFooter() {
		return ("document.write("+ name +");\n" +
				"</script>");
	}
	
	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}
	
	protected void clearScript() {
		script = new StringBuffer();
	}
	
	protected void appendLineToScript(String line) {
		script.append(line + "\n");
	}
	
	public String getTreeConstructorScript() {
		return (getHeader() + "\n" +
				script.toString() + "\n" +
				getFooter() + "\n");
	}
	
	
	
	

	public String getClassPrefix() {
		return classPrefix;
	}

	public void setClassPrefix(String classPrefix) {
		this.classPrefix = classPrefix;
	}

}
