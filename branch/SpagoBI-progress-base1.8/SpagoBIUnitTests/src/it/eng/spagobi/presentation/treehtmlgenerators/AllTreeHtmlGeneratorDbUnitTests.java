package it.eng.spagobi.presentation.treehtmlgenerators;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTreeHtmlGeneratorDbUnitTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AllTreeHtmlGeneratorDbUnitTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for it.eng.spagobi.presentation.treehtmlgenerators");
//		// $JUnit-BEGIN$
		suite.addTestSuite(AdminTreeHtmlGeneratorDbUnitTest.class);
		suite.addTestSuite(DevTreeHtmlGeneratorDbUnitTest.class);
		suite.addTestSuite(ExecTreeHtmlGeneratorDbUnitTest.class);
		suite.addTestSuite(FunctionalitiesTreeInsertObjectHtmlGeneratorDbUnitTest.class);
//		// $JUnit-END$
		return suite;
	}

}
