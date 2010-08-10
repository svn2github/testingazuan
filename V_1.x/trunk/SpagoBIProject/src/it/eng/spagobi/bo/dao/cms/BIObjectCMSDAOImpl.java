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
import it.eng.spago.cms.operations.ExportOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.ImportOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.cms.operations.VersionOperation;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * Implements all the methods for access the biobject cms contents
 * 
 * @author fiscato
 */


public class BIObjectCMSDAOImpl implements IBIObjectCMSDAO {

	protected final String DOCUMENT_FILE_NAME = "document.xml";
	protected final String TEMPLATE_FILE_NAME = "template.xml";
	protected final String SUBOBJECTS_FILE_NAME = "subobjects.xml";
	protected final String SNAPSHOTS_FILE_NAME = "snapshots.xml";
	
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
		
		BIObject.SubObjectDetail targetSubObj = null;
		List subobjects = getSubObjects(pathParent);
		for(Iterator it = subobjects.iterator(); it.hasNext(); ) {
			BIObject.SubObjectDetail subobject = (BIObject.SubObjectDetail)it.next();
			if(subobject.getName().equalsIgnoreCase(name)) {
				targetSubObj = subobject;
				break;
			}
		}
		
		String lastModifcationDate = DateFormat.getDateInstance().format(new Date());
	    String creationDate = (targetSubObj == null)? lastModifcationDate: targetSubObj.getLastModifcationDate();
		
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
	        String[] lastModifcationDateValues = new String[] { lastModifcationDate };
	        String[] creationDateValues = new String[] { creationDate };
	        
	        CmsProperty propname = new CmsProperty("name", namePropValues);
	        CmsProperty propvis = new CmsProperty("public", visibilityPropValues);
	        CmsProperty propown = new CmsProperty("owner", ownerPropValues);
	        CmsProperty propdesc = new CmsProperty("description", descrPropValues);
	        CmsProperty proplastm = new CmsProperty("lastModifcationDate", lastModifcationDateValues);
	        CmsProperty propcdate = new CmsProperty("creationDate", creationDateValues);
	        
