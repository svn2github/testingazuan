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
package it.eng.qbe.catalogue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import it.eng.qbe.query.Query;


/**
 * The Class SingleDataMartWizardObjectSourceBeanImpl.
 */
public class QueryCatalogue {
	
	Map queries;
	Map meta;
	
	long counter;
	
	public QueryCatalogue() {
		this.queries = new HashMap();
		this.meta = new HashMap();
		this.counter = 0;
	}
	
	/*
	 * Externalize the id creation strategy is little bit to mutch for the moment. 
	 * If you want to modify this method keep in mind by the way that it have a dependence with 
	 * the statment class.Infact the id is used as a prefix to all entity aliases so it must not
	 * broke the valid alias sintax (avoid spaces and special char) 
	 * 
	 * @todo id generation must be consistent also across different execution. Add an initial offset
	 * to setup properly the counter in order to not override preloaded queries.
	 */
	public String getNextValidId() {
		return "q" + (++counter);
	}
	
	public Set getIds() {
		return new HashSet(queries.keySet());
	}
	
	public boolean containsQuery(String id) {
		return this.queries.containsKey(id);
	}
	
	public Query getQuery(String id) {
		return (Query)this.queries.get(id);
	}
	
	public String addQuery(Query query) {
		Iterator subqueriesIterator;
		Query subquery;
		
		if(query.getId() == null) {
			query.setId( getNextValidId() );
		}
		
		if(query.getName() == null) {
			query.setName( "query-" + query.getId() );
		}
		
		if(query.getDescription() == null) {
			query.setDescription( "query-" + query.getId() );
		}
		queries.put( query.getId(), query);
		
		// recursively add (or update) all subqueries to the catalogue
		subqueriesIterator = query.getSubqueryIds().iterator();		
		while(subqueriesIterator.hasNext()) {
			String id = (String)subqueriesIterator.next();
			subquery = query.getSubquery(id);
			addQuery(subquery);
		}
		
		return query.getId();
	}
	
	public Query removeQuery(String id) {
		Query query;
		Query parentQuery;
		Iterator subqueriesIterator;
		Query subquery;
		
		query = (Query)queries.remove(id);			
		removeSubqueries(query);
		
		if(query.hasParentQuery()) {
			parentQuery = query.getParentQuery();
			parentQuery.removeSubquery(query.getId());
		}
		
		return query;
	}
	
	private void removeSubqueries(Query query) {
		Iterator subqueriesIterator;
		Query subquery;
		
		// recursively remove all subqueries from the catalogue
		subqueriesIterator = query.getSubqueryIds().iterator();		
		while(subqueriesIterator.hasNext()) {
			String id = (String)subqueriesIterator.next();
			subquery = query.getSubquery(id);
			queries.remove(subquery.getId());
			removeSubqueries(subquery);
		}
	}

	public Set getAllQueries(boolean includeSubqueries) {
		Set results = null;
		
		if(includeSubqueries) {
			results = queries.entrySet();
		} else {
			results = new HashSet();
			Iterator it = queries.keySet().iterator();
			while(it.hasNext()) {
				String queryId = (String)it.next();
				Query query = (Query)queries.get(queryId);
				if(!query.hasParentQuery()) {
					results.add(query);
				}
			}
		}
		
		return results;
	}
	
}
