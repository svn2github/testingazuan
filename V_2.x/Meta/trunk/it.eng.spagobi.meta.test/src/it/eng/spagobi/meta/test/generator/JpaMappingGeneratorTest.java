package it.eng.spagobi.meta.test.generator;

import static org.junit.Assert.*;

import java.io.File;

import it.eng.spagobi.meta.compiler.DataMartGenerator;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;

import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.initializer.TestConnectionFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class JpaMappingGeneratorTest {

	TestConnectionFactory.DatabaseType dbType=TestConnectionFactory.DatabaseType.MYSQL;
	Model rootModel=null;
	PhysicalModel physicalModel=null;
	PhysicalModelInitializer modelInitializer=null;
	BusinessModel businessModel=null;
	BusinessModelInitializer businessModelInitializer=null;
        
	JpaMappingGenerator gen = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// initialize model
		
		rootModel = ModelFactory.eINSTANCE.createModel();
		rootModel.setName("modelDemo");
		
		modelInitializer = new PhysicalModelInitializer();
		modelInitializer.setRootModel(rootModel);		
		physicalModel = modelInitializer.initialize( 
        		"physicalModelDemo", 
        		TestConnectionFactory.createConnection(dbType),  
        		TestConnectionFactory.getDefaultCatalogue(dbType), 
        		TestConnectionFactory.getDefaultSchema(dbType));
		
        businessModelInitializer = new BusinessModelInitializer();
        businessModel = businessModelInitializer.initialize("businessModelDemo", physicalModel);	
        
        gen = new JpaMappingGenerator();
	}

	@Test
	public void testGenerate() {
		String outputDir="C:/progetti/spagobi2.0/workspaceSpagoBIMeta/SpagoBIReverse/src/";
		try {
			gen.generateJpaMapping( businessModel, outputDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DataMartGenerator generator=new DataMartGenerator(
				"C:/progetti/spagobi2.0/workspaceSpagoBIMeta/SpagoBIReverse/src/",
				"C:/progetti/spagobi2.0/workspaceSpagoBIMeta/SpagoBIReverse/build/classes",
				"C:/ProgramFiles/eclipse-jee-helios-SR1-win32-SpagiBIMETA/plugins/",
				"C:/progetti/spagobi2.0/workspaceSpagoBIMeta/SpagoBIReverse/dist",
				"it/eng/spagobi/meta"
				);
		
		generator.compile();
		generator.jar();
	}


}
