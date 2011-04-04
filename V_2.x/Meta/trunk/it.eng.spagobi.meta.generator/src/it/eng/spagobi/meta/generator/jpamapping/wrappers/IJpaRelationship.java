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

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IJpaRelationship {
	
	final static String ONE_TO_MANY = "one-to-many";
	final static String MANY_TO_ONE = "many-to-one";
	final static String MANY_TO_MANY = "many-to-many";
	final static String ONE_TO_ONE = "one-to-one";
	
	boolean isBidirectional();
	String getCardinality();
	boolean isOneToMany();	
	boolean isManyToMany();	
	
	AbstractJpaTable getReferencedTable();
	
	AbstractJpaTable getJpaTable();
	
	/**
	 * Returns a descriptive string used in a comment in the generated 
	 * file (from the Velocity template).
	 */
	public String getDescription();
	
	public String getPropertyName();
	String getBidirectionalPropertyName();
	
	/**
	 * @return the name of the metod GETTER
	 */
	String getGetter(String par);
	/**
	 * @return the name of the metod SETTER
	 */
	String getSetter(String par);
	
	public String getSimpleSourceColumnName();
	
	public String getCollectionType();
	
	public String getOppositeWithAnnotation();
}
