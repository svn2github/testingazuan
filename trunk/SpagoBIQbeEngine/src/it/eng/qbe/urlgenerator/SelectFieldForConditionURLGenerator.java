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
package it.eng.qbe.urlgenerator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andrea Zoppello
 * 
 * This implementation of IURLGenerator is responsible for generating URL to
 * select a field for where clause
 *
 */

public class SelectFieldForConditionURLGenerator implements IURLGenerator{

	/** 
	 * @see it.eng.qbe.utility.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	private String className = null;
	private String aliasedClassName = null;
	private String classPrefix = null;
	
	
	private IQbeUrlGenerator qbeUrlGenerator = null;
	private HttpServletRequest httpRequest = null;
	
	/**
	 * @param qbeUrlGenerator
	 * @param httpRequest
	 */
	/*
	public SelectFieldForConditionURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
	}
	*/
	
	/**
	 * @param classCompleteName
	 * @param qbeUrlGenerator
	 * @param httpRequest
	 */
	public SelectFieldForConditionURLGenerator(String classCompleteName, IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest, String classPrefix){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
		
		this.className = classCompleteName;
		if (classPrefix == null){
			classPrefix = "a";
		}
		if (classCompleteName.indexOf(".") > 0){
			aliasedClassName = classPrefix + classCompleteName.substring(classCompleteName.lastIndexOf(".")+1);
		}else{
			aliasedClassName = classPrefix + classCompleteName;
		}
	}
	
	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	public String generateURL(Object source) {
		

		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_WHERE_ACTION");
		params.put("COMPLETE_FIELD_NAME", aliasedClassName + "."+source.toString());
		params.put("CLASS_NAME", this.className);
		params.put("ALIAS_CLASS_NAME",this.aliasedClassName);
	
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
	
	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object addtionalParameter) {
		StringBuffer sb = new StringBuffer();
		sb.append("javascript: selectFieldForConditionCallBack(");
		sb.append("\\'SELECT_FIELD_FOR_WHERE_ACTION\\',");
		sb.append("\\'"+aliasedClassName + "."+source.toString()+"\\',");
		sb.append("\\'"+this.className+"\\',");
		sb.append("\\'"+addtionalParameter.toString()+"\\'");
		sb.append(");");
		
		return sb.toString();
	}
	
	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object source2, Object addtionalParameter) {
		return generateURL(source, addtionalParameter);
	}

	public String getClassPrefix() {
		return classPrefix;
	}

	public void setClassPrefix(String classPrefix) {
		this.classPrefix = classPrefix;
	}

	public String getClassName() {
		return className;
	}
	
}
