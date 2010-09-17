/**
 * 
 */
package it.eng.spagobi.meta.serializer;

import java.io.Reader;
import java.io.Writer;

import it.eng.spagobi.meta.model.Model;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IModelSerializer {
	void serialize(Model model, Writer writer);
	Model deserialize(Reader reader);	
}
