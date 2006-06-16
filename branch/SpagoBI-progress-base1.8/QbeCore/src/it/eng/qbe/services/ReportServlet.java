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
import it.eng.qbe.utility.JarUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;

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
	
	String query;
	File jarFile;
	Session session;
	Connection connection;
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 
		
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String action = (String)request.getParameter("action");
		query = (String)request.getParameter("query");
		String jarFileStr = (String)request.getParameter("jarfilepath");
		String format = (String)request.getParameter("format");
		if(format == null) format = "application/pdf";
		
		jarFile = new File(jarFileStr);
		updateCurrentClassLoader();
		session = getHibernateSession();
		connection = session.connection();		
						
		setJasperClasspath();		
		
		// TODO build this file in a way to be sure that they are unique over multiple request
		File templateFile = new File("report.jrxml");
		File reportFile = new File("report");
		File resultFile = null;
		
		buildTemplateFromSQLQuery(templateFile);
		if(action != null && action.equals("buildTemplate")){
			resultFile = templateFile;
			format = "text/jrxml";
		}
		else {
			ReportRunner runner = new ReportRunner();		
			runner.run(templateFile, reportFile, format, connection);
			resultFile = reportFile;
		}
		
				
		response.setContentType(format);
		response.setContentLength((int)resultFile.length());
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(resultFile));			
		int b = -1;
		while((b = in.read()) != -1) {
			response.getOutputStream().write(b);
		}
		response.getOutputStream().flush();
		in.close();
		
		// instant cleaning
		templateFile.delete();
		reportFile.delete();
	}

	private Session getHibernateSession() {
		URL url = JarUtils.getResourceFromJarFile(jarFile, "hibernate.cfg.xml");
		Configuration cfg = new Configuration().configure(url);		
		return cfg.buildSessionFactory().openSession();
	}

	public void buildTemplateFromSQLQuery(File templateFile) {
		IQueryRewriter queryRevriter = new HqlToSqlQueryRewriter(session);	
		String sqlQuery = queryRevriter.rewrite(query);
		try {
			ITemplateBuilder templateBuilder = new SQLTemplateBuilder(sqlQuery, connection);
			templateBuilder.buildTemplateToFile(templateFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateCurrentClassLoader(){
		try{
			ClassLoader previous = Thread.currentThread().getContextClassLoader();
			ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
			Thread.currentThread().setContextClassLoader(current);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setJasperClasspath(){
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
