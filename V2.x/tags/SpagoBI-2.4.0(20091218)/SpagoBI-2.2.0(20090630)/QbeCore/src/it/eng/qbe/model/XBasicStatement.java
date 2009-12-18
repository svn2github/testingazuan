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
package it.eng.qbe.model;

import it.eng.qbe.newquery.Query;

import java.io.IOException;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicStatement.
 * 
 * @author Andrea Gioia
 */
public abstract class  XBasicStatement implements XIStatement{

	/** The data mart model. */
	IDataMartModel dataMartModel;
	
	/** The query. */
	Query query;
	
	/** The parameters. */
	Map parameters;
	
	/** The query string. */
	String queryString;
	
	/** The max results. */
	int maxResults;
	
	/** The fetch size. */
	int fetchSize;
	
	/** The offset. */
	int offset;
	
	/**
	 * Instantiates a new basic statement.
	 * 
	 * @param dataMartModel the data mart model
	 */
	protected XBasicStatement(IDataMartModel dataMartModel) {
		this.dataMartModel = dataMartModel;
	}
	
	/**
	 * Instantiates a new basic statement.
	 * 
	 * @param dataMartModel the data mart model
	 * @param query the query
	 */
	protected XBasicStatement(IDataMartModel dataMartModel, Query query) {
		this.dataMartModel = dataMartModel;
		this.query = query;
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#getQueryString()
	 */
	public String getQueryString() {
		if(queryString == null) {
			try {
				queryString = getQueryString(query, parameters);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return queryString;
	}	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#getDataMartModel()
	 */
	public IDataMartModel getDataMartModel() {
		return dataMartModel;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#getQuery()
	 */
	public Query getQuery() {
		return query;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#setQuery(it.eng.qbe.query.IQuery)
	 */
	public void setQuery(Query query) {
		this.query = query;
		this.queryString = null;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#getFetchSize()
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#setFetchSize(int)
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#getMaxResults()
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#setMaxResults(int)
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#getOffset()
	 */
	public int getOffset() {
		return offset;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#setOffset(int)
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#setParameters(java.util.Properties)
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

}
