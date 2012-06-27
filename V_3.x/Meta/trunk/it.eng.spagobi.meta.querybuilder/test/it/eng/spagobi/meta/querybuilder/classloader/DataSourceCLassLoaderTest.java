/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.classloader;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.querybuilder.AbtractQueryBuilderTestCase;
import it.eng.spagobi.meta.querybuilder.TestCaseConstants;

import java.io.File;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class DataSourceCLassLoaderTest extends AbtractQueryBuilderTestCase {

	// How many time creation/deletion loop must iterate
	public static final int ITERATION_NUMBER = 10;
	
	public static final File TEST_INPUT_FOLDER = new File(TestCaseConstants.TEST_FOLDER, "models/keys");
	
	public static final File TEST_OUTPUT_FOLDER = new File(TestCaseConstants.TEST_OUPUT_ROOT_FOLDER, "classloader");
	
	// a model with no keys but with relationships
	private static final File TEST_MODEL_FILE1 = new File(TEST_INPUT_FOLDER, "NoKey.sbimodel");
	private static final File TEST_MODEL_FILE2 = new File(TEST_INPUT_FOLDER, "SimpleKey.sbimodel");

	private static final File jarFolder1 = new File(TEST_OUTPUT_FOLDER, "jar1");
	private static final File jarFolder2 = new File(TEST_OUTPUT_FOLDER, "jar2");
	

	protected void setUp() throws Exception {
		modelName = "My Model";
		super.setUp();
		File jarFile1 = new File(jarFolder1, "datamart.jar");
		File jarFile2 = new File(jarFolder2, "datamart.jar");
		if(jarFile1.exists()) {
			try {
				doDeleteJarFileTest(jarFile1);
			} catch(Throwable t) {
				System.err.println("SET-UP: impossible to delete an old version of datamart.jar");
				throw new Exception(t);
			}
			System.err.println("SET-UP: delelted an old version of datamart.jar");
		} else {
			System.err.println("SET-UP: Test output folder is empty");
		}
		if(jarFile2.exists()) {
			try {
				doDeleteJarFileTest(jarFile2);
			} catch(Throwable t) {
				System.err.println("SET-UP: impossible to delete an old version of datamart.jar");
				throw new Exception(t);
			}
			System.err.println("SET-UP: delelted an old version of datamart.jar");
		} else {
			System.err.println("SET-UP: Test output folder is empty");
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDataSourceCLassLoader() throws Exception {
		ClassLoader cl1, cl2;
		Class employeePKClass1,employeePKClass2;
		boolean classNotFound =  false;
		try {
			doDataSourceCreationTest( TEST_MODEL_FILE1, jarFolder1 );	
			cl1 =  Thread.currentThread().getContextClassLoader();
			employeePKClass1 = cl1.loadClass("it.eng.spagobi.meta.EmployeePK");
			setUpGenarator();
			doDataSourceCreationTest( TEST_MODEL_FILE2, jarFolder2 );	
			cl2 =  Thread.currentThread().getContextClassLoader();
			try {
				employeePKClass2 = cl2.loadClass("it.eng.spagobi.meta.EmployeePK");
			} catch (ClassNotFoundException e) {
				classNotFound = true;
			}
			assertTrue("Error: the two class loaders are equals", cl1!=cl2);//!=lock the id.. it's ok in this case
			assertTrue("Error: the second loader found the class EmployeePK", classNotFound);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

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

	    boolean fileDeletionResult = file.delete();
	    if(!fileDeletionResult) {
	    	throw new SpagoBIPluginException("Can't delete the file "+ file.getAbsolutePath()+" the file is writtable? "+file.canWrite() +" "+file.canExecute());
	    }
	   
	}
	
}
