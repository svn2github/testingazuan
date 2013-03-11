/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.initializer;

import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.AbstractSpagoBIMetaTest;
import it.eng.spagobi.meta.test.TestModelFactory;

import org.junit.Assert;


public class AbstractPhysicalModelInizializtaionTest extends AbstractSpagoBIMetaTest {
	
	// add generic tests related to physical model here ...
	
	public void testModelInitializationSmoke() {
		assertNotNull("Metamodel cannot be null", rootModel);
	}
	
	public void testPhysicalModelInitializationSmoke() {
		assertTrue("Metamodel must have one physical model ", rootModel.getPhysicalModels().size() == 1);
	}
	
	public void testBusinessModelInitializationSmoke() {
		assertTrue("Metamodel must have one business model ", rootModel.getBusinessModels().size() == 1);	
	}
	
	public void testPropertyConnectionName() {
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.name";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] must be defined", modelPropertyType);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] default value cannot be null", modelPropertyType.getDefaultValue());
		Assert.assertEquals(TestModelFactory.CONNECTION_NAME, modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionDriver() {
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.driver";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] must be defined", modelPropertyType);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] default value cannot be null", modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionUrl() {
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.url";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] must be defined", modelPropertyType);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] default value cannot be null", modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionUser() {
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.username";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] must be defined", modelPropertyType);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] default value cannot be null", modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionPassword() {
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.password";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] must be defined", modelPropertyType);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] default value cannot be null", modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionDatabaseName() {
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.databasename";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] must be defined", modelPropertyType);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] default value cannot be null", modelPropertyType.getDefaultValue());
		Assert.assertEquals(TestModelFactory.DATABASE_NAME, modelPropertyType.getDefaultValue());
	}
	
	public void testPhysicalModelSourceDatabase() {
		Assert.assertNotNull("Database name connot be null", physicalModel.getDatabaseName());
		Assert.assertNotNull("Database version connot be null", physicalModel.getDatabaseVersion());
	}

	
}
