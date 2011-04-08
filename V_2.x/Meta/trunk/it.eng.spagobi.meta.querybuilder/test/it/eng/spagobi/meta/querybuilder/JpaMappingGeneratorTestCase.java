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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.Assert;

import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingClassesGenerator;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.generator.utils.Compiler;
import it.eng.spagobi.meta.generator.utils.StringUtils;
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
public class JpaMappingGeneratorTestCase extends TestCase {
	
	IModelSerializer serializer;
	JpaMappingJarGenerator jarGenerator;	
	DBConnection connection;
	
	public static String CONNECTION_DIALECT = "org.hibernate.dialect.MySQLDialect";
	public static String CONNECTION_DRIVER = "com.mysql.jdbc.Driver";
	public static String CONNECTION_URL = "jdbc:mysql://localhost:3306/foodmart";
	public static String CONNECTION_USER = "root";
	public static String CONNECTION_PWD = "mysql";
	
	private static final File TEST_FOLDER = new File("test-resources");
	private static final File TEST_MODEL_NOKEY = new File(TEST_FOLDER, "models/NoKey.sbimodel");
	private static final File TEST_MODEL_SIMPLEKEY = new File(TEST_FOLDER, "models/SimpleKey.sbimodel");
	private static final File TEST_MODEL_COMPKEY = new File(TEST_FOLDER, "models/CompKey.sbimodel");
	private static final File TEST_MODEL_RELNOKEY = new File(TEST_FOLDER, "models/RelNoKey.sbimodel");
	private static final File TEST_MODEL_RELSIMPLEKEY = new File(TEST_FOLDER, "models/RelSimpleKey.sbimodel");
	private static final File TEST_MODEL_RELCOMPKEY = new File(TEST_FOLDER, "models/RelCompKey.sbimodel");


	protected void setUp() throws Exception {
		super.setUp();
		serializer = new EmfXmiSerializer();		
		jarGenerator = new JpaMappingJarGenerator();
		jarGenerator.setLibDir(new File(TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		jarGenerator.setLibs(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});	
		
		connection = new DBConnection();			
		connection.setName( "My Model" );
		connection.setDialect(CONNECTION_DIALECT );			
		connection.setDriverClass( CONNECTION_DRIVER  );	
		connection.setUrl( CONNECTION_URL );
		connection.setUsername( CONNECTION_USER );		
		connection.setPassword( CONNECTION_PWD );
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		serializer = null;
		jarGenerator = null;
	}
	
	
	private IDataSource getDataSource(File file) {
		IDataSourceConfiguration configuration;
		configuration = new FileDataSourceConfiguration("My Model", file);
		configuration.loadDataSourceProperties().put("connection", connection);
		IDataSource dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
		
		return dataSource;
	}
		
	public void testAllInOneShot() throws Exception {
		testNoKeyGenerator();
		testSimpleKeyGenerator();
		testCompKeyGenerator();
		testRelNoKeyGenerator();
		testRelSimpleKeyGenerator();
		testRelCompKeyGenerator();
	}
	
	public void testNoKeyGenerator() throws Exception {
		doTest(TEST_MODEL_NOKEY, new File(TEST_FOLDER, "outs/01_relNoKey"));
	}
	
	public void testSimpleKeyGenerator() throws Exception {
		doTest(TEST_MODEL_SIMPLEKEY, new File(TEST_FOLDER, "outs/02_relSimpleKey"));
	}
	
	public void testCompKeyGenerator() throws Exception {
		doTest(TEST_MODEL_COMPKEY, new File(TEST_FOLDER, "outs/03_CompKey"));
	}
	
	public void testRelNoKeyGenerator() throws Exception {
		doTest(TEST_MODEL_RELNOKEY, new File(TEST_FOLDER, "outs/04_relNoKey"));
	}
	
	public void testRelSimpleKeyGenerator() throws Exception {
		doTest(TEST_MODEL_RELSIMPLEKEY, new File(TEST_FOLDER, "outs/05_relSimpleKey"));
	}
	
	public void testRelCompKeyGenerator() throws Exception {
		doTest(TEST_MODEL_RELCOMPKEY, new File(TEST_FOLDER, "outs/06_relCompKey"));
	}
	
	
	
	private void doTest(File modelFile, File outputDir) throws Exception {
		Model model = serializer.deserialize(modelFile);
		jarGenerator.generate(model.getBusinessModels().get(0), outputDir.getAbsolutePath());
		assertNotNull("src dir cannot be null", jarGenerator.getSrcDir());
		assertNotNull("bin dir cannot be null", jarGenerator.getBinDir());
		assertNotNull("lib dir cannot be null", jarGenerator.getLibDir());
		assertNotNull("dist dir cannot be null", jarGenerator.getDistDir());
		assertNotNull("jar file cannot be null", jarGenerator.getJarFile());
		
		File viewFile = new File(jarGenerator.getSrcDir(), "views.json");
		assertTrue("Impossible to find view.json file in [" + jarGenerator.getSrcDir() + "]", viewFile.exists());
		
		String viewFileContent = StringUtils.getStringFromFile(viewFile);
		JSONObject viewJSON = new JSONObject( viewFileContent );
		
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
