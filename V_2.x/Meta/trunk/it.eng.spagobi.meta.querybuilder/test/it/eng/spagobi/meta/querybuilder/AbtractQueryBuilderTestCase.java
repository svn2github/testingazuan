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

import junit.framework.TestCase;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbtractQueryBuilderTestCase extends TestCase {
	
	protected ClassLoader classLoader;
	protected DBConnection connection;
	protected IModelSerializer serializer;
	protected JpaMappingJarGenerator jarGenerator;	
	protected String modelName;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		if(modelName == null) modelName = "My Model";
		setUpClassLoader();
		setUpConncetion();
		setUpSerializer();
		setUpGenarator();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSerializer();
		tearDownConncetion();
		tearDownClassLoader();
	}
	
	protected void setUpGenarator() {
		jarGenerator = new JpaMappingJarGenerator();
		jarGenerator.setLibDir(new File(TestCaseConstants.TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		jarGenerator.setLibs(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});		
	}
	
	protected void tearDownGenerator() {
		jarGenerator = null;
	}
	
	protected void setUpSerializer() {
		serializer = new EmfXmiSerializer();	
	}
	
	protected void tearDownSerializer() {
		serializer = null;
	}
	
	protected void setUpClassLoader() {
		classLoader = Thread.currentThread().getContextClassLoader();
	}
	
	protected void tearDownClassLoader() {
		Thread.currentThread().setContextClassLoader(classLoader);
	}
	
	protected void setUpConncetion() {
		connection = new DBConnection();			
		connection.setName( "My Model" );
		connection.setDialect( TestCaseConstants.CONNECTION_DIALECT );			
		connection.setDriverClass( TestCaseConstants.CONNECTION_DRIVER  );	
		connection.setUrl( TestCaseConstants.CONNECTION_URL );
		connection.setUsername( TestCaseConstants.CONNECTION_USER );		
		connection.setPassword( TestCaseConstants.CONNECTION_PWD );
	}
	
	protected void tearDownConncetion() {
		connection = null;
	}
	
	protected IDataSource createDataSource(File jarFile) {
		IDataSourceConfiguration configuration;
		configuration = new FileDataSourceConfiguration(modelName, jarFile);
		configuration.loadDataSourceProperties().put("connection", connection);
		IDataSource dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
		
		return dataSource;
	}
}
