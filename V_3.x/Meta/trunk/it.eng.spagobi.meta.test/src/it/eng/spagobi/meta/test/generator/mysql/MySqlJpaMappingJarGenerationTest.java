package it.eng.spagobi.meta.test.generator.mysql;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.generator.AbstractJpaMappingGenerationTest;
import it.eng.spagobi.meta.util.ModelManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;


public class MySqlJpaMappingJarGenerationTest extends AbstractJpaMappingGenerationTest {
	
	static JpaMappingJarGenerator jpaMappingJarGenerator;

	public void setUp() throws Exception {
		super.setUp();
		try {
			if(dbType == null) dbType = TestCostants.DatabaseType.MYSQL;
			
			if(rootModel == null) {
				rootModel = TestModelFactory.createModel( dbType );
				if(rootModel != null && rootModel.getPhysicalModels() != null && rootModel.getPhysicalModels().size() > 0) {
					physicalModel = rootModel.getPhysicalModels().get(0);
				}
				if(rootModel != null && rootModel.getBusinessModels() != null && rootModel.getBusinessModels().size() > 0) {
					businessModel = rootModel.getBusinessModels().get(0);
				}
			}
			
			if(jpaMappingJarGenerator == null)  {
				jpaMappingJarGenerator = TestGeneratorFactory.createJarGeneraor();
				generator = jpaMappingJarGenerator;
			}
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	public void testGenerationSmoke() {
		generator.generate(businessModel, TestCostants.outputFolder.toString());
	}
	
	public void testBinFolderExistence() {
		File distFolder = jpaMappingJarGenerator.getDistDir();
		assertNotNull("dist folder cannot be null", distFolder);
		assertTrue("dist folder [" + distFolder +"] does not exist", distFolder.exists());
		assertTrue("dist folder [" + distFolder +"] is a file not a folder as expected", distFolder.isDirectory());
		assertTrue("dist folder [" + distFolder +"] cannot be read", distFolder.canRead());
		assertTrue("dist folder [" + distFolder +"] cannot be write", distFolder.canWrite());
	}
	
	public void testJarFileExistence() {
		File jarFile = jpaMappingJarGenerator.getJarFile();
		assertTrue("Impossible to find datamart.jar file in [" + jarFile.getParent() + "]", jarFile.exists());
	}
	
	public void testJarFileContent() {
		try {
			JarFile jarFile = new JarFile( jpaMappingJarGenerator.getJarFile() );
			assertNotNull("Impossible to find file persistence.xml in jar file [" + jpaMappingJarGenerator.getJarFile() + "]", jarFile.getJarEntry("META-INF/persistence.xml"));
			assertNotNull("Impossible to find file labels.properties in jar file [" + jpaMappingJarGenerator.getJarFile() + "]", jarFile.getJarEntry("label.properties"));
			assertNotNull("Impossible to find file qbe.properties in jar file [" + jpaMappingJarGenerator.getJarFile() + "]", jarFile.getJarEntry("qbe.properties"));
			assertNotNull("Impossible to find file views.json in jar file [" + jpaMappingJarGenerator.getJarFile()  + "]", jarFile.getJarEntry("views.json"));
			
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Test the model generation when to the business model the same physical 
	 * table is added more than one time.
	 * 
	 * @see BusinessModelInitializer row 374 for identifier problems
	 * @see BusinessModelInitializer row 492 for relationship problems
	 */
	public void _testDoubleImportedTableModelGeneration() {
		fail();
	}
}
