package it.eng.qbe.utility;


import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.message.MessageBundle;
/**
 * @author Andrea Zoppello
 * 
 * The implementation of IQbeMessageHelper used when QBE is used as a STANDALONE Web Application
 */
public class QbeWebMessageHelper implements
		IQbeMessageHelper {

	public String getMessage(RequestContainer aRequestContainer, String code){
		return MessageBundle.getMessage(code);
	}
	
	public String getMessage(RequestContainer aRequestContainer, String code, String bundle){
		SessionContainer session = aRequestContainer.getSessionContainer();
		SpagoBIInfo spagoBIInfo = (SpagoBIInfo)session.getAttribute("spagobi");
		if(spagoBIInfo != null && spagoBIInfo.getLoacale() != null) 
			return MessageBundle.getMessage(code, bundle, spagoBIInfo.getLoacale());
		return MessageBundle.getMessage(code, bundle);
	}

}
