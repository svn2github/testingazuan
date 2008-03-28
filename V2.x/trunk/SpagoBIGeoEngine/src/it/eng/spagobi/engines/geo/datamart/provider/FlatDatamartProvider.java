/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.engines.geo.datamart.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.engines.geo.application.GeoEngineCLI;
import it.eng.spagobi.engines.geo.configuration.Constants;
import it.eng.spagobi.engines.geo.configuration.DataSource;
import it.eng.spagobi.engines.geo.configuration.DatamartProviderConfiguration;
import it.eng.spagobi.engines.geo.datamart.Datamart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Executes the query and obtains the data associated to the svg map
 */
public class FlatDatamartProvider extends AbstractDatamartProvider { 
    
    private static final String PARAMETER = "PARAM";
    
    /**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(FlatDatamartProvider.class);
	

    /**
     * Constructor
     */
    public FlatDatamartProvider() {
        super();
    }
    
    public FlatDatamartProvider(DatamartProviderConfiguration datamartProviderConfiguration) {
        super(datamartProviderConfiguration);
    }
    
    private String getFilteredQuery(String filterValue) {
    	String aggragateQuery = null;
    	String query = datamartProviderConfiguration.getExecutableQuery();
    	
    	String subQueryAlias = "t" + System.currentTimeMillis();
    	String normalizedSubQueryAlias = "n" + System.currentTimeMillis();
    	String dimGeoAlias = "g" + System.currentTimeMillis();
    	
    	DatamartProviderConfiguration.Hierarchy hierarchy = datamartProviderConfiguration.getSelectedHierarchy();
    	DatamartProviderConfiguration.Hierarchy.Level level = datamartProviderConfiguration.getSelectedLevel();
    	DatamartProviderConfiguration.Hierarchy.Level baseLevel = datamartProviderConfiguration.getBaseLevel();
    	
	    aggragateQuery = "SELECT * " ;
	    aggragateQuery += " \nFROM ( " + query + ") " + subQueryAlias;
	    aggragateQuery += " \nWHERE " + subQueryAlias + "." + level.getColumnId();
	    aggragateQuery += " = '" + filterValue + "'";
    	 
    	
    	return aggragateQuery;
    }
    
    
    public Datamart getDatamartObject() throws EMFUserError {
    	
    	logger.debug("BEGIN");
    	

    	Datamart datamart = null;
        String query = null;
        DatamartProviderConfiguration.Hierarchy.Level level = null;
        String[] kpiColumnNames = null;
        SourceBean drillSB = null;
        String columnid = null;
        
        query = datamartProviderConfiguration.getExecutableQuery();
        logger.debug("query: " + query);        
        
        level = datamartProviderConfiguration.getSelectedLevel();
        kpiColumnNames = datamartProviderConfiguration.getKpiColumnNames();
        
        columnid = level.getColumnId();
        logger.debug("Target geo feature: " + level.getFeatureName());
        logger.debug("Geo column name: " + columnid); 
        logger.debug("Measure column names: " + Arrays.toString(kpiColumnNames));
        
        datamart = new Datamart();
    	datamart.setTargetFeatureName(level.getFeatureName());         
    	        
    	   
        try {       
        	initDatamart(datamart, 
            		this.datamartProviderConfiguration.getDataSource(), query, 
            		columnid, kpiColumnNames);
            
        } catch (Exception ex) {
        	logger.warn("Impossible to connect on primary connection", ex);
        	if(datamartProviderConfiguration.getBkpDataSource() != null) {
        		try {
        			logger.debug("Backup connection will be used in place of primary connection");
					initDatamart(datamart, 
							this.datamartProviderConfiguration.getBkpDataSource(), query, 
							columnid, kpiColumnNames);
				} catch (Exception e) {
					logger.fatal("Impossible to build datamart", e);
					ex.printStackTrace();
	            	throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
				}
        	} else {
        		logger.fatal("Impossible to build datamart", ex);
        		ex.printStackTrace();
            	throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
        	}       	
        	
        } finally {
        	
        }
        
        logger.debug("END");
        
        return datamart;
    }
    
