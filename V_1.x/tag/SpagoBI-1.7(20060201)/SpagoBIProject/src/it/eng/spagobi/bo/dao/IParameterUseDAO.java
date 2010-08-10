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
import it.eng.spagobi.bo.ParameterUse;

import java.util.List;


/**
 * Defines the interfaces for all methods needed to insert, 
 * modify and deleting a parameter use mode.
 * 
 * @author zoppello
 */
public interface IParameterUseDAO {
	
	/**
	 * Loads all detail information for a parameter use mode identified by 
	 * its <code>useID</code>. All these information, achived by a query 
	 * to the DB, are stored into a<code>ParameterUse</code> object, which is
	 * returned.
	 * 
	 * @param useID The id for the parameter use mode to load
	 * @return A <code>ParameterUse</code> object containing all loaded information
	 * @throws EMFUserError EMFUserError If an Exception occurred
	 */
	public ParameterUse loadByUseID(Integer useID) throws EMFUserError;
	
	/*
	 * Loads a parameter use detail from its <code>id</code> and <code>lovId</code>.
	 * 
	 * @param parameterID The parameter use id (parId)
	 * @param listOfValuesID The parameter use lovId
	 * @return A <code>ParameterUse</code> object storing all information
	 * @throws EMFUserError If an Exception occurs.
	
	public ParameterUse loadByParameterIDandListOfValuesID(Integer parameterID, Integer listOfValuesID) throws EMFUserError;
	*/
	/**
	 * Given at input a <code>ParameterUse</code> objects, asks for all possible Checks 
	 * associated whith it and fills the <code>AssociatedChecks</code> object's list. 
	 * 
	 * @param  aParameterUse The <code>ParameterUse</code> object to fill the Checks list in
	 * @throws EMFUserError if an Exception occurred.
	 */
	public void fillAssociatedChecksForParUse(ParameterUse aParameterUse) throws EMFUserError;
	
	/**
	 * Given at input a <code>ParameterUse</code> objects, asks for all possible Roles
	 * associated whith it and fills the <code>ListRoles</code> object's list. 
	 * @param aParameterUse
	 * @throws EMFUserError
	 */
	public void fillRolesForParUse(ParameterUse aParameterUse) throws EMFUserError;
	
	/**
	 * Implements the query to modify a parameter use mode. All information needed 
	 * is stored into the input <code>ParameterUse</code> object.
	 * 
	 * @param aParameterUse	The object containing all modify information
	 * @throws EMFUserError	If an Exception occurred
	 */
	public void modifyParameterUse(ParameterUse aParameterUse) throws EMFUserError;

	/**
	 * Implements the query to insert a parameter use mode. All information needed
	 * is stored into the input <code>ParameterUse</code> object.
	 * 
	 * @param aParameterUse	The object containing all insert information
	 * @throws EMFUserError	If an Exception occurred
	 */
	public void insertParameterUse(ParameterUse aParameterUse) throws EMFUserError;

	/**
	 * Implements the query to erase a ParameterUse mode. All information needed is stored 
	 * into the input <code>ParameterUse</code> object.
	 * 
	 * @param aParameterUse	The object containing all erase information
	 * @throws EMFUserError	If an Exception occurred
	 */
	public void eraseParameterUse(ParameterUse aParameterUse) throws EMFUserError;
	
	/**
	 * Controls if a parameter has some use modes associated or not. It is useful 
	 * because a parameter can be deleted only if it hasn't any use mode associated.
	 * 
	 * @param parId The parameter id
	 * @return	True if the parameter has one or more modes associated; 
	 * 			false if it hasn't any
	 * @throws EMFUserError	If an Exception occurred
	 */
	public boolean hasParUseModes (String parId) throws EMFUserError;
	
	
	
	public List loadParametersUseByParId(Integer parId) throws EMFUserError;
	
	public void eraseParameterUseByParId(Integer parId) throws EMFUserError;
	
}