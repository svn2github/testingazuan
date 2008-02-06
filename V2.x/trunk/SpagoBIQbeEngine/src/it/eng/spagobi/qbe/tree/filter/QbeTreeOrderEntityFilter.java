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
public class QbeTreeOrderEntityFilter extends ComposableQbeTreeEntityFilter{

	public QbeTreeOrderEntityFilter() {
		super();
	}
	
	public QbeTreeOrderEntityFilter(IQbeTreeEntityFilter parentFilter) {
		super(parentFilter);
	}
	
	public List filter(IDataMartModel datamartModel, List entities) {
		List list = null;
		
		ComparableEntitiesList comparableEntities = new ComparableEntitiesList(datamartModel);
		comparableEntities.addEntities( entities );
		list = comparableEntities.getEntitiesOrderedByLabel();
		
		return list;
	}
	
	private class ComparableEntitiesList {

		private List list;
		private IDataMartModel datamartModel;
		
		ComparableEntitiesList(IDataMartModel datamartModel) {
			list = new ArrayList();
			this.datamartModel = datamartModel;
		}
		
		void addEntity(DataMartEntity entity) {
			String label = DatamartLabelFactory.getEntityLabel(datamartModel, entity);	
			EntityWrapper field = new EntityWrapper(label, entity);
			list.add(field);
		}
		
		void addEntities(Set entities) {
			if (entities != null && entities.size() > 0) {
				Iterator it = entities.iterator();
				while (it.hasNext()) {
					DataMartEntity relation = (DataMartEntity) it.next();
					addEntity(relation);
				}
			}
		}
		
		void addEntities(List relations) {
			if (relations != null && relations.size() > 0) {
				Iterator it = relations.iterator();
				while (it.hasNext()) {
					DataMartEntity entity = (DataMartEntity) it.next();
					addEntity(entity);
				}
			}
		}
		
		List getEntitiesOrderedByLabel () {
			Collections.sort(list);
			List toReturn = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EntityWrapper field = (EntityWrapper) it.next();
				toReturn.add(field.getEntity());
			}
			return toReturn;
		}
		
	}
	
	
	private class EntityWrapper implements Comparable {
		
		private DataMartEntity entity;
		private String label;
		
		EntityWrapper (String label, DataMartEntity entity) {
			this.entity = entity;
			this.label = label;
		}
		
		public int compareTo(Object o) {
			if (o == null) throw new NullPointerException();
			if (!(o instanceof EntityWrapper)) throw new ClassCastException();
			EntityWrapper anotherEntity = (EntityWrapper) o;
			return this.getLabel().compareTo(anotherEntity.getLabel());
		}
		
		public DataMartEntity getEntity() {
			return entity;
		}
		public void setEntity(DataMartEntity entity) {
			this.entity = entity;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		
	}
}
