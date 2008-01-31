package it.eng.spagobi.tools.importexport;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;


/**
 * Implements methods for recording the association of roles, engines, and connection 
 * setted by the user. The association recorder can be exported into xml format 
 */
public class UserAssociationsKeeper {
    

    static private Logger logger = Logger.getLogger(UserAssociationsKeeper.class);

	private SourceBean associationSB = null;
	private SourceBean roleAssSB = null;
	private SourceBean engineAssSB = null;
	private SourceBean connectionAssSB = null;
	
	/**
	 * Defines the internal structure for recording associations
	 */
	public UserAssociationsKeeper() {
	    logger.debug("IN");
		try{
			associationSB = new SourceBean("USER_ASSOCIATIONS");
			roleAssSB = new SourceBean("ROLE_ASSOCIATIONS");
			engineAssSB = new SourceBean("ENGINE_ASSOCIATIONS");
			connectionAssSB = new SourceBean("CONNECTION_ASSOCIATIONS");
			associationSB.setAttribute(roleAssSB);
			associationSB.setAttribute(engineAssSB);
			associationSB.setAttribute(connectionAssSB);
		} catch (Exception e) {
			logger.error("Error while creating the association SourceBean \n " , e );
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	/**
	 * Records an association between an exported role and an existing one
	 * @param exportedRoleName the name of the exported role
	 * @param existingRolename the name of the existing role
	 */
	public void recordRoleAssociation(String exportedRoleName, String existingRolename) {
	    logger.debug("IN");
		if( (associationSB==null) || (roleAssSB==null) ) {
		    logger.warn("Cannot record the association between exported role "+exportedRoleName+" " +
		            			  "and the role " + existingRolename + ", the association SourceBean is null");
			return;
		}
		try{
			SourceBean roleSB = (SourceBean) roleAssSB.getFilteredSourceBeanAttribute("ROLE_ASSOCIATION", "exported", exportedRoleName);
			// association already recorder
			if (roleSB != null) {
				roleSB.updAttribute("associatedTo", existingRolename);
				//return;
			} else {
				// record association
				roleSB = new SourceBean("ROLE_ASSOCIATION");
				roleSB.setAttribute("exported", exportedRoleName);
				roleSB.setAttribute("associatedTo", existingRolename);
				roleAssSB.setAttribute(roleSB);
			}
		} catch (Exception e) {
		    logger.error( "Error while recording the association between exported role "+exportedRoleName+" " +
		            			  "and the role " + existingRolename + " \n " , e);
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	/**
	 * Records an association between an exported engine and an existing one
	 * @param exportedEngineLabel the label of the exported engine
	 * @param existingEngineLabel the label of the existing engine
	 */
	public void recordEngineAssociation(String exportedEngineLabel, String existingEngineLabel) {
	    logger.debug("IN");
		if( (associationSB==null) || (engineAssSB==null) ) {
		    logger.warn("Cannot record the association between exported engine "+exportedEngineLabel+" " +
		            			  "and the engine " + existingEngineLabel + ", the association SourceBean is null");
			return;
		}
		try{
			SourceBean engineSB = (SourceBean) engineAssSB.getFilteredSourceBeanAttribute("ENGINE_ASSOCIATION", "exported", exportedEngineLabel);
			// association already recorder
			if(engineSB != null) {
				engineSB.updAttribute("associatedTo", existingEngineLabel);
				//return;
			} else {
				// record association
				engineSB = new SourceBean("ENGINE_ASSOCIATION");
				engineSB.setAttribute("exported", exportedEngineLabel);
				engineSB.setAttribute("associatedTo", existingEngineLabel);
				engineAssSB.setAttribute(engineSB);
			}
		} catch (Exception e) {
		    logger.error("Error while recording the association between exported engine "+exportedEngineLabel+" " +
		            			  "and the engine " + existingEngineLabel + " \n " , e);
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	/**
	 * Records an association between an exported connection and an existing one
	 * @param exportedConName the name of the exported connection
	 * @param existingConName the name of the existing connection
	 */
	public void recordConnectionAssociation(String exportedConName, String existingConName) {
	    logger.debug("IN");
		if( (associationSB==null) || (connectionAssSB==null) ) {
		    logger.warn("Cannot record the association between exported connection "+exportedConName+" " +
		            			  "and the connection " + existingConName + ", the association SourceBean is null");
			return;
		}
		try{
			SourceBean conSB = (SourceBean) connectionAssSB.getFilteredSourceBeanAttribute("CONNECTION_ASSOCIATION", "exported", exportedConName);
			// association already recorder
			if(conSB != null) {
				conSB.updAttribute("associatedTo", existingConName);
				//return;
			} else {
				// record association
				conSB = new SourceBean("CONNECTION_ASSOCIATION");
				conSB.setAttribute("exported", exportedConName);
				conSB.setAttribute("associatedTo", existingConName);
				connectionAssSB.setAttribute(conSB);
			}
		} catch (Exception e) {
		    logger.error("Error while recording the association between exported connection "+exportedConName+" " +
		            			  "and the connection " + existingConName + " \n " , e);
		}finally{
		    logger.debug("OUT");
		    
		}
	}
	
	
	/**
	 * Exports the associations as xml
	 * @return the xml representation of the associations
	 */
	public String toXml() {
	    logger.debug("IN");
		String xml = "";
		try{
			xml = associationSB.toXML(false);
		} catch (Exception e) {
		    logger.error("Error while exporting the association SourceBean to xml  \n " , e);
		}
		logger.debug("OUT");
		return xml;
	}
	
	
	/**
	 * Fill the associations reading an xml string
	 * @param xmlStr the xml string which defines the associations 
	 */
	public void fillFromXml(String xmlStr) {
	    logger.debug("IN");
		try {
			SourceBean associationSBtmp = SourceBean.fromXMLString(xmlStr);
			SourceBean roleAssSBtmp = (SourceBean)associationSBtmp.getAttribute("ROLE_ASSOCIATIONS");
			if(roleAssSBtmp==null) throw new Exception("Cannot recover ROLE_ASSOCIATIONS bean");
			SourceBean engineAssSBtmp = (SourceBean)associationSBtmp.getAttribute("ENGINE_ASSOCIATIONS");
			if(engineAssSBtmp==null) throw new Exception("Cannot recover ENGINE_ASSOCIATIONS bean");
			SourceBean connectionAssSBtmp = (SourceBean)associationSBtmp.getAttribute("CONNECTION_ASSOCIATIONS");
			if(connectionAssSBtmp==null) throw new Exception("Cannot recover CONNECTION_ASSOCIATIONS bean");
			associationSB = associationSBtmp;
			roleAssSB = roleAssSBtmp;
			engineAssSB = engineAssSBtmp;
			connectionAssSB = connectionAssSBtmp;
		} catch (Exception e) {
		    logger.error("Error while loading SourceBean from xml  \n " , e);
		}finally{
		    logger.debug("OUT");
		}
	}
	
	public String getAssociatedRole(String expRoleName) {
	    logger.debug("IN");
		String assRole = null;
		SourceBean assRoleSB = (SourceBean)roleAssSB.getFilteredSourceBeanAttribute("ROLE_ASSOCIATION", "exported", expRoleName);
		if(assRoleSB!=null) {
			assRole = (String)assRoleSB.getAttribute("associatedTo");
			if(assRole.trim().equals("")) {
				assRole = null;
			}
		}
		logger.debug("OUT");
		return assRole;
	}
	
	public String getAssociatedEngine(String expEngineLabel) {
		logger.debug("IN");
		String assEngine = null;
		SourceBean assEngineSB = (SourceBean)engineAssSB.getFilteredSourceBeanAttribute("ENGINE_ASSOCIATION", "exported", expEngineLabel);
		if(assEngineSB!=null) {
			assEngine = (String)assEngineSB.getAttribute("associatedTo");
			if(assEngine.trim().equals("")) {
				assEngine = null;
			}
		}
		logger.debug("OUT");
		return assEngine;
	}	

	public String getAssociatedConnection(String expConnectionName) {
		logger.debug("IN");
		String assConnection = null;
		SourceBean assConnectionSB = (SourceBean)connectionAssSB.getFilteredSourceBeanAttribute("CONNECTION_ASSOCIATION", "exported", expConnectionName);
		if(assConnectionSB!=null) {
			assConnection = (String)assConnectionSB.getAttribute("associatedTo");
			if(assConnection.trim().equals("")) {
				assConnection = null;
			}
		}
		logger.debug("OUT");
		return assConnection;
	}
	
}
