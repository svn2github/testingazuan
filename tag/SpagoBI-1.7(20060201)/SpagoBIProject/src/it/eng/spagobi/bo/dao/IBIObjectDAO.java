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
	 * <code>path</code> and its <code>role</code>. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param path	The BI object path
	 * @param role	The BI object role
	 * @return The BIobject execution information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public BIObject loadBIObjectForExecutionByPathAndRole(String path, String role)
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
	public  BIObject loadBIObjectForDetail(Integer biObjectID) throws EMFUserError;

	
	/**
	 * Loads all  detail information  for a BI Object identified by its 
	 * <code>path</code> identifier string. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param  path  the BI object path string
	 * @return The BI object detail information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public  BIObject loadBIObjectForDetail(String path) throws EMFUserError;
	
	/**
	 * Loads all  tree information  for a BI Object identified by its 
	 * <code>path</code> identifier string. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObject</code> object, 
	 * which is returned.
	 * 
	 * @param  path  the BI object path string
	 * @return The BI object tree information, stored into a <code>BIObject</code>
	 * @throws EMFUserError If an Exception occurs
	 */
	public BIObject loadBIObjectForTree(String path) throws EMFUserError;
	
	
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
	 * Implements the query to erase a BI Object. All information needed is stored 
	 * into the input <code>BIObject</code> object.
	 * 
	 * @param obj The object containing all delete information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseBIObject(BIObject obj) throws EMFUserError;

	/**
	 * Given a <code>BIObject</code> at input, fills it with template information.
	 * 
	 * @param obj The object to fill template information in.
	 */
	public void fillBIObjectTemplate(BIObject obj);
	
	/**
	 * Given the path String for a report and th User profile, gets the corret roles for 
	 * execution.
	 * 
	 * @param pathReport	The String path for the report
	 * @param profile	The user profile
	 * @return	The list of correct roles for execution
	 * @throws EMFUserError	If an Exception occurred
	 */
	public List getCorrectRolesForExecution(String pathReport, IEngUserProfile profile) throws EMFUserError;
	
	/**
	 * Gets the correct roles for Report execution, given only the Report's path.
	 * 
	 * @param pathReport	The String path for the report
	 * @return	The list of correct roles for execution
	 * @throws EMFUserError	If an Exception occurred
	 */
	public List getCorrectRolesForExecution(String pathReport) throws EMFUserError;
	
}