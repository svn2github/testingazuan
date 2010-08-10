
package it.eng.qbe.action;

import it.eng.qbe.utility.Logger;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to recover the Thread Context ClassLoader that was modified dynamically by QBE 
 * before exit QBE
 */
public class RecoverClassLoaderAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		try{
		
			ClassLoader toRecoverClassLoader = (ClassLoader)ApplicationContainer.getInstance().getAttribute("CURRENT_THREAD_CONTEXT_LOADER"); 
			Logger.debug(RecoverClassLoaderAction.class, "Recovering ClassLoader " + toRecoverClassLoader.toString());
			if ( toRecoverClassLoader != null){
				Thread.currentThread().setContextClassLoader(toRecoverClassLoader);
			}
			String actor = (String)getRequestContainer().getSessionContainer().getAttribute("ACTOR");
			response.setAttribute("PAGE", "TreeObjectsPage");
			response.setAttribute("ACTOR", actor);
		}catch (SourceBeanException sbe){
			sbe.printStackTrace();
		}
		
	}
}
