/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.catalogue.service;

import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.AbstractSpagoBIAction;
import it.eng.spagobi.commons.utilities.AuditLogUtilities;
import it.eng.spagobi.tools.catalogue.dao.IMetaModelsDAO;
import it.eng.spagobi.tools.catalogue.dao.MetaModel;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.util.HashMap;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class SaveMetaModelAction extends AbstractSpagoBIAction {

	// logger component
	public static Logger logger = Logger.getLogger(SaveMetaModelAction.class);

	public static String ID = "id";
	public static String NAME = "name";
	public static String DESCRIPTION = "description";
	
	@Override
	public void doService() {
		
		logger.debug("IN");
		
		try {
		
			IMetaModelsDAO dao = DAOFactory.getMetaModelsDAO();
			dao.setUserProfile(this.getUserProfile());
			
			MetaModel model = getMetaModelFromRequest();
			LogMF.debug(logger, "Model read from request : [{0}]", model);
			
			HashMap logParameters = new HashMap<String, String>();
			logParameters.put("MODEL", model.toString());
			String logOperation = null;
			
			try {
				if (isNew(model)) {
					MetaModel existing = dao.loadMetaModelByName(model.getName());
					if (existing != null) {
						logger.debug("A meta model with name already exists");
						throw new SpagoBIServiceException(SERVICE_NAME, "A meta model with name already exists");
					}
					logOperation = "META_MODEL_CATALOGUE.ADD";
					dao.insertMetaModel(model);
					logger.debug("Model [" + model + "] inserted");
				} else {
					logOperation = "META_MODEL_CATALOGUE.MODIFY";
					dao.modifyMetaModel(model);
					logger.debug("Model [" + model + "] updated");
				}
			} catch (SpagoBIServiceException e) {
				throw e;
			} catch (Throwable t) {
				AuditLogUtilities.updateAudit(getHttpRequest(), this.getUserProfile(), logOperation, logParameters , "KO");
				throw new SpagoBIServiceException(this.getActionName(), "Error while saving meta model", t);
			}
			
			AuditLogUtilities.updateAudit(getHttpRequest(), this.getUserProfile(), logOperation, logParameters , "OK");
			
			try {
				JSONObject response = new JSONObject();
				response.put("id", model.getId());
				writeBackToClient( new JSONSuccess(response) );
			} catch (Exception e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the response to the client", e);
			}
			
		} finally {
			logger.debug("OUT");
		}
		
	}
	
	private boolean isNew(MetaModel model) {
		return model.getId() == 0;
	}

	private MetaModel getMetaModelFromRequest() {
		Integer id = getAttributeAsInteger( ID );
		String name = getAttributeAsString( NAME );
		String description = getAttributeAsString( DESCRIPTION );
		MetaModel model = new MetaModel();
		model.setId(id);
		model.setName(name);
		model.setDescription(description);
		
		FileItem uploaded = (FileItem) getAttribute("UPLOADED_FILE");
		if (uploaded == null) {
			throw new SpagoBIEngineServiceException(getActionName(), "No file was uploaded");
		}
		
		return model;
	}
	
}
