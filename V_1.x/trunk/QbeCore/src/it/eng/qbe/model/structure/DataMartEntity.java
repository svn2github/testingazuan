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
package it.eng.qbe.model.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia
 *
 */
public class DataMartEntity implements IDataMartItem {
	
	long id;
	String name;
	String path;
	String role;
	DataMartEntity parent;
	DataMartModelStructure structure;
		
	Map fields;
	Map subEntities;
	
	public DataMartEntity(String name, DataMartModelStructure structure) {
		this.name = name;
		this.structure = structure;
		this.id = structure.getNextId();
		this.path = "";
		this.role = null;
		this.parent = null;
		this.fields = new HashMap();
		this.subEntities = new HashMap();
	}
	
	public DataMartField addField(String fieldName) {
		
		DataMartField field = new DataMartField(fieldName, this);
		addField(field);
		return field;
	}
	
	private void addField(DataMartField field) {
		fields.put(field.getName(), field);
	}
	
	public DataMartField getField(String fieldName) {
		return (DataMartField)fields.get(fieldName);
	}
	
	public List getFields() {
		List list = new ArrayList();
		String key = null;
		for(Iterator it = fields.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			list.add(fields.get(key));			
		}
		return list;
	}
	
	public DataMartEntity addSubEntity(String subentityName) {
		DataMartEntity subEntity = new DataMartEntity(subentityName, structure);
		addSubEntity(subEntity);
		return subEntity;
	}
	
	private void addSubEntity(DataMartEntity entity) {
		subEntities.put(entity.getName(), entity);
	}
	
	public DataMartEntity getSubEntity(String entityName) {
		return (DataMartEntity)subEntities.get(entityName);
	}
	
	public List getSubEntities() {
		List list = new ArrayList();
		String key = null;
		for(Iterator it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			list.add(subEntities.get(key));			
		}
		return list;
	}
	
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
	
	public List getAllFieldOccurencesOnSubEntity(String entityName, String fieldName) {
		List list = new ArrayList();
		List entities = getAllSubEntities(entityName);
		for(int i = 0; i < entities.size(); i++) {
			DataMartEntity entity = (DataMartEntity)entities.get(i);
			List fields = entity.getFields();
			for(int j = 0; j < fields.size(); j++) {
				DataMartField field = (DataMartField)fields.get(j);
				if(field.getName().endsWith("." + fieldName)) {
					list.add(field);
				}
			}
		}
		
		return list;
	}

	public String getName() {
		return name;
	}
	
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

	public DataMartModelStructure getStructure() {
		return structure;
	}

	protected void setStructure(DataMartModelStructure structure) {
		this.structure = structure;
	}

	protected String getRole() {
		return role;
	}

	protected void setRole(String role) {
		this.role = role;
	}
	
}
