/**

Copyright 2005 Engineering Ingegneria Informatica S.p.A.

This file is part of SpagoBI.

SpagoBI is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
any later version.

SpagoBI is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Spago; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

**/
package it.eng.spagobi.security;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public class SecurityProviderUtilities {
	

	private static SourceBean getProfileAttributesSourceBean() {
		SourceBean profileAttributesSB = (SourceBean) 
			ConfigSingleton.getInstance().getAttribute("LIFERAY_PORTAL_SECURITY.PROFILE_ATTRIBUTES");
		if (profileAttributesSB == null) {
			SpagoBITracer.critical("SPAGOBI(LiferaySecurityProvider)", 
					SecurityProviderUtilities.class.getName(), "getProfileAttributesSourceBean()", 
					"There is not the needed LIFERAY_PORTAL_SECURITY.PROFILE_ATTRIBUTES attribute " +
					"in the ConfigSingleton!!");
			return null;
		} else return profileAttributesSB;
		
	}

	public static List getAllProfileAtributesNames () throws EMFInternalError {
		SourceBean profileAttrsSB = getProfileAttributesSourceBean();
		if (profileAttrsSB == null) {
			throw new EMFInternalError(EMFErrorSeverity.ERROR, 
					"Profile attributes attribute not found in ConfigSingleton");
		}
		List toReturn = new ArrayList();
		List attrs = profileAttrsSB.getAttributeAsList("ATTRIBUTE");
		if (attrs != null && attrs.size() > 0) {
			Iterator iterAttrs = attrs.iterator();
			SourceBean attrSB = null;
			String nameattr = null;
			while(iterAttrs.hasNext()) {
				attrSB = (SourceBean) iterAttrs.next();
				if (attrSB == null)
					continue;
				nameattr = (String) attrSB.getAttribute("name");
				if (nameattr == null) {
					throw new EMFInternalError(EMFErrorSeverity.ERROR, 
							"Attribute 'name' missing in SourceBean\n" + attrSB.toXML(false));
				}
				toReturn.add(nameattr);
			}
		}
		return toReturn;
	}
}
