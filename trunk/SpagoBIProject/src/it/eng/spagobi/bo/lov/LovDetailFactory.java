/**
 * 
 */
package it.eng.spagobi.bo.lov;

import it.eng.spago.base.SourceBeanException;

/**
 * @author Gioia
 *
 */
public class LovDetailFactory {
	
	public static final String JAVACLASSLOV = "JAVACLASSLOV";
	public static final String SCRIPTLOV 	= "SCRIPTLOV";
	public static final String QUERYLOV 	= "QUERY";
	public static final String FIXEDLISTLOV = "ROWS";
	
	public static ILovDetail getLovFromXML(String dataDefinition) throws SourceBeanException {
		ILovDetail lov = null;
		
		dataDefinition = dataDefinition.trim();
		if(dataDefinition.startsWith("<" + JAVACLASSLOV + ">")) {
			lov = new JavaClassDetail(dataDefinition);
		} else if(dataDefinition.startsWith("<" + SCRIPTLOV + ">")) {
			lov = new ScriptDetail(dataDefinition);
		} else if(dataDefinition.startsWith("<" + QUERYLOV + ">")) {
			lov = new QueryDetail(dataDefinition);
		} else if( dataDefinition.startsWith("<" + FIXEDLISTLOV + ">")
				|| dataDefinition.startsWith("<LOV>") ) {
			lov = new FixedListDetail(dataDefinition);
		}
		
		return lov;
	}
}
