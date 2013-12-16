/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.backcompatibility;

import it.eng.spagobi.meta.test.query.mysql.MySqlModelQueryTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BackCompatibilityTestSuite extends TestCase {
	static public Test suite() {
		TestSuite suite = new TestSuite("Back compatibility tests");
		suite.addTestSuite(BackCompatibility33Test.class);
		// add here test suites of other versions ...
		return suite;
	}
}