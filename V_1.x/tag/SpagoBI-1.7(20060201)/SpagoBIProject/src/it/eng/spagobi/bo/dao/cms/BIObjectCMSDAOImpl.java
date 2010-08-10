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

package it.eng.spagobi.bo.dao.cms;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.cms.exec.OperationExecutor;
import it.eng.spago.cms.exec.OperationExecutorManager;
import it.eng.spago.cms.exec.entities.ElementProperty;
import it.eng.spago.cms.exec.operations.DeleteOperation;
import it.eng.spago.cms.exec.operations.GetOperation;
import it.eng.spago.cms.exec.operations.OperationBuilder;
import it.eng.spago.cms.exec.operations.SetOperation;
import it.eng.spago.cms.exec.results.ElementDescriptor;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Implements all the methods for access the biobject cms contents
 * 
 * @author fiscato
 */


public class BIObjectCMSDAOImpl implements IBIObjectCMSDAO {

	/**
	 * Get the template of the object
	 * 
	 * @param Path cms path of the object
	 * @param profile Profile of the current user
	 * @retun InputStream inpustream of the template 
	 */
	public InputStream getTemplate(String path, IEngUserProfile profile) {
		InputStream isTemp = null;
		CMSConnection connection = null;
		try {
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			GetOperation get = OperationBuilder.buildGetOperation();
			get.setPath(path + "/template");
			get.setRetriveContentInformation("true");
			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
			isTemp = desc.getContent();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getTemplate", "Cannot retrive object template", e);
		} finally {
			if(connection!=null) {
		    	if(!connection.isClose())
		    		connection.close();
		    }
		}
		return isTemp;
	}
	
	
	
