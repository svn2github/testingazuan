/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.weka;

import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.services.proxy.EventServiceProxy;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

/**
 * Process weka execution requests and returns bytes of the filled
 * reports
 */
public class WekaServlet extends HttpServlet {
	
	/**
	 * Logger component
	 */
	private static transient Logger logger = Logger.getLogger(WekaServlet.class);

	/**
	 * Input parameters map
	 */
	private Map params = null;
	
	
	public static final String CLUSTERER = "clusterer";
	public static final String CLUSTERNUM = "clusterNum";
	//public static final String TEMPLATE_PATH = "templatePath"; 
	public static final String CR_MANAGER_URL = "cr_manager_url"; 
	public static final String CONNECTION = "connectionName"; 
	public static final String INPUT_CONNECTION = "inputConnectionName"; 
	public static final String OUTPUT_CONNECTION = "outputConnectionName"; 
	public static final String WRITE_MODE = "writeMode"; 
	public static final String KEYS = "keys";
	public static final String VERSIONING = "versioning";
	public static final String VERSION_COLUMN_NAME = "versionColumnName";
	public static final String VERSION = "version";
	public static final String WEKA_ROLES_HANDLER_CLASS_NAME = "it.eng.spagobi.engines.drivers.weka.events.handlers.WekaRolesHandler";
	public static final String WEKA_PRESENTAION_HANDLER_CLASS_NAME = "it.eng.spagobi.engines.drivers.weka.events.handlers.WekaEventPresentationHandler";
	public static final String PROCESS_ACTIVATED_MSG = "processActivatedMsg";
	public static final String PROCESS_NOT_ACTIVATED_MSG = "processNotActivatedMsg";
	
	protected AuditAccessUtils auditAccessUtils;
	
	private EventServiceProxy eventService=null;
		
	public class RunnerThread extends Thread {
		private File file = null;	
		private HttpSession session=null;
		private Connection con=null;
		
		public RunnerThread(File file,HttpSession session,Connection con) {
			this.file = file;
			this.session=session;
			this.con=con;
		}
		
		public void run() {
			logger.info(":service: Runner thread started succesfully");
				
		
			// registering the start execution event
			String startExecutionEventDescription = "${weka.execution.started}<br/>";
			
			String parametersList = "${weka.execution.parameters}<br/><ul>";
			Set paramKeys = params.keySet();
			Iterator paramKeysIt = paramKeys.iterator();
			while (paramKeysIt.hasNext()) {
				String key = (String) paramKeysIt.next();
				if (!key.equalsIgnoreCase("template") 
						&& !key.equalsIgnoreCase("document")
						&& !key.equalsIgnoreCase("processActivatedMsg")
						&& !key.equalsIgnoreCase("processNotActivatedMsg")
						&& !key.equalsIgnoreCase("userId")
						&& !key.equalsIgnoreCase("SPAGOBI_AUDIT_SERVLET")
						&& !key.equalsIgnoreCase("spagobicontext")
						&& !key.equalsIgnoreCase("SPAGOBI_AUDIT_ID")) {
					Object valueObj = params.get(key);
					parametersList += "<li>" + key + " = " + (valueObj != null ? valueObj.toString() : "") + "</li>";
				}
			}
			parametersList += "</ul>";

			
			Map startEventParams = new HashMap();				
			startEventParams.put(EventServiceProxy.EVENT_TYPE, EventServiceProxy.DOCUMENT_EXECUTION_START);
			startEventParams.put("document", params.get("document"));
			
			String startEventId = null;
			try {
				startEventId =eventService.fireEvent(startExecutionEventDescription + parametersList, startEventParams, WEKA_ROLES_HANDLER_CLASS_NAME, WEKA_PRESENTAION_HANDLER_CLASS_NAME);
			} catch (Exception e) {
				logger.error(":run: problems while registering the start process event", e);
			}
			
			// AUDIT UPDATE

			String auditId = (String) params.get("SPAGOBI_AUDIT_ID");
			String userId = (String) params.get("userId");
			AuditAccessUtils auditAccessUtils = (AuditAccessUtils) params.get("SPAGOBI_AUDIT_UTILS");
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,userId,auditId, new Long(System.currentTimeMillis()), null, 
					"EXECUTION_STARTED", null, null);


			Connection incon = con;   //(con!=null)?con: getConnection((String)params.get(INPUT_CONNECTION));
			Connection outcon =con;    // (con!=null)?con: getConnection((String)params.get(OUTPUT_CONNECTION));
			
			if (incon == null || outcon == null) {
				logger.error(":service:Cannot obtain"
						+ " connection for engine ["
						+ this.getClass().getName() + "] control"
						+ " configuration in engine-config.xml config file");
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,userId,auditId, new Long(System.currentTimeMillis()), null,
						"EXECUTION_FAILED", "No connection available", null);
				return;
			}
			
			
			
