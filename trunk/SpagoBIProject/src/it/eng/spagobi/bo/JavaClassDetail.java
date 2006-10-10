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

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.io.Serializable;

/**
 * Defines the <code>JavaClassDetail</code> objects. This object is used to store 
 * JavaClass Wizard detail information.
 * 
 * @author Zerbetto
 */

public class JavaClassDetail implements Serializable {

	private String javaClassName = "";
	private boolean isSingleValue = false;
	private boolean isListOfValues = false;

	public String getJavaClassName() {
		return javaClassName;
	}
	
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}
	
	public boolean isSingleValue() {
		return isSingleValue;
	}
	
	public void setIsSingleValue(boolean isSingleValue) {
		this.isSingleValue = isSingleValue;
	}
	
	public boolean isListOfValues() {
		return isListOfValues;
	}
	
	public void setIsListOfValues(boolean isListOfValues) {
		this.isListOfValues = isListOfValues;
	}
	
	public String toXML () { 
		String XML = "<JAVACLASSLOV>" +
				     "<JAVA_CLASS_NAME>"+this.getJavaClassName()+"</JAVA_CLASS_NAME>" +
			         "<SINGLE_VALUE>"+this.isSingleValue()+"</SINGLE_VALUE>" +
			         "<LIST_VALUES>"+this.isListOfValues()+"</LIST_VALUES>" +
				     "</JAVACLASSLOV>";
		return XML;
	}
	
	public static JavaClassDetail fromXML(String dataDefinition1) throws SourceBeanException {
		dataDefinition1.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition1);
		SourceBean javaClassNameSB = (SourceBean)source.getAttribute("JAVA_CLASS_NAME");
		String javaClassName = javaClassNameSB.getCharacters(); 
		
		boolean singlevalue = true;
		SourceBean singleSB = (SourceBean)source.getAttribute("SINGLE_VALUE");
		String singleValueStr = singleSB.getCharacters();
		singlevalue = Boolean.valueOf(singleValueStr).booleanValue(); 
			
		boolean listvalues = false;
		SourceBean listSB = (SourceBean)source.getAttribute("LIST_VALUES");
		String listValueStr = listSB.getCharacters();
	    listvalues = Boolean.valueOf(listValueStr).booleanValue(); 
		
	    JavaClassDetail javaClassDetail = new JavaClassDetail();
	    javaClassDetail.setJavaClassName(javaClassName);
	    javaClassDetail.setIsListOfValues(listvalues);
        javaClassDetail.setIsSingleValue(singlevalue);
		return javaClassDetail;
	}
	
}
