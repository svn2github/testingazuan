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

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.qbe.commons.constants.QbeConstants;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public abstract class AbstractQbeEngineAction extends AbstractBaseHttpAction {
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractQbeEngineAction.class);
    
	
	
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);
	}
	
	

	public void setUserId(String userId) {
		setAttributeInSession(QbeConstants.USER_ID, userId);
	}
	
	public String getUserId() {
		return (String)getAttributeFromSession(QbeConstants.USER_ID);
	}
	
	public Locale getQbeEngineLocale() {
		return  (Locale)getAttributeFromSession(QbeConstants.LOCALE);
	}

	public void setQbeEngineLocale(Locale locale) {
		setAttributeInSession(QbeConstants.LOCALE, locale);
	}

	
	public DataMartModel getDatamartModel() {
		return  (DataMartModel)getAttributeFromSession(QbeConstants.DATAMART_MODEL);
	}

	public void setDatamartModel(DataMartModel datamartModel) {
		setAttributeInSession(QbeConstants.DATAMART_MODEL, datamartModel);
	}
	
	public ISingleDataMartWizardObject getDatamartWizard() {
		return  (ISingleDataMartWizardObject)getAttributeFromSession(QbeConstants.DATAMART_WIZARD);
	}

	public void setDatamartWizard(ISingleDataMartWizardObject datamartWizard) {
		setAttributeInSession(QbeConstants.DATAMART_WIZARD, datamartWizard);
	}	
	
	public IQuery getQuery() {
		return getDatamartWizard().getQuery();
	}
	
	/*
	public IQuery getActiveQuery() {
		IQuery activeQuery = null;
		ISingleDataMartWizardObject mainDataMartWizard = null;		
		
		mainDataMartWizard = getDatamartWizard();
		
		if (isSubqueryModeActive()){
			activeQuery =  mainDataMartWizard.getQuery().getSelectedSubquery();
		} else {
			activeQuery = mainDataMartWizard.getQuery();
		}
		
		return activeQuery;
	}
	*/
	
	public void setStandaloneModeActive(boolean standaloneMode) {
		if(standaloneMode) {
			delAttributeFromSession(QbeConstants.STANDALONE_MODE);
		} else {
			setAttributeInSession(QbeConstants.STANDALONE_MODE, FALSE);
			
		}
	}
	
	public boolean isStandaloneModality() {
		String standaloneMode = (String)getAttributeFromSession(QbeConstants.STANDALONE_MODE);
		return (standaloneMode == null || !standaloneMode.equalsIgnoreCase(FALSE));
	}
	
	
	
	
	
	public void updateLastUpdateTimeStamp(){
		String str = String.valueOf(System.currentTimeMillis());
		logger.debug("Last Update Timestamp [" + str + "]");
		setAttributeInSession(QbeConstants.LAST_UPDATE_TIMESTAMP, str);
	}
	
	
	
	
	
	
	public SpagoBIRequest getSpagoBIRequest() {
		return  (SpagoBIRequest)getAttributeFromSession(QbeConstants.SPAGOBI_REQUEST);
	}

	public void setSpagoBIRequest(SpagoBIRequest spagoBIRequest) {
		setAttributeInSession(QbeConstants.SPAGOBI_REQUEST, spagoBIRequest);	
	}
	
	public void setSubqueryModeActive(boolean subqueryMode) {
		if(subqueryMode) {
			setAttributeInSession(QbeConstants.QUERY_MODE, QbeConstants.SUBQUERY_MODE);
		} else {
			delAttributeFromSession(QbeConstants.QUERY_MODE);
		}
		
		//getQuery().setSubqueryModeActive(subqueryMode);
		//getActiveQuery().setSubqueryModeActive(subqueryMode);
	}
	
	public boolean isSubqueryModeActive() {
		String qbeQueryMode = (String)getAttributeFromSession(QbeConstants.QUERY_MODE);
		return (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase(QbeConstants.SUBQUERY_MODE));
	}	
	
	public String getSubqueryField() {
		return  (String)getAttributeFromSession(QbeConstants.SUBQUERY_FIELD);
	}

	public void setSubqueryField(String field) {
		setAttributeInSession(QbeConstants.SUBQUERY_FIELD, field);
	}
	
	public void delSubqueryField() {
		delAttributeFromSession(QbeConstants.SUBQUERY_FIELD);
	}
}
