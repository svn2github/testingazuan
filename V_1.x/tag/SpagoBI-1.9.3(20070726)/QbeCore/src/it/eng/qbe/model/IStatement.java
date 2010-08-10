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

import it.eng.spago.base.SourceBean;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Andrea Gioia
 *
 */
public interface IStatement {
	public void setQuery(IQuery query);
	public IQuery getQuery();
	public String getQueryString();
	public String getQueryString(IQuery query, Properties parameters) throws IOException;
	public IDataMartModel getDataMartModel();
	public int getFetchSize();
	public void setFetchSize(int fetchSize);
	public int getMaxResults();
	public void setMaxResults(int maxResults);
	public int getOffset();
	public void setOffset(int offset);
	public void setParameters(Properties parameters);
	
	public SourceBean execute() throws Exception ;
	public SourceBean execute(int offset) throws Exception;
	public SourceBean execute(int offset, int fetchSize) throws Exception;		
	public SourceBean execute(IQuery query, Properties parameters, int offset, int fetchSize, int maxResults) throws Exception;
	public SourceBean executeWithPagination(int pageNumber, int pageSize) throws Exception;	
	public SourceBean executeWithPagination(IQuery query, Properties parameters,  int pageNumber, int pageSize, int maxResults) throws Exception;
	
}
