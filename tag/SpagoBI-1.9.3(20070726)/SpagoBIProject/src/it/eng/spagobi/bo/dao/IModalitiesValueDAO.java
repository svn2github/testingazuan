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

import java.util.List;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.ModalitiesValue;

/**
 * Defines the interfaces for all methods needed to insert, modify
 * and deleting a LOV value.
 * 
 * @author Zoppello
 */

public interface IModalitiesValueDAO {
	
	/**
	 * Loads all detail information for an value identified by its <code>modalitiesValueID</code>.
	 * All these information, achived by a query to the DB, are stored into a <code>ModalitiesValue</code> object, which is
	 * returned.
	 * 
	 * @param modalitiesValueID The id for the value to load
	 * @return	A <code>ModalitiesValue</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	
	
	public ModalitiesValue loadModalitiesValueByID(Integer modalitiesValueID) throws EMFUserError;
	
	
	/**
	 * Loads all detail information for a lov by its <code>label</code>.
	 * All these information, achived by a query to the DB, are stored into a <code>ModalitiesValue</code> object, which is
	 * returned.
	 * 
	 * @param label The label for the value to load
	 * @return	A <code>ModalitiesValue</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	
	public ModalitiesValue loadModalitiesValueByLabel(String label) throws EMFUserError;
	
	
	/**
	 * Loads all detail information for all values .
	 * All these information, achived by a query to the DB, are stored into a 
	 * list of <code>ModalitiesValue</code> objects, which is
	 * returned.
	 * 
	 * @return	The list containing all values
	 * @throws EMFUserError If an exception occurs
	 */
	
	public List loadAllModalitiesValue() throws EMFUserError;
	/**
	 * Implements the query to modify a value. All information needed is stored 
	 * into the input <code>ModalitiesValue</code> object.
	 * 
	 * @param  aModalitiesValue The object containing all modify information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void modifyModalitiesValue(ModalitiesValue aModalitiesValue) throws EMFUserError;

	/**
	 * Implements the query to insert a value. All information needed is stored 
	 * into the input <code>ModalitiesValue</code> object.
	 * 
	 * @param aModalitiesValue The object containing all insert information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertModalitiesValue(ModalitiesValue aModalitiesValue) throws EMFUserError;
	
	/**
	 * Implements the query to erase a value. All information needed is stored 
	 * into the input <code>ModalitiesValue</code> object.
	 * 
	 * @param aModalitiesValue The object containing all delete information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseModalitiesValue(ModalitiesValue aModalitiesValue) throws EMFUserError;
	/**
	 * Select all <code>ModalitiesValue</code> object ordered by code
	 *
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadAllModalitiesValueOrderByCode() throws EMFUserError;
	
	
	/**
	 * Controls if a value in the predefined LOV is associated or not with 
	 * a parameter. It is useful because a Value can be deleted only if it 
	 * hasn't any parameter associated.
	 * 
	 * @param idLov The value  id
	 * @return	True if the value has one or more parameters associated; 
	 * 			false if it hasn't any
	 * @throws EMFUserError	If an Exception occurred
	 */
	public boolean hasParameters (String idLov) throws EMFUserError;
}