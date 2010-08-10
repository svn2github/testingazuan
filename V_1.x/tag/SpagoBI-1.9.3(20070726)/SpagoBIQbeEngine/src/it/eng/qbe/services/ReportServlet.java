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
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
	
	public static final String ACTION_PARAMETER = "action";
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 
	
	public String getActionParameter(HttpServletRequest request) {
		return (String)request.getParameter(ACTION_PARAMETER);
	}
	
	/**
	 * Handle an export request of a QBE query resultset. First generates a jasper report template. Than compile &
	 * fill it. In the end exports the filled report to the target export format. If the parameter <i>action</i> is equal
	 * to <i>buildTemplate</i> it juat return the report template
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Logger.debug(this.getClass(), "service: start method service");		
		String action = getActionParameter(request);
		String inlineStr = (String)request.getParameter("inline");
		boolean inline = (inlineStr != null && inlineStr.equals("true"));
		String queryName = (String)request.getParameter("queryName");
		String query = (String)request.getParameter("query");
		if(query == null || query.equalsIgnoreCase("") || query.equalsIgnoreCase("null")) {
			copyMessageToResponse(response, "Query is not defined !!!");
			return;
		}
		
		String jarFileStr = (String)request.getParameter("jarfilepath");
		
		
		File jarFile = new File(jarFileStr);
		
		String jndiDataSourceName = (String)request.getParameter("jndiDataSourceName");
		String dialect = (String)request.getParameter("dialect");
		String format = (String)request.getParameter("format");
		String lang = (String)request.getParameter("lang");
		
		String savedQueryObjectId = (String)request.getParameter("_savedObjectId");
		
		String orderedfldList = (String)request.getParameter("orderedFldList");
		if (orderedfldList == null){
			orderedfldList = "";
		}
		
		String extractedEntitiesList = (String)request.getParameter("extractedEntitiesList");
		if (extractedEntitiesList == null){
			extractedEntitiesList = "";
		}
		if(format == null) format = "application/pdf";
		
		
		Session aSession = null;
		
		File templateFile = null; 
		File reportFile = null; 
		File resultFile = null;
		try{
			aSession = getSession(jarFile,jndiDataSourceName, dialect );
			Connection connection = aSession.connection();		
			
			setJasperClasspath(jarFile);
			
			templateFile = File.createTempFile("report", ".jrxml"); 
			reportFile = File.createTempFile("report", ".rpt"); 
			resultFile = null;
			
			
			boolean pagination = false;
			if(action == null 
					|| !action.equals("buildTemplate")){
				if(format.equalsIgnoreCase("text/html")
						|| format.equalsIgnoreCase("application/vnd.ms-excel")) pagination = true;
			}
			
			if(lang.equalsIgnoreCase("SQL"))
				buildTemplateFromSQLQuery(templateFile,query, pagination, connection, orderedfldList, extractedEntitiesList, jarFile.getParent(), savedQueryObjectId);
			else if(lang.equalsIgnoreCase("HQL"))
				buildTemplateFromHQLQuery(templateFile,query, pagination, aSession, connection, orderedfldList, extractedEntitiesList, jarFile.getParent(), savedQueryObjectId);
			else
				throw new ServletException("Query language not supported: " + lang);
		
			
			if(action != null && action.equals("buildTemplate")){
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
			
			copyFileToResponse(response, inline, resultFile, queryName, format);
		
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
	private void buildTemplateFromHQLQuery(File templateFile, String query, boolean pagination, Session session, Connection connection, String orderedFieldList,  String extractedEntitiesList, String formulaFilePath, String savedQueryObjectID) throws Exception {
		Map params = getParams();
		params.put("pagination", pagination?"true":"false");
		
		IQueryRewriter queryRevriter = new HqlToSqlQueryRewriter(session);	
		String sqlQuery = queryRevriter.rewrite(query);
		ITemplateBuilder templateBuilder = new SQLTemplateBuilder(sqlQuery, connection, params, orderedFieldList, extractedEntitiesList, formulaFilePath);
		if (savedQueryObjectID != null){
			((SQLTemplateBuilder)templateBuilder).fillCalculatedFields(savedQueryObjectID);
		}
		templateBuilder.buildTemplateToFile(templateFile);
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
	
	
	
	
	
	private void buildTemplateFromSQLQuery(File templateFile, String sqlQuery, boolean pagination, Connection connection, 
			String orderedFieldList, String extractedEntitiesList, String formulaFilePath, String savedQueryObjectID) throws Exception {
		
		Map params = getParams();
		params.put("pagination", pagination?"true":"false");
		
		ITemplateBuilder templateBuilder = new SQLTemplateBuilder(sqlQuery, connection, params,  
				orderedFieldList, extractedEntitiesList, formulaFilePath);
		if (savedQueryObjectID != null){
			((SQLTemplateBuilder)templateBuilder).fillCalculatedFields(savedQueryObjectID);
		}
		templateBuilder.buildTemplateToFile(templateFile);
	}
	
	public Session getSession(File jarFile, String jndiDataSourceName, String dialect){
		
		Configuration cfg = initHibernateConfiguration(jarFile, jndiDataSourceName, dialect);
		return cfg.buildSessionFactory().openSession();

	}
	
	public Configuration initHibernateConfiguration(File jarFile, String jndiDataSourceName, String dialect){
		Configuration cfg = null;
	
		List viewJarFiles = getViewJarFilesInDirectory(jarFile.getParent());
		if (jndiDataSourceName != null && !jndiDataSourceName.equalsIgnoreCase("")) {
		// SE I PARAMETRI VENGONO PASSATI IN INPUT USO QUELLI
		
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: connection properties defined by hand");
		
		
			cfg = new Configuration();
			cfg.setProperty("hibernate.dialect", dialect);
			cfg.setProperty("hibernate.connection.datasource", jndiDataSourceName);
			cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");			
		
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: jar file obtained: " + jarFile);
		
			
			
		}else {
		
			// ALTRIMENTI CERCO I PARAMETRI DI CONFIGURAZIONE SUL FILE hibconn.properies
			URL hibConnPropertiesUrl = JarUtils.getResourceFromJarFile(jarFile, "hibconn.properties") ;
			if (hibConnPropertiesUrl != null){
				Properties prop = new Properties();
				try{
					prop.load(hibConnPropertiesUrl.openStream());
				}catch (IOException ioe) {
					ioe.printStackTrace();
				}
		
			
			
				cfg = new Configuration();
				cfg.setProperty("hibernate.dialect", prop.getProperty("hibernate.dialect"));
				cfg.setProperty("hibernate.connection.datasource", prop.getProperty("hibernate.connection.datasource"));
				cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");
				
				updateCurrentClassLoader(jarFile);
				for (int i=0; i < viewJarFiles.size(); i++){
					updateCurrentClassLoader((File)viewJarFiles.get(i));
				}
				cfg.addJar(jarFile);
				for (int i=0; i < viewJarFiles.size(); i++){
					cfg.addJar((File)viewJarFiles.get(i));
				}
			}else {
		
				// ALTRIMENTI EFFETTUO LA CONFIGURAZIONE DAL FILE hibernate.cfg.cml
		
				// ---------------------- NOTA BENE
				// IN QUESTO IL FILE DEVE CONTENERE I RIFERIMENTI A TUTTI GLI HBM
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: connection properties defined in hibernate.cfg.xml");
						
			
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: current class loader updated");
		
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: trying to read configuration from hibernate.cfg.xml file");
				URL url = JarUtils.getResourceFromJarFile(jarFile, "hibernate.cfg.xml") ;
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: configuration file found at " + url);
			
			
				cfg = new Configuration().configure(url);
			}
		}
		updateCurrentClassLoader(jarFile);
		for (int i=0; i < viewJarFiles.size(); i++){
			updateCurrentClassLoader((File)viewJarFiles.get(i));
		}
		cfg.addJar(jarFile);
		for (int i=0; i < viewJarFiles.size(); i++){
			cfg.addJar((File)viewJarFiles.get(i));
		}
		return cfg;
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
	
	public List getViewJarFilesInDirectory(String directory){
		List files = new ArrayList();
//		String qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
//		String directory = qbeDataMartDir + System.getProperty("file.separator") + dataMartPath + System.getProperty("file.separator");
		File dir = new File(directory);
//	   	 It is also possible to filter the list of returned files.
	       // This example does not return any files that start with `.'.
	       FilenameFilter filter = new FilenameFilter() {
	           public boolean accept(File dir, String name) {
	               return name.endsWith(".jar") && !name.equalsIgnoreCase("datamart.jar");
	           }
	       };
	    
	       String[] children = dir.list(filter);
           if (children == null) {
               // Either dir does not exist or is not a directory
           } else {
               for (int i=0; i<children.length; i++) {
                   // Get filename of file or directory
                   String filename = children[i];
                   files.add(new File(dir, filename));
               }
           }
          return files;
	}
	

}
