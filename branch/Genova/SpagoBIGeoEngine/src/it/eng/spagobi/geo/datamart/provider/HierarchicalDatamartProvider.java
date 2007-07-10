/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.datamart.provider;

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
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.DatamartProviderConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Executes the query and obtains the data associated to the svg map
 */
public class HierarchicalDatamartProvider extends AbstractDatamartProvider { 

    /**
     * Constructor
     */
    public HierarchicalDatamartProvider() {
        super();
    }
    
    public HierarchicalDatamartProvider(DatamartProviderConfiguration datamartProviderConfiguration) {
        super(datamartProviderConfiguration);
    }
    
    private String getDimGeoQuery() {
    	String query = "";
    	
    	DatamartProviderConfiguration.Hierarchy hierarchy = datamartProviderConfiguration.getSelectedHierarchy();
    	//DatamartProviderConfiguration.Hierarchy.Level level = datamartProviderConfiguration.getSelectedLevel();
    	DatamartProviderConfiguration.Hierarchy.Level baseLevel = datamartProviderConfiguration.getBaseLevel();
    	datamartProviderConfiguration.getHierarchyLevel();
    	
    	List levels = hierarchy.getSublevels(baseLevel.getName());
    	
    	query += "SELECT " + baseLevel.getColumnId();
    	for(int i = 0; i < levels.size(); i++) {
    		DatamartProviderConfiguration.Hierarchy.Level subLevel;
    		subLevel = (DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
    		query += ", " + subLevel.getColumnId();
    	}
    	query += " FROM " + hierarchy.getTable();
    	query += " GROUP BY " + baseLevel.getColumnId();
    	for(int i = 0; i < levels.size(); i++) {
    		DatamartProviderConfiguration.Hierarchy.Level subLevel;
    		subLevel = (DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
    		query += ", " + subLevel.getColumnId();
    	}
    	
    	return query;
    	
    }
    
    private String getAggreagteQuery() {
    	String aggragateQuery = null;
    	String query = datamartProviderConfiguration.getExecutableQuery();
    	
    	String subQueryAlias = "t" + System.currentTimeMillis();
    	String normalizedSubQueryAlias = "n" + System.currentTimeMillis();
    	String dimGeoAlias = "g" + System.currentTimeMillis();
    	
    	DatamartProviderConfiguration.Hierarchy hierarchy = datamartProviderConfiguration.getSelectedHierarchy();
    	DatamartProviderConfiguration.Hierarchy.Level level = datamartProviderConfiguration.getSelectedLevel();
    	DatamartProviderConfiguration.Hierarchy.Level baseLevel = datamartProviderConfiguration.getBaseLevel();
    	
    	System.out.println("\n\n-----------------------------\nOriginal query:\n" + query);
    	
    	if(hierarchy.getType().equalsIgnoreCase("custom")) {
    		System.out.println("\nCUSTOM HIERARCHY...\n");
	    	String aggregationColumnName = level.getColumnId(); 
	    	aggragateQuery = "SELECT " + subQueryAlias + "." + aggregationColumnName + " AS " + aggregationColumnName;
	    	String[] kpiColumnNames = datamartProviderConfiguration.getKpiColumnNames();
	    	for(int i = 0; i < kpiColumnNames.length; i++) {
	    		aggragateQuery +=  ", SUM(" + subQueryAlias + "." + kpiColumnNames[i] + ") AS " + kpiColumnNames[i];
	    	}
	    	aggragateQuery += " \nFROM ( " + query + ") " + subQueryAlias;
	    	aggragateQuery += " \nGROUP BY " + subQueryAlias + "." + aggregationColumnName;
    	} else {
    		System.out.println("\nDEFAULT HIERARCHY...\n");
    		String aggregationColumnName = level.getColumnId(); 
	    	aggragateQuery = "SELECT " + dimGeoAlias + "." + aggregationColumnName + " AS " + aggregationColumnName;
	    	String[] kpiColumnNames = datamartProviderConfiguration.getKpiColumnNames();
	    	for(int i = 0; i < kpiColumnNames.length; i++) {
	    		aggragateQuery +=  ", SUM(" + subQueryAlias + "." + kpiColumnNames[i] + ") AS " + kpiColumnNames[i];
	    	}
	    	
	    	String normalizedSubQuery = query;
	    
	    	normalizedSubQuery ="SELECT " + normalizedSubQueryAlias + "." + datamartProviderConfiguration.getColumnId() +  " AS " + datamartProviderConfiguration.getColumnId();
	    	for(int i = 0; i < kpiColumnNames.length; i++) {
	    		normalizedSubQuery +=  ", SUM(" + normalizedSubQueryAlias + "." + kpiColumnNames[i] + ") AS " + kpiColumnNames[i];
	    	}
	    	normalizedSubQuery += " \nFROM ( " + query + ") " + normalizedSubQueryAlias;
	    	normalizedSubQuery += " \nGROUP BY " + normalizedSubQueryAlias + "." + datamartProviderConfiguration.getColumnId();
	    	System.out.println("\nNormalized query:\n" + normalizedSubQuery);
	    	
	    	
	    	aggragateQuery += " \nFROM ( \n" + normalizedSubQuery + "\n ) " + subQueryAlias;
	    	String dimGeoQuery = getDimGeoQuery();
	    	System.out.println("\nDimGeo query:\n" + dimGeoQuery);
	    	aggragateQuery += ", (" + dimGeoQuery + ") " + dimGeoAlias;
	    	aggragateQuery += " \nWHERE " + subQueryAlias + "." + datamartProviderConfiguration.getColumnId();
	    	aggragateQuery += " = " + dimGeoAlias + "." + baseLevel.getColumnId();
	    	aggragateQuery += " \nGROUP BY " + dimGeoAlias + "." + aggregationColumnName;
    	}
    	
    	System.out.println("\nExecutable query:\n" + aggragateQuery);
    	
    	return aggragateQuery;
    }

    /**
     * Hierarchy AWARE !!!
     * Executes the query and obtains the data associated to the svg map
     * for the data recovering (see template definition into GeoAction class)
     */
    public Datamart getDatamartObject() throws EMFUserError {

    	Datamart datamart = new Datamart();
        SQLCommand cmdSelect = null;
        DataResult dr = null;
        ScrollableDataResult sdr = null;
        DataConnection dataConnection = null;
        String connectionName = datamartProviderConfiguration.getConnectionName();
        String query = getAggreagteQuery();
        
        DatamartProviderConfiguration.Hierarchy.Level level = datamartProviderConfiguration.getSelectedLevel();
    	datamart.setTargetFeatureName(level.getFeatureName()); 
        
    	String columnid = level.getColumnId();
        String[] kpiColumnNames = datamartProviderConfiguration.getKpiColumnNames();
        //SourceBean drillSB = (SourceBean)datamartProviderConfiguration.getAttribute(DRILL);
        
        
        try{
            dataConnection = DataConnectionManager.getInstance().getConnection(connectionName);
            cmdSelect = dataConnection.createSelectCommand(query);
            dr = cmdSelect.execute();
            sdr = (ScrollableDataResult) dr.getDataObject();
            ResultSet resultSet = sdr.getResultSet();
            Map values = new HashMap();
            Map attributes = null;
            Map links = new HashMap();
            while(resultSet.next()) {
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
            	}
            	
            	values.put(id, attributes);
            	//String link = createLink(drillSB, resultSet);
            	//links.put(id, link);
            }
            datamart.setValues(values);
            datamart.setLinks(links);
            datamart.setKpiNames(kpiColumnNames);
            datamart.setSelectedKpi(0);
            
        } catch (Exception ex) {
        	TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
        					    "DefaultDatamartProvider :: getDatamartObject : " +
        					    "Cannot load the data from the datawarehouse", ex);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
        } finally {
            Utils.releaseResources(dataConnection, cmdSelect, dr);
        }
        return datamart;
    }
    
    
    