			Map endEventParams = new HashMap();				
			endEventParams.put(EventServiceProxy.EVENT_TYPE, EventServiceProxy.DOCUMENT_EXECUTION_END);
			endEventParams.put("document", params.get("document"));
			endEventParams.put(EventServiceProxy.START_EVENT_ID, startEventId);
			
			String endExecutionEventDescription = "";
			
			WekaKFRunner kfRunner = new WekaKFRunner(incon, outcon);
			
			logger.debug(":service:Start parsing file: " + file);
			try {
				kfRunner.loadKFTemplate(file);
				kfRunner.setWriteMode((String)params.get(WRITE_MODE));
				kfRunner.setKeyColumnNames(parseKeysProp((String)params.get(KEYS)));
				String versioning = (String)params.get(VERSIONING);
				if(versioning != null && versioning.equalsIgnoreCase("true")){
					logger.debug(":service:versioning activated");
					kfRunner.setVersioning(true);
					String str;
					if( (str = (String)params.get(VERSION_COLUMN_NAME)) != null) 
						kfRunner.setVersionColumnName(str);
					logger.debug(":service:version column name is " + kfRunner.getVersionColumnName());
					if( (str = (String)params.get(VERSION)) != null) 
						kfRunner.setVersion(str);
					logger.debug(":service:version is " + kfRunner.getVersion());
					
				}
				kfRunner.setupSavers();
				kfRunner.setupLoaders();
				logger.debug(":service:Getting loaders & savers infos ...");
				logger.debug( "\n" + Utils.getLoderDesc(kfRunner.getLoaders()) );
				logger.debug( "\n" + Utils.getSaverDesc(kfRunner.getSavers()) );
				logger.debug(":service:Executing knowledge flow ...");
				kfRunner.run(false, true);
				
				endExecutionEventDescription = "${weka.execution.executionOk}<br/>";
				endEventParams.put("operation-result", "success");
				
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,userId,auditId, new Long(System.currentTimeMillis()), null,
						"EXECUTION_PERFORMED", "Execution Performed", null);
				} catch (Exception e) {
				logger.error("Impossible to load/parse templete file",e);
				endExecutionEventDescription = "${weka.execution.executionKo}<br/>";
				endEventParams.put("operation-result", "failure");
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,userId,auditId, new Long(System.currentTimeMillis()), null,
						"EXECUTION_FAILED", e.getMessage(), null);
			}
			
			try {	
				eventService.fireEvent(endExecutionEventDescription + parametersList, endEventParams, WEKA_ROLES_HANDLER_CLASS_NAME, WEKA_PRESENTAION_HANDLER_CLASS_NAME);
			} catch (Exception e) {
			
				logger.error(":run: problems while registering the end process event", e);
			}
			file.delete();		
			
			// TODO put this block in a finally block
			try {	
				// is it correct to close connection retrived as JNDI resoureces ?
				if ((con != null) && (!con.isClosed())) con.close();
				if ((incon != null) && (!incon.isClosed())) incon.close();
				if ((outcon != null) && (!outcon.isClosed())) outcon.close();
			} catch (SQLException sqle) {
				logger.error("Problems closing connection", sqle);
			}
	    }				
	}
	
	
	/**
	 * Initialize the engine
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		logger.debug("Initializing SpagoBI Weka Engine...");

	}
	
	/**
	 * process weka execution requests
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.debug("Start processing a new request...");

		logger.debug("Reading request parameters...");

		params = new HashMap();
		Enumeration enumer = request.getParameterNames();
		String parName = null;
		String parValue = null;
		// legge i parametri dalla request e li inserisce nella mappa params....
		while (enumer.hasMoreElements()) {
			parName = (String) enumer.nextElement();
			parValue = request.getParameter(parName);
			addParToParMap(params, parName, parValue);
			logger.debug( ":service:Read "
					+ "parameter [" + parName + "] with value [" + parValue
					+ "] from request");
		}

		logger.debug(":service:Request parameters read sucesfully" + params);

		logger.info(":service:reading template file ...");
		
		IEngUserProfile profile = (IEngUserProfile) request.getSession().getAttribute(IEngUserProfile.ENG_USER_PROFILE);

		//String userId=(String)params.get("userId");
		//logger.debug("User ID ="+userId);
		
		// creo il proxy per invocare il servizio degli eventi...
		eventService=new EventServiceProxy((String)profile.getUserUniqueIdentifier(),request.getSession());
		
		File file = null;
		String message = null;
		Thread runner = null;

		auditAccessUtils = (AuditAccessUtils) request.getSession()
				.getAttribute("SPAGOBI_AUDIT_UTILS");
		
		
		
		//usa il profilo per prendere la connessione
		Connection con = getConnection(request.getSession(),(String)profile.getUserUniqueIdentifier(),(String)params.get("document"));
		try {
			ContentServiceProxy contentProxy=new ContentServiceProxy((String)profile.getUserUniqueIdentifier(),request.getSession());
			HashMap requestParameters = ParametersDecoder.getDecodedRequestParameters(request);
			Content template=contentProxy.readTemplate( (String)params.get("document"),requestParameters);
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			byte[] content = bASE64Decoder.decodeBuffer(template.getContent());

			logger.info( ":service:template file has been read succesfully");
			logger.info( ":service:saving tamplete file to local temp dir ...");
			file = File.createTempFile("weka", null);
			ParametersFiller.fill(new StringReader(new String(content)),
					new FileWriter(file), params);
			logger.info(":service:template file saved succesfully to a local temp dir");
			runner = new RunnerThread(file,request.getSession(),con);
			// lancio il processo!!!!!!!!!!!!
			runner.start();
			message = (String) params.get(PROCESS_ACTIVATED_MSG);
			logger.info(":service: Return the default waiting message");

		} catch (Exception e) {
			logger.error(":service: error while process startup", e);
			message = (String) params.get(PROCESS_NOT_ACTIVATED_MSG);

		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>\n");
		buffer.append("<head><title>Service Response</title></head>\n");
		buffer.append("<body>");
		buffer
				.append("<p style=\"text-align:center;font-size:13pt;font-weight:bold;color:#000033;\">");
		buffer.append(message);
		buffer.append("</p>");
		buffer.append("</body>\n");
		buffer.append("</html>\n");

		response.setContentLength(buffer.length());
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.print(buffer.toString());
		writer.flush();

		logger.info("service:Request processed");
	}

	
	
	
    /**
     * This method, based on the data sources table, gets a database connection
     * and return it
     * 
     * @return the database connection
     */
    private Connection getConnection(HttpSession session,String userId,String documentId) {
	logger.debug("IN.documentId:"+documentId);
	DataSourceServiceProxy proxyDS = new DataSourceServiceProxy(userId,session);
	IDataSource ds = proxyDS.getDataSource(documentId);
	Connection conn = null;
	try {
		conn =  ds.toSpagoBiDataSource().readConnection();
	} catch (Exception e) {
		logger.error("Impossible to retrive connection", e);
	} 
	return conn;
    }
	
    /**
     * Get the connection from JNDI
     * 
     * @param connectionConfig
     *                SourceBean describing data connection
     * @return Connection to database
     */
    private Connection getConnectionFromJndiDS(SpagoBiDataSource connectionConfig) {
	logger.debug("IN");	
	Connection connection = null;
	Context ctx;
	String resName = connectionConfig.getJndiName();
	logger.debug("resName:"+resName);
	try {
	    ctx = new InitialContext();
	    DataSource ds = (DataSource) ctx.lookup(resName);
	    connection = ds.getConnection();
	} catch (NamingException ne) {
	    logger.error("JNDI error", ne);
	} catch (SQLException sqle) {
	    logger.error("Cannot retrive connection", sqle);
	}finally{
	    logger.debug("OUT");
	}
	return connection;
    }

    /**
     * Get the connection using jdbc
     * 
     * @param connectionConfig
     *                SpagoBiDataSource describing data connection
     * @return Connection to database
     */
    private Connection getDirectConnection(SpagoBiDataSource connectionConfig) {
	logger.debug("IN");
	Connection connection = null;
	try {
	    String driverName = connectionConfig.getDriver();
	    Class.forName(driverName);
	    String url = connectionConfig.getUrl();
	    String username = connectionConfig.getUser();
	    String password = connectionConfig.getPassword();
	    connection = DriverManager.getConnection(url, username, password);
	} catch (ClassNotFoundException e) {
	    logger.error("Driver not found", e);
	} catch (SQLException e) {
	    logger.error("Cannot retrive connection", e);
	}finally{
		logger.debug("OUT");
	}
	return connection;
    }

	
	
	/**
	 * @param params
	 * @param parName
	 * @param parValue
	 */
	private void addParToParMap(Map params, String parName, String parValue) {
		logger.debug("IN");
		String newParValue;
		
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			newParValue = (String)values.get(0);
			
		} else {
			newParValue = parValue;
		}
		
		params.put(parName, newParValue);
		logger.debug("OUT");
	}



	
	private String[] parseKeysProp(String keysStr) {
		if(keysStr == null) return null;
		return keysStr.split(",");
	}
	


}
