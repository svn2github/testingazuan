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
package it.eng.spagobi.engines.console.services;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.engines.console.ConsoleEngineInstance;
import it.eng.spagobi.services.proxy.DataSetServiceProxy;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datawriter.JSONDataWriter;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;




/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class GetWarningListAction extends AbstractConsoleEngineAction {
	
	public static final String SERVICE_NAME = "GET_WARNING_LIST";
	
	
	// request parameters
	//public static String ERRORS_DATASET_LABEL = "consoleWarnings";
	public static String DATASET_LABEL = "ds_label"; 
	public static String ERRORS_DETAIL_COLUMN = "detailColumn"; //alias for the detail column
	public static String ID = "id";
	
	public static String USER_ID = "userId";
	public static String CALLBACK = "callback";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetWarningListAction.class);
	
	 
	public void service(SourceBean request, SourceBean response) {
		
		String dataSetLabel;
		String user;
		String callback;
		String rowId;
		
		IDataSet dataSet;
		IDataStore dataStore;
		JSONObject dataSetJSON;
		
		
		logger.debug("IN");
		Monitor monitor =MonitorFactory.start("SpagoBI_Console.GetWarningListAction.service");	
		
		try {
			super.service(request,response);
			ConsoleEngineInstance consoleEngineInstance = getConsoleEngineInstance();
		
			dataSetLabel = getAttributeAsString( DATASET_LABEL );
			logger.debug("Parameter [" + DATASET_LABEL + "] is equals to [" + dataSetLabel + "]");			
			Assert.assertTrue(!StringUtilities.isEmpty( dataSetLabel ), "Parameter [" + DATASET_LABEL + "] cannot be null or empty");
			
			callback = getAttributeAsString( CALLBACK );
			logger.debug("Parameter [" + CALLBACK + "] is equals to [" + callback + "]");
			
			rowId = getAttributeAsString( ID );
			logger.debug("Parameter [" + ID + "] is equals to [" + rowId + "]");
			Assert.assertNotNull(rowId, "Input parameters [" + ID + "] cannot be null");
			
			dataSet = null;
			try {
				dataSet = getDataSet(dataSetLabel);
			} catch(Throwable t) {
				throw new SpagoBIServiceException("Impossible to find a dataset whose label is [" + dataSetLabel + "]", t);
			}
			Assert.assertNotNull(dataSet, "Impossible to find a dataset whose label is [" + dataSetLabel + "]");
				/*
			Map params = new HashMap();
			params.put("id", rowId);
			dataSet.setParamsMap(params);
			*/
			Map params = consoleEngineInstance.getAnalyticalDrivers();
			params.put("id", rowId);
			dataSet.setParamsMap(params);
			dataSet.setUserProfile((UserProfile)consoleEngineInstance.getEnv().get(EngineConstants.ENV_USER_PROFILE));
			Monitor monitorLD =MonitorFactory.start("SpagoBI_Console.GetWarningListAction.service.LoadData");	
			dataSet.loadData();
			monitorLD.stop();
			dataStore = dataSet.getDataStore();
			Assert.assertNotNull(dataStore, "The dataStore returned by loadData method of the class [" + dataSet.getClass().getName()+ "] cannot be null");
					
			dataStore.getMetaData().setProperty("detailProperty", ERRORS_DETAIL_COLUMN);
			//int fieldIndex = dataStore.getMetaData().getFieldIndex(ERRORS_DETAIL_COLUMN);
			//dataStore.getMetaData().getFieldMeta(fieldIndex).setProperty("detail", true);
			
			
			
			dataSetJSON = null;
			
			try {
				JSONDataWriter writer = new JSONDataWriter();
				
				Object resultNumber = dataStore.getMetaData().getProperty("resultNumber");
				if(resultNumber == null) dataStore.getMetaData().setProperty("resultNumber", new Integer((int)dataStore.getRecordsCount()));
				
				
				dataSetJSON = (JSONObject)writer.write(dataStore);
			} catch (Throwable e) {
				throw new SpagoBIServiceException("Impossible to serialize datastore", e);
			}
			
			try {
				writeBackToClient( new JSONSuccess( dataSetJSON, callback ) );
			} catch (IOException e) {
				throw new SpagoBIServiceException("Impossible to write back the responce to the client", e);
			}
		} catch (Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			monitor.stop();
			logger.debug("OUT");
		}
	}
	
	private IDataSet getDataSet(String label) {
		IDataSet dataSet;
		DataSetServiceProxy datasetProxy;
		
		datasetProxy = getConsoleEngineInstance().getDataSetServiceProxy();
		dataSet =  datasetProxy.getDataSetByLabel(label);
		
		return dataSet;
	}

}
