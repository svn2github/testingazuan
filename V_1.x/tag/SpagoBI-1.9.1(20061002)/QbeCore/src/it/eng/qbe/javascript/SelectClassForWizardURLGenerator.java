
package it.eng.qbe.javascript;

import it.eng.qbe.utility.IQbeUrlGenerator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



/**
 * @author Andrea Zoppello
 * 
 * This implementation of IURLGenerator is responsible for generating URL to
 * select a class
 *
 */
public class SelectClassForWizardURLGenerator implements IURLGenerator{

	private IQbeUrlGenerator qbeUrlGenerator = null;
	private HttpServletRequest httpRequest = null;
	
	public SelectClassForWizardURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest ){
		this.qbeUrlGenerator = qbeUrlGenerator;;
		this.httpRequest = httpRequest;
	}
	
	/** 
	 * @see it.eng.qbe.utility.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	public String generateURL(Object source) {
		
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_CLASS_ACTION");
		params.put("className", source.toString());
		params.put("NEW_SESSION", "TRUE");
		
		return qbeUrlGenerator.getUrl(httpRequest, params);
		
	}

	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object addtionalParameter) {
		return generateURL(source);
	}
	
	/**
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object source2, Object addtionalParameter) {
		return generateURL(source);
	}
}