    /**
     * Save a subObject 
     * 
     * @param content the byte content of the subobject
     * @param pathParent the cms path of the object father
     * @param name name of the suboject
     * @param description Description of the subobject
     * @param publicVisibility Visibility of the subObject, if true the object is public 
     * if false the object is visibile only to the owner
     * @param profile Profile of the current user useful to get the owner use rof the subobject
     * 
     */
	public void saveSubObject(byte[] content, String pathParent, String name, String description, 
			                  boolean publicVisibility, IEngUserProfile profile) throws EMFUserError {
		Object userIdObj = profile.getUserUniqueIdentifier();
		String userId = userIdObj.toString();
		// if the name is not set throw an error
		if( (name==null) || name.trim().equals("") ) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"saveSubObject", 
		            			"No name set");
		}
		// if the description is null set its value to empty string
		if(description==null) {
			description = "";
		}
		// get the visibility string
		String visibilityStr = "false";
		if(publicVisibility) 
			visibilityStr = "true";
		try {
			controlForSubObjectsNode(pathParent, profile);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveSubObject", "Error while contolling subobjects node", e);
		}
		
		OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
		CMSConnection connection = null;
		try {
	        connection = CMSManager.getInstance().getConnection();
	        SetOperation set = OperationBuilder.buildSetOperation();
	        set = OperationBuilder.buildSetOperation();
	        set.setContent(new ByteArrayInputStream(content));
	        set.setType(SetOperation.TYPE_CONTENT);
	        set.setPath(pathParent + "/subobjects/" + name);
	        String[] ownerPropValues = new String[] { userId };
	        set.setStringProperty("owner", ownerPropValues);
	        String[] visibilityPropValues = new String[] { visibilityStr };
	        set.setStringProperty("public", visibilityPropValues);
	        String[] namePropValues = new String[] { name };
	        set.setStringProperty("name", namePropValues);
	        String[] descrPropValues = new String[] { description };
	        set.setStringProperty("description", descrPropValues);
	        executor.setObject(connection, set, profile, true);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveSubObject", "cannot save object with name " + name +
					            "for user " + userId, e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if(connection!=null) {
		    	if(!connection.isClose())
		    		connection.close();
		    }
		}
	}

	
	
	/**
	 * Gets the list of all the current user accessible subObject. The list contains 
	 * BIObject.SubObjectDetail objects
	 * 
	 * @param profile Profile of the current user
	 * @param pathParent cms path of the father biobject
	 * @return List of BIObject.SubObjectDetail objects each one containing the information
	 * about a subObject of the biObject
	 */
	public List getAccessibleSubObjects(String pathParent, IEngUserProfile profile) {
		List subObjects = new ArrayList();
		String user = (String)profile.getUserUniqueIdentifier();
		CMSConnection connection = null;
		try {
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			GetOperation get = OperationBuilder.buildGetOperation();
			get.setPath(pathParent + "/subobjects");
			get.setRetriveContentInformation("false");
			get.setRetrivePropertiesInformation("false");
			get.setRetriveVersionsInformation("false");
			get.setRetriveChildsInformation("true");
			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
			SourceBean elementSB = desc.getDescriptor();
			// if the sourceBean is null return the empty list
			if(elementSB == null)
				return subObjects;
			List childs = elementSB.getAttributeAsList("CHILDS.CHILD");
			Iterator iterChilds = childs.iterator();
			while(iterChilds.hasNext()) {
				SourceBean childSB = (SourceBean)iterChilds.next();
				String pathChild = (String)childSB.getAttribute("PATH");
			    get.setPath(pathChild);
			    get.setRetriveContentInformation("false");
				get.setRetrivePropertiesInformation("true");
				get.setRetriveVersionsInformation("false");
				get.setRetriveChildsInformation("false");
				connection = CMSManager.getInstance().getConnection();
				desc = executor.getObject(connection, get, profile, true);
				ElementProperty ownProp = desc.getNamedElementProperties("owner")[0];
				String owner = ownProp.getStringValues()[0];
				ElementProperty publicProp = desc.getNamedElementProperties("public")[0];
				String publicVisStr = publicProp.getStringValues()[0];
				ElementProperty nameProp = desc.getNamedElementProperties("name")[0];
				String name = nameProp.getStringValues()[0];
				ElementProperty descrProp = desc.getNamedElementProperties("description")[0];
				String description = descrProp.getStringValues()[0];
				
				boolean publicVis = false;
				if(publicVisStr.equalsIgnoreCase("true"))
					publicVis = true;
				if( publicVis || owner.equals(user) ) {
					BIObject biobj = new BIObject();
					BIObject.SubObjectDetail subDet = 
						biobj.new SubObjectDetail(name, pathChild, owner, description, publicVis );
					subObjects.add(subDet);
				} 
			}
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getAccessibleSubObjects", "Cannot retrive subobjects List", e);
		} finally {
			if(connection!=null) {
		    	if(!connection.isClose())
		    		connection.close();
		    }
		}
		return subObjects;
	}

	
	
	/**
	 * Gets the inputStream of the subObject content
	 * 
	 * @param pathParent cms path of the parent object
	 * @param name name of the subObject
	 * @param profile Profile of the current user
	 * @return InputStream of the subObject content
	 * 
	 */
	public InputStream getSubObject(String pathParent, String name, IEngUserProfile profile) {
		// each subobject into the cms file will be store under
		// pathObject/subobjects/nameuser_name 
		// in this manner it's possible that two users give the same name to different queries
		Object userIdObj = profile.getUserUniqueIdentifier();
		String userId = userIdObj.toString();
		InputStream isTemp = null;
		CMSConnection connection = null;
		try {
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			GetOperation get = OperationBuilder.buildGetOperation();
			get.setPath(pathParent + "/subobjects/" + name);
			get.setRetriveContentInformation("true");
			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
			isTemp = desc.getContent();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getSubObject", "Cannot retrive subobject template", e);
		} finally {
			if(connection!=null) {
		    	if(!connection.isClose())
		    		connection.close();
		    }
		}
		
		return isTemp;
	}

	
	/**
	 * Delete a subObject
	 * 
	 * @param pathParent path of the parent object
	 * @param name name of the subObject
	 * @param profile Profile of the user
	 */
	public void deleteSubObject(String pathParent, String name, IEngUserProfile profile) throws EMFUserError {
		
		String pathTodel = pathParent + "/subobjects/" + name;
		OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
		try {
			CMSConnection connection = CMSManager.getInstance().getConnection();
			DeleteOperation del = OperationBuilder.buildDeleteOperation();
			del.setPath(pathTodel);
			executor.deleteObject(connection, del, profile, true);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		                        "deleteSubObject", "Cannot delete subobject", e);
			Vector params = new Vector();
			params.add(name);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 7001, params);
		}
	}
	
	
	
	
	/**
	 * Control if the PATH_PARENT/subobjects node of the biobjectt exists and if not it will create it
	 * 
	 * @param pathParent cms path of the parent biobject
	 * @param profile Profile of the current user
	 * @throws Exception
	 */
	private void controlForSubObjectsNode(String pathParent, IEngUserProfile profile) throws Exception {
		OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
		CMSConnection connection = null;
		try {
			connection = CMSManager.getInstance().getConnection();
			GetOperation get = OperationBuilder.buildGetOperation();
			get.setPath(pathParent + "/subobjects");
			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
			connection = CMSManager.getInstance().getConnection();
			if(desc.getDescriptor()==null) {
				SetOperation set = OperationBuilder.buildSetOperation();
		        set.setType(SetOperation.TYPE_CONTAINER);
		        set.setPath(pathParent + "/subobjects");
		        executor.setObject(connection, set, profile, true);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			if(connection!=null) {
		    	if(!connection.isClose())
		    		connection.close();
		    }
		}
	}
	
}



