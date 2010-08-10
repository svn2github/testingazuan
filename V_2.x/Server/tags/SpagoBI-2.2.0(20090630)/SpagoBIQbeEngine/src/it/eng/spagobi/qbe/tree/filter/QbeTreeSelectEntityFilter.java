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
import it.eng.qbe.query.IQuery;
import it.eng.qbe.wizard.EntityClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeTreeSelectEntityFilter.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QbeTreeSelectEntityFilter extends ComposableQbeTreeEntityFilter{
	
	/** The query. */
	private IQuery query;

	/**
	 * Instantiates a new qbe tree select entity filter.
	 */
	public QbeTreeSelectEntityFilter() {
		super();
		
	}
	
	/**
	 * Instantiates a new qbe tree select entity filter.
	 * 
	 * @param parentFilter the parent filter
	 * @param query the query
	 */
	public QbeTreeSelectEntityFilter(IQbeTreeEntityFilter parentFilter, IQuery query) {
		super(parentFilter);
		setQuery(query);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.filter.ComposableQbeTreeEntityFilter#filter(it.eng.qbe.model.IDataMartModel, java.util.List)
	 */
	public List filter(IDataMartModel datamartModel, List entities) {
		List list = new ArrayList();
		
		Set selectedEntityNames = new HashSet();	
		for (Iterator it = getQuery().getEntityClassesIterator(); it.hasNext(); ){
			EntityClass entityClass  = (EntityClass)it.next();	
			selectedEntityNames.add( entityClass.getClassName() );
		}
		
		for (Iterator it = entities.iterator(); it.hasNext(); ){
			DataMartEntity entity  = (DataMartEntity)it.next();	
			if( entity.getParent() != null 
					|| selectedEntityNames.contains(entity.getType()) ) {
				list.add(entity);
			}			
		}		
		
		return list;
	}

	/**
	 * Gets the query.
	 * 
	 * @return the query
	 */
	protected IQuery getQuery() {
		return query;
	}

	/**
	 * Sets the query.
	 * 
	 * @param query the new query
	 */
	protected void setQuery(IQuery query) {
		this.query = query;
	}
}
