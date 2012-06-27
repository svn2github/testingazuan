/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.wizard;

import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.meta.querybuilder.oda.Connection;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
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
	 * It look also if exist alreay a query
	 * @param dataSetDesign the wizard page
	 */
	protected void initQueryBuilder(DataSetDesign dataSetDesign ){
		Query query;
		String queryText;
		Connection connection = null;
		if(dataSetDesign!=null){
			try {
				it.eng.spagobi.meta.querybuilder.oda.Driver customDriver = new it.eng.spagobi.meta.querybuilder.oda.Driver();
				connection = (Connection)customDriver.getConnection( null );
				java.util.Properties connProps =  DesignSessionUtil.getEffectiveDataSourceProperties( dataSetDesign.getDataSourceDesign() );
				connection.open( connProps );

				if(queryBuilder==null){
					queryBuilder = new QueryBuilder(connection.getDatasource());

					//Looks if a query exists..
					//if so.. deserializes and add it in the query builder
					queryText = dataSetDesign.getQueryText();

					if( queryText != null && !queryText.equals("")){
						query =  SerializerFactory.getDeserializer("application/json").deserializeQuery(queryText,getQueryBuilder().getDataSource()) ;
						queryBuilder.setQuery(query);
					}
				}
				connection.close();

			} catch( OdaException e ) {
				logger.error("Error getting the connection");
				throw new SpagoBIRuntimeException("Error getting the connection",e);
			}catch( SerializationException e ) {
				logger.error("Error deserializing query");
				throw new SpagoBIRuntimeException("Error deserializing query",e);
			}
		}
	}	
	
}
