/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package utilities;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.SQLStatements;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;



public class DataSourceUtilities {
	private static transient Logger logger = Logger.getLogger(DataSourceUtilities.class);
	public static String SCHEMA = "schema";
	public static String STMT = "stmt";
	public static String NUM_PARS = "numPars";

	/**  This method gets all request parameters and define an hashmap object
	 * 
	 * @param request the sourcebean with the request
	 * 
	 * @return the hashmap with all parameters
	 * 
	 */
	public HashMap  getAttributesAsMap(SourceBean request){
		logger.debug("IN");
		
		HashMap<String , Object> params = new HashMap <String , Object>();
		List reqParams = (List)request.getContainedAttributes();
		Iterator it = reqParams.iterator();
		while (it.hasNext()) {
			SourceBeanAttribute param = (SourceBeanAttribute)it.next();
			String paramName = param.getKey();
			String paramValue;
			try {
				paramValue = (String) param.getValue();
			} catch (Exception e) {
				logger.debug("OUT");
				logger.error("Impossible read value for the parameter [" + paramName + "] into request's content", e);
				throw new SpagoBIServiceException("", "Impossible read value for the parameter [" + paramName + "] into request's content", e);
			}
			params.put(paramName, paramValue);
		}
		
		logger.debug("OUT");
		return params;
	}
	
	/** This method execute an update query on a generic db with Spago instructions.
	 * 
	 * @param the hashmap with all parameters
	 * 
	 * @return a boolean value with the response of the operation.
	 * 
	 */
	public boolean executeUpdateQuery(HashMap<String , Object> params) throws Throwable, Exception{
		boolean toReturn = true;
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		
		logger.debug("IN");
		
		try {
			String schema = (String)params.get( SCHEMA ); 
			logger.debug("Parameter [" + SCHEMA + "] is equals to [" + schema + "]");			
			Assert.assertTrue(!StringUtilities.isEmpty( schema ), "Parameter [" + SCHEMA + "] cannot be null or empty");
			
			DataConnectionManager dataConnectionManager = DataConnectionManager.getInstance();
			dataConnection = dataConnectionManager.getConnection(schema); 
			dataConnection.initTransaction();
			
			String statement = SQLStatements.getStatement((String)params.get( STMT )); 
			logger.debug("Parameter [" + STMT + "] is equals to [" + statement + "]");			
			Assert.assertTrue(!StringUtilities.isEmpty( statement ), "Parameter [" + STMT + "] cannot be null or empty");
			
			String numParsStr = (String) params.get( NUM_PARS );
			int numPars = (numParsStr != null)?Integer.parseInt(numParsStr):0;
			logger.debug("Parameter [ numPars ] is equals to [" + numPars + "]");
			
			sqlCommand = dataConnection.createUpdateCommand(statement);
			
			if (numPars > 0){
				List inputParameter = new ArrayList(numPars);
						
				Iterator iterator =  params.keySet().iterator();
				int i=1;
				while (iterator.hasNext()) {
					String paramName = (String) iterator.next();
					String paramValue = (String)params.get( paramName );
		
					if (paramName.equalsIgnoreCase("par"+i)){
						String parType = (String)params.get("typePar"+i);
						inputParameter.add(dataConnection.createDataField(paramName,getParamType(parType), paramValue));
						i++;
					}					
				}								
				dataResult = sqlCommand.execute(inputParameter);
			}else{
				dataResult = sqlCommand.execute();
			}
			dataConnection.commitTransaction();
		} // try
		catch (Exception ex) {
			toReturn = false;
			logger.error("QueryExecutor::executeQuery:", ex);
			try{
				dataConnection.rollBackTransaction();
			} catch (Throwable t) {
				toReturn = false;
				//throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
				throw new Throwable(t);
			}
			throw new Throwable(ex);
			
		} 
		finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		} // finally try
		
		return toReturn;
	}


	
	
	private int getParamType(String parType){
		int toReturn = 0;
		if (parType.equalsIgnoreCase("num") || parType.equalsIgnoreCase("integer")) return java.sql.Types.INTEGER;	
		else if (parType.equalsIgnoreCase("decimal") ) return java.sql.Types.DECIMAL;	
		else if (parType.equalsIgnoreCase("double")) return java.sql.Types.DOUBLE;
		else if (parType.equalsIgnoreCase("string") || parType.equalsIgnoreCase("char")) return java.sql.Types.VARCHAR;
		else if (parType.equalsIgnoreCase("boolean") ) return java.sql.Types.BOOLEAN;
		else if (parType.equalsIgnoreCase("date") || parType.equalsIgnoreCase("datetime")) return java.sql.Types.DATE;
		return toReturn;
	}
	
	
	/**
	 * Creates a Spago DataConnection object starting from a sql connection.
	 * 
	 * @param con Connection to the export database
	 * 
	 * @return The Spago DataConnection Object
	 * 
	 * @throws EMFInternalError the EMF internal error
	 */
	public DataConnection getDataConnection(Connection con) throws EMFInternalError {
		DataConnection dataCon = null;
		try {
			Class mapperClass = Class.forName("it.eng.spago.dbaccess.sql.mappers.OracleSQLMapper");
			SQLMapper sqlMapper = (SQLMapper)mapperClass.newInstance();
			dataCon = new DataConnection(con, "2.1", sqlMapper);
		} catch(Exception e) {
			logger.error("Error while getting Data Source " + e);
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "cannot build spago DataConnection object");
		}
		return dataCon;
	}
	
	
}
