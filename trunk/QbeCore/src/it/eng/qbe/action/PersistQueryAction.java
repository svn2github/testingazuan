
package it.eng.qbe.action;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to Persist the current working query represented by
 * the object ISingleDataMartWizardObject in session
 */
public class PersistQueryAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
	
		DataMartModel dmModel = (DataMartModel)getRequestContainer().getSessionContainer().getAttribute("dataMartModel");
	
		String queryId = (String)request.getAttribute("queryId");
		
		String  queryDescritpion  = (String)request.getAttribute("queryDescritpion");
		
		String  visibility  = (String)request.getAttribute("visibility");
		
		ISingleDataMartWizardObject obj = (ISingleDataMartWizardObject)getRequestContainer().getSessionContainer().getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		if ((queryId != null) && (queryId.trim().length() > 0)){
			obj.setQueryId(queryId);
		}
		
		if ((queryDescritpion != null) && (queryDescritpion.trim().length() > 0)){
			obj.setDescription(queryDescritpion);
		}
		
		if ((visibility != null) && (visibility.trim().length() > 0)){
			if (visibility.equalsIgnoreCase("public")){
				obj.setVisibility(true);
			}else {
				obj.setVisibility(false);
			}
		}
		
		
		dmModel.persistQueryAction(obj);
		SessionContainer session = getRequestContainer().getSessionContainer();
		String cTM = String.valueOf(System.currentTimeMillis());
		session.setAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP", cTM);
		session.setAttribute("QBE_LAST_UPDATE_TIMESTAMP", cTM);
		
	}
}
