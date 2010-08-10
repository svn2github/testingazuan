/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.datamart;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * @author Administrator
 * 
 */
public class DefaultDatamartProvider extends AbstractDatamartProvider {

    private static final String REGISTERED_POOL_NAME = "REGISTERED_POOL_NAME";

    private static final String QUERY = "QUERY";

    private static final String ELEMENT_ID = "ELEMENT_ID";

    private static final String ELEMENT_NAME = "ELEMENT_NAME";

    /**
     * The constructor
     */
    public DefaultDatamartProvider() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.eng.geo.mapproviders.SVGMapProviderIFace#getSVGMap()
     */
    public DatamartObject getDatamartObject(SourceBean datamartProviderConfiguration) {

        DatamartObject datamartObject = new DatamartObject();

        SQLCommand cmdSelect = null;
        DataResult dr = null;
        ScrollableDataResult sdr = null;
        //        ArrayList parameters = new ArrayList(1);

        DataConnection dataConnection = null;
        String connectionName = (String) datamartProviderConfiguration.getAttribute(REGISTERED_POOL_NAME);
        String query = (String) datamartProviderConfiguration.getAttribute(QUERY);
        String elementId = (String) datamartProviderConfiguration.getAttribute(ELEMENT_ID);
        String elementName = (String) datamartProviderConfiguration.getAttribute(ELEMENT_NAME);

        try {

            dataConnection = DataConnectionManager.getInstance().getConnection(connectionName);
            cmdSelect = dataConnection.createSelectCommand(query);
            /*
             * parameters.add(
             * dataConnection.createDataField("path_id",Types.VARCHAR, pathId));
             * dr = cmdSelect.execute(parameters);
             */
            dr = cmdSelect.execute();
            sdr = (ScrollableDataResult) dr.getDataObject();

            ResultSet resultSet = sdr.getResultSet();

            ArrayList elementIdList = new ArrayList();
            ArrayList valueList = new ArrayList();
            if (resultSet.first()) {
                elementIdList.add(0, resultSet.getString(resultSet.findColumn(elementId)));
                valueList.add(0, resultSet.getString(resultSet.findColumn(elementName)));
                for (int i = 1; resultSet.next(); i++) {
                    elementIdList.add(i, resultSet.getString(resultSet.findColumn(elementId)));
                    valueList.add(i, resultSet.getString(resultSet.findColumn(elementName)));
                }
            }
            datamartObject.setElementIdList(elementIdList);
            datamartObject.setValueList(valueList);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            //		KFTracerSingleton.log(
            //			Constants.NOME_MODULO,
            //			TracerSingleton.MAJOR,
            //			"OracleDAO::readData:Non è possibile recuperare le informazioni
            // per il dettaglio "
            //				+ ex);
            //		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
        } finally {
            Utils.releaseResources(dataConnection, cmdSelect, dr);
        }
        return datamartObject;
    }
}