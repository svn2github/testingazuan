
package it.eng.qbe.action;

import org.hibernate.SessionFactory;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.security.GenericLoginHandler;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to put the contents of the query composed automatically
 * in the expert query contents
 */
public class GenericLoginAction extends AbstractAction {
	
		
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		try{
			
			RequestContainer aRequestContainer = getRequestContainer();
			
			GenericLoginHandler handler = new GenericLoginHandler();
			handler.service(aRequestContainer, request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
