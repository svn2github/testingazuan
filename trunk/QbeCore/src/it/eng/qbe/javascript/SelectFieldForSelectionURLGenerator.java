
package it.eng.qbe.javascript;

import it.eng.qbe.utility.IQbeUrlGenerator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectFieldForSelectionURLGenerator implements IURLGenerator{

	/** 
	 * @see it.eng.qbe.utility.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	private String classCompleteName = null;
	private String aliasedClassName = null;
	
	
	
	private IQbeUrlGenerator qbeUrlGenerator = null;
	private HttpServletRequest httpRequest = null;
	
	public SelectFieldForSelectionURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
	}
	
	public SelectFieldForSelectionURLGenerator(String classCompleteName, IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
		this.classCompleteName = classCompleteName;
		
		if (classCompleteName.indexOf(".") > 0){
			aliasedClassName = "a" + classCompleteName.substring(classCompleteName.lastIndexOf(".")+1);
		}else{
			aliasedClassName = "a" + classCompleteName;
		}
	}
	public String generateURL(Object source) {
		
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");
		params.put("COMPLETE_FIELD_NAME", aliasedClassName + "."+source.toString());
		params.put("CLASS_NAME", this.classCompleteName);
		params.put("ALIAS_CLASS_NAME",this.aliasedClassName);
	
		
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
	
	public String generateURL(Object source, Object addtionalParameter) {
		return generateURL(source);
	}

	public String generateURL(Object source, Object source2, Object addtionalParameter) {
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");
		params.put("COMPLETE_FIELD_NAME", aliasedClassName + "."+source.toString());
		params.put("ALIAS_FIELD_NAME", source2);
		params.put("CLASS_NAME", this.classCompleteName);
		params.put("ALIAS_CLASS_NAME",this.aliasedClassName);
	
		
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
}
