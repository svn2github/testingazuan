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

import it.eng.spago.base.SessionContainer;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjMetaDataAndContent;
import it.eng.spagobi.analiticalmodel.document.bo.ObjNote;
import it.eng.spagobi.analiticalmodel.document.handlers.BIObjectNotesManager;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.chiron.serializer.MetadataJSONSerializer;
import it.eng.spagobi.chiron.serializer.SerializationException;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetadata;
import it.eng.spagobi.tools.objmetadata.metadata.SbiObjMetadata;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Zerbetto Davide

 *
 */
public class GetMetadataAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_METADATA_ACTION";
	// REQUEST PARAMETERS
	public static final String MESSAGE = "MESSAGE";
	public static final String OWNER = "OWNER";
	public static final String SHORT_TEXT ="shorttext";
	public static final String LONG_TEXT ="longtext";
	
	public static final String SHORT_TEXT_METADATA_TYPE = "SHORT_TEXT";
	public static final String LONG_TEXT_METADATA_TYPE = "LONG_TEXT";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetMetadataAction.class);
	
	public void doService() {
		logger.debug("IN");
		try {
			JSONObject totMetadata = new JSONObject();
			
			// retrieving execution instance from session, no need to check if user is able to execute the current document
			Integer objectId = this.getAttributeAsInteger("OBJECT_ID");
			
			//START SHORT TEXT METADATA RETRIEVAL
			List shortTextMetaDataAndContents = new ArrayList();
	
			List shortTextMetadata = DAOFactory.getObjMetadataDAO().loadObjMetaDataListByType(SHORT_TEXT_METADATA_TYPE);	
			if(shortTextMetadata!=null && !shortTextMetadata.isEmpty()){
				Iterator it = shortTextMetadata.iterator();
				while (it.hasNext()) {
					ObjMetaDataAndContent tempBoth = new ObjMetaDataAndContent();
					ObjMetadata temp = (ObjMetadata)it.next();
					tempBoth.setMeta(temp);
					if(temp!=null){
						ObjMetacontent tempCont = (ObjMetacontent) DAOFactory.getObjMetacontentDAO().loadObjMetacontentBySubobjId(temp.getObjMetaId(), objectId, null);
						tempBoth.setMetacontent(tempCont);	
					}
					shortTextMetaDataAndContents.add(tempBoth);
				}
				JSONArray shortTextMetadataListJSON = (JSONArray)SerializerFactory.getSerializer("application/json").serialize( shortTextMetaDataAndContents,null);
				totMetadata.put(SHORT_TEXT, shortTextMetadataListJSON);
			}
			//END SHORT TEXT METADATA RETRIEVAL
			
			//START LONG TEXT METADATA RETRIEVAL
			List longTextMetadataContents = new ArrayList();
			
			List longTextMetaDataAndContent = DAOFactory.getObjMetadataDAO().loadObjMetaDataListByType(LONG_TEXT_METADATA_TYPE);
			if(longTextMetaDataAndContent!=null  && !longTextMetaDataAndContent.isEmpty()){
				Iterator it2 = longTextMetaDataAndContent.iterator();
				while (it2.hasNext()) {
					ObjMetaDataAndContent tempBoth = new ObjMetaDataAndContent();
					ObjMetadata temp = (ObjMetadata)it2.next();
					tempBoth.setMeta(temp);
					if(temp!=null){
						ObjMetacontent tempCont = (ObjMetacontent) DAOFactory.getObjMetacontentDAO().loadObjMetacontentBySubobjId(temp.getObjMetaId(), objectId, null);
						tempBoth.setMetacontent(tempCont);		
					}
					longTextMetadataContents.add(tempBoth);
				}
				JSONArray longTextMetadataListJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize( longTextMetadataContents,null);
				totMetadata.put(LONG_TEXT, longTextMetadataListJSON);
			}
			//END LONG TEXT METADATA RETRIEVAL

			writeBackToClient( new JSONSuccess( totMetadata ) ); 
			
		} catch (EMFUserError e) {
			logger.error("Errors while loading Metadata",e);
			e.printStackTrace();
		} catch (SerializationException e) {
			logger.error("Serialization Excepiton",e);
			e.printStackTrace();
		} catch (JSONException e) {
			logger.error("JSONException Excepiton",e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException Excepiton",e);
			e.printStackTrace();
		} finally {
			logger.debug("OUT");
		}
	}

}
