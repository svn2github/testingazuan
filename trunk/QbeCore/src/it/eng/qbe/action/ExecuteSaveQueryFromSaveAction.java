
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
		return (ISingleDataMartWizardObject)getSessionContainer().getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
	}
	
	private DataMartModel getDataMartModel() {
		return (DataMartModel)getSessionContainer().getAttribute("dataMartModel");
	}

	public void service(SourceBean request, SourceBean response) throws Exception{
		if ((getDataMartWizard().getSelectClause() != null) && (getDataMartWizard().getSelectClause().getSelectFields().size() > 0)){
			
		
		getDataMartWizard().composeQuery();
		
		if ((getDataMartWizard().getExpertQueryDisplayed() == null)||(getDataMartWizard().getExpertQueryDisplayed().trim().length() == 0)){
			try{
				getDataMartWizard().setExpertQueryDisplayed(getDataMartWizard().getFinalSqlQuery(getDataMartModel()));
			}catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		boolean joinOk = checkJoins(getDataMartWizard(), response);
		
		if (!joinOk){		
			response.setAttribute("ERROR_MSG_FINAL", "QBE.Warning.Join");
		} 
		else{
			try {
				getDataMartWizard().executeQbeQuery(getDataMartModel(), 0, 10);
				getDataMartWizard().executeExpertQuery(getDataMartModel(), 0, 10);
			}catch(HibernateException he) {				
				Logger.error(ExecuteSaveQueryFromSaveAction.class, he);				
				String causeMsg = he.getCause().getMessage();
				response.setAttribute("ERROR_MSG_FINAL", causeMsg);
			}catch (java.sql.SQLException se) {
				Logger.error(ExecuteSaveQueryAction.class, se);
				String causeMsg = se.getMessage();
				response.setAttribute("ERROR_MSG_FINAL", causeMsg);
			}catch(Exception e){
				Logger.error(ExecuteSaveQueryFromSaveAction.class, e);
				response.setAttribute("ERROR_MSG_FINAL", e.getMessage());					
			}					
		}
		}else{
			IQbeMessageHelper qbeMsg = Utils.getQbeMessageHelper();
			String bundle =  "component_spagobiqbeIE_messages";
			response.setAttribute("ERROR_MSG_FINAL", qbeMsg.getMessage(getRequestContainer(), "QBE.Error.ImpossibleExecution", bundle));	
		}
	}//service
}
