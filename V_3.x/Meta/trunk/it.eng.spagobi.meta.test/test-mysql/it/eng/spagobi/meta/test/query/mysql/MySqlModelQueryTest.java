package it.eng.spagobi.meta.test.query.mysql;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriverWithClassLoader;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;
import it.eng.qbe.statement.QbeDatasetFactory;
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
		generator = TestGeneratorFactory.createJarGeneraor();
		generator.generate(businessModel, TestCostants.outputFolder.toString());
	}
	
	public void testRootModelDataSourceSmoke() {
		File jarFile = generator.getJarFile();
		createDataSource("BUSINESS_MODEL_TEST", jarFile);
	}

	// =============================================
	// TESTS ON VIEW MODEL
	// =============================================
	
	public void testViewModelGenerationSmoke() {
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
		
		generator = TestGeneratorFactory.createJarGeneraor();
		generator.generate(viewBusinessModel, TestCostants.outputFolder.toString());
	}
	
	public void testViewModelDataSourceSmoke() {
		File jarFile = generator.getJarFile();
		createDataSource("BUSINESS_VIEW_MODEL_TEST", jarFile);
		
		Iterator<IModelEntity> it = dataSource.getModelStructure().getRootEntityIterator("BUSINESS_VIEW_MODEL_TEST");
		while(it.hasNext()) {
			IModelEntity entity = it.next();
			String name = entity.getName();
			for(IModelField filed : entity.getAllFields()) {
				String uniqueName = filed.getUniqueName();
				assertNotNull(uniqueName);
			}
		}
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
	
	
	
	
}
