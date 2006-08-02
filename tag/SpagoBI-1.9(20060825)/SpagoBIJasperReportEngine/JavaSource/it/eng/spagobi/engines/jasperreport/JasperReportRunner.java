/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.jasperreport;

import it.businesslogic.ireport.Report;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

import sun.misc.BASE64Encoder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Jasper Report implementation built to provide all methods to
 * run a report inside SpagoBI. It is the jasper report Engine implementation
 * for SpagoBI.
 * 
 * @author Gioia
 */
public class JasperReportRunner {
	
	private String templatePath = null;
	private String spagobibaseurl = null;
	private static transient Logger logger = Logger.getLogger(JasperReportRunner.class);
	
	/**
	 * Class Constructor
	 * 
	 * @param spagobibaseurl The basic url for SpagoBI
	 * @param templatePath The path for the report template
	 */
	public JasperReportRunner(String spagobibaseurl, String templatePath) {
		super();
		this.templatePath = templatePath;
		this.spagobibaseurl = spagobibaseurl;
	}
	
	/**
	 * This method, known all input information, runs a report with JasperReport 
	 * inside SpagoBI. it is the Jasper Report Engine's core method.
	 * 
	 * @param ds The input Data Source for the Report
	 * @param parameters The input parameters map
	 * @param servletContext The java servlet context object
	 * @param servletResponse The java http servlet response object
	 * @throws Exception If any Exception occurred
	 */
	public void runReport(Connection conn, Map parameters, OutputStream out, ServletContext servletContext, 
			                HttpServletResponse servletResponse, HttpServletRequest servletRequest) throws Exception {
		
		/* TODO Since this is the back-end (logic) part of the JasperEngine the direct use of  HttpServletResponse, 
		 * HttpServletRequest and ServletContext objects shuold be pushed back to JasperReportServlet that is 
		 * the front-end (control) part of the engine */		
		File[] compiledSubreports = null;
		try {								
			String tmpDirectory = System.getProperty("java.io.tmpdir");
			
			// get the classpath used by JasperReprorts Engine (by default equals to WEB-INF/lib)
			String webinflibPath = servletContext.getRealPath("WEB-INF") + System.getProperty("file.separator") + "lib";
			logger.debug("JasperReports lib-dir is [" + this.getClass().getName()+ "]");
			
			// get all jar file names in the jasper classpath
			logger.debug("Reading jar files from lib-dir...");
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
					logger.debug("Appending jar file [" + fileToAppend + "] to JasperReports classpath");
					jasperReportClassPathStringBuffer.append(fileToAppend);
					jasperReportClassPathStringBuffer.append(System.getProperty("path.separator"));  
				}
			}
			
			String jasperReportClassPath = jasperReportClassPathStringBuffer.toString();
			jasperReportClassPath = jasperReportClassPath.substring(0, jasperReportClassPath.length() - 1);
			
			// set jasper classpath property
			System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
			logger.debug("Set [jasper.reports.compile.class.path properties] to value [" + System.getProperty("jasper.reports.compile.class.path")+"]");
			
			
			// compile subreports
			String subreportsDir = servletContext.getRealPath("WEB-INF") + System.getProperty("file.separator") + "subrpttemp" + System.getProperty("file.separator");
			File destDir = new File(subreportsDir);
			destDir.mkdirs();
			compiledSubreports = compileSubreports(parameters, destDir);
						
			// set classloader
			ClassLoader previous = Thread.currentThread().getContextClassLoader();
			ClassLoader current = URLClassLoader.newInstance(new URL[]{destDir.toURL()}, previous);
			Thread.currentThread().setContextClassLoader(current); 
						
			// set tmpdir property
			System.setProperty("jasper.reports.compile.temp", tmpDirectory);
			logger.debug("Set [jasper.reports.compile.temp] to value [" + System.getProperty("jasper.reports.compile.temp")+"]");
			
			byte[] jcrContent = new SpagoBIAccessUtils().getContent(this.spagobibaseurl, this.templatePath);
			InputStream is = new java.io.ByteArrayInputStream(jcrContent);						
			logger.debug("Compiling template file ...");
			JasperReport report  = JasperCompileManager.compileReport(is);
			logger.debug("Template file compiled  succesfully");
			
			// Create the virtualizer
			JRFileVirtualizer virtualizer = null; 
			boolean isVirtualizationActive = false;
			SourceBean config = JasperReportConf.getInstance().getConfig();
			String active = (String)config.getAttribute("VIRTUALIZER.active");
			if(active != null) isVirtualizationActive = active.equalsIgnoreCase("true");
			
