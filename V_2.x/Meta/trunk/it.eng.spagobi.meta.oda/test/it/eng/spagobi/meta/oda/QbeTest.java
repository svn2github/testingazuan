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

package it.eng.spagobi.meta.oda;



import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.hibernate.HibernateDriver;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ModelEntity;
import it.eng.qbe.model.structure.ModelField;
import it.eng.qbe.model.structure.ModelStructure;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;
import it.eng.qbe.statement.QbeDatasetFactory;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTest {
	public static final String HIBERNATE_SIMPLE_QBE_FILE = "D:\\Documenti\\Sviluppo\\servers\\tomcat6spagobi3\\resources\\qbe\\datamarts\\foodmart\\datamart.jar";
	public static final String DATAMART_NAME = "foodmart";
	
	public static String CONNECTION_DIALECT = "org.hibernate.dialect.MySQLDialect";
	public static String CONNECTION_DRIVER = "com.mysql.jdbc.Driver";
	public static String CONNECTION_URL = "jdbc:mysql://localhost:3306/foodmart";
	public static String CONNECTION_USER = "root";
	public static String CONNECTION_PWD = "mysql";

	public static void main(String[] args) {
		
		IDataSourceConfiguration configuration;
		IDataSource hibernateSimpleDataSource; 
		
		File file = new File(HIBERNATE_SIMPLE_QBE_FILE);
		
		configuration = new FileDataSourceConfiguration("foodmart", file);
		
		DBConnection connection = new DBConnection();			
		connection.setName( "foodmart" );
		connection.setDialect(CONNECTION_DIALECT );			
		connection.setDriverClass( CONNECTION_DRIVER  );	
		connection.setUrl( CONNECTION_URL );
		connection.setUsername( CONNECTION_USER );		
		connection.setPassword( CONNECTION_PWD );
		configuration.loadDataSourceProperties().put("connection", connection);
		
		hibernateSimpleDataSource = DriverManager.getDataSource(HibernateDriver.DRIVER_ID, configuration);
		
		Query query = new Query();
		
		IModelStructure modelStructure = hibernateSimpleDataSource.getModelStructure();
		List entities = modelStructure.getRootEntities(DATAMART_NAME);
		if(entities.size() > 0) {
			ModelEntity entity = (ModelEntity)entities.get(0);
			List fields = entity.getAllFields();
			for(int i = 0; i < fields.size(); i++) {
				ModelField field = (ModelField)fields.get(i);

				query.addSelectFiled(field.getUniqueName(), null, field.getName(), true, true, false, null, null);			
			}
		}
		
		IStatement statement = hibernateSimpleDataSource.createStatement(query);
		IDataSet datsSet = QbeDatasetFactory.createDataSet(statement);
		
		try {
			datsSet.loadData();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		IDataStore dataStore = datsSet.getDataStore();
		
		//JSONDataWriter dataSetWriter = new JSONDataWriter();
		//JSONObject output = (JSONObject)dataSetWriter.write(dataStore);
		
		System.out.println(dataStore.getRecordsCount());
	}

}
