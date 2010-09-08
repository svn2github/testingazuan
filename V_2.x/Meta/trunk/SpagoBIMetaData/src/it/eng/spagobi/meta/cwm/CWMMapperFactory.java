/**
 * 
 */
package it.eng.spagobi.meta.cwm;

import it.eng.spagobi.meta.cwm.emf.CWMEMFImpl;
import it.eng.spagobi.meta.cwm.emf.CWMMapperEMFImpl;
import it.eng.spagobi.meta.cwm.jmi.SpagoBICWMJMIImpl;
import it.eng.spagobi.meta.cwm.jmi.SpagoBICWMMapperJMIImpl;

/**
 * @author agioia
 *
 */
public class CWMMapperFactory {
	public static ICWMMapper getMapper(CWMImplType implementationType) {
		ICWMMapper mapper = null;
		
		if(implementationType == CWMImplType.JMI) {
			mapper = new SpagoBICWMMapperJMIImpl();
		} else {
			mapper = new CWMMapperEMFImpl();
		}
		
		return mapper;
	}
}
