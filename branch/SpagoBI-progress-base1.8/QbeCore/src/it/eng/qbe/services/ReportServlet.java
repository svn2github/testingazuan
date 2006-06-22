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

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.export.IQueryRewriter;
import it.eng.qbe.export.ITemplateBuilder;
import it.eng.qbe.export.ReportRunner;
import it.eng.qbe.export.SQLTemplateBuilder;
import it.eng.qbe.model.DataMartModel;
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

/**
 * @author Gioia
 *
 */
public class ReportServlet extends HttpServlet{
	
	String query;
	File jarFile;
	Session session;
	Connection connection;
	
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
		return (String)request.getParameter("ACTION_PARAMETER");
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
		query = (String)request.getParameter("query");
		String jarFileStr = (String)request.getParameter("jarfilepath");
		String jndiDataSourceName = (String)request.getParameter("jndiDataSourceName");
		String dialect = (String)request.getParameter("dialect");
		String format = (String)request.getParameter("format");
		if(format == null) format = "application/pdf";
		
		jarFile = new File(jarFileStr);
		updateCurrentClassLoader();
		session = getHibernateSession(jndiDataSourceName, dialect);
		connection = session.connection();		
						
		setJasperClasspath();		
		
		File templateFile = File.createTempFile("report", ".jrxml"); 
		File reportFile = File.createTempFile("report", ".rpt"); 
		File resultFile = null;
		
		buildTemplateFromSQLQuery(templateFile);
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
		
		// instant cleaning
		templateFile.delete();
		reportFile.delete();
	}
	
	
	public Map getParams() {
		Map map = new HashMap();
		SourceBean config = (SourceBean)ConfigSingleton.getInstance();
		List params = config.getAttributeAsList("QBE.TEMPLATE-BUILDER.PARAMETERS.PARAMETER");
		for(int i = 0; i < params.size(); i++) {
			SourceBean param = (SourceBean)params.get(i);
			System.out.println(param);
			String pname = (String)param.getAttribute("name");
			String pvalue = (String)param.getAttribute("value");
			System.out.println(pname + " = " + pvalue);
			map.put(pname, pvalue);
		}
		return map;
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
	
	private Session getHibernateSession(String jndiDataSourceName, String dialect) {
		/*
		URL url = JarUtils.getResourceFromJarFile(jarFile, "hibernate.cfg.xml");
		Configuration cfg = new Configuration().configure(url);		
		return cfg.buildSessionFactory().openSession();
		*/
		return DataMartModel.getHibernateConfiguration(jarFile, jndiDataSourceName, dialect).buildSessionFactory().openSession();
	}
	
	private void buildTemplateFromSQLQuery(File templateFile) {
		IQueryRewriter queryRevriter = new HqlToSqlQueryRewriter(session);	
		String sqlQuery = queryRevriter.rewrite(query);
		try {
			ITemplateBuilder templateBuilder = new SQLTemplateBuilder(sqlQuery, connection, getParams());
			templateBuilder.buildTemplateToFile(templateFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateCurrentClassLoader(){
		try{
			ClassLoader previous = Thread.currentThread().getContextClassLoader();
			ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
			Thread.currentThread().setContextClassLoader(current);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setJasperClasspath(){
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
	

}
