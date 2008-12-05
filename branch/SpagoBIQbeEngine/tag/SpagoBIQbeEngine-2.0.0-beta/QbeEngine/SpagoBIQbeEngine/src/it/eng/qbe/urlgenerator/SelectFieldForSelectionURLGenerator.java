/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.urlgenerator;

import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.querybuilder.select.service.AddCalculatedFieldAction;
import it.eng.spagobi.qbe.querybuilder.select.service.AddSelectFieldAction;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectFieldForSelectionURLGenerator.
 * 
 * @author Zoppello
 */
public class SelectFieldForSelectionURLGenerator implements IURLGenerator{

	/** The class name. */
	private String className = null;
		
	
	/** The qbe url generator. */
	private IQbeUrlGenerator qbeUrlGenerator = null;
	
	/** The http request. */
	private HttpServletRequest httpRequest = null;
	
	/**
	 * Instantiates a new select field for selection url generator.
	 * 
	 * @param qbeUrlGenerator the qbe url generator
	 * @param httpRequest the http request
	 */
	public SelectFieldForSelectionURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
	}
	
	/**
	 * Instantiates a new select field for selection url generator.
	 * 
	 * @param className the class name
	 * @param qbeUrlGenerator the qbe url generator
	 * @param httpRequest the http request
	 */
	public SelectFieldForSelectionURLGenerator(String className, IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
		this.className = className;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.urlgenerator.IURLGenerator#generateURL(java.lang.Object)
	 */
	public String generateURL(Object fieldName) {
		
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");
		
		params.put(AddSelectFieldAction.CLASS_NAME, className);
		params.put(AddSelectFieldAction.FIELD_NAME, (String)fieldName);
			
		return qbeUrlGenerator.getActionUrl(httpRequest, params);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.urlgenerator.IURLGenerator#generateURL(java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object addtionalParameter) {
		return generateURL(source);
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.urlgenerator.IURLGenerator#generateURL(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object fieldName, Object fieldLabel, Object additionalParameters) {
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");		
		
		
		params.put(AddSelectFieldAction.CLASS_NAME, className);
		params.put(AddSelectFieldAction.FIELD_NAME, (String)fieldName);
		params.put(AddSelectFieldAction.FIELD_LABEL, fieldLabel);	
		
		if ( additionalParameters != null){
			String additionalParametersString = (String)additionalParameters;
			String[] addPars = additionalParametersString.split(";");
			if (addPars.length == 3){
				params.put(AddSelectFieldAction.FIELD_HIBTYPE, addPars[0]);	
				params.put(AddSelectFieldAction.FIELD_HIBSCALE, addPars[1]);
				params.put(AddSelectFieldAction.FIELD_HIBPREC, addPars[2]);
			}
		}
		
		
		return qbeUrlGenerator.getActionUrl(httpRequest, params);
	}
	
	/**
	 * Generate url for calculate field.
	 * 
	 * @param calculatedFieldId the calculated field id
	 * @param entityName the entity name
	 * @param cFieldCompleteName the c field complete name
	 * 
	 * @return the string
	 */
	public String generateURLForCalculateField(String calculatedFieldId, String entityName, String cFieldCompleteName){
		Map params = new HashMap();
		params.put("ACTION_NAME","SELECT_CALC_FIELD_ACTION");
		
		params.put(AddCalculatedFieldAction.CLASS_NAME, className);
		params.put(AddCalculatedFieldAction.CFIELD_ID, calculatedFieldId);
		params.put(AddCalculatedFieldAction.CFIELD_COMPLETE_NAME, cFieldCompleteName);
		return qbeUrlGenerator.getActionUrl(httpRequest, params);
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.urlgenerator.IURLGenerator#getClassName()
	 */
	public String getClassName() {
		return className;
	}
}
