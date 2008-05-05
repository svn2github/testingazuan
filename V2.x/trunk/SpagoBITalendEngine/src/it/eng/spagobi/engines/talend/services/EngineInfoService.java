/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.services;

import it.eng.spagobi.engines.talend.SpagoBITalendEngine;
import it.eng.spagobi.engines.talend.runtime.Job;
import it.eng.spagobi.engines.talend.runtime.JobDeploymentDescriptor;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

/**
 * @author Andrea Gioia
 *
 */
public class EngineInfoService extends HttpServlet {
	
	private static transient Logger logger = Logger.getLogger(EngineInfoService.class);
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("Starting EngineInfoService service method...");
		
		String engineRootDir = getServletContext().getRealPath("WEB-INF");
		SpagoBITalendEngine.getInstance().getConfig().setEngineRootDir(new File(engineRootDir));
		
		SpagoBITalendEngine engine = SpagoBITalendEngine.getInstance();
		
		String infoType = request.getParameter("infoType");
		if(infoType.equalsIgnoreCase("version")) {
			response.getOutputStream().write(engine.getVersion().getBytes());
		} else if(infoType.equalsIgnoreCase("complianceVersion")) {
				response.getOutputStream().write(engine.getComplianceVersion().getBytes());
		} else if (infoType.equalsIgnoreCase("name")) {
			response.getOutputStream().write(engine.getFullName().getBytes());
		} else {
			response.getOutputStream().write(engine.getInfo().getBytes());
		}
		
	}
}

