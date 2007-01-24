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
/*
 * Created on 4-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.IExportManager;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet used to manage export operation
 */
public class ExportServlet extends HttpServlet{
	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 
	
    /**
     * Service method definition which, based on a particular parameter, 
     * redirects the execution to a specific handler
     * 
     * @param request The http servlet request
     * @param response The http servlet response
     * @throws IOException If any exception occurred
     */
	public void service(HttpServletRequest request, HttpServletResponse response) {
		try{
			String operation = (String)request.getParameter("OPERATION");
			if((operation!=null) && (operation.equalsIgnoreCase("download"))){
				manageDownload(request, response, true);
				return;
			} else if((operation!=null) && (operation.equalsIgnoreCase("downloadLog"))) {
				manageDownload(request, response, false);
				return;
			} else if((operation!=null) && (operation.equalsIgnoreCase("downloadManualTask"))) {
				manageDownload(request, response, false);
				return;
			} 
		} finally {}
	}
		
	
	/**
	 * Handle a download request of an eported file. Reads the file, sends it as an http response attachment 
	 * and in the end deletes the file.
	 * @param request the http request
	 * @param response the http response
	 * @param deleteFile if true delete the downloadedFile
	 */
	private void manageDownload(HttpServletRequest request, HttpServletResponse response, boolean deleteFile) {
		try{	
			String exportFilePath = (String)request.getParameter("PATH");
			File exportedFile = new File(exportFilePath);
			String exportFileName = exportedFile.getName();
			response.setHeader("Content-Disposition","attachment; filename=\"" + exportFileName + "\";");
			byte[] exportContent = "".getBytes();
			FileInputStream fis = null;
			try{
				fis = new FileInputStream(exportFilePath);
				exportContent = GeneralUtilities.getByteArrayFromInputStream(fis);
			} catch (IOException ioe) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "manageDownload",
                        			  "Cannot get bytes of the exported file" + ioe);
			}
 			response.setContentLength(exportContent.length);
		 	response.getOutputStream().write(exportContent);
		 	response.getOutputStream().flush();
		 	if(fis!=null)
		 		fis.close();
		 	if(deleteFile) {
		 		exportedFile.delete();
		 	}
		} catch (IOException ioe) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "manageDownload",
		                           "Cannot flush response" + ioe);
		} finally {	}
	}

	/*
	private void sendError(HttpServletRequest request, HttpServletResponse response, Exception e){
		String respStr = "<html><body><center>";
		respStr += "<h1>Error</h1>";
		respStr += "<br/><br/>";
		respStr += e.getMessage();
		respStr += "</center></body></html>";
		try{
			response.getOutputStream().write(respStr.getBytes());
			response.getOutputStream().flush();
		} catch (Exception ex) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "sendError",
					               "Cannot flush response" + ex);
		}
	}*/
	

}	

