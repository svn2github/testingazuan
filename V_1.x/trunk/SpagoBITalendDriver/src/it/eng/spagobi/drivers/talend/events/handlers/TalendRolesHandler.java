/**
 * 
 * LICENSE: see 'LICENSE.sbi.drivers.talend.txt' file
 * 
 */
package it.eng.spagobi.drivers.talend.events.handlers;

import java.util.List;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.drivers.handlers.IRolesHandler;
import it.eng.spagobi.utilities.SpagoBITracer;

public class TalendRolesHandler implements IRolesHandler {

	public List calculateRoles(String parameters) throws EMFInternalError, EMFUserError {
		String[] splittedParameters = parameters.split("&");
		if (splittedParameters == null || splittedParameters.length == 0) {
			SpagoBITracer.major("TALEND DRIVER",
					this.getClass().getName(),
					"calculateRoles(String)",
					"Missing parameters for roles retrieval");
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "Missing parameters for roles retrieval");
		}
		String biobjectIdStr = null;
		for (int i = 0; i < splittedParameters.length; i++) {
			String parameter = splittedParameters[i].trim();
			String[] splittedParameter = parameter.split("=");
			String parameterName = splittedParameter[0];
			if (parameterName.trim().equalsIgnoreCase("biobjectId")) {
				if (splittedParameter.length != 2) {
					SpagoBITracer.major("TALEND DRIVER",
							this.getClass().getName(),
							"calculateRoles(String)",
							"Malformed parameter for roles retrieval");
					throw new EMFInternalError(EMFErrorSeverity.ERROR, "Malformed parameter for roles retrieval");
				}
				biobjectIdStr = splittedParameter[1];
				break;
			}
		}
		if (biobjectIdStr == null) {
			SpagoBITracer.major("TALEND DRIVER",
					this.getClass().getName(),
					"calculateRoles(String)",
					"Missing parameters for roles retrieval");
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "Missing parameters for roles retrieval");
		}
		Integer biobjectId = null;
		try {
			biobjectId = new Integer(biobjectIdStr);
		} catch (Exception e) {
			SpagoBITracer.major("TALEND DRIVER",
					this.getClass().getName(),
					"calculateRoles(String)",
					"Malformed BIObject id: " + biobjectIdStr, e);
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "Malformed BIObject id: " + biobjectIdStr);
		}
		List roles = null;
		try {
			roles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(biobjectId);
		} catch (EMFUserError e) {
			SpagoBITracer.major("TALEND DRIVER",
					this.getClass().getName(),
					"calculateRoles(String)",
					"Error while loading correct roles for execution for document with id = " + biobjectId, e);
			throw e;
		}
		return roles;
	}

}
