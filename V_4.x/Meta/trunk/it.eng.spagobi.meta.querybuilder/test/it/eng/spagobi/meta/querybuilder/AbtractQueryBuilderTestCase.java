/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder;

import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriverWithClassLoader;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbtractQueryBuilderTestCase extends TestCase {
	
	protected ClassLoader classLoader;
	protected it.eng.spagobi.tools.datasource.bo.IDataSource dataSource;
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
		System.err.println("-- SET-UP --------------------------------");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSerializer();
		tearDownConncetion();
		tearDownClassLoader();
		
		System.err.println("-- TEAR-DOWN --------------------------------");
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
		dataSource = new DataSource();			
		
		dataSource.setLabel(modelName);
		dataSource.setHibDialectClass( TestCaseConstants.CONNECTION_DIALECT  );			
		dataSource.setDriver( TestCaseConstants.CONNECTION_DRIVER );			
		dataSource.setPwd( TestCaseConstants.CONNECTION_PWD  );
		dataSource.setUrlConnection( TestCaseConstants.CONNECTION_URL );
		dataSource.setUser(TestCaseConstants.CONNECTION_USER );
		
	}
	
	protected void tearDownConncetion() {
		dataSource = null;
	}
	
	protected IDataSource createDataSource(File jarFile) {
		IDataSourceConfiguration configuration;
		configuration = new FileDataSourceConfiguration(modelName, jarFile);
		configuration.loadDataSourceProperties().put("datasource", dataSource);
		IDataSource dataSource = DriverManager.getDataSource(JPADriverWithClassLoader.DRIVER_ID, configuration, false);
		
		return dataSource;
	}
}
