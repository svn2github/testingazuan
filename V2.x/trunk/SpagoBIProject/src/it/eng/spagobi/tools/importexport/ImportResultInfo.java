/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.importexport;

import java.util.HashMap;
import java.util.Map;

public class ImportResultInfo {

	String pathLogFile = "";
	String pathAssociationsFile = "";
	Map manualTasks = new HashMap();
	
	/**
	 * Gets the manual tasks.
	 * 
	 * @return the manual tasks
	 */
	public Map getManualTasks() {
		return manualTasks;
	}
	
	/**
	 * Sets the manual tasks.
	 * 
	 * @param manualTasks the new manual tasks
	 */
	public void setManualTasks(Map manualTasks) {
		this.manualTasks = manualTasks;
	}
	
	/**
	 * Gets the path log file.
	 * 
	 * @return the path log file
	 */
	public String getPathLogFile() {
		return pathLogFile;
	}
	
	/**
	 * Sets the path log file.
	 * 
	 * @param pathLogFile the new path log file
	 */
	public void setPathLogFile(String pathLogFile) {
		this.pathLogFile = pathLogFile;
	}
	
	/**
	 * Gets the path associations file.
	 * 
	 * @return the path associations file
	 */
	public String getPathAssociationsFile() {
		return pathAssociationsFile;
	}
	
	/**
	 * Sets the path associations file.
	 * 
	 * @param pathAssFile the new path associations file
	 */
	public void setPathAssociationsFile(String pathAssFile) {
		this.pathAssociationsFile = pathAssFile;
	}
	
}
