package it.eng.qbe.utility;


import it.eng.spago.base.RequestContainer;
import it.eng.spagobi.utilities.PortletUtilities;

import javax.portlet.PortletRequest;

public class QbeSpagoBIMessageHelper implements
		IQbeMessageHelper {

	private static final String DEFAULT_BUNDLE = "messages";
	
	public String getMessage(RequestContainer aRequestContainer, String code){
		return getMessage(aRequestContainer, code, DEFAULT_BUNDLE);
	}
	
	public String getMessage(RequestContainer aRequestContainer, String code, String bundle){
		//PortletRequest pr = PortletUtilities.getPortletRequest();
		return PortletUtilities.getMessage(code, bundle);
	}

}
