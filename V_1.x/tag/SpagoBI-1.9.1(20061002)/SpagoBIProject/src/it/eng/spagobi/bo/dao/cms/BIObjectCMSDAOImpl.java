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
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

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
	public InputStream getTemplate(String path) {
		InputStream isTemp = null;
		try {
			GetOperation getOp = new GetOperation();
			getOp.setPath(path + "/template");
			getOp.setRetriveContentInformation("true");
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			isTemp = cmsnode.getContent();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getTemplate", "Cannot retrive object template", e);
		} finally {	}
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
			controlForSubObjectsNode(pathParent);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveSubObject", "Error while contolling subobjects node", e);
		}
		
		try {
			SetOperation setOp = new SetOperation();
	        setOp.setContent(new ByteArrayInputStream(content));
	        setOp.setType(SetOperation.TYPE_CONTENT);
	        setOp.setPath(pathParent + "/subobjects/" + name);
	        setOp.setEraseOldProperties(false);
	        List properties = new ArrayList();
	        String[] ownerPropValues = new String[] { userId };
	        String[] visibilityPropValues = new String[] { visibilityStr };
	        String[] namePropValues = new String[] { name };
	        String[] descrPropValues = new String[] { description };
	        CmsProperty propname = new CmsProperty("name", namePropValues);
	        CmsProperty propvis = new CmsProperty("public", visibilityPropValues);
	        CmsProperty propown = new CmsProperty("owner", ownerPropValues);
	        CmsProperty propdesc = new CmsProperty("description", descrPropValues);
	        properties.add(propname);
	        properties.add(propvis);
	        properties.add(propown);
	        properties.add(propdesc);
	        setOp.setProperties(properties);
	        CmsManager manager = new CmsManager();
			manager.execSetOperation(setOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveSubObject", "cannot save object with name " + name +
					            "for user " + userId, e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {	}
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
		try{
			GetOperation getOp = new GetOperation();
			getOp.setPath(pathParent + "/subobjects");
			getOp.setRetriveContentInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("true");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			// if the sourceBean is null return the empty list
			if(cmsnode == null)
				return subObjects;
			if(cmsnode.getChilds() == null)
				return subObjects;
			List childs = cmsnode.getChilds();
			Iterator iterChilds = childs.iterator();
			while(iterChilds.hasNext()) {
				CmsNode child = (CmsNode)iterChilds.next();
				String pathChild = child.getPath();
				getOp.setPath(pathChild);
			    getOp.setRetriveContentInformation("false");
				getOp.setRetrivePropertiesInformation("true");
				getOp.setRetriveVersionsInformation("false");
				getOp.setRetriveChildsInformation("false");
				CmsNode cmsnodechild = manager.execGetOperation(getOp);
				List properties = cmsnodechild.getProperties();
				Iterator iterProps = properties.iterator();
				String owner = "";
				String publicVisStr = "";
				String name = "";
				String description = "";
				while(iterProps.hasNext()) {
					CmsProperty prop = (CmsProperty)iterProps.next();
					String nameprop = prop.getName();
					if(nameprop.equalsIgnoreCase("owner")) {
						owner = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("public")) {
						publicVisStr = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("name")) {
						name = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("description")) {
						description = prop.getStringValues()[0];
					}
				}
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
		} finally {}
		return subObjects;
	}

	
	
	/**
	 * Gets the inputStream of the subObject content
	 * @param pathParent cms path of the parent object
	 * @param name name of the subObject
	 * @return InputStream of the subObject content
	 * 
	 */
	public InputStream getSubObject(String pathParent, String name) {
		InputStream isTemp = null;
		try {
			GetOperation getOp = new GetOperation();
			getOp.setPath(pathParent + "/subobjects/" + name);
			getOp.setRetriveContentInformation("true");
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			isTemp = cmsnode.getContent();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getSubObject", "Cannot retrive subobject template", e);
		} finally {}
		return isTemp;
	}

	
	/**
	 * Delete a subObject
	 * @param pathParent path of the parent object
	 * @param name name of the subObject
	 */
	public void deleteSubObject(String pathParent, String name) throws EMFUserError {
		
		String pathTodel = pathParent + "/subobjects/" + name;
		try {
			DeleteOperation delOp = new DeleteOperation();
			delOp.setPath(pathTodel);
			CmsManager manager = new CmsManager();
			manager.execDeleteOperation(delOp);
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
	 * @param pathParent cms path of the parent biobject
	 * @throws Exception
	 */
	private void controlForSubObjectsNode(String pathParent) throws Exception {
		try {
			GetOperation getOp = new GetOperation();
			getOp.setPath(pathParent + "/subobjects");
			getOp.setRetriveContentInformation("true");
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
            CmsManager manager = new CmsManager();
            CmsNode cmsnode = manager.execGetOperation(getOp);
			if((cmsnode==null) || (cmsnode.getName()==null) || cmsnode.getName().trim().equals("") ) {
				SetOperation setOp = new SetOperation();
				setOp.setEraseOldProperties(false);
		        setOp.setType(SetOperation.TYPE_CONTAINER);
		        setOp.setPath(pathParent + "/subobjects");
		        manager.execSetOperation(setOp);
			}
		} catch(Exception e) {
			throw e;
		} finally {	}
	}
	
	
	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#fillBIObjectTemplate(it.eng.spagobi.bo.BIObject)
	 */
	public void fillBIObjectTemplate(BIObject obj)  {
		try{
			CmsManager manager = new CmsManager();
			GetOperation getOp = new GetOperation(); 
			ConfigSingleton config = ConfigSingleton.getInstance();
			SourceBean biobjectsPathSB = (SourceBean) config.getAttribute(SpagoBIConstants.CMS_BIOBJECTS_PATH);
			String biobjectsPath = (String) biobjectsPathSB.getAttribute("path");
			String path = biobjectsPath + "/" + obj.getUuid() + "/template";
			getOp.setPath(path);
			getOp.setRetriveContentInformation("true");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("true");
			getOp.setRetriveChildsInformation("true");
            CmsNode cmsnode = manager.execGetOperation(getOp);			
            List properties = cmsnode.getProperties();
            String fileName = "";
            Iterator iterprop = properties.iterator();
            while(iterprop.hasNext()) {
            	CmsProperty prop = (CmsProperty)iterprop.next();
            	if(prop.getName().equalsIgnoreCase("fileName"))
            		fileName = prop.getStringValues()[0];
            }
			InputStream instr = cmsnode.getContent();
			byte[] content = GeneralUtilities.getByteArrayFromInputStream(instr);
            UploadedFile template = new UploadedFile();
	        template.setFileContent(content);
            template.setFileName(fileName);
            obj.setTemplate(template);
		} catch (Exception e) {
			SpagoBITracer.major("BIObjectModule", 
								"BIObjectCMSDAOImpl", 
								"fillBIObjectTemplate", 
								"Cannot fill BIObject Template",e);
		}

	}
	
	
	
	/**
	 * Gets the list of all the subObjects which belongs to a biobject. The list contains 
	 * BIObject.SubObjectDetail objects
	 * 
	 * @param pathParent cms path of the father biobject
	 * @return List of BIObject.SubObjectDetail objects each one containing the information
	 * about a subObject of the biObject
	 */
	public List getSubObjects(String pathParent) {
		List subObjects = new ArrayList();
		try{
			GetOperation getOp = new GetOperation();
			getOp.setPath(pathParent + "/subobjects");
			getOp.setRetriveContentInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("true");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			if(cmsnode == null)
				return subObjects;
			if(cmsnode.getChilds() == null)
				return subObjects;
			List childs = cmsnode.getChilds();
			Iterator iterChilds = childs.iterator();
			while(iterChilds.hasNext()) {
				CmsNode child = (CmsNode)iterChilds.next();
				String pathChild = child.getPath();
				getOp.setPath(pathChild);
			    getOp.setRetriveContentInformation("false");
				getOp.setRetrivePropertiesInformation("true");
				getOp.setRetriveVersionsInformation("false");
				getOp.setRetriveChildsInformation("false");
				CmsNode cmsnodechild = manager.execGetOperation(getOp);
				List properties = cmsnodechild.getProperties();
				Iterator iterProps = properties.iterator();
				String owner = "";
				String publicVisStr = "";
				String name = "";
				String description = "";
				while(iterProps.hasNext()) {
					CmsProperty prop = (CmsProperty)iterProps.next();
					String nameprop = prop.getName();
					if(nameprop.equalsIgnoreCase("owner")) {
						owner = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("public")) {
						publicVisStr = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("name")) {
						name = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("description")) {
						description = prop.getStringValues()[0];
					}
				}
				boolean publicVis = false;
				if(publicVisStr.equalsIgnoreCase("true"))
					publicVis = true;
				BIObject biobj = new BIObject();
				BIObject.SubObjectDetail subDet = biobj.new SubObjectDetail(name, pathChild, owner, description, publicVis );
				subObjects.add(subDet);
			}
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getSubObjects", "Cannot retrive subobjects List", e);
		} finally {}
		return subObjects;
	}



	/**
	 * Save Notes for a specific execution of the biobject
	 * @param biobjPath path of the biobject executed
	 * @param execIdentif identifier of the execution
	 * @param notes notes to save
	 */
	public void saveExecutionNotes(String biobjPath, String execIdentif, String notes) throws Exception {
		try{
			CmsManager manager = new CmsManager();
			// check if notes node exists
			GetOperation getOp = new GetOperation(biobjPath+"/notes", false, false, false, false);
			CmsNode cmsnode = manager.execGetOperation(getOp);
			// if notes node doesn't exist create it
			if(cmsnode==null) {
				SetOperation setOp = new SetOperation();
		        setOp.setType(SetOperation.TYPE_CONTAINER);
		        setOp.setPath(biobjPath+"/notes");
		        manager.execSetOperation(setOp);
			}
		    // store notes
			SetOperation setOp = new SetOperation();
	        setOp.setContent(new ByteArrayInputStream(notes.getBytes()));
	        setOp.setType(SetOperation.TYPE_CONTENT);
	        setOp.setPath(biobjPath+"/notes/" + execIdentif);
			manager.execSetOperation(setOp);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "saveExecutionNotes", "cannot save notes for biobj path " + biobjPath, e);
			throw e;
		}
	}
	


	/**
	 * Get Notes for a specific execution of the biobject
	 * @param biobjPath path of the biobject executed
	 * @param execIdentif identifier of the execution
	 * @return String notes saved
	 */
	public String getExecutionNotes(String biobjPath, String execIdentif) throws Exception {
		String notes = " ";
		try{
			CmsManager manager = new CmsManager();
			GetOperation getOp = new GetOperation();
			getOp.setPath(biobjPath+"/notes/" +execIdentif);
			getOp.setRetriveContentInformation("true");
			CmsNode cmsnode = manager.execGetOperation(getOp);
			// if notes node doesn't exist create it
			if(cmsnode!=null) {
				InputStream notesIS = cmsnode.getContent();
				byte[] notesByts = GeneralUtilities.getByteArrayFromInputStream(notesIS);
				notes = new String(notesByts);
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getExecutionNotes", "cannot recover notes for biobj " +
					           	"path " + biobjPath + " and exec identif " + execIdentif, e);
		}
		return notes;
	}


}



