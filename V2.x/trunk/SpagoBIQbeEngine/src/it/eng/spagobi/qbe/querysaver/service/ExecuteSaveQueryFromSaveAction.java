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
package it.eng.spagobi.qbe.querysaver.service;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.locale.IQbeMessageHelper;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.queryresultshandler.service.ExecuteQueryAction;

import org.hibernate.HibernateException;


/**
 * This action do the execution of the query represented by ISingleDataMartWizardObject in session
 * 
 * If ISingleDataMartWizardObject is configured to run the query composed automatically this action 
 * do some control on join conditions.
 *  
 */
public class ExecuteSaveQueryFromSaveAction extends AbstractQbeEngineAction {
	
	
	/**
	 * 
	 * @param query
	 * @param serviceResponse
	 * @return
	 * @throws SourceBeanException
	 */
	public boolean doCheckJoins(IQuery query, SourceBean serviceResponse) throws SourceBeanException{
		String bundle = "component_spagobiqbeIE_messages";
		
		IQbeMessageHelper msgHelper = null;
		StringBuffer warning = new StringBuffer();
		
		msgHelper = QbeEngineConf.getInstance().getQbeMessageHelper();
		
		
		if (!query.areAllEntitiesJoined()){
			String locMsg = msgHelper.getMessage(getRequestContainer(), "QBE.Warning.Nojoin", bundle);			
			serviceResponse.setAttribute("JOIN_WARNINGS", locMsg);
			return false;
		}		
		
		return true;		
	}
	
	private SessionContainer getSessionContainer() {
		return getRequestContainer().getSessionContainer();
	}
	
	private ISingleDataMartWizardObject getDataMartWizard() {
		return Utils.getWizardObject(getSessionContainer());
	}
	
	private DataMartModel getDataMartModel() {
		return (DataMartModel)getSessionContainer().getAttribute("dataMartModel");
	}

	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);	
		
		if ( !getActiveQuery().isEmpty() ){
			
		
		getDataMartWizard().composeQuery(getDataMartModel());
		
		if ((getDataMartWizard().getExpertQueryDisplayed() == null)||(getDataMartWizard().getExpertQueryDisplayed().trim().length() == 0)){
			try{
				getDataMartWizard().setExpertQueryDisplayed(getDataMartWizard().getFinalSqlQuery(getDataMartModel()));
			}catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		boolean joinOk = false;
		try {
			joinOk = doCheckJoins(getDataMartWizard().getQuery(), response);
		} catch (SourceBeanException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (!joinOk){		
			setAttribute("ERROR_MSG_FINAL", "QBE.Warning.Join");
		} 
		else{
			try {
				getDataMartWizard().executeQbeQuery(getDataMartModel(), 0, 10);
				getDataMartWizard().executeExpertQuery(getDataMartModel(), 0, 10);
			}catch(HibernateException he) {				
				Logger.error(ExecuteSaveQueryFromSaveAction.class, he);				
				String causeMsg = he.getCause().getMessage();
				setAttribute("ERROR_MSG_FINAL", causeMsg);
			}catch (java.sql.SQLException se) {
				Logger.error(ExecuteQueryAction.class, se);
				String causeMsg = se.getMessage();
				setAttribute("ERROR_MSG_FINAL", causeMsg);
			}catch(Exception e){
				Logger.error(ExecuteSaveQueryFromSaveAction.class, e);
				setAttribute("ERROR_MSG_FINAL", e.getMessage());					
			}					
		}
		}else{
			IQbeMessageHelper qbeMsg = QbeEngineConf.getInstance().getQbeMessageHelper();
			String bundle =  "component_spagobiqbeIE_messages";
			setAttribute("ERROR_MSG_FINAL", qbeMsg.getMessage(getRequestContainer(), "QBE.Error.ImpossibleExecution", bundle));	
		}
	}//service
}
