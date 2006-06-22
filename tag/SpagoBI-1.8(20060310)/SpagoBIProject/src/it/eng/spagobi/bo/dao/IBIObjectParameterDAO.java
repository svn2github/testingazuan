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
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.metadata.SbiObjPar;

/**
 * Defines the interfaces for all methods needed to insert, modify and deleting a
 * BI Object Parameter.
 * 
 * @author Zoppello
 *
 */
public interface IBIObjectParameterDAO {
	
	/**
	 * Loads all detail information for a BI Object Parameter identified by its 
	 * <code>biObjectId</code> and by its <code>parameterID</code>. All these information,
	 * achived by a query to the DB, are stored into a <code>BIObjectParameter</code> object, 
	 * which is returned.
	 * 
	 * @param biObjectID The id for the BI object parameter to load
	 * @param parameterID The parameter-id for the BI object parameter to load
	 * @return	A <code>BIObjectParameter</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	
	public BIObjectParameter loadBIObjectParameterForDetail(Integer biObjectID, Integer parameterID) throws EMFUserError;

	

	/**
	 * Implements the query to modify a BI Object parameter. All information needed is stored 
	 * into the input <code>BIObjectParameter</code> object.
	 * 
	 * @param aBIObjectParameter The object containing all modify information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void modifyBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError;

	/**
	 * Implements the query to insert a BI Object Parameter. All information needed is stored 
	 * into the input <code>BIObjectParameter</code> object.
	 * 
	 * @param aBIObjectParameter The object containing all insert information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError;

	/**
	 * Implements the query to erase a BIObjectParameter. All information needed is stored 
	 * into the input <code>aBIObjectParameter</code> object.
	 * 
	 * @param aBIObjectParameter The object containing all delete information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError;

	/**
	 * Controls if a BI object parameter has one or more parameters associated. It is useful 
	 * because a parameter cannot be erased if it has some BIObjects parameters associated
	 * 
	 * @param parId The BI object Parameter id
	 * @return True if there are parameters associaated, else false
	 * @throws EMFUserError If an Exception occurred
	 */
	public boolean hasObjParameters(String parId) throws EMFUserError;
	
	/**
	 * Returns the list of all BIObject parameters associated to a <code>BIObject</code>,
	 * known its <code>biObjectID>/code>
	 * 
	 * 
	 * @param biObjectID The input BI object id code
	 * @return The list of all BI Object Parameters associated
	 * @throws EMFUserError If any exception occurred
	 */
	public List loadBIObjectParametersById(Integer biObjectID) throws EMFUserError ;

}