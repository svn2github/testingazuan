/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.generator.jpamapping.keys;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaMappingCompKeyGeneratorTestCase extends AbstractJpaMappingViewsGenertorTestCase {

	// a model with composite keys only but no relationships
	private static final File TEST_MODEL_COMPKEY = new File(TEST_FOLDER, "models/generateKeys/CompKey.sbimodel");
	
	
	protected void setUp() throws Exception {
		super.setUp();
		this.model = serializer.deserialize(TEST_MODEL_COMPKEY);
		this.outputFolder = new File(OUTPUT_FOLDER, "compKey");
	}
	
	
	public void testCompKey() {
		doTest();
	}
	
	public void doTest() {
		super.doTest();
		// add specific tests here
	}
}
