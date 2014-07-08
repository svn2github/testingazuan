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
public class JpaMappingSimpleKeyGeneratorTestCase extends AbstractJpaMappingViewsGenertorTestCase {

	
	// a model without simple keys only (1 column key) but no relationships
	private static final File TEST_MODEL_SIMPLEKEY = new File(TEST_FOLDER, "models/generateKeys/SimpleKey.sbimodel");
	
	protected void setUp() throws Exception {
		super.setUp();
		this.model = serializer.deserialize(TEST_MODEL_SIMPLEKEY);
		this.outputFolder = new File(OUTPUT_FOLDER, "simpleKey");
	}
	
	public void testSimpleKey() {
		doTest();
	}
	
	public void doTest() {
		super.doTest();
		// add specific tests here
	}
}
