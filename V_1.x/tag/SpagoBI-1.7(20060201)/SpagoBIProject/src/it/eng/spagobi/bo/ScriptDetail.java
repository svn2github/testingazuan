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
package it.eng.spagobi.bo;

import java.io.Serializable;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

/**
 * Defines the <code>ScriptDetail</code> objects. This object is used to store 
 * Script Wizard detail information.
 * 
 * @author sulis
 */
public class ScriptDetail  implements Serializable  {
	
	private String script = "";
	private boolean singleValue = false;
	private boolean listOfValues = false;
	
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
	 * Return true only if the execution of the script return an only value.
	 * 
	 * @return true if the script produce an unique output value
	 */
	public boolean isSingleValue() {
		return singleValue;
	}
	
	/**
	 * Set the singleValue flag of the Script. 
	 * A single value script return an unique value.
	 * 
	 * @param singleValue the boolean value of the singleValue flag
	 */
	public void setSingleValue(boolean singleValue) {
		this.singleValue = singleValue;
	}
	
	/**
	 * Return true only if the execution of the script return a list of values.
	 * 
	 * @return true if the script produce a list of output values
	 */
	public boolean isListOfValues() {
		return listOfValues;
	}
	
	/**
	 * Set the listOfValue flag of the Script. 
	 * A list of values script return a list of values.
	 * 
	 * @param listOfValues the boolean value of the listOfValues flag
	 */
	public void setListOfValues(boolean listOfValues) {
		this.listOfValues = listOfValues;
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
			         "<SINGLE_VALUE>"+this.isSingleValue()+"</SINGLE_VALUE>" +
			         "<LIST_VALUES>"+this.isListOfValues()+"</LIST_VALUES>" +
				     "</SCRIPTLOV>";
		return XML;
	}
	
	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods
	 * in order to obtain the source <code>ScriptDetail</code> objects whom XML has been 
	 * built. 
	 * 
	 * @param dataDefinition1	The XML input String
	 * @return The corrispondent <code>ScriptDetail</code> object
	 * @throws SourceBeanException If a SourceBean Exception occurred
	 */
	public static ScriptDetail fromXML (String dataDefinition1) throws SourceBeanException {
		dataDefinition1.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition1);
		SourceBean scriptSB = (SourceBean)source.getAttribute("SCRIPT");
		String script = scriptSB.getCharacters(); 
		
		boolean singlevalue = true;
		SourceBean singleSB = (SourceBean)source.getAttribute("SINGLE_VALUE");
		String singleValueStr = singleSB.getCharacters();
		singlevalue = Boolean.valueOf(singleValueStr).booleanValue(); 
			
		boolean listvalues = false;
		SourceBean listSB = (SourceBean)source.getAttribute("LIST_VALUES");
		String listValueStr = listSB.getCharacters();
	    listvalues = Boolean.valueOf(listValueStr).booleanValue(); 
		
		ScriptDetail scriptDet = new ScriptDetail();
        scriptDet.setScript(script);
        scriptDet.setListOfValues(listvalues);
        scriptDet.setSingleValue(singlevalue);
		return scriptDet;
	}
	
	
	
	
}