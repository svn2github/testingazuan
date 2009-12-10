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
package it.eng.spagobi.analiticalmodel.document.x;

import java.util.Date;

import it.eng.spagobi.chiron.serializer.MetadataJSONSerializer;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Zerbetto Davide
 *
 */
public class SaveMetadataAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "SAVE_METADATA_ACTION";
	
	public static final String METADATA = "METADATA";
	
	// logger component
	private static Logger logger = Logger.getLogger(SaveMetadataAction.class);
	
	public void doService() {
		logger.debug("IN");
		try {
			String jsonEncodedMetadata = getAttributeAsString( METADATA );
			logger.debug(METADATA + " = [" + jsonEncodedMetadata + "]");
			JSONArray metadata = new JSONArray(jsonEncodedMetadata);
			for (int i = 0; i < metadata.length(); i++) {					
				JSONObject aMetadata = metadata.getJSONObject(i);
				Integer metadataId = aMetadata.getInt(MetadataJSONSerializer.METADATA_ID);
				Integer biobjectId = aMetadata.getInt(MetadataJSONSerializer.BIOBJECT_ID);
				Integer subobjectId = aMetadata.getInt(MetadataJSONSerializer.SUBOBJECT_ID);
				String text = aMetadata.getString(MetadataJSONSerializer.TEXT);
				ObjMetacontent aObjMetacontent = DAOFactory.getObjMetacontentDAO().loadObjMetacontent(metadataId, biobjectId, subobjectId);
				if (aObjMetacontent == null) {
					logger.debug("ObjMetacontent for metadata id = " + metadataId + ", biobject id = " + biobjectId + 
							", subobject id = " + subobjectId + " was not found, creating a new one...");
					aObjMetacontent = new ObjMetacontent();
					aObjMetacontent.setObjmetaId(metadataId);
					aObjMetacontent.setBiobjId(biobjectId);
					aObjMetacontent.setSubobjId(subobjectId);
					aObjMetacontent.setContent(text.getBytes("UTF-8"));
					aObjMetacontent.setCreationDate(new Date());
					aObjMetacontent.setLastChangeDate(new Date());
					DAOFactory.getObjMetacontentDAO().insertObjMetacontent(aObjMetacontent);
				} else {
					logger.debug("ObjMetacontent for metadata id = " + metadataId + ", biobject id = " + biobjectId + 
							", subobject id = " + subobjectId + " was found, it will be modified...");
					aObjMetacontent.setContent(text.getBytes("UTF-8"));
					aObjMetacontent.setLastChangeDate(new Date());
					DAOFactory.getObjMetacontentDAO().modifyObjMetacontent(aObjMetacontent);
				}
			}		
			
		} catch (Exception e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Exception occurred while saving metadata", e);
		} finally {
			logger.debug("OUT");
		}
	}

}
