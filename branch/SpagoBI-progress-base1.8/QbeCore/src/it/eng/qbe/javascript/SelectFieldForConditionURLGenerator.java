
package it.eng.qbe.javascript;

import it.eng.qbe.utility.IQbeUrlGenerator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andrea Zoppello
 * 
 * This implementation of IURLGenerator is responsible for generating URL to
 * select a field for where clause
 *
 */

public class SelectFieldForConditionURLGenerator implements IURLGenerator{

	/** 
	 * @see it.eng.qbe.utility.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	private String classCompleteName = null;
	private String aliasedClassName = null;
	
	
	private IQbeUrlGenerator qbeUrlGenerator = null;
	private HttpServletRequest httpRequest = null;
	
	/**
	 * @param qbeUrlGenerator
	 * @param httpRequest
	 */
	public SelectFieldForConditionURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
	}
	
	/**
	 * @param classCompleteName
	 * @param qbeUrlGenerator
	 * @param httpRequest
	 */
	public SelectFieldForConditionURLGenerator(String classCompleteName, IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
		
		this.classCompleteName = classCompleteName;
		
		if (classCompleteName.indexOf(".") > 0){
			aliasedClassName = "a" + classCompleteName.substring(classCompleteName.lastIndexOf(".")+1);
		}else{
			aliasedClassName = "a" + classCompleteName;
		}
	}
	
	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	public String generateURL(Object source) {
		

		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_WHERE_ACTION");
		params.put("COMPLETE_FIELD_NAME", aliasedClassName + "."+source.toString());
		params.put("CLASS_NAME", this.classCompleteName);
		params.put("ALIAS_CLASS_NAME",this.aliasedClassName);
	
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
	
	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object addtionalParameter) {
		/*
		Map params = new HashMap();
		params.put("ACTION_NAME","SELECT_FIELD_FOR_WHERE_ACTION");
		params.put("COMPLETE_FIELD_NAME", aliasedClassName + "."+source.toString());
		params.put("CLASS_NAME", this.classCompleteName);
		params.put("HIB_TYPE",addtionalParameter.toString());
		return qbeUrlGenerator.getUrl(httpRequest, params);
		*/
		StringBuffer sb = new StringBuffer();
		sb.append("javascript: selectFieldForConditionCallBack(");
		sb.append("\\'SELECT_FIELD_FOR_WHERE_ACTION\\',");
		sb.append("\\'"+aliasedClassName + "."+source.toString()+"\\',");
		sb.append("\\'"+this.classCompleteName+"\\',");
		sb.append("\\'"+addtionalParameter.toString()+"\\'");
		sb.append(");");
		
		return sb.toString();
	}
	
	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object source2, Object addtionalParameter) {
		return generateURL(source, addtionalParameter);
	}
	
}
