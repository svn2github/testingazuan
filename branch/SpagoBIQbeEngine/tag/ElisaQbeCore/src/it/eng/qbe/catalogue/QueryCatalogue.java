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
			query.setId( "id" + (++counter) );
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
	
	
	/*
	public void refreshQuery(Query query, QueryMeta meta) {
		Assert.assertNotNull(query, "Impossibble refresh query. Input parameters query cannot be null");
		Assert.assertNotNull(query.getId(), "Impossibble refresh query. Query id cannot be null");
		Assert.assertNotNull(getQuery(query.getId()), "Impossibble refresh query. A query with id [" + query.getId() + "] does not exist in the catalog");
		if(meta != null) {
			Assert.assertTrue(query.getId().equals(meta.getId()), "Impossibble refresh query. The query id [" + query.getId() + "] does not match with the meta id [" + meta.getId()+ "]");
			this.meta.put(meta.getId(), meta);
		}
		this.queries.put( query.getId(), query);
		
		
	}
	
	
	private String addQuery(Query query, QueryMeta meta) {
		String id = "id" + (++counter);
		query.setId(id);
		this.queries.put( query.getId(), query);
		
		if(meta == null) {
			meta = new QueryMeta();
			meta.setId( query.getId() );
		}
		
		if(meta.getName() == null) {
			meta.setName( "query-" + query.getId() );
		}
		
		this.meta.put( query.getId(), meta);
		return id;
	}
	*/
	
}
