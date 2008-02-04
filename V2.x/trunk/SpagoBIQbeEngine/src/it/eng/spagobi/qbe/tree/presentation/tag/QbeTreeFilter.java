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
package it.eng.spagobi.qbe.tree.presentation.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.utility.JsTreeUtils;
import it.eng.qbe.utility.QbeProperties;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTreeFilter implements IQbeTreeFilter {
	
	/*
	 * TODO implement filter chain pattern in order to make the behaviour of the filter composable 
	 * (similar pattern used by java xxxInputStream classes)
	 */
	
	public List filterEntities(IDataMartModel datamartModel, List entities) {
		List list;
		DataMartEntity entity;
		
		list = new ArrayList();
		
		for(int i = 0; i < entities.size(); i++) {
			entity = (DataMartEntity)entities.get(i);
			if( isEntityVisible(datamartModel, entity)) {
				list.add(entity);
			}
		}
		
		ComparableEntitiesList comparableEntities = new ComparableEntitiesList(datamartModel);
		comparableEntities.addEntities( list );
		list = comparableEntities.getEntitiesOrderedByLabel();
		
		return list;
	}
	
	public List filterFields(IDataMartModel datamartModel, List fields) {
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

	private boolean isFieldVisible(IDataMartModel datamartModel, DataMartField field) {
		return true;
	}	
	
	/*
	 * TODO: refactor ...
	 * + use a plugable comparator (i.e. label comparator)
	 * + generalize, if possible, to use this code for both fields and entities (i.e. base the code on the super class DataMartItem).	 * 
	 */
	
	
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
