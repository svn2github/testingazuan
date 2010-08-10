package it.eng.spagobi.presentation.publishers;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPublisherJUnitTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AllPublisherJUnitTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for it.eng.spagobi.presentation.publishers");
//		$JUnit-BEGIN$
//		suite.addTestSuite(DetailBIObjectParameterPublisherJUnitTest.class);
		suite.addTestSuite(DetailBIObjectPublisherJUnitTest.class);
		suite.addTestSuite(DetailEnginePublisherJUnitTest.class);
		suite.addTestSuite(DetailFunctionalityPublisherJUnitTest.class);
		suite.addTestSuite(DetailModalitiesChecksPublisherJUnitTest.class);
//		suite.addTestSuite(DetailModalitiesPublisherJUnitTest.class);
		suite.addTestSuite(DetailModalitiesValuePublisherJUnitTest.class);
		suite.addTestSuite(DetailParameterPublisherJUnitTest.class);
		suite.addTestSuite(DynamicForwardPublisherJUnitTest.class);
		suite.addTestSuite(ExecuteBIObjectPublisherJUnitTest.class);
//		suite.addTestSuite(ListBIObjectParametersPublisherJUnitTest.class);
		suite.addTestSuite(ListEnginesPublisherJUnitTest.class);
		suite.addTestSuite(ListLovsPublisherJUnitTest.class);
		suite.addTestSuite(ListModalitiesChecksPublisherJUnitTest.class);
		suite.addTestSuite(ListParametersPublisherJUnitTest.class);
//		suite.addTestSuite(ListParameterUsesPublisherJUnitTest.class);
		suite.addTestSuite(SaveConfigurationPublisherJUnitTest.class);
		suite.addTestSuite(TreeObjectsPublisherJUnitTest.class);
//		$JUnit-END$
		return suite;
	}
	
}
