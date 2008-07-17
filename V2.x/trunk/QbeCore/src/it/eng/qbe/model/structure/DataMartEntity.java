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

import it.eng.spagobi.utilities.strings.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class DataMartEntity.
 * 
 * @author Andrea Gioia
 */
public class DataMartEntity implements IDataMartItem {
	
	/** The id. */
	long id;
	
	/** The name. */
	String name;			// ProductClass
	
	/** The path. */
	String path;			// Product(product_id)
	
	/** The role. */
	String role;			// product_class_id
	
	/** The type. */
	String type;			// it.foodmart.ProductClass
	
	/** The parent. */
	DataMartEntity parent;	
	
	/** The root. */
	DataMartEntity root;	
	
	/** The structure. */
	DataMartModelStructure structure;
			
	/** The fields. */
	Map fields;	
	
	/** The sub entities. */
	Map subEntities;
	
		
	/**
	 * Instantiates a new data mart entity.
	 * 
	 * @param name the name
	 * @param path the path
	 * @param role the role
	 * @param type the type
	 * @param structure the structure
	 */
	public DataMartEntity(String name, String path, String role, String type,
			DataMartModelStructure structure) {
		
		setStructure( structure );
		
		setId ( structure.getNextId() );
		setName( name );		
		setPath( path == null? "" : path );
		setRole( role );
		setType( type );
		
		this.parent = null;
		this.fields = new HashMap();
		this.subEntities = new HashMap();
	}
	
	/**
	 * Gets the unique name.
	 * 
	 * @return the unique name
	 */
	public String getUniqueName() {
		String uniqueName = "";
			
		uniqueName += getRoot().getType() + ":";
		uniqueName += getPath() + ":";
		uniqueName += getName();
		if(getRole() != null) uniqueName +=  "(" + getRole() + ")";
		
		return uniqueName;
	}
	
	/**
	 * 
	 * @return the unique type - i.e. type + role (if any). Ex. it.eng.spagobi.Promotion(promotion_id)
	 */
	public String getUniqueType() {
		String entityType = getType();
		if ( !StringUtils.isEmpty( getRole() ) ) {
			entityType += "(" + getRole() + ")";
		}
		return entityType;
	}
	
	/**
	 * Adds the field.
	 * 
	 * @param field the field
	 */
	private void addField(DataMartField field) {
		fields.put(field.getUniqueName(), field);
		getStructure().addField(field);
	}
	
	/**
	 * Adds the field.
	 * 
	 * @param fieldName the field name
	 * @param isKey the is key
	 * 
	 * @return the data mart field
	 */
	private DataMartField addField(String fieldName, boolean isKey) {
		
		DataMartField field = new DataMartField(fieldName, this);
		field.setKey(isKey);
		addField(field);
		return field;
	}
	
	/**
	 * Adds the normal field.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return the data mart field
	 */
	public DataMartField addNormalField(String fieldName) {
		return addField(fieldName, false);
	}
	
	/**
	 * Adds the key field.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return the data mart field
	 */
	public DataMartField addKeyField(String fieldName) {		
		return addField(fieldName, true);
	}
	
	
	/**
	 * Gets the field.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return the field
	 */
	public DataMartField getField(String fieldName) {
		return (DataMartField)fields.get(fieldName);
	}
	
	/**
	 * Gets the all fields.
	 * 
	 * @return the all fields
	 */
	public List getAllFields() {
		List list = new ArrayList();
		String key = null;
		for(Iterator it = fields.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			list.add(fields.get(key));			
		}
		return list;
	}	
	
	/**
	 * Gets the fields by type.
	 * 
	 * @param isKey the is key
	 * 
	 * @return the fields by type
	 */
	private List getFieldsByType(boolean isKey) {
		List list = new ArrayList();
		String key = null;
		for(Iterator it = fields.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			DataMartField field = (DataMartField)fields.get(key);
			if(field.isKey() == isKey) {
				list.add(field);		
			}
		}
		return list;
	}
	
	/**
	 * Gets the key fields.
	 * 
	 * @return the key fields
	 */
	public List getKeyFields() {
		return getFieldsByType(true);
	}
	
	/**
	 * Gets the key field iterator.
	 * 
	 * @return the key field iterator
	 */
	public Iterator getKeyFieldIterator() {
		return getKeyFields().iterator();
	}
	
	/**
	 * Gets the normal fields.
	 * 
	 * @return the normal fields
	 */
	public List getNormalFields() {
		return getFieldsByType(false);
	}
	
	/**
	 * Gets the normal field iterator.
	 * 
	 * @return the normal field iterator
	 */
	public Iterator getNormalFieldIterator() {
		return getNormalFields().iterator();
	}	
	
