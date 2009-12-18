/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.javascript;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseJsTreeBuilder.
 * 
 * @author Gioia
 */
public abstract class BaseJsTreeBuilder implements IJsTreeBuilder {
	
	/** The name. */
	String name = DEFAULT_NAME;
	
	/** The buffer. */
	StringBuffer buffer;
	
	/** The class prefix. */
	String classPrefix = null;
	
	
	/** The Constant DEFAULT_NAME. */
	public static final String DEFAULT_NAME = "dtree";
	
		
	/**
	 * Adds the node.
	 * 
	 * @param id the id
	 * @param pid the pid
	 * @param nname the nname
	 * @param url the url
	 * @param title the title
	 * @param target the target
	 * @param icon the icon
	 * @param iconOpen the icon open
	 * @param open the open
	 * @param onclick the onclick
	 * @param checkName the check name
	 * @param checkValue the check value
	 * @param checked the checked
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

	/**
	 * Adds the tree.
	 */
	public void addTree() {
		buffer.append(name +" = new dTree('" + name + "');");
	}
	
	/**
	 * Adds the header.
	 */
	public void addHeader() {
		buffer.append("<script type=\"text/javascript\">");
	}
	
	/**
	 * Adds the footer.
	 */
	public void addFooter() {
		buffer.append("document.write("+ name +");");
		buffer.append("</script>");
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the class prefix.
	 * 
	 * @return the class prefix
	 */
	public String getClassPrefix() {
		return classPrefix;
	}

	/**
	 * Sets the class prefix.
	 * 
	 * @param classPrefix the new class prefix
	 */
	public void setClassPrefix(String classPrefix) {
		this.classPrefix = classPrefix;
	}

}
