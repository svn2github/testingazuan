/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.editing;

import it.eng.spagobi.meta.model.AbtractModelTestCase;
import it.eng.spagobi.meta.model.TestCaseConstants;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;

import java.io.File;
import java.util.List;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ChangeNameTestCase extends AbtractModelTestCase {	
	
	public static final File TEST_INPUT_FOLDER = new File(TestCaseConstants.TEST_FOLDER, "models/edit");
	public static final File TEST_OUTPUT_FOLDER = new File(TestCaseConstants.TEST_OUPUT_ROOT_FOLDER, "edit");
	
	private static final File TEST_MODEL_FILE = new File(TEST_INPUT_FOLDER, "RelSimpleKey.sbimodel");

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
		assertNotNull( model.getBusinessModels() );
		assertTrue( model.getBusinessModels().size() > 0);
		
		BusinessModel businessModel = model.getBusinessModels().get(0);
		assertNotNull( businessModel );
		
		List<BusinessRelationship> relationships = businessModel.getRelationships();
		assertNotNull( relationships );
		assertTrue( relationships.size() > 0);
		
		BusinessRelationship relationship = relationships.get(0);
		assertNotNull( relationship );
		
		List<BusinessColumn> destinationColumns = relationship.getDestinationColumns();
		assertNotNull( destinationColumns );
		assertTrue( destinationColumns.size() > 0);
		
		for(BusinessColumn column : destinationColumns) {
			column.setName(column.getName() + "X");
		}
		
		// -------
		relationships = businessModel.getRelationships();
		assertNotNull( relationships );
		assertTrue( relationships.size() > 0);
		
		relationship = relationships.get(0);
		assertNotNull( relationship );
		
		destinationColumns = relationship.getDestinationColumns();
		assertNotNull( destinationColumns );
		assertTrue( destinationColumns.size() > 0);
		
		for(BusinessColumn column : destinationColumns) {
			assertTrue(column.getName().endsWith("X"));
		}
	}
	

}
