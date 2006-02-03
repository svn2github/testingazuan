/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.services.dao;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.cms.exec.OperationExecutor;
import it.eng.spago.cms.exec.OperationExecutorManager;
import it.eng.spago.cms.exec.operations.GetOperation;
import it.eng.spago.cms.exec.operations.OperationBuilder;
import it.eng.spago.cms.exec.results.ElementDescriptor;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * The DAO implementation for Tree objects.
 * 
 * @author sulis
 */
public class TreeObjectsDAO implements ITreeObjectsDAO {

	private OperationExecutor executor = null;
	private CMSConnection connection = null;
	private GetOperation getOp = null;
    private IEngUserProfile profile = null;  
	
    
    
    /**
	 * Gets an objects tree from path and User Profile information.
	 * 
	 * @param initialPath The objects tree path String
	 * @param prof The User Profile object
	 * @return Tre SourceBean containing the objects Tree.
	 * 
	 */
	public SourceBean getXmlTreeObjects(String initialPath, IEngUserProfile prof) {
		SourceBean dataTree = null; 
		try {
			executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			getOp = OperationBuilder.buildGetOperation();
			profile = prof;
			dataTree = openRecursively(initialPath, true);
			connection.close();
		} catch (Exception e) {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "TreeObjectsDAO", 
		            "getXmlTreeObjects", "Cannot get XML tree objects", e);
		}
		return dataTree;
	}

	
	
	/**
	 * A method that allows to open an object tree in a recursive way. 
	 * 
	 * @param path The tree path
	 * @param isRoot Tells us if a tree element is the root or not
	 * @return The source bean representing tree
	 */
	
	private SourceBean openRecursively(String path, boolean isRoot) {
		
		String name = "";
		String description = "";
		String type = "";
		BigDecimal idFunct = null;
		BigDecimal idType = null;
		String codeType = null;
		SourceBean bean = null;
		
		try {	
			// set the parameters for the get operation
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("true");
			getOp.setRetriveContentInformation("false");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");        	
			// get the cms node descriptor of the functionality
			ElementDescriptor desc = executor.getObject(connection, getOp, profile, false);
			SourceBean result = desc.getDescriptor();
			if(result == null) 
				return null;
            // get the type of the object
            try {
            	type = desc.getNamedElementProperties(ObjectsTreeConstants.NODE_CMS_TYPE)[0].getStringValues()[0];
            } catch (Exception e) {
              type = "";  
            }
			
            if(isRoot) {
				bean = new SourceBean("SystemFunctionalities");
				bean.setAttribute("name", "tree.rootfolder.name");
				bean.setAttribute("description", "tree.rootfolder.description");
				bean.setAttribute("path", path);
				bean.setAttribute("codeType", "LOW_FUNCT");
			} else if(type.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE) && !isRoot) {
            	bean = getInformationLowFunct(path);
            } else if(type.equalsIgnoreCase(SpagoBIConstants.REPORT_TYPE_CODE) && !isRoot) {              
            	bean = getInformationReport(path);
            } else if(type.equalsIgnoreCase(SpagoBIConstants.OLAP_TYPE_CODE) && !isRoot) {
            	bean = getInformationOlap(path);
            } else if(type.equalsIgnoreCase(SpagoBIConstants.DATAMART_TYPE_CODE) && !isRoot) {
            	bean = getInformationDatamart(path); 
            } else if(type.equalsIgnoreCase(SpagoBIConstants.DASH_TYPE_CODE) && !isRoot) {
            	bean = getInformationDash(path); 
            } 
               
            // get the child of the nodes
			List childs = result.getAttributeAsList("CHILDS.CHILD");  	 
			// for each child
			Iterator iter = childs.iterator();
	   		while(iter.hasNext()) {
	   			SourceBean child = (SourceBean)iter.next();
	   		    String pathChild = (String)child.getAttribute("path");		    
	   		    SourceBean underBean = openRecursively(pathChild, false);
	   		    if(underBean != null) {
	   		    	bean.setAttribute(underBean);
	   		    }
	   		}
	   		            
            
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "TreeObjectsDAO", 
		            "openRecursively", "Cannot execute recursive opening", e);
		}
		// return the sourceBean
		return bean;
	}


	
	
	
	/**
	 * Gets low Functionality information known its path
	 * 
	 * @param path The low functionality path
	 * @return The information Source Bean
	 * @throws EMFUserError If any exception occurs
	 */
	private SourceBean getInformationLowFunct(String path) throws EMFUserError {
		SourceBean bean = null;
		try {
        	LowFunctionality funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath(path);
        	// create the sourcebean
            bean = new SourceBean("SystemFunctionality");
            // fill the sourcebean
        	Domain domain = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("FUNCT_TYPE", "LOW_FUNCT");
        	String name = funct.getName();
			String description = funct.getDescription();
			if(description==null)
            	description = "";
			Integer idFunct = funct.getId();
			Integer idType = domain.getValueId();
			String codeType = domain.getValueCd(); 
			bean.setAttribute("id", idFunct);
	        bean.setAttribute("name", name);
	        bean.setAttribute("description", description);
	        bean.setAttribute("path", path);
	        bean.setAttribute("idType", idType);
	        bean.setAttribute("codeType", codeType);
	        bean.setAttribute("execRoles", rolesString(funct.getExecRoles()) );
	        bean.setAttribute("devRoles", rolesString(funct.getDevRoles()));
	        bean.setAttribute("testRoles", rolesString(funct.getTestRoles()));
    	} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "TreeObjectsDAO", 
					            "getInformationLowFunct", "Cannot recover detail information from database", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
    	}
    	return bean;
	}
	
	
	/**
	 * Gets Report information known BI Object path
	 * 
	 * @param path The low functionality path
	 * @return The information Source Bean
	 * @throws EMFUserError If any exception occurs
	 */
	
	private SourceBean getInformationReport(String path) throws EMFUserError {
		SourceBean bean = null;
		try {
            BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForTree(path);    	
	        bean = new SourceBean("Report");
	        // LUCA ORIGINAL CODE
	        //String name = obj.getLabel();
	    	//
	        
	        // AZ
	        String name = obj.getName();
	        String label = obj.getLabel();
	        String description = obj.getDescription();
	    	if(description==null) 
            	description = "";
	    	Integer idFunct = obj.getId();
	    	Integer idType = obj.getBiObjectTypeID();
	    	String codeType = obj.getBiObjectTypeCode();
	    	bean.setAttribute("id", idFunct);
	    	// - AZ
	    	bean.setAttribute("label", label);
	    	// - AZ
	    	bean.setAttribute("name", name);
	    	bean.setAttribute("description", description);
	    	bean.setAttribute("path", path);
	    	bean.setAttribute("idType", idType);
	    	bean.setAttribute("codeType", codeType);
	    	bean.setAttribute("state", obj.getStateCode());
    	} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "TreeObjectsDAO", 
					            "getInformationReport", "Cannot recover detail information from database", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
    	return bean;
	}
	
	/**
	 * Gets OLAP information known BI Object path
	 * 
	 * @param path The low functionality path
	 * @return The information Source Bean
	 * @throws EMFUserError If any exception occurs
	 */
	
	private SourceBean getInformationOlap(String path) throws EMFUserError {
		SourceBean bean = null;
		try {
        	BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForTree(path);
//        	 LUCA ORIGINAL CODE
	        //String name = obj.getLabel();
	    	//
        	String label = obj.getLabel();
        	String name = obj.getName();
            // fill bean
        	bean = new SourceBean("Report");
    		String description = obj.getDescription();
    		if(description==null) 
            	description = "";
    		Integer idFunct = obj.getId();
    		Integer idType = obj.getBiObjectTypeID();
    		String codeType = obj.getBiObjectTypeCode();
    		bean.setAttribute("id", idFunct);
            bean.setAttribute("name", name);
            bean.setAttribute("label", label);
            bean.setAttribute("description", description);
    	    bean.setAttribute("path", path);
    	    bean.setAttribute("idType", idType);
    	    bean.setAttribute("codeType", codeType);
    	    bean.setAttribute("state", obj.getStateCode());
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "TreeObjectsDAO", 
					            "getInformationOlap", "Cannot recover detail information from database", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
    	return bean;
	}
	
	/**
	 * Gets DATAMART Object information starting from the biobject cms path
	 * 
	 * @param path The biobject cms path
	 * @return a SourceBean containing all the biobject datamart information
	 * @throws EMFUserError If any exception occurs
	 */
	private SourceBean getInformationDatamart(String path) throws EMFUserError {
		SourceBean bean = null;
		try {
        	BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForTree(path);
        	String label = obj.getLabel();
        	String name = obj.getName();
        	bean = new SourceBean("Datamart");
    		String description = obj.getDescription();
    		if(description==null) 
            	description = "";
    		Integer idFunct = obj.getId();
    		Integer idType = obj.getBiObjectTypeID();
    		String codeType = obj.getBiObjectTypeCode();
    		bean.setAttribute("id", idFunct);
            bean.setAttribute("name", name);
            bean.setAttribute("label", label);
            bean.setAttribute("description", description);
    	    bean.setAttribute("path", path);
    	    bean.setAttribute("idType", idType);
    	    bean.setAttribute("codeType", codeType);
    	    bean.setAttribute("state", obj.getStateCode());
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
								"TreeObjectsDAO", 
					            "getInformationDatamart", 
					            "Cannot recover detail information from database", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
    	return bean;
	}
	
	
	
	/**
	 * Gets DASHBOARD Object information starting from the biobject cms path
	 * 
	 * @param path The biobject cms path
	 * @return a SourceBean containing all the biobject dashboard information
	 * @throws EMFUserError If any exception occurs
	 */
	private SourceBean getInformationDash(String path) throws EMFUserError {
		SourceBean bean = null;
		try {
        	BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForTree(path);
        	String label = obj.getLabel();
        	String name = obj.getName();
        	bean = new SourceBean("Dash");
    		String description = obj.getDescription();
    		if(description==null) 
            	description = "";
    		Integer idFunct = obj.getId();
    		Integer idType = obj.getBiObjectTypeID();
    		String codeType = obj.getBiObjectTypeCode();
    		bean.setAttribute("id", idFunct);
            bean.setAttribute("name", name);
            bean.setAttribute("label", label);
            bean.setAttribute("description", description);
    	    bean.setAttribute("path", path);
    	    bean.setAttribute("idType", idType);
    	    bean.setAttribute("codeType", codeType);
    	    bean.setAttribute("state", obj.getStateCode());
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
								"TreeObjectsDAO", 
					            "getInformationDash", 
					            "Cannot recover detail information from database", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
    	return bean;
	}
	
	
	
	
	
	
	/**
	 * Taken as input the list of roles, creates a string contaning all roles names.
	 * 
	 * @param roles The list of roles
	 * @return A string containing all roles names
	 */
	private String rolesString(Role[] roles) {
		String rolesStr = ""; 
		for(int i=0; i<roles.length; i++) {
			rolesStr = rolesStr + roles[i].getName();
			if(i != (roles.length-1)) {
				rolesStr = rolesStr + "---";
			}
		}
		return rolesStr;
	}
	
	
}