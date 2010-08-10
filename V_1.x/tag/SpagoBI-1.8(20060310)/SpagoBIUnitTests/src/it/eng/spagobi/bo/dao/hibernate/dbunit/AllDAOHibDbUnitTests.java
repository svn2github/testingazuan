package it.eng.spagobi.bo.dao.hibernate.dbunit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDAOHibDbUnitTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AllDAOHibDbUnitTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for it.eng.spagobi.bo.dao.hibernate");
//		// $JUnit-BEGIN$
		suite.addTestSuite(BIObjectDAOHibImplDbUnitTest.class);
		suite.addTestSuite(BIObjectParameterDAOHibImplDbUnitTest.class);
		suite.addTestSuite(CheckDAOHibImplDbUnitTest.class);
		suite.addTestSuite(DomainDAOHibImplDbUnitTest.class);
		suite.addTestSuite(EngineDAOHibImplDbUnitTest.class);
		suite.addTestSuite(LovDAOHibImplDbUnitTest.class);
		suite.addTestSuite(LowFunctionalityDAOHibImplDbUnitTest.class);
		suite.addTestSuite(ParameterDAOHibImplDbUnitTest.class);
		suite.addTestSuite(ParameterUseDAOHibImplDbUnitTest.class);
		suite.addTestSuite(RoleDAOHibImplDbUnitTest.class);
//		// $JUnit-END$
		return suite;
	}

}
