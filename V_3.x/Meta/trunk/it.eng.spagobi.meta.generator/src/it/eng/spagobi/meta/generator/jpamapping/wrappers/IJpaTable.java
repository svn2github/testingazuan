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
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

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
	 * @return the name of the table. It is used in java comments or for label generation
	 */
	String getName();
	
	/**
	 * 
	 * @return the name of the table. It is used in java comments or for tooltip generation
	 */
	String getDescription();
	
	/**
	 * 
	 * @return the name of the class generated from this table (not qualified)
	 */
	String getClassName();
	
	/**
	 * @returns the generated java class name (qualified).
	 */
	String getQualifiedClassName();
	
	String getSqlName();
	
	String getUniqueName();
	
	/**
	 * 
	 * @return return true if the wrapped table has no key 
	 */
	boolean hasFakePrimaryKey();	
	
	/**
	 * 
	 * @return it return false iff the table have one column key. If the table have no key
	 * 			it return true in order to generate a fake key composed by all columns contained in the table
	 * 			just to keep happy the jpa runtime that work only if all entities have a key. To understand if
	 * 			the composed key is a real key or a fake one use the method <code>hasFakePrimaryKey</code>
	 */
	boolean hasCompositeKey();
	
	/**
	 * 
	 * @return the name of the java class used to map the composite primary key (note: composite primary key
	 * 			are mapped in a separate class and not inline in the same class of the table they belong to)
	 */
	String getCompositeKeyClassName();
	
	String getQualifiedCompositeKeyClassName();
	
	/** 
	 * 
	 * @return the name of the composed key property
	 */
	String getCompositeKeyPropertyName();
	
	/**
	 * 
	 * @return the columns contained in the primary key
	 */
	List<IJpaColumn> getPrimaryKeyColumns();
	
	
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
	 * @return equals to getSimpleColumns(true, true, true);
	 */
	List<IJpaColumn> getSimpleColumns();
	
	/**
	 * Returns the <code>IJpaColumn</code> objects for the the columns that
	 * are not part of any association.
	 * 
	 * @param genOnly  			Whether to include only the columns marked for generation.
	 * 
	 * @param includePk  		Whether to include the primary key column(s).
	 * 
	 * @param includeInherited 	Whether to include the columns associated with java properties
	 *            				that exist in the super class (if any).
	 */
	List<IJpaColumn> getSimpleColumns(boolean genOnly, boolean includePk, boolean includeInherited);
	
	/**
	 * 
	 * @return all the relationships defined upon this table
	 */
	List<IJpaRelationship> getRelationships();
}
