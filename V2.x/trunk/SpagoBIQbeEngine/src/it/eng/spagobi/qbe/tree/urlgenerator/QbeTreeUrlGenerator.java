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
package it.eng.spagobi.qbe.tree.urlgenerator;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.querybuilder.select.service.AddCalculatedFieldAction;
import it.eng.spagobi.qbe.querybuilder.select.service.AddSelectFieldAction;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTreeUrlGenerator implements IQbeTreeUrlGenerator {

	private IQbeUrlGenerator urlGenerator;
	private HttpServletRequest httpRequest;
	private String actionName; // "SELECT_FIELD_FOR_SELECT_ACTION"
	private String actionType;
	

	public QbeTreeUrlGenerator(String actionName, String actionType, IQbeUrlGenerator urlGenerator, HttpServletRequest httpRequest) {
		setUrlGenerator( urlGenerator );
		setHttpRequest( httpRequest );
		setActionName(actionName);
		setActionType( actionType );
	}	
	
	public String getActionUrl(DataMartField field) {		
		String actionUrl = null;
		
		if( actionType.equalsIgnoreCase("action")) {
			Map params = new HashMap();					
			params.put("ACTION_NAME", getActionName());	
			params.put("FIELD_UNIQUE_NAME", field.getUniqueName());	
			actionUrl =  urlGenerator.getActionUrl(httpRequest, params);			
		} else if (actionType.equalsIgnoreCase("page")) {
			Map params = new HashMap();					
			params.put("PAGE_NAME", getActionName());	
			params.put("FIELD_UNIQUE_NAME", field.getUniqueName());	
			actionUrl =  urlGenerator.getActionUrl(httpRequest, params);					
		} else if(actionType.equalsIgnoreCase("script")) {
			StringBuffer sb = new StringBuffer();
			sb.append("javascript: " + getActionName() + "(");
			sb.append("\\'" + field.getUniqueName() + "\\'");			
			sb.append(");");			
			actionUrl = sb.toString();
		} else if(actionType.equalsIgnoreCase("url")) {
			StringBuffer sb = new StringBuffer();
			sb.append( getActionName() );
			sb.append( "?" );
			sb.append( "FIELD_UNIQUE_NAME" );
			sb.append( "=" );
			sb.append( field.getUniqueName() );
			actionUrl = sb.toString();
		} else {
			actionUrl = "javascript:void(0);";
		}
		
		
		
		/*
		if(getActionName().equalsIgnoreCase(AddSelectFieldAction.ACTION_NAME)) {		
			params.put(AddSelectFieldAction.FIELD_UNIQUE_NAME, field.getUniqueName());
			actionUrl =  urlGenerator.getActionUrl(httpRequest, params);
		} else {
			
			StringBuffer sb = new StringBuffer();
			sb.append("javascript: selectFieldForConditionCallBack(");
			sb.append("\\'" + field.getUniqueName() + "\\'");			
			sb.append(");");			
			actionUrl = sb.toString();
		}
		*/
			
		return actionUrl;
	}
	
	
	
	
	
	public String getActionUrlForCalculateField(String calculatedFieldId, String entityName, String cFieldCompleteName){
		Map params = new HashMap();
		params.put("ACTION_NAME","SELECT_CALC_FIELD_ACTION");
		
		String className = null; // field.getParent().getRoot().getType();
		params.put(AddCalculatedFieldAction.CLASS_NAME, className);
		params.put(AddCalculatedFieldAction.CFIELD_ID, calculatedFieldId);
		params.put(AddCalculatedFieldAction.CFIELD_COMPLETE_NAME, cFieldCompleteName);
		return urlGenerator.getActionUrl(httpRequest, params);
	}
	
	
	
	
	public String getResourceUrl(String url) {
		return urlGenerator.getResourceUrl(httpRequest, url);
	}

	protected IQbeUrlGenerator getUrlGenerator() {
		return urlGenerator;
	}

	protected void setUrlGenerator(IQbeUrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}
	
	protected HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	protected void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}





	protected String getActionName() {
		return actionName;
	}





	protected void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


}
