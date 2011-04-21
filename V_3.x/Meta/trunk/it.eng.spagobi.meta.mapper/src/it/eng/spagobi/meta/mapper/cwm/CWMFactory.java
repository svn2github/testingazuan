/**
 * 
 */
package it.eng.spagobi.meta.mapper.cwm;

import it.eng.spagobi.meta.mapper.cwm.emf.CWMEMFImpl;
import it.eng.spagobi.meta.mapper.cwm.jmi.SpagoBICWMJMIImpl;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CWMFactory {
	public static ICWM createModel(String modelName, CWMImplType implementationType) {
		ICWM cwm = null;
		
		if(implementationType == CWMImplType.JMI) {
			cwm = new SpagoBICWMJMIImpl(modelName);
		} else {
			cwm = new CWMEMFImpl(modelName);
		}
		
		return cwm;
	}
}
