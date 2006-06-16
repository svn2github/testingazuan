package it.eng.spagobi.init;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * This class initialize the functionalities tree 
 * using some configuration parameters.
 * The class is called from Spago Framework during the 
 * Application Start-Up.
 * 
 */

public class TreeInitializer implements InitializerIFace {
	
	private SourceBean _config;
	
	
	/**
	 * Create and initialize all the repositories defined
	 * in the configuration SourceBean, the method is called automatically
	 * from Spago Framework at application start up 
	 * if the Spago initializers.xml file is configured 
	 * @param config, SourceBean containing parameters configuration
	 * for each repository to create
	 */
	public void init(SourceBean config) {
		
		_config = config;
		
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
			    "TreeInitializer", 
			    "init", 
			    "start initialization \n" + config);
    	initialize();
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
			    "TreeInitializer", 
			    "init", 
			    "end initialization");
	}

	public SourceBean getConfig() {
		return _config;
	}

	private void initialize() {
		try {
			ILowFunctionalityDAO functionalityDAO = DAOFactory.getLowFunctionalityDAO();
			List functions = functionalityDAO.loadAllLowFunctionalities(false);
			if (functions != null && functions.size() > 0) {
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
					    "TreeInitializer", 
					    "initialize", 
					    "Tree already initialized");
			} else {
				List nodes = _config.getAttributeAsList("TREE_INITIAL_STRUCTURE.NODE");
				Iterator it = nodes.iterator();
				while (it.hasNext()) {
					SourceBean node = (SourceBean) it.next();
					String code = (String) node.getAttribute("code");
					String name = (String) node.getAttribute("name");
					String description = (String) node.getAttribute("description");
					String codeType = (String) node.getAttribute("codeType");
					String parentPath = (String) node.getAttribute("parentPath");
					LowFunctionality functionality = new LowFunctionality();
					functionality.setCode(code);
					functionality.setName(name);
					functionality.setDescription(description);
					functionality.setCodType(codeType);
					functionality.setPath(parentPath + "/" + code);
					if (parentPath != null && !parentPath.trim().equals("")) {
						// if it is not the root load the id of the parent path and set not permissions
						LowFunctionality parentFunctionality = functionalityDAO.loadLowFunctionalityByPath(parentPath, false);
						functionality.setParentId(parentFunctionality.getId());
						functionality.setDevRoles(new Role[0]);
						functionality.setExecRoles(new Role[0]);
						functionality.setTestRoles(new Role[0]);
					} else {
						// if it is the root the parent path id is set to null and set all permissions
						functionality.setParentId(null);
						List allRoles = DAOFactory.getRoleDAO().loadAllRoles();
						Role[] roles = (Role[]) allRoles.toArray(new Role[allRoles.size()]);
						functionality.setDevRoles(roles);
						functionality.setExecRoles(roles);
						functionality.setTestRoles(roles);
					}
					functionalityDAO.insertLowFunctionality(functionality, null);
				}
			}
		} catch (EMFUserError e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
				    "TreeInitializer", 
				    "initialize", 
				    "Error while initializing tree", e);
			
		}
	}
	
}
