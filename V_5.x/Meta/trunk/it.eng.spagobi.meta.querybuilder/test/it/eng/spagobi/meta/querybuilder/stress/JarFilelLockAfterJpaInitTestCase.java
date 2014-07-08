/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.stress;

import it.eng.qbe.classloader.ClassLoaderManager;
import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.querybuilder.AbtractQueryBuilderTestCase;
import it.eng.spagobi.meta.querybuilder.TestCaseConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class JarFilelLockAfterJpaInitTestCase extends AbtractQueryBuilderTestCase {

	// How many time creation/deletion loop must iterate
	public static final int ITERATION_NUMBER = 10;
	
	public static final File TEST_INPUT_FOLDER = new File(TestCaseConstants.TEST_FOLDER, "models/stress");
	public static final File TEST_OUTPUT_FOLDER = new File(TestCaseConstants.TEST_OUPUT_ROOT_FOLDER, "stress");
	
	// a model with no keys but with relationships
	private static final File TEST_JAR_FILE = new File(TEST_INPUT_FOLDER, "datamart.jar");
	


	protected void setUp() throws Exception {
		modelName = "a";
		super.setUp();
		File jarFile = new File(TEST_OUTPUT_FOLDER, "datamart.jar");
		if(jarFile.exists()) {
			try {
				doDeleteJarFileTest(jarFile);
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

	public void testJarFileLock() throws Exception {
		try {
			for(int i = 0; i < ITERATION_NUMBER; i++){
				//tearDownClassLoader();
				copyFile(TEST_JAR_FILE, TEST_OUTPUT_FOLDER);
				File jarFile = new File(TEST_OUTPUT_FOLDER, "datamart.jar");
				doJpaInitializationTest( jarFile );	
				System.err.println("jpa succesfully initialized (iteration " + (i+1) + " of " + ITERATION_NUMBER +")");
				doDeleteJarFileTest( jarFile );
				//System.err.println("Jar file [" + jarFile.getAbsolutePath() + "] deleted succesfully (iteration " + (i+1) + " of " + ITERATION_NUMBER +")");
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
		boolean b = file.delete();
		if(!b) {
			System.err.println("FILE NOT DELETED");
			OutputStream out = null;
			try {
				out = new FileOutputStream(file);
				out.write(1);
			} catch (Throwable t) {
				System.err.println("An error occurred while clearing file content");
			} finally {
				closeOutputStream(out);
			}
			

			 b = file.delete();

			 if(!b) {
				 throw new SpagoBIPluginException("Can't delete the file "+ file.getAbsolutePath()+" the file is writtable? "+file.canWrite() +" "+file.canExecute());
			 } else {
				 System.err.println("NOW THE FILE IS DELETED");
			 }
			 
			 
		}
	}
	
	private void closeOutputStream(OutputStream outputStram) {
		if (outputStram != null) {
			try {
				outputStram.flush();
				outputStram.close();
			} catch (Throwable t) {
				throw new RuntimeException("Impossible to close stream");
			}
		}
	}
	
	private void doJpaInitializationTest(File jarFile) throws Exception {
		ClassLoaderManager.updateCurrentWebClassLoader(jarFile);
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(modelName, buildEmptyConfiguration());
//		EntityManager entityManager = factory.createEntityManager();
//		entityManager.clear();
//		entityManager.close();
		factory.close();
	}
	
	private Map<String,Object> buildEmptyConfiguration() {
		Map<String,Object> cfg = new HashMap<String,Object>();
		if(connectionDescriptor.isJndiConncetion()) {
			cfg.put("javax.persistence.nonJtaDataSource", connectionDescriptor.getJndiName());
		} else {
			cfg.put("javax.persistence.jdbc.url", connectionDescriptor.getUrl());
			cfg.put("javax.persistence.jdbc.password", connectionDescriptor.getPassword());
			cfg.put("javax.persistence.jdbc.user", connectionDescriptor.getUsername());
			cfg.put("javax.persistence.jdbc.driver", connectionDescriptor.getDriverClass());
		}
		return cfg;
	}
	
	
	private void copyFile(File sourceFile, File destinationFolder) {
		try {
						
			File destinationFile = new File(destinationFolder, sourceFile.getName());
			if(!destinationFolder.exists()) {
				destinationFolder.mkdirs();
			}
			
			boolean b = destinationFile.delete();
			if(b == false) {
				System.err.println("ERROR: Destination file already exists and cannot be deleted");
			}
			
		    InputStream in = new FileInputStream(sourceFile);
		    OutputStream out = new FileOutputStream(destinationFile);

		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0){
		    	out.write(buf, 0, len);
		    }
		    in.close();
		    out.flush();
		    out.close();
		   
		    /*
		    // release pointers
		    in = null;
		    out = null;
		    
		    // force garbage collection
		    Runtime javaRuntime = Runtime.getRuntime();
		    javaRuntime.gc();
			*/
		   
		    System.err.println("File [" + destinationFile.getAbsolutePath() + "] succesfully created");
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to copy file [" + sourceFile + "] into folder [" + destinationFolder + "]", t);
		}

	}
	
}
