package it.eng.spagobi.meta.test.query.postgres;

import it.eng.qbe.model.properties.IModelProperties;
import it.eng.qbe.model.properties.SimpleModelProperties;
import it.eng.qbe.model.structure.FilteredModelStructure;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.ModelEntity;
import it.eng.qbe.model.structure.ModelViewEntity;
import it.eng.qbe.model.structure.ModelViewEntity.ViewRelationship;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;
import it.eng.qbe.statement.QbeDatasetFactory;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.querybuilder.ui.shared.result.DataStoreReader;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.query.AbstractModelQueryTest;
import it.eng.spagobi.meta.test.utils.ModelManager;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class PostgresModelQueryTest extends AbstractModelQueryTest {
	
	
	public void setUp() throws Exception {
		try {
			// if this is the first test on mysql after the execution
			// of tests on an other database force a tearDown to clean
			// and regenerate properly all the static variables contained in
			// parent class AbstractSpagoBIMetaTest
			if(dbType != TestCostants.DatabaseType.POSTGRES){
				doTearDown();
			}
			super.setUp();
			
			if(dbType == null) dbType = TestCostants.DatabaseType.POSTGRES;
						
			if(rootModel == null) {
				setRootModel( TestModelFactory.createModel( dbType ) );
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

	// =============================================
	// TESTS ON STANDARD MODEL
	// =============================================
	
	/**
	 * Test mapping generation over the standard business model
	 */
	public void testRootModelGenerationSmoke() {
		try  {
			jpaMappingJarGenerator.generate(businessModel, TestCostants.outputFolder.toString());
		} catch (Throwable t) {
			fail();
		}
	}
	
	/**
	 * Test data source creation over the standard business model
	 */
	public void testRootModelDataSourceSmoke() {
		try {
			File jarFile = jpaMappingJarGenerator.getJarFile();
			createDataSource("BUSINESS_MODEL_TEST", jarFile);
		} catch (Throwable t) {
			fail();
		}
	}
	
	public static final  String TABLE_TAG = "TTG";
	public static final  String COLUMN_TAG = "CTG";
	public static final  String RELATIONSHIP_TAG = "RTG";
	
	/**
	 * modify the name of all entities, sub-entities and fields than check if the labels
	 * are ported properly in the qbe
	 */
	public void testLabels() {
		List<BusinessTable> businessTables = businessModel.getBusinessTables();
		for(BusinessTable businessTable : businessTables) {
			businessTable.setName( businessTable.getName() + " " + TABLE_TAG);
			List<BusinessColumn> businessColumns = businessTable.getColumns();
			for(BusinessColumn businessColumn : businessColumns) {
				businessColumn.setName( businessColumn.getName() + " " + COLUMN_TAG);
			}
			
			List<BusinessRelationship> relationships = businessTable.getRelationships();
			for(BusinessRelationship relationship : relationships) {
				ModelProperty property = relationship.getProperties().get("structural.destinationRole");
				assertNotNull(property);
				property.setValue( property.getValue() + " " + RELATIONSHIP_TAG);
			}
		}
		
		jpaMappingJarGenerator.generate(businessModel, TestCostants.outputFolder.toString());
		File jarFile = jpaMappingJarGenerator.getJarFile();
		createDataSource("BUSINESS_MODEL_TEST", jarFile);

		
		SimpleModelProperties labels = (SimpleModelProperties)dataSource.getModelI18NProperties(null);
		if( labels == null) {
			labels = new SimpleModelProperties();
		}
		
		FilteredModelStructure filteredModelStructure = getFilteredDataSource(dataSource);
		List<IModelEntity> modelEntities = filteredModelStructure.getRootEntities("BUSINESS_MODEL_TEST");
		for(IModelEntity modelEntity : modelEntities) {
			String label = labels.getProperty(modelEntity, "label");
			label =  StringUtilities.isEmpty(label)? modelEntity.getName(): label;
			assertTrue(label.endsWith( TABLE_TAG ));
			
			List<IModelField> fields = modelEntity.getAllFields();
			for(IModelField field : fields) {
				label = labels.getProperty(field, "label");
				label = StringUtilities.isEmpty(label)? field.getName(): label;
				assertTrue(label.endsWith( COLUMN_TAG ));
			}
			
			List<IModelEntity> subEntities = modelEntity.getSubEntities();
			for(IModelEntity subEntity : subEntities) {
				label = labels.getProperty(subEntity, "label");
				label =  StringUtilities.isEmpty(label)? subEntity.getName(): label;
				assertTrue("Value [" + label + "] of property [" + labels.getPropertyQualifiedName(subEntity, "label") + "] does not ends with " + RELATIONSHIP_TAG,
						label.endsWith( RELATIONSHIP_TAG ));
			}
		}
	}

	// =============================================
	// TESTS ON VIEW MODEL
	// =============================================
	
	/**
	 * Create a business view PRODUCT that joins product and product_class into the filtered model 
	 * and test mapping generation
	 */
	public void testViewModelGenerationSmoke() {
		setFilteredModel(TestModelFactory.createFilteredModel( dbType, "VIEW_MODEL_TEST" ));
		
		// create view here....
		ModelManager modelManager = new ModelManager(filteredModel);
		PhysicalTable source = filteredPhysicalModel.getTable("product");
		PhysicalTable destination = filteredPhysicalModel.getTable("product_class");
		BusinessTable businessTable = filteredBusinessModel.getBusinessTableByPhysicalTable( source ).get(0);
	
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
		jpaMappingJarGenerator.generate(filteredBusinessModel, TestCostants.outputFolder.toString());
	}
	
	/**
	 * Create a business view PRODUCT that joins product and product_class into the filtered model 
	 * and test data source creation
	 */
	public void testViewModelDataSourceSmoke() {
		try {
			File jarFile = jpaMappingJarGenerator.getJarFile();
			createDataSource("BUSINESS_VIEW_MODEL_TEST", jarFile);
		} catch (Throwable t) {
			fail();
		}
	}
	
	/**
	 * Test the existence of view table
	 */
	public void testViewTable() {
		IModelEntity viewEntity = dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product::Product");
		assertNotNull(viewEntity);
		assertEquals("it.eng.spagobi.meta.Product::Product", viewEntity.getUniqueName());
		assertTrue(viewEntity instanceof ModelViewEntity);		
	}
	
	/**
	 * Test the existence of inner view tables
	 */
	public void testViewInnerTables() {
		
		IModelEntity innerEntityProduct = dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product::Product_product");
		assertNotNull(innerEntityProduct);
		assertEquals("it.eng.spagobi.meta.Product_product::Product_product", innerEntityProduct.getUniqueName());
		assertTrue(innerEntityProduct instanceof ModelEntity);	
		
		IModelEntity innerEntityProductClass = dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product_class::Product_product_class");
		assertNotNull(innerEntityProductClass);
		assertEquals("it.eng.spagobi.meta.Product_product_class::Product_product_class", innerEntityProductClass.getUniqueName());
		assertTrue(innerEntityProductClass instanceof ModelEntity);	
		
		ModelViewEntity viewEntity = (ModelViewEntity)dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product::Product");
		
		List<IModelEntity> innerEntities = viewEntity.getInnerEntities();
		assertEquals(2, innerEntities.size());
		assertTrue(innerEntities.contains(innerEntityProduct));
		assertTrue(innerEntities.contains(innerEntityProductClass));
	}
	
	/**
	 * Test that the number of fields contained in the view table is equal to
	 * the sum of the fields contained in its inner tables
	 */
	public void testViewFields() {
		
		IModelEntity innerEntityProduct = dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product::Product_product");
		
		IModelEntity innerEntityProductClass = dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product_class::Product_product_class");
		
		ModelViewEntity viewEntity = (ModelViewEntity)dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product::Product");
		
		assertEquals(innerEntityProduct.getAllFields().size() + innerEntityProductClass.getAllFields().size()
				, viewEntity.getAllFields().size());		
	}
	
	/**
	 * Test view sub entities
	 * 
	 * TODO add some meaningful assertions
	 */
	public void testViewSubEntities() {
		ModelViewEntity viewEntity = (ModelViewEntity)dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product::Product");
		List<IModelEntity> subEntities = viewEntity.getSubEntities();
		for(IModelEntity subentity : subEntities) {
			System.err.println(subentity.getUniqueName());
			for(IModelField field : subentity.getAllFields()) {
				System.err.println("  - " + field.getUniqueName());
			}
		}
	}
	
	public void testViewInnerEntitiesVisibilityProperty() {
		FilteredModelStructure filteredModelStructure = getFilteredDataSource(dataSource);
			
		// test inner entities visibility
		IModelEntity innerEntityProduct = filteredModelStructure.getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product::Product_product");
		assertNull(innerEntityProduct);
		
		IModelEntity innerEntityProductClass = filteredModelStructure.getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product_class::Product_product_class");
		assertNull(innerEntityProductClass);
	}
	
	public void testViewFieldsVisibilityProperty() {
		FilteredModelStructure filteredModelStructure = getFilteredDataSource(dataSource);
	
		IModelEntity innerEntityProduct = filteredModelStructure.getRootEntity("BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product::Product_product");
		
		// test fields visibility
		innerEntityProduct = dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product::Product_product");
		
		IModelEntity viewEntity = filteredModelStructure.getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product::Product");
		
		assertEquals(innerEntityProduct.getAllFields().size() + 1
				, viewEntity.getAllFields().size());	
		
		IModelField productFamilyField = viewEntity.getField("it.eng.spagobi.meta.Product::Product:it.eng.spagobi.meta.Product_product_class:compId.product_family");
		assertNotNull(productFamilyField);
	}
	
	public void testViewInnerJoinRelashionship() {
		Query query = new Query();
		query.addSelectFiled("it.eng.spagobi.meta.Product::Product:it.eng.spagobi.meta.Product_product_class:compId.product_family"
				, "NONE", "family", true, true, false, "NONE", null);
		query.addSelectFiled("it.eng.spagobi.meta.Product::Product:it.eng.spagobi.meta.Product_product:compId.product_id", "NONE"
				, "id", true, true, false, "NONE", null);
		IStatement statement = dataSource.createStatement(query);
		IDataSet dataSet =  QbeDatasetFactory.createDataSet(statement);
		int offset=0; int pageSize= 25; int maxResults = -1;
		dataSet.loadData(offset,pageSize,maxResults);
		IDataStore dataStore = dataSet.getDataStore();
		int resultSize = DataStoreReader.getMaxResult(dataStore);
		
		assertEquals(1560, resultSize);
	}
	
	public void _testViewInboundRelashionship() {
		Query query = new Query();
		query.addSelectFiled("it.eng.spagobi.meta.Product::Product:it.eng.spagobi.meta.Product_product_class:compId.product_family"
				, "NONE", "family", true, true, false, "NONE", null);
		query.addSelectFiled("it.eng.spagobi.meta.Product::Product:it.eng.spagobi.meta.Product_product:compId.product_id", "NONE"
				, "id", true, true, false, "NONE", null);
		
		IStatement statement = dataSource.createStatement(query);
		IDataSet dataSet =  QbeDatasetFactory.createDataSet(statement);
		int offset=0; int pageSize= 25; int maxResults = -1;
		dataSet.loadData(offset,pageSize,maxResults);
		IDataStore dataStore = dataSet.getDataStore();
		int resultSize = DataStoreReader.getMaxResult(dataStore);
		
		assertEquals(1560, resultSize);
	}
	
	public void testViewOutboundRelashionship() {
		
		ModelViewEntity viewEntity = (ModelViewEntity)dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product::Product");
		List<ViewRelationship> relationships = viewEntity.getRelationships();
		assertNotNull(relationships);
		assertEquals(1, relationships.size()); // ...because _Product_ClaSS_ is a view so there is no FK to it
		for(ViewRelationship relationship : relationships) {
			IModelEntity destinationEntity = relationship.getDestinationEntity();
			assertNotNull(destinationEntity);
			List<IModelField> destinationFields = relationship.getDestinationFileds();
			assertNotNull(destinationFields);
			assertTrue(destinationFields.size() > 0);
			
			IModelEntity sourceEntity = relationship.getSourceEntity();
			assertNotNull(sourceEntity);
			List<IModelField> sourceFields = relationship.getSourceFileds();
			assertNotNull(sourceFields);
			assertTrue(sourceFields.size() > 0);
			
			assertTrue(sourceFields.size() == destinationFields.size());
		}
		
		Query query = new Query();
		query.addSelectFiled(
				"it.eng.spagobi.meta.Product:Product_class:Product_class:product_department"
				, "NONE", "department", true, true, false, "NONE", null);
		
		query.addSelectFiled(
				"it.eng.spagobi.meta.Product::Product:it.eng.spagobi.meta.Product_product:compId.product_id"
				, "NONE" , "id", true, true, false, "NONE", null);
		
		IStatement statement = dataSource.createStatement(query);
		IDataSet dataSet =  QbeDatasetFactory.createDataSet(statement);
		int offset=0; int pageSize= 25; int maxResults = -1;
		dataSet.loadData(offset,pageSize,maxResults);
		IDataStore dataStore = dataSet.getDataStore();
		int resultSize = DataStoreReader.getMaxResult(dataStore);
		
		assertEquals(1560, resultSize);
	}
	
	
	
	
}
