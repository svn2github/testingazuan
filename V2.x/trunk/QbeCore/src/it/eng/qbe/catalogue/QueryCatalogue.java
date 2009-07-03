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
import java.util.Map;

import it.eng.qbe.newquery.Query;
import it.eng.qbe.newquery.QueryMeta;



// TODO: Auto-generated Javadoc
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
	
	public String addQuery(Query query, QueryMeta meta) {
		String id = "id" + (++counter);
		query.setId(id);
		this.queries.put( query.getId(), query);
		this.meta.put( query.getId(), meta);
		return id;
	}
	
	public Query getQuery(String id) {
		return (Query)this.queries.get(id);
	}
	
	public QueryMeta getQueryMeta(String id) {
		return (QueryMeta)this.meta.get(id);
	}
}
