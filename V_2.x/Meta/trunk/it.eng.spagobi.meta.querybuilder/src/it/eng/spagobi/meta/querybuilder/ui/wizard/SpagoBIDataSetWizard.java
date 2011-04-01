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

import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.meta.oda.impl.Connection;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
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
				IDriver customDriver = new it.eng.spagobi.meta.oda.impl.Driver();
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
		}else{
			queryBuilder = new QueryBuilder();
		}
	}	
	
}
