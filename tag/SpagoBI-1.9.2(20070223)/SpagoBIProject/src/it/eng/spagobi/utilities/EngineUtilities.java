package it.eng.spagobi.utilities;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;

public class EngineUtilities {

	public static boolean isInternal(Engine engine) {
		boolean response = false;
		Domain engineType = getEngTypeDom(engine);
		if("INT".equalsIgnoreCase(engineType.getValueCd())) 
			response=true;
		return response;
	}
	
	public static boolean isExternal(Engine engine) {
		boolean response = false;
		Domain engineType = getEngTypeDom(engine);
		if("EXT".equalsIgnoreCase(engineType.getValueCd())) 
			response=true;
		return response;
	}
	
	
	private static Domain getEngTypeDom(Engine engine) {
		Domain engineType = null;
		try {
			engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
		} catch (EMFUserError e) {
			 SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, EngineUtilities.class.getName(), 
		 				        "getEngTypeDom", "Error retrieving engine type domain", e);
		}
		return engineType;
	}
}
