/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.datamart;

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

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Executes the query and obtains the data associated to the svg map
 */
public class DefaultDatamartProvider extends AbstractDatamartProvider {

    private static final String REGISTERED_POOL_NAME = "connection_name";

    private static final String QUERY = "QUERY";

    private static final String COLUMN_ID = "column_id";

    private static final String COLUMN_VALUE = "column_value";

    /**
     * Constructor
     */
    public DefaultDatamartProvider() {
        super();
    }

    /**
     * Executes the query and obtains the data associated to the svg map
     * @param datamartProviderConfiguration SourceBean wich contains the configuration 
     * for the data recovering (see template definition into GeoAction class)
     */
    public DatamartObject getDatamartObject(SourceBean datamartProviderConfiguration) throws EMFUserError {

        DatamartObject datamartObject = new DatamartObject();
        SQLCommand cmdSelect = null;
        DataResult dr = null;
        ScrollableDataResult sdr = null;
        DataConnection dataConnection = null;
        String connectionName = (String) datamartProviderConfiguration.getAttribute(REGISTERED_POOL_NAME);
        String query = (String) datamartProviderConfiguration.getAttribute(QUERY);
        String columnid = (String) datamartProviderConfiguration.getAttribute(COLUMN_ID);
        String columnvalue = (String) datamartProviderConfiguration.getAttribute(COLUMN_VALUE);
        try{
            dataConnection = DataConnectionManager.getInstance().getConnection(connectionName);
            cmdSelect = dataConnection.createSelectCommand(query);
            dr = cmdSelect.execute();
            sdr = (ScrollableDataResult) dr.getDataObject();
            ResultSet resultSet = sdr.getResultSet();
            Map styles = new HashMap();
            while(resultSet.next()) {
            	String id = resultSet.getString(resultSet.findColumn(columnid));
            	if((id==null) || (id.trim().equals(""))) {
            		continue;
            	}
            	String valueStr = resultSet.getString(resultSet.findColumn(columnvalue));
            	if((valueStr==null) || (valueStr.trim().equals(""))) {
            		continue;
            	}
            	Integer value = new Integer(valueStr);
            	styles.put(id, value);
            }
            datamartObject.setValues(styles);
        } catch (Exception ex) {
        	TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
        					    "DefaultDatamartProvider :: getDatamartObject : " +
        					    "Cannot load the data from the datawarehouse", ex);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
        } finally {
            Utils.releaseResources(dataConnection, cmdSelect, dr);
        }
        return datamartObject;
    }
}