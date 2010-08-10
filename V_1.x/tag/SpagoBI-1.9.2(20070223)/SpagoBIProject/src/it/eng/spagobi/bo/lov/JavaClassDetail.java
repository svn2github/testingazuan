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

import groovy.lang.Binding;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.javaClassLovs.IJavaClassLov;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

/**
 * Defines the <code>JavaClassDetail</code> objects. This object is used to store 
 * JavaClass Wizard detail information.
 * 
 * @author Zerbetto
 */

public class JavaClassDetail implements ILovDetail {

	private String javaClassName = "";
	
	// just for back compatibility purpose
	private boolean singleValue = false;

	public JavaClassDetail() {}
	
	public JavaClassDetail(String dataDefinition) throws SourceBeanException {
		loadFromXML(dataDefinition);
	}
	
	public void loadFromXML(String dataDefinition) throws SourceBeanException {
		dataDefinition.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		SourceBean javaClassNameSB = (SourceBean)source.getAttribute("JAVA_CLASS_NAME");
		String javaClassName = javaClassNameSB.getCharacters();
		
		SourceBean singleValueSB = (SourceBean)source.getAttribute("SINGLE_VALUE");
		String singleValueStr = null;
		if(singleValueSB != null) singleValueStr = singleValueSB.getCharacters(); 
		singleValue = (singleValueStr != null && singleValueStr.equalsIgnoreCase("true"));
		
		setSingleValue(singleValue);
	    setJavaClassName(javaClassName);		
	}
	
	public String toXML () { 
		String XML = "<JAVACLASSLOV>" +
				     "<JAVA_CLASS_NAME>"+this.getJavaClassName()+"</JAVA_CLASS_NAME>" +
				     (isSingleValue()?"<SINGLE_VALUE>true</SINGLE_VALUE>":"") +
				     "</JAVACLASSLOV>";
		return XML;
	}
	
	public String getLovResult(IEngUserProfile profile) throws Exception {
		String result = null;
		
		String javaClassName = getJavaClassName();
		if (javaClassName == null || javaClassName.trim().equals("")){
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
					GeneralUtilities.class.getName(), 
					"getLovResult", "The java class name is not specified");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1071");
		}
		IJavaClassLov javaClassLov = null;
		Class javaClass = null;
		try {
			javaClass = Class.forName(javaClassName);
		} catch (ClassNotFoundException e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
					GeneralUtilities.class.getName(), 
					"getLovResult", "Java class '" + javaClassName + "' not found!!");
			Vector v = new Vector();
			v.add(javaClassName);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1072", v);
		}
		try {
			javaClassLov = (IJavaClassLov) javaClass.newInstance();
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
					GeneralUtilities.class.getName(), 
					"getLovResult", "Error while instatiating Java class '" + javaClassName + "'.");
			Vector v = new Vector();
			v.add(javaClassName);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1073", v);
		}
		result = javaClassLov.getValues(profile);
		
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
	
	public String getJavaClassName() {
		return javaClassName;
	}
	
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}	
	
	
	
	public static JavaClassDetail fromXML(String dataDefinition1) throws SourceBeanException {
		dataDefinition1.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition1);
		SourceBean javaClassNameSB = (SourceBean)source.getAttribute("JAVA_CLASS_NAME");
		String javaClassName = javaClassNameSB.getCharacters(); 
						
	    JavaClassDetail javaClassDetail = new JavaClassDetail();
	    javaClassDetail.setJavaClassName(javaClassName);
	    return javaClassDetail;
	}

	public boolean isSingleValue() {
		return singleValue;
	}

	public void setSingleValue(boolean singleValue) {
		this.singleValue = singleValue;
	}
	
}