	        properties.add(propname);
	        properties.add(propvis);
	        properties.add(propown);
	        properties.add(propdesc);
	        properties.add(proplastm);
	        properties.add(propcdate);
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
				String lastModifcationDate = "";
				String creationDate = "";
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
					if(nameprop.equalsIgnoreCase("lastModifcationDate")) {
						lastModifcationDate = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("creationDate")) {
						creationDate = prop.getStringValues()[0];
					}
				}
				boolean publicVis = false;
				if(publicVisStr.equalsIgnoreCase("true"))
					publicVis = true;
				if( publicVis || owner.equals(user) ) {
					BIObject biobj = new BIObject();
					BIObject.SubObjectDetail subDet = 
						biobj.new SubObjectDetail(name, pathChild, owner, description, 
								lastModifcationDate, creationDate, publicVis );
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
		        setOp.setVersionable(false);
		        manager.execSetOperation(setOp);
			}
		} catch(Exception e) {
			throw e;
		} finally {	}
	}
	
	
	/**
	 * Control if the BIOBJECT_PATH_PARENT/snapshots node exists and create it in case it doesn't exist
	 * @param pathParent cms path of the parent biobject
	 * @throws Exception
	 */
	private void controlForSnapshotsNode(String pathParent) throws Exception {
		try {
			GetOperation getOp = new GetOperation();
			getOp.setPath(pathParent + "/snapshots");
			getOp.setRetriveContentInformation("false");
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
            CmsManager manager = new CmsManager();
            CmsNode cmsnode = manager.execGetOperation(getOp);
			if((cmsnode==null) || (cmsnode.getName()==null) || cmsnode.getName().trim().equals("") ) {
				SetOperation setOp = new SetOperation();
				setOp.setEraseOldProperties(false);
		        setOp.setType(SetOperation.TYPE_CONTAINER);
		        setOp.setPath(pathParent + "/snapshots");
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
            if (cmsnode != null) {
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
            }
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
				String lastModifcationDate = "";
				String creationDate = "";
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
					if(nameprop.equalsIgnoreCase("lastModifcationDate")) {
						lastModifcationDate = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("creationDate")) {
						creationDate = prop.getStringValues()[0];
					}
				}
				boolean publicVis = false;
				if(publicVisStr.equalsIgnoreCase("true"))
					publicVis = true;
				BIObject biobj = new BIObject();
				BIObject.SubObjectDetail subDet = biobj.new SubObjectDetail(name, pathChild, owner, description, 
						lastModifcationDate, creationDate, publicVis );
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

	/**
	 * Save a subObject of the object
	 * 
	 * @param content byte array containing the content of the template
	 * @param path the cms path of the document
	 */
	public void saveTemplate(byte[] content, String path, String templateName) throws EMFUserError {
		try {
			CmsManager manager = new CmsManager();
			GetOperation getOp = new GetOperation();
			getOp.setPath(path);
			getOp.setRetriveContentInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("false");
			CmsNode cmsnode = manager.execGetOperation(getOp);
			if (cmsnode == null) {
				SpagoBITracer.major("SpagoBI", this.getClass().getName(),
			            "saveTemplate", "The document with path " + path + " is not present in cms.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			SetOperation setOp = new SetOperation();
			setOp.setContent(new ByteArrayInputStream(content));
			setOp.setType(SetOperation.TYPE_CONTENT);
			String templatePath = path + "/template";
			setOp.setPath(templatePath);
			setOp.setVersionable(true);
			// define properties list
			List properties = new ArrayList();
			String[] nameFilePropValues = new String[] { templateName };
			String today = new Long(new Date().getTime()).toString();
			String[] datePropValues = new String[] { today };
			CmsProperty propFileName = new CmsProperty("fileName", nameFilePropValues);
			CmsProperty propDateLoad = new CmsProperty("dateLoad", datePropValues);
			properties.add(propFileName);
			properties.add(propDateLoad);
	        setOp.setProperties(properties);
	        // exec operation
			manager.execSetOperation(setOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveTemplate", "cannot save template for document with path " + path, e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}



	public void deleteSnapshot(String snapshotPath) throws EMFUserError {
		String name = snapshotPath.substring(snapshotPath.lastIndexOf("/snapshots/") + 11);
		try {
			DeleteOperation delOp = new DeleteOperation();
			delOp.setPath(snapshotPath);
			CmsManager manager = new CmsManager();
			manager.execDeleteOperation(delOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		                        "deleteSnapshot", "Cannot delete snapshot", e);
			List params = new ArrayList();
			params.add(name);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 7001, params);
		}
	}



	public List getSnapshots(String pathParent) {
		List snapshots = new ArrayList();
		try{
			GetOperation getOp = new GetOperation();
			getOp.setPath(pathParent + "/snapshots");
			getOp.setRetriveContentInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("true");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			if(cmsnode == null)
				return snapshots;
			if(cmsnode.getChilds() == null)
				return snapshots;
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
				String name = "";
				String description = "";
				Date dateCreation = null; 
				while(iterProps.hasNext()) {
					CmsProperty prop = (CmsProperty)iterProps.next();
					String nameprop = prop.getName();
					if(nameprop.equalsIgnoreCase("name")) {
						name = prop.getStringValues()[0];
					}
					if(nameprop.equalsIgnoreCase("description")) {
						description = prop.getStringValues()[0];
					} 
					if(nameprop.equalsIgnoreCase("dateCreation")) {
						String dateCreationStr = prop.getStringValues()[0];
						Long dateCreationLong = new Long(dateCreationStr);
						dateCreation = new Date(dateCreationLong.longValue());
					} 
				}
				BIObject biobj = new BIObject();
				BIObject.BIObjectSnapshot snap = biobj.new BIObjectSnapshot(pathChild, name, description, dateCreation);
				snapshots.add(snap);
			}
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getSnapshots", "Cannot retrive snapshot List", e);
		} finally {}
		return snapshots;
	}



	public void saveSnapshot(byte[] content, String pathParent, String name, String description) throws EMFUserError {
		// if the name is not set throw an error
		if( (name==null) || name.trim().equals("") ) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"saveSnapshot", 
		            			"No name set");
		}
		// if the description is null set its value to empty string
		if(description==null) {
			description = "";
		}
		// check if snapshot node exist and create it in case it doens't exist
		try {
			controlForSnapshotsNode(pathParent);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveSnapshot", "Error while contolling snapshots node", e);
		}
		// save the snapshot
		try {
			UUIDGenerator uuidgen = UUIDGenerator.getInstance();
			UUID uuid = uuidgen.generateTimeBasedUUID();
			String uuidStr = uuid.toString();
			SetOperation setOp = new SetOperation();
	        setOp.setContent(new ByteArrayInputStream(content));
	        setOp.setType(SetOperation.TYPE_CONTENT);
	        setOp.setPath(pathParent + "/snapshots/" + uuidStr);
	        setOp.setEraseOldProperties(false);
	        List properties = new ArrayList();
	        String today = new Long(new Date().getTime()).toString();
	        String[] dateCreationValues = new String[] { today };
	        String[] namePropValues = new String[] { name };
	        String[] descrPropValues = new String[] { description };
	        CmsProperty propname = new CmsProperty("name", namePropValues);
	        CmsProperty propdesc = new CmsProperty("description", descrPropValues);
			CmsProperty propDateCreation = new CmsProperty("dateCreation", dateCreationValues);
	        properties.add(propname);
	        properties.add(propdesc);
	        properties.add(propDateCreation);
	        setOp.setProperties(properties);
	        CmsManager manager = new CmsManager();
			manager.execSetOperation(setOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveSnapshot", "Error while saving snapshot " + name, e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {	}
	}

	public void exportDocument(BIObject obj, String destFolder, boolean exportSubObjects, 
															boolean exportSnapshots) throws EMFUserError {
		try {
			boolean isBooklet = false;
			if (obj.getBiObjectTypeCode().equalsIgnoreCase("BOOKLET")) isBooklet = true;
			File destFolderFile = new File(destFolder);
			destFolderFile.mkdirs();
			
			// exports the document node; if the document is BOOKLET type, all sub nodes are exported
			exportNode(obj.getPath(), destFolder + "/" + DOCUMENT_FILE_NAME, !isBooklet);
			
			if (!isBooklet) {
				// the template is exported
				if (existsNode(obj.getPath() + "/template")) {
					exportNode(obj.getPath() + "/template", destFolder + "/" + TEMPLATE_FILE_NAME, true);
				}
				// if requested and if existing, all subobjects are exported
				if (exportSubObjects && existsNode(obj.getPath() + "/subobjects")) {
					exportNode(obj.getPath() + "/subobjects", destFolder + "/" + SUBOBJECTS_FILE_NAME, false);
				}
				// if requested and if existing, all snapshots are exported
				if (exportSubObjects && existsNode(obj.getPath() + "/snapshots")) {
					exportNode(obj.getPath() + "/snapshots", destFolder + "/" + SNAPSHOTS_FILE_NAME, false);
				}
			}
			
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "exportDocument", "Error while exporting object with label " + obj.getLabel(), e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}

	public String importDocument(String sourceFolder) throws EMFUserError {
		try {
			File documentFile = new File(sourceFolder + "/" + DOCUMENT_FILE_NAME);
			ConfigSingleton config = ConfigSingleton.getInstance();
			SourceBean biobjectsPathSB = (SourceBean) config.getAttribute(SpagoBIConstants.CMS_BIOBJECTS_PATH);
			String biobjectsPath = (String) biobjectsPathSB.getAttribute("path");
			importNode(biobjectsPath, documentFile);
			// the name of the biobjects is the last part of the source folder
			String uuid = sourceFolder.substring(sourceFolder.lastIndexOf(File.separator) + 1);
			String newDocumentBasePath = biobjectsPath + "/" + uuid;
			// imports template if exists
			File templateFile = new File(sourceFolder + "/" + TEMPLATE_FILE_NAME);
			if (templateFile.exists()) importNode(newDocumentBasePath, templateFile);
			// imports subobjects if they exist
			File subObjectsFile = new File(sourceFolder + "/" + SUBOBJECTS_FILE_NAME);
			if (subObjectsFile.exists()) importNode(newDocumentBasePath, subObjectsFile);
			// imports snapshots if they exist
			File snapshotsFile = new File(sourceFolder + "/" + SNAPSHOTS_FILE_NAME);
			if (snapshotsFile.exists()) importNode(newDocumentBasePath, snapshotsFile);
			// version template node
			if (existsNode(newDocumentBasePath + "/template")) {
				versionNode(newDocumentBasePath + "/template", false);
			}
			return newDocumentBasePath;
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "importDocument", "Error while importing object from " + sourceFolder, e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}

	private boolean existsNode (String path) {
		try {
			GetOperation getOp = new GetOperation();
			getOp.setPath(path);
			getOp.setRetriveContentInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("false");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			return cmsnode != null; 
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            "existsNode", "Error while getting node at path " + path, e);
			return false;
		}
	}

	private void exportNode (String nodePath, String destFilePath, boolean noRecurse) throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(destFilePath);
			ExportOperation exportOp = new ExportOperation();
			exportOp.setPath(nodePath);
			exportOp.setOutputStream(fos);
			exportOp.setNoRecurse(noRecurse);
			CmsManager manager = new CmsManager();
			manager.execExportOperation(exportOp);
			fos.flush();
		} finally {
			if (fos != null) fos.close();
		}
	}

	private void importNode (String nodePath, File sourceFile) throws Exception {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(sourceFile);
			ImportOperation importOp = new ImportOperation();
			importOp.setPath(nodePath);
			importOp.setInputStream(fis);
			CmsManager manager = new CmsManager();
			manager.execImportOperation(importOp);
		} finally {
			if (fis != null) fis.close();
		}
	}

	private void versionNode (String path, boolean noRecurse) throws Exception {
		VersionOperation versionOp = new VersionOperation(path, noRecurse);
		CmsManager manager = new CmsManager();
		manager.execVersionOperation(versionOp);
	}
}



