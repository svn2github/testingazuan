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
 * Interface of classes that can build a java script dTree object. By contract class
 * of this type have only to create the tree and build its structure. The iclusion
 * of the dTree.js script and all of css files used to render it should be handled 
 * separatly by the caller. 
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public interface IQbeTree {
	
	public void createTree();
	public void createTree(String treeName);
	
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
			 String checked);
	

	public String getClassPrefix() ;
	public void setClassPrefix(String classPrefix);

	public String getTreeConstructorScript();
}
