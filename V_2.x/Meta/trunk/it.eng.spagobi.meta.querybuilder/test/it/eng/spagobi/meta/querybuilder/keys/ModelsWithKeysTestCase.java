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
package it.eng.spagobi.meta.querybuilder.keys;

import java.io.File;


import org.json.JSONObject;

import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;
import it.eng.spagobi.meta.querybuilder.AbtractQueryBuilderTestCase;
import it.eng.spagobi.meta.querybuilder.TestCaseConstants;
import junit.framework.TestCase;


/**
 * This test case test models that contain different combination of keys and relationships definitions
 * 
 * 
 * NOTE: Tests only the ModelStructure and the Query's results. The generated mapping is not tested
 * because it is already tested by specific tast cases in the exporter package
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModelsWithKeysTestCase extends AbtractQueryBuilderTestCase {
	
	public static final File TEST_INPUT_FOLDER = new File(TestCaseConstants.TEST_FOLDER, "models/keys");
	public static final File TEST_OUTPUT_FOLDER = new File(TestCaseConstants.TEST_OUPUT_ROOT_FOLDER, "keys");
	
	// a model without keys and relationships
	private static final File TEST_MODEL_NOKEY = new File(TEST_INPUT_FOLDER, "NoKey.sbimodel");
	
	// a model without simple keys only (1 column key) but no relationships
	private static final File TEST_MODEL_SIMPLEKEY = new File(TEST_INPUT_FOLDER, "SimpleKey.sbimodel");
	
	// a model with composite keys only but no relationships
	private static final File TEST_MODEL_COMPKEY = new File(TEST_INPUT_FOLDER, "CompKey.sbimodel");
	
	// a model with no keys but with relationships
	private static final File TEST_MODEL_RELNOKEY = new File(TEST_INPUT_FOLDER, "RelNoKey.sbimodel");
	
	// a model with simple keys and relationships (...defined upon keys)
	private static final File TEST_MODEL_RELSIMPLEKEY = new File(TEST_INPUT_FOLDER, "RelSimpleKey.sbimodel");
	
	// a model with composite keys and relationships (...defined upon keys)
	private static final File TEST_MODEL_RELCOMPKEY = new File(TEST_INPUT_FOLDER, "RelCompKey.sbimodel");


	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
		
	public void testNoKeyGenerator() throws Exception {
		doTest(TEST_MODEL_NOKEY, new File(TEST_OUTPUT_FOLDER, "01_relNoKey"));
	}
	
	public void testSimpleKeyGenerator() throws Exception {
		doTest(TEST_MODEL_SIMPLEKEY, new File(TEST_OUTPUT_FOLDER, "02_relSimpleKey"));
	}
	
	public void testCompKeyGenerator() throws Exception {
		doTest(TEST_MODEL_COMPKEY, new File(TEST_OUTPUT_FOLDER, "03_CompKey"));
	}
	
	public void testRelNoKeyGenerator() throws Exception {
		doTest(TEST_MODEL_RELNOKEY, new File(TEST_OUTPUT_FOLDER, "04_relNoKey"));
	}
	
	public void testRelSimpleKeyGenerator() throws Exception {
		doTest(TEST_MODEL_RELSIMPLEKEY, new File(TEST_OUTPUT_FOLDER, "05_relSimpleKey"));
	}
	
	public void testRelCompKeyGenerator() throws Exception {
		doTest(TEST_MODEL_RELCOMPKEY, new File(TEST_OUTPUT_FOLDER, "06_relCompKey"));
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
		
		File jarFile = new File(jarGenerator.getDistDir(), "datamart.jar");
		IDataSource dataSource = null;
		try {
			dataSource = createDataSource(jarFile);
		} catch(Throwable t){
			fail();
		}
		
		dataSource.getModelStructure();
	}
	
}
