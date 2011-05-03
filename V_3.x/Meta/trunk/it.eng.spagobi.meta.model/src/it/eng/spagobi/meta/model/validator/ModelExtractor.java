/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.model.validator;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModelExtractor {
	public static Model getModel(ModelObject o) {
		Model model;
		
		model = null;
		
		if(o == null) return null;
		
		if(o instanceof Model) {
			model = (Model)o;
		} else if(o instanceof BusinessModel) {
			model = ((BusinessModel)o).getParentModel();
		} else if(o instanceof BusinessColumnSet) {
			model = getModel( ((BusinessColumnSet)o).getModel() );
		} else if(o instanceof BusinessColumn) {
			model = getModel( ((BusinessColumn)o).getTable() );
		} else if(o instanceof BusinessIdentifier) {
			model = getModel( ((BusinessIdentifier)o).getModel() );
		} else if(o instanceof BusinessRelationship) {
			model = getModel( ((BusinessRelationship)o).getModel() );
		}
		
		return model;
	}
}
