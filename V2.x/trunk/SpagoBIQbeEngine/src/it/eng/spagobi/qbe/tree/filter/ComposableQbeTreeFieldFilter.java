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

import java.util.List;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class ComposableQbeTreeFieldFilter implements IQbeTreeFieldFilter {
	
	private IQbeTreeFieldFilter parentFilter;
	
	public ComposableQbeTreeFieldFilter() {
		parentFilter = null;
	}
	
	public ComposableQbeTreeFieldFilter(IQbeTreeFieldFilter parentFilter) {
		setParentFilter(parentFilter);
	}
	
	public List filterFields(IDataMartModel datamartModel, List fields) {
		if( getParentFilter() != null) {
			fields = getParentFilter().filterFields(datamartModel, fields);
		}
		
		return filter( datamartModel, fields );
	}
	
	public abstract List filter(IDataMartModel datamartModel, List fields);

	protected IQbeTreeFieldFilter getParentFilter() {
		return parentFilter;
	}

	protected void setParentFilter(IQbeTreeFieldFilter parentFilter) {
		this.parentFilter = parentFilter;
	}

}
