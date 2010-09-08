/**
 * 
 */
package it.eng.spagobi.meta.cwm;

import it.eng.spagobi.meta.cwm.emf.CWMEMFImpl;
import it.eng.spagobi.meta.cwm.jmi.CWMJMIImpl;

/**
 * @author agioia
 *
 */
public class CWMFactory {
	public static ICWM createModel(String modelName, CWMImplType implementationType) {
		ICWM cwm = null;
		
		if(implementationType == CWMImplType.JMI) {
			cwm = new CWMJMIImpl(modelName);
		} else {
			cwm = new CWMEMFImpl(modelName);
		}
		
		return cwm;
	}
}
