
package it.eng.spagobi.meta.test.mapper;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.mapper.cwm.CWMImplType;
import it.eng.spagobi.meta.mapper.cwm.CWMMapperFactory;
import it.eng.spagobi.meta.mapper.cwm.ICWM;
import it.eng.spagobi.meta.mapper.cwm.ICWMMapper;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.TestConnectionFactory;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.util.ModelPrinter;



/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CWMMapperEncodeTest {
	
	public static String FILENAME = "cwmmodel.xmi";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	
		TestCostants.DatabaseType dbType;
		Model rootModel;
		PhysicalModel physicalModel;
		PhysicalModelInitializer modelInitializer;
		BusinessModel businessModel;
		BusinessModelInitializer businessModelInitializer;
		
		ICWMMapper modelMapper;
		ICWM cwm;
		        
		dbType = TestCostants.DatabaseType.MYSQL;
		
        // db schema -> spagobi model -> cwm (jmi) -> xmi
		       
		rootModel = ModelFactory.eINSTANCE.createModel();
		rootModel.setName("modelDemo");
		
		modelInitializer = new PhysicalModelInitializer();
		modelInitializer.setRootModel(rootModel);		
		physicalModel = modelInitializer.initialize( 
        		"physicalModelDemo", 
        		TestConnectionFactory.createConnection(dbType),
        		"Test Connection",
        		TestCostants.MYSQL_DRIVER,
        		TestCostants.MYSQL_URL,
        		TestCostants.MYSQL_USER,
        		TestCostants.MYSQL_PWD,
        		"DB Name",
        		TestConnectionFactory.getDefaultCatalogue(dbType), 
        		TestConnectionFactory.getDefaultSchema(dbType));
        
        
        businessModelInitializer = new BusinessModelInitializer();
        businessModel = businessModelInitializer.initialize("businessModelDemo", physicalModel);
        
        ModelPrinter.print(businessModel);
       
        modelMapper = CWMMapperFactory.getMapper(CWMImplType.JMI);
        cwm = modelMapper.encodeICWM(physicalModel);        
        cwm.exportToXMI(FILENAME);        
	}

}
