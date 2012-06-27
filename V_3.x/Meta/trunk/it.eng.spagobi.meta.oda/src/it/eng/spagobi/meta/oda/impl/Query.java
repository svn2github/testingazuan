/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.oda.impl;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.statement.IStatement;
import it.eng.qbe.statement.QbeDatasetFactory;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation class of IQuery for an ODA runtime driver.
 * <br>
 * For demo purpose, the auto-generated method stubs have
 * hard-coded implementation that returns a pre-defined set
 * of meta-data and query results.
 * A custom ODA driver is expected to implement own data source specific
 * behavior in its place. 
 * 
 * @authors  Andrea Gioia (andrea.gioia@eng.it)
 */
public class Query implements IQuery {
	private static Logger logger = LoggerFactory.getLogger(Query.class);

	private int m_maxRows;
	private String queryString;
	IDataSource datasource;
	private it.eng.qbe.query.Query query;
	IDataStore datsStore;

	public Query(IDataSource datasource) {
		this.datasource = datasource;
		m_maxRows = 101;
		logger.debug("ODA Query created, datasource is [{}]",this.datasource);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	public void prepare(String queryText) throws OdaException {
		logger.debug("Open query "+ queryText);
		logger.debug("ODA Query prepare");
		this.queryString = queryText;
		if(queryText!=null){
			query = new it.eng.qbe.query.Query();
			logger.debug("Datasource is [{}]",datasource);
			
			try {
				logger.debug("Deserializing the query with text: [{}] ....",queryText);
				query =  SerializerFactory.getDeserializer("application/json").deserializeQuery(queryText, datasource) ;
				logger.debug("Deserialized query!");
			} catch (Exception e) {
				logger.error("Error deserializing the query with text: [{}]",queryText);
				new OdaException("Error deserializing the query with text: [{}]",queryText);
			}

			IStatement statement = datasource.createStatement(query);
			statement.setMaxResults(this.m_maxRows);
			IDataSet datsSet = QbeDatasetFactory.createDataSet(statement);

			try {
				logger.debug("Executing query ");
				datsSet.loadData(0,m_maxRows,m_maxRows);
				datsStore = datsSet.getDataStore() ;
				long rows = datsSet.getDataStore().getRecordsCount();
				if(m_maxRows>rows){
					m_maxRows=new Long(rows).intValue()-2;
				}
				logger.debug("Query executed");
			} catch (Throwable e) {
				logger.error("Error executing the query with text: [{}]",queryText);
				new OdaException("Error executing the query with text: [{}]",queryText);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
		// do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	public void close() throws OdaException {
		query = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException {

		return datsStore!=null? new ResultSetMetaData(datsStore.getMetaData()): null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws OdaException {

		return new ResultSet( datsStore , m_maxRows);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		// do nothing; assumes no data set query property
		logger.debug("Query.setProperty([{}],[{}]) STUB setting properties",name, value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
		m_maxRows = m_maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	public int getMaxRows() throws OdaException
	{
		return m_maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters() throws OdaException
	{
		// TODO Auto-generated method stub
		// only applies to input parameter
		logger.debug("Query.clearInParameters STUB");
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setInt([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setInt([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
	 */
	public void setDouble( String parameterName, double value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setDouble([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setDouble([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setBigDecimal([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setBigDecimal([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
	 */
	public void setString( String parameterName, String value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setString([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setString([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setDate([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setDate([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setTime([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setTime([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setTimestamp([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setTimestamp([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String, boolean)
	 */
	public void setBoolean( String parameterName, boolean value )
	throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setBoolean([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
	 */
	public void setBoolean( int parameterId, boolean value )
	throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setBoolean([{}],[{}]) STUB setting properties",parameterId, value);  
		// only applies to input parameter
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String, java.lang.Object)
	 */
	public void setObject( String parameterName, Object value )
	throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setObject([{}],[{}]) STUB setting properties",parameterName, value);
		// only applies to named input parameter
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int, java.lang.Object)
	 */
	public void setObject( int parameterId, Object value ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setObject([{}],[{}]) STUB setting properties",parameterId, value);
		// only applies to input parameter
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
	 */
	public void setNull( String parameterName ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setNull([{}],[{}]) STUB setting properties",parameterName);
		// only applies to named input parameter
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
	 */
	public void setNull( int parameterId ) throws OdaException
	{
		// TODO Auto-generated method stub
		logger.debug("setNull([{}]) STUB setting properties",parameterId);
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.String)
	 */
	public int findInParameter( String parameterName ) throws OdaException
	{
		// TODO Auto-generated method stub
		// only applies to named input parameter
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException
	{
		/* TODO Auto-generated method stub
		 * Replace with implementation to return an instance 
		 * based on this prepared query.
		 */
		return new ParameterMetaData();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
		// only applies to sorting, assumes not supported
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec() throws OdaException
	{
		// only applies to sorting
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse.datatools.connectivity.oda.spec.QuerySpecification)
	 */
	public void setSpecification( QuerySpecification querySpec )
	throws OdaException, UnsupportedOperationException
	{
		// assumes no support
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
	 */
	public QuerySpecification getSpecification()
	{
		// assumes no support
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
	 */
	public String getEffectiveQueryText() {
		return this.queryString;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
	 */
	public void cancel() throws OdaException, UnsupportedOperationException
	{
		// assumes unable to cancel while executing a query
		throw new UnsupportedOperationException();
	}

}
