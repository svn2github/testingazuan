package it.eng.spagobi.meta.test.initializer;

import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.junit.Assert;


public class AbstractPhysicalModelInizializtaionTest extends AbstractModelInizializtaionTest {

	
	public AbstractPhysicalModelInizializtaionTest() {
		super();
	}
	
	// add generic tests related to physical model here ...
	
	public void testPropertyConnectionName() {
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.name";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] must be defined", modelPropertyType);
		Assert.assertNotNull("Property type [" + modelPropertyTypeName + "] default value cannot be null", modelPropertyType.getDefaultValue());
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
	}
	
	public void testPhysicalModelSourceDatabase() {
		Assert.assertNotNull("Database name connot be null", physicalModel.getDatabaseName());
		Assert.assertNotNull("Database version connot be null", physicalModel.getDatabaseVersion());
	}
}
