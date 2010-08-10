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
	 * @return	A <code>LowFunctionality</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	public LowFunctionality loadLowFunctionalityByID(Integer functionalityID) throws EMFUserError;
	
	/**
	 * Loads all information for a low functionality identified by its 
	 * <code>functionalityPath</code>. All these information, are stored into a
	 * <code>LowFunctionality</code> object, which is
	 * returned.
	 * 
	 * @param functionalityPath The path for the low functionality to load
	 * @return	A <code>LowFunctionality</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	public LowFunctionality loadLowFunctionalityByPath(String functionalityPath) throws EMFUserError;
	
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
	 * Control if the functionality with the given path has childs
	 * 
	 * @param path String path of the functionality
	 */
	public boolean hasChild(String path) throws EMFUserError;
}