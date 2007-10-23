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
package it.eng.qbe.utility;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import it.eng.qbe.log.Logger;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CalculatedField {
	
	

	private String id = null;
	private String entityName = null;
	private String fldLabel = null;
	private String script = null;
	private String mappings = null;
	private String fldCompleteNameInQuery = null;
	private String classNameInQuery = null;
	private String inExport = null;
	
	private Map mappingPositions = new HashMap();
	private Map mappingMap = new HashMap();
	
	
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
	
	
	public String getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String getFldLabel() {
		return fldLabel;
	}
	
	public void setFldLabel(String fldLabel) {
		this.fldLabel = fldLabel;
	}

	public String getMappings() {
		return mappings;
	}

	public void setMappings(String mappings) {
		this.mappings = mappings;
		
	}
	
	public void calculateMappings(ISingleDataMartWizardObject aWizardObject){
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
				mappingPositions.put(fieldId, Utils.findPositionOf(aWizardObject, classNameInQuery + "." + fieldId));
			}else{
				if (prefix != null){
					completeRequiredFieldId = prefix + "." + completeRequiredFieldId;
				}
				mappingPositions.put(fieldId, Utils.findPositionOf(aWizardObject, classNameInQuery + "." + completeRequiredFieldId));
			}
		}
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean equals(Object obj) {
		if (obj instanceof CalculatedField) {
			return this.getId().equals(((CalculatedField)obj).getId());
		}else{
			return false;
		}
	}

	public Map getMappingMap() {
		return mappingMap;
	}

	public void setMappingMap(Map mappingMap) {
		this.mappingMap = mappingMap;
	}

	public Map getMappingPositions() {
		return mappingPositions;
	}

	public void setMappingPositions(Map mappingPositions) {
		this.mappingPositions = mappingPositions;
	}

	public String getFldCompleteNameInQuery() {
		return fldCompleteNameInQuery;
	}

	public void setFldCompleteNameInQuery(String fldCompleteNameInQuery) {
		this.fldCompleteNameInQuery = fldCompleteNameInQuery;
	}

	public String getClassNameInQuery() {
		return classNameInQuery;
	}

	public void setClassNameInQuery(String classNameInQuery) {
		this.classNameInQuery = classNameInQuery;
	}

	public String getInExport() {
		return inExport;
	}

	public void setInExport(String inExport) {
		this.inExport = inExport;
	}

	
	
	
	
	
	
}	
