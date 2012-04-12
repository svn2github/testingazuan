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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.filter.PhysicalTableFilter;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

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
		Model model;
		

		model = ModelFactory.eINSTANCE.createModel();
		model.setName(MODEL_NAME);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
        		"PHYSICAL" + MODEL_NAME, 
        		TestConnectionFactory.createConnection(dbType),
        		CONNECTION_NAME,
        		TestCostants.MYSQL_DRIVER,
        		TestCostants.MYSQL_URL,
        		TestCostants.MYSQL_USER,
        		TestCostants.MYSQL_PWD,
        		DATABASE_NAME,
        		TestConnectionFactory.getDefaultCatalogue(dbType), 
        		TestConnectionFactory.getDefaultSchema(dbType));
		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + MODEL_NAME, physicalModel);
        
		return model;
	}
	
	public static Model createFilteredModel(TestCostants.DatabaseType dbType) {
		return createFilteredModel(dbType, FILTERED_MODEL_NAME);
	}
	public static Model createFilteredModel(TestCostants.DatabaseType dbType, String modelName) {
		Model model;
		
		String name = modelName!=null? modelName: FILTERED_MODEL_NAME;
		
		model = ModelFactory.eINSTANCE.createModel();
		model.setName(name);
		
		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		
		physicalModelInitializer.setRootModel(model);
		
		List<String> selectedTables = Arrays.asList(TestCostants.MYSQL_FILTERED_TABLES_FOR_PMODEL);   
		
		PhysicalModel physicalModel = physicalModelInitializer.initialize( 
        		"PHYSICAL" + name, 
        		TestConnectionFactory.createConnection(dbType),
        		CONNECTION_NAME,
        		TestCostants.MYSQL_DRIVER,
        		TestCostants.MYSQL_URL,
        		TestCostants.MYSQL_USER,
        		TestCostants.MYSQL_PWD,
        		DATABASE_NAME,
        		TestConnectionFactory.getDefaultCatalogue(dbType), 
        		TestConnectionFactory.getDefaultSchema(dbType), selectedTables);
		
		List<PhysicalTable> physicalTableToIncludeInBusinessModel = new ArrayList<PhysicalTable>();
		for(int i = 0; i < TestCostants.MYSQL_FILTERED_TABLES_FOR_BMODEL.length; i++) {
			physicalTableToIncludeInBusinessModel.add( physicalModel.getTable(TestCostants.MYSQL_FILTERED_TABLES_FOR_BMODEL[i]) );
		}
		PhysicalTableFilter physicalTableFilter = new PhysicalTableFilter(physicalTableToIncludeInBusinessModel);		
		BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
		BusinessModel businessModel = businessModelInitializer.initialize("BUSINESS_" + name, physicalTableFilter, physicalModel);
		
		return model;
	}
}
