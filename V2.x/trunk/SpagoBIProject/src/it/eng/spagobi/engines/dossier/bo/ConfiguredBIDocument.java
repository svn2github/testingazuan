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
package it.eng.spagobi.engines.dossier.bo;

import it.eng.spago.base.SourceBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Store the information about single document configuration
 *
 */
public class ConfiguredBIDocument {

//	private Integer id = null;
	private String name = "";
	private String label = "";
	private String description = "";
	private List roles = null;
	private Map parameters = null;
	// the placeholder 
	private String logicalName= "";
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List getRoles() {
		return roles;
	}
	public void setRoles(List roles) {
		this.roles = roles;
	}
	public Map getParameters() {
		return parameters;
	}
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public String getLogicalName() {
		return logicalName;
	}
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	public String toXml() {
		String toReturn = 	"<" + logicalName + " label='" + label + "' name='" + name + "' description='" + description + "' >\n";
		toReturn +=			"  <PARAMETERS>\n";
		Set keys = parameters.keySet();
		Iterator iterKeys = keys.iterator();
		while(iterKeys.hasNext()) {
			String paramname = (String) iterKeys.next();
			String paramvalue = (String) parameters.get(paramname);
			toReturn +=			"    <PARAMETER name='" + paramname + "' value='" + paramvalue + "' />\n";
		}
		toReturn +=			"  </PARAMETERS>\n";
		toReturn += 		"</" + logicalName + ">\n";
		return toReturn;
	}
	public static ConfiguredBIDocument fromXml(SourceBean input) {
		ConfiguredBIDocument toReturn = new ConfiguredBIDocument();
		toReturn.setLogicalName(input.getName());
		toReturn.setLabel((String) input.getAttribute("label"));
		toReturn.setName((String) input.getAttribute("name"));
		toReturn.setDescription((String) input.getAttribute("description"));
		List parametersList = input.getAttributeAsList("PARAMETERS.PARAMETER");
		Map parameters = new HashMap();
		if (parametersList != null && parametersList.size() > 0) {
			Iterator parametersListIt = parametersList.iterator();
			while (parametersListIt.hasNext()) {
				SourceBean parameterSb = (SourceBean) parametersListIt.next();
				String name = (String) parameterSb.getAttribute("name");
				String value = (String) parameterSb.getAttribute("value");
				parameters.put(name, value);
			}
		}
		toReturn.setParameters(parameters);
		return toReturn;
	}
}
