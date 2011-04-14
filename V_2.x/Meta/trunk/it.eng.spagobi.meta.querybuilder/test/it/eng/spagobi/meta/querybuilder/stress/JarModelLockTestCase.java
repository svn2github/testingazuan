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
package it.eng.spagobi.meta.querybuilder.stress;

import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;
import it.eng.spagobi.meta.querybuilder.TestCaseConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import junit.framework.TestCase;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class JarModelLockTestCase extends TestCase {

	IModelSerializer serializer;
	JpaMappingJarGenerator jarGenerator;	
	DBConnection connection;
	
	
	// a model with no keys but with relatioships
	private static final File TEST_MODEL_RELNOKEY = new File(TestCaseConstants.TEST_FOLDER, "models/a.sbimodel");
	


	protected void setUp() throws Exception {
		super.setUp();
		serializer = new EmfXmiSerializer();		
		jarGenerator = new JpaMappingJarGenerator();
		jarGenerator.setLibDir(new File(TestCaseConstants.TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		jarGenerator.setLibs(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});	
		
		connection = new DBConnection();			
		connection.setName( "a" );
		connection.setDialect( TestCaseConstants.CONNECTION_DIALECT );			
		connection.setDriverClass( TestCaseConstants.CONNECTION_DRIVER  );	
		connection.setUrl( TestCaseConstants.CONNECTION_URL );
		connection.setUsername( TestCaseConstants.CONNECTION_USER );		
		connection.setPassword( TestCaseConstants.CONNECTION_PWD );
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		serializer = null;
		jarGenerator = null;
	}
	
	
	private IDataSource getDataSource(File file) {
		IDataSourceConfiguration configuration;
		configuration = new FileDataSourceConfiguration("a", file);
		configuration.loadDataSourceProperties().put("connection", connection);
		IDataSource dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
		
		return dataSource;
	}
		

	public void testJarModelLock() throws Exception {
		try {
			for(int i=0; i<10; i++){
				doTest(TEST_MODEL_RELNOKEY, new File(TestCaseConstants.TEST_FOLDER, "outs/a"));	
				deleteFile( new File(TestCaseConstants.TEST_FOLDER, "outs/a"));
			}
		} catch (SpagoBIPluginException e) {
			e.printStackTrace();
			fail("Can't delete the file");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	private void deleteFile(File file){
		if(!file.exists()){
			return;
		}
	    if (file.isDirectory()) {
	    	File[] files = file.listFiles();
	    	for(int i=0; i<files.length; i++){
	    		deleteFile(files[i]);
	    	}
	    }
	  
	    
		while(!file.delete()) {
			System.err.println(".");
		}
		/*
	    boolean fileDeletionResult = file.delete();
	    if(!fileDeletionResult) {
	    	throw new SpagoBIPluginException("Can't delete the file "+ file.getAbsolutePath()+" the file is writtable? "+file.canWrite() +" "+file.canExecute());
	    }
	    */
	}
	
	private void doTest(File modelFile, File outputDir) throws Exception {
		Model model = serializer.deserialize(modelFile);
		jarGenerator.generate(model.getBusinessModels().get(0), outputDir.getAbsolutePath());

		File file = new File(jarGenerator.getDistDir(), "datamart.jar");
		IDataSource dataSource = null;
		try {
			dataSource = getDataSource(file);
		} catch(Throwable t){
			fail();
		}
		
		dataSource.getModelStructure();
	}
	
}
