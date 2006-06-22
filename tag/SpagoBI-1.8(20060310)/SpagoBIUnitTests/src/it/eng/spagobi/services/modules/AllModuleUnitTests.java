package it.eng.spagobi.services.modules;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllModuleUnitTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AllModuleUnitTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for it.eng.spagobi.services.modules");
//		$JUnit-BEGIN$
		suite.addTestSuite(DetailBIObjectModuleDbUnitTest.class);
//		suite.addTestSuite(DetailBIObjectParameterModuleDbUnitTest.class);
		suite.addTestSuite(DetailChecksModuleDbUnitTest.class);
		suite.addTestSuite(DetailEngineModuleDbUnitTest.class);
		suite.addTestSuite(DetailFunctionalityModuleDbUnitTest.class);
//		suite.addTestSuite(DetailModalitiesModuleDbUnitTest.class);
		suite.addTestSuite(DetailModalitiesValueModuleDbUnitTest.class);
		suite.addTestSuite(DetailParameterModuleDbUnitTest.class);
		suite.addTestSuite(ExecuteBIObjectModuleDbUnitTest.class);
//		suite.addTestSuite(ListBIObjectParametersModuleDbUnitTest.class);
		suite.addTestSuite(ListChecksModuleDbUnitTest.class);
		suite.addTestSuite(ListEnginesModuleDbUnitTest.class);
		suite.addTestSuite(ListLookupLovModuleDbUnitTest.class);
		suite.addTestSuite(ListLookupModalityValuesModuleDbUnitTest.class);
		suite.addTestSuite(ListLookupParametersModuleDbUnitTest.class);
		suite.addTestSuite(ListLovsModuleDbUnitTest.class);
		suite.addTestSuite(ListParametersModuleDbUnitTest.class);
//		suite.addTestSuite(ListParameterUsesModuleDbUnitTest.class);
		suite.addTestSuite(ListPredefinedChecksModuleDbUnitTest.class);
		suite.addTestSuite(ListTestQueryModuleDbUnitTest.class);
		suite.addTestSuite(ListTestScriptModuleDbUnitTest.class);
		suite.addTestSuite(PortletLoginModuleJUnitTest.class);
//		suite.addTestSuite(SingleObjectExecutionModuleDbUnitTest.class);
		suite.addTestSuite(TreeObjectsModuleDbUnitTest.class);
//		$JUnit-END$
		return suite;
	}

	
}
