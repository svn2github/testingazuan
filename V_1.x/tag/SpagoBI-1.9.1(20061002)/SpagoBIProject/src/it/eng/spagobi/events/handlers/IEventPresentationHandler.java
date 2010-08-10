package it.eng.spagobi.events.handlers;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.EventLog;

public interface IEventPresentationHandler {

	public void loadEventInfo(EventLog event, SourceBean response) throws SourceBeanException, EMFUserError;
	
}
