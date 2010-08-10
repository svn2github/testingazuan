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

package it.eng.spagobi.bo.dao;

import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.exceptions.OperationExecutionException;

import java.util.ArrayList;
import java.util.List;
/**
 * Interface providing some CMS functionalities methods. 
 * 
 * @author sulis
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IFunctionalityCMSDAO{
	/**
	 * Given a CMS path, ercovers all its children, putting them 
	 * into an Array List, which is returned. 
	 * 
	 * @param parentPath the CMS path
	 * @return the array list containing all childrens
	 * 
	 */
	public ArrayList recoverChilds(String parentPath) throws BuildOperationException, OperationExecutionException;
	
}