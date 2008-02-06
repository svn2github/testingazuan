/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.qbe.tree.filter;

import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTreeAccessModalityFieldFilter extends ComposableQbeTreeFieldFilter {
	
	private IQbeTreeFieldFilter parentFilter;
	
	public QbeTreeAccessModalityFieldFilter() {
		parentFilter = null;
	}
	
	public QbeTreeAccessModalityFieldFilter(IQbeTreeFieldFilter parentFilter) {
		setParentFilter(parentFilter);
	}
	
	
	public List filter(IDataMartModel datamartModel, List fields) {
		List list;
		DataMartField field;
		
		list = new ArrayList();
		
		for(int i = 0; i < fields.size(); i++) {
			field = (DataMartField)fields.get(i);
			if( isFieldVisible(datamartModel, field)) {
				list.add(field);
			}
		}
		
		return list;
	}
	
	private boolean isFieldVisible(IDataMartModel datamartModel, DataMartField field) {
		return true;
	}
}
