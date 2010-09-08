/**
 * 
 */
package it.eng.spagobi.meta.cwm;

import it.eng.spagobi.meta.cwm.jmi.CWMJMIImpl;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

/**
 * @author agioia
 *
 */
public interface ICWMMapper {
	public PhysicalModel decodeModel(ICWM cwm);
	public ICWM encodeModel(PhysicalModel model);
}
