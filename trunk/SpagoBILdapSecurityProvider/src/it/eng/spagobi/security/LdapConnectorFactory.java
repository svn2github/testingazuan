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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

public class LdapConnectorFactory {

	/**
	 * 
	 * @return
	 */
	public static LDAPConnector createLDAPConnector(){
		
		SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
		SourceBean config = (SourceBean)configSingleton.getAttribute("LDAP_AUTHORIZATIONS.CONFIG");
		
		Map attr=new HashMap();

		
		String t=((SourceBean)config.getAttribute(LDAPConnector.ADMIN_USER)).getCharacters();
	
		
		attr.put(LDAPConnector.ADMIN_USER, ((SourceBean)config.getAttribute(LDAPConnector.ADMIN_USER)).getCharacters());
		attr.put(LDAPConnector.ADMIN_PSW, ((SourceBean)config.getAttribute(LDAPConnector.ADMIN_PSW)).getCharacters());
		attr.put(LDAPConnector.HOST, ((SourceBean)config.getAttribute(LDAPConnector.HOST)).getCharacters());
		attr.put(LDAPConnector.PORT, ((SourceBean)config.getAttribute(LDAPConnector.PORT)).getCharacters());
		attr.put(LDAPConnector.OBJECTCLASS, ((SourceBean)config.getAttribute(LDAPConnector.OBJECTCLASS)).getCharacters());
		attr.put(LDAPConnector.OU_ATTRIBUTE, ((SourceBean)config.getAttribute(LDAPConnector.OU_ATTRIBUTE)).getCharacters());
		attr.put(LDAPConnector.SEARCH_ROOT, ((SourceBean)config.getAttribute(LDAPConnector.SEARCH_ROOT)).getCharacters());
		attr.put(LDAPConnector.SEARCH_ROOT_GROUP, ((SourceBean)config.getAttribute(LDAPConnector.SEARCH_ROOT_GROUP)).getCharacters());
		attr.put(LDAPConnector.OBJECTCLASS_GROUP, ((SourceBean)config.getAttribute(LDAPConnector.OBJECTCLASS_GROUP)).getCharacters());		
		attr.put(LDAPConnector.USER_DN, ((SourceBean)config.getAttribute(LDAPConnector.USER_DN)).getCharacters());
		
		List attrList=config.getAttributeAsList(LDAPConnector.ATTRIBUTES_ID);
		Iterator iterAttr=attrList.iterator();
		String[] elencoAttributi=new String[attrList.size()];
		int i=0;
		while (iterAttr.hasNext()){
			SourceBean tmp=(SourceBean)iterAttr.next();
			elencoAttributi[i]=tmp.getCharacters();
			i++;
		}
		attr.put(LDAPConnector.ATTRIBUTES_ID, elencoAttributi);


		List attrListGroup=config.getAttributeAsList(LDAPConnector.ATTRIBUTES_ID_GROUP);
		Iterator iterAttrGroup=attrListGroup.iterator();
		String[] elencoAttributiGroup=new String[attrListGroup.size()];
		int j=0;
		while (iterAttrGroup.hasNext()){
			SourceBean tmp=(SourceBean)iterAttrGroup.next();
			elencoAttributiGroup[j]=tmp.getCharacters();
			j++;
		}
		attr.put(LDAPConnector.ATTRIBUTES_ID_GROUP, elencoAttributiGroup);


		return new LDAPConnector(attr);	
	}
}
