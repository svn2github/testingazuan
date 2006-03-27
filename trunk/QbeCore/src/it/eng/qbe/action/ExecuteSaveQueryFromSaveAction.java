
package it.eng.qbe.action;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.IQbeMessageHelper;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
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
public class ExecuteSaveQueryFromSaveAction extends AbstractAction {
	
	
	/**
	 * @param wizObj
	 * @param serviceResponse
	 * @return true if controls on join condition are correctt, false otherwise
	 * @throws SourceBeanException
	 */
	public boolean checkJoins(ISingleDataMartWizardObject wizObj, SourceBean serviceResponse) throws SourceBeanException{
	
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
			Logger.error(ExecuteSaveQueryFromSaveAction.class, e);
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
					Logger.debug(ExecuteSaveQueryFromSaveAction.class, "Check if Join Exist between ["+e1+"] and ["+e2+"]");
					
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
							String locMsg = msgHelper.getMessage(getRequestContainer(), "QBE.Warning.Nojoin");
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
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception{
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		aWizardObject.composeQuery();
		
		executeExpertQuery(aWizardObject, response, aSessionContainer);
			
		executeFinalQuery(aWizardObject, response, aSessionContainer);
			
		
				
	}//service
	
	
	private void executeFinalQuery(ISingleDataMartWizardObject aWizardObject, SourceBean response, SessionContainer aSessionContainer) throws Exception{
	
		String finalQueryString = aWizardObject.getFinalQuery();
		
		boolean joinOk = checkJoins(aWizardObject, response);
		
		if (!joinOk){
			
		
			response.setAttribute("ERROR_MSG_FINAL", "QBE.Warning.Join");
		} 
		else{
			if ((finalQueryString == null) || (finalQueryString.trim().length() == 0)){
			
				response.setAttribute("ERROR_MSG_FINAL", "QBE.Error.ImpossibleExecution");
			
			}else{
			
				DataMartModel dataMartModel  = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
			
				Session aSession = Utils.getSessionFactory(dataMartModel, ApplicationContainer.getInstance()).openSession();
			
				Query aQuery = aSession.createQuery(finalQueryString);
								
				aQuery.setFirstResult(1);
				aQuery.setMaxResults(5);
				
				try{
				
					aQuery.list();
					
					aSession.close();
										
				}catch (HibernateException he) {
					
					Logger.error(ExecuteSaveQueryFromSaveAction.class, he);
					
					String causeMsg = he.getCause().getMessage();
					response.setAttribute("ERROR_MSG_FINAL", causeMsg);
				
				}catch(Exception e){
					Logger.error(ExecuteSaveQueryFromSaveAction.class, e);
					
					String causeMsg = e.getMessage();
					response.setAttribute("ERROR_MSG_FINAL", causeMsg);
				}
			}
		}
	}
	
	
	private void executeExpertQuery(ISingleDataMartWizardObject aWizardObject, SourceBean response, SessionContainer aSessionContainer) throws Exception{
		
		String finalQueryString = aWizardObject.getExpertQueryDisplayed();
		
		if ((finalQueryString == null) || (finalQueryString.trim().length() == 0)){
		
			response.setAttribute("ERROR_MSG_EXPERT", "QBE.Error.ImpossibleExecution");
		
		}else{
		
			DataMartModel dataMartModel  = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
		
			Session aSession = Utils.getSessionFactory(dataMartModel, ApplicationContainer.getInstance()).openSession();
		
			Query aQuery = aSession.createQuery(finalQueryString);
				
			aQuery.setFirstResult(1);
			aQuery.setMaxResults(5);
							
			try{
				
				aQuery.list();
					
				aSession.close();
								
			}catch (HibernateException he) {
					
				Logger.error(ExecuteSaveQueryFromSaveAction.class, he);
				String causeMsg = he.getCause().getMessage();
				response.setAttribute("ERROR_MSG_EXPERT", causeMsg);
				
			}catch(Exception e){
				
				Logger.error(ExecuteSaveQueryFromSaveAction.class, e);
				String causeMsg = e.getMessage();
				response.setAttribute("ERROR_MSG_EXPERT", causeMsg);
			}
			
		}
	}
	
	
	
	
	
}
