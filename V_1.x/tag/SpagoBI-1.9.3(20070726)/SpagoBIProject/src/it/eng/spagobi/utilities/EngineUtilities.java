/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
