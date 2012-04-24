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
package it.eng.spagobi.meta.test;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.filter.PhysicalTableFilter;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.TestCostants.DatabaseType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class TestModelFactory {
	
	public static final String MODEL_NAME = "MODEL_TEST";
	public static final String FILTERED_MODEL_NAME = "FILTERED_MODEL_TEST";
	public static final String CONNECTION_NAME = "Test connection";
	public static final String DATABASE_NAME = "Test Database";
	
	public static Model createModel(TestCostants.DatabaseType dbType) {
		Model model = null;
		switch(dbType) {
        	case MYSQL:   
        		model = createModelOnMySql();
        		break;
        	case POSTGRES:  
        		model = createModelOnPostgres();
        		break;
        	case ORACLE:  
        		model = createModelOnOracle();
        		break;
		}
		
		return model;
	}
	
	public static Model createFilteredModel(TestCostants.DatabaseType dbType) {
		return createFilteredModel(dbType, FILTERED_MODEL_NAME);
	}
	public static Model createFilteredModel(TestCostants.DatabaseType dbType, String modelName) {
		Model model = null;
		switch(dbType) {
        	case MYSQL:   
        		model = createFilteredModelOnMySql(modelName);
        		break;
        	case POSTGRES:  
        		model = createFilteredModelOnPostgres(modelName);
        		break;
        	case ORACLE:  
        		model = createFilteredModelOnOracle(modelName);
        		break;
		}
		
		return model;
	}

	public static ConnectionDescriptor getConnectionDescriptor(DatabaseType dbType) {
		ConnectionDescriptor connectionDescriptor = null;			
		switch(dbType) {
    	case MYSQL:   
    		connectionDescriptor = getConnectionDescriptorOnMySql();
    		break;
    	case POSTGRES:  
    		connectionDescriptor = getConnectionDescriptorOnPostgres();
    		break;
    	case ORACLE:  
    		connectionDescriptor = getConnectionDescriptorOnOracle();
    		break;
	}
		return connectionDescriptor;
	}
	
	// =======================================================
	// MYSQL
	// =======================================================
	private static Model createModelOnMySql() {
		Model model;
		

		model = ModelFactory.eINSTANCE.createModel();
		model.setName(MODEL_NAME);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
        		"PHYSICAL" + MODEL_NAME, 
        		TestConnectionFactory.createConnection(TestCostants.DatabaseType.MYSQL),
        		CONNECTION_NAME,
        		TestCostants.MYSQL_DRIVER,
        		TestCostants.MYSQL_URL,
        		TestCostants.MYSQL_USER,
        		TestCostants.MYSQL_PWD,
        		DATABASE_NAME,
        		TestCostants.MYSQL_DEFAULT_CATALOGUE, 
        		TestCostants.MYSQL_DEFAULT_SCHEMA
        );
		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + MODEL_NAME, physicalModel);
        
		return model;
	}
	
	public static Model createFilteredModelOnMySql(String modelName) {
		Model model;
		
		String name = modelName!=null? modelName: FILTERED_MODEL_NAME;
		
		model = ModelFactory.eINSTANCE.createModel();
		model.setName(name);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);
		
		List<String> selectedTables = Arrays.asList(TestCostants.MYSQL_FILTERED_TABLES_FOR_PMODEL);   
		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
        		"PHYSICAL" + name, 
        		TestConnectionFactory.createConnection(TestCostants.DatabaseType.MYSQL),
        		CONNECTION_NAME,
        		TestCostants.MYSQL_DRIVER,
        		TestCostants.MYSQL_URL,
        		TestCostants.MYSQL_USER,
        		TestCostants.MYSQL_PWD,
        		DATABASE_NAME,
        		TestConnectionFactory.getDefaultCatalogue(TestCostants.DatabaseType.MYSQL), 
        		TestConnectionFactory.getDefaultSchema(TestCostants.DatabaseType.MYSQL), selectedTables);
		
		List<PhysicalTable> physicalTableToIncludeInBusinessModel = new ArrayList<PhysicalTable>();
		for(int i = 0; i < TestCostants.MYSQL_FILTERED_TABLES_FOR_BMODEL.length; i++) {
			physicalTableToIncludeInBusinessModel.add( physicalModel.getTable(TestCostants.MYSQL_FILTERED_TABLES_FOR_BMODEL[i]) );
		}
		PhysicalTableFilter physicalTableFilter = new PhysicalTableFilter(physicalTableToIncludeInBusinessModel);		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + name, physicalTableFilter, physicalModel);
		
		return model;
	}
	
	public static ConnectionDescriptor getConnectionDescriptorOnMySql() {
		ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor();			
		connectionDescriptor.setName( "Test Model" );
		connectionDescriptor.setDialect( TestCostants.MYSQL_DEFAULT_DIALECT);			
		connectionDescriptor.setDriverClass( TestCostants.MYSQL_DRIVER);	
		connectionDescriptor.setUrl( TestCostants.MYSQL_URL );
		connectionDescriptor.setUsername( TestCostants.MYSQL_USER );		
		connectionDescriptor.setPassword( TestCostants.MYSQL_PWD );
		return connectionDescriptor;
	}
	
	// =======================================================
	// POSTGRES
	// =======================================================
	private static Model createModelOnPostgres() {
		Model model;
		

		model = ModelFactory.eINSTANCE.createModel();
		model.setName(MODEL_NAME);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
        		"PHYSICAL" + MODEL_NAME, 
        		TestConnectionFactory.createConnection(TestCostants.DatabaseType.POSTGRES),
        		CONNECTION_NAME,
        		TestCostants.POSTGRES_DRIVER,
        		TestCostants.POSTGRES_URL,
        		TestCostants.POSTGRES_USER,
        		TestCostants.POSTGRES_PWD,
        		DATABASE_NAME,
        		TestCostants.POSTGRES_DEFAULT_CATALOG, 
        		TestCostants.POSTGRES_DEFAULT_SCHEMA
        );
		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + MODEL_NAME, physicalModel);
        
		return model;
	}
	
	public static Model createFilteredModelOnPostgres(String modelName) {
		Model model;
		
		String name = modelName!=null? modelName: FILTERED_MODEL_NAME;
		
		model = ModelFactory.eINSTANCE.createModel();
		model.setName(name);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);
		
		List<String> selectedTables = Arrays.asList(TestCostants.POSTGRES_FILTERED_TABLES_FOR_PMODEL);   
		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
				"PHYSICAL" + MODEL_NAME, 
        		TestConnectionFactory.createConnection(TestCostants.DatabaseType.POSTGRES),
        		CONNECTION_NAME,
        		TestCostants.POSTGRES_DRIVER,
        		TestCostants.POSTGRES_URL,
        		TestCostants.POSTGRES_USER,
        		TestCostants.POSTGRES_PWD,
        		DATABASE_NAME,
        		TestCostants.POSTGRES_DEFAULT_CATALOG, 
        		TestCostants.POSTGRES_DEFAULT_SCHEMA,
        		selectedTables
        );
		
		List<PhysicalTable> physicalTableToIncludeInBusinessModel = new ArrayList<PhysicalTable>();
		for(int i = 0; i < TestCostants.POSTGRES_FILTERED_TABLES_FOR_BMODEL.length; i++) {
			physicalTableToIncludeInBusinessModel.add( physicalModel.getTable(TestCostants.POSTGRES_FILTERED_TABLES_FOR_BMODEL[i]) );
		}
		PhysicalTableFilter physicalTableFilter = new PhysicalTableFilter(physicalTableToIncludeInBusinessModel);		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + name, physicalTableFilter, physicalModel);
		
		return model;
	}
	
	public static ConnectionDescriptor getConnectionDescriptorOnPostgres() {
		ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor();			
		connectionDescriptor.setName( "Test Model" );
		connectionDescriptor.setDialect( TestCostants.POSTGRES_DEFAULT_DIALECT);			
		connectionDescriptor.setDriverClass( TestCostants.POSTGRES_DRIVER);	
		connectionDescriptor.setUrl( TestCostants.POSTGRES_URL );
		connectionDescriptor.setUsername( TestCostants.POSTGRES_USER );		
		connectionDescriptor.setPassword( TestCostants.POSTGRES_PWD );
		return connectionDescriptor;
	}
	
	// =======================================================
	// ORACLE
	// =======================================================
	private static Model createModelOnOracle() {
		Model model;
		

		model = ModelFactory.eINSTANCE.createModel();
		model.setName(MODEL_NAME);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
        		"PHYSICAL" + MODEL_NAME, 
        		TestConnectionFactory.createConnection(TestCostants.DatabaseType.ORACLE),
        		CONNECTION_NAME,
        		TestCostants.ORACLE_DRIVER,
        		TestCostants.ORACLE_URL,
        		TestCostants.ORACLE_USER,
        		TestCostants.ORACLE_PWD,
        		DATABASE_NAME,
        		TestCostants.ORACLE_DEFAULT_CATALOGUE, 
        		TestCostants.ORACLE_DEFAULT_SCHEMA
        );
		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + MODEL_NAME, physicalModel);
        
		return model;
	}
	
	public static Model createFilteredModelOnOracle(String modelName) {
		Model model;
		
		String name = modelName!=null? modelName: FILTERED_MODEL_NAME;
		
		model = ModelFactory.eINSTANCE.createModel();
		model.setName(name);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);
		
		List<String> selectedTables = Arrays.asList(TestCostants.ORACLE_FILTERED_TABLES_FOR_PMODEL);   
		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
				"PHYSICAL" + MODEL_NAME, 
        		TestConnectionFactory.createConnection(TestCostants.DatabaseType.ORACLE),
        		CONNECTION_NAME,
        		TestCostants.ORACLE_DRIVER,
        		TestCostants.ORACLE_URL,
        		TestCostants.ORACLE_USER,
        		TestCostants.ORACLE_PWD,
        		DATABASE_NAME,
        		TestCostants.ORACLE_DEFAULT_CATALOGUE, 
        		TestCostants.ORACLE_DEFAULT_SCHEMA,
        		selectedTables);
		
		List<PhysicalTable> physicalTableToIncludeInBusinessModel = new ArrayList<PhysicalTable>();
		for(int i = 0; i < TestCostants.ORACLE_FILTERED_TABLES_FOR_BMODEL.length; i++) {
			physicalTableToIncludeInBusinessModel.add( physicalModel.getTable(TestCostants.ORACLE_FILTERED_TABLES_FOR_BMODEL[i]) );
		}
		PhysicalTableFilter physicalTableFilter = new PhysicalTableFilter(physicalTableToIncludeInBusinessModel);		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + name, physicalTableFilter, physicalModel);
		
		return model;
	}
	
	public static ConnectionDescriptor getConnectionDescriptorOnOracle() {
		ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor();			
		connectionDescriptor.setName( "Test Model" );
		connectionDescriptor.setDialect( TestCostants.ORACLE_DEFAULT_DIALECT);			
		connectionDescriptor.setDriverClass( TestCostants.ORACLE_DRIVER);	
		connectionDescriptor.setUrl( TestCostants.ORACLE_URL );
		connectionDescriptor.setUsername( TestCostants.ORACLE_USER );		
		connectionDescriptor.setPassword( TestCostants.ORACLE_PWD );
		return connectionDescriptor;
	}
}
