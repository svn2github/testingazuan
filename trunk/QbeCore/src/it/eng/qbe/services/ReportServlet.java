/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.services;

import it.eng.qbe.action.ExecuteSaveQueryAction;
import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.export.IQueryRewriter;
import it.eng.qbe.export.ITemplateBuilder;
import it.eng.qbe.export.ReportRunner;
import it.eng.qbe.export.SQLTemplateBuilder;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.JarUtils;
import it.eng.qbe.utility.Logger;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * @author Gioia
 *
 */
public class ReportServlet extends HttpServlet{
	
	static Map extensions;
	static {
		extensions = new HashMap();
		extensions.put("text/jrxml", "jrxml");
		extensions.put("text/html", "html");
		extensions.put("text/xml", "xml");
		extensions.put("text/plain", "txt");
		extensions.put("text/csv", "csv");
		extensions.put("application/pdf", "pdf");
		extensions.put("application/rtf", "rtf");
		extensions.put("application/vnd.ms-excel", "xls");
	}
	
	public static final String ACTION_PARAMETER 				= "action";
	public static final String INLINE_PARAMETER 				= "inline";
	public static final String QUERYNAME_PARAMETER 				= "queryName";
	public static final String QUERY_PARAMETER 					= "query";
	public static final String QUERY_LANG_PARAMETER 			= "lang";
	public static final String JNDI_DATASOURCE_NAME_PARAMETER 	= "jndiDataSourceName";
	public static final String DIALECT_PARAMETER 				= "dialect";
	public static final String JAR_FILE_PATH_PARAMETER 			= "jarfilepath";
	public static final String FORMAT_PARAMETER 				= "format";
	
	
	private boolean isStringEmpty(String str) {
		return (str == null || str.trim().equalsIgnoreCase(""));
	}
	
	
	private boolean getParameterAsBoolean(HttpServletRequest request, String paramName) {
		if( isStringEmpty(paramName) ) return false;
		String paramValue = (String)request.getParameter(paramName);
		return (paramValue != null && paramValue.equalsIgnoreCase("true"));
	}
	
	private String getParameterAsString(HttpServletRequest request, String paramName) {
		if( isStringEmpty(paramName) ) return null;
		return (String)request.getParameter(paramName);
	}
	
	private boolean isInline(HttpServletRequest request) {
		return getParameterAsBoolean(request, INLINE_PARAMETER);
	}
	
	private String getQueryName(HttpServletRequest request) {
		return getParameterAsString(request, QUERYNAME_PARAMETER);
	}
	
	private String getQuery(HttpServletRequest request) {
		return getParameterAsString(request, QUERY_PARAMETER);
	}
	
	private boolean isQueryDefined(HttpServletRequest request) {
		return ( isStringEmpty( getQuery(request) ) == false
				&& getQuery(request).equalsIgnoreCase("null") == false);
	}
	
	private String getJNDIDataSourceName(HttpServletRequest request) {
		return getParameterAsString(request, JNDI_DATASOURCE_NAME_PARAMETER);
	}
	
	private String getDataSourceSQLDialect(HttpServletRequest request) {
		return getParameterAsString(request, DIALECT_PARAMETER);
	}
	
	private File getJarFile(HttpServletRequest request) {
		String jarFileStr = getParameterAsString(request, JAR_FILE_PATH_PARAMETER);
		return new File(jarFileStr);
	}

	private String getFormat(HttpServletRequest request) {
		String format = getParameterAsString(request, FORMAT_PARAMETER);
		if(format == null) format = "application/pdf";
		return format;
	}
	
	private String getQueryLang(HttpServletRequest request) {
		return getParameterAsString(request, QUERY_LANG_PARAMETER);
	}
	
	private boolean isSQLQuery(HttpServletRequest request) {
		return (isStringEmpty(getQueryLang(request)) == false 
				&& getQueryLang(request).equalsIgnoreCase("SQL"));
	}
	
	private boolean isHQLQuery(HttpServletRequest request) {
		return (isStringEmpty(getQueryLang(request)) == false 
				&& getQueryLang(request).equalsIgnoreCase("HQL"));
	}
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 
	
