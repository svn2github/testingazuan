/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.model.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class DataMartModelStructure.
 * 
 * @author Andrea Gioia
 */
public class DataMartModelStructure {
	
	/** The id. */
	long id = 0;
	
	/** The root entities. */
	Map rootEntities;
	
	/** The fields. */
	Map fields;
	
	/** The entities. */
	Map entities;
	
	/**
	 * Instantiates a new data mart model structure.
	 */
	public DataMartModelStructure() {
		rootEntities = new HashMap();
		fields = new HashMap();
		entities = new HashMap();
	}
	
	/**
	 * Adds the root entity.
	 * 
	 * @param name the name
	 * @param path the path
	 * @param role the role
	 * @param type the type
	 * 
	 * @return the data mart entity
	 */
	public DataMartEntity addRootEntity(String name, String path, String role, String type) {
		DataMartEntity entity = new DataMartEntity(name, path, role, type, this);
		addRootEntity(entity);
		return entity;
	}
	
	/**
	 * Adds the root entity.
	 * 
	 * @param entity the entity
	 */
	private void addRootEntity(DataMartEntity entity) {
		rootEntities.put(entity.getUniqueName(), entity);
		addEntity(entity);
	}
	
	
	/**
	 * Gets the root entity.
	 * 
	 * @param entityName the entity name
	 * 
	 * @return the root entity
	 */
	public DataMartEntity getRootEntity(String entityName) {
		return (DataMartEntity)rootEntities.get(entityName);
	}
	
	/**
	 * Gets the root entity iterator.
	 * 
	 * @param datamartName the datamart name
	 * 
	 * @return the root entity iterator
	 */
	public Iterator getRootEntityIterator(String datamartName) {
		return getRootEntities(datamartName).iterator();
	}
	
	/**
	 * Gets the root entities.
	 * 
	 * @param datamartName the datamart name
	 * 
	 * @return the root entities
	 */
	public List getRootEntities(String datamartName) {
		List list = new ArrayList();
		Iterator it = rootEntities.keySet().iterator();
		while(it.hasNext()) {
			String entityName = (String)it.next();
			// TODO replace with this ...
			//list.add( entities.get(entityName).getCopy() );
			list.add( rootEntities.get(entityName) );
		}
		return list;
	}	
		
	/**
	 * Adds the entity.
	 * 
	 * @param entity the entity
	 */
	public void addEntity(DataMartEntity entity) {
		entities.put(entity.getUniqueName(), entity);
	}
	
	/**
	 * Gets the entity.
	 * 
	 * @param entityUniqueName the entity unique name
	 * 
	 * @return the entity
	 */
	public DataMartEntity getEntity(String entityUniqueName) {
		DataMartEntity entity = (DataMartEntity)entities.get(entityUniqueName);
		return entity;
	}
	
	/**
	 * Adds the field.
	 * 
	 * @param field the field
	 */
	public void addField(DataMartField field) {
		fields.put(field.getUniqueName(), field);
	}
	
	/**
	 * Gets the field.
	 * 
	 * @param fieldUniqueName the field unique name
	 * 
	 * @return the field
	 */
	public DataMartField getField(String fieldUniqueName) {
		DataMartField field = (DataMartField)fields.get(fieldUniqueName);
		return field;
	}
	
	
	
	
	/**
	 * Gets the next id.
	 * 
	 * @return the next id
	 */
	public long getNextId() {
		return ++id;
	}
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		String key = null;
		for(Iterator it = rootEntities.keySet().iterator(); it.hasNext();) {
			key = (String)it.next();
			DataMartEntity o = (DataMartEntity)rootEntities.get(key);
			buffer.append("\n------------------------------------\n");
			if(o == null)
				buffer.append(key + " --> NULL\n");
			else
				buffer.append(o.toString() + "\n");
		}
		return buffer.toString();
	}	 
}
