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
package it.eng.qbe.model;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Andrea Gioia
 *
 */
public abstract class  BasicStatement implements IStatement{

	IDataMartModel dataMartModel;
	IQuery query;
	Properties parameters;
	String queryString;
	int maxResults;
	int fetchSize;
	int offset;
	
	protected BasicStatement(IDataMartModel dataMartModel) {
		this.dataMartModel = dataMartModel;
	}
	
	protected BasicStatement(IDataMartModel dataMartModel, IQuery query) {
		this.dataMartModel = dataMartModel;
		this.query = query;
	}
	
	
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
	
	public IDataMartModel getDataMartModel() {
		return dataMartModel;
	}
	
	public IQuery getQuery() {
		return query;
	}
	
	public void setQuery(IQuery query) {
		this.query = query;
		this.queryString = null;
	}
	
	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Properties getParameters() {
		return parameters;
	}

	public void setParameters(Properties parameters) {
		this.parameters = parameters;
	}

}
