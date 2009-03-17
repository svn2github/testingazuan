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

import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;

import org.json.JSONObject;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class FoldersJSONSerializer implements Serializer {
	
	public static final String ID = "id";
	public static final String CODE = "code";
	public static final String CODTYPE = "codType";
	public static final String PATH = "path";
	public static final String PROG = "prog";
	public static final String PARENTID = "parentId";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	
	public static final String DEVROLES = "devRoles";
	public static final String TESTROLES = "testRoles";
	public static final String EXECROLES = "execRoles";
	public static final String BIOBJECTS = "biObjects";
	
	
	public Object serialize(Object o) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof LowFunctionality) ) {
			throw new SerializationException("FoldersJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			LowFunctionality lowFunct = (LowFunctionality)o;
			result = new JSONObject();
			
			result.put(ID, lowFunct.getId() );
			result.put(CODE, lowFunct.getCode() );
			result.put(CODTYPE, lowFunct.getCodType() );
			result.put(PATH, lowFunct.getPath() );			
			result.put(PROG, lowFunct.getProg() );
			result.put(PARENTID, lowFunct.getParentId() );
			result.put(NAME, lowFunct.getName() );			
			result.put(DESCRIPTION, lowFunct.getDescription() );	
			result.put(DEVROLES, lowFunct.getDevRoles() );
			result.put(TESTROLES, lowFunct.getTestRoles() );		
			result.put(EXECROLES, lowFunct.getExecRoles() );
			result.put(BIOBJECTS, lowFunct.getBiObjects() );		
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}
	
	
}
