/**
 * 
 */
package it.eng.spagobi.meta.mapper.cwm;

import it.eng.spagobi.meta.model.physical.PhysicalModel;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface ICWMMapper {
	public PhysicalModel decodeModel(ICWM cwm);
	public ICWM encodeModel(PhysicalModel model);
}
