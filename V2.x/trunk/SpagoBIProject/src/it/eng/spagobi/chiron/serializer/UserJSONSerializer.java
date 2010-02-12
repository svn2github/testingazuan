/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.chiron.serializer;

import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.profiling.bean.SbiExtUserRoles;
import it.eng.spagobi.profiling.bean.SbiUserAttributes;
import it.eng.spagobi.profiling.bo.UserBO;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author franceschini
 *
 */
public class UserJSONSerializer implements Serializer {
	private static Logger logger = Logger.getLogger(UserJSONSerializer.class);
	
	public static final String USER_ID = "userId";
	public static final String FULL_NAME = "fullName";
	public static final String ID = "id";

	public Object serialize(Object o, Locale locale)
			throws SerializationException {
		logger.debug("IN");
		JSONObject result = new JSONObject();

		if ( !(o instanceof UserBO) ) {
			throw new SerializationException("UserJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {

			UserBO userBO = (UserBO)o;
			result.put(ID, userBO.getId());
			result.put(USER_ID, userBO.getUserId());
			result.put(FULL_NAME, userBO.getFullName());
			
			//roles
			List<Integer> userRoles = userBO.getSbiExtUserRoleses();
			Iterator itRoles = userRoles.iterator();
			JSONArray rolesJSON = new JSONArray();
			//rolesJSON.put("roles");

			while(itRoles.hasNext()){
				JSONObject jsonRole = new JSONObject();
				Integer roleId = (Integer)itRoles.next();

				Role role = DAOFactory.getRoleDAO().loadByID(roleId);
				jsonRole.put("name", role.getName());
				jsonRole.put("id", role.getId());
				jsonRole.put("description", role.getDescription());
				rolesJSON.put(jsonRole);
			}	
			result.put("userRoles", rolesJSON);
			
			//attributes
			HashMap<Integer, HashMap<String, String>> userAttributes = userBO.getSbiUserAttributeses();
			Iterator itAttrs = userAttributes.keySet().iterator();
			JSONArray attrsJSON = new JSONArray();
			//attrsJSON.put("attributes");

			while(itAttrs.hasNext()){
				JSONObject jsonAttr = new JSONObject();
				Integer userAttrID = (Integer)itAttrs.next();
				HashMap<String, String> nameAndValueAttr = userAttributes.get(userAttrID);
				
				String attrName= nameAndValueAttr.keySet().iterator().next();//unique value
				jsonAttr.put("name", attrName);
				jsonAttr.put("id", userAttrID);
				jsonAttr.put("value", nameAndValueAttr.get(attrName));
				attrsJSON.put(jsonAttr);
			}	
			result.put("userAttributes", attrsJSON);
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			logger.debug("OUT");
		}
		return result;
	}

}
