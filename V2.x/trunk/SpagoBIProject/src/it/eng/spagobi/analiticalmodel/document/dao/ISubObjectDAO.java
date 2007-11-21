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
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;

import java.io.InputStream;
import java.util.List;

public interface ISubObjectDAO {

	/**
	 * Save a subObject of the object
	 * @param content byte array containing the content of the subobject
	 * @param idBIObj the id of the biobject parent
	 * @param name the name of the new subobject
	 * @param description the description of the new subobject
	 * @param publiicVisibility the public or private visibility of the subobject
	 * @param profile the profile of the user
	 */
	public void saveSubObject(byte[] content, Integer idBIObj, String name, 
			                  String description, boolean publicVisibility,
			                  IEngUserProfile profile) throws EMFUserError;
	
	/**
	 * Gets the detail of all the subobjects accessible to the user
	 * @param idBIObj the id of the biobject parent
	 * @param profile Profile of the user
	 * @return List of BIObject.SubObjectDetail objects 
	 */
	public List getAccessibleSubObjects(Integer idBIObj, IEngUserProfile profile);
	
	/**
	 * Gets the InputStream of the subobjects content
	 * @param idBIObj the id of the biobject parent
	 * @param name name of the subobject
	 * @return InputStream of the subobject content
	 */
	public InputStream getSubObject(Integer idBIObj, String name);
	
	
	/**
	 * Delete a subObject
	 * @param idBIObj the id of the biobject parent
	 * @param name name of the subObject
	 */
	public void deleteSubObject(Integer idBIObj, String name) throws EMFUserError;
	
	/**
	 * Gets the detail of all the biobject subobjects 
	 * @param idBIObj the id of the biobject parent
	 * @return List of BIObject.SubObjectDetail objects 
	 */
	public List getSubObjects(Integer idBIObj);
	
	
}
