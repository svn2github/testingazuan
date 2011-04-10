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
package it.eng.spagobi.meta.generator.jpamapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

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
	
	Model model;
	IModelSerializer serializer;
	JpaMappingJarGenerator jarGenerator;
	
	Compiler compiler;
	
	
	
	private static final File TEST_FOLDER = new File("test-resources");
	private static final File TEST_MODEL_FILE = new File(TEST_FOLDER, "TestModel.sbimodel");
	private static final File TEST_VIEW_MODEL_FILE = new File(TEST_FOLDER, "FoodmartViews.sbimodel");


	protected void setUp() throws Exception {
		super.setUp();
		serializer = new EmfXmiSerializer();
					
		jarGenerator = new JpaMappingJarGenerator();
		jarGenerator.setLibDir(new File(TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		jarGenerator.setLibs(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		model = null;
		serializer = null;
		jarGenerator = null;
		compiler = null;
	}
	
	public void testJarGenrationSmoke() throws Exception {
		model = serializer.deserialize(TEST_MODEL_FILE);
		jarGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings/nokey").getAbsolutePath());
		
		doTestGeneratedFiles();
		doTestViewFileJSONSintax();
	}
	
	public void testJarGeneratorWithViews() throws Exception {
		model = serializer.deserialize(TEST_VIEW_MODEL_FILE);
		jarGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings/foodmartviews").getAbsolutePath());
		
		doTestGeneratedFiles();
		doTestViewFileJSONSintax();
		doTestViewFileJSONContent();
		doTestJarFileContent();
		doTestPersistenceUnitContent();
		
	}
	
	
	private void doTestGeneratorState() {
		assertNotNull("src dir cannot be null", jarGenerator.getSrcDir());
		assertNotNull("bin dir cannot be null", jarGenerator.getBinDir());
		assertNotNull("lib dir cannot be null", jarGenerator.getLibDir());
		assertNotNull("dist dir cannot be null", jarGenerator.getDistDir());
		assertNotNull("jar file cannot be null", jarGenerator.getJarFile());
	}
		
	private void doTestGeneratedFiles() {
		assertTrue("src dir cannot be found", jarGenerator.getSrcDir().getAbsoluteFile().exists());
		assertNotNull("bin dir cannot be found", jarGenerator.getBinDir().getAbsoluteFile().exists());
		assertNotNull("lib dir cannot be found", jarGenerator.getLibDir().getAbsoluteFile().exists());
		assertNotNull("dist dir cannot be found", jarGenerator.getDistDir().getAbsoluteFile().exists());
		
		File viewFile = new File(jarGenerator.getSrcDir(), "views.json");
		assertTrue("Impossible to find view.json file in [" + jarGenerator.getSrcDir() + "]", viewFile.exists());
		
		File metainfDir;
		File persistenceFile;
		File labelsFile;
		File propertiesFile;
		
		metainfDir = new File(jarGenerator.getSrcDir(), "META-INF");
		assertTrue("Impossible to find META-INF folder in [" + jarGenerator.getSrcDir() + "]", metainfDir.exists());
		persistenceFile = new File(metainfDir, "persistence.xml");
		assertTrue("Impossible to find persistence.xml file in [" + metainfDir + "]", persistenceFile.exists());
		labelsFile = new File(jarGenerator.getSrcDir(), "labels.properties");
		assertTrue("Impossible to find labels.properties file in [" + jarGenerator.getSrcDir() + "]", labelsFile.exists());
		propertiesFile = new File(jarGenerator.getSrcDir(), "qbe.properties");
		assertTrue("Impossible to find qbe.properties file in [" + jarGenerator.getSrcDir() + "]", propertiesFile.exists());
		
		metainfDir = new File(jarGenerator.getBinDir(), "META-INF");
		assertTrue("Impossible to find META-INF folder in [" + jarGenerator.getBinDir() + "]", metainfDir.exists());
		persistenceFile = new File(metainfDir, "persistence.xml");
		assertTrue("Impossible to find persistence.xml file in [" + metainfDir + "]", persistenceFile.exists());
		labelsFile = new File(jarGenerator.getBinDir(), "labels.properties");
		assertTrue("Impossible to find labels.properties file in [" + jarGenerator.getBinDir() + "]", labelsFile.exists());
		propertiesFile = new File(jarGenerator.getBinDir(), "qbe.properties");
		assertTrue("Impossible to find qbe.properties file in [" + jarGenerator.getBinDir() + "]", propertiesFile.exists());
	
		assertTrue("Impossible to find datamart.jar file in [" + jarGenerator.getJarFile().getParent() + "]", jarGenerator.getJarFile().exists());
		
	}
	
	private void doTestViewFileJSONSintax() {
		try {
			File viewFile = new File(jarGenerator.getSrcDir(), "views.json");
			String viewFileContent = StringUtils.getStringFromFile(viewFile);
			JSONObject viewJSON = new JSONObject( viewFileContent );
		} catch(Throwable t) {
			fail();
		}
	}
	
	private void doTestViewFileJSONContent() throws Exception {
		
		File viewFile = new File(jarGenerator.getSrcDir(), "views.json");
		String viewFileContent = StringUtils.getStringFromFile(viewFile);
		JSONObject viewJSON = new JSONObject( viewFileContent );
			
		JSONArray views = viewJSON.optJSONArray("views");
		assertNotNull("Impossible lo read views configurations", views);
		assertEquals("Model contains a number of view different from 1", 1, views.length());
		JSONObject view = (JSONObject)views.get(0);
		assertNotNull("View name not defined", view.optString("name"));
		assertEquals("First view name is wrong", "EmployeeClosure", view.optString("name"));
			
		JSONArray tables = view.optJSONArray("tables");
		assertNotNull("View does not contains any table", tables);
		assertEquals("The number of tables contained in the view is different from 2", 2, tables.length());
			
		JSONArray joins = view.optJSONArray("joins");
		assertNotNull("View does not contains any table", joins);
		assertEquals("The number of joins contained in the view is different from 2", tables.length()-1, joins.length());
		
	}
	
	private void doTestJarFileContent() {
		JarFile jarFile;
		
		try {
			jarFile = new JarFile( jarGenerator.getJarFile() );
			assertNotNull("Impossible to find file persistence.xml in jar file [" + jarGenerator.getJarFile() + "]", jarFile.getJarEntry("META-INF/persistence.xml"));
			assertNotNull("Impossible to find file labels.properties in jar file [" + jarGenerator.getJarFile() + "]", jarFile.getJarEntry("labels.properties"));
			assertNotNull("Impossible to find file qbe.properties in jar file [" + jarGenerator.getJarFile() + "]", jarFile.getJarEntry("qbe.properties"));
			assertNotNull("Impossible to find file views.json in jar file [" + jarGenerator.getJarFile()  + "]", jarFile.getJarEntry("views.json"));
		} catch (IOException e) {
			fail();
		}
	}
	
	private void doTestPersistenceUnitContent() throws Exception {
		JarFile jarFile = new JarFile( jarGenerator.getJarFile() );
		JarEntry entry = jarFile.getJarEntry("META-INF/persistence.xml");
		InputStream in = jarFile.getInputStream(entry);
		String fileContents = StringUtils.getStringFromStream(in);
		in.close();
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.Employee</class>"));
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.Position</class>"));
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.EmployeeClosureEmployee</class>"));
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.EmployeeClosurePosition</class>"));
	}
}
