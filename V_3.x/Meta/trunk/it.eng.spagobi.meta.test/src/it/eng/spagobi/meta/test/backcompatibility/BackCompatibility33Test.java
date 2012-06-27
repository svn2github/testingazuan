/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.backcompatibility;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriverWithClassLoader;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.ModelViewEntity;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;
import it.eng.qbe.statement.QbeDatasetFactory;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.querybuilder.ui.shared.result.DataStoreReader;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.query.AbstractModelQueryTest;
import it.eng.spagobi.meta.test.serialization.EmfXmiSerializer;
import it.eng.spagobi.meta.test.serialization.IModelSerializer;
import it.eng.spagobi.meta.test.utils.ModelManager;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;



public class BackCompatibility33Test extends AbstractBackCompatibilityTest {
	
	
	
	protected void createDataSource(String modelName, File jarFile) {
		IDataSourceConfiguration configuration = new FileDataSourceConfiguration(modelName, jarFile);
		ConnectionDescriptor connectionDescriptor = TestModelFactory.getConnectionDescriptor(dbType);
		configuration.loadDataSourceProperties().put("connection", connectionDescriptor);
		dataSource = DriverManager.getDataSource(JPADriverWithClassLoader.DRIVER_ID, configuration, false);
	}
	

	public void testModelDeserialization() {
		Model model = null;
		File modelFile = new File(oldModelsFolder, "mdl_fdmrt_ky_3_3.sbimodel");
		try {
			IModelSerializer serializer = new EmfXmiSerializer();
			model = serializer.deserialize( modelFile );
		} catch(Throwable t) {
			t.printStackTrace();
			fail();
		}
		
		Assert.assertNotNull(model);
		Assert.assertNotNull(model.getBusinessModels());
		Assert.assertEquals(1, model.getPhysicalModels().size());
		Assert.assertEquals(1, model.getBusinessModels().size());
		
		this.setRootModel(model);
	}
	
	public void testModelGeneration() {
		generator = TestGeneratorFactory.createJarGeneraor();
		generator.generate(businessModel, TestCostants.outputFolder.toString());
	}
	
	public void testDataSourceCreation() {
		File jarFile = generator.getJarFile();
		createDataSource("mdl_fdmrt_ky_3_3", jarFile);
		
		Iterator<IModelEntity> it = dataSource.getModelStructure().getRootEntityIterator("mdl_fdmrt_ky_3_3");
		while(it.hasNext()) {
			IModelEntity entity = it.next();
			String name = entity.getName();
			for(IModelField filed : entity.getAllFields()) {
				String uniqueName = filed.getUniqueName();
				assertNotNull(uniqueName);
			}
		}
		
	}
	
	public void testQuerySmoke() {
		Query query = new Query();
		List<IModelEntity> rootEntities = dataSource.getModelStructure().getRootEntities("mdl_fdmrt_ky_3_3");
		IModelEntity entity = rootEntities.get(0);
		for(IModelField field: entity.getAllFields()) {
			String fieldUniqueName = field.getUniqueName();
			query.addSelectFiled(fieldUniqueName, "NONE", field.getName(), true, true, false, "NONE", null);
		}
		
		IStatement statement = dataSource.createStatement(query);
		IDataSet dataSet =  QbeDatasetFactory.createDataSet(statement);
		int offset=0; int pageSize= 25; int maxResults = -1;
		dataSet.loadData(offset,pageSize,maxResults);
		IDataStore dataStore = dataSet.getDataStore();
		int resultSize = DataStoreReader.getMaxResult(dataStore);
		
		assertTrue(resultSize > 0);
	}
	
	public void testQueryOnView() {
		Query query = null;
		List<IModelEntity> rootEntities = dataSource.getModelStructure().getRootEntities("mdl_fdmrt_ky_3_3");
		for(IModelEntity entity : rootEntities) {
			if(entity instanceof ModelViewEntity) {
				query = new Query();
				for(IModelField field : entity.getAllFields()) {
					String fieldUniqueName = field.getUniqueName();
					query.addSelectFiled(fieldUniqueName, "NONE", field.getName(), true, true, false, "NONE", null);
				}
				break;
			}
		}
		
		assertNotNull("Impossible to find one business view in the model", query);
		
		IStatement statement = dataSource.createStatement(query);
		IDataSet dataSet =  QbeDatasetFactory.createDataSet(statement);
		int offset=0; int pageSize= 25; int maxResults = -1;
		dataSet.loadData(offset,pageSize,maxResults);
		IDataStore dataStore = dataSet.getDataStore();
		int resultSize = DataStoreReader.getMaxResult(dataStore);
		
		assertTrue(resultSize > 0);
	}
	
	public void _testQueryOnCube() {
		
		//.getRootEntities("mdl_fdmrt_ky_3_3");
		
		String storeSalesFieldUniqueName = "it.eng.spagobi.meta.SalesFact1998:storeSales";
		IModelField storeSalesField = dataSource.getModelStructure().getField(storeSalesFieldUniqueName);
				
		String storeCityFieldUniqueName = "it.eng.spagobi.meta.SalesFact1998:relStoreId(relstoreid):relRegionId(relregionid):salesCity";
		//								   it.eng.spagobi.meta.Sales_fact_1998::rel_Store_id_in_Store(rel_store_id_in_store)	
		IModelField storeCityField = dataSource.getModelStructure().getField(storeCityFieldUniqueName);
		
		
		if(storeCityField == null) {
			List<IModelEntity> rootEntities = dataSource.getModelStructure().getRootEntities("mdl_fdmrt_ky_3_3");
			for(IModelEntity entity : rootEntities) {
				if(entity.getType().equals("it.eng.spagobi.meta.Sales_fact_1998")) {
					for(IModelEntity subEntity : entity.getSubEntities()) {
						String uniqueName = subEntity.getUniqueName();
						uniqueName = uniqueName;
					}
				}
			}
		}
		
		assertNotNull("Impossible to find filed [" + storeSalesFieldUniqueName + "]", storeSalesField);
		assertNotNull("Impossible to find filed [" + storeCityFieldUniqueName + "]", storeCityField);
		
	
		Query query = new Query();
		query.addSelectFiled("it.eng.spagobi.meta.SalesFact1998:relStoreId(relstoreid):relRegionId(relregionid):salesCity" 
				, "NONE", "Store City", true, true, true, "NONE", null);
		query.addSelectFiled("it.eng.spagobi.meta.SalesFact1998:storeSales"
				, "SUM", "Store Sales", true, true, false, "DESC", null);
		
		IStatement statement = dataSource.createStatement(query);
		IDataSet dataSet =  QbeDatasetFactory.createDataSet(statement);
		int offset=0; int pageSize= 25; int maxResults = -1;
		dataSet.loadData(offset,pageSize,maxResults);
		IDataStore dataStore = dataSet.getDataStore();
		int resultSize = DataStoreReader.getMaxResult(dataStore);
		
		assertEquals(1560, resultSize);
	}
	
	
	
}
