/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.importexport.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DownloadFileAction extends AbstractHttpAction {

    static private Logger logger = Logger.getLogger(DownloadFileAction.class);

    /* (non-Javadoc)
     * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
     */
    public void service(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	try {
	    freezeHttpResponse();
	    HttpServletRequest httpRequest = getHttpRequest();
	    HttpServletResponse httpResponse = getHttpResponse();
	    String operation = (String) request.getAttribute("OPERATION");
	    if ((operation != null) && (operation.equalsIgnoreCase("downloadExportFile"))) {
		manageDownload(httpRequest, httpResponse, true);
		return;
	    } else if ((operation != null) && (operation.equalsIgnoreCase("downloadLog"))) {
		manageDownload(httpRequest, httpResponse, false);
		return;
	    } else if ((operation != null) && (operation.equalsIgnoreCase("downloadManualTask"))) {
		manageDownload(httpRequest, httpResponse, false);
		return;
	    }
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Handle a download request of an eported file. Reads the file, sends it as
     * an http response attachment and in the end deletes the file.
     * 
     * @param request
     *                the http request
     * @param response
     *                the http response
     * @param deleteFile
     *                if true delete the downloadedFile
     */
    private void manageDownload(HttpServletRequest request, HttpServletResponse response, boolean deleteFile) {
	logger.debug("IN");
	try {
	    String exportFileName = (String) request.getParameter("FILE_NAME");
	    String exportFileFolder = "";
	    ConfigSingleton conf = ConfigSingleton.getInstance();
	    SourceBean exporterSB = (SourceBean) conf.getAttribute("IMPORTEXPORT.EXPORTER");
	    String pathExportFolder = (String) exporterSB.getAttribute("exportFolder");
	    pathExportFolder = GeneralUtilities.checkForSystemProperty(pathExportFolder);
	    if (!pathExportFolder.startsWith("/") && pathExportFolder.charAt(1) != ':') {
	    	String root = ConfigSingleton.getRootPath();
	    	pathExportFolder = root + "/" + pathExportFolder;
	    }
	    File exportedFile = new File(exportFileFolder + "/" + exportFileName + ".zip");
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + exportFileName + ".zip\";");
	    byte[] exportContent = "".getBytes();
	    FileInputStream fis = null;
	    try {
		fis = new FileInputStream(exportedFile);
		exportContent = GeneralUtilities.getByteArrayFromInputStream(fis);
	    } catch (IOException ioe) {
		logger.error("Cannot get bytes of the exported file", ioe);
	    }
	    response.setContentLength(exportContent.length);
	    response.getOutputStream().write(exportContent);
	    response.getOutputStream().flush();
	    if (fis != null)
		fis.close();
	    if (deleteFile) {
		exportedFile.delete();
	    }
	} catch (IOException ioe) {
	    logger.error("Cannot flush response", ioe);
	} finally {
	    logger.debug("IN");
	}
    }

}
