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
