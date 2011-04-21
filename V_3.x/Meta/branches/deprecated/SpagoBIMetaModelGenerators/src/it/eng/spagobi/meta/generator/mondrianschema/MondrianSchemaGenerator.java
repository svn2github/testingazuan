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
	public void generate(ModelObject o, String outputdir) {
		OlapModel model;
		
		if(o instanceof OlapModel) {
			model = (OlapModel)o;
			generateMondrianSchema(model, outputdir);
		} else {
			throw new GenerationException("Impossible to create JPA Mapping from an object of type [" + o.getClass().getName() + "]");
		}
	}
	
	public void generateMondrianSchema(OlapModel model, String outputdir) {
		throw new GenerationException("Function generateMondrianSchema not yet implemented");
	}

}
