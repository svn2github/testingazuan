/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.querybuilder.ui.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.IDataSource;
import it.eng.spagobi.meta.oda.impl.OdaStructureBuilder;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class SpagoBIDataSetWizard extends DataSetWizard {

	protected QueryBuilder queryBuilder;
	
	private static Logger logger = LoggerFactory.getLogger(SpagoBIDataSetWizard.class);
	
	public SpagoBIDataSetWizard(){
		super();
		logger.trace("IN");
		this.setWindowTitle("Create a new SpagoBI Data Set");
		this.setHelpAvailable(false);
	//	queryBuilder = new QueryBuilder();
		logger.trace("OUT");
	}
	
	@Override
	public boolean performFinish() {
		logger.debug("SpagoBIDataSetWizard performFinish");
		return (getPages()[getPageCount()-1].isPageComplete());
	}

	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}
	
	/**
	 * Initializes the query builder: it take the data store properties and create a new data source
	 * @param dataSetDesign the wizard page
	 */
	protected void initQueryBuilder(DataSetDesign dataSetDesign ){
		if(queryBuilder==null){
		    if( dataSetDesign == null ){
		    	queryBuilder = new QueryBuilder(); // nothing to initialize
		    }else{
			    Properties properties =  dataSetDesign.getDataSourceDesign().getPublicProperties();
			    
			    Map<String, Object> dataSourceProperties = new HashMap<String, Object>();
			    
				String modelName = properties.getProperty("datamart_name");
				List<String> modelNames = new ArrayList<String>();
				modelNames.add( modelName );
			
				DBConnection connection = new DBConnection();
				connection.setName( modelName );
				connection.setDialect( properties.getProperty("database_dialect") );			
				connection.setJndiName( null );			
				connection.setDriverClass( properties.getProperty("database_driver") );			
				connection.setPassword( properties.getProperty("database_password") );
				connection.setUrl( properties.getProperty("database_url") );
				connection.setUsername( properties.getProperty("database_user") );	

				dataSourceProperties.put("connection", connection);
				dataSourceProperties.put("dblinkMap", new HashMap());
				IDataSource dataSource = OdaStructureBuilder.getDataSource(modelNames, dataSourceProperties);
				queryBuilder = new QueryBuilder(dataSource);
		    }
		}
	}
}
