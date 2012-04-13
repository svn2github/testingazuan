package it.eng.spagobi.meta.test.generator;

import it.eng.spagobi.meta.generator.IGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.utils.ModelManager;
import junit.framework.TestCase;


public class AbstractJpaMappingGenerationTest extends TestCase {

	protected static PhysicalModelInitializer physicalModelInitializer;
	protected static BusinessModelInitializer businessModelInitializer;
	
	protected static TestCostants.DatabaseType dbType;
	
	protected static Model rootModel;
	protected static PhysicalModel physicalModel;
	protected static BusinessModel businessModel;
	
	protected static Model viewModel;
	protected static PhysicalModel viewPhysicalModel;
	protected static BusinessModel viewBusinessModel;

	protected static IGenerator generator = null;
	
	protected boolean tearDown = false;
    
	public AbstractJpaMappingGenerationTest() {
		super();
	}
	
	public void setUp() throws Exception {
		try {
			
			if(physicalModelInitializer == null)  physicalModelInitializer = new PhysicalModelInitializer();
			if(businessModelInitializer == null)  businessModelInitializer = new BusinessModelInitializer();
			tearDown = false;
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	protected void tearDown() throws Exception {
		if(tearDown) {
			dbType = null;
			rootModel=null;
			physicalModel=null;
			businessModel=null;
			physicalModelInitializer=null;
			businessModelInitializer=null;
			generator = null;
		}
	}
	
	public void setRootModel(Model model) {
		rootModel = model;
		if(rootModel != null && rootModel.getPhysicalModels() != null && rootModel.getPhysicalModels().size() > 0) {
			physicalModel = rootModel.getPhysicalModels().get(0);
		}
		if(rootModel != null && rootModel.getBusinessModels() != null && rootModel.getBusinessModels().size() > 0) {
			businessModel = rootModel.getBusinessModels().get(0);
		}
	}
	
	public void setViewModel(Model model) {
		viewModel = model;
		if(viewModel != null && viewModel.getPhysicalModels() != null && viewModel.getPhysicalModels().size() > 0) {
			viewPhysicalModel = viewModel.getPhysicalModels().get(0);
		}
		if(viewModel != null && viewModel.getBusinessModels() != null && viewModel.getBusinessModels().size() > 0) {
			viewBusinessModel = viewModel.getBusinessModels().get(0);
		}
	}
	
}
