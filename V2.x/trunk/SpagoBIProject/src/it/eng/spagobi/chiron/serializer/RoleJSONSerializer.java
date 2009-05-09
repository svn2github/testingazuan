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

import org.json.JSONObject;

import it.eng.spagobi.commons.bo.Role;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class RoleJSONSerializer implements Serializer {

	public static final String ROLE_ID = "id";
	private static final String ROLE_NAME = "name";
	private static final String ROLE_DESCRIPTION = "description";
	private static final String ROLE_CODE = "code";
	private static final String ROLE_TYPE_ID = "typeId";
	private static final String ROLE_TYPE_CD = "typeCd";
	
	public Object serialize(Object o) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof Role) ) {
			throw new SerializationException("RoleJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			Role role = (Role)o;
			result = new JSONObject();
			
			result.put(ROLE_ID, role.getId() );
			result.put(ROLE_NAME, role.getName() );
			result.put(ROLE_DESCRIPTION, role.getDescription() );
			result.put(ROLE_CODE, role.getCode() );
			result.put(ROLE_TYPE_ID, role.getRoleTypeID() );
			result.put(ROLE_TYPE_CD, role.getRoleTypeCD() );
			
			
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}

}
