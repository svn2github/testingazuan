/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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


package it.eng.spagobi.tools.dataset.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.utilities.DataSourceUtilities;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 * @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */
public class QueryDataSet extends DataSetConfig {
    private String query=null;
    private static transient Logger logger=Logger.getLogger(QueryDataSet.class);
    private DataSource dataSource=null;
    
    /**
     * Gets the query.
     * 
     * @return the query
     */
    public String getQuery() {
        return query;
    }
    
    /**
     * Sets the query.
     * 
     * @param query the new query
     */
    public void setQuery(String query) {
        this.query = query;
    }
    
    
    
    /**
     * Instantiates a new query data set.
     */
    public QueryDataSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Instantiates a new query data set.
	 * 
	 * @param a the a
	 */
	public QueryDataSet(DataSetConfig a) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
	}
    
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * Sets the data source.
     * 
     * @param dataSource the new data source
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    
	/**
	 * Gets the list of names of the profile attributes required.
	 * 
	 * @return list of profile attribute names
	 * 
	 * @throws Exception the exception
	 */
	public List getProfileAttributeNames() throws Exception {
		List names = new ArrayList();
		String query = getQuery();
		while(query.indexOf("${")!=-1) {
			int startind = query.indexOf("${");
			int endind = query.indexOf("}", startind);
			String attributeDef = query.substring(startind + 2, endind);
			if(attributeDef.indexOf("(")!=-1) {
				int indroundBrack = query.indexOf("(", startind);
				String nameAttr = query.substring(startind+2, indroundBrack);
				names.add(nameAttr);
			} else {
				names.add(attributeDef);
			}
			query = query.substring(endind);
		}
		return names;
	}

	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.dataset.bo.DataSet#getDataSetResult(it.eng.spago.security.IEngUserProfile)
	 */
	public String getDataSetResult(IEngUserProfile profile) throws Exception {
		logger.debug("IN");
		String statement=getQuery();
		statement = GeneralUtilities.substituteProfileAttributesInString(statement, profile);
		String resStr = null;
		
		//gets connection
		DataSourceUtilities dsUtil = new DataSourceUtilities();
		String datasourceL=dataSource.getLabel();
		
		SourceBean rowsSourceBean = (SourceBean) executeSelect(datasourceL, statement);
		resStr=rowsSourceBean.toXML();
		logger.debug("OUT");
		return resStr;
	}
	
	/**
	 * Execute select.
	 * 
	 * @param datasource the datasource
	 * @param statement the statement
	 * 
	 * @return the object
	 * 
	 * @throws EMFInternalError the EMF internal error
	 */
	public static Object executeSelect(String datasource, String statement) throws EMFInternalError {
		logger.debug("IN");
		Object result = null;
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		try {

			DataSourceUtilities dsUtil = new DataSourceUtilities();
			Connection conn = dsUtil.getConnection(datasource); 
			dataConnection = dsUtil.getDataConnection(conn);

			sqlCommand = dataConnection.createSelectCommand(statement);
			dataResult = sqlCommand.execute();
			ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult
			.getDataObject();
			//List temp = Arrays.asList(scrollableDataResult.getColumnNames());
			//columnsNames.addAll(temp);
			result = scrollableDataResult.getSourceBean();
		} finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		}
		logger.debug("OUT");
		return result;
	}

	
	public SpagoBiDataSet toSpagoBiDataSet() {
		SpagoBiDataSet sbd = new SpagoBiDataSet();
		sbd.setQuery(query);
		sbd.setLabel(getLabel());
		sbd.setName(getName());
		sbd.setParameters(getParameters());
		sbd.setDescription(getDescription());
		sbd.setType("SbiQueryDataSet");
		sbd.setDataSource(dataSource.toSpagoBiDataSource());
		return sbd;
	}

	
	
	
    
}
