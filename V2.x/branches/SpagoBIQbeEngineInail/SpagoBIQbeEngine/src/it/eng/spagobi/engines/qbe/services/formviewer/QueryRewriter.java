/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.qbe.services.formviewer;

import java.util.ArrayList;
import java.util.List;

import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.Query;
import it.eng.spagobi.engines.qbe.QbeEngineInstance;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QueryRewriter {
	
	List<DataMartField> fields;
	QbeEngineInstance engineInstance;
	
	public QueryRewriter(QbeEngineInstance engineInstance, String[] fieldUniqueNames) {
		DataMartField field;
		
		this.engineInstance = engineInstance;
		this.fields = new ArrayList();
		for(int i = 0; i < fieldUniqueNames.length; i++) {
			field = engineInstance.getDatamartModel().getDataMartModelStructure().getField( fieldUniqueNames[i] );
			fields.add(field);
		}
	}
	
	public Query rewrite(Query query) {
		
		Query resultQuery; 
		
		resultQuery = new Query();
		List selectFields = query.getSelectFields(true);
		for(int i = 0; i < selectFields.size(); i++) {
			ISelectField selectField = (ISelectField)selectFields.get(i);
			selectField.copy();
		}
		
		return resultQuery;
	}
}
