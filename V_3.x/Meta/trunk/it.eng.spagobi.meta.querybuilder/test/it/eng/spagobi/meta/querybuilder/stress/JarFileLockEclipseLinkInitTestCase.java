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

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriverWithClassLoader;
import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.oda.impl.OdaStructureBuilder;
import it.eng.spagobi.meta.querybuilder.TestCaseConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class JarFileLockEclipseLinkInitTestCase extends TestCase {

	public static final File TEST_INPUT_FOLDER = new File(TestCaseConstants.TEST_FOLDER, "models/stress");
	public static final File TEST_OUTPUT_FOLDER = new File(TestCaseConstants.TEST_OUPUT_ROOT_FOLDER, "stress");
	private static Logger logger = LoggerFactory.getLogger(JarFileLockEclipseLinkInitTestCase.class);

	// a model with no keys but with relationships
	private static final File TEST_JAR_FILE = new File(TEST_INPUT_FOLDER, "datamart.jar");
    ConnectionDescriptor connection;
   


    protected void setUp() throws Exception {
        connection = new ConnectionDescriptor();           
        connection.setName( "a" );
        connection.setDialect( TestCaseConstants.CONNECTION_DIALECT );           
        connection.setDriverClass( TestCaseConstants.CONNECTION_DRIVER  );   
        connection.setUrl( TestCaseConstants.CONNECTION_URL );
        connection.setUsername( TestCaseConstants.CONNECTION_USER );       
        connection.setPassword( TestCaseConstants.CONNECTION_PWD );
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
   
   
    private IDataSource getDataSource(File file) {
        IDataSourceConfiguration configuration;
        configuration = new FileDataSourceConfiguration("a", file);
        configuration.loadDataSourceProperties().put("connection", connection);
        IDataSource dataSource = DriverManager.getDataSource(JPADriverWithClassLoader.DRIVER_ID, configuration);
        logger.debug("Datasource is [{}]",dataSource );
        return dataSource;
    }
       

    public void testLock() throws Exception {
        try {
            for(int i=0; i<10; i++){
                //copiare da un altra parte file del datamart
                copyFile(TEST_JAR_FILE, TEST_OUTPUT_FOLDER);
                //mettere file del datamart
                doTest(TEST_JAR_FILE);
                //doTest1(new File(TestCaseConstants.TEST_FOLDER, "PERCORSO FILE"));   
                deleteFile(TEST_JAR_FILE);
                //ri-copiare file datamart in directory originale
                copyFile(new File(TEST_OUTPUT_FOLDER, "datamart.jar"), TEST_INPUT_FOLDER);
            }
        } catch (SpagoBIPluginException e) {
            fail("Can't delete the file");
        } catch (Exception e) {
            fail();
        }

    }

    private void deleteFile(File file){
        if(!file.exists()){
            logger.error("File doesn't exists");
        	return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                deleteFile(files[i]);
            }
        }
        boolean fileDeletionResult = file.delete();
        if(!fileDeletionResult){
        	logger.error("Can't delete the file "+ file.getAbsolutePath()+" the file is writtable? "+file.canWrite() +" "+file.canExecute());
            throw new SpagoBIPluginException("Can't delete the file "+ file.getAbsolutePath()+" the file is writtable? "+file.canWrite() +" "+file.canExecute());
        }
    }
   
    private void doTest(File datamartfile) throws Exception {

        IDataSource dataSource = null;
        try {
            dataSource = getDataSource(datamartfile);
        } catch(Throwable t){
            fail();
        }
       
    }
   
   
    private void doTest1(File datamartfile) throws Exception {
        IDataSource dataSource = null;
        try {
            dataSource = getDataSource(datamartfile);
        } catch(Throwable t){
            fail();
        }
       
        dataSource.getModelStructure();
    }
    
    private void copyFile(File sourceFile, File destinationFolder) {
    	try {
    		File destinationFile = new File(destinationFolder, sourceFile.getName());
    		if(!destinationFolder.exists()) {
    			destinationFolder.mkdirs();
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

    		logger.debug("File succesfully copied");
    	} catch(Throwable t) {
    		throw new RuntimeException("Impossible to copy file [" + sourceFile + "] into folder [" + destinationFolder + "]", t);
    	}

    }
   
}