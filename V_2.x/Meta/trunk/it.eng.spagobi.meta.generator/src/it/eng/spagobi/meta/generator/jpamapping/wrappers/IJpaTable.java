/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.generator.jpamapping.wrappers;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.AbstractJpaRelationship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaColumn;

import java.util.List;



/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IJpaTable {
	
	/**
	 * 
	 * @return the package of the java class generated from this table
	 */
	String getPackage();
	
	/**
	 * 
	 * @return 	the string containing all the imports needed in order to successfully compile 
	 * 			the java class generated from this table
	 */
	String getImportStatements();
	
	/**
	 * 
	 * @return the name of the class generated from this table (not qualified)
	 */
	String getClassName();
	
	/**
	 * 
	 * @return true if the table have a primary key. false otherwise
	 */
	boolean hasPrimaryKey();	
	
	/**
	 * 
	 * @return true if the table have a composite primary key (a key composed by more then one column). 
	 * 		   false otherwise
	 */
	boolean hasCompositeKey();
	
	/**
	 * 
	 * @return the name of the java class used to map the composite primary key (note: composite primary key
	 * 			are mapped in a separate class and not inline in the same class of the table they belong to)
	 */
	String getCompositeKeyClassName();
	
	/**
	 * 
	 * @return the default fetch strategy
	 */
	String getDefaultFetch();
	
	/**
	 * 
	 * @return all the columns contained in this business table
	 */
	List<IJpaColumn> getColumns();
	
	/**
	 * 
	 * @return 
	 */
	List<IJpaColumn> getSimpleColumns();
	
	/**
	 * 
	 * @param genOnly
	 * @param includePk
	 * @param includeInherited
	 * @return
	 */
	List<IJpaColumn> getSimpleColumns(boolean genOnly, boolean includePk, boolean includeInherited);
	
	/**
	 * 
	 * @return all the relationships defined uppon this table
	 */
	List<IJpaRelationship> getRelationships();
}
