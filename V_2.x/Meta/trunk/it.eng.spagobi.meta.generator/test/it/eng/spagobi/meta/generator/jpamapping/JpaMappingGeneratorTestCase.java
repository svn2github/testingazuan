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

import org.json.JSONObject;
import org.junit.Assert;

import it.eng.spagobi.meta.generator.utils.Compiler;
import it.eng.spagobi.meta.generator.utils.StringUtil;
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
	JpaMappingCodeGenerator codeGenerator;
	JpaMappingClassesGenerator classesGenerator;
	JpaMappingJarGenerator jarGenerator;
	
	Compiler compiler;
	
	
	
	private static final File TEST_FOLDER = new File("test-resources");
	private static final File TEST_MODEL_FILE = new File(TEST_FOLDER, "TestModel.sbimodel");

	protected void setUp() throws Exception {
		super.setUp();
		serializer = new EmfXmiSerializer();
		model = serializer.deserialize(TEST_MODEL_FILE);
		codeGenerator = new JpaMappingCodeGenerator();
		
		classesGenerator = new JpaMappingClassesGenerator();
		classesGenerator.setLibDir(new File(TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		classesGenerator.setLibs(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});
			
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
		codeGenerator = null;
		classesGenerator = null;
		jarGenerator = null;
		compiler = null;
	}
	
	public void testSmoke() {
		 assertNotNull("Impossible to load [" + TEST_MODEL_FILE + "]", model);
	}
	
	public void _testCodeGenerate() {
		codeGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings").getAbsolutePath());
	
		assertNotNull("src dir cannot be null", codeGenerator.getSrcDir());
	}
	
	public void _testClassesGenerator() {
		classesGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings").getAbsolutePath());
	
		assertNotNull("src dir cannot be null", classesGenerator.getSrcDir());
		assertNotNull("bin dir cannot be null", classesGenerator.getBinDir());
		assertNotNull("lib dir cannot be null", classesGenerator.getLibDir());
	}
	
	public void testJarGenerator() throws Exception {
		jarGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings").getAbsolutePath());
		assertNotNull("src dir cannot be null", jarGenerator.getSrcDir());
		assertNotNull("bin dir cannot be null", jarGenerator.getBinDir());
		assertNotNull("lib dir cannot be null", jarGenerator.getLibDir());
		assertNotNull("dist dir cannot be null", jarGenerator.getDistDir());
		assertNotNull("jar file cannot be null", jarGenerator.getJarFile());
		
		File viewFile = new File(jarGenerator.getSrcDir(), "views.json");
		assertTrue("Impossible to find view.json file in [" + jarGenerator.getSrcDir() + "]", viewFile.exists());
		
		String viewFileContent = StringUtil.getStringFromFile(viewFile);
		JSONObject viewJSON = new JSONObject( viewFileContent );

	}
	
}
