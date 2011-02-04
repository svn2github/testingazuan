/**
 * 
 */
package it.eng.spagobi.meta.generator.mondrianschema;

import it.eng.spagobi.meta.generator.GenerationException;
import it.eng.spagobi.meta.generator.IGenerator;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.olap.OlapModel;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class MondrianSchemaGenerator implements IGenerator {
		
	@Override
	public void generate(ModelObject o, File outputFile) {
		OlapModel model;
		
		if(o instanceof OlapModel) {
			model = (OlapModel)o;
			generateMondrianSchema(model, outputFile);
		} else {
			throw new GenerationException("Impossible to create Mondrian Schema from an object of type [" + o.getClass().getName() + "]");
		}
	}
	
	public void generateMondrianSchema(OlapModel model, File outputFile) {
		throw new GenerationException("Function generateMondrianSchema not yet implemented");
	}

}
