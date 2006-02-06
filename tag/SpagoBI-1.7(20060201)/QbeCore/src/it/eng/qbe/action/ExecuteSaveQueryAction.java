
package it.eng.qbe.action;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.IQbeMessageHelper;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.QbeWebMessageHelper;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;

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
		
		
		String previewMode = (String)request.getAttribute("previewModeFromQueryResult"); 
		String source = (String)request.getAttribute("SOURCE_FROM_QUERY_RESULT");
		String pageNumberString = (String) request.getAttribute("pageNumber");
		String ignoreJoins = (String) request.getAttribute("ignoreJoins");
		
		if ("QUERY_RESULT".equalsIgnoreCase(source)){
			if ("ExpertMode".equalsIgnoreCase(previewMode)){
				
				aWizardObject.setUseExpertedVersion(true);
								
			}else{
					
				aWizardObject.setUseExpertedVersion(false);
					
			}
		}  
				
		
		aWizardObject.composeQuery();
		
	
		//Logger.debug(ExecuteSaveQueryAction.class,"LA QUERY FINALE DEL WIZARD "+ aWizardObject.getFinalQuery());
		
		String finalQueryString = null;
		if (aWizardObject.isUseExpertedVersion()){
			finalQueryString = aWizardObject.getExpertQueryDisplayed();
		}else{
			
			 finalQueryString = aWizardObject.getFinalQuery();
		}
		
		boolean joinOk = true;
		
		if ((ignoreJoins == null) || (!(ignoreJoins.equalsIgnoreCase("true")))){
			if (!aWizardObject.isUseExpertedVersion()){
				// If I'm not using expert
				// Check for join controls
				joinOk = checkJoins(aWizardObject, response);
				
				
			}
		}
		
		if (!joinOk){
			
			if (aSessionContainer.getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
				aSessionContainer.delAttribute(QUERY_RESPONSE_SOURCE_BEAN);
			}//if (aSessionContainer.getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
		
			response.setAttribute("ERROR_MSG", "QBE.Warning.Join");
		} 
		else{
			if ((finalQueryString == null) || (finalQueryString.trim().length() == 0)){
			
				if (aSessionContainer.getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
					aSessionContainer.delAttribute(QUERY_RESPONSE_SOURCE_BEAN);
				}//if (aSessionContainer.getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
			
				response.setAttribute("ERROR_MSG", "QBE.Error.ImpossibleExecution");
			
			}else{
			
				DataMartModel dataMartModel  = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
			
				Session aSession = Utils.getSessionFactory(dataMartModel, ApplicationContainer.getInstance()).openSession();
			
				Query aQuery = aSession.createQuery(finalQueryString);
					
				int pageNumber = 0;
				
				if (pageNumberString != null) {
					pageNumber = Integer.valueOf(pageNumberString).intValue();
				}
				/* NUOVO */
				else{
					pageNumber = 0;
				}
				
				try{
				
					List result = aQuery.list();
					
					int pageSize = 30;
					int initItem = pageNumber * 30;
					int endItem = initItem + (pageSize - 1);
					boolean hasNextPage = true;
					boolean hasPrevPage = true;
					if (endItem > result.size()){
						endItem = result.size();
						hasNextPage = false;
					}
					
					if (pageNumber == 0){
						initItem = 0;
						hasPrevPage = false;
					}
					List toReturn = result.subList(initItem, endItem);
					
					aSession.close();
					
					SourceBean queryResponseSourceBean = new SourceBean(QUERY_RESPONSE_SOURCE_BEAN);
					queryResponseSourceBean.setAttribute("query", finalQueryString);
					queryResponseSourceBean.setAttribute("list", toReturn);
					queryResponseSourceBean.setAttribute("currentPage", new Integer(pageNumber));
					queryResponseSourceBean.setAttribute("hasNextPage", new Boolean(hasNextPage));
					queryResponseSourceBean.setAttribute("hasPreviousPage", new Boolean(hasPrevPage));
					aSessionContainer.setAttribute(QUERY_RESPONSE_SOURCE_BEAN, queryResponseSourceBean);
					
				}catch (HibernateException he) {
					
					Logger.error(ExecuteSaveQueryAction.class, he);
					if (aSessionContainer.getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
						aSessionContainer.delAttribute(QUERY_RESPONSE_SOURCE_BEAN);
					}
					String causeMsg = he.getCause().getMessage();
					response.setAttribute("ERROR_MSG", causeMsg);
				
				}catch(Exception e){
					Logger.error(ExecuteSaveQueryAction.class, e);
					if (aSessionContainer.getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
						aSessionContainer.delAttribute(QUERY_RESPONSE_SOURCE_BEAN);
					}
					String causeMsg = e.getMessage();
					response.setAttribute("ERROR_MSG", causeMsg);
				}
				
				
				/*OLD
				try{
					HibernatePage aHibernatePage = new HibernatePage(aQuery,
						pageNumber, 30);
					SourceBean queryResponseSourceBean = new SourceBean(QUERY_RESPONSE_SOURCE_BEAN);
					queryResponseSourceBean.setAttribute("query", finalQueryString);
					queryResponseSourceBean.setAttribute("list", aHibernatePage.getList());
					queryResponseSourceBean.setAttribute("currentPage", new Integer(pageNumber));
					queryResponseSourceBean.setAttribute("hasNextPage", new Boolean(aHibernatePage.hasNextPage()));
					queryResponseSourceBean.setAttribute("hasPreviousPage", new Boolean(aHibernatePage.hasPreviousPage()));
					aSessionContainer.setAttribute(QUERY_RESPONSE_SOURCE_BEAN, queryResponseSourceBean);
				}catch (HibernateException he) {
					Logger.error(ExecuteSaveQueryAction.class, he);
					if (aSessionContainer.getAttribute(QUERY_RESPONSE_SOURCE_BEAN) != null){
						aSessionContainer.delAttribute(QUERY_RESPONSE_SOURCE_BEAN);
					}
					String causeMsg = he.getCause().getMessage();
					response.setAttribute("ERROR_MSG", causeMsg);
				}
				*/
				
			}//else
		}//else
	}//service
}
