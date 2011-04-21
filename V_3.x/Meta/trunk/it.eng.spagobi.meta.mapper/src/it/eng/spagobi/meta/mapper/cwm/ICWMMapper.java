/**
 * 
 */
package it.eng.spagobi.meta.mapper.cwm;

import it.eng.spagobi.meta.mapper.IMapper;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class ICWMMapper implements IMapper {
	
	
	@Override
	public ModelObject decodeModel(Object o) {
		if(o instanceof ICWM) {
			return decodeICWM( (ICWM)o );
		} else {
			return null;		
		}
		
	}
	
	public abstract PhysicalModel decodeICWM(ICWM cwm);
	
	@Override
	public Object encodeModel(ModelObject modelObject) {
		// TODO for the moment we are able to encode only a small portion of the entire model
		if(modelObject instanceof PhysicalModel) {
			return encodeICWM( (PhysicalModel)modelObject );
		} else {
			return null;		
		}
	}
	
	public abstract ICWM encodeICWM(PhysicalModel model);
}
