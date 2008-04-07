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
package it.eng.spagobi.tools.dataset.bo;

import it.eng.spago.security.IEngUserProfile;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSet {

	private int dsId;
    	private String name=null;
    	private String description=null;
    	private String label=null;
    	private String parameters=null;

    	
    	
	public int getDsId() {
			return dsId;
		}
		public void setDsId(int dsId) {
			this.dsId = dsId;
		}
	public String getParameters() {
	    return parameters;
	}
	public void setParameters(String parameters) {
	    this.parameters = parameters;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getDescription() {
	    return description;
	}
	public void setDescription(String description) {
	    this.description = description;
	}
	public String getLabel() {
	    return label;
	}
	public void setLabel(String label) {
	    this.label = label;
	}
	
	public String getDataSetResult(IEngUserProfile profile) throws Exception
	{
		return null;
	}
	
	
	
	
	
	
	
	
	
    	
}
