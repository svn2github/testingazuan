/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.initializer.descriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class HierarchyDescriptor {
	
	private String name;
	private boolean hasAll;
	private List<HierarchyLevelDescriptor> levels;
	
	public HierarchyDescriptor(){
		name = "";
		hasAll =  false;
		levels = new ArrayList<HierarchyLevelDescriptor>();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the hasAll
	 */
	public boolean isHasAll() {
		return hasAll;
	}
	/**
	 * @param hasAll the hasAll to set
	 */
	public void setHasAll(boolean hasAll) {
		this.hasAll = hasAll;
	}
	/**
	 * @return the levels
	 */
	public List<HierarchyLevelDescriptor> getLevels() {
		return levels;
	}
	/**
	 * @param levels the levels to set
	 */
	public void setLevels(List<HierarchyLevelDescriptor> levels) {
		this.levels = levels;
	}
	
	

}
