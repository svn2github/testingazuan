/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.jasperreport;

import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.services.proxy.SecurityServiceProxy;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Decoder;

/**
 * Process jasper report execution requests and returns bytes of the filled
 * reports
 */
public class JasperReportServlet extends HttpServlet {

    static Map extensions;
    static {
	extensions = new HashMap();
	extensions.put("jrxml", "text/jrxml");
	extensions.put("html", "text/html");
	extensions.put("xml", "text/xml");
	extensions.put("txt", "text/plain");
	extensions.put("csv", "text/csv");
	extensions.put("pdf", "application/pdf");
	extensions.put("rtf", "application/rtf");
	extensions.put("xls", "application/vnd.ms-excel");
    }

    /**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(JasperReportServlet.class);

    /**
     * Initialize the engine
     */
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	logger.debug(this.getClass().getName() + ":init:Initializing SpagoBI JasperReport Engine...");
    }

    /**
     * process jasper report execution requests
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	logger.debug(this.getClass().getName() + ":service:Start processing a new request...");

	// USER PROFILE
	String documentId = (String) request.getParameter("document");
	HttpSession session = request.getSession();
	IEngUserProfile profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	/*
	String userId = (String) request.getParameter("userId");
	IEngUserProfile profile = null;
	try {
	    HttpSession session = request.getSession();
	    profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	    if (profile == null) {
		SecurityServiceProxy proxy = new SecurityServiceProxy();
		profile = proxy.getUserProfile(userId);
		session.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
	    }
	} catch (SecurityException e1) {
	    logger.error("SecurityException", e1);
	    throw new ServletException();
	}
*/
	Map params = new HashMap();
	Enumeration enumer = request.getParameterNames();
	String parName = null;
	String parValue = null;
	logger.debug(this.getClass().getName() + ":service:Reading request parameters...");
	while (enumer.hasMoreElements()) {
	    parName = (String) enumer.nextElement();
	    parValue = request.getParameter(parName);
	    addParToParMap(params, parName, parValue);
	    logger.debug(this.getClass().getName() + ":service:Read " + "parameter [" + parName + "] with value ["
		    + parValue + "] from request");
	}
	logger.debug(this.getClass().getName() + ":service:Request parameters read sucesfully" + params);

	// AUDIT UPDATE
	String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
	AuditAccessUtils auditAccessUtils = (AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
	if (auditAccessUtils != null)
	    auditAccessUtils.updateAudit((String) profile.getUserUniqueIdentifier(), auditId, new Long(System
		    .currentTimeMillis()), null, "EXECUTION_STARTED", null, null);

	String spagobibase = (String) params.get("spagobiurl");
	JasperReportRunner jasperReportRunner = new JasperReportRunner(spagobibase);
	Connection con = getConnection(documentId, new String(""));
	if (con == null) {
	    logger.error(this.getClass().getName() + ":service:Cannot obtain" + " connection for engine ["
		    + this.getClass().getName() + "] control" + " configuration in engine-config.xml config file");
	    // AUDIT UPDATE
	    if (auditAccessUtils != null)
		auditAccessUtils.updateAudit((String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System
			.currentTimeMillis()), "EXECUTION_FAILED", "No connection available", null);
	    return;
	}
	try {
	    String outputType = (String) params.get("param_output_format");
	    String tmpdir = (String) EnginConf.getInstance().getConfig().getAttribute("GENERALSETTINGS.tmpdir");
	    if (!tmpdir.startsWith("/")) {
		String contRealPath = getServletContext().getRealPath("/");
		if (contRealPath.endsWith("\\") || contRealPath.endsWith("/")) {
		    contRealPath = contRealPath.substring(0, contRealPath.length() - 1);
		}
		tmpdir = contRealPath + "/" + tmpdir;
	    }
	    tmpdir = tmpdir + System.getProperty("file.separator") + "reports";
	    File dir = new File(tmpdir);
	    dir.mkdirs();
	    File tmpFile = File.createTempFile("report", "." + outputType, dir);
	    OutputStream out = new FileOutputStream(tmpFile);
	    jasperReportRunner.runReport(con, params, out, getServletContext(), response, request);
	    out.flush();
	    out.close();

	    if (outputType == null)
		outputType = ExporterFactory.getDefaultType();
	    response.setHeader("Content-Disposition", "filename=\"report." + outputType + "\";");
	    // response.setContentType((String)extensions.get(outputType));
	    response.setContentLength((int) tmpFile.length());

	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(tmpFile));
	    int b = -1;
	    while ((b = in.read()) != -1) {
		response.getOutputStream().write(b);
	    }
	    response.getOutputStream().flush();
	    in.close();
	    // instant cleaning
	    tmpFile.delete();

	    // AUDIT UPDATE
	    if (auditAccessUtils != null)
		auditAccessUtils.updateAudit((String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System
			.currentTimeMillis()), "EXECUTION_PERFORMED", null, null);
	} catch (Exception e) {
	    logger.error(this.getClass().getName() + ":service:error " + "during report production \n\n " + e);
	    // AUDIT UPDATE
	    if (auditAccessUtils != null)
		auditAccessUtils.updateAudit((String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System
			.currentTimeMillis()), "EXECUTION_FAILED", e.getMessage(), null);
	    return;
	}

	logger.info(this.getClass().getName() + ":service:Request processed");
    }

    /**
     * @param params
     * @param parName
     * @param parValue
     */
    private void addParToParMap(Map params, String parName, String parValue) {
	String newParValue;

	ParametersDecoder decoder = new ParametersDecoder();
	if (decoder.isMultiValues(parValue)) {
	    List values = decoder.decode(parValue);
	    newParValue = "";
	    for (int i = 0; i < values.size(); i++) {
		newParValue += (i > 0 ? "," : "");
		newParValue += values.get(i);
	    }

	} else {
	    newParValue = parValue;
	}

	params.put(parName, newParValue);
    }

    /**
     * This method, based on the data sources table, gets a database connection
     * and return it
     * 
     * @return the database connection
     */
    public Connection getConnection(String documentId, String engineLabel) {
	// calls service for gets data source object
	DataSourceServiceProxy proxyDS = new DataSourceServiceProxy();
	SpagoBiDataSource ds = proxyDS.getDataSource(documentId);
	// get connection
	String jndi = ds.getJndiName();
	if (jndi != null && !jndi.equals("")) {
	    return getConnectionFromJndiDS(ds);
	} else {
	    return getDirectConnection(ds);
	}
    }

    /**
     * Get the connection from JNDI
     * 
     * @param connectionConfig
     *                SourceBean describing data connection
     * @return Connection to database
     */
    private Connection getConnectionFromJndiDS(SpagoBiDataSource connectionConfig) {
	Connection connection = null;
	Context ctx;
	String resName = connectionConfig.getJndiName();
	try {
	    ctx = new InitialContext();
	    DataSource ds = (DataSource) ctx.lookup(resName);
	    connection = ds.getConnection();
	} catch (NamingException ne) {
	    logger.error("JNDI error", ne);
	} catch (SQLException sqle) {
	    logger.error("Cannot retrive connection", sqle);
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
	}
	return connection;
    }

    /**
     * Decode a Base64 String into a byte array
     * 
     * @param encoded
     *                String encoded with Base64 algorithm
     * @return byte array decoded
     */
    private byte[] decodeBase64(String encoded) {
	byte[] clear = null;
	try {
	    BASE64Decoder decoder = new BASE64Decoder();
	    clear = decoder.decodeBuffer(encoded);
	    return clear;
	} catch (IOException ioe) {
	    logger.error("Error during base64 decoding", ioe);
	}
	return clear;
    }



}
