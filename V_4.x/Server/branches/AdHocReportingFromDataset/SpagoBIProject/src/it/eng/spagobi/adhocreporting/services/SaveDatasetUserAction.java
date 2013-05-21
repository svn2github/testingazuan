/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.adhocreporting.services;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.rest.client.api.TilabClientAPI;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.dataset.constants.DataSetConstants;
import it.eng.spagobi.tools.dataset.dao.IDataSetDAO;
import it.eng.spagobi.tools.dataset.service.ManageDatasets;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

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
				UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
				UUID uuid = uuidGen.generateTimeBasedUUID();
				this.getRequestContainer().getServiceRequest().setAttribute(DataSetConstants.PERSIST_TABLE_NAME, uuid.toString());
			} catch (SourceBeanException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot modify request", e);
			}
			
			GuiGenericDataSet ds = getGuiGenericDatasetToInsert();
			String label = saveIntoTilabConsole(ds);
			ds.setLabel(label);
			// invoke save service method
			datatsetInsert(ds, dao, locale);
			
		} finally {
			logger.debug("OUT");
		}
	}

	private String saveIntoTilabConsole(GuiGenericDataSet ds) {
		return ds.getLabel();
//		try {
//			TilabClientAPI api = new TilabClientAPI();
//			String datasourceLabel = ds.getActiveDetail().getDataSourcePersist();
//			DataSource dataSource = (DataSource) DAOFactory.getDataSourceDAO().loadDataSourceByLabel(datasourceLabel);
//			String label = api.writeDataset(dataSource, ds);
//			logger.debug("Dataset saved with label [" + label + "]");
//			return label;
//		} catch (Exception e) {
//			throw new SpagoBIRuntimeException("Error while saving dataset into Tilab console", e);
//		}
	}

}