    /**
     * Creates the link associated to a resultset row
     * @param drillSB The drill configuration
     * @param resultSet the resultset 
     * @return The url link associated to the resultset row
     */
    /*
    private String createLink(SourceBean drillSB, ResultSet resultSet) {
    	String link = "../SpagoBIDrillServlet?";
    	try{
	    	String docLbl = (String)drillSB.getAttribute("document");
	    	link += "DOCUMENT_LABEL=" + docLbl + "&amp;";
	    	List paramSBs = (List)drillSB.getAttribute(PARAMETER);
	    	Iterator iterPar = paramSBs.iterator();
	    	while(iterPar.hasNext()) {
	    		SourceBean paramSB = (SourceBean)iterPar.next();
	    		String type = (String)paramSB.getAttribute("type");
	    		String name = (String)paramSB.getAttribute("name");
	    		String value = (String)paramSB.getAttribute("value");
	    		if(type.equalsIgnoreCase("absolute")) {
	    			link += name + "=" + value + "&amp;";
	    		} else if(type.equalsIgnoreCase("relative")) {
	    			String realValue = resultSet.getString(resultSet.findColumn(value));
	    			link += name + "=" + realValue + "&amp;";
	    		}
	    	}
	    	link = link.substring(0, link.length()-5);
    	} catch (Exception e) {
    		link = "javascript:void(0)";
    		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
				    			"DefaultDatamartProvider :: createLink : " +
				    			"Cannot create drill link", e);
    	}
    	return link;
    }
    */
    
    
}