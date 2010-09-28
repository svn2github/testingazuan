/**
 * 
 */
package it.eng.spagobi.meta.generator;

import java.io.File;

import it.eng.spagobi.meta.model.ModelObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IGenerator {
	void generate(ModelObject o, File outputFile);
}
