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
package it.eng.qbe.utility;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import it.eng.qbe.log.Logger;
import it.eng.qbe.query.IQuery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class CalculatedField.
 */
public class CalculatedField {
	
	

	/** The id. */
	private String id = null;
	
	/** The entity name. */
	private String entityName = null;
	
	/** The fld label. */
	private String fldLabel = null;
	
	/** The script. */
	private String script = null;
	
	/** The mappings. */
	private String mappings = null;
	
	/** The fld complete name in query. */
	private String fldCompleteNameInQuery = null;
	
	/** The class name in query. */
	private String classNameInQuery = null;
	
	/** The in export. */
	private String inExport = null;
	
	/** The mapping positions. */
	private Map mappingPositions = new HashMap();
	
	/** The mapping map. */
	private Map mappingMap = new HashMap();
	
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	public CalculatedField getCopy() {
		CalculatedField calculatedField = new CalculatedField();
		
		calculatedField.setId(id);
		calculatedField.setEntityName(entityName);
		calculatedField.setFldLabel(fldLabel);
		calculatedField.setScript(script);
		calculatedField.setMappings(mappings);
		calculatedField.setFldCompleteNameInQuery(fldCompleteNameInQuery);
		calculatedField.setClassNameInQuery(classNameInQuery);
		calculatedField.setInExport(inExport);
		Map map = null;
		Iterator it = null;
		
		map = new HashMap();
		it = mappingPositions.keySet().iterator();
		while(it.hasNext()) {
			Object key = it.next();
			Object value = mappingPositions.get(key);
			map.put(key, value);		
		}
		calculatedField.setMappingPositions(map);
		
		map = new HashMap();
		it = mappingMap.keySet().iterator();
		while(it.hasNext()) {
			Object key = it.next();
			Object value = mappingPositions.get(key);
			map.put(key, value);		
		}
		calculatedField.setMappingMap(map);
		
		return calculatedField;
	}
	
	
	/**
	 * Gets the entity name.
	 * 
	 * @return the entity name
	 */
	public String getEntityName() {
		return entityName;
	}
	
	/**
	 * Sets the entity name.
	 * 
	 * @param entityName the new entity name
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	/**
	 * Gets the fld label.
	 * 
	 * @return the fld label
	 */
	public String getFldLabel() {
		return fldLabel;
	}
	
	/**
	 * Sets the fld label.
	 * 
	 * @param fldLabel the new fld label
	 */
	public void setFldLabel(String fldLabel) {
		this.fldLabel = fldLabel;
	}

	/**
	 * Gets the mappings.
	 * 
	 * @return the mappings
	 */
	public String getMappings() {
		return mappings;
	}

	/**
	 * Sets the mappings.
	 * 
	 * @param mappings the new mappings
	 */
	public void setMappings(String mappings) {
		this.mappings = mappings;
		
	}
	
	/**
	 * Calculate mappings.
	 * 
	 * @param query the query
	 */
	public void calculateMappings(IQuery query){
		String[] mappingArray = mappings.split(",");
	
		String prefix = null;
		if  ((this.fldCompleteNameInQuery != null) && 
			(this.fldCompleteNameInQuery.indexOf(".") > 0)){
			prefix = fldCompleteNameInQuery.substring(0, fldCompleteNameInQuery.lastIndexOf("."));
		}
		String completeRequiredFieldId = null;
		for ( int i =0; i < mappingArray.length; i++){
			String[] splitMapping = mappingArray[i].split("->");
			String fieldId = splitMapping[0];
			String groovyInputId = splitMapping[1];
			completeRequiredFieldId = fieldId; 
			
			mappingMap.put(groovyInputId,fieldId);
			if (classNameInQuery.equalsIgnoreCase(entityName)){
				mappingPositions.put(fieldId, query.findPositionOf( classNameInQuery + "." + fieldId ));
			}else{
				if (prefix != null){
					completeRequiredFieldId = prefix + "." + completeRequiredFieldId;
				}
				mappingPositions.put(fieldId, query.findPositionOf( classNameInQuery + "." + completeRequiredFieldId));
			}
		}
	}

	/**
	 * Gets the script.
	 * 
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Sets the script.
	 * 
	 * @param script the new script
	 */
	public void setScript(String script) {
		this.script = script;
	}
	
	/**
	 * Calculate.
	 * 
	 * @param gse the gse
	 * @param record the record
	 * @param binding the binding
	 * 
	 * @return the object
	 * 
	 * @throws Exception the exception
	 */
	public Object calculate(GroovyScriptEngine gse, Object[] record, Binding binding) throws Exception{
		try{
			Iterator it = mappingMap.keySet().iterator();
			while(it.hasNext()){
				String groovyInput = (String)it.next();
				int posistionsOfTheValueInRecord = ((Integer)mappingPositions.get(mappingMap.get(groovyInput))).intValue();
			
				binding.setVariable(groovyInput,record[posistionsOfTheValueInRecord]);
			 
			}
			return gse.run(this.script, binding);
		}catch (Throwable t) {
			Logger.debug(CalculatedField.class, t.getMessage());
			return "N.C.";
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof CalculatedField) {
			return this.getId().equals(((CalculatedField)obj).getId());
		}else{
			return false;
		}
	}

	/**
	 * Gets the mapping map.
	 * 
	 * @return the mapping map
	 */
	public Map getMappingMap() {
		return mappingMap;
	}

	/**
	 * Sets the mapping map.
	 * 
	 * @param mappingMap the new mapping map
	 */
	public void setMappingMap(Map mappingMap) {
		this.mappingMap = mappingMap;
	}

	/**
	 * Gets the mapping positions.
	 * 
	 * @return the mapping positions
	 */
	public Map getMappingPositions() {
		return mappingPositions;
	}

	/**
	 * Sets the mapping positions.
	 * 
	 * @param mappingPositions the new mapping positions
	 */
	public void setMappingPositions(Map mappingPositions) {
		this.mappingPositions = mappingPositions;
	}

	/**
	 * Gets the fld complete name in query.
	 * 
	 * @return the fld complete name in query
	 */
	public String getFldCompleteNameInQuery() {
		return fldCompleteNameInQuery;
	}

	/**
	 * Sets the fld complete name in query.
	 * 
	 * @param fldCompleteNameInQuery the new fld complete name in query
	 */
	public void setFldCompleteNameInQuery(String fldCompleteNameInQuery) {
		this.fldCompleteNameInQuery = fldCompleteNameInQuery;
	}

	/**
	 * Gets the class name in query.
	 * 
	 * @return the class name in query
	 */
	public String getClassNameInQuery() {
		return classNameInQuery;
	}

	/**
	 * Sets the class name in query.
	 * 
	 * @param classNameInQuery the new class name in query
	 */
	public void setClassNameInQuery(String classNameInQuery) {
		this.classNameInQuery = classNameInQuery;
	}

	/**
	 * Gets the in export.
	 * 
	 * @return the in export
	 */
	public String getInExport() {
		return inExport;
	}

	/**
	 * Sets the in export.
	 * 
	 * @param inExport the new in export
	 */
	public void setInExport(String inExport) {
		this.inExport = inExport;
	}

	
	
	
	
	
	
}	
