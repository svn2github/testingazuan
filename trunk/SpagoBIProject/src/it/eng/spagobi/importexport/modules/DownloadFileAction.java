package it.eng.spagobi.importexport.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;


public class DownloadFileAction extends  AbstractHttpAction {

	public void service(SourceBean request, SourceBean response) throws Exception {
		try{
			freezeHttpResponse();
			HttpServletRequest httpRequest = getHttpRequest();
			HttpServletResponse httpResponse = getHttpResponse();
			String operation = (String)request.getAttribute("OPERATION");
			if((operation!=null) && (operation.equalsIgnoreCase("download"))){
				manageDownload(httpRequest, httpResponse, true);
				return;
			} else if((operation!=null) && (operation.equalsIgnoreCase("downloadLog"))) {
				manageDownload(httpRequest, httpResponse, false);
				return;
			} else if((operation!=null) && (operation.equalsIgnoreCase("downloadManualTask"))) {
				manageDownload(httpRequest, httpResponse, false);
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

}