	/**
	 * Handle an export request of a QBE query resultset. First generates a jasper report template. Than compile &
	 * fill it. In the end exports the filled report to the target export format. If the parameter <i>action</i> is equal
	 * to <i>buildTemplate</i> it juat return the report template
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Logger.debug(this.getClass(), "service: start method service");		
				
		String queryName = getQueryName(request);
				
		
		if(isQueryDefined(request) == false) {
			copyMessageToResponse(response, "Query is not defined !!!");
			return;
		}
				
		String format = getFormat(request);
		
			
		
		Session aSession = null;
		
		File templateFile = null; 
		File reportFile = null; 
		File resultFile = null;
		try{
			aSession = getHibernateSession(request);
			Connection connection = aSession.connection();		
			
			setJasperClasspath(getJarFile(request));
			
			templateFile = File.createTempFile("report", ".jrxml"); 
			reportFile = File.createTempFile("report", ".rpt"); 
			resultFile = null;
			
			if( isSQLQuery(request) )
				buildTemplateFromSQLQuery(templateFile,getQuery(request), connection);
			else if( isHQLQuery(request) )
				buildTemplateFromHQLQuery(templateFile,getQuery(request), aSession, connection);
			else
				throw new ServletException("Query language not supported: " + getQueryLang(request));
			
			if(getAction(request) != null && getAction(request).equals("buildTemplate")){
				resultFile = templateFile;
				format = "text/jrxml";
				if(queryName == null) queryName = "reportTemplate";
			}
			else {
				ReportRunner runner = new ReportRunner();		
				
				runner.run(templateFile, reportFile, format, connection);
				
				resultFile = reportFile;
				if(queryName == null) queryName = "queryResults";
			}		
			
			copyFileToResponse(response, isInline(request), resultFile, queryName, format);
		
		}catch(Exception e) {
			e.printStackTrace();
			copyErrorMessageToResponse(response, e);
			
		}finally{
			if (templateFile != null && templateFile.exists())
				templateFile.delete();
			if (reportFile != null && reportFile.exists())
				reportFile.delete();
			if ((aSession != null) && (aSession.isOpen())) 
				aSession.close();
		}
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// QUERY Methods
	////////////////////////////////////////////////////////////////////////////////
	
	private String getAction(HttpServletRequest request) {
		return (String)request.getParameter(ACTION_PARAMETER);
	}
	
	
	
	
	public Map getParams() {
		Map map = new HashMap();
		SourceBean config = (SourceBean)ConfigSingleton.getInstance();
		List params = config.getAttributeAsList("QBE.TEMPLATE-BUILDER.PARAMETERS.PARAMETER");
		for(int i = 0; i < params.size(); i++) {
			SourceBean param = (SourceBean)params.get(i);			
			String pname = (String)param.getAttribute("name");
			String pvalue = (String)param.getAttribute("value");			
			map.put(pname, pvalue);
		}
		return map;
	}
	
	private void copyMessageToResponse(HttpServletResponse response, String msg) {
		response.setContentType("text/plain");
		response.setContentLength((int)msg.length());
		try {
			response.getWriter().print(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void copyErrorMessageToResponse(HttpServletResponse response, Exception exception) {
		copyMessageToResponse(response, exception.getMessage());
		Logger.error(ExecuteSaveQueryAction.class, exception);
	}
	
	private void copyFileToResponse(HttpServletResponse response, boolean inline, File file, String fileName, String fileFormat) throws IOException {
		response.setHeader("Content-Disposition", (inline?"inline":"attachment") + "; filename=\"" + fileName + "." + extensions.get(fileFormat) + "\";");
		response.setContentType(fileFormat);
		response.setContentLength((int)file.length());
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));			
		int b = -1;
		while((b = in.read()) != -1) {
			response.getOutputStream().write(b);
		}
		response.getOutputStream().flush();
		in.close();
	}
	
	
	
	private void buildTemplateFromHQLQuery(File templateFile, String query, Session session, Connection connection) throws Exception {
		IQueryRewriter queryRevriter = new HqlToSqlQueryRewriter(session);	
		String sqlQuery = queryRevriter.rewrite(query);
		ITemplateBuilder templateBuilder = new SQLTemplateBuilder(sqlQuery, connection, getParams());
		templateBuilder.buildTemplateToFile(templateFile);
	}
	
	private void buildTemplateFromSQLQuery(File templateFile, String sqlQuery, Connection connection) throws Exception {
		
		ITemplateBuilder templateBuilder = new SQLTemplateBuilder(sqlQuery, connection, getParams());
		templateBuilder.buildTemplateToFile(templateFile);
	}
	
	private Session getHibernateSession(HttpServletRequest request){
						
		Configuration cfg = null;
		if ( isStringEmpty( getJNDIDataSourceName(request) ) == false ) {
			// SE I PARAMETRI VENGONO PASSATI IN INPUT USO QUELLI			
			Logger.debug(ReportServlet.class, "getSession: connection properties defined by hand");
						
			cfg = new Configuration();
			
			cfg.setProperty("hibernate.dialect", getDataSourceSQLDialect(request));
			cfg.setProperty("hibernate.connection.datasource", getJNDIDataSourceName(request));
			cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");			
			
			Logger.debug(ReportServlet.class, "getSession: jar file obtained: " + getJarFile(request));			
			Logger.debug(ReportServlet.class, "getSession: current class loader updated");			
			
			updateCurrentClassLoader(getJarFile(request));
			cfg.addJar(getJarFile(request));			
			Logger.debug(ReportServlet.class, "getSession: add jar file to configuration");			
		
		} else {			
			// ALTRIMENTI CERCO I PARAMETRI DI CONFIGURAZIONE SUL FILE hibconn.properies
			URL hibConnPropertiesUrl = JarUtils.getResourceFromJarFile(getJarFile(request), "hibconn.properties") ;
			
			if (hibConnPropertiesUrl != null){
				Properties prop = new Properties();
				try{
					prop.load(hibConnPropertiesUrl.openStream());
				}catch (IOException ioe) {
					ioe.printStackTrace();
				}
			
				Logger.debug(DataMartModel.class, "getSession: connection properties loaded by hibconn.properties in jar");
				
				cfg = new Configuration();
				
				cfg.setProperty("hibernate.dialect", prop.getProperty("hibernate.dialect"));
				cfg.setProperty("hibernate.connection.datasource", prop.getProperty("hibernate.connection.datasource"));
				cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");			
								
				Logger.debug(ReportServlet.class, "getSession: jar file obtained: " + getJarFile(request));				
				Logger.debug(ReportServlet.class, "getSession: current class loader updated");	
				
				updateCurrentClassLoader(getJarFile(request));
				cfg.addJar(getJarFile(request));
				Logger.debug(ReportServlet.class, "getSession: add jar file to configuration");
			
			} else {			
				// ALTRIMENTI EFFETTUO LA CONFIGURAZIONE DAL FILE hibernate.cfg.cml
			
				// ---------------------- NOTA BENE
				// IN QUESTO CASO IL FILE DEVE CONTENERE I RIFERIMENTI A TUTTI GLI HBM
				Logger.debug(ReportServlet.class, "getSession: connection properties defined in hibernate.cfg.xml");
				updateCurrentClassLoader(getJarFile(request));
				Logger.debug(ReportServlet.class, "getSession: jar file obtained: " + getJarFile(request));
				
				Logger.debug(ReportServlet.class, "getSession: current class loader updated");
			
				Logger.debug(ReportServlet.class, "getSession: trying to read configuration from hibernate.cfg.xml file");
				URL url = JarUtils.getResourceFromJarFile(getJarFile(request), "hibernate.cfg.xml") ;
				Logger.debug(ReportServlet.class, "getSession: configuration file found at " + url);
				cfg = new Configuration().configure(url);
			
			}
		}
		
		return cfg.buildSessionFactory().openSession();
	}
	
	private void setJasperClasspath(File jarFile){
		// get the classpath used by JasperReprorts Engine (by default equals to WEB-INF/lib)
		String webinflibPath = this.getServletContext().getRealPath("WEB-INF") + System.getProperty("file.separator") + "lib";
		//logger.debug("JasperReports lib-dir is [" + this.getClass().getName()+ "]");
		
		// get all jar file names in the jasper classpath
		//logger.debug("Reading jar files from lib-dir...");
		StringBuffer jasperReportClassPathStringBuffer  = new StringBuffer();
		File f = new File(webinflibPath);
		String fileToAppend = null;
		
		if (f.isDirectory()){
			String[] jarFiles = f.list();
			for (int i=0; i < jarFiles.length; i++){
				String namefile = jarFiles[i];
				if(!namefile.endsWith("jar"))
					continue; // the inclusion of txt files causes problems
				fileToAppend = webinflibPath + System.getProperty("file.separator")+ jarFiles[i];
				//logger.debug("Appending jar file [" + fileToAppend + "] to JasperReports classpath");
				jasperReportClassPathStringBuffer.append(fileToAppend);
				jasperReportClassPathStringBuffer.append(System.getProperty("path.separator"));  
			}
		}
		
		String jasperReportClassPath = jasperReportClassPathStringBuffer.toString();
		jasperReportClassPath = jasperReportClassPath.substring(0, jasperReportClassPath.length() - 1);
		
		// set jasper classpath property
		System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
		//logger.debug("Set [jasper.reports.compile.class.path properties] to value [" + System.getProperty("jasper.reports.compile.class.path")+"]");	
		
		// append HibernateJarFile to jasper classpath
		if(jasperReportClassPath != null && !jasperReportClassPath.equalsIgnoreCase("")) 
			jasperReportClassPath += System.getProperty("path.separator");
		
		jasperReportClassPath += jarFile.toString();		
		System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
	
	}
	
	
	/**
	 * This method update the Thread Context ClassLoader adding to the class loader the jarFile
	 * @param jarFile
	 */
	public static void updateCurrentClassLoader(File jarFile){
		try{
			ClassLoader previous = Thread.currentThread().getContextClassLoader();
			ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
			Thread.currentThread().setContextClassLoader(current);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	

}
