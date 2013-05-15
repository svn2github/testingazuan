/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.adhocreporting.services;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.dataset.constants.DataSetConstants;
import it.eng.spagobi.tools.dataset.dao.IDataSetDAO;
import it.eng.spagobi.tools.dataset.service.ManageDatasets;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * @author Zerbetto Davide (davide.zerbetto@eng.it)
 * 
 *         This action is intended for final users; it saves a new dataset.
 * 
 */
public class SaveDatasetUserAction extends ManageDatasets {

	public static final String SERVICE_NAME = "SAVE_DATASET_USER_ACTION";
	
	// logger component
	private static Logger logger = Logger.getLogger(SaveDatasetUserAction.class);

	public void doService() {
		logger.debug("IN");
		try {
			
			IDataSetDAO dao;
			IEngUserProfile profile = getUserProfile();
			try {
				dao = DAOFactory.getDataSetDAO();
				dao.setUserProfile(profile);
			} catch (EMFUserError e) {				
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot access database", e);
			}
			Locale locale = getLocale();
			
			// add extra information into request (more secure on server-side instead of client side)
			try {
				this.getRequestContainer().getServiceRequest().setAttribute(DataSetConstants.DS_TYPE_CD, "Qbe");
			} catch (SourceBeanException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot modify request", e);
			}
			
			GuiGenericDataSet ds = getGuiGenericDatasetToInsert();
			String label = sendToTiLABConsole(ds);
			
			// add extra information into request (more secure on server-side instead of client side)
			try {
				this.getRequestContainer().getServiceRequest().setAttribute(DataSetConstants.LABEL, label);
			} catch (SourceBeanException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot modify request", e);
			}
			
			// invoke save service method
			datatsetInsert(dao, locale);
			
		} finally {
			logger.debug("OUT");
		}
	}

	private String sendToTiLABConsole(GuiGenericDataSet ds) {
		// TODO: TiLAB: mandare il dataset alla console e recuperare il dataset salvato
		return ds.getLabel();
	}

}
