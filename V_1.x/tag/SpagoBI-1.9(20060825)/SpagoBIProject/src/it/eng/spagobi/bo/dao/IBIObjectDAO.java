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

import java.util.List;

/**
 * Defines the interfaces for all methods needed to insert, modify and deleting 
 * a BI object.
 * 
 * @author Zoppello
 */
public interface IBIObjectDAO {

	/**
	 * Loads all  information for the execution of a BI Object identified by its 
	 * <code>id</code> and its <code>role</code>. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param id	The BI object id
	 * @param role	The BI object role
	 * @return The BIobject execution information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public BIObject loadBIObjectForExecutionByIdAndRole(Integer id, String role)
			throws EMFUserError;
	
	/**
	 * Loads all  detail information  for a BI Object identified by its 
	 * <code>label</code> identifier. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param label The BI object label identifier
	 * @return The BI object detail information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public BIObject loadBIObjectByLabel(String label)
			throws EMFUserError;
	
	/**
	 * Loads all  detail information  for a BI Object identified by its 
	 * <code>biObjectID</code> identifier. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param biObjectID the BI object identifier
	 * @return The BI object detail information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public  BIObject loadBIObjectById(Integer biObjectID) throws EMFUserError;

	
	/**
	 * Loads all  detail information  for a BI Object identified by its 
	 * <code>id</code> identifier integer. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param  id  The Integer representing the BI object id
	 * @return The BI object detail information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public  BIObject loadBIObjectForDetail(Integer id) throws EMFUserError;
	
	/**
	 * Loads all  detail information  for a BI Object identified by its 
	 * <code>path</code> in the cms. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param  id  The Integer representing the BI object id
	 * @return The BI object detail information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public  BIObject loadBIObjectForDetail(String path) throws EMFUserError;
	
	/**
	 * Loads all  tree information  for a BI Object identified by its 
	 * <code>id</code> identifier Integer. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param  id  The Integer representing the BI object id
	 * @return The BI object tree information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public BIObject loadBIObjectForTree(Integer id) throws EMFUserError;
	
	
	/**
	 * Implements the query to modify a BI Object. All information needed is stored 
	 * into the input <code>BIObject</code> object.
	 * 
	 * @param  obj The BIobject containing all modify information
	 * @throws EMFUserError If an Exception occurred
	 */
	
	public void modifyBIObject(BIObject obj) throws EMFUserError;
	
	/**
	 * Implements the query to modify a BI Object, but without updating versioning. 
	 * All information needed is stored into the input <code>BIObject</code> object.
	 * 
	 * @param  obj The BIobject containing all modify information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void modifyBIObjectWithoutVersioning(BIObject obj) throws EMFUserError;

	/**
	 * Implements the query to insert a BIObject. All information needed is stored 
	 * into the input <code>BIObject</code> object.
	 * 
	 * @param obj The object containing all insert information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertBIObject(BIObject obj) throws EMFUserError;

	/**
	 * Deletes a BIObject from a functionality. If the functionality is not specified 
	 * (i.e. idFunct == null), the method deletes the BIObject from all the functionalities.
	 * Then, if the BIObject is no more referenced in any 
	 * functionality, deletes it completely from db and from CMS.
	 * 
	 * @param obj The object containing all delete information
	 * @param idFunct The Integer representing the functionality id
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseBIObject(BIObject obj, Integer idFunct) throws EMFUserError;

	
	/**
	 * Given the id for a report and the user profile, gets the corret roles for 
	 * execution.
	 * 
	 * @param id	The Integer id for the report
	 * @param profile	The user profile
	 * @return	The list of correct roles for execution
	 * @throws EMFUserError	If an Exception occurred
	 */
	public List getCorrectRolesForExecution(Integer id, IEngUserProfile profile) throws EMFUserError;
	
	/**
	 * Gets the correct roles for Report execution, given only the Report's id.
	 * 
	 * @param id	The Integer id for the report
	 * @return	The list of correct roles for execution
	 * @throws EMFUserError	If an Exception occurred
	 */
	public List getCorrectRolesForExecution(Integer id) throws EMFUserError;
	
	/**
	 * Gets the biparameters associated with to a biobject 
	 * @param aBIObject BIObject the biobject to analize
	 * @return List, list of the biparameters associated with the biobject
	 * @throws EMFUserError
	 */
	public List getBIObjectParameters(BIObject aBIObject) throws EMFUserError;
	
	/**
	 * Loads all the BIObjects
	 * @return the list of BIObjects
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadAllBIObjects() throws EMFUserError;
	
	/**
	 * Loads all the BIObjects that belong to sub functionalities of the given functionality path 
	 * @return the list of BIObjects
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadAllBIObjectsFromInitialPath(String initialPath) throws EMFUserError;
	
}
