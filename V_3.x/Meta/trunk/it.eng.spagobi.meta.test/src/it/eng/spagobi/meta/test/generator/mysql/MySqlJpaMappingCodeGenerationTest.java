package it.eng.spagobi.meta.test.generator.mysql;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.generator.AbstractMappingGenerationTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class MySqlJpaMappingCodeGenerationTest extends AbstractMappingGenerationTest {

	static JpaMappingCodeGenerator jpaMappingCodeGenerator;
	
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
			
			if(jpaMappingCodeGenerator == null)  {
				jpaMappingCodeGenerator = TestGeneratorFactory.createCodeGeneraor();
				generator = jpaMappingCodeGenerator;
			}
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	public void doTestGeneratorState() {
		File srcFolder = jpaMappingCodeGenerator.getSrcDir();
		assertNotNull("src folder cannot be null", srcFolder);
		assertTrue("src folder [" + srcFolder +"] does not exist", srcFolder.exists());
		assertTrue("src folder [" + srcFolder +"] is a file not a folder as expected", srcFolder.isDirectory());
		assertTrue("src folder [" + srcFolder +"] cannot be read", srcFolder.canRead());
		assertTrue("src folder [" + srcFolder +"] cannot be write", srcFolder.canWrite());
		
		File templateFolder = jpaMappingCodeGenerator.getTemplateDir();
		assertNotNull("bin folder cannot be null", templateFolder);
		assertTrue("src folder [" + templateFolder +"] does not exist", templateFolder.exists());
		assertTrue("src folder [" + templateFolder +"] is a file not a folder as expected", templateFolder.isDirectory());
		assertTrue("src folder [" + templateFolder +"] cannot be read", templateFolder.canRead());
		assertTrue("src folder [" + templateFolder +"] cannot be write", templateFolder.canWrite());		
	}
	
	public void testGenerationSmoke() {
		generator.generate(businessModel, TestCostants.outputFolder.toString());
	}
	
	public void testSrcFolderExistence() {
		File srcFolder = jpaMappingCodeGenerator.getSrcDir();
		assertNotNull("src folder cannot be null", srcFolder);
		assertTrue("src folder [" + srcFolder +"] does not exist", srcFolder.exists());
		assertTrue("src folder [" + srcFolder +"] is a file not a folder as expected", srcFolder.isDirectory());
		assertTrue("src folder [" + srcFolder +"] cannot be read", srcFolder.canRead());
		assertTrue("src folder [" + srcFolder +"] cannot be write", srcFolder.canWrite());
	}
	
	public void testMeatInfFolderExistence() {
		File metainfDir = new File(jpaMappingCodeGenerator.getSrcDir(), "META-INF");
		assertTrue("Impossible to find META-INF folder in [" + jpaMappingCodeGenerator.getSrcDir() + "]", metainfDir.exists());
	}
	
	public void testPersistenceFileExistence() {
		File metainfDir = new File(jpaMappingCodeGenerator.getSrcDir(), "META-INF");
		File persistenceFile = new File(metainfDir, "persistence.xml");
		assertTrue("Impossible to find persistence.xml file in [" + metainfDir + "]", persistenceFile.exists());
	}
	
	public void testLabelFileExistence() {
		File labelsFile = new File(jpaMappingCodeGenerator.getSrcDir(), "label.properties");
		assertTrue("Impossible to find labels.properties file in [" + jpaMappingCodeGenerator.getSrcDir() + "]", labelsFile.exists());		
	}
	
	public void testQbePropertiesFileExistence() {
		File propertiesFile = new File(jpaMappingCodeGenerator.getSrcDir(), "qbe.properties");
		assertTrue("Impossible to find qbe.properties file in [" + jpaMappingCodeGenerator.getSrcDir() + "]", propertiesFile.exists());
	}
	
	public void testViewFileExistence() {
		File viewFile = new File(jpaMappingCodeGenerator.getSrcDir(), "views.json");
		assertTrue("Impossible to find view.json file in folder [" + jpaMappingCodeGenerator.getSrcDir() + "]", viewFile.exists());
	}	
	
	public void testPersistenceUnitBaseContent() {
		String fileContents;
		
		fileContents = null;
		try {
			File metainfDir = new File(jpaMappingCodeGenerator.getSrcDir(), "META-INF");
			File persistenceFile = new File(metainfDir, "persistence.xml");
			InputStream in = new FileInputStream( persistenceFile );
			fileContents = StringUtils.getStringFromStream(in);
			in.close();
		} catch(Throwable t) {
			t.printStackTrace();
			fail();
		}
		
		assertNotNull(fileContents);
		assertTrue(fileContents.contains("<provider>org.hibernate.ejb.HibernatePersistence</provider>"));
		
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.Employee</class>"));
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.Position</class>"));
		
		assertTrue(fileContents.contains("<property name=\"eclipselink.session.customizer\" value=\"it.eng.qbe.datasource.jpa.JNDICustomizer\"/>"));
	}
}
