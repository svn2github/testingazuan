/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.generator.postgres;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PostgresJpaMappingTestSuite extends TestCase {
	static public Test suite() {
		TestSuite suite = new TestSuite("Generation tests on Postgres");
		
		suite.addTestSuite(PostgresJpaModelTest.class);
		suite.addTestSuite(PostgresJpaMappingCodeGenerationTest.class);
		suite.addTestSuite(PostgresJpaMappingClassesGenerationTest.class);
		suite.addTestSuite(PostgresJpaMappingJarGenerationTest.class);
		return suite;
	}
}