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
package it.eng.spagobi.qbe.tree;


// TODO: Auto-generated Javadoc
/**
 * The Class DTree.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class DTree implements IQbeTree {
	
	
	/** The name. */
	private String name;
	
	/** The script. */
	private StringBuffer script;	
		
	/** The Constant DEFAULT_NAME. */
	public static final String DEFAULT_NAME = "dtree";
	
	/**
	 * Instantiates a new d tree.
	 */
	public DTree() {
		this(DEFAULT_NAME);
	}
	
	/**
	 * Instantiates a new d tree.
	 * 
	 * @param treeName the tree name
	 */
	public DTree(String treeName) {
		clearScript();
		setName(treeName);
	}
		
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.IQbeTree#createTree()
	 */
	public void createTree() {	
		appendLineToScript(getName() + " = new dTree('" + getName() + "');");
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.IQbeTree#createTree(java.lang.String)
	 */
	public void createTree(String treeName) {
		setName(treeName);
		appendLineToScript(getName() + " = new dTree('" + getName() + "');");
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.IQbeTree#addNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
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

	
	
	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	protected String getHeader() {
		return "<script type=\"text/javascript\">";
	}
	
	/**
	 * Gets the footer.
	 * 
	 * @return the footer
	 */
	protected String getFooter() {
		return ("document.write("+ name +");\n" +
				"</script>");
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	protected String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Clear script.
	 */
	protected void clearScript() {
		script = new StringBuffer();
	}
	
	/**
	 * Append line to script.
	 * 
	 * @param line the line
	 */
	protected void appendLineToScript(String line) {
		script.append(line + "\n");
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.IQbeTree#getTreeConstructorScript()
	 */
	public String getTreeConstructorScript() {
		return (getHeader() + "\n" +
				script.toString() + "\n" +
				getFooter() + "\n");
	}
}
