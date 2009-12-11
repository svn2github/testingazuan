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

import it.eng.spagobi.analiticalmodel.document.bo.ObjMetaDataAndContent;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetadata;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;

/**
 * 
 * @author Zerbetto Davide
 *
 */
public class GetMetadataAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_METADATA_ACTION";
	
	// REQUEST PARAMETERS
	public static final String OBJECT_ID = "OBJECT_ID";
	public static final String SUBOBJECT_ID = "SUBOBJECT_ID";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetMetadataAction.class);
	
	public void doService() {
		logger.debug("IN");
		try {
			JSONArray toReturn = new JSONArray();
			
			Integer objectId = this.getAttributeAsInteger(OBJECT_ID);
			logger.debug("Object id = " + objectId);
			Integer subObjectId = null;
			try {
				subObjectId = this.getAttributeAsInteger(SUBOBJECT_ID);
			} catch (NumberFormatException e) {}
			logger.debug("Subobject id = " + subObjectId);
			
			List metaDataAndContents = new ArrayList();
	
			List metadata = DAOFactory.getObjMetadataDAO().loadAllObjMetadata();
			if (metadata != null && !metadata.isEmpty()) {
				Iterator it = metadata.iterator();
				while (it.hasNext()) {
					ObjMetadata objMetadata = (ObjMetadata) it.next();
					ObjMetacontent objMetacontent = (ObjMetacontent) DAOFactory.getObjMetacontentDAO().loadObjMetacontent(objMetadata.getObjMetaId(), objectId, subObjectId);
					ObjMetaDataAndContent metaAndContent = new ObjMetaDataAndContent();
					metaAndContent.setMeta(objMetadata);
					metaAndContent.setMetacontent(objMetacontent);	
					metaDataAndContents.add(metaAndContent);
				}
			}

			toReturn = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(metaDataAndContents, null);

			writeBackToClient( new JSONSuccess( toReturn ) ); 
			
		} catch (Exception e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Exception occurred while retrieving metadata", e);
		} finally {
			logger.debug("OUT");
		}
	}

}
