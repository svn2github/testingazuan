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

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;

import org.json.JSONObject;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class DocumentsJSONSerializer implements Serializer {
	
	public static final String ID = "id";
	public static final String LABEL = "label";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String TYPECODE = "typeCode";
	public static final String TYPEID = "typeId";
	public static final String ENCRYPT = "encrypt";
	public static final String VISIBLE = "visible";
	public static final String PROFILEDVISIBILITY = "profiledVisibility";
	public static final String ENGINE = "engine";
	public static final String DATASOURCE = "datasource";
	public static final String DATASET = "dataset";
	public static final String UUID = "uuid";
	public static final String RELNAME = "relname";
	public static final String STATECODE = "stateCode";
	public static final String STATEID = "stateId";
	public static final String FUNCTIONALITIES = "functionalities";
	public static final String CREATIONDATE = "creationDate";
	public static final String EXTENDEDDESCRIPTION = "extendedDescription";
	public static final String CREATIONUSER = "creationUser";
	public static final String LANGUAGE = "language";
	public static final String OBJECTVE = "objectve";
	public static final String KEYWORDS = "keywords";
	public static final String REFRESHSECONDS = "refreshSeconds";
	
	
	public Object serialize(Object o) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof BIObject) ) {
			throw new SerializationException("DocumentsJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			BIObject obj = (BIObject)o;
			result = new JSONObject();
			
			result.put(ID, obj.getId() );
			result.put(LABEL, obj.getLabel() );
			result.put(NAME, obj.getName() );
			
			
			result.put(TYPECODE, obj.getBiObjectTypeCode());
			result.put(TYPEID, obj.getBiObjectTypeID());
			result.put(ENCRYPT, obj.getEncrypt());
			result.put(VISIBLE, obj.getVisible());
			result.put(PROFILEDVISIBILITY, obj.getProfiledVisibility());
			result.put(ENGINE, obj.getEngine());
			result.put(DATASOURCE, obj.getDataSourceId());
			result.put(DATASET, obj.getDataSetId());
			result.put(UUID, obj.getUuid());
			result.put(RELNAME, obj.getRelName());
			result.put(STATECODE, obj.getStateCode());
			result.put(STATEID, obj.getStateID());
			result.put(FUNCTIONALITIES, obj.getFunctionalities());
			result.put(CREATIONDATE, obj.getCreationDate());
			result.put(EXTENDEDDESCRIPTION, obj.getExtendedDescription());
			result.put(CREATIONUSER, obj.getCreationUser());
			result.put(LANGUAGE, obj.getLanguage());
			result.put(OBJECTVE, obj.getObjectve());
			result.put(KEYWORDS, obj.getKeywords());
			result.put(REFRESHSECONDS, obj.getRefreshSeconds());
			
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}
	
	
}
