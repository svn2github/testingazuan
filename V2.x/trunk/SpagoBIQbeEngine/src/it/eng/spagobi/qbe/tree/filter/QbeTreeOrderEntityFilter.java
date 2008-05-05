/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
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

// TODO: Auto-generated Javadoc
/**
 * The Class QbeTreeOrderEntityFilter.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QbeTreeOrderEntityFilter extends ComposableQbeTreeEntityFilter{

	/**
	 * Instantiates a new qbe tree order entity filter.
	 */
	public QbeTreeOrderEntityFilter() {
		super();
	}
	
	/**
	 * Instantiates a new qbe tree order entity filter.
	 * 
	 * @param parentFilter the parent filter
	 */
	public QbeTreeOrderEntityFilter(IQbeTreeEntityFilter parentFilter) {
		super(parentFilter);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.filter.ComposableQbeTreeEntityFilter#filter(it.eng.qbe.model.IDataMartModel, java.util.List)
	 */
	public List filter(IDataMartModel datamartModel, List entities) {
		List list = null;
		
		ComparableEntitiesList comparableEntities = new ComparableEntitiesList(datamartModel);
		comparableEntities.addEntities( entities );
		list = comparableEntities.getEntitiesOrderedByLabel();
		
		return list;
	}
	
	/**
	 * The Class ComparableEntitiesList.
	 */
	private class ComparableEntitiesList {

		/** The list. */
		private List list;
		
		/** The datamart model. */
		private IDataMartModel datamartModel;
		
		/**
		 * Instantiates a new comparable entities list.
		 * 
		 * @param datamartModel the datamart model
		 */
		ComparableEntitiesList(IDataMartModel datamartModel) {
			list = new ArrayList();
			this.datamartModel = datamartModel;
		}
		
		/**
		 * Adds the entity.
		 * 
		 * @param entity the entity
		 */
		void addEntity(DataMartEntity entity) {
			String label = DatamartLabelFactory.getEntityLabel(datamartModel, entity);	
			EntityWrapper field = new EntityWrapper(label, entity);
			list.add(field);
		}
		
		/**
		 * Adds the entities.
		 * 
		 * @param entities the entities
		 */
		void addEntities(Set entities) {
			if (entities != null && entities.size() > 0) {
				Iterator it = entities.iterator();
				while (it.hasNext()) {
					DataMartEntity relation = (DataMartEntity) it.next();
					addEntity(relation);
				}
			}
		}
		
		/**
		 * Adds the entities.
		 * 
		 * @param relations the relations
		 */
		void addEntities(List relations) {
			if (relations != null && relations.size() > 0) {
				Iterator it = relations.iterator();
				while (it.hasNext()) {
					DataMartEntity entity = (DataMartEntity) it.next();
					addEntity(entity);
				}
			}
		}
		
		/**
		 * Gets the entities ordered by label.
		 * 
		 * @return the entities ordered by label
		 */
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
	
	
	/**
	 * The Class EntityWrapper.
	 */
	private class EntityWrapper implements Comparable {
		
		/** The entity. */
		private DataMartEntity entity;
		
		/** The label. */
		private String label;
		
		/**
		 * Instantiates a new entity wrapper.
		 * 
		 * @param label the label
		 * @param entity the entity
		 */
		EntityWrapper (String label, DataMartEntity entity) {
			this.entity = entity;
			this.label = label;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			if (o == null) throw new NullPointerException();
			if (!(o instanceof EntityWrapper)) throw new ClassCastException();
			EntityWrapper anotherEntity = (EntityWrapper) o;
			return this.getLabel().compareTo(anotherEntity.getLabel());
		}
		
		/**
		 * Gets the entity.
		 * 
		 * @return the entity
		 */
		public DataMartEntity getEntity() {
			return entity;
		}
		
		/**
		 * Sets the entity.
		 * 
		 * @param entity the new entity
		 */
		public void setEntity(DataMartEntity entity) {
			this.entity = entity;
		}
		
		/**
		 * Gets the label.
		 * 
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		
		/**
		 * Sets the label.
		 * 
		 * @param label the new label
		 */
		public void setLabel(String label) {
			this.label = label;
		}
		
	}
}
