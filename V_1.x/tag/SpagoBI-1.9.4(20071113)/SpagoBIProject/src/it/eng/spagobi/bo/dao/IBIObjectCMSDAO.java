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
/*
 * Created on 13-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.bo.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Defines all the methods needed for access contents of BIObject into CMS Repository 
 * 
 * @author Fiscato
 */
public interface IBIObjectCMSDAO {

	
	/**
	 * Save Notes for a specific execution of the biobject
	 * @param biobjPath path of the biobject executed
	 * @param execIdentif identifier of the execution
	 * @param notes notes to save
	 */
	public void saveExecutionNotes(String biobjPath, String execIdentif, String notes) throws Exception;
	
	/**
	 * Get Notes for a specific execution of the biobject
	 * @param biobjPath path of the biobject executed
	 * @param execIdentif identifier of the execution
	 * @return String notes saved
	 */
	public String getExecutionNotes(String biobjPath, String execIdentif) throws Exception;
	
	
	/**
	 * Get the template of the object
	 * 
	 * @param path Cms path of the object
	 * @param profile User Profile
	 * @return InputStream of the template contents
	 */ 
	public InputStream getTemplate(String path); 
	
	/**
	 * Save a subObject of the object
	 * 
	 * @param content byte array containing the content of the subobject
	 * @param pathParent the cms path of the parent
	 * @param name the name of the new subobject
	 * @param description the description of the new subobject
	 * @param publiicVisibility the public or private visibility of the subobject
	 * @param profile the profile of the user
	 */
	public void saveSubObject(byte[] content, String pathParent, String name, 
			                  String description, boolean publicVisibility,
			                  IEngUserProfile profile) throws EMFUserError;
	
	/**
	 * Gets the detail of all the subobjects accessible to the user
	 * 
	 * @param pathParent Cms path of the object
	 * @param profile Profile of the user
	 * @return List of BIObject.SubObjectDetail objects 
	 */
	public List getAccessibleSubObjects(String pathParent, IEngUserProfile profile);
	
	/**
	 * Gets the InputStream of the subobjects content
	 * 
	 * @param pathParent cms path of the object parent
	 * @param name name of the subobject
	 * @return InputStream of the subobject content
	 */
	public InputStream getSubObject(String pathParent, String name);
	
	
	/**
	 * Delete a subObject
	 * @param pathParent path of the parent object
	 * @param name name of the subObject
	 */
	public void deleteSubObject(String pathParent, String name) throws EMFUserError;
	
	
	
	/**
	 * Given a <code>BIObject</code> at input, fills it with template information.
	 * 
	 * @param obj The object to fill template information in.
	 */
	public void fillBIObjectTemplate(BIObject obj);
	
	
	
	/**
	 * Gets the detail of all the biobject subobjects 
	 * 
	 * @param pathParent Cms path of the object
	 * @return List of BIObject.SubObjectDetail objects 
	 */
	public List getSubObjects(String pathParent);
	
	/**
	 * Save a subObject of the object
	 * 
	 * @param content: byte array containing the content of the template
	 * @param path: the cms path of the document
	 * @param templateName: the name of the template
	 */
	public void saveTemplate(byte[] content, String path, String templateName) throws EMFUserError;
	
	
	/**
	 * Save a snapshot of the object
	 * 
	 * @param content byte array containing the content of the snapshot
	 * @param pathParent the cms path of the parent
	 * @param name the name of the new subobject
	 * @param description the description of the new subobject
	*/
	public void saveSnapshot(byte[] content, String pathParent, String name, 
			                 String description) throws EMFUserError;
	
	/**
	 * Gets the list of the snapshot details that are children of a biobject  
	 * @param pathParent Cms path of the biobject
	 * @return List of BIObject.BIObjectSnapshot objects 
	 */
	public List getSnapshots(String pathParent);
	
	/**
	 * Delete a snapshot
	 * @param snapshotPath path of the snapshot
	 * @param name name of the snapshot
	 */
	public void deleteSnapshot(String snapshotPath) throws EMFUserError;
	
	/**
	 * Exports a document node
	 * @param obj The BIObject document to be exported: its node is supposed to be existing
	 * @param destFolder The folder where document will be serialized
	 * @param exportSubObjects Flag indicating if the subobjects must be exported
	 * @param exportSnapshots Flag indicating if the snapshots must be exported
	 */
	public void exportDocument(BIObject obj, String destFolder, boolean exportSubObjects, boolean exportSnapshots) 
		throws EMFUserError;
	
	/**
	 * Imports a document
	 * @param sourceFolder The folder where document will be deserialized from
	 * @retrun the String representing the new document base node path in the cms
	 */
	public String importDocument(String sourceFolder) throws EMFUserError;
	
}