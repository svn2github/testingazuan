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
import it.eng.spago.base.SourceBean;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Interface IStatement.
 * 
 * @author Andrea Gioia
 */
public interface XIStatement {
	
	/**
	 * Sets the query.
	 * 
	 * @param query the new query
	 */
	public void setQuery(Query query);
	
	/**
	 * Gets the query.
	 * 
	 * @return the query
	 */
	public Query getQuery();
	
	/**
	 * Gets the query string.
	 * 
	 * @return the query string
	 */
	public String getQueryString();
	
	/**
	 * Gets the query string.
	 * 
	 * @param query the query
	 * @param parameters the parameters
	 * 
	 * @return the query string
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getQueryString(Query query, Map parameters) throws IOException;
	
	/**
	 * Gets the data mart model.
	 * 
	 * @return the data mart model
	 */
	public IDataMartModel getDataMartModel();
	
	/**
	 * Gets the fetch size.
	 * 
	 * @return the fetch size
	 */
	public int getFetchSize();
	
	/**
	 * Sets the fetch size.
	 * 
	 * @param fetchSize the new fetch size
	 */
	public void setFetchSize(int fetchSize);
	
	/**
	 * Gets the max results.
	 * 
	 * @return the max results
	 */
	public int getMaxResults();
	
	/**
	 * Sets the max results.
	 * 
	 * @param maxResults the new max results
	 */
	public void setMaxResults(int maxResults);
	
	/**
	 * Gets the offset.
	 * 
	 * @return the offset
	 */
	public int getOffset();
	
	/**
	 * Sets the offset.
	 * 
	 * @param offset the new offset
	 */
	public void setOffset(int offset);
	
	/**
	 * Sets the parameters.
	 * 
	 * @param parameters the new parameters
	 */
	public void setParameters(Map parameters);
	
	/**
	 * Execute.
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean execute() throws Exception ;
	
	/**
	 * Execute.
	 * 
	 * @param offset the offset
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean execute(int offset) throws Exception;
	
	/**
	 * Execute.
	 * 
	 * @param offset the offset
	 * @param fetchSize the fetch size
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean execute(int offset, int fetchSize) throws Exception;		
	
	/**
	 * Execute.
	 * 
	 * @param query the query
	 * @param parameters the parameters
	 * @param offset the offset
	 * @param fetchSize the fetch size
	 * @param maxResults the max results
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean execute(Query query, Map parameters, int offset, int fetchSize, int maxResults) throws Exception;
	
	/**
	 * Execute with pagination.
	 * 
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean executeWithPagination(int pageNumber, int pageSize) throws Exception;	
	
	/**
	 * Execute with pagination.
	 * 
	 * @param query the query
	 * @param parameters the parameters
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param maxResults the max results
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean executeWithPagination(Query query, Map parameters,  int pageNumber, int pageSize, int maxResults) throws Exception;
	
	/**
	 * Execute with pagination.
	 * 
	 * @param offset the offset
	 * @param fetchSize the fetch size
	 * @param maxResults the max results
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean executeWithPagination(int offset, int fetchSize, int maxResults) throws Exception;
	
}
