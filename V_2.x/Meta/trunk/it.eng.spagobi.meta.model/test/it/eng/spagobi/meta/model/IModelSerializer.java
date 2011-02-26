/**
 * 
 */
package it.eng.spagobi.meta.model;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import it.eng.spagobi.meta.model.Model;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IModelSerializer {
	void serialize(Model model, File file);
	void serialize(Model model, OutputStream outputStream);
	
	Model deserialize(File file);	
	Model deserialize(InputStream inputStream);	
}
