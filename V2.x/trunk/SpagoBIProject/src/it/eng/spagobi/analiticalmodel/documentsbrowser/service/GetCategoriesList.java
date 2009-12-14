package it.eng.spagobi.analiticalmodel.documentsbrowser.service;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.chiron.serializer.SerializationException;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetadata;
import it.eng.spagobi.tools.objmetadata.dao.IObjMetadataDAO;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCategoriesList extends AbstractSpagoBIAction{
	
	private static Logger logger = Logger.getLogger(GetCategoriesList.class);
	@Override
	public void doService() {
		logger.debug("IN");
		try {
			JSONArray toReturn = new JSONArray();
			IObjMetadataDAO metadataDAO = DAOFactory.getObjMetadataDAO();
			List results = metadataDAO.loadAllObjMetadata();
			ArrayList objects = new ArrayList();
			for (int i = 0; i < results.size(); i++) {
				// look for binary content mimetype
				ObjMetadata metadata = (ObjMetadata)results.get(i);			
				objects.add(metadata);
			}
			toReturn = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(objects, null);
			System.out.println(toReturn.toString());
			writeBackToClient( new JSONSuccess( toReturn ) ); 
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SerializationException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.debug("OUT");
		
	}
}
