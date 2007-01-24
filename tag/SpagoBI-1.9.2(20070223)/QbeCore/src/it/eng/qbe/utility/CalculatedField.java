package it.eng.qbe.utility;

import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ScriptException;

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