			if(isVirtualizationActive) {
				logger.debug("Virtualization of fill process is active");
				String maxSizeStr = (String)config.getAttribute("VIRTUALIZER.maxSize");
				int maxSize = 2; 
				if(maxSizeStr!=null) maxSize = Integer.parseInt(maxSizeStr);
				String dir = (String)config.getAttribute("VIRTUALIZER.dir");
				if(dir == null){
					dir = tmpDirectory;
				} else {
					if(!dir.startsWith("/")) {
						String contRealPath = servletContext.getRealPath("/");
						if(contRealPath.endsWith("\\")||contRealPath.endsWith("/")) {
							contRealPath = contRealPath.substring(0, contRealPath.length()-1);
						}
						dir = contRealPath + "/" + dir;
					}
				}
				dir = dir + System.getProperty("file.separator") + "jrcache";
				File file = new File(dir);
				file.mkdirs();
				logger.debug("Max page cached during virtualization process: " + maxSize);
				logger.debug("Dir used as storing area during virtualization: " + dir);
				virtualizer = new JRFileVirtualizer(maxSize, dir);
				parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
				virtualizer.setReadOnly(true);
			}
			
			logger.debug("Filling report ...");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
			logger.debug("Report filled succesfully");
						
			logger.debug("Exporting report ...");
			String outputType = (String) parameters.get("param_output_format");
			if(outputType == null) {
				logger.debug("Output type is not specified. Default type will be used");
				outputType = ExporterFactory.getDefaultType();
			}
			logger.debug("Output format is [" + outputType + "]");
			
			JRExporter exporter = ExporterFactory.getExporter(outputType);	
			String mimeType = ExporterFactory.getMIMEType(outputType);
			
			if(exporter != null) logger.debug("Configured exporter class [" + exporter.getClass().getName() + "]");
			else logger.debug("Exporter class [null]");
			logger.debug("Configured MIME type [" + mimeType + "]");
			
