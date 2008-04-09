package it.eng.spagobi.commons.services;

import org.apache.log4j.Logger;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractHttpModule;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;

public class BaseProfileModule extends AbstractHttpModule{
	
	static Logger logger = Logger.getLogger(BaseProfileModule.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		//Get userid from request
		String requestUserId =(String)request.getAttribute("userid");
		String sessionUserId = "";
		String profileUser = "";
		//Check if CAS is active
		ConfigSingleton serverConfig = ConfigSingleton.getInstance();
		SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
	    String active = (String) validateSB.getCharacters();
	    
	    //If CAS is active gets userid in session
	    if (active != null && active.equals("true")) {
	    	sessionUserId = (String) this.getHttpRequest().getSession().getAttribute("edu.yale.its.tp.cas.client.filter.user");
	    	if(requestUserId!=null && sessionUserId!= null){
	    		//if userid in session different from userid in request throws exception
		    	if (!requestUserId.equals(sessionUserId)){
		    		throw new SecurityException();
		    	}
		    }
	    	else if (sessionUserId==null){
	    		throw new SecurityException();
	    	}
	    }
	    
	    //If CAS is not active gets userid from session
	    else {
	    	IEngUserProfile profile = null;
			RequestContainer reqCont = RequestContainer.getRequestContainer();
			SessionContainer sessCont = reqCont.getSessionContainer();
			SessionContainer permSess = sessCont.getPermanentContainer();
			profile = (IEngUserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			if (profile!=null)profileUser = profile.getUserUniqueIdentifier().toString();
			//If session userid differs from request userid or is null, puts the new userid in session
			if (profileUser == null || profileUser == "" || !profileUser.equals(requestUserId)){
				permSess.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profileUser);
			}
	    }	
	}

}
