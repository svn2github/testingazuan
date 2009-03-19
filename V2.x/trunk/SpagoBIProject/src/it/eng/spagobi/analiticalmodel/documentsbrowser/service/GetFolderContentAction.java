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

*/
package it.eng.spagobi.analiticalmodel.documentsbrowser.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.exceptions.SpagoBIException;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;
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
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */
public class GetFolderContentAction extends AbstractBaseHttpAction{
	
	// REQUEST PARAMETERS
	public static final String FOLDER_ID = "folderId";
	
	public static final String ROOT_NODE_ID = "rootNode";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetFolderContentAction.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		List functionalities;
		List objects;
		
		logger.debug("IN");
		
		try {
			setSpagoBIRequestContainer( request );
			setSpagoBIResponseContainer( response );
			
			String functID = getAttributeAsString(FOLDER_ID);
			
			
			if (functID == null || functID.equalsIgnoreCase(ROOT_NODE_ID)){
				//getting default folder (root)
				LowFunctionality rootFunct = DAOFactory.getLowFunctionalityDAO().loadRootLowFunctionality(false);
				functID = String.valueOf(rootFunct.getId());
			}
			logger.debug("Parameter [" + FOLDER_ID + "] is equal to [" + functID + "]");
		
			SessionContainer sessCont = getSessionContainer();
			SessionContainer permCont = sessCont.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permCont.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			
			
			//getting children documents
			//LowFunctionality lowFunct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(functID, true);
			//objects = lowFunct.getBiObjects();
			List tmpObjects = DAOFactory.getBIObjectDAO().loadBIObjects(Integer.valueOf(functID), profile);
			objects = new ArrayList();
			if(tmpObjects != null) {
                for(Iterator it = tmpObjects.iterator(); it.hasNext();) {
                    BIObject obj = (BIObject)it.next();
                    if(ObjectsAccessVerifier.checkProfileVisibility(obj, profile))
                    	objects.add(obj);
                }
			}
		
			JSONArray documentsJSON = (JSONArray)SerializerFactory.getSerializer("application/json").serialize( objects );
			JSONObject documentsResponseJSON =  createJSONResponseDocuments(documentsJSON);
			
			//getting children folders
			functionalities = DAOFactory.getLowFunctionalityDAO().loadChildFunctionalities(Integer.valueOf(functID), false);	
			JSONArray foldersJSON = (JSONArray)SerializerFactory.getSerializer("application/json").serialize( functionalities );			
			JSONObject foldersResponseJSON =  createJSONResponseFolders(foldersJSON);
		
			try {
				writeBackToClient( new JSONSuccess( createJSONResponse(foldersResponseJSON, documentsResponseJSON) ) );
			} catch (IOException e) {
				throw new SpagoBIException("Impossible to write back the responce to the client", e);
			}
			
		} catch (Throwable t) {
			throw new SpagoBIException("An unexpected error occured while executing " + getActionName(), t);
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Creates a json array with children document informations
	 * @param rows
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONResponseDocuments(JSONArray rows) throws JSONException {
		JSONObject results;
		
		results = new JSONObject();
		results.put("title", "Documents");
		results.put("icon", "document.png");
		results.put("samples", rows);
		return results;
	}

	/**
	 * Creates a json array with children folders informations
	 * @param rows
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONResponseFolders(JSONArray rows) throws JSONException {
		JSONObject results;
		
		results = new JSONObject();
		results.put("title", "Folders");
		results.put("icon", "folder.png");
		results.put("samples", rows);
		return results;
	}

	/**
	 * Creates a json array with children document informations
	 * @param rows
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONResponse(JSONObject folders, JSONObject documents) throws JSONException {
		JSONObject results = new JSONObject();
		JSONArray folderContent = new JSONArray();

		folderContent.put(folders);
		folderContent.put(documents);
		results.put("folderContent", folderContent);
		
		return results;
	}
}
