package it.eng.spagobi.services.event.service;

import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.drivers.handlers.IRolesHandler;
import it.eng.spagobi.events.EventsManager;
import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import java.util.List;

import org.apache.log4j.Logger;

public class EventServiceImpl extends AbstractServiceImpl {
    static private Logger logger = Logger.getLogger(EventServiceImpl.class);

    public String fireEvent(String token, String user, String description,
	    String parameters, String rolesHandler, String presentationHandler) {
	logger.debug("IN");
	if (activeSso) {
	    try {
		if (validateTicket(token)) {
		    return fireEvent(user, description, parameters,
			    rolesHandler, presentationHandler);
		} else {
		    logger.error("Token NOT VALID");
		    return null;
		}
	    } catch (SecurityException e) {
		logger.error("SecurityException", e);
		return null;
	    } finally {
		logger.debug("OUT");
	    }
	} else {
	    logger.debug("OUT");
	    return fireEvent(user, description, parameters, rolesHandler,
		    presentationHandler);
	}

    }

    private String fireEvent(String user, String description,
	    String parameters, String rolesHandler, String presentationHandler) {
	logger.debug("IN");
	String returnValue = null;

	try {
	    if (user != null) {

		IRolesHandler rolesHandlerClass = (IRolesHandler) Class.forName(rolesHandler).newInstance();
		List roles = rolesHandlerClass.calculateRoles(parameters);
		Integer id = EventsManager.getInstance().registerEvent(user,description, parameters, roles, presentationHandler);
		returnValue = id.toString();
		logger.debug("Service executed succesfully");
	    } else {
		logger.warn("User is NULL");
	    }
	    return returnValue;
	} catch (Exception e) {
	    SpagoBITracer.critical("SpagoBI", getClass().getName(), "service",
		    "Exception", e);
	}finally{
	    logger.debug("OUT");
	}
	return null;
    }

}
