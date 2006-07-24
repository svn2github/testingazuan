
package it.eng.qbe.action;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.security.IEngUserProfile;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to put the contents of the query composed automatically
 * in the expert query contents
 */
public class LogoutAction extends AbstractAction {
	
		
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		IEngUserProfile userProfile = (IEngUserProfile)getRequestContainer().getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		if (userProfile != null){
			getRequestContainer().getSessionContainer().getPermanentContainer().delAttribute(IEngUserProfile.ENG_USER_PROFILE);
		}
		
		
		
	}
}
