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
package it.eng.spagobi.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.dao.jdbc.DomainDAOImpl;
import it.eng.spagobi.bo.dao.jdbc.EngineDAOImpl;

/**
 * Defines the <code>QueryDetail</code> objects. This object is used to store 
 * Query Wizard detail information.
 * 
 * @author sulis
 */


public class QueryDetail  implements Serializable  {
	
	private String connectionName= "" ;
	private String visibleColumns = "";
	private String valueColumns = "";
	private String queryDefinition = "";
	/**
	 * @return Returns the connectionName.
	 */
	public String getConnectionName() {
		return connectionName;
	}
	/**
	 * @param connectionName The connectionName to set.
	 */
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	/**
	 * @return Returns the queryDefinition.
	 */
	public String getQueryDefinition() {
		return queryDefinition;
	}
	/**
	 * @param queryDefinition The queryDefinition to set.
	 */
	public void setQueryDefinition(String queryDefinition) {
		this.queryDefinition = queryDefinition;
	}
	/**
	 * @return Returns the valueColumns.
	 */
	public String getValueColumns() {
		return valueColumns;
	}
	/**
	 * @param valueColumns The valueColumns to set.
	 */
	public void setValueColumns(String valueColumns) {
		this.valueColumns = valueColumns;
	}
	/**
	 * @return Returns the visibleColumns.
	 */
	public String getVisibleColumns() {
		return visibleColumns;
	}
	/**
	 * @param visibleColumns The visibleColumns to set.
	 */
	public void setVisibleColumns(String visibleColumns) {
		this.visibleColumns = visibleColumns;
	}
	
	/**
	 * Loads the XML string defined by a <code>QueryDetail</code> object. The object
	 * gives us all XML field values. Once obtained, the XML represents the data 
	 * definition for a query Input Type Value LOV object. 
	 * 
	 * @return The XML output String
	 */
	public String toXML () { 
		String XML = "<QUERY>" +
				     "<CONNECTION>"+this.getConnectionName()+"</CONNECTION>" +
			         "<STMT>"+this.getQueryDefinition()+
				     "</STMT>" +
				     "<VALUE-COLUMN>"+this.getValueColumns()+"</VALUE-COLUMN>" +
				     "<VISIBLE-COLUMNS>"+this.getVisibleColumns()+"</VISIBLE-COLUMNS>" +
				     "</QUERY>";
		return XML;
	}
	
	
	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods
	 * in order to obtain the source <code>QueryDetail</code> objects whom XML has been 
	 * built. 
	 * 
	 * @param dataDefinition1	The XML input String
	 * @return The corrispondent <code>QueryDetail</code> object
	 * @throws SourceBeanException If a SourceBean Exception occurred
	 */
	public static QueryDetail fromXML (String dataDefinition1) throws SourceBeanException {
		dataDefinition1.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition1);
		SourceBean connection = (SourceBean)source.getAttribute("CONNECTION");
		String connectionName = connection.getCharacters(); 
		SourceBean statement = (SourceBean)source.getAttribute("STMT");
		String queryDefinition = statement.getCharacters();
		SourceBean valCol = (SourceBean)source.getAttribute("VALUE-COLUMN");
		String valueColumns = valCol.getCharacters();
		SourceBean visCol = (SourceBean)source.getAttribute("VISIBLE-COLUMNS");
		String visibleColumns = visCol.getCharacters();
		QueryDetail query = new QueryDetail();
		query.setConnectionName(connectionName);
		query.setQueryDefinition(queryDefinition);
		query.setValueColumns(valueColumns);
		query.setVisibleColumns(visibleColumns);
		return query;
	}
	
	
}