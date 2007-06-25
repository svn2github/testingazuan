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

    private static final String REGISTERED_POOL_NAME = "connection_name";

    private static final String QUERY = "QUERY";

    private static final String COLUMN_ID = "column_id";

    private static final String COLUMN_VALUE = "column_value";
    
    private static final String DRILL = "DRILL";
    
    private static final String PARAMETER = "PARAM";
    
    private static final String TARGET_FEATURE_NAME = "target_feature_name";

    /**
     * Constructor
     */
    public HierarchicalDatamartProvider() {
        super();
    }
    
    public HierarchicalDatamartProvider(SourceBean datamartProviderConfiguration) {
        super(datamartProviderConfiguration);
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
        String connectionName = (String) datamartProviderConfiguration.getAttribute(REGISTERED_POOL_NAME);
        String query = (String) datamartProviderConfiguration.getAttribute(QUERY);
        String columnid = (String) datamartProviderConfiguration.getAttribute(COLUMN_ID);
        String targetFeatureName = (String) datamartProviderConfiguration.getAttribute(TARGET_FEATURE_NAME);
        String columnvalueStr = (String) datamartProviderConfiguration.getAttribute(COLUMN_VALUE);
        String[] columnvalue = columnvalueStr.split(",");
        SourceBean drillSB = (SourceBean)datamartProviderConfiguration.getAttribute(DRILL);
        try{
            dataConnection = DataConnectionManager.getInstance().getConnection(connectionName);
            cmdSelect = dataConnection.createSelectCommand(query);
            dr = cmdSelect.execute();
            sdr = (ScrollableDataResult) dr.getDataObject();
            ResultSet resultSet = sdr.getResultSet();
            Map values = new HashMap();
            Map attributes = new HashMap();
            Map links = new HashMap();
            while(resultSet.next()) {
            	String id = resultSet.getString(resultSet.findColumn(columnid));
            	if((id==null) || (id.trim().equals(""))) {
            		continue;
            	}
            	for(int i = 0; i < columnvalue.length; i++) {
	            	String value = resultSet.getString(resultSet.findColumn(columnvalue[i]));
	            	if((value==null) || (value.trim().equals(""))) {
	            		continue;
	            	}
	            	attributes.put(columnvalue[i], value);
            	}
            	System.out.println("-> " + id);
            	values.put(id, attributes);
            	//String link = createLink(drillSB, resultSet);
            	//links.put(id, link);
            }
            datamart.setValues(values);
            datamart.setLinks(links);
            datamart.setTargetFeatureName(targetFeatureName);
            datamart.setKpiNames(columnvalue);
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
    
    
}