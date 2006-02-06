package it.eng.qbe.utility;


import it.eng.spago.base.RequestContainer;
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
		return MessageBundle.getMessage(code, bundle);
	}

}
