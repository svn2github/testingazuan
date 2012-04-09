package it.eng.spagobi.meta.test.generator.mysql;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingClassesGenerator;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.generator.AbstractMappingGenerationTest;

import java.io.File;


public class MySqlJpaMappingClassesGenerationTest extends AbstractMappingGenerationTest {

	static JpaMappingClassesGenerator jpaMappingClassesGenerator;
	
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
			
			if(jpaMappingClassesGenerator == null)  {
				jpaMappingClassesGenerator = TestGeneratorFactory.createClassesGeneraor();
				generator = jpaMappingClassesGenerator;
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
		File binFolder = jpaMappingClassesGenerator.getBinDir();
		assertNotNull("bin folder cannot be null", binFolder);
		assertTrue("bin folder [" + binFolder +"] does not exist", binFolder.exists());
		assertTrue("bin folder [" + binFolder +"] is a file not a folder as expected", binFolder.isDirectory());
		assertTrue("bin folder [" + binFolder +"] cannot be read", binFolder.canRead());
		assertTrue("bin folder [" + binFolder +"] cannot be write", binFolder.canWrite());
	}
	
	public void testMeatInfFolderExistence() {
		File metainfDir = new File(jpaMappingClassesGenerator.getBinDir(), "META-INF");
		assertTrue("Impossible to find META-INF folder in [" + jpaMappingClassesGenerator.getBinDir() + "]", metainfDir.exists());
	}
	
	public void testPersistenceFileExistence() {
		File metainfDir = new File(jpaMappingClassesGenerator.getBinDir(), "META-INF");
		File persistenceFile = new File(metainfDir, "persistence.xml");
		assertTrue("Impossible to find persistence.xml file in [" + metainfDir + "]", persistenceFile.exists());
	}
	
	public void testLabelFileExistence() {
		File labelsFile = new File(jpaMappingClassesGenerator.getBinDir(), "label.properties");
		assertTrue("Impossible to find labels.properties file in [" + jpaMappingClassesGenerator.getBinDir() + "]", labelsFile.exists());		
	}
	
	public void testQbePropertiesFileExistence() {
		File propertiesFile = new File(jpaMappingClassesGenerator.getBinDir(), "qbe.properties");
		assertTrue("Impossible to find qbe.properties file in [" + jpaMappingClassesGenerator.getBinDir() + "]", propertiesFile.exists());
	}
	
	public void testViewFileExistence() {
		File viewFile = new File(jpaMappingClassesGenerator.getBinDir(), "views.json");
		assertTrue("Impossible to find view.json file in folder [" + jpaMappingClassesGenerator.getBinDir() + "]", viewFile.exists());
	}
}
