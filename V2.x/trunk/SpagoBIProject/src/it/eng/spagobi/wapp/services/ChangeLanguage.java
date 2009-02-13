package it.eng.spagobi.wapp.services;

/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;

import java.util.Locale;

import org.apache.log4j.Logger;

public class ChangeLanguage extends AbstractHttpAction{

	static private Logger logger = Logger.getLogger(ReadHtmlFile.class);

	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {
		logger.debug("IN");
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		SessionContainer sessCont = reqCont.getSessionContainer();
		SessionContainer permSess = sessCont.getPermanentContainer();

		String language=(String)serviceRequest.getAttribute("Language_id");
		Locale locale = MessageBuilder.getBrowserLocaleFromSpago();		

		if(language!=null){
			logger.debug("language selected: "+language);
			IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			UserProfile userProfile=null;
			String lang="";
			if (profile  instanceof UserProfile) {
				userProfile = (UserProfile) profile;
			}

			if(language.equalsIgnoreCase("IT")){
				//permSess.setAttribute(SpagoBIConstants.CURRENT_LANGUAGE, locale.ITALIAN.getLanguage());
				permSess.setAttribute("AF_LANGUAGE", locale.ITALY.getLanguage());
				permSess.setAttribute("AF_COUNTRY", locale.ITALY.getCountry());
				lang=locale.ITALY.getLanguage();
			}
			else if(language.equalsIgnoreCase("EN")){
				//permSess.setAttribute(SpagoBIConstants.CURRENT_LANGUAGE, locale.ENGLISH.getLanguage());
				permSess.setAttribute("AF_LANGUAGE", locale.US.getLanguage());
				permSess.setAttribute("AF_COUNTRY", locale.US.getCountry());
				lang=locale.US.getLanguage();
			}
			if(language.equalsIgnoreCase("FR")){
				//permSess.setAttribute(SpagoBIConstants.CURRENT_LANGUAGE, locale.FRENCH.getLanguage());
				permSess.setAttribute("AF_LANGUAGE", locale.FRANCE.getLanguage());
				permSess.setAttribute("AF_COUNTRY", locale.FRANCE.getCountry());
				lang=locale.FRANCE.getLanguage();
			}

			if(userProfile!=null){
				userProfile.setAttributeValue(SpagoBIConstants.LANGUAGE, lang);
				logger.debug("modified profile attribute to "+ lang);
			}
			else{
				logger.error("profile attribute not modified to "+ lang);				
			}
			
		}
		else
		{
			logger.debug("Take default language");
			permSess.setAttribute(SpagoBIConstants.CURRENT_LANGUAGE, locale.getLanguage());
		}




		serviceResponse.setAttribute("MENU_MODE", "ALL_TOP");
		serviceResponse.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "home");
		logger.debug("OUT");
	}

}


