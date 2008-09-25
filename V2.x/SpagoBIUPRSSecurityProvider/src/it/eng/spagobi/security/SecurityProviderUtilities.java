/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.security;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.axis.AxisProperties;
import org.apache.log4j.Logger;



public class SecurityProviderUtilities {
	static private final String STMT_GET_ROLES = "SELECT_ROLES_LIST";
	static private final String STMT_GET_USER_ROLES = "SELECT_USER_ROLES_LIST";
    static private final String STMT_GET_ATTRIBUTES = "SELECT_ATTRIBUTES_LIST";
    static private final String STMT_GET_USER_ATTRIBUTES = "SELECT_USER_ATTRIBUTES_LIST";
    
    protected static ConfigSingleton config = ConfigSingleton.getInstance();
    
	static private Logger logger = Logger.getLogger(UPRSSecurityServiceSupplierImpl.class);

	
	public static void debug(Class classErr, String nameMeth, String message){
		SpagoBITracer.debug("SPAGOBI(ExoSecurityProvider)",
	            			classErr.getName(),
	            			nameMeth,
	            			message);
	}
	
	public  static Pattern getFilterPattern(){
		ConfigSingleton config = ConfigSingleton.getInstance();
		debug(SecurityProviderUtilities.class, "init", "Spago configuration retrived ");
		SourceBean secFilterSB = (SourceBean)config.getAttribute("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER");
		debug(SecurityProviderUtilities.class, "init", "source bean filter retrived " + secFilterSB);
        String rolePatternFilter = secFilterSB.getCharacters();
        debug(SecurityProviderUtilities.class, "init", "filter string retrived " + rolePatternFilter);
        Pattern pattern = Pattern.compile(rolePatternFilter);
        debug(SecurityProviderUtilities.class, "init", "regular expression pattern compiled " + pattern);
        return pattern;
	}
	
	/**
     * Get the informations about the proxy.
     * 
     * @return Connection to database
     * 
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException the SQL exception
     */
    public static boolean getProxyInfo() {
 
		String host = "";
		String port = "";
		String user = "";
		String password = "";		
    	
    	SourceBean sbUPRS_PROXY = (SourceBean)config.getAttribute("UPRS_SECURITY.CONF.PROXY");
		host = (String)sbUPRS_PROXY.getAttribute("host");
		port = (String)sbUPRS_PROXY.getAttribute("port");
		user = (String)sbUPRS_PROXY.getAttribute("user");
		password = (String)sbUPRS_PROXY.getAttribute("password");
		
		if (host == null || port == null || user == null){
			logger.error("Host, port or user is null. Host : " + host + " - port: " + port + " - user: "+ user +
					"- \n Is impossible to connect to UPRS.");
			return false;
		}
    	
	    AxisProperties.setProperty("http.proxyHost", host);
        AxisProperties.setProperty("http.proxyPort", port);
        AxisProperties.setProperty("http.proxyUser", user);
        AxisProperties.setProperty("http.proxyPassword", password);
    	return true;
    }
    
	/**
     * Get the connection using jdbc.
     * 
     * @return Connection to database
     * 
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException the SQL exception
     */
    public static  Connection getDirectConnection() throws ClassNotFoundException, SQLException {
    	Connection connection = null;
		String driver = "";
		String url = "";
		String user = "";
		String password = "";		
    	
    	SourceBean sbUPRS_DS = (SourceBean)config.getAttribute("UPRS_SECURITY.DATASET.DATASOURCE");
		driver = (String)sbUPRS_DS.getAttribute("driver");
		url = (String)sbUPRS_DS.getAttribute("url");
		user = (String)sbUPRS_DS.getAttribute("user");
		password = (String)sbUPRS_DS.getAttribute("password");
		
		if (driver == null || url == null || user == null){
			logger.error("Driver, url or user is null. Driver : " + driver + " - url: " + url + " - user: "+ user +
					"- \n Is impossible to connect to db for define UserRoles List.");
			return null;
		}
		
    	
    	try {
    	    Class.forName(driver);
    	    
    	    connection = DriverManager.getConnection(url, user, password);
    	} catch (ClassNotFoundException e) {
    	    logger.error("Driver not found", e);
    	} catch (SQLException e) {
    	    logger.error("Cannot retrive connection", e);
    	}finally{
    		logger.debug("OUT");
    	}
    	return connection;
    }
    
    public static  DataConnection getDataConnection(Connection con)
	    throws EMFInternalError
	{
	    DataConnection dataCon = null;
	    try
	    {
	        Class mapperClass = Class.forName("it.eng.spago.dbaccess.sql.mappers.OracleSQLMapper");
	        SQLMapper sqlMapper = (SQLMapper)mapperClass.newInstance();
	        dataCon = new DataConnection(con, "2.1", sqlMapper);
	    }
	    catch(Exception e)
	    {
	        logger.error("Error while getting Data Source " + e);
	        throw new EMFInternalError("ERROR", "cannot build spago DataConnection object");
	    }
	    return dataCon;
	}
    
