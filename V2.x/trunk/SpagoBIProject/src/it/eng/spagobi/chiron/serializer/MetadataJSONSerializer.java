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

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.analiticalmodel.document.bo.ObjMetaDataAndContent;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetadata;

import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Chiarelli Chiara
 */
public class MetadataJSONSerializer implements Serializer {
	
	public static final String ID = "meta_id";
	public static final String NAME = "meta_name";
	public static final String TEXT = "meta_content";
	public static final String CREATION_DATE = "meta_creation_date";
	public static final String CHANGE_DATE = "meta_change_date";
	
	public String contextName = "";

	public Object serialize(Object o, Locale locale) throws SerializationException {
		JSONObject  result = new JSONObject();

		contextName = GeneralUtilities.getSpagoBiContext();
		if( !(o instanceof ObjMetaDataAndContent) ) {
			throw new SerializationException("MetadataJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
					ObjMetaDataAndContent both = (ObjMetaDataAndContent)o;
					ObjMetadata meta = both.getMeta();
					ObjMetacontent content = both.getMetacontent();

					result.put(ID, meta.getObjMetaId().toString());
					result.put(NAME, meta.getName());
					if(content!=null){
						result.put(TEXT, new String(content.getContent(),"UTF-8"));
						result.put(CREATION_DATE, content.getCreationDate());
						result.put(CHANGE_DATE, content.getLastChangeDate());
					}else{
						result.put(TEXT, "");
						result.put(CREATION_DATE, "");
						result.put(CHANGE_DATE, "");
					}
		
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		//System.out.println(result);
		return result;
	}

}
