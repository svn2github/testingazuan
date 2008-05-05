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
package it.eng.spagobi.qbe.queryresultshandler.service;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.locale.IQbeMessageHelper;
import it.eng.qbe.log.Logger;
import it.eng.qbe.query.IQuery;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import org.hibernate.HibernateException;


// TODO: Auto-generated Javadoc
/**
 * This action do the execution of the query represented by ISingleDataMartWizardObject in session
 * 
 * If ISingleDataMartWizardObject is configured to run the query composed automatically this action
 * do some control on join conditions.
 */
public class ExecuteQueryAction extends AbstractQbeEngineAction {
	
	/** The QUER y_ respons e_ sourc e_ bean. */
	public static String QUERY_RESPONSE_SOURCE_BEAN = "QUERY_RESPONSE_SOURCE_BEAN"; 
		
	
	/**
	 * Check joins.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @return true, if successful
	 * 
	 * @throws SourceBeanException the source bean exception
	 */
	public boolean checkJoins(SourceBean request, SourceBean response) throws SourceBeanException {
		if (isCheckJoinsEnabled(request)){
			if (!getDatamartWizard().isUseExpertedVersion()){
				// If I'm not using expert
				// Check for join controls
				return doCheckJoins(getDatamartWizard().getQuery(), response);				
			}
		}
		return true;
	}
	
	/**
	 * Do check joins.
	 * 
	 * @param query the query
	 * @param serviceResponse the service response
	 * 
	 * @return true, if do check joins
	 * 
	 * @throws SourceBeanException the source bean exception
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
	
	/**
	 * Gets the session container.
	 * 
	 * @return the session container
	 */
	private SessionContainer getSessionContainer() {
		return getRequestContainer().getSessionContainer();
	}
	
	
	
	
		
	/**
	 * Gets the execution mode.
	 * 
	 * @param request the request
	 * 
	 * @return the execution mode
	 */
	private String getExecutionMode(SourceBean request) {
		return (String)request.getAttribute("previewModeFromQueryResult"); 
	}
	
	/**
	 * Checks if is expert execution mode enabled.
	 * 
	 * @param request the request
	 * 
	 * @return true, if is expert execution mode enabled
	 */
	private boolean isExpertExecutionModeEnabled(SourceBean request) {
		return ("ExpertMode".equalsIgnoreCase(getExecutionMode(request)));
	}
	
	/**
	 * Checks if is check joins enabled.
	 * 
	 * @param request the request
	 * 
	 * @return true, if is check joins enabled
	 */
	private boolean isCheckJoinsEnabled(SourceBean request) {
		String ignoreJoins = (String) request.getAttribute("ignoreJoins");
		return (ignoreJoins == null) || (!(ignoreJoins.equalsIgnoreCase("true")));
	}
	
	/**
	 * Gets the source.
	 * 
	 * @param request the request
	 * 
	 * @return the source
	 */
	private String getSource(SourceBean request) {
		return (String)request.getAttribute("SOURCE_FROM_QUERY_RESULT");
	}
	
	/**
	 * Gets the page number.
	 * 
	 * @param request the request
	 * 
	 * @return the page number
	 */
	private int getPageNumber(SourceBean request) {
		String pageNumberString = (String) request.getAttribute("pageNumber");
        int pageNumber = 0;
        if(pageNumberString == null || pageNumberString.length() == 0) {
        	pageNumber = 0;
        } else {
            try {
            	pageNumber = Integer.parseInt(pageNumberString);
            } catch (NumberFormatException nfe) {
            	Logger.error(ExecuteQueryAction.class, nfe);
                pageNumber = 0;
            }
        }
        return pageNumber;
	}
	
	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	private int getPageSize() {
		String pageSizeStr = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.page-size");
		int pageSize = 30;
		if (pageSizeStr != null) pageSize = new Integer(pageSizeStr).intValue();
		return pageSize;
	}
	
	/**
	 * Return error.
	 * 
	 * @param response the response
	 * @param errorMsg the error msg
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException  {				
		super.service(request, response);	
		
		if (!getQuery().isEmpty()){
			
		
			if ("QUERY_RESULT".equalsIgnoreCase( getSource(request) )){			
			
		
				getDatamartWizard().setUseExpertedVersion( isExpertExecutionModeEnabled(request) );			
			}  				
		
			getDatamartWizard().composeQuery(getDatamartModel());	
	
			try {
				if (!checkJoins(request, response)){
					returnError(response, "QBE.Warning.Join");
				} 
				else{
					try {
						if (getDatamartWizard().getExpertQueryDisplayed() == null){
							
							getDatamartWizard().setExpertQueryDisplayed(getDatamartWizard().getFinalSqlQuery(getDatamartModel()));
						}
						SourceBean queryResponseSourceBean = getDatamartWizard().executeQuery(getDatamartModel(), getPageNumber(request), this.getPageSize());
						getSessionContainer().setAttribute(QUERY_RESPONSE_SOURCE_BEAN, queryResponseSourceBean);
					}catch (HibernateException he) {
						Logger.error(ExecuteQueryAction.class, he);					
						returnError(response, he.getLocalizedMessage() + "\n" + he.getCause().getLocalizedMessage());
					}catch (java.sql.SQLException se) {
						Logger.error(ExecuteQueryAction.class, se);
						returnError(response, se.getLocalizedMessage());
					}catch(Exception e){
						Logger.error(ExecuteQueryAction.class, e);
						returnError(response, e.getLocalizedMessage());					
					}
}
			} catch (SourceBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//else
		}else{
			IQbeMessageHelper qbeMsg = QbeEngineConf.getInstance().getQbeMessageHelper();
			String bundle =  "component_spagobiqbeIE_messages";
			returnError(response, qbeMsg.getMessage(getRequestContainer(), "QBE.Error.ImpossibleExecution", bundle));	
		}
	}//service
}
