package it.eng.spagobi.security;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.profiling.bean.SbiAttribute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class InternalSecurityInfoProviderImpl implements ISecurityInfoProvider{

	static private Logger logger = Logger.getLogger(InternalSecurityInfoProviderImpl.class);
	
	public List getAllProfileAttributesNames() {
    	logger.debug("IN");
		List attributes = new ArrayList();
		//gets attributes from database
		try {
			List<SbiAttribute> sbiAttributes = DAOFactory.getSbiAttributeDAO().loadSbiAttributes();
			Iterator it = sbiAttributes.iterator();
			while(it.hasNext()) {
				SbiAttribute attribute = (SbiAttribute)it.next();

				attributes.add(attribute.getAttributeName());
			}
		} catch (EMFUserError e) {
			logger.error(e.getMessage());
		}

		logger.debug("OUT");
		return attributes;
	}

	public List getRoles() {
    	logger.debug("IN");
    	//get roles from database
		List roles = new ArrayList();

		//gets roles from database
		try {
			roles = DAOFactory.getRoleDAO().loadAllRoles();

		} catch (EMFUserError e) {
			logger.error(e.getMessage());
		}
		logger.debug("OUT");
		return roles;
	}

}
