/**
 * 
 */
package it.eng.qbe.export;

import it.eng.qbe.utility.JarUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Gioia
 *
 */
public class HJasperRunner {
	
	File jarFile;
	File templateFile;
	String query;
	ServletContext servletContext;
	
	public HJasperRunner(File jarFile, String query, ServletContext servletContext) {
		this.jarFile = jarFile;
		this.query = query;
		this.servletContext = servletContext;
		updateCurrentClassLoader();
		addJarToJasperClasspath();
	}
	
	public HJasperRunner(File jarFile, File templateFile) {
		this.jarFile = jarFile;
		this.templateFile = templateFile;
	}
	
	
	public void run() {
		if(templateFile != null) {
			buildPdfReportFromTemplate();
		}
		else {
			buildPdfReportFromQuery();
		}
	}
	
	public void buildPdfReportFromTemplate() {		
		runReportToPdfFile(compileReport(), 
				getParameters(), 
				changeExtension(templateFile, "pdf"));
	}
	
	public void buildPdfReportFromQuery() {		
		templateFile = new File("template.jrxml");
		buildTemplateFromQuery();
		buildPdfReportFromTemplate();
	}
	
	public void buildPdfReportFromQuery(File file) {		
		templateFile = file;
		buildTemplateFromQuery();		
		buildPdfReportFromTemplate();		
	}
	
	public void buildTemplateFromQuery() {			
		ITemplateBuilder templateBuilder = new HibernateTemplateBuilder(query, getHibernateSessionFactory());
		try {
			templateBuilder.buildTemplateToFile(templateFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JasperReport compileReport(){
		JasperReport report  = null;		
		
		try {
			report = JasperCompileManager.compileReport(templateFile.toString());
		} catch (JRException e) {
			e.printStackTrace();
		}		
		return report;
	}
	
	public Map getParameters() {
		Map params = null;
		
		Session session = getHibernateSessionFactory().openSession();
		params = new HashMap();
		params.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);	
		return params;
	}
	
	public void runReportToPdfFile(JasperReport report, Map params, File outFile) {
						
		byte[] output = null;
		try {
			output = JasperRunManager.runReportToPdf(report, params);
		} catch (JRException e) {
			e.printStackTrace();
			System.exit(1);
		}		
				
		try {
			FileOutputStream writer = new FileOutputStream(outFile);
			writer.write(output);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
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
	
	public void addJarToJasperClasspath() {
		
		// get the classpath used by JasperReprorts Engine (by default equals to WEB-INF/lib)
		String webinflibPath = servletContext.getRealPath("WEB-INF") + System.getProperty("file.separator") + "lib";
				
		// get all jar file names in the jasper classpath		
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
				jasperReportClassPathStringBuffer.append(fileToAppend);
				jasperReportClassPathStringBuffer.append(System.getProperty("path.separator"));  
			}
		}
		
		String jasperReportClassPath = jasperReportClassPathStringBuffer.toString();
		jasperReportClassPath = jasperReportClassPath.substring(0, jasperReportClassPath.length() - 1);
		System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
		
		
		jasperReportClassPath = System.getProperty("jasper.reports.compile.class.path");
		
		if(jasperReportClassPath != null && !jasperReportClassPath.equalsIgnoreCase("")) 
			jasperReportClassPath += System.getProperty("path.separator");
		jasperReportClassPath += jarFile.toString();		
		System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
	}
	
	public SessionFactory getHibernateSessionFactory() {
		URL url = JarUtils.getResourceFromJarFile(jarFile, "hibernate.cfg.xml");
		Configuration cfg = new Configuration().configure(url);		
		return cfg.buildSessionFactory();
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		File jarFile = null;
		File templateFile = null;
		String query = null;
		
		if (args.length < 2) {
			return;
		}

		jarFile = new File(args[0]);		
		templateFile = new File(args[1]);		
		query = "select\n"  + 
			"\taProduct.id.brandName as brandName,\n" +  
			"\taProduct.id.productName as productName,\n" +
			"\taProduct.id.grossWeight as grossWeight\n" +
			"from\n" +  
			"\tit.eng.foodmart.Product as aProduct\n" +  
			"order by\n" +  
			"\taProduct.id.brandName,\n" + 
			"\taProduct.id.grossWeight\n";
		
		
		query = "select\n"  + 
		"\taProduct.id.brandName,\n" +  
		"\taProduct.id.productName,\n" +
		"\taProduct.id.grossWeight\n" +
		"from\n" +  
		"\tit.eng.foodmart.Product as aProduct\n" +  
		"order by\n" +  
		"\taProduct.id.brandName,\n" + 
		"\taProduct.id.grossWeight\n";
		
		
		long start = System.currentTimeMillis();
		
		//HJasperRunner runner = new HJasperRunner(jarFile, templateFile);
		/*
		HJasperRunner runner = new HJasperRunner(jarFile, query);
		runner.run();				
		*/	
		System.out.println("Report creation time : "
					+ (System.currentTimeMillis() - start));
		
	}
	
	public File changeExtension(File file, String newExtension) {
		File newFile = null;
		String fileName = file.toString();
		int index = fileName.lastIndexOf(".");
		if(index != -1) {
			fileName = fileName.substring(0, index);
		}
		fileName += "." + newExtension; 
		
		newFile = new File(fileName);
		
		return newFile;
	}

}
