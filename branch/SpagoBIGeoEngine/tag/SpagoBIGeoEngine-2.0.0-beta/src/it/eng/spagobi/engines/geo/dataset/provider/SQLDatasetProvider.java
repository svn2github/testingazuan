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
package it.eng.spagobi.engines.geo.dataset.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.dataset.DataSet;
import it.eng.spagobi.engines.geo.dataset.provider.configurator.SQLDatasetProviderConfigurator;
import it.eng.spagobi.engines.geo.datasource.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLDatasetProvider.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class SQLDatasetProvider extends AbstractDatasetProvider {
	 	
	/** The data source. */
	private DataSource dataSource;	
	
	/** The query. */
	private String query;
	
		
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SQLDatasetProvider.class);
	

    /**
     * Constructor.
     */
    public SQLDatasetProvider() {
        super();
    }
    
    /* (non-Javadoc)
     * @see it.eng.spagobi.engines.geo.dataset.provider.AbstractDatasetProvider#init(java.lang.Object)
     */
    public void init(Object conf) throws GeoEngineException {
		super.init(conf);
		SQLDatasetProviderConfigurator.configure( this, getConf() );
	}
    
    
    
    
    
    
    /* (non-Javadoc)
     * @see it.eng.spagobi.engines.geo.dataset.provider.AbstractDatasetProvider#getDataSet()
     */
    public DataSet getDataSet() throws GeoEngineException {

    	DataSet dataSet = new DataSet();
        
    	SQLCommand cmdSelect = null;
        DataResult dr = null;
        String aggregateQuery = getAggreagteQuery();
        
        Connection connection = null;
        
    	
        
        
        try {
        	dataSet.setTargetFeatureName( getSelectedLevel().getFeatureName() ); 
            
        	String columnid = getSelectedLevel().getColumnId();
            
        	
        	
        	String[] measureColumnNames = (String[])getMetaData().getMeasureColumnNames().toArray(new String[0]);
     
        	connection = getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute(aggregateQuery);
            ResultSet resultSet = statement.getResultSet();
                           
            
            HashMap orderedKpiValuesMap = new HashMap();                 
            for(int i = 0; i < measureColumnNames.length; i++) {
            	orderedKpiValuesMap.put(measureColumnNames[i], new TreeSet());
            }           
            
            Map values = new HashMap();
            Map attributes = null;
            Map links = new HashMap();
          //  resultSet.beforeFirst();
            while(resultSet.next()) {
            	String id = resultSet.getString(resultSet.findColumn(columnid));
            	if((id==null) || (id.trim().equals(""))) {
            		continue;
            	}
            	
            	attributes = new HashMap();
            	for(int i = 0; i < measureColumnNames.length; i++) {
                	String value = resultSet.getString(resultSet.findColumn(measureColumnNames[i]));
                	if((value==null) || (value.trim().equals(""))) {
                		continue;
                	}
                	attributes.put(measureColumnNames[i], value);
                	
                	((Set)orderedKpiValuesMap.get(measureColumnNames[i])).add(new Double(value));
                	
            	}
            	
            	values.put(id, attributes);           	
            	
                String link = getSelectedLevel().getLink().toString(resultSet, this.getEnv());
                links.put(id, link);
            	
            }
            dataSet.setValues(values);
            dataSet.setLinks(links);
            dataSet.setKpiNames(measureColumnNames);
            dataSet.setSelectedKpi(0);
            dataSet.setOrderedKpiValuesMap(orderedKpiValuesMap);
            
            
            
        } catch (SQLException e) {  
        	logger.error("Impossible to execute query: " + aggregateQuery, e);
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String rootCause = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			String description = "Impossible to execute query: " + aggregateQuery;
			description += "<br><br>The root cause of the error is: " + rootCause;
			List hints = new ArrayList();
			hints.add("Check if your database connection is properly configured: " + getDataSource()!=null?getDataSource().toString():"NULL");
			hints.add("Check if your base query is correct (" + query + ")");
			hints.add("Check if the generated query is correct (" + aggregateQuery + ")");
			hints.add("Check if the configuration of hierarchies and of metadata is correct");
			throw new GeoEngineException("Database error", description, hints, e);
        } catch (NamingException e) {
        	logger.error("Impossible to execute query: " + aggregateQuery, e);
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String rootCause = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			String description = "Impossible to get connection instance from JNDI server: " + getDataSource()!=null?getDataSource().toString():"NULL" ;
			description += "<br>The root cause of the error is: " + rootCause;
			List hints = new ArrayList();
			hints.add("Check if the jndi name is correct: " + getDataSource()!=null?getDataSource().getJndiName():"NULL");
			hints.add("Check if the jndi resorce is properly bound in this context");
			hints.add("Check if JNDI server had encountered an error during resource " +
					"instantiation, maybe due to a wrong connection definition inside server configuration");

			throw new GeoEngineException("Database error", description, hints, e);
		} catch (ClassNotFoundException e) {
			logger.error("Impossible to execute query: " + aggregateQuery, e);
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String rootCause = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			String description = "Impossible to find JDBC driver class for datasource " + getDataSource()!=null?getDataSource().toString():"NULL" ;
			description += "<br>The root cause of the error is: " + rootCause;
			List hints = new ArrayList();
			hints.add("Check if the driver name is correct: " + getDataSource()!=null?getDataSource().getDriver():"NULL");
			hints.add("Check if the jdbc driver class is in the class path");
			throw new GeoEngineException("Database error", description, hints, e);
		} finally {
        	if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.warn("Impossible to close connection to: " + getDataSource().toString(), e);
				}
        	}
        }
        return dataSet;
    }
    
    

	    
	    /**
    	 * Gets the dim geo query.
    	 * 
    	 * @return the dim geo query
    	 */
    	private String getDimGeoQuery() {
	    	String query = "";
	    	
	    	Hierarchy hierarchy = getSelectedHierarchy();
	    	String baseLevelName = getMetaData().getLevelName( hierarchy.getName() );
	    	Hierarchy.Level baseLevel = hierarchy.getLevel( baseLevelName );
	    		
	    	List levels = hierarchy.getSublevels(baseLevel.getName());
	    	
	    	query += "SELECT " + baseLevel.getColumnId();
	    	for(int i = 0; i < levels.size(); i++) {
	    		Hierarchy.Level subLevel;
	    		subLevel = (Hierarchy.Level)levels.get(i);
	    		query += ", " + subLevel.getColumnId();
	    	}
	    	query += " FROM " + hierarchy.getTable();
	    	query += " GROUP BY " + baseLevel.getColumnId();
	    	for(int i = 0; i < levels.size(); i++) {
	    		Hierarchy.Level subLevel;
	    		subLevel = (Hierarchy.Level)levels.get(i);
	    		query += ", " + subLevel.getColumnId();
	    	}
	    	
	    	return query;
	    	
	    }
	    
	    
	    /**
    	 * Gets the executable query.
    	 * 
    	 * @return the executable query
    	 */
    	public String getExecutableQuery() {
			String executableQuery;
			
			executableQuery = query;
			if(executableQuery.indexOf("${") == -1) return executableQuery;
			while(executableQuery.indexOf("${")!=-1) {
				int startInd = executableQuery.indexOf("${") + 2;
				int endInd = executableQuery.indexOf("}", startInd);
				String paramName = executableQuery.substring(startInd, endInd);
				String paramValue = null;
				if( getEnv().containsKey( paramName ) ) {
					paramValue = getEnv().get(paramName).toString();
				}
				if(paramValue==null) {
					logger.error("Cannot find in service request a valid value for parameter: parameter name " + paramName);
					
					paramValue = "";
				}
				executableQuery = executableQuery.substring(0, startInd - 2) + paramValue + executableQuery.substring(endInd + 1);
			}
			return executableQuery;
		}
	    
	    /**
    	 * Gets the aggreagte query.
    	 * 
    	 * @return the aggreagte query
    	 */
    	private String getAggreagteQuery() {
	    	String aggragateQuery = null;
	    	String query = getExecutableQuery();
	    	
	    	String subQueryAlias = "t" + System.currentTimeMillis();
	    	String normalizedSubQueryAlias = "n" + System.currentTimeMillis();
	    	String dimGeoAlias = "g" + System.currentTimeMillis();
	    	
	    	Hierarchy hierarchy = getSelectedHierarchy();
	    	Hierarchy.Level level = getSelectedLevel();
	    	String baseLevelName = getMetaData().getLevelName( hierarchy.getName() );
	    	Hierarchy.Level baseLevel = hierarchy.getLevel( baseLevelName );
	    	
	    	if(hierarchy.getType().equalsIgnoreCase("custom")) {
	    		System.out.println("\nCUSTOM HIERARCHY...\n");
		    	String aggregationColumnName = level.getColumnId(); 
		    	aggragateQuery = "SELECT " + subQueryAlias + "." + aggregationColumnName + " AS " + aggregationColumnName;
		    	String[] kpiColumnNames = (String[])getMetaData().getMeasureColumnNames().toArray(new String[0]);
		    	for(int i = 0; i < kpiColumnNames.length; i++) {
		    		String aggFunc = "SUM";
		    		
		    		if(!getMetaData().getAggregationFunction(kpiColumnNames[i]).trim().equalsIgnoreCase("")) {
		    			aggFunc = getMetaData().getAggregationFunction(kpiColumnNames[i]).trim().toUpperCase();
		    		}
		    		aggragateQuery +=  ", " + aggFunc + "(" + subQueryAlias + "." + kpiColumnNames[i] + ") AS " + kpiColumnNames[i];
		    	}
		    	aggragateQuery += " \nFROM ( " + query + ") " + subQueryAlias;
		    	aggragateQuery += " \nGROUP BY " + subQueryAlias + "." + aggregationColumnName;
	    	} else {
	    		String aggregationColumnName = level.getColumnId(); 
		    	aggragateQuery = "SELECT " + dimGeoAlias + "." + aggregationColumnName + " AS " + aggregationColumnName;
		    	String[] kpiColumnNames = (String[])getMetaData().getMeasureColumnNames().toArray(new String[0]);
		    	for(int i = 0; i < kpiColumnNames.length; i++) {
		    		String aggFunc = "SUM";
		    		if(!getMetaData().getAggregationFunction(kpiColumnNames[i]).trim().equalsIgnoreCase("")) {
		    			aggFunc = getMetaData().getAggregationFunction(kpiColumnNames[i]).trim().toUpperCase();
		    		}
		    		aggragateQuery +=  ", " + aggFunc + "(" + subQueryAlias + "." + kpiColumnNames[i] + ") AS " + kpiColumnNames[i];
		    	}
		    	
		    	String normalizedSubQuery = query;
		    
		    	normalizedSubQuery ="SELECT " + normalizedSubQueryAlias + "." + getMetaData().getGeoIdColumnName( hierarchy.getName() ) +  " AS " + getMetaData().getGeoIdColumnName( hierarchy.getName() );
		    	for(int i = 0; i < kpiColumnNames.length; i++) {
		    		String aggFunc = "SUM";
		    		if(!getMetaData().getAggregationFunction(kpiColumnNames[i]).trim().equalsIgnoreCase("")) {
		    			aggFunc = getMetaData().getAggregationFunction(kpiColumnNames[i]).trim().toUpperCase();
		    		}
		    		normalizedSubQuery +=  ", " + aggFunc + "(" + normalizedSubQueryAlias + "." + kpiColumnNames[i] + ") AS " + kpiColumnNames[i];
		    	}
		    	normalizedSubQuery += " \nFROM ( " + query + ") " + normalizedSubQueryAlias;
		    	normalizedSubQuery += " \nGROUP BY " + normalizedSubQueryAlias + "." + getMetaData().getGeoIdColumnName( hierarchy.getName() );
		    	System.out.println("\nNormalized query:\n" + normalizedSubQuery);
		    	
		    	
		    	aggragateQuery += " \nFROM ( \n" + normalizedSubQuery + "\n ) " + subQueryAlias;
		    	String dimGeoQuery = getDimGeoQuery();
		    	System.out.println("\nDimGeo query:\n" + dimGeoQuery);
		    	aggragateQuery += ", (" + dimGeoQuery + ") " + dimGeoAlias;
		    	aggragateQuery += " \nWHERE " + subQueryAlias + "." + getMetaData().getGeoIdColumnName( hierarchy.getName() );
		    	aggragateQuery += " = " + dimGeoAlias + "." + baseLevel.getColumnId();
		    	aggragateQuery += " \nGROUP BY " + dimGeoAlias + "." + aggregationColumnName;
	    	}
	    	
	    	System.out.println("\nExecutable query:\n" + aggragateQuery);
	    	
	    	return aggragateQuery;
	    }

	    /**
    	 * Gets the filtered query.
    	 * 
    	 * @param filterValue the filter value
    	 * 
    	 * @return the filtered query
    	 */
    	private String getFilteredQuery(String filterValue) {
	    	String aggragateQuery = null;
	    	String query = getExecutableQuery();
	    	
	    	String subQueryAlias = "t" + System.currentTimeMillis();
	    	String normalizedSubQueryAlias = "n" + System.currentTimeMillis();
	    	String dimGeoAlias = "g" + System.currentTimeMillis();
	    	
	    	Hierarchy hierarchy = getSelectedHierarchy();
	    	Hierarchy.Level level = getSelectedLevel();
	    	String baseLevelName = getMetaData().getLevelName( hierarchy.getName() );
	    	Hierarchy.Level baseLevel = hierarchy.getLevel( baseLevelName );
	    	
	    	if(hierarchy.getType().equalsIgnoreCase("custom")) {
	    		System.out.println("\nCUSTOM HIERARCHY...\n");
		    	String aggregationColumnName = level.getColumnId(); 
		    	aggragateQuery = "SELECT * " ;
		    	aggragateQuery += " \nFROM ( " + query + ") " + subQueryAlias;
		    	aggragateQuery += " \nWHERE " + subQueryAlias + "." + level.getColumnId();
		    	aggragateQuery += " = '" + filterValue + "'";
	    	} else {
	    		System.out.println("\nDEFAULT HIERARCHY...\n");
	    		String aggregationColumnName = level.getColumnId(); 
		    	aggragateQuery = "SELECT * ";
		    	String[] kpiColumnNames = (String[])getMetaData().getMeasureColumnNames().toArray(new String[0]);
		    	
		    	
		    	String normalizedSubQuery = query;
		    
		    	normalizedSubQuery ="SELECT " + normalizedSubQueryAlias + "." + getMetaData().getGeoIdColumnName( hierarchy.getName() ) +  " AS " + getMetaData().getGeoIdColumnName( hierarchy.getName() );
		    	for(int i = 0; i < kpiColumnNames.length; i++) {
		    		normalizedSubQuery +=  ", SUM(" + normalizedSubQueryAlias + "." + kpiColumnNames[i] + ") AS " + kpiColumnNames[i];
		    	}
		    	normalizedSubQuery += " \nFROM ( " + query + ") " + normalizedSubQueryAlias;
		    	normalizedSubQuery += " \nGROUP BY " + normalizedSubQueryAlias + "." + getMetaData().getGeoIdColumnName( hierarchy.getName() );
		    	System.out.println("\nNormalized query:\n" + normalizedSubQuery);
		    	
		    	
		    	aggragateQuery += " \nFROM ( \n" + normalizedSubQuery + "\n ) " + subQueryAlias;
		    	String dimGeoQuery = getDimGeoQuery();
		    	System.out.println("\nDimGeo query:\n" + dimGeoQuery);
		    	aggragateQuery += ", (" + dimGeoQuery + ") " + dimGeoAlias;
		    	aggragateQuery += " \nWHERE " + subQueryAlias + "." + getMetaData().getGeoIdColumnName( hierarchy.getName() );
		    	aggragateQuery += " = " + dimGeoAlias + "." + baseLevel.getColumnId();
		    	aggragateQuery += " \nAND  " + dimGeoAlias + "." + level.getColumnId() + " = '" + filterValue + "'";
	    	}
	    	
	    	System.out.println("\nExecutable query:\n" + aggragateQuery);
	    	
	    	return aggragateQuery;
	    }
	    
	    
	    
	    
	    
	    
	    /* (non-Javadoc)
    	 * @see it.eng.spagobi.engines.geo.dataset.provider.AbstractDatasetProvider#getDataDetails(java.lang.String)
    	 */
    	public SourceBean getDataDetails(String featureValue) {
	    	SourceBean results = null;
	    	
	     
	    	Hierarchy hierarchy = getSelectedHierarchy();
	    	String baseLevelName = getMetaData().getLevelName( hierarchy.getName() );
	    	Hierarchy.Level baseLevel = hierarchy.getLevel( baseLevelName );    
	    	String columnid = baseLevel.getColumnId();     
	    	
	    	String targetLevelName = getSelectedLevelName();
	    	String filterValue = featureValue;
	    	if(filterValue.trim().startsWith(targetLevelName + "_")) {
	    		filterValue = filterValue.substring(targetLevelName.length()+1);
			}
	    	
	        
	    	String filteredQuery = "";    	
	    	filteredQuery = getFilteredQuery(filterValue);
	    	int max_rows = 1000;
	        
	    	Connection connection = null;
	        try{
	        	connection = getDataSource().getConnection();
	            Statement statement = connection.createStatement();
	            statement.execute(filteredQuery);
	            ResultSet resultSet =  statement.getResultSet();
	            
	            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
	            int columnCount = resultSetMetaData.getColumnCount();

	            results = new SourceBean("ROWS");
	            SourceBean row;
	            //resultSet.beforeFirst();
	            int rowno = 0;
	            while(resultSet.next()) {
	            	if(++rowno > 1000) break;
	            	
	            	String id = resultSet.getString(resultSet.findColumn(columnid));
	            	if((id==null) || (id.trim().equals(""))) {
	            		continue;
	            	}
	            	
	            	row = new SourceBean("ROW");
	            	
	            	for (int i=1; i<=columnCount; i++) {                   
	            		row.setAttribute(resultSetMetaData.getColumnLabel(i), (resultSet.getString(i)==null)?"":resultSet.getString(i));
	                }
	            	results.setAttribute(row);         	
	            }
	            
	            	
	            
	            
	            
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        	//throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
	        } finally {
	        	if(connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
			        	//throw new EMFUserError(EMFErrorSeverity.ERROR, "Impossible to close connection");
					}
	        	}
	        }
	        
	        return results;
	    }
	    
	    
	    
	    
	    
	    


		/**
		 * Gets the data source.
		 * 
		 * @return the data source
		 */
		protected DataSource getDataSource() {
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
}
