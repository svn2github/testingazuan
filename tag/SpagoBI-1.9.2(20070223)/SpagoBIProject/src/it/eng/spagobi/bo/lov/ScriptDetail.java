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
package it.eng.spagobi.bo.lov;

import java.io.Serializable;
import java.util.HashMap;

import groovy.lang.Binding;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.utilities.GeneralUtilities;

/**
 * Defines the <code>ScriptDetail</code> objects. This object is used to store 
 * Script Wizard detail information.
 * 
 * @author sulis
 */
public class ScriptDetail  implements ILovDetail  {
	
	private String script = "";
	
	//	 just for back compatibility purpose
	private boolean singleValue = false;
	
	public ScriptDetail() {}
	
	public ScriptDetail(String dataDefinition) throws SourceBeanException {
		loadFromXML (dataDefinition);
	}
	
	public void loadFromXML (String dataDefinition) throws SourceBeanException {
		dataDefinition.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		SourceBean scriptSB = (SourceBean)source.getAttribute("SCRIPT");
		String script = scriptSB.getCharacters(); 
		
		SourceBean singleValueSB = (SourceBean)source.getAttribute("SINGLE_VALUE");
		String singleValueStr = null;
		if(singleValueSB != null) singleValueStr = singleValueSB.getCharacters(); 
		singleValue = (singleValueStr != null && singleValueStr.equalsIgnoreCase("true"));
		
		setSingleValue(singleValue);		
        setScript(script);
	}
	
	/**
	 * Loads the XML string defined by a <code>ScriptDetail</code> object. The object
	 * gives us all XML field values. Once obtained, the XML represents the data 
	 * definition for a Script to load values Input Type Value LOV object. 
	 * 
	 * @return The XML output String
	 */
	public String toXML () { 
		String XML = "<SCRIPTLOV>" +
				     "<SCRIPT>"+this.getScript()+"</SCRIPT>" +	
				     (isSingleValue()?"<SINGLE_VALUE>true</SINGLE_VALUE>":"") +
				     "</SCRIPTLOV>";
		return XML;
	}
	
	public String getLovResult(IEngUserProfile profile) throws Exception {
		String result = null;
		HashMap attributes = GeneralUtilities.getAllProfileAttributes(profile);
		Binding bind = GeneralUtilities.fillBinding(attributes);
		result = GeneralUtilities.testScript(getScript(), bind);
		if(isSingleValue() && !result.contains("<")) result = convertResult(result);
		return result;
	}
	
	private String convertResult(String result) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<ROWS>");
		sb.append("<ROW VALUE=\"" + result +"\"/>");
		sb.append("<VISIBLE-COLUMNS>VALUE</VISIBLE-COLUMNS>");
		sb.append("<VALUE-COLUMN>VALUE</VALUE-COLUMN>");
		sb.append("<DESCRIPTION-COLUMN>VALUE</DESCRIPTION-COLUMN>");
		sb.append("</ROWS>");
		
		return sb.toString();
	}
	
	
	/**
	 * Get the string of the script
	 * 
	 * @return The string of the script 
	 */
	public String getScript() {
		return script;
	}
	
	/**
	 * Set the string of the script
	 * 
	 * @param script the string of the script
	 */
	public void setScript(String script) {
		this.script = script;
	}
		
	
	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods
	 * in order to obtain the source <code>ScriptDetail</code> objects whom XML has been 
	 * built. 
	 * 
	 * @param dataDefinition	The XML input String
	 * @return The corrispondent <code>ScriptDetail</code> object
	 * @throws SourceBeanException If a SourceBean Exception occurred
	 */
	public static ScriptDetail fromXML (String dataDefinition) throws SourceBeanException {
		ScriptDetail scriptDet = new ScriptDetail(dataDefinition);
        return scriptDet;
	}

	public boolean isSingleValue() {
		return singleValue;
	}

	public void setSingleValue(boolean singleValue) {
		this.singleValue = singleValue;
	}	
}