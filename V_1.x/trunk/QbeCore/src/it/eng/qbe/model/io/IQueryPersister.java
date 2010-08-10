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
package it.eng.qbe.model.io;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.List;

/**
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements 
 * logig to load and persist query on a persistent store than can be, JSR 170 repository, Database,
 * File System and so on
 * 
 */
public interface IQueryPersister {

	/**
	 * @param dm: The datamart model
	 * @param wizObject: The Query object to persist
	 */
	public void persist(DataMartModel dm, ISingleDataMartWizardObject wizObject);
	
	/**
	 * @param dm: The datamart model
	 * @return: all the query for datamart dm
	 */
	public List loadAllQueries(DataMartModel dm);
	
	/**
	 * @param dm: The datamart model
	 * @param key: The identifier of the query to retrieve
	 * @return: the query of the datamart identified by key
	 */
	public ISingleDataMartWizardObject load(DataMartModel dm, String key);
}
