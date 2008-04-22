/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.qbe.initializer.service;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.datasource.QbeDataSourceManager;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.SpagoBIRequest;
import it.eng.spagobi.qbe.initializer.engine.service.QbeEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


public class InitializeQbeAction extends AbstractQbeEngineAction {

	// INPUT PARAMETERS
	
	public static final String IP_JNDI_DS = "JNDI_DS";
	public static final String IP_DIALECT = "DIALECT";
	public static final String IP_PATH = "PATH";
	public static final String IP_QUERY_ID = "queryId";
	
	public static final String IP_SPAGOBI_REQUEST = QbeEngineStartAction.OP_SPAGOBI_REQUEST;
	public static final String IP_DATAMART_PROPERTIES = QbeEngineStartAction.OP_DATAMART_PROPERTIES;
	
		
	// OUTPUT PARAMETERS
		
	// SESSION PARAMETRES	
	
	

	public void service(SourceBean request, SourceBean response) throws EngineException  {
		super.service(request, response);		
		
		IDataSource dataSource = null;
		Locale qbeLocale = null;
		String jndiDataSourceName = null;
		Map dblinkMap = null;
		DataMartModelAccessModality dataMartModelAccessModality = null;
		Properties properties = null;
		ISingleDataMartWizardObject datamartWizard = null;
		String userId = null;
		List datamartNames = null;
		boolean standaloneMode;
		
		String dataSourceName = getAttributeAsString(IP_JNDI_DS);
		String dialect = getAttributeAsString(IP_DIALECT);		
		String path = getAttributeAsString(IP_PATH);	
		String queryId = getAttributeAsString(IP_QUERY_ID);
		SpagoBIRequest spagoBIRequest = (SpagoBIRequest)request.getAttribute(IP_SPAGOBI_REQUEST);
		String propertiesStr = getAttributeAsString(IP_DATAMART_PROPERTIES);
		
		DBConnection connection = new DBConnection();
		
		if(spagoBIRequest != null) {
			standaloneMode = false;
			setSpagoBIRequest(spagoBIRequest);
			
			userId = spagoBIRequest.getUserId();
			
			dialect = spagoBIRequest.getTemplate().getDialect();
			datamartNames = spagoBIRequest.getTemplate().getDatamartNames();
			queryId = spagoBIRequest.getQueryId();
			
			qbeLocale = spagoBIRequest.getLocale();
			
			connection.setName(spagoBIRequest.getDataSource().getLabel());
			connection.setDialect(dialect);			
			connection.setJndiName(spagoBIRequest.getDataSource().getJndiName());			
			connection.setDriverClass(spagoBIRequest.getDataSource().getDriver());			
			connection.setPassword(spagoBIRequest.getDataSource().getPassword());
			connection.setUrl(spagoBIRequest.getDataSource().getUrl());
			connection.setUsername(spagoBIRequest.getDataSource().getUser());
			
			dblinkMap = spagoBIRequest.getTemplate().getDblinkMap();
			dataMartModelAccessModality = spagoBIRequest.getTemplate().getDatamartModelAccessModality();
			
			if(propertiesStr != null) {
				properties = new Properties();
				try {
					properties.load(new ByteArrayInputStream(propertiesStr.getBytes()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		} else  {
			standaloneMode = true;
			
			datamartNames = new ArrayList();
			datamartNames.add(path);
			
			qbeLocale = Locale.getDefault();
			
			connection.setName(dataSourceName);
			connection.setDialect(dialect);
			connection.setJndiName( QbeEngineConf.getInstance().getJndiConnectionName(dataSourceName) );
			
			dblinkMap = new HashMap();
			dataMartModelAccessModality = null;
			
			properties = new Properties();
		}
		
		setQbeEngineLocale(qbeLocale);							
		
		dataSource = QbeDataSourceManager.getInstance().getDataSource(datamartNames, dblinkMap,  connection);
				
		DataMartModel dmModel = new DataMartModel(dataSource);
		if(properties != null) {
			dmModel.setDataMartProperties(properties); 
		}
		if(dataMartModelAccessModality != null) {
			dmModel.setDataMartModelAccessModality(dataMartModelAccessModality);
		}
		dmModel.setName(dmModel.getDataSource().getDatamartName());
		dmModel.setDescription(dmModel.getDataSource().getDatamartName());
		
		
		
		ApplicationContainer.getInstance().setAttribute("CURRENT_THREAD_CONTEXT_LOADER", Thread.currentThread().getContextClassLoader());
			
		
		if ((queryId == null) || ((queryId != null) && (queryId.equalsIgnoreCase("#")))){
			datamartWizard = new SingleDataMartWizardObjectSourceBeanImpl();
		} else {
			datamartWizard = dmModel.getQuery(queryId);
		}
		
		setUserId(userId);
		setDatamartModel(dmModel);	
		setDatamartWizard(datamartWizard);  
		setStandaloneModeActive(standaloneMode);
		
		
	}
}
