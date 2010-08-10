
package it.eng.qbe.action;

import org.hibernate.HibernateException;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Gioia
 */
public class ExportResultAction extends AbstractAction {
	
	public static String QUERY_RESPONSE_SOURCE_BEAN = "QUERY_RESPONSE_SOURCE_BEAN"; 
	
	private SessionContainer getSessionContainer() {
		return getRequestContainer().getSessionContainer();
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
	
	public void service(SourceBean request, SourceBean response) throws Exception{

		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		DataMartModel dataMartModel  = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
			
		String query = null;
		aWizardObject.composeQuery();
		if (aWizardObject.isUseExpertedVersion()){
			query = aWizardObject.getExpertQueryDisplayed();
		}else{
			try{
				query =  aWizardObject.getFinalSqlQuery(dataMartModel);
			}catch(Throwable t){
				t.printStackTrace();
			}
		} 
		
		if(query == null) {
			
			returnError(response, "Query is null !!!");
		
		}else{
		
			try {
		
				SourceBean queryResponseSourceBean = aWizardObject.executeSqlQuery(dataMartModel, query, 0, 10);
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
		}
		
				
				
	}	
}
