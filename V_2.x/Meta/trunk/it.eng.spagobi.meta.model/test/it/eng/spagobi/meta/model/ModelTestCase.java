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
package it.eng.spagobi.meta.model;

import java.io.File;

import org.junit.Assert;

import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;
import junit.framework.TestCase;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModelTestCase extends AbtractModelTestCase {	
	
	private static final File TEST_MODEL_FILE = new File(TestCaseConstants.TEST_FOLDER, "TestModel.sbimodel");

	protected void setUp() throws Exception {
		super.setUp();
		model = serializer.deserialize(TEST_MODEL_FILE);
	}

	protected void tearDown() throws Exception {
		model = null;
		super.tearDown();
	}
	
	public void testSmoke() {
		 assertNotNull("Impossible to load [" + TEST_MODEL_FILE + "]", model);
	}
	
	public void testSerializer() {
		Model testModel;
		File outFile = new File(TestCaseConstants.TEST_FOLDER, "out.sbimodel");
		serializer.serialize(model, outFile);
	       
		testModel = serializer.deserialize(outFile);
		
		Assert.assertTrue("Original model and test model have not the same name", testModel.getName().equals(model.getName()));
		Assert.assertTrue("Original model and test model have not the same number of business models", testModel.getBusinessModels().size() == model.getBusinessModels().size());
		Assert.assertTrue("Original model and test model have not the same number of physical models", testModel.getPhysicalModels().size() == model.getPhysicalModels().size());
		
		PhysicalModel physicalModel = model.getPhysicalModels().get(0);
		PhysicalModel testPhysicalModel = testModel.getPhysicalModels().get(0);
		Assert.assertTrue("Original pmodel and test pmodel have not the same number of FK", physicalModel.getForeignKeys().size() == testPhysicalModel.getForeignKeys().size());
		Assert.assertTrue("Original pmodel and test pmodel have not the same number of PK", physicalModel.getPrimaryKeys().size() == testPhysicalModel.getPrimaryKeys().size());
		Assert.assertTrue("Original pmodel and test pmodel have not the same number of tables", physicalModel.getTables().size() == testPhysicalModel.getTables().size());
		
		BusinessModel businessModel = model.getBusinessModels().get(0);
		BusinessModel testBusinessModel = testModel.getBusinessModels().get(0);
		Assert.assertTrue("Original pmodel and test pmodel have not the same number of bidentifiers", businessModel.getIdentifiers().size() == testBusinessModel.getIdentifiers().size());
		Assert.assertTrue("Original pmodel and test pmodel have not the same number of brelationship", businessModel.getRelationships().size() == testBusinessModel.getRelationships().size());
		Assert.assertTrue("Original pmodel and test pmodel have not the same number of tables", businessModel.getTables().size() == testBusinessModel.getTables().size());
		
	}
	
	public void testDeleteTable() {
		Model testModel;
		File outFile = new File(TestCaseConstants.TEST_FOLDER, "out.sbimodel");
		
		BusinessModel businessModel = model.getBusinessModels().get(0);
		BusinessTable table = (BusinessTable)businessModel.getTables().get(0);
		String tableName = table.getName();
		
		Assert.assertNotNull("Impossible to get table [" + tableName+ "]", businessModel.getBusinessTable(tableName));
		Assert.assertTrue("Ipossible to remove table [" + tableName+ "]", businessModel.getTables().remove(table) );
		Assert.assertNull("Table [" + tableName+ "] has not been removed porperly", businessModel.getBusinessTable(tableName));
		
		serializer.serialize(model, outFile);
		testModel = serializer.deserialize(outFile);
		businessModel = testModel.getBusinessModels().get(0);
		Assert.assertNull("Table [" + tableName+ "] has not been removed porperly", businessModel.getBusinessTable(tableName));
		
	}
}