	/**
	 * Adds the sub entity.
	 * 
	 * @param subEntityName the sub entity name
	 * @param subEntityRole the sub entity role
	 * @param subEntityType the sub entity type
	 * 
	 * @return the data mart entity
	 */
	public DataMartEntity addSubEntity(String subEntityName, String subEntityRole, String subEntityType) {
				
		String subEntityPath = "";
		if(getParent() != null) {
			subEntityPath = getName() +  "(" + role + ")";
			if(!getPath().equalsIgnoreCase("")) {
				subEntityPath = getPath() + "." + subEntityPath;
			}
		}
		
		DataMartEntity subEntity = new DataMartEntity(subEntityName, subEntityPath, subEntityRole, subEntityType, structure);
		subEntity.setParent(this);
		
		addSubEntity(subEntity);
		return subEntity;
	}
	
	/**
	 * Adds the sub entity.
	 * 
	 * @param entity the entity
	 */
	private void addSubEntity(DataMartEntity entity) {
		subEntities.put(entity.getUniqueName(), entity);
		getStructure().addEntity(entity);
	}
	
	/**
	 * Gets the sub entity.
	 * 
	 * @param entityUniqueName the entity unique name
	 * 
	 * @return the sub entity
	 */
	public DataMartEntity getSubEntity(String entityUniqueName) {
		return (DataMartEntity)subEntities.get(entityUniqueName);
	}
	
	/**
	 * Gets the sub entities.
	 * 
	 * @return the sub entities
	 */
	public List getSubEntities() {
		List list = new ArrayList();
		String key = null;
		for(Iterator it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			list.add(subEntities.get(key));			
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Gets the all sub entities.
	 * 
	 * @return the all sub entities
	 */
	public List getAllSubEntities() {
		List list = new ArrayList();
		String key = null;
		for(Iterator it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			DataMartEntity entity = (DataMartEntity)subEntities.get(key);
			list.add(entity);
			list.addAll(entity.getAllSubEntities());
		}
		return list;
	}
	
	/**
	 * Gets the all sub entities.
	 * 
	 * @param entityName the entity name
	 * 
	 * @return the all sub entities
	 */
	public List getAllSubEntities(String entityName) {
		List list = new ArrayList();
		String key = null;
		for(Iterator it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			DataMartEntity entity = (DataMartEntity)subEntities.get(key);
			if(entity.getName().equalsIgnoreCase(entityName)) {
				list.add(entity);
			}
			
			list.addAll(entity.getAllSubEntities(entityName));
		}
		return list;
	}
	
	/**
	 * Gets the all field occurences on sub entity.
	 * 
	 * @param entityName the entity name
	 * @param fieldName the field name
	 * 
	 * @return the all field occurences on sub entity
	 */
	public List getAllFieldOccurencesOnSubEntity(String entityName, String fieldName) {
		List list = new ArrayList();
		List entities = getAllSubEntities(entityName);
		for(int i = 0; i < entities.size(); i++) {
			DataMartEntity entity = (DataMartEntity)entities.get(i);
			List fields = entity.getAllFields();
			for(int j = 0; j < fields.size(); j++) {
				DataMartField field = (DataMartField)fields.get(j);
				if(field.getName().endsWith("." + fieldName)) {
					list.add(field);
				}
			}
		}
		
		return list;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		String line = name.toUpperCase() + "(id="+id
			+";path="+path
			+";parent:" + (parent==null?"NULL": parent.getName())
			+";role="+role;
		
		
		buffer.append(line + "\n");
		String key = null;
		for(Iterator it = fields.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			Object o = fields.get(key);
			buffer.append(" - " + (o==null? "NULL": o.toString()) + "\n");
		}
		
		for(Iterator it = subEntities.keySet().iterator(); it.hasNext();) {
			key = (String)it.next();
			Object o = subEntities.get(key);
			buffer.append(" + " + (o==null? "NULL": o.toString()));
		}
		return buffer.toString();
	}

	/**
	 * Gets the structure.
	 * 
	 * @return the structure
	 */
	public DataMartModelStructure getStructure() {
		return structure;
	}

	/**
	 * Sets the structure.
	 * 
	 * @param structure the new structure
	 */
	protected void setStructure(DataMartModelStructure structure) {
		this.structure = structure;
	}


	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public DataMartEntity getParent() {
		return parent;
	}


	/**
	 * Sets the parent.
	 * 
	 * @param parent the new parent
	 */
	public void setParent(DataMartEntity parent) {
		this.parent = parent;
	}



	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public String getPath() {
		return path;
	}



	/**
	 * Sets the path.
	 * 
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}



	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public String getRole() {
		return role;
	}



	/**
	 * Sets the role.
	 * 
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}



	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}



	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}



	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	private void setId(long id) {
		this.id = id;
	}


	/**
	 * Gets the root.
	 * 
	 * @return the root
	 */
	public DataMartEntity getRoot() {
		if(root == null) {
			root = this;
			while(root.getParent() != null) {
				root = root.getParent();
			}
		}		
		
		return root;
	}


	/**
	 * Sets the root.
	 * 
	 * @param root the new root
	 */
	public void setRoot(DataMartEntity root) {
		this.root = root;
	}
	
	

	
}
