package it.eng.qbe.utility;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: Scriptlet.java,v 1.11 2006/04/19 10:26:14 teodord Exp $
 */
public class Scriptlet extends JRDefaultScriptlet
{

	
	public GroovyScriptEngine gsEngine = null;
	public Binding binding = null;
	public Map qbeJasperReportFldMap = null;

	/**
	 *
	 */
	public void beforeReportInit() throws JRScriptletException
	{
		// The format of QBE_JR_MAPPING_STRING IS IN TERM OF
		// qbeFldName->JRFieldName;qbeFldName->JRFieldName;qbeFldName->JRFieldName;
		
		JRFillParameter o = (JRFillParameter)this.parametersMap.get("QBE_JR_MAPPING");
		String qbeJasperReportFldMappingString = (String)o.getValue();
		this.qbeJasperReportFldMap = new HashMap();
		String[] splitMappings = qbeJasperReportFldMappingString.split(";");
		String singleQbeJrMapping = null;
		String[] singleQbeJrMappingSplit = null;
		
		String qbeCompleteFldName = null;
		String jrFldName = null;
		
		for (int i=0; i < splitMappings.length; i++){
			singleQbeJrMapping = splitMappings[i];
			singleQbeJrMappingSplit = singleQbeJrMapping.split("->");
			qbeCompleteFldName = singleQbeJrMappingSplit[0];
			jrFldName = singleQbeJrMappingSplit[1];
			this.qbeJasperReportFldMap.put(qbeCompleteFldName, jrFldName);
		}
		
		this.gsEngine = GroovyEngine.getGroovyEngine().getGroovyScriptEngine();
		this.binding = new Binding();
		this.binding.setVariable("qbe_mode", "export");
	
	}


	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void beforePageInit() throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void afterPageInit() throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void beforeColumnInit() throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void afterColumnInit() throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void beforeGroupInit(String groupName) throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void afterGroupInit(String groupName) throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void beforeDetailEval() throws JRScriptletException
	{
		
	}


	/**
	 *
	 */
	public void afterDetailEval() throws JRScriptletException
	{
	}

	public String executeGroovyScript(String groovyScriptName,String entityName, String classNameInQuery, String fldCompleteNameInQuery,  String mappings){
		try{
			String result = "";
			System.out.println("--> Called execute GroovyScript ["+groovyScriptName+"]["+mappings+"] ["+entityName+"]" );
			String[] mappingArray = mappings.split(",");
			
			for ( int i =0; i < mappingArray.length; i++){
				
				String[] splitMapping = mappingArray[i].split("->");
				
				String fieldId = splitMapping[0];
				
				String groovyInputId = splitMapping[1];
				
				String prefix = null;
				if  ((fldCompleteNameInQuery != null) && 
					(fldCompleteNameInQuery.indexOf(".") > 0)){
					prefix = fldCompleteNameInQuery.substring(0, fldCompleteNameInQuery.lastIndexOf("."));
				}
				
				String completeRequiredFieldId = fieldId; 
				
				if (classNameInQuery.equalsIgnoreCase(entityName)){
					
				}else{
					if (prefix != null){
						completeRequiredFieldId = prefix + "." + completeRequiredFieldId;
					}
					
				}
				
				
				
				String resultingJRFField = (String)this.qbeJasperReportFldMap.get(classNameInQuery + "."+ completeRequiredFieldId);
				JRFillField fillFld = (JRFillField)this.fieldsMap.get(resultingJRFField);
				
				if (fillFld == null)
					return "N.C.";
				binding.setVariable(groovyInputId,fillFld.getValue());
				
			}
			
			Object o = this.gsEngine.run(groovyScriptName, this.binding);
			if (o != null){
				result = o.toString();
			}
			return result;	
		}catch (Throwable e) {
			e.printStackTrace();
			return  "N.C.";
		}
		
		
	}

	


}