			// for base types use default exporter, mimeType and parameters if these 
			// are not specified by configuration file 
			if (outputType.equalsIgnoreCase("csv")) {
		    	if(mimeType == null) mimeType = "text/plain";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRCsvExporter();
		    } else if (outputType.equalsIgnoreCase("html")) {
		    	if(mimeType == null) mimeType = "text/html";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRHtmlExporter();
		    	exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		    	exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		    	// define the map structure for report images
		    	HashMap m_imagesMap = new HashMap();
		    	UUIDGenerator generator = UUIDGenerator.getInstance();
		    	UUID uuid = generator.generateTimeBasedUUID();
		    	String mapName = uuid.toString();
		    	servletRequest.getSession().setAttribute(mapName, m_imagesMap);
		    	exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,m_imagesMap);
		    	exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image.jsp?mapname="+mapName+"&image=");
		    } else if (outputType.equalsIgnoreCase("xls")) {
		    	if(mimeType == null) mimeType = "application/vnd.ms-excel";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRXlsExporter();
		    } else if (outputType.equalsIgnoreCase("xml")) {
		    	if(mimeType == null) mimeType = "text/xml";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRXmlExporter();
		    } else if (outputType.equalsIgnoreCase("txt")) {
		    	if(mimeType == null) mimeType = "text/plain";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRTextExporter(); 
		    } else if (outputType.equalsIgnoreCase("pdf"))	{			
		    	if(mimeType == null) mimeType = "application/pdf";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRPdfExporter(); 	
		    } else if (outputType.equalsIgnoreCase("JPG"))	{	
		    	byte[] bytes = getImageBytes(report, jasperPrint);
		    	if(mimeType == null) mimeType = "application/jpeg";
		        out.write(bytes);
		    	return;
		    } else if (outputType.equalsIgnoreCase("JPGBASE64"))	{	
		    	byte[] bytes = getImagesBase64Bytes(report, jasperPrint);
		    	if(mimeType == null) mimeType = "text/plain";
		        out.write(bytes);
		    	return;
		    } else {
		    	if(mimeType != null && exporter != null) servletResponse.setContentType(mimeType);
		    	else {
		    		logger.warn("Impossible to load exporter for type " + outputType);
		    		logger.warn("Pdf exporter will be used");
		    		servletResponse.setContentType("application/pdf");
			    	exporter = new JRPdfExporter(); 
		    	}
		    }
			
			logger.debug("MIME type of response is [" + servletResponse.getContentType()+ "]");
			logger.debug("Exporter class used is  [" + exporter.getClass().getName()+ "]");
			
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
	        exporter.exportReport();
	        logger.debug("Report exported succesfully");
	        
	        
		}catch(Exception e){
			logger.error("An exception has occured", e);
			throw e;
		}finally{			
			// delate compiled subreport if any
	        if(compiledSubreports != null){
				for(int i = 0; i < compiledSubreports.length ; i++){
					File subreport = compiledSubreports[i];
					if(subreport != null) subreport.delete();
					logger.debug("Delating subreport file: " + subreport.getName());
				}
				
			}
	        
			try{			
				if ((conn != null) && (!conn.isClosed())) conn.close();
			}catch(SQLException sqle){}
			
		}
	}	
	
	
	
	private byte[] getImagesBase64Bytes(JasperReport report, JasperPrint jasperPrint) {
		byte[] bytes = new byte[0];
		try {
			String message = "<IMAGES>";
			List bufferedImages = generateReportImages(report, jasperPrint);
			Iterator iterImgs = bufferedImages.iterator();
			int count = 1;
			while(iterImgs.hasNext()){
				message += "<IMAGE page=\""+count+"\">";
				BufferedImage image = (BufferedImage)iterImgs.next();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
				encoder.encode(image);
				byte[] byteImg = baos.toByteArray();
				baos.close();
				BASE64Encoder encoder64 = new BASE64Encoder();
				String encodedImage = encoder64.encode(byteImg);
				message += encodedImage;
				message += "</IMAGE>";
				count ++;
			}
			message += "</IMAGES>";
			bytes = message.getBytes();
		} catch (Exception e) {
			logger.error("Error while producing byte64 encoding of the report images", e);
		}
		return bytes;
	}
	
	
	
	private byte[] getImageBytes(JasperReport report, JasperPrint jasperPrint) {
		byte[] bytes = new byte[0];
		try {
			List bufferedImages = generateReportImages(report, jasperPrint);
			// calculate dimension of the final page
			Iterator iterImgs = bufferedImages.iterator();
			int totalHeight = 0;
			int totalWidth = 0;
			while(iterImgs.hasNext()){
				BufferedImage image = (BufferedImage)iterImgs.next();
				int hei = image.getHeight();
				int wid = image.getWidth();
				totalHeight += hei;
				totalWidth = wid;
			}
			// create an unique buffer image
			BufferedImage finalImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D finalGr2 = finalImage.createGraphics();
			// append all images to the final
			iterImgs = bufferedImages.iterator();
			int y = 0;
			int x = 0;
			while(iterImgs.hasNext()){
				BufferedImage image = (BufferedImage)iterImgs.next();
				int hei = image.getHeight();
				finalGr2.drawImage(image, new AffineTransform(1f,0f,0f,1f,x,y), null);
				y += hei;
			}
			// gets byte of the jpeg image 
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
			encoder.encode(finalImage);
			bytes = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			logger.error("Error while producing jpg image of the report", e);
		}
		return bytes;
	}
	
	
	
	
	
	private List generateReportImages(JasperReport report, JasperPrint jasperPrint) {
		List bufferedImages = new ArrayList();
		try{
			int height = report.getPageHeight();
			int width = report.getPageWidth();
	    	boolean export = true;
			int index = 0;
			while(export==true){
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		    	Graphics2D gr2 = image.createGraphics();
		    	JRExporter exporter = new JRGraphics2DExporter();
		    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		    	exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, gr2 );
				exporter.setParameter(JRGraphics2DExporterParameter.PAGE_INDEX, new Integer(index));
				try{
					exporter.exportReport();
				} catch(Exception e) {
					export = false;
					continue;
				}
				index++;
				bufferedImages.add(image);	
			}
		} catch (Exception e) {
			logger.error("Error while producing jpg images of the report", e);
		}
		return bufferedImages;
	}
	
	
	
	
	
	
	private File[] compileSubreports(Map params, File destDir) throws JRException {
		
		String subrptnumStr = (String)params.get("srptnum");
		int subrptnum = Integer.parseInt(subrptnumStr);
		String[] subreports = new String[subrptnum];
		File[] files = new File[subrptnum];
		
		Iterator it = params.keySet().iterator();
		while(it.hasNext()){
			String parName = (String)it.next();
			if(parName.startsWith("subrpt")) {
				int start = parName.indexOf('.') + 1;
				int end = parName.indexOf('.', start);				
				String numberStr = parName.substring(start, end);
				int number = Integer.parseInt(numberStr) - 1;
				subreports[number] = (String)params.get(parName);
				//logger.debug("JasperReports parse subreport number [" + start + " - " + end + "] : " +  numberStr);
				logger.debug("JasperReports subreport PATH : " +  params.get(parName));				
			}
		}
				
		for(int i = 0; i < subreports.length; i++) {
			InputStream is = null;
			
			byte[] jcrContent = new SpagoBIAccessUtils().getContent(this.spagobibaseurl, subreports[i]);
						
			is = new ByteArrayInputStream(jcrContent);
			JasperDesign  jasperDesign = JRXmlLoader.load(is);
			
			is = new java.io.ByteArrayInputStream(jcrContent);						
			files[i] = new File(destDir, jasperDesign.getName()+ ".jasper");
			logger.debug("Compiling template file: " + files[i]);
			FileOutputStream fos =  null;
			try {
				fos = new FileOutputStream(files[i]);
			} catch (FileNotFoundException e) {
				logger.error("Internal error in compiling subreport method", e);
			}
			JasperCompileManager.compileReportToStream(is, fos);			
			logger.debug("Template file compiled  succesfully");
		}
		
		return files;
	}
}
