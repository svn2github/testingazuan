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
package it.eng.spagobi.qbe.tree.urlgenerator;

import it.eng.qbe.model.structure.DataMartField;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.querybuilder.select.service.AddCalculatedFieldAction;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeTreeUrlGenerator.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QbeTreeUrlGenerator implements IQbeTreeUrlGenerator {

	/** The url generator. */
	private IQbeUrlGenerator urlGenerator;
	
	/** The http request. */
	private HttpServletRequest httpRequest;
	
	/** The action name. */
	private String actionName; // "SELECT_FIELD_FOR_SELECT_ACTION"
	
	/** The action type. */
	private String actionType;
	

	/**
	 * Instantiates a new qbe tree url generator.
	 * 
	 * @param actionName the action name
	 * @param actionType the action type
	 * @param urlGenerator the url generator
	 * @param httpRequest the http request
	 */
	public QbeTreeUrlGenerator(String actionName, String actionType, IQbeUrlGenerator urlGenerator, HttpServletRequest httpRequest) {
		setUrlGenerator( urlGenerator );
		setHttpRequest( httpRequest );
		setActionName(actionName);
		setActionType( actionType );
	}	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.urlgenerator.IQbeTreeUrlGenerator#getActionUrl(it.eng.qbe.model.structure.DataMartField)
	 */
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
	
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.urlgenerator.IQbeTreeUrlGenerator#getActionUrlForCalculateField(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getActionUrlForCalculateField(String calculatedFieldId, String entityName, String cFieldCompleteName){
		Map params = new HashMap();
		params.put("ACTION_NAME","SELECT_CALC_FIELD_ACTION");
		
		String className = null; // field.getParent().getRoot().getType();
		params.put(AddCalculatedFieldAction.CLASS_NAME, className);
		params.put(AddCalculatedFieldAction.CFIELD_ID, calculatedFieldId);
		params.put(AddCalculatedFieldAction.CFIELD_COMPLETE_NAME, cFieldCompleteName);
		return urlGenerator.getActionUrl(httpRequest, params);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.urlgenerator.IQbeTreeUrlGenerator#getResourceUrl(java.lang.String)
	 */
	public String getResourceUrl(String url) {
		return urlGenerator.getResourceUrl(httpRequest, url);
	}

	/**
	 * Gets the url generator.
	 * 
	 * @return the url generator
	 */
	protected IQbeUrlGenerator getUrlGenerator() {
		return urlGenerator;
	}

	/**
	 * Sets the url generator.
	 * 
	 * @param urlGenerator the new url generator
	 */
	protected void setUrlGenerator(IQbeUrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}
	
	/**
	 * Gets the http request.
	 * 
	 * @return the http request
	 */
	protected HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	/**
	 * Sets the http request.
	 * 
	 * @param httpRequest the new http request
	 */
	protected void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}





	/**
	 * Gets the action name.
	 * 
	 * @return the action name
	 */
	protected String getActionName() {
		return actionName;
	}





	/**
	 * Sets the action name.
	 * 
	 * @param actionName the new action name
	 */
	protected void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * Gets the action type.
	 * 
	 * @return the action type
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * Sets the action type.
	 * 
	 * @param actionType the new action type
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


}
