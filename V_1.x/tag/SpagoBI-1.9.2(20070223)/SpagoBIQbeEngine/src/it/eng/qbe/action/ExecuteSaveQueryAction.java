
package it.eng.qbe.action;

import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.IQbeMessageHelper;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


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
				return doCheckJoins(getDataMartWizard(), response);				
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param wizObj
	 * @param serviceResponse
	 * @return
	 * @throws SourceBeanException
	 */
	public boolean doCheckJoins(ISingleDataMartWizardObject wizObj, SourceBean serviceResponse) throws SourceBeanException{
		String bundle = "component_spagobiqbeIE_messages";
		
		IQbeMessageHelper msgHelper = null;
		StringBuffer warning = new StringBuffer();
		
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");   
		
		String msgHelperClass = null;
		if (qbeMode.equalsIgnoreCase("WEB")){
			
			msgHelperClass = "it.eng.qbe.utility.QbeWebMessageHelper";
		}else{
			msgHelperClass = "it.eng.qbe.utility.QbeSpagoBIMessageHelper";
		}
		
		try{
			msgHelper = (IQbeMessageHelper)Class.forName(msgHelperClass).newInstance();
		}catch (Exception e) {
			Logger.error(ExecuteSaveQueryAction.class, e);
		}
		
		wizObj.getEntityClasses();
		
		boolean warnings = false;
		if (wizObj.getEntityClasses().size() == 1){
			return true;
		}else{
			for (int i =0; i < wizObj.getEntityClasses().size(); i++ ){
				EntityClass e1 = (EntityClass)wizObj.getEntityClasses().get(i);
				
				for (int j = i+1; j < wizObj.getEntityClasses().size(); j++ ){
					EntityClass e2 = (EntityClass)wizObj.getEntityClasses().get(j);
					Logger.debug(ExecuteSaveQueryAction.class, "Check if Join Exist between ["+e1+"] and ["+e2+"]");
					
					if (e1.getClassName() != e2.getClassName()){
						boolean joinFound = false;
						if (wizObj.getWhereClause() != null){
							for (int k=0; ((k < wizObj.getWhereClause().getWhereFields().size()) && !joinFound); k++){
								IWhereField wf = (IWhereField)wizObj.getWhereClause().getWhereFields().get(k);
								
								if (wf.getFieldOperator().equalsIgnoreCase("=")){
									joinFound = (
										(wf.getFieldName().startsWith(e1.getClassAlias()) &&  wf.getFieldValue().startsWith(e2.getClassAlias()))
										||
										(wf.getFieldName().startsWith(e2.getClassAlias())&&  wf.getFieldValue().startsWith(e1.getClassAlias()))
									);
								}
							}
						}//if (wizObj.getWhereClause() != null){
						if (!joinFound){
							warnings = true;
							String locMsg = msgHelper.getMessage(getRequestContainer(), "QBE.Warning.Nojoin", bundle);
							warning.append( locMsg + e1.getClassName() +"," + e2.getClassName() + "\n");
						}
					}//if (e1.getClassName() != e2.getClassName()){
				}//for (int j =0; j < wizObj.getEntityClasses().size(); j++ )
			}//for (int i =0; i < wizObj.getEntityClasses().size(); i++ )
		}//else{
		
		if (warnings){
			serviceResponse.setAttribute("JOIN_WARNINGS", warning.toString());
			return false;
		}else {
			return true;
		}
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
		
			getDataMartWizard().composeQuery();	
	
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
					returnError(response, he.getCause().getMessage());
				}catch (java.sql.SQLException se) {
					Logger.error(ExecuteSaveQueryAction.class, se);
					returnError(response, se.getMessage());
				}catch(Exception e){
					Logger.error(ExecuteSaveQueryAction.class, e);
					returnError(response, e.getMessage());					
				}
		}//else
		}else{
			IQbeMessageHelper qbeMsg = QbeConf.getInstance().getQbeMessageHelper();
			String bundle =  "component_spagobiqbeIE_messages";
			returnError(response, qbeMsg.getMessage(getRequestContainer(), "QBE.Error.ImpossibleExecution", bundle));	
		}
	}//service
}
