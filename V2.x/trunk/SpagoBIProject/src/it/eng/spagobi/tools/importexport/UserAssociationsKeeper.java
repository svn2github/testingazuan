package it.eng.spagobi.tools.importexport;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

/**
 * Implements methods for recording the association of roles, engines, and connection 
 * setted by the user. The association recorder can be exported into xml format 
 */
public class UserAssociationsKeeper {

	private SourceBean associationSB = null;
	private SourceBean roleAssSB = null;
	private SourceBean engineAssSB = null;
	private SourceBean connectionAssSB = null;
	
	/**
	 * Defines the internal structure for recording associations
	 */
	public UserAssociationsKeeper() {
		try{
			associationSB = new SourceBean("USER_ASSOCIATIONS");
			roleAssSB = new SourceBean("ROLE_ASSOCIATIONS");
			engineAssSB = new SourceBean("ENGINE_ASSOCIATIONS");
			connectionAssSB = new SourceBean("CONNECTION_ASSOCIATIONS");
			associationSB.setAttribute(roleAssSB);
			associationSB.setAttribute(engineAssSB);
			associationSB.setAttribute(connectionAssSB);
		} catch (Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
					            "constructor", "Error while creating the association SourceBean \n " + e );
		}
	}
	
	
	/**
	 * Records an association between an exported role and an existing one
	 * @param exportedRoleName the name of the exported role
	 * @param existingRolename the name of the existing role
	 */
	public void recordRoleAssociation(String exportedRoleName, String existingRolename) {
		if( (associationSB==null) || (roleAssSB==null) ) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		            			  "recordRoleAssociation", 
		            			  "Cannot record the association between exported role "+exportedRoleName+" " +
		            			  "and the role " + existingRolename + ", the association SourceBean is null");
			return;
		}
		try{
			// association already recorder
			if(roleAssSB.getFilteredSourceBeanAttribute("ROLE_ASSOCIATION", "exported", exportedRoleName)!= null) {
				return;
			}
			// record association
			SourceBean roleSB = new SourceBean("ROLE_ASSOCIATION");
			roleSB.setAttribute("exported", exportedRoleName);
			roleSB.setAttribute("associatedTo", existingRolename);
			roleAssSB.setAttribute(roleSB);
		} catch (Exception e) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		                          "recordRoleAssociation", 
		                          "Error while recording the association between exported role "+exportedRoleName+" " +
		            			  "and the role " + existingRolename + " \n " + e);
		}
	}
	
	
	/**
	 * Records an association between an exported engine and an existing one
	 * @param exportedEngineLabel the label of the exported engine
	 * @param existingEngineLabel the label of the existing engine
	 */
	public void recordEngineAssociation(String exportedEngineLabel, String existingEngineLabel) {
		if( (associationSB==null) || (engineAssSB==null) ) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		            			  "recordEngineAssociation", 
		            			  "Cannot record the association between exported engine "+exportedEngineLabel+" " +
		            			  "and the engine " + existingEngineLabel + ", the association SourceBean is null");
			return;
		}
		try{
			// association already recorder
			if(engineAssSB.getFilteredSourceBeanAttribute("ENGINE_ASSOCIATION", "exported", exportedEngineLabel)!= null) {
				return;
			}
			// record association
			SourceBean engineSB = new SourceBean("ENGINE_ASSOCIATION");
			engineSB.setAttribute("exported", exportedEngineLabel);
			engineSB.setAttribute("associatedTo", existingEngineLabel);
			engineAssSB.setAttribute(engineSB);
		} catch (Exception e) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		                          "recordEngineAssociation", 
		                          "Error while recording the association between exported engine "+exportedEngineLabel+" " +
		            			  "and the engine " + existingEngineLabel + " \n " + e);
		}
	}
	
	
	/**
	 * Records an association between an exported connection and an existing one
	 * @param exportedConName the name of the exported connection
	 * @param existingConName the name of the existing connection
	 */
	public void recordConnectionAssociation(String exportedConName, String existingConName) {
		if( (associationSB==null) || (connectionAssSB==null) ) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		            			  "recordConnectionAssociation", 
		            			  "Cannot record the association between exported connection "+exportedConName+" " +
		            			  "and the connection " + existingConName + ", the association SourceBean is null");
			return;
		}
		try{
			// association already recorder
			if(connectionAssSB.getFilteredSourceBeanAttribute("CONNECTION_ASSOCIATION", "exported", exportedConName)!= null) {
				return;
			}
			// record association
			SourceBean conSB = new SourceBean("CONNECTION_ASSOCIATION");
			conSB.setAttribute("exported", exportedConName);
			conSB.setAttribute("associatedTo", existingConName);
			connectionAssSB.setAttribute(conSB);
		} catch (Exception e) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		                          "recordConnectionAssociation", 
		                          "Error while recording the association between exported connection "+exportedConName+" " +
		            			  "and the connection " + existingConName + " \n " + e);
		}
	}
	
	
	/**
	 * Exports the associations as xml
	 * @return the xml representation of the associations
	 */
	public String toXml() {
		String xml = "";
		try{
			xml = associationSB.toXML(false);
		} catch (Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
                    			"toXml", 
                    			"Error while exporting the association SourceBean to xml  \n " + e);
		}
		return xml;
	}
	
	
	/**
	 * Fill the associations reading an xml string
	 * @param xmlStr the xml string which defines the associations 
	 */
	public void fillFromXml(String xmlStr) {
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
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
        						"fillFromXml", 
        						"Error while loading SourceBean from xml  \n " + e);
		}
	}
	
	
	
	public String getAssociatedRole(String expRoleName) {
		String assRole = null;
		SourceBean assRoleSB = (SourceBean)roleAssSB.getFilteredSourceBeanAttribute("ROLE_ASSOCIATION", "exported", expRoleName);
		if(assRoleSB!=null) {
			assRole = (String)assRoleSB.getAttribute("associatedTo");
			if(assRole.trim().equals("")) {
				assRole = null;
			}
		}
		return assRole;
	}
	
	
}
