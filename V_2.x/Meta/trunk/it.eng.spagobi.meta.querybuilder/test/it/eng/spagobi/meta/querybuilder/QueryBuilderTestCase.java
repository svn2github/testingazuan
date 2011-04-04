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
package it.eng.spagobi.meta.querybuilder;

import java.io.File;

import org.junit.Assert;

import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;
import junit.framework.TestCase;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QueryBuilderTestCase extends TestCase {
	
	protected DBConnection connection;
	protected IDataSource dataSource;
	
	protected String modelName;
	protected String testEntityUniqueName;
	
	
	public static String CONNECTION_DIALECT = "org.hibernate.dialect.MySQLDialect";
	public static String CONNECTION_DRIVER = "com.mysql.jdbc.Driver";
	public static String CONNECTION_URL = "jdbc:mysql://localhost:3306/foodmart";
	public static String CONNECTION_USER = "root";
	public static String CONNECTION_PWD = "mysql";
	
	private static final String QBE_FILE = "test-resources/jpa/datamart.jar";
	
	protected void setUp() throws Exception {
		super.setUp();
		
		connection = new DBConnection();			
		connection.setName( "foodmart" );
		connection.setDialect(CONNECTION_DIALECT );			
		connection.setDriverClass( CONNECTION_DRIVER  );	
		connection.setUrl( CONNECTION_URL );
		connection.setUsername( CONNECTION_USER );		
		connection.setPassword( CONNECTION_PWD );
		
		setUpDataSource();
	}

	protected void setUpDataSource() {
		IDataSourceConfiguration configuration;
		
		modelName = "foodmart"; 
		
		File file = new File(QBE_FILE);
		configuration = new FileDataSourceConfiguration(modelName, file);
		configuration.loadDataSourceProperties().put("connection", connection);
		dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
		
		testEntityUniqueName = "it.eng.spagobi.meta.Customer::Customer";
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		connection = null;
		tearDownDataSource();
	}
	
	protected void tearDownDataSource() {
		dataSource.close();
		dataSource  = null;
	}
}