    /**
     * Execute the query and return a list.
     * 
     * @param connection the connection object
     * @param stmtName the statement name to execute for recovery data
     * @param pars list of parameters
     * 
     * @return list with data
     * 
     * @param Connection to database
     */
    public static  List executeQuery(String stmtName, List pars) {

    	List inputParameter = new ArrayList();
    	String statement = "";
    	
    	SourceBean sbUPRS_STMT = (SourceBean)config.getAttribute("UPRS_SECURITY.DATASET.STATEMENTS");
		List sbStatements = (List)sbUPRS_STMT.getAttributeAsList("STATEMENT");
				
		Iterator iter_sb_stmt = sbStatements.iterator();
		while(iter_sb_stmt.hasNext()) {
			SourceBean sbStmt = (SourceBean)iter_sb_stmt.next();
			String name = (String)sbStmt.getAttribute("name");
			if (name.equalsIgnoreCase(stmtName)){
				statement = (String)sbStmt.getAttribute("query");
				logger.debug("statement="+statement);
				break;
			}	
		}
		
		if (statement == null){
			logger.error("Statement is null. Is impossible to define UserRoles List.");
			return null;
		}
		Connection connection = null;
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		List columnsNames = new ArrayList();
		List lstValues = new ArrayList();
		try {
			connection = getDirectConnection();
			//gets data
			dataConnection = getDataConnection(connection);
			sqlCommand = dataConnection.createSelectCommand(statement);
			if (pars != null){
				for (int i=0; i< pars.size(); i++){
					inputParameter.add(dataConnection.createDataField("", Types.VARCHAR,pars.get(i))); 
				}
			}
            if ((inputParameter != null) && (inputParameter.size() != 0)) 
                dataResult = sqlCommand.execute(inputParameter);
            else
                dataResult = sqlCommand.execute();

			ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult.getDataObject();
			List temp = Arrays.asList(scrollableDataResult.getColumnNames());
			columnsNames.addAll(temp);
			lstValues = scrollableDataResult.getSourceBean().getContainedAttributes();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		}
		return lstValues;
    }
    
    /**
	 * Gets the list of roles of a user
	 * 
	 * @param userId the user identifier
	 * 
	 * @return the list of names of all roles for the user 
	 */
	public static HashMap getAllUserRoles(String userId) {
	    logger.debug("IN");
	    
	    HashMap roles = new HashMap();
	    List pars = new ArrayList();
	    pars.add(userId);
		
		List lstRoles = executeQuery(STMT_GET_USER_ROLES, pars);		

		//creates the final roles list
		Iterator iter_sb_roles = lstRoles.iterator();
		while(iter_sb_roles.hasNext()) {
			SourceBeanAttribute tmp_attributeSB = (SourceBeanAttribute)iter_sb_roles.next();
			SourceBean roleSB = (SourceBean)tmp_attributeSB.getValue();
			String code = (String)roleSB.getAttribute("CODE");
			String role = (String)roleSB.getAttribute("DESCRIPTION");
			logger.debug("ADD: attribute="+role);
			if (role != null) roles.put(code, role);
		}
		logger.debug("OUT");
		return roles;
	}
	
	 /**
	 * Gets the list of all roles 
	 * 
	 * @return the list of names of all roles for the user 
	 */
	public static List getAllRoles() {
	    logger.debug("IN");
	    
	    List roles = new ArrayList();
		List lstRoles = executeQuery(STMT_GET_ROLES,  null);		

		//creates the final roles list
		Iterator iter_sb_roles = lstRoles.iterator();
		while(iter_sb_roles.hasNext()) {
			SourceBeanAttribute tmp_attributeSB = (SourceBeanAttribute)iter_sb_roles.next();
			SourceBean roleSB = (SourceBean)tmp_attributeSB.getValue();
			String strCode = (String)roleSB.getAttribute("CODE");
			String strRole = (String)roleSB.getAttribute("DESCRIPTION");
			Role role = new Role(strCode, strRole);
			logger.debug("ADD: attribute="+role);
			if (role != null) roles.add(role);
		}
		logger.debug("OUT");
		return roles;
	}
	
	/**
	 * Gets the list of names of all attributes of all profiles from db.
	 * 
	 * @return the list of names of all attributes of all profiles defined
	 */
	public static List getAllProfileAttributesNames() {
	    logger.debug("IN");
    
	    List attributes = new ArrayList();
		List lstAttributes = executeQuery(STMT_GET_ATTRIBUTES, null);		

		//creates the final attributes list
		Iterator iter_sb_roles = lstAttributes.iterator();
		while(iter_sb_roles.hasNext()) {
			SourceBeanAttribute tmp_attributeSB = (SourceBeanAttribute)iter_sb_roles.next();
			SourceBean attributeSB = (SourceBean)tmp_attributeSB.getValue();
			String attribute = (String)attributeSB.getAttribute("DESCRIPTION");
			logger.debug("ADD: attribute="+attribute);
			attributes.add( attribute);
		}
		logger.debug("OUT");
		return attributes;
	}
	
	/**
	 * Get all the profile attributes of the users given his unique identifier
	 * 
	 * @param userUniqueIdentifier String representing the unique identifier.
	 * @return service The attribute list map (code, value). 
	 * 
	 */
	public static HashMap getUserProfileAttributes (String userId) {
		
		SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, SecurityProviderUtilities.class.getName(), 
				"getUserProfileAttributes",
				" Trying to load user attributes for user with unique identifer '" + userId +"'.");
		
		// load the  user profile attributes into a temporary hashmap
		HashMap userAttributes = new HashMap();
		List pars = new ArrayList();
		pars.add(userId);
	
		List lstAttributes = executeQuery(STMT_GET_USER_ATTRIBUTES, pars);		

		//creates the final user attributes list
		Iterator iter_sb_attributes = lstAttributes.iterator();
		while(iter_sb_attributes.hasNext()) {
			SourceBeanAttribute tmp_attributeSB = (SourceBeanAttribute)iter_sb_attributes.next();
			SourceBean attributeSB = (SourceBean)tmp_attributeSB.getValue();
			String attribute = (String)attributeSB.getAttribute("CODE");
			String value = (String)attributeSB.getAttribute("VALUE");
			logger.debug("ADD:attribute= "+attribute +" value= "+ value);
			if (attribute != null && value != null) userAttributes.put(attribute, value);
		}
		logger.debug("OUT");
		
		return userAttributes;
	}
}
