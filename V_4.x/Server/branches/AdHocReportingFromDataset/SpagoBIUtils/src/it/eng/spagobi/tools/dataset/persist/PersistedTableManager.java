/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.dataset.persist;

import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.metadata.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.metadata.IMetaData;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.utilities.temporarytable.TemporaryTableManager;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;



/** Functions that manage the persistence of the dataset
 * 
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */

public class PersistedTableManager {

	private static transient Logger logger = Logger.getLogger(PersistedTableManager.class);
	public static final String DIALECT_MYSQL = "MySQL";
	public static final String DIALECT_POSTGRES = "PostgreSQL";
	public static final String DIALECT_ORACLE = "OracleDialect";
	public static final String DIALECT_HSQL = "HSQL";
	public static final String DIALECT_ORACLE9i10g = "Oracle9Dialect";
	public static final String DIALECT_SQLSERVER = "SQLServer";
	public static final String DIALECT_DB2 = "DB2";
	public static final String DIALECT_INGRES = "Ingres";
	public static final String DIALECT_TERADATA = "Teradata";

	public void persistDataSet(IDataSet dataset, IDataSource dsPersist) {
		logger.debug("IN");

		// get persisted table name
		String tableName = dataset.getLabel();
		logger.debug("Persisted table name is [" + tableName + "]");
		

		String signature = dataset.getSignature();
		logger.debug("Dataset signature : " + signature);
		if (signature != null && signature.equals(TemporaryTableManager.getLastDataSetSignature(tableName))) {
			// signature matches: no need to create a Persistent Table
			logger.debug("Signature matches: no need to create a Persistent Table");
			//return TemporaryTableManager.getLastDataSetTableDescriptor(tableName);
			return;
		}
		//drop the persisted table if one exists
		try {
			logger.debug("Signature does not match: dropping PersistedTable " + tableName + " if it exists...");
			TemporaryTableManager.dropTableIfExists(tableName, dsPersist);
		} catch (Exception e) {
			logger.error("Impossible to drop the temporary table with name " + tableName, e);
			throw new SpagoBIEngineRuntimeException("Impossible to drop the persisted table with name " + tableName, e);
		}
			
		
		if (dataset.getDsType().equalsIgnoreCase("QUERY")){			
			//for dataset of query type uses a "create table as select ..." statement.
			Connection connection = null;
			try{
				connection = dsPersist.getConnection();
			} catch (Exception e) {
				logger.error("Cannot get connection to target datasource. " , e);
				throw new SpagoBIEngineRuntimeException("Cannot get connection to datasource", e);
			}						
			dataset.persist(tableName, connection);
		}else{
			//for dataset not query type uses a batch statement
			dataset.loadData();
			IDataStore datastore = dataset.getDataStore();
			perstistNoQueryDatasetType(tableName, datastore, dsPersist);
		}		
	}
	
	private void perstistNoQueryDatasetType(String tableName, IDataStore datastore, IDataSource datasource){
		logger.debug("IN");
		Connection connection = null;
		String query = "";
		try{
			try{
				connection = datasource.getConnection();
			} catch (Exception e) {
				logger.error("Cannot get connection to target datasource. " , e);
				throw new SpagoBIEngineRuntimeException("Cannot get connection to datasource", e);
			}
			Statement statemenet = connection.createStatement();
			query = "create table " + tableName + " (" ;
			IMetaData md = datastore.getMetaData();
			for (int i=0, l=md.getFieldCount(); i<l; i++){
				 IFieldMetaData fmd = md.getFieldMeta(i);
				 query += " " + fmd.getAlias() + getDBFieldType(fmd.getType());	
				 query += " " + fmd.getAlias() + ((i<l)?" , " : "");	
			}
			query += " )";
			executeStatement(query, datasource);
			
			
			 
		/*	for (Employee employee: employees) {
			    String query = "insert into employee (name, city) values('"
			            + employee.getName() + "','" + employee.getCity + "')";
			    statemenet.addBatch(query);
			}*/
			statemenet.executeBatch();
			statemenet.close();
			connection.close();
		} catch (Exception e) {
			logger.error("Error peristing the temporary table", e);
			throw new SpagoBIEngineRuntimeException("Error peristing the temporary table", e);
		}
		logger.debug("OUT");
	}
	
	private String getDBFieldType(Object type){
		String toReturn = "";
		
		return toReturn;
	}
		/*

	public Connection getConnection() {
		try {
			IDataSource datasource = this.getDataSource();
			Boolean multiSchema = datasource.getMultiSchema();
			logger.debug("Datasource is multischema: " + multiSchema);
			String schema;
			if (multiSchema == null || !multiSchema.booleanValue()) {
				schema = null;
			} else {
				String attributeName = datasource.getSchemaAttribute();
				logger.debug("Datasource multischema attribute name: " + attributeName);
				UserProfile userProfile = (UserProfile)getEnv().get(EngineConstants.ENV_USER_PROFILE);
				logger.debug("Looking for attribute " + attributeName + " for user " + userProfile + " ...");
				Object attributeValue = userProfile.getUserAttribute(attributeName);
				logger.debug("Atribute " + attributeName + " for user " + userProfile.getUserId() + " is " + attributeValue);
				if (attributeValue == null) {
					throw new RuntimeException("No attribute with name " + attributeName + " found for user " + userProfile.getUserId());
				} else {
					schema = attributeValue.toString();
				}
			}
			return this.getDataSource().getConnection(schema);
		} catch (Exception e) {
			throw new SpagoBIEngineRuntimeException("Cannot get connection to datasource", e);
		}
	}
*/
	private static void executeStatement(String sql, IDataSource dataSource) throws Exception {
		logger.debug("IN");
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			logger.debug("Executing sql " + sql);
			stmt.execute(sql);
			connection.commit();
			logger.debug("Sql " + sql + " executed successfully");
		} catch (Exception e ) {
			if (connection != null) {
				connection.rollback();
			}
			throw e;
		} finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
			logger.debug("OUT");
		}
	}
}
