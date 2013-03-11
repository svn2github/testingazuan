/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.test.edit.ModelEditingTestSuite;
import it.eng.spagobi.meta.test.generator.JpaMappingGenerationTestSuite;
import it.eng.spagobi.meta.test.initializer.ModelInitializationTestSuite;
import it.eng.spagobi.meta.test.query.ModelQueryTestSuite;
import it.eng.spagobi.meta.test.serialization.ModelSerializationTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class MetaTestSuite extends TestCase {
	static public Test suite() {
		TestSuite suite = new TestSuite("One test suite to bring them all");
		suite.addTest(ModelInitializationTestSuite.suite());  
		suite.addTest(ModelEditingTestSuite.suite());  		
		suite.addTest(ModelSerializationTestSuite.suite());
		suite.addTest(JpaMappingGenerationTestSuite.suite());
		suite.addTest(ModelQueryTestSuite.suite());
		
		return suite;
	}
}