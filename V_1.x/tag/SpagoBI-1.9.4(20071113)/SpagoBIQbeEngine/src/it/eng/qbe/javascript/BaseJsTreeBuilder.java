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
package it.eng.qbe.javascript;

/**
 * @author Gioia
 *
 */
public abstract class BaseJsTreeBuilder implements IJsTreeBuilder {
	String name = DEFAULT_NAME;
	StringBuffer buffer;
	String classPrefix = null;
	
	
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

	public String getClassPrefix() {
		return classPrefix;
	}

	public void setClassPrefix(String classPrefix) {
		this.classPrefix = classPrefix;
	}

}
