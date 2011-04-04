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

import java.io.File;

import org.junit.Assert;

import it.eng.spagobi.meta.compiler.DataMartGenerator;
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
	
	DataMartGenerator compiler;
	
	
	
	private static final File TEST_FOLDER = new File("test-resources");
	private static final File TEST_MODEL_FILE = new File(TEST_FOLDER, "TestModel.sbimodel");

	protected void setUp() throws Exception {
		super.setUp();
		serializer = new EmfXmiSerializer();
		model = serializer.deserialize(TEST_MODEL_FILE);
		codeGenerator = new JpaMappingCodeGenerator();
		
		classesGenerator = new JpaMappingClassesGenerator();
		classesGenerator.setLibDir(new File(TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		classesGenerator.setLibraryLink(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});
		
		jarGenerator = new JpaMappingJarGenerator();
		jarGenerator.setLibDir(new File(TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		jarGenerator.setLibraryLink(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});
		
		setUpCompiler();
	}
	
	protected void setUpCompiler() {
		File baseDir = new File(TEST_FOLDER, "mappings");
		String packageName = model.getBusinessModels().get(0).getProperties().get("structural.package").getValue();
		
		compiler = new DataMartGenerator(
				baseDir, 
				packageName.replace(".", "/")
		);
		compiler.setLibDir(new File(TEST_FOLDER.getParentFile(), "libs/eclipselink"));
		compiler.setLibraryLink(new String[]{
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
	
	public void testCodeGenerate() {
		codeGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings").getAbsolutePath());
	}
	
	public void testClassesGenerator() {
		classesGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings").getAbsolutePath());
		//assertTrue("Impossible to compile mapping files", compiler.compile());
	}
	
	public void testJarGenerator() {
		jarGenerator.generate(model.getBusinessModels().get(0), new File(TEST_FOLDER, "mappings").getAbsolutePath());
//		try {
//			compiler.jar();
//		} catch(Throwable t) {
//			fail("Impossible to generate the jar file");
//		}
	}
}
