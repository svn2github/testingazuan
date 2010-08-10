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
import it.eng.spagobi.bo.LowFunctionality;

import java.util.List;
import java.util.Set;


/**
 * Defines  the interfaces for all methods needed to insert, modify and deleting a low functionality.
 * 
 * @author Zoppello
 */


public interface ILowFunctionalityDAO {
	
	/**
	 * Loads all information for a low functionality identified by its 
	 * <code>functionalityID</code>. All these information, are stored into a
	 * <code>LowFunctionality</code> object, which is
	 * returned.
	 * 
	 * @param functionalityID The id for the low functionality to load
	 * @param recoverBIObjects If true the <code>LowFunctionality</code> at output will have the 
	 * list of contained <code>BIObject</code> objects
	 * @return	A <code>LowFunctionality</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	public LowFunctionality loadLowFunctionalityByID(Integer functionalityID, boolean recoverBIObjects) throws EMFUserError;
	
	/**
	 * Loads all information for a low functionality identified by its 
	 * <code>functionalityPath</code>. All these information, are stored into a
	 * <code>LowFunctionality</code> object, which is
	 * returned.
	 * 
	 * @param functionalityPath The path for the low functionality to load
	 * @param recoverBIObjects If true the <code>LowFunctionality</code> at output will have the 
	 * list of contained <code>BIObject</code> objects
	 * @return	A <code>LowFunctionality</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	public LowFunctionality loadLowFunctionalityByPath(String functionalityPath, boolean recoverBIObjects) throws EMFUserError;
	
	/**
	 * Implements the query to modify a low functionality. All information needed is stored 
	 * into the input <code>LowFunctionality</code> object.
	 * 
	 * @param aLowFunctionality The object containing all modify information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void modifyLowFunctionality(LowFunctionality aLowFunctionality) throws EMFUserError;
	
	/**
	 * Implements the query to insert a low functionality. All information needed is stored 
	 * into the input <code>LowFunctionality</code> object.
	 * 
	 * @param aLowFunctionality The object containing all insert information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertLowFunctionality(LowFunctionality aLowFunctionality, IEngUserProfile profile) throws EMFUserError;
	
	/**
	 * Implements the query to erase a low functionality. All information needed is stored 
	 * into the input <code>LowFunctionality</code> object.
	 * 
	 * @param aLowFunctionality The object containing all erase information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseLowFunctionality(LowFunctionality aLowFunctionality, IEngUserProfile profile) throws EMFUserError;
	
	/**
	 * Control if exist a functionality with the given code
	 * @param code  The code of the functionality
	 * @return The functionality ID
	 */
	public Integer existByCode(String code) throws EMFUserError;
	
	/**
	 * Control if the functionality with the given id has childs
	 * 
	 * @param id Integer id of the functionality
	 */
	public boolean hasChild(Integer id) throws EMFUserError;
	
	public void deleteInconsistentRoles (Set set) throws EMFUserError;
	
	/**
	 * Loads all the functionalities
	 * @param recoverBIObjects If true each <code>LowFunctionality</code> at output will have the 
	 * list of contained <code>BIObject</code> objects
	 * @return the list of functionalities
	 * @throws EMFUserError
	 */
	public List loadAllLowFunctionalities(boolean recoverBIObjects) throws EMFUserError;
	
	/**
	 * Loads all the sub functionalities of the given initial path 
	 * @param initialPath The String representing the initial path
	 * @param recoverBIObjects If true each <code>LowFunctionality</code> at output will have the 
	 * list of contained <code>BIObject</code> objects
	 * @return the list of functionalities
	 * @throws EMFUserError
	 */
	public List loadSubLowFunctionalities(String initialPath, boolean recoverBIObjects) throws EMFUserError;
	
	/**
	 * Loads all the child functionalities of the given parent functionality 
	 * @param parentId The Integer representing the parent id
	 * @param recoverBIObjects If true each <code>LowFunctionality</code> at output will have the 
	 * list of contained <code>BIObject</code> objects
	 * @return the list of functionalities
	 * @throws EMFUserError
	 */
	public List loadChildFunctionalities(Integer parentId, boolean recoverBIObjects) throws EMFUserError;
	
}