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
package it.eng.spagobi.qbe.commons.service;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spagobi.qbe.QbeEngineInstance;
import it.eng.spagobi.qbe.commons.constants.QbeConstants;
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.engines.EngineConstants;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractQbeEngineAction.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public abstract class AbstractQbeEngineAction extends AbstractEngineAction {
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(AbstractQbeEngineAction.class);
    
	
    public QbeEngineInstance getEngineInstance() {
    	return (QbeEngineInstance)getAttributeFromSession( EngineConstants.ENGINE_INSTANCE );
    }
    
	

	
	/**
	 * Gets the datamart model.
	 * 
	 * @return the datamart model
	 */
	public DataMartModel getDatamartModel() {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return null;
    	}
    	return qbeEngineInstance.getDatamartModel();
	}

	/**
	 * Sets the datamart model.
	 * 
	 * @param datamartModel the new datamart model
	 */
	public void setDatamartModel(DataMartModel datamartModel) {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return;
    	}
    	qbeEngineInstance.setDatamartModel(datamartModel);
	}
	
	/**
	 * Gets the datamart wizard.
	 * 
	 * @return the datamart wizard
	 */
	public ISingleDataMartWizardObject getDatamartWizard() {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return null;
    	}
    	return qbeEngineInstance.getDatamartWizard();
	}

	/**
	 * Sets the datamart wizard.
	 * 
	 * @param datamartWizard the new datamart wizard
	 */
	public void setDatamartWizard(ISingleDataMartWizardObject datamartWizard) {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return;
    	}
    	qbeEngineInstance.setDatamartWizard(datamartWizard);
	}	
	
	/**
	 * Gets the query.
	 * 
	 * @return the query
	 */
	public IQuery getQuery() {
		return getDatamartWizard().getQuery();
	}
	
	
	/**
	 * Sets the standalone mode active.
	 * 
	 * @param standaloneMode the new standalone mode active
	 */
	public void setStandaloneModeActive(boolean standaloneMode) {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return;
    	}
    	qbeEngineInstance.setStandaloneMode(standaloneMode);
	}
	
	/**
	 * Checks if is standalone modality.
	 * 
	 * @return true, if is standalone modality
	 */
	public boolean isStandaloneModality() {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return false;
    	}
    	return qbeEngineInstance.isStandaloneMode();
	}
	
	
	
	
	
	/**
	 * Update last update time stamp.
	 */
	public void updateLastUpdateTimeStamp(){
		String str = String.valueOf(System.currentTimeMillis());
		logger.debug("Last Update Timestamp [" + str + "]");
		setAttributeInSession(QbeConstants.LAST_UPDATE_TIMESTAMP, str);
	}

	/**
	 * Sets the subquery mode active.
	 * 
	 * @param subqueryMode the new subquery mode active
	 */
	public void setSubqueryModeActive(boolean subqueryMode) {
		if(subqueryMode) {
			setAttributeInSession(QbeConstants.QUERY_MODE, QbeConstants.SUBQUERY_MODE);
		} else {
			delAttributeFromSession(QbeConstants.QUERY_MODE);
		}
		
		//getQuery().setSubqueryModeActive(subqueryMode);
		//getActiveQuery().setSubqueryModeActive(subqueryMode);
	}
	
	/**
	 * Checks if is subquery mode active.
	 * 
	 * @return true, if is subquery mode active
	 */
	public boolean isSubqueryModeActive() {
		String qbeQueryMode = (String)getAttributeFromSession(QbeConstants.QUERY_MODE);
		return (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase(QbeConstants.SUBQUERY_MODE));
	}	
	
	/**
	 * Gets the subquery field.
	 * 
	 * @return the subquery field
	 */
	public String getSubqueryField() {
		return  (String)getAttributeFromSession(QbeConstants.SUBQUERY_FIELD);
	}

	/**
	 * Sets the subquery field.
	 * 
	 * @param field the new subquery field
	 */
	public void setSubqueryField(String field) {
		setAttributeInSession(QbeConstants.SUBQUERY_FIELD, field);
	}
	
	/**
	 * Del subquery field.
	 */
	public void delSubqueryField() {
		delAttributeFromSession(QbeConstants.SUBQUERY_FIELD);
	}
	
	
}
