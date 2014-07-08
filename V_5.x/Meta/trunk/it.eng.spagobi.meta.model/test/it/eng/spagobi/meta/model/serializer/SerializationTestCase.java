/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.serializer;

import it.eng.spagobi.meta.model.AbtractModelTestCase;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.TestCaseConstants;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.io.File;

import org.junit.Assert;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SerializationTestCase extends AbtractModelTestCase {	
	
	
	public static final File TEST_INPUT_FOLDER = new File(TestCaseConstants.TEST_FOLDER, "models/serializer");
	public static final File TEST_OUTPUT_FOLDER = new File(TestCaseConstants.TEST_OUPUT_ROOT_FOLDER, "serializer");
	
	private static final File TEST_MODEL_FILE = new File(TEST_INPUT_FOLDER, "TestModel.sbimodel");
	
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
		File outFile = new File(TEST_OUTPUT_FOLDER, "out.sbimodel");
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
		File outFile = new File(TEST_OUTPUT_FOLDER, "out.sbimodel");
		
		BusinessModel businessModel = model.getBusinessModels().get(0);
		BusinessTable table = (BusinessTable)businessModel.getTables().get(0);
		String tableUniqueName = table.getUniqueName();
		
		Assert.assertNotNull("Impossible to get table [" + tableUniqueName+ "]", businessModel.getBusinessTableByUniqueName(tableUniqueName));
		Assert.assertTrue("Ipossible to remove table [" + tableUniqueName+ "]", businessModel.getTables().remove(table) );
		Assert.assertNull("Table [" + tableUniqueName+ "] has not been removed porperly", businessModel.getBusinessTableByUniqueName(tableUniqueName));
		
		serializer.serialize(model, outFile);
		testModel = serializer.deserialize(outFile);
		businessModel = testModel.getBusinessModels().get(0);
		Assert.assertNull("Table [" + tableUniqueName+ "] has not been removed porperly", businessModel.getBusinessTableByUniqueName(tableUniqueName));
		
	}
}
