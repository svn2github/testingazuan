/**
 * 
 */
package it.eng.spagobi.meta.mapper.cwm;

import it.eng.spagobi.meta.mapper.cwm.emf.CWMMapperEMFImpl;
import it.eng.spagobi.meta.mapper.cwm.jmi.SpagoBICWMMapperJMIImpl;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
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
