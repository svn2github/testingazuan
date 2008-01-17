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
package it.eng.spagobi.qbe.commons.service;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import it.eng.qbe.action.SpagoBIStartAction;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IQuery;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.dispatching.action.AbstractHttpAction;

/**
 * @author Andrea Gioia
 *
 */
public abstract class AbstractQbeEngineAction extends AbstractHttpAction {
	
	private SourceBean request;
	private SourceBean response;
	
	public static final String QBE_ENGINE_LOCALE = "QBE_ENGINE_LOCALE";
	public static final String QBE_SPAGOBI_REQUEST = "QBE_SPAGOBI_REQUEST";
	public static final String QBE_DATAMART_MODEL = "dataMartModel";
	private static final String QBE_QUERY = WizardConstants.SINGLE_DATA_MART_WIZARD;
	private static final String QBE_QUERY_MODE = WizardConstants.QUERY_MODE;
	private static final String QBE_SUBQUERY_MODE = WizardConstants.SUBQUERY_MODE;
	private static final String QBE_SUBQUERY_FIELD =  WizardConstants.SUBQUERY_FIELD;
	private static final String QBE_LAST_UPDATE_TIMESTAMP =  "QBE_LAST_UPDATE_TIMESTAMP";

	private static final String QBE_USER_ID = "userId";
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractQbeEngineAction.class);
	
	
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		this.request = request;
		this.response = response;
	}
	
	public HttpSession getHttpSession() {
		return getHttpRequest().getSession();
	}
	
	public SessionContainer getSession() {
		return getRequestContainer().getSessionContainer();
	}
	
	public Object getAttributeFromSession(String attrName) {
		return getRequestContainer().getSessionContainer().getAttribute(attrName);
	}
	
	public void delAttributeFromSession(String attrName) {
		if(getAttributeFromSession(attrName) != null) {
			getRequestContainer().getSessionContainer().delAttribute(attrName);
		}
	}
		
	public void setAttributeInSession(String attrName, Object attrValue) {
		delAttributeFromSession(attrName);
		getRequestContainer().getSessionContainer().setAttribute(attrName, attrValue);
	}	
	
	public String getAttributeAsString(String attrName) {
		return (String)request.getAttribute(attrName);
	}

	public Locale getQbeEngineLocale() {
		return  (Locale)getAttributeFromSession(QBE_ENGINE_LOCALE);
	}

	public void setQbeEngineLocale(Locale locale) {
		setAttributeInSession(QBE_ENGINE_LOCALE, locale);
	}
	
	public SpagoBIRequest getSpagoBIRequest() {
		return  (SpagoBIRequest)getAttributeFromSession(QBE_SPAGOBI_REQUEST);
	}

	public void setSpagoBIRequest(SpagoBIRequest spagoBIRequest) {
		setAttributeInSession(QBE_SPAGOBI_REQUEST, spagoBIRequest);
	}
	
	public DataMartModel getDatamartModel() {
		return  (DataMartModel)getAttributeFromSession(QBE_DATAMART_MODEL);
	}

	public void setDatamartModel(DataMartModel datamartModel) {
		setAttributeInSession(QBE_DATAMART_MODEL, datamartModel);
	}
	
	
	public void setSubqueryModeActive(boolean subqueryMode) {
		if(subqueryMode) {
			setAttributeInSession(QBE_QUERY_MODE, QBE_SUBQUERY_MODE);
		} else {
			delAttributeFromSession(QBE_QUERY_MODE);
		}
	}
	
	public boolean isSubqueryModeActive() {
		String qbeQueryMode = (String)getAttributeFromSession(QBE_QUERY_MODE);
		return (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase(QBE_SUBQUERY_MODE));
	}
	
	public ISingleDataMartWizardObject getMainDataMartWizard() {
		return  (ISingleDataMartWizardObject)getAttributeFromSession(QBE_QUERY);
	}

	public void setMainDataMartWizard(ISingleDataMartWizardObject dataMartWizard) {
		setAttributeInSession(QBE_QUERY, dataMartWizard);
	}	
	
	public ISingleDataMartWizardObject getActiveDataMartWizard(){
		ISingleDataMartWizardObject activeDataMartWizard = null;
		ISingleDataMartWizardObject mainDataMartWizard = null;		
		
		mainDataMartWizard = getMainDataMartWizard();
		
		if (isSubqueryModeActive()){
			activeDataMartWizard =  (ISingleDataMartWizardObject)mainDataMartWizard.getSelectedSubquery();
		} else {
			activeDataMartWizard = mainDataMartWizard;
		}
		
		return activeDataMartWizard;
	}
	
	public IQuery getActiveQuery() {
		return getActiveDataMartWizard().getQuery();
	}
	
	public String getSubqueryField() {
		return  (String)getAttributeFromSession(QBE_SUBQUERY_FIELD);
	}

	public void setSubqueryField(String field) {
		setAttributeInSession(QBE_SUBQUERY_FIELD, field);
	}	
	
	public void setUserId(String userId) {
		setAttributeInSession(QBE_USER_ID, userId);
	}
	
	public String getUserId() {
		return (String)getAttributeFromSession(QBE_USER_ID);
	}
	
	public void updateLastUpdateTimeStamp(){
		String str = String.valueOf(System.currentTimeMillis());
		logger.debug("Last Update Timestamp [" + str + "]");
		setAttributeInSession(QBE_LAST_UPDATE_TIMESTAMP, str);
	}
}
