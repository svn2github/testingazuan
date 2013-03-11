/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.serialization;

import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.serialization.mysql.MySqlBusinessModelSerializationTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModelSerializationTestSuite extends TestCase {
	static public Test suite() {
		TestSuite suite = new TestSuite("Seraialization tests");
		if(TestCostants.enableTestsOnMySql) suite.addTestSuite(MySqlBusinessModelSerializationTest.class);
		return suite;
	}
}