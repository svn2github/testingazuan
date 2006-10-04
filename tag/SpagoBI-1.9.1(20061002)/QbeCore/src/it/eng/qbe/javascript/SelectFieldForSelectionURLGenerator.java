
package it.eng.qbe.javascript;

import it.eng.qbe.action.SelectFieldForSelectAction;
import it.eng.qbe.utility.IQbeUrlGenerator;
import it.eng.spagobi.utilities.javascript.QbeJsTreeNodeId;

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
	private String className = null;
		
	
	private IQbeUrlGenerator qbeUrlGenerator = null;
	private HttpServletRequest httpRequest = null;
	
	public SelectFieldForSelectionURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
	}
	
	public SelectFieldForSelectionURLGenerator(String className, IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest){
		this.qbeUrlGenerator = qbeUrlGenerator;
		this.httpRequest = httpRequest;
		this.className = className;
	}
	
	public String generateURL(Object fieldName) {
		
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");
		
		params.put(SelectFieldForSelectAction.CLASS_NAME, className);
		params.put(SelectFieldForSelectAction.FIELD_NAME, (String)fieldName);
			
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
	
	public String generateURL(Object source, Object addtionalParameter) {
		return generateURL(source);
	}

	public String generateURL(Object fieldName, Object fieldLabel, Object addtionalParameter) {
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");		
		
		//QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, (String)fieldName);
		params.put(SelectFieldForSelectAction.CLASS_NAME, className);
		params.put(SelectFieldForSelectAction.FIELD_NAME, (String)fieldName);
		params.put(SelectFieldForSelectAction.FIELD_LABEL, fieldLabel);			
		
		return qbeUrlGenerator.getUrl(httpRequest, params);
	}
}
