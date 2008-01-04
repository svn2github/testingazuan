package it.eng.spagobi.tools.importexport.services;

import it.eng.spago.base.SourceBean;
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

    public void service(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	try {
	    freezeHttpResponse();
	    HttpServletRequest httpRequest = getHttpRequest();
	    HttpServletResponse httpResponse = getHttpResponse();
	    String operation = (String) request.getAttribute("OPERATION");
	    if ((operation != null) && (operation.equalsIgnoreCase("download"))) {
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
	    String exportFilePath = (String) request.getParameter("PATH");
	    File exportedFile = new File(exportFilePath);
	    String exportFileName = exportedFile.getName();
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + exportFileName + "\";");
	    byte[] exportContent = "".getBytes();
	    FileInputStream fis = null;
	    try {
		fis = new FileInputStream(exportFilePath);
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
