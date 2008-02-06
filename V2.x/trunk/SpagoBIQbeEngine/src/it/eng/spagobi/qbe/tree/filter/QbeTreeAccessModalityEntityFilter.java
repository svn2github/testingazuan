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
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.utility.QbeProperties;
import it.eng.spagobi.qbe.tree.presentation.tag.DatamartLabelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTreeAccessModalityEntityFilter extends ComposableQbeTreeEntityFilter{

	public QbeTreeAccessModalityEntityFilter() {
		super();
	}
	
	public QbeTreeAccessModalityEntityFilter(IQbeTreeEntityFilter parentFilter) {
		super(parentFilter);
	}
	
	public List filter(IDataMartModel datamartModel, List entities) {
		List list = null;
		DataMartEntity entity;
		
		list = new ArrayList();
		
		for(int i = 0; i < entities.size(); i++) {
			entity = (DataMartEntity)entities.get(i);
			if( isEntityVisible(datamartModel, entity)) {
				list.add(entity);
			}
		}
		
		return list;
	}
	
	private boolean isEntityVisible(IDataMartModel datamartModel, DataMartEntity entity) {
		QbeProperties qbeProperties = new QbeProperties(datamartModel);
		
		String entityName = entity.getName();
		if (entity.getRole() != null && !entity.getRole().equalsIgnoreCase("")){
			entityName = entity.getName() + "("+ entity.getRole() + ")";
		}		
		
		if(!qbeProperties.isTableVisible(entityName)) return false;
		if(!datamartModel.getDataMartModelAccessModality().isEntityAccessible(entityName)) return false;
		return true;
	}	
}
