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
package it.eng.spagobi.bo.lov;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.utilities.GeneralUtilities;

import java.io.Serializable;


/**
 * Defines the <code>QueryDetail</code> objects. This object is used to store 
 * Query Wizard detail information.
 */


public class QueryDetail  implements ILovDetail  {
	
	private String connectionName= "" ;
	private String visibleColumns = "";
	private String valueColumns = "";
	private String descriptionColumns = "";
	private String queryDefinition = "";
	private String invisibleColumns = "";
	
	public QueryDetail() { }
	
	public QueryDetail(String dataDefinition) throws SourceBeanException {
		loadFromXML(dataDefinition);
	}
	
	public void loadFromXML (String dataDefinition) throws SourceBeanException {
		dataDefinition.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		
		SourceBean connection = (SourceBean)source.getAttribute("CONNECTION");
		String connectionName = connection.getCharacters(); 
		
		SourceBean statement = (SourceBean)source.getAttribute("STMT");
		String queryDefinition = statement.getCharacters();
		
		SourceBean valCol = (SourceBean)source.getAttribute("VALUE-COLUMN");
		String valueColumns = valCol.getCharacters();
		
		SourceBean visCol = (SourceBean)source.getAttribute("VISIBLE-COLUMNS");
		String visibleColumns = visCol.getCharacters();
		
		SourceBean invisCol = (SourceBean)source.getAttribute("INVISIBLE-COLUMNS");
		String invisibleColumns = "";
		// compatibility control (versions till 1.9RC does not have invisible columns definition)
		if (invisCol != null) invisibleColumns = invisCol.getCharacters();
		
		SourceBean descCol = (SourceBean)source.getAttribute("DESCRIPTION-COLUMN");
		String descriptionColumns = null;
		// compatibility control (versions till 1.9.1 does not have description columns definition)
		if (descCol != null) descriptionColumns = descCol.getCharacters();
		else descriptionColumns = valueColumns;
		
		
		
		setConnectionName(connectionName);
		setQueryDefinition(queryDefinition);
		setValueColumns(valueColumns);
		setDescriptionColumns(descriptionColumns);
		setVisibleColumns(visibleColumns);
		setInvisibleColumns(invisibleColumns);
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
			         "<STMT>"+this.getQueryDefinition() + "</STMT>" +
				     "<VALUE-COLUMN>"+this.getValueColumns()+"</VALUE-COLUMN>" +
				     "<DESCRIPTION-COLUMN>"+this.getDescriptionColumns()+"</DESCRIPTION-COLUMN>" +
				     "<VISIBLE-COLUMNS>"+this.getVisibleColumns()+"</VISIBLE-COLUMNS>" +
				     "<INVISIBLE-COLUMNS>"+(this.getInvisibleColumns() != null ? this.getInvisibleColumns() : "") +"</INVISIBLE-COLUMNS>" +
				     "</QUERY>";
		return XML;
	}
	
	public String getLovResult(IEngUserProfile profile) throws Exception {
		String resStr = null;
			
		String pool = getConnectionName();
		String statement = getQueryDefinition();
		DataConnectionManager dataConnectionManager = DataConnectionManager.getInstance();
		DataConnection dataConnection   = dataConnectionManager.getConnection(pool);
		statement = GeneralUtilities.substituteProfileAttributesInString(statement, profile);
		SQLCommand sqlCommand = dataConnection.createSelectCommand(statement);
		DataResult dataResult = sqlCommand.execute();
        ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult.getDataObject();
		SourceBean result = scrollableDataResult.getSourceBean();
		
		SourceBean valueColumnSB = buildSourceBean("VALUE-COLUMN", getValueColumns());
		SourceBean descriptionColumnSB = buildSourceBean("DESCRIPTION-COLUMN", getDescriptionColumns());
		SourceBean visibleColumnsSB = buildSourceBean("VISIBLE-COLUMNS", getVisibleColumns());
		SourceBean invisibleColumnsSB = buildSourceBean("INVISIBLE-COLUMNS", getInvisibleColumns());
		
		result.setAttribute(valueColumnSB);
		result.setAttribute(descriptionColumnSB);
		result.setAttribute(visibleColumnsSB);
		result.setAttribute(invisibleColumnsSB);
				
		resStr = result.toXML(false);
		resStr = resStr.trim();
		if(resStr.startsWith("<?")) {
			resStr = resStr.substring(2);
			int indFirstTag = resStr.indexOf("<");
			resStr = resStr.substring(indFirstTag);
		}
		Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		return resStr;
	}
	
	private SourceBean buildSourceBean(String name, String value) throws SourceBeanException {
		SourceBean sb = null;
		sb = SourceBean.fromXMLString("<"+name+">" + (value != null ? value : "") + "</"+name+">");
		return sb;
	}
	
	
	
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
	 * @return Returns the invisibleColumns.
	 */
	public String getInvisibleColumns() {
		return invisibleColumns;
	}
	/**
	 * @param invisibleColumns The invisibleColumns to set.
	 */
	public void setInvisibleColumns(String invisibleColumns) {
		this.invisibleColumns = invisibleColumns;
	}
	
	
	
	
	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods
	 * in order to obtain the source <code>QueryDetail</code> objects whom XML has been 
	 * built. 
	 * 
	 * @param dataDefinition	The XML input String
	 * @return The corrispondent <code>QueryDetail</code> object
	 * @throws SourceBeanException If a SourceBean Exception occurred
	 */
	public static QueryDetail fromXML (String dataDefinition) throws SourceBeanException {
		return new QueryDetail(dataDefinition);
	}

	public String getDescriptionColumns() {
		return descriptionColumns;
	}

	public void setDescriptionColumns(String descriptionColumns) {
		this.descriptionColumns = descriptionColumns;
	}
	
	
}