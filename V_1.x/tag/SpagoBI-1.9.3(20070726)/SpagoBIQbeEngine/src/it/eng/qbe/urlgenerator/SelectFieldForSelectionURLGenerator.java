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

import it.eng.qbe.action.SelectCalculatedFieldForSelectAction;
import it.eng.qbe.action.SelectFieldForSelectAction;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zoppello
 */
public class SelectFieldForSelectionURLGenerator implements IURLGenerator{

	/** 
	 * @see it.eng.qbe.utility.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	private String className = null;
		
	
	private IQbeUrlGenerator qbeUrlGenerator = null;
	private HttpServletRequest httpRequest = null;
	
	public SelectFieldForSelectionURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
	}
	
	public SelectFieldForSelectionURLGenerator(String className, IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
		this.className = className;
	}
	
	public String generateURL(Object fieldName) {
		
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");
		
		params.put(SelectFieldForSelectAction.CLASS_NAME, className);
		params.put(SelectFieldForSelectAction.FIELD_NAME, (String)fieldName);
			
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
	
	public String generateURL(Object source, Object addtionalParameter) {
		return generateURL(source);
	}

	public String generateURL(Object fieldName, Object fieldLabel, Object additionalParameters) {
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");		
		
		
		params.put(SelectFieldForSelectAction.CLASS_NAME, className);
		params.put(SelectFieldForSelectAction.FIELD_NAME, (String)fieldName);
		params.put(SelectFieldForSelectAction.FIELD_LABEL, fieldLabel);	
		
		if ( additionalParameters != null){
			String additionalParametersString = (String)additionalParameters;
			String[] addPars = additionalParametersString.split(";");
			if (addPars.length == 3){
				params.put(SelectFieldForSelectAction.FIELD_HIBTYPE, addPars[0]);	
				params.put(SelectFieldForSelectAction.FIELD_HIBSCALE, addPars[1]);
				params.put(SelectFieldForSelectAction.FIELD_HIBPREC, addPars[2]);
			}
		}
		
		
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
	
	public String generateURLForCalculateField(String calculatedFieldId, String entityName, String cFieldCompleteName){
		Map params = new HashMap();
		params.put("ACTION_NAME","SELECT_CALC_FIELD_ACTION");
		
		params.put(SelectCalculatedFieldForSelectAction.CLASS_NAME, className);
		params.put(SelectCalculatedFieldForSelectAction.CFIELD_ID, calculatedFieldId);
		params.put(SelectCalculatedFieldForSelectAction.CFIELD_COMPLETE_NAME, cFieldCompleteName);
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}

	public String getClassName() {
		return className;
	}
}
