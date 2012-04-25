package it.eng.spagobi.meta.test.query.mysql;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriverWithClassLoader;
import it.eng.qbe.model.structure.FilteredModelStructure;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.ModelEntity;
import it.eng.qbe.model.structure.ModelViewEntity;
import it.eng.qbe.model.structure.ModelViewEntity.ViewRelationship;
import it.eng.qbe.model.structure.filter.IQbeTreeEntityFilter;
import it.eng.qbe.model.structure.filter.IQbeTreeFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeQueryEntityFilter;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;
import it.eng.qbe.statement.QbeDatasetFactory;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
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
import java.util.Iterator;
import java.util.List;



public class MySqlModelQueryTest extends AbstractModelQueryTest {
	
	static JpaMappingJarGenerator jpaMappingJarGenerator;
	static IDataSource dataSource;
	
	public void setUp() throws Exception {
		try {
			// if this is the first test on postgres after the execution
			// of tests on an other database force a tearDown to clean
			// and regenerate properly all the static variables contained in
			// parent class AbstractSpagoBIMetaTest
			if(dbType != TestCostants.DatabaseType.MYSQL){
				doTearDown();
			}
			super.setUp();
			
			if(dbType == null) dbType = TestCostants.DatabaseType.MYSQL;
						
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
	
	protected void createDataSource(String modelName, File jarFile) {
		IDataSourceConfiguration configuration = new FileDataSourceConfiguration(modelName, jarFile);
		ConnectionDescriptor connectionDescriptor = TestModelFactory.getConnectionDescriptor(dbType);
		configuration.loadDataSourceProperties().put("connection", connectionDescriptor);
		dataSource = DriverManager.getDataSource(JPADriverWithClassLoader.DRIVER_ID, configuration, false);
	}
	
	public void testRootModelGenerationSmoke() {
		jpaMappingJarGenerator.generate(businessModel, TestCostants.outputFolder.toString());
	}
	
	public void testRootModelDataSourceSmoke() {
		File jarFile = jpaMappingJarGenerator.getJarFile();
		createDataSource("BUSINESS_MODEL_TEST", jarFile);
	}

	// =============================================
	// TESTS ON VIEW MODEL
	// =============================================
	
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
	
	public void testViewModelDataSourceSmoke() {
		File jarFile = jpaMappingJarGenerator.getJarFile();
		createDataSource("BUSINESS_VIEW_MODEL_TEST", jarFile);
		
		IModelEntity viewEntity = dataSource.getModelStructure().getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product::Product");
		assertNotNull(viewEntity);
		assertEquals("it.eng.spagobi.meta.Product::Product", viewEntity.getUniqueName());
		assertTrue(viewEntity instanceof ModelViewEntity);		
	}
	
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
	
	public void testViewVisibilityProperty() {
		IQbeTreeEntityFilter entityFilter = new QbeTreeAccessModalityEntityFilter();
		entityFilter = new QbeTreeOrderEntityFilter(entityFilter);
		
		IQbeTreeFieldFilter fieldFilter = new QbeTreeAccessModalityFieldFilter();
		fieldFilter = new QbeTreeOrderFieldFilter(fieldFilter);
		
		QbeTreeFilter treeFilter = new  QbeTreeFilter(entityFilter, fieldFilter);
		
		FilteredModelStructure filteredModelStructure = new FilteredModelStructure(
				dataSource.getModelStructure(), dataSource, treeFilter);
		
		// test inner entities visibility
		IModelEntity innerEntityProduct = filteredModelStructure.getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product::Product_product");
		assertNull(innerEntityProduct);
		
		IModelEntity innerEntityProductClass = filteredModelStructure.getRootEntity(
				"BUSINESS_VIEW_MODEL_TEST", "it.eng.spagobi.meta.Product_product_class::Product_product_class");
		assertNull(innerEntityProductClass);
		
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
		assertEquals(2, relationships.size());
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
				"it.eng.spagobi.meta.Product:Product_class1:Product_class1:product_department"
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
