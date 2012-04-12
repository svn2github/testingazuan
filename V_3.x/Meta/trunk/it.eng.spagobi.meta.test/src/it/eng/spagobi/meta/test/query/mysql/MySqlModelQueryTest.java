package it.eng.spagobi.meta.test.query.mysql;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriverWithClassLoader;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;

import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.query.AbstractModelQueryTest;

import java.io.File;



public class MySqlModelQueryTest extends AbstractModelQueryTest {
	
	static IDataSource dataSource;
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
	
	protected void createDataSource(File jarFile) {
		ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor();			
		connectionDescriptor.setName( "Test Model" );
		connectionDescriptor.setDialect( "org.hibernate.dialect.MySQLDialect" );			
		connectionDescriptor.setDriverClass( TestCostants.MYSQL_DRIVER);	
		connectionDescriptor.setUrl( TestCostants.MYSQL_URL );
		connectionDescriptor.setUsername( TestCostants.MYSQL_USER );		
		connectionDescriptor.setPassword( TestCostants.MYSQL_PWD );
		
		IDataSourceConfiguration configuration;
		configuration = new FileDataSourceConfiguration("Test Model", jarFile);
		configuration.loadDataSourceProperties().put("connection", connectionDescriptor);
		
		dataSource = DriverManager.getDataSource(JPADriverWithClassLoader.DRIVER_ID, configuration, false);
	}
	
	public void testGenerationSmoke() {
		generator.generate(businessModel, TestCostants.outputFolder.toString());
	}
	
	public void testDataSourceSmoke() {
		File jarFile = jpaMappingJarGenerator.getJarFile();
		createDataSource(jarFile);
	}
	
	
}
