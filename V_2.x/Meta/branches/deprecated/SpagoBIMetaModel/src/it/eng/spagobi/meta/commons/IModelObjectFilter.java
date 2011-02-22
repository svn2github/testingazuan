/**
 * 
 */
package it.eng.spagobi.meta.commons;

import it.eng.spagobi.meta.model.ModelObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IModelObjectFilter {
	boolean filter(ModelObject o);
}
