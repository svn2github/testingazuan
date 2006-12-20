package it.eng.qbe.action;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import org.apache.xerces.dom3.as.ASDataType;
import org.hibernate.SessionFactory;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to instantiate the datamart where
 * 
 * working with qbe
 */
public class StartWizardSubQueryAction extends AbstractAction {

	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		
		
		
		
        RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		

		String fieldID = (String)request.getAttribute("ON_FIELD_ID");
		aSessionContainer.setAttribute(WizardConstants.QUERY_MODE, WizardConstants.SUBQUERY_MODE);
		aSessionContainer.setAttribute(WizardConstants.SUBQUERY_FIELD, fieldID);
		
		if (aWizardObject.getSubQueryOnField(fieldID) == null ){
			aWizardObject.addSubQueryOnField(fieldID);	
		} 
	}
}
