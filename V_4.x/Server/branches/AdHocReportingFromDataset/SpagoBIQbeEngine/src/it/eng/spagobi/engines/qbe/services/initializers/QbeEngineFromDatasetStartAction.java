/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.qbe.services.initializers;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.qbe.QbeEngine;
import it.eng.spagobi.engines.qbe.QbeEngineAnalysisState;
import it.eng.spagobi.engines.qbe.QbeEngineInstance;
import it.eng.spagobi.engines.qbe.registry.bo.RegistryConfiguration;
import it.eng.spagobi.engines.qbe.registry.serializer.RegistryConfigurationJSONSerializer;
import it.eng.spagobi.engines.qbe.template.QbeTemplateParseException;
import it.eng.spagobi.engines.worksheet.WorksheetEngineInstance;
import it.eng.spagobi.services.proxy.DataSetServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineStartupException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 * The Class QbeEngineFromDatasetStartAction.
 * Called when opening QBE engine by passing a datase, not a document.
 * 
 * @author Giulio Gavardi
 */
public class QbeEngineFromDatasetStartAction extends QbeEngineStartAction {	
	
	// INPUT PARAMETERS
	
	// OUTPUT PARAMETERS
	public static final String LANGUAGE = "LANGUAGE";
	public static final String COUNTRY = "COUNTRY";
	
	// SESSION PARAMETRES	
	public static final String ENGINE_INSTANCE = EngineConstants.ENGINE_INSTANCE;
	public static final String REGISTRY_CONFIGURATION = "REGISTRY_CONFIGURATION";
	
	// INPUT PARAMETERS
	
	// The passed dataset label 
	public static final String DATASET_LABEL = "dataset_label";
	// label of default datasource associated to Qbe Engine
	public static final String DATASOURCE_LABEL = "datasource_label";
// label of datasource associate to the dataset
	public static final String SELECTED_DATASOURCE_LABEL = "selected_datasource_label";
	
	
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(QbeEngineFromDatasetStartAction.class);
    
    public static final String ENGINE_NAME = "SpagoBIQbeEngine";
				
	
	
	@Override
	public IDataSet getDataSet() {
		logger.debug("IN");
		// dataset information is coming with the request
		String datasetLabel = this.getAttributeAsString( DATASET_LABEL );
		logger.debug("Parameter [" + DATASET_LABEL + "]  is equal to [" + datasetLabel + "]");
		Assert.assertNotNull(datasetLabel, "Dataset not specified");
		IDataSet dataSet = getDataSetServiceProxy().getDataSetByLabel(datasetLabel);  	
		logger.debug("OUT");
		return dataSet;
	}

	@Override
	// this is engine default datasource (not of interest inthis case)
	public IDataSource getDataSource() {
//		logger.debug("IN");
//		// datasource information is coming with the request
//		String datasourceLabel = this.getAttributeAsString( DATASOURCE_LABEL );
//		logger.debug("Parameter [" + DATASOURCE_LABEL + "]  is equal to [" + datasourceLabel + "]");
//		Assert.assertNotNull(datasourceLabel, "Data source not specified");
//		IDataSource dataSource = getDataSourceServiceProxy().getDataSourceByLabel(datasourceLabel);
//		logger.debug("oUT");
//		return dataSource;
		return null;
	}


	@Override
	public String getDocumentId() {
		// there is no document at the time
		return null;
	}   

// no template in this use case
	 public SourceBean getTemplateAsSourceBean() {
		 SourceBean templateSB = null;
		 return templateSB;
	 }
	 
	 
	 
	   public Map addDatasetsToEnv(){
			 Map env = super.getEnv();
			 env.put(EngineConstants.ENV_LOCALE, getLocale()); 
			 String datasetLabel = this.getAttributeAsString( DATASET_LABEL);
			 env.put(EngineConstants.ENV_DATASET_LABEL, datasetLabel);
			 
			 // this is datasource in use
			 String datasourceLabel = this.getAttributeAsString( SELECTED_DATASOURCE_LABEL);
			 
			 // substitute default engine's datasource with dataset one
			 IDataSource datSource = getDataSourceServiceProxy().getDataSourceByLabel(datasourceLabel);
			 env.put(EngineConstants.ENV_DATASOURCE, datSource); 

			 DataSetServiceProxy serviceProxy = getDataSetServiceProxy();
			 List<IDataSet> dataSets = new ArrayList<IDataSet>();
			 dataSets.add(serviceProxy.getDataSetByLabel(datasetLabel));
			 env.put(EngineConstants.ENV_DATASETS, dataSets);
			 return env;
	   }
	 
	 
}
