/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.backcompatibility;

import java.io.File;

import it.eng.qbe.datasource.IDataSource;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import junit.framework.TestCase;


public class AbstractBackCompatibilityTest extends TestCase {

	protected static TestCostants.DatabaseType dbType;
	
	protected static Model rootModel;
	protected static PhysicalModel physicalModel;
	protected static BusinessModel businessModel;

	protected static PhysicalModelInitializer physicalModelInitializer;
	protected static BusinessModelInitializer businessModelInitializer;
	
	protected static JpaMappingJarGenerator generator = null;
	
	static File oldModelsFolder;
	static IDataSource dataSource;
	
	protected boolean tearDown = false;
    
	public AbstractBackCompatibilityTest() {
		super();
	}
	
	public void setUp() throws Exception {
		try {
			if(dbType == null) dbType = TestCostants.DatabaseType.MYSQL;
			
			if(physicalModelInitializer == null)  physicalModelInitializer = new PhysicalModelInitializer();
			if(businessModelInitializer == null)  businessModelInitializer = new BusinessModelInitializer();
			if(generator == null)   generator = TestGeneratorFactory.createJarGeneraor();
			if(oldModelsFolder == null) {
				File testProjectRootFolder = new File(TestCostants.workspaceFolder, "it.eng.spagobi.meta.test");
				oldModelsFolder = new File(testProjectRootFolder, "resources/backcompatibility");
			}
			
			tearDown = false;
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	protected void tearDown() throws Exception {
		if(tearDown) {
			rootModel=null;
			physicalModel=null;
			businessModel=null;
			physicalModelInitializer=null;
			businessModelInitializer=null;
			generator = null;
		}
	}
	
	public void setRootModel(Model model) {
		rootModel = model;
		if(rootModel != null && rootModel.getPhysicalModels() != null && rootModel.getPhysicalModels().size() > 0) {
			physicalModel = rootModel.getPhysicalModels().get(0);
		}
		if(rootModel != null && rootModel.getBusinessModels() != null && rootModel.getBusinessModels().size() > 0) {
			businessModel = rootModel.getBusinessModels().get(0);
		}
	}
}
