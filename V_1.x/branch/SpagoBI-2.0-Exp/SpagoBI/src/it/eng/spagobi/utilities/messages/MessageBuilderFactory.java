package it.eng.spagobi.utilities.messages;

import it.eng.spago.base.ApplicationContainer;
import it.eng.spagobi.constants.SpagoBIConstants;


public class MessageBuilderFactory {
	
	public static IMessageBuilder getMessageBuilder() {
		ApplicationContainer spagoContext = ApplicationContainer.getInstance();
		IMessageBuilder msgBuilder = (IMessageBuilder)spagoContext.getAttribute(SpagoBIConstants.MESSAGE_BUILDER);
		if(msgBuilder==null) {
			msgBuilder = new MessageBuilder();
			spagoContext.setAttribute(SpagoBIConstants.MESSAGE_BUILDER, msgBuilder);
		}	
		return msgBuilder;
	}
	
	
}
