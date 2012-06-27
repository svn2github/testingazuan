/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.generator.mondrianschema;

import it.eng.spagobi.meta.generator.GenerationException;
import it.eng.spagobi.meta.generator.IGenerator;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.olap.OlapModel;

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

	@Override
	public void hideTechnicalResources() {
		// TODO Auto-generated method stub
		
	}

	
	
}
