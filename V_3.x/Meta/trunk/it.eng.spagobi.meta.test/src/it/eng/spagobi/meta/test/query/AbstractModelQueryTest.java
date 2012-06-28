/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.query;

import java.io.File;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriverWithClassLoader;
import it.eng.qbe.model.structure.FilteredModelStructure;
import it.eng.qbe.model.structure.filter.IQbeTreeEntityFilter;
import it.eng.qbe.model.structure.filter.IQbeTreeFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderFieldFilter;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.test.AbstractSpagoBIMetaTest;
import it.eng.spagobi.meta.test.TestModelFactory;


public class AbstractModelQueryTest extends AbstractSpagoBIMetaTest {

	protected static JpaMappingJarGenerator jpaMappingJarGenerator;
	protected static IDataSource dataSource;
	
	
	public AbstractModelQueryTest() {
		super();
	}
	
	protected void doTearDown() {
		super.doTearDown();
		jpaMappingJarGenerator = null;
		dataSource = null;		
	}
	
	protected void createDataSource(String modelName, File jarFile) {
		IDataSourceConfiguration configuration = new FileDataSourceConfiguration(modelName, jarFile);
		ConnectionDescriptor connectionDescriptor = TestModelFactory.getConnectionDescriptor(dbType);
		configuration.loadDataSourceProperties().put("connection", connectionDescriptor);
		dataSource = DriverManager.getDataSource(JPADriverWithClassLoader.DRIVER_ID, configuration, false);
	}
	
	protected FilteredModelStructure getFilteredDataSource(IDataSource dataSource) {
		IQbeTreeEntityFilter entityFilter = new QbeTreeAccessModalityEntityFilter();
		entityFilter = new QbeTreeOrderEntityFilter(entityFilter);
		
		IQbeTreeFieldFilter fieldFilter = new QbeTreeAccessModalityFieldFilter();
		fieldFilter = new QbeTreeOrderFieldFilter(fieldFilter);
		
		QbeTreeFilter treeFilter = new  QbeTreeFilter(entityFilter, fieldFilter);
		
		FilteredModelStructure filteredModelStructure = new FilteredModelStructure(
				dataSource.getModelStructure(), dataSource, treeFilter);
		
		return filteredModelStructure;
	}
	
	// add generic tests related to physical model here ...
	
}
