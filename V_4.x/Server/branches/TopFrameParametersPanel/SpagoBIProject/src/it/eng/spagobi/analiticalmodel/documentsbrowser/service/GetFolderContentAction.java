/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.analiticalmodel.documentsbrowser.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.analiticalmodel.documentsbrowser.utils.FolderContentUtil;
import it.eng.spagobi.utilities.exceptions.SpagoBIException;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 *
 */
public class GetFolderContentAction extends AbstractBaseHttpAction{

	// REQUEST PARAMETERS
	public static final String FOLDER_ID = "folderId";

	// logger component
	private static Logger logger = Logger.getLogger(GetFolderContentAction.class);

	public void service(SourceBean request, SourceBean response) throws Exception {

		logger.debug("IN");

		try {
			setSpagoBIRequestContainer( request );
			setSpagoBIResponseContainer( response );

			String functID = getAttributeAsString(FOLDER_ID);		
			logger.debug("Parameter [" + FOLDER_ID + "] is equal to [" + functID + "]");
			
			FolderContentUtil fcUtil = new FolderContentUtil();
			
			HttpServletRequest httpRequest = getHttpRequest();
			SessionContainer sessCont = getSessionContainer();
			
			JSONObject folderContent = fcUtil.getFolderContent(functID, request, response, httpRequest, sessCont);
			try {
				writeBackToClient( new JSONSuccess( folderContent ) );
			} catch (IOException e) {
				throw new SpagoBIException("Impossible to write back the responce to the client", e);
			}

		} catch (Throwable t) {
			throw new SpagoBIException("An unexpected error occured while executing " + getActionName(), t);
		} finally {
			logger.debug("OUT");
		}
	}
	
}
