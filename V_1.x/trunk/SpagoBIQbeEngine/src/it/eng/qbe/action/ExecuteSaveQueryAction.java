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
package it.eng.qbe.action;

import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.locale.IQbeMessageHelper;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IQuery;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereField;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;

import org.hibernate.HibernateException;


/**
 * @author Andrea Zoppello
 * 
 * This action do the execution of the query represented by ISingleDataMartWizardObject in session
 * 
 * If ISingleDataMartWizardObject is configured to run the query composed automatically this action 
 * do some control on join conditions.
 *  
 */
public class ExecuteSaveQueryAction extends AbstractAction {
	
	public static String QUERY_RESPONSE_SOURCE_BEAN = "QUERY_RESPONSE_SOURCE_BEAN"; 
		
	
	public boolean checkJoins(SourceBean request, SourceBean response) throws SourceBeanException {
		if (isCheckJoinsEnabled(request)){
			if (!getDataMartWizard().isUseExpertedVersion()){
				// If I'm not using expert
				// Check for join controls
				return doCheckJoins((IQuery)getDataMartWizard(), response);				
			}
		}
		return true;
	}
	
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
		
		msgHelper = QbeConf.getInstance().getQbeMessageHelper();
		
		
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
		//return (ISingleDataMartWizardObject)getSessionContainer().getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		return Utils.getWizardObject(getSessionContainer());
	}
	
	private DataMartModel getDataMartModel() {
		return (DataMartModel)getSessionContainer().getAttribute("dataMartModel");
	}
		
	private String getExecutionMode(SourceBean request) {
		return (String)request.getAttribute("previewModeFromQueryResult"); 
	}
	
	private boolean isExpertExecutionModeEnabled(SourceBean request) {
		return ("ExpertMode".equalsIgnoreCase(getExecutionMode(request)));
	}
	
	private boolean isCheckJoinsEnabled(SourceBean request) {
		String ignoreJoins = (String) request.getAttribute("ignoreJoins");
		return (ignoreJoins == null) || (!(ignoreJoins.equalsIgnoreCase("true")));
	}
	
	private String getSource(SourceBean request) {
		return (String)request.getAttribute("SOURCE_FROM_QUERY_RESULT");
	}
	
	private int getPageNumber(SourceBean request) {
		String pageNumberString = (String) request.getAttribute("pageNumber");
        int pageNumber = 0;
        if(pageNumberString == null || pageNumberString.length() == 0) {
        	pageNumber = 0;
        } else {
            try {
            	pageNumber = Integer.parseInt(pageNumberString);
            } catch (NumberFormatException nfe) {
            	Logger.error(ExecuteSaveQueryAction.class, nfe);
                pageNumber = 0;
            }
        }
        return pageNumber;
	}
	
	private int getPageSize() {
		String pageSizeStr = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.page-size");
		int pageSize = 30;
		if (pageSizeStr != null) pageSize = new Integer(pageSizeStr).intValue();
		return pageSize;
	}
	
	private void returnError(SourceBean response, String errorMsg) {
		if (getSessionContainer().getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
			getSessionContainer().delAttribute(QUERY_RESPONSE_SOURCE_BEAN);
		}	
		try {
			response.setAttribute("ERROR_MSG", errorMsg);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
	}
	
	public void service(SourceBean request, SourceBean response) throws Exception {				
		
		if ((getDataMartWizard().getSelectClause() != null) && (getDataMartWizard().getSelectClause().getSelectFields().size() > 0)){
			
		
			if ("QUERY_RESULT".equalsIgnoreCase( getSource(request) )){			
			
		
				getDataMartWizard().setUseExpertedVersion( isExpertExecutionModeEnabled(request) );			
			}  				
		
			getDataMartWizard().composeQuery(getDataMartModel());	
	
			if (!checkJoins(request, response)){
				returnError(response, "QBE.Warning.Join");
			} 
			else{
				try {
					if (getDataMartWizard().getExpertQueryDisplayed() == null){
						
						getDataMartWizard().setExpertQueryDisplayed(getDataMartWizard().getFinalSqlQuery(getDataMartModel()));
					}
					SourceBean queryResponseSourceBean = getDataMartWizard().executeQuery(getDataMartModel(), getPageNumber(request), this.getPageSize());
					getSessionContainer().setAttribute(QUERY_RESPONSE_SOURCE_BEAN, queryResponseSourceBean);
				}catch (HibernateException he) {
					Logger.error(ExecuteSaveQueryAction.class, he);					
					returnError(response, he.getLocalizedMessage() + "\n" + he.getCause().getLocalizedMessage());
				}catch (java.sql.SQLException se) {
					Logger.error(ExecuteSaveQueryAction.class, se);
					returnError(response, se.getLocalizedMessage());
				}catch(Exception e){
					Logger.error(ExecuteSaveQueryAction.class, e);
					returnError(response, e.getLocalizedMessage());					
				}
		}//else
		}else{
			IQbeMessageHelper qbeMsg = QbeConf.getInstance().getQbeMessageHelper();
			String bundle =  "component_spagobiqbeIE_messages";
			returnError(response, qbeMsg.getMessage(getRequestContainer(), "QBE.Error.ImpossibleExecution", bundle));	
		}
	}//service
}
