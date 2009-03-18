/**
 * 
 */
package it.eng.spagobi.analiticalmodel.documentsbrowser.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.utilities.exceptions.SpagoBIException;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;
import it.eng.spagobi.utilities.service.JSONSuccess;


/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */
public class GetFolderContentAction extends AbstractBaseHttpAction{
	
	// REQUEST PARAMETERS
	public static final String FOLDER_ID = "folderId";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetFolderContentAction.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		List functionalities;
		List objects;
		
		logger.debug("IN");
		
		try {
			setSpagoBIRequestContainer( request );
			setSpagoBIResponseContainer( response );
			
			Integer functID = getAttributeAsInteger(FOLDER_ID);
			
			//Integer functID = (request.getAttribute("folderID")== null)?new Integer("-1"):(Integer)request.getAttribute("folderID");
			
			//getting children documents
			LowFunctionality lowFunct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(functID, true);
			objects = lowFunct.getBiObjects();
			JSONArray documentsJSON = (JSONArray)SerializerFactory.getSerializer("application/json").serialize( objects );
			JSONObject documentsResponseJSON =  createJSONResponseDocuments(documentsJSON);
			
			//getting children folders
			functionalities = DAOFactory.getLowFunctionalityDAO().loadChildFunctionalities(functID, false);	
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
		/*
		JSONObject title;
		JSONObject icon;
		JSONArray results;
			
		title = new JSONObject();
		icon = new JSONObject();
		results = new JSONArray();
		
		title.put("title", "Documents");
		icon.put("icon", "document.png");
		results.put(title);
		results.put(icon);
		results.put(rows);
		*/
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
		
		/*
		
		
		
		JSONObject title;
		JSONObject icon;
		JSONArray results;
			
		title = new JSONObject();
		icon = new JSONObject();
		results = new JSONArray();
		
		title.put("title", "Folders");
		icon.put("icon", "folder.png");
		results.put(title);
		results.put(icon);
		results.put(rows);
	*/
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
