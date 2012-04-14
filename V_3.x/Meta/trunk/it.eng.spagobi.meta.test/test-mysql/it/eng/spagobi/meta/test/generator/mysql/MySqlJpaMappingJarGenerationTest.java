package it.eng.spagobi.meta.test.generator.mysql;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.AbstractSpagoBIMetaTest;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.generator.AbstractJpaMappingGenerationTest;
import it.eng.spagobi.meta.test.utils.ModelManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;


public class MySqlJpaMappingJarGenerationTest extends AbstractSpagoBIMetaTest {
	
	static JpaMappingJarGenerator jpaMappingJarGenerator;

	public void setUp() throws Exception {
		super.setUp();
		try {
			if(dbType == null) dbType = TestCostants.DatabaseType.MYSQL;
			
			if(rootModel == null) {
				setRootModel(TestModelFactory.createModel( dbType ));
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
	public void testDoubleImportedTableModelGeneration() {
		ModelManager modelManager = new ModelManager(rootModel);
		PhysicalTable physicalTable = physicalModel.getTable("currency");
		modelManager.addBusinessTable(physicalTable);
		modelManager.addBusinessTable(physicalTable);
		
		List<BusinessTable> businessTables = businessModel.getBusinessTableByPhysicalTable(physicalTable);
		assertEquals(3, businessTables.size());
		
		BusinessTable businessTable1 = businessTables.get(0);
		BusinessTable businessTable2 = businessTables.get(1);
		BusinessTable businessTable3 = businessTables.get(2);
		
		assertTrue(businessTable1.getName() != businessTable2.getName() && businessTable1.getName() != businessTable3.getName());
		assertTrue(businessTable2.getName() != businessTable3.getName() && businessTable2.getName() != businessTable1.getName());
		assertTrue(businessTable3.getName() != businessTable2.getName() && businessTable3.getName() != businessTable1.getName());
		
		assertTrue(businessTable1.getUniqueName() != businessTable2.getUniqueName() && businessTable1.getUniqueName() != businessTable3.getUniqueName());
		assertTrue(businessTable2.getUniqueName() != businessTable3.getUniqueName() && businessTable2.getUniqueName() != businessTable1.getUniqueName());
		assertTrue(businessTable3.getUniqueName() != businessTable1.getUniqueName() && businessTable3.getUniqueName() != businessTable2.getUniqueName());
	
		assertTrue(!businessTable1.equals(businessTable2) && !businessTable1.equals(businessTable3));
		assertTrue(!businessTable2.equals(businessTable3) && !businessTable2.equals(businessTable1));
		assertTrue(!businessTable3.equals(businessTable1) && !businessTable3.equals(businessTable2));
	
		generator.generate(businessModel, TestCostants.outputFolder.toString());
	}
	
	// =============================================
	// TESTS ON VIEW MODEL
	// =============================================
	public void testViewGenerationSmoke() {
		setViewModel(TestModelFactory.createFilteredModel( dbType, "VIEW_MODEL_TEST" ));
		
		// create view here....
		ModelManager modelManager = new ModelManager(viewModel);
		PhysicalTable source = viewPhysicalModel.getTable("product");
		PhysicalTable destination = viewPhysicalModel.getTable("product_class");
		BusinessTable businessTable = viewBusinessModel.getBusinessTableByPhysicalTable( source ).get(0);
	
		List<PhysicalColumn> sourceCol = new ArrayList<PhysicalColumn>();
		sourceCol.add(source.getColumn("product_class_id"));
		List<PhysicalColumn> destinationCol = new ArrayList<PhysicalColumn>();
		destinationCol.add(destination.getColumn("product_class_id"));
		int cardinality = 0;
		String relationshipName = "inner_join_test";
		BusinessViewInnerJoinRelationshipDescriptor innerJoinRelationshipDescriptor
			= new BusinessViewInnerJoinRelationshipDescriptor(source, destination, sourceCol, destinationCol, cardinality, relationshipName);
		
		BusinessView businessView = modelManager.createView(businessTable, innerJoinRelationshipDescriptor);
		modelManager.addBusinessColumn(destination.getColumn("product_family"), businessView);
		
		jpaMappingJarGenerator = TestGeneratorFactory.createJarGeneraor();
		generator = jpaMappingJarGenerator;
		jpaMappingJarGenerator.generate(viewBusinessModel, TestCostants.outputFolder.toString());
	}
}