    public SourceBean getDataDetails(String filterValue) throws EMFUserError {
    	SourceBean results = null;
    	
     
        DatamartProviderConfiguration.Hierarchy.Level level = datamartProviderConfiguration.getBaseLevel();        
    	String columnid = level.getColumnId();
    	
    	
        
    	String filteredQuery = "";    	
    	filteredQuery = getFilteredQuery(filterValue);
    	int max_rows = 1000;
        
    	Connection connection = null;
        try{
        	connection = datamartProviderConfiguration.getDataSource().readConnection();
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
        	throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
        } finally {
        	if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
		        	throw new EMFUserError(EMFErrorSeverity.ERROR, "Impossible to close connection");
				}
        	}
        }
        
        return results;
    }
    
    private String getRowAsString(ResultSet rs) throws SQLException {
    	StringBuffer buffer = new StringBuffer();
    	int columnCount = rs.getMetaData().getColumnCount();
    	for(int i = 1; i <= columnCount; i++) {
    		Object columnValue = rs.getObject(i);
    		if(columnValue != null) {
    			buffer.append(columnValue.toString() + ";");
    		} else {
    			buffer.append("NULL" + ";");
    		}
    	}
    	
    	return buffer.toString();
    }
    
    private String getHeaderString(ResultSet rs) throws SQLException {
    	StringBuffer buffer = new StringBuffer();
    	int columnCount = rs.getMetaData().getColumnCount();
    	for(int i = 1; i <= columnCount; i++) {
    		String columnLabel = rs.getMetaData().getColumnLabel(i);
    		if(columnLabel != null) {
    			buffer.append(columnLabel.toString() + ";");
    		} else {
    			buffer.append("NULL" + ";");
    		}
    	}
    	
    	return buffer.toString();
    }
    
    /**
     * Creates the link associated to a resultset row
     * @param drillSB The drill configuration
     * @param resultSet the resultset 
     * @return The url link associated to the resultset row
     */
   
    private String createLink(SourceBean drillSB, ResultSet resultSet) {
    	//String link = "../SpagoBIDrillServlet?";
    	String link = "javascript:parent.parent.execDrill(this.name,'/SpagoBIGeoEngine/SpagoBIDrillServlet?";
    	if(drillSB == null) return "javascript:void(0)";
    	
    	try{
	    	String docLbl = (String)drillSB.getAttribute("document");
	    	link += "DOCUMENT_LABEL=" + docLbl + "&";
	    	List paramSBs = drillSB.getAttributeAsList(PARAMETER);
	    	if (paramSBs != null){
		    	Iterator iterPar = paramSBs.iterator();
		    	while(iterPar.hasNext()) {
		    		SourceBean paramSB = (SourceBean)iterPar.next();
		    		String type = (String)paramSB.getAttribute("type");
		    		String name = (String)paramSB.getAttribute("name");
		    		String value = (String)paramSB.getAttribute("value");
		    		if(type.equalsIgnoreCase("absolute")) {
		    			link += name + "=" + value + "&";
		    		} else if(type.equalsIgnoreCase("relative")) {
		    			String realValue = resultSet.getString(resultSet.findColumn(value));
		    			link += name + "=" + realValue + "&";
		    		}
		    	}
	    	}
	    	link = link.substring(0, link.length()-1);
	    	link += "')";
    	} catch (Exception e) {
    		link = "javascript:void(0)";
    		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
				    			"DefaultDatamartProvider :: createLink : " +
				    			"Cannot create drill link", e);
    	}
    	return link;
    }    
    
    private void initDatamart(Datamart datamart, 
    		DataSource datasource, String query, 
    		String columnid, String[] kpiColumnNames) throws NumberFormatException, SQLException, NamingException, ClassNotFoundException  {
    	
    	
    	Connection connection = null;
		try {
			connection = datasource.readConnection();
			logger.debug("created a new connection on url: " + datasource.getUrl());
		
	        Statement statement = connection.createStatement();
	        statement.execute(query);
	        ResultSet resultSet = statement.getResultSet();           
	        
	        logger.debug( getHeaderString(resultSet) );
	    	
	        HashMap orderedKpiValuesMap = new HashMap();                 
	        for(int i = 0; i < kpiColumnNames.length; i++) {
	        	orderedKpiValuesMap.put(kpiColumnNames[i], new TreeSet());
	        }           
	        
	        SourceBean drillSB = (SourceBean)datamartProviderConfiguration.getDrillConfigurationSB();   
	        
	        
	        Map values = new HashMap();
	        Map attributes = null;
	        Map links = new HashMap();
	        while(resultSet.next()) {
	        	
	        	logger.debug( getRowAsString(resultSet) );
	        	
	        	String id = resultSet.getString(resultSet.findColumn(columnid));
	        	if((id==null) || (id.trim().equals(""))) {
	        		continue;
	        	}
	        	
	        	attributes = new HashMap();
	        	for(int i = 0; i < kpiColumnNames.length; i++) {
	            	String value = resultSet.getString(resultSet.findColumn(kpiColumnNames[i]));
	            	if((value==null) || (value.trim().equals(""))) {
	            		continue;
	            	}
	            	attributes.put(kpiColumnNames[i], value);
	            	
	            	((Set)orderedKpiValuesMap.get(kpiColumnNames[i])).add(new Double(value));
	            	
	        	}
	        	
	        	values.put(id, attributes);
	        	
	            String link = createLink(drillSB, resultSet);
	            links.put(id, link);
	        }
	        datamart.setValues(values);
	        datamart.setLinks(links);
	        datamart.setKpiNames(kpiColumnNames);
	        datamart.setSelectedKpi(0);
	        datamart.setOrderedKpiValuesMap(orderedKpiValuesMap);   
      
	    } finally {
	    	if(connection != null) {
        		try {
					connection.close();
					logger.debug("connection closed succesfully on url: " + datasource.getUrl());
				} catch (SQLException e) {
					logger.error("impossible to close connection on url: "+ datasource.getUrl(), e);
					e.printStackTrace();
				}
        	}
	    }
    }
}