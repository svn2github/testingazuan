/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import it.eng.spagobi.utilities.ParametersDecoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class BirtReportServlet extends BirtReportServletODA {
	
	public void service(
	        HttpServletRequest request,
	        HttpServletResponse response)
	        throws IOException, ServletException {
		
 		Map params = new HashMap();
	 	Enumeration enumer = request.getParameterNames();
	 	String parName = null;
	 	String parValue = null;
	 	while (enumer.hasMoreElements()){
	 		parName = (String)enumer.nextElement();
	 		parValue = request.getParameter(parName);
	 		//params.put(parName, parValue);
	 		addParToParMap(params, parName, parValue);
	 	}		

	 	if (securityAble && !authenticate(request, response)) {
	 		PrintWriter writer = response.getWriter();
	 		String resp = "<html><body><center><h2>Unauthorized</h2></center></body></html>";
	 		writer.write(resp);
	 		writer.flush();
	 		writer.close();
	 		return;
	 	} else {
	 		String template = (String)params.get("templatePath");
	 		String spagobibase = (String)params.get("spagobiurl");
	 		String dateformat = (String)params.get("dateformat");
	 		BirtReportRunner birtReportRunner = new BirtReportRunner(spagobibase, template, dateformat);
	 		Connection connection = getConnection(request.getParameter("connectionName"));
	 		if (connection == null){
	 			logger.error("Engines "+this.getClass().getName()+ " service() Cannot obtain" +
	 				     " connection for engine ["+this.getClass().getName()+"] control" +
	 				     " configuration in engine-config.xml config file");
	 			return;
	 		}
	 		try {
	 			birtReportRunner.runReport(connection, params, getServletContext(), response, request);
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 		} finally {
				try {
					if (connection != null && !connection.isClosed()) 
						connection.close();
				} catch (SQLException sqle) {
					sqle.printStackTrace();
					connection = null;
				}
			}
	 	}

	}
	
	/**
	 *  This methos see in spagbi.xml config file ( in section <ENGINE className=engineName> if a connection is configured 
	 *  if null is pass to this method a default datasource is returned if exist
	 * @param connectionName
	 * @return
	 */
	public Connection getConnection(String connectionName) {
		
		String engineClassName = this.getClass().getName();
		logger.debug("Engines" + this.getClass().getName()+ "getConnection() Try to retrieve configuration settings for engine [" + engineClassName + "]");
		// read the engines configuration file
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			document = reader.read(getClass().getResourceAsStream("/engine-config.xml"));
		}catch(DocumentException de){
			logger.error("Engines"+ this.getClass().getName()+ "getConnection():"+
					     "Error during parsing of the engine configuration file", de);
		}
		// get the engine configuration node
        Node connectionDescriptorNode = null;
		if(connectionName == null){
			logger.debug("Engines"+ this.getClass().getName()+ "getConnection() Connection name not specified for engine [" + engineClassName + "] look for a default connection" );
			connectionDescriptorNode = document.selectSingleNode( "//ENGINE-CONFIGURATION/CONNECTION[@isDefault='true'");
		}else{
			logger.debug("Engines"+this.getClass().getName()+ "getConnection() Searching for Connection name [" + connectionName +"] for engine [" + engineClassName + "] " );
			connectionDescriptorNode = document.selectSingleNode( "//ENGINE-CONFIGURATION/CONNECTION[@name='"+connectionName+"'");
		}
		// if configuration is jndi get datasource from jndi context
		String jndi = connectionDescriptorNode.valueOf("@isJNDI");
		if(jndi.equalsIgnoreCase("true")) {
			return getConnectionFromJndiDS(connectionDescriptorNode);
		} else {
			return getDirectConnection(connectionDescriptorNode);
		}
	}
	
	/**
	 * Get the connection from JNDI
	 * @param conDesc Xml node describing data connection
	 * @return Connection to database
	 */
	private Connection getConnectionFromJndiDS(Node conDesc) {
		Connection connection = null;
		String iniCont = conDesc.valueOf("@initialContext");
        String resName = conDesc.valueOf("@resourceName");
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup(iniCont);
			DataSource ds = (DataSource) envCtx.lookup(resName);
			connection = ds.getConnection();
		} catch (NamingException ne) {
			logger.error("Engines"+ this.getClass().getName()+ "getConnectionFromJndiDS():"+
			         "JNDI error", ne);
		} catch (SQLException sqle) {
			logger.error("Engines"+ this.getClass().getName()+ "getConnectionFromJndiDS():"+
			         "Cannot retrive connection", sqle);
		}
		return connection;
	}
	
	/**
	 * Get the connection using jdbc (connection string direct to database)
	 * @param conDesc Xml node describing data connection
	 * @return Connection to database
	 */
	private Connection getDirectConnection(Node conDesc) {
		Connection connection = null;
	    try {
	        String driverName = conDesc.valueOf("@driver");
	        Class.forName(driverName);
	        String url = conDesc.valueOf("@jdbcUrl");
	        String username = conDesc.valueOf("@user");
	        String password = conDesc.valueOf("@password");
	        connection = DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	    	logger.error("Engines"+ this.getClass().getName()+ "getDirectConnection():"+
				         "Driver not found", e);
	    } catch (SQLException e) {
	    	logger.error("Engines"+ this.getClass().getName()+ "getDirectConnection():"+
			             "Cannot retrive connection", e);
	    }
	    return connection;
	}
	
	/**
	 * @param params
	 * @param parName
	 * @param parValue
	 */
	private void addParToParMap(Map params, String parName, String parValue) {
		String newParValue;
		
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			newParValue = (String)values.get(0);
			/*
			for(int i = 0; i < values.size(); i++) {
				newParValue += (i>0?",":"");
				newParValue += values.get(i);
			}
			*/
			
		} else {
			newParValue = parValue;
		}
		
		params.put(parName, newParValue);
	}
	
}
