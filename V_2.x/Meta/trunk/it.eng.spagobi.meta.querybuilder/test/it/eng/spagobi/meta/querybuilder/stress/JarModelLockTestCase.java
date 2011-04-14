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
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;
import it.eng.spagobi.meta.querybuilder.AbtractQueryBuilderTestCase;
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
public class JarModelLockTestCase extends AbtractQueryBuilderTestCase {

	// How many time creation/deletion loop must iterate
	public static final int ITERATION_NUMBER = 10;
	
	public static final File TEST_INPUT_FOLDER = new File(TestCaseConstants.TEST_FOLDER, "models/stress");
	public static final File TEST_OUTPUT_FOLDER = new File(TestCaseConstants.TEST_OUPUT_ROOT_FOLDER, "stress");
	
	// a model with no keys but with relationships
	private static final File TEST_MODEL_FILE = new File(TEST_INPUT_FOLDER, "lockTestModel.sbimodel");
	


	protected void setUp() throws Exception {
		modelName = "a";
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testJarModelLock() throws Exception {
		try {
			for(int i = 0; i < ITERATION_NUMBER; i++){
				File outputFolder = new File( TEST_OUTPUT_FOLDER, "lock" );
				doDataSourceCreationTest( TEST_MODEL_FILE, outputFolder );	
				doDeleteJarFileTest( outputFolder );
			}
		} catch (SpagoBIPluginException e) {
			e.printStackTrace();
			fail("Can't delete the file");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	private void doDeleteJarFileTest(File file){
		if(!file.exists()){
			return;
		}
	    if (file.isDirectory()) {
	    	File[] files = file.listFiles();
	    	for(int i=0; i<files.length; i++){
	    		doDeleteJarFileTest(files[i]);
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
	
	private void doDataSourceCreationTest(File modelFile, File outputDir) throws Exception {
		Model model = serializer.deserialize(modelFile);
		jarGenerator.generate(model.getBusinessModels().get(0), outputDir.getAbsolutePath());

		File jarFile = new File(jarGenerator.getDistDir(), "datamart.jar");
		
		IDataSource dataSource = null;
		try {
			dataSource = createDataSource(jarFile);
		} catch(Throwable t){
			t.printStackTrace();
			fail();
		}
		
		IModelStructure structure = null;
		try {
			structure = dataSource.getModelStructure();
		} catch(Throwable t){
			t.printStackTrace();
			fail();
		}
		
	}
	
}
