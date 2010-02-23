package it.eng.spagobi.tools.downloadFiles.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.tools.importexport.ImportUtilities;
import it.eng.spagobi.tools.importexport.services.DownloadFileAction;

import org.apache.log4j.Logger;

public class DownloadZipAction extends AbstractHttpAction {

	static private Logger logger = Logger.getLogger(DownloadZipAction.class);

	static byte[] buf = new byte[1024]; 

	public static final String DIRECTORY_LOG="DIRECTORY_LOG";
	public static final String DATE_LOG="DATE_LOG";
	public static final String TIME_LOG="TIME_LOG";

	public void service(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		try {
			freezeHttpResponse();
			HttpServletRequest httpRequest = getHttpRequest();
			HttpServletResponse httpResponse = getHttpResponse();
			// get Attribute DATE, MINUTE, DIRECTORY
			String directory = (String) request.getAttribute(DIRECTORY_LOG);
			String date = (String) request.getAttribute(DATE_LOG);
			String time = (String) request.getAttribute(TIME_LOG);
			if(date==null || time==null){
				logger.error("time or date not specified");
				return;
			}

			logger.debug("earch file relative to date "+date.toString()+" and time "+time.toString());

			// generate the match
			String match=generateMatch(date, time);

			logger.debug("earch files whose name match "+match);


			// open directory
			if(directory==null){
				logger.error("search directory not specified");
				return;
			}

			File dir=new File(directory);
			if(!dir.isDirectory()){
				logger.error("Not a valid directory specified");
				return;
			}
			Vector<String> filesToZip=new Vector<String>();
			searchDateFiles(filesToZip, dir, match);


			Date today=(new Date());
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
            //date = (Date)formatter.parse("11-June-07");    
            String randomName = formatter.format(today);			
			randomName=randomName.replaceAll(" ", "_");
			randomName=randomName.replaceAll(":", "-");
			String directoryZip=System.getProperty("java.io.tmpdir");
			String fileZip=randomName+".zip";
			String pathZip=directoryZip+fileZip;
			pathZip=pathZip.replaceAll("\\\\","/");			
			directoryZip=directoryZip.replaceAll("\\\\", "/");

//			String directoryZip="C:/logs";
//			String fileZip="prova.zip";
//			String pathZip=directoryZip+"/"+fileZip;

			createZipFromFiles(filesToZip, pathZip, directory);

			manageDownloadZipFile(httpRequest, httpResponse, directoryZip, fileZip);

			//manageDownloadExportFile(httpRequest, httpResponse);
		} catch (Exception e) {
			logger.error("Error in writing the zip ",e);
		}finally {
			logger.debug("OUT");
		}
	}

	public String generateMatch(String date, String time){
		logger.debug("IN");
		String toReturn=null;

		date=date.replaceAll("\\\\", "-");
		date=date.replaceAll("/", "-");
		time=time.replaceAll(":", "-");
		toReturn=date+" "+time;
		logger.debug("OUT");

		return toReturn;
	}


	public void searchDateFiles(Vector<String> vector, File  file, String match){
		logger.debug("IN");
		if(file.isDirectory() && file.list()!=null && file.list().length!=0){
			for (int i = 0; i < file.list().length; i++) {
				String childFileName=file.list()[i];
				File childFile=new File(file.getAbsolutePath()+"/"+childFileName);
				if(!childFile.exists()){
					logger.warn(childFile.getName()+" not exists");
				}
				else{
					searchDateFiles(vector, childFile, match);
				}
			}
		}
		else{
			String fileName=file.getName();
			if(fileName.indexOf(match)!=-1){
				vector.add(file.getAbsolutePath());
			}
		}
		logger.debug("OUT");
	}





	public void createZipFromFiles(Vector<String> fileNames, String outputFileName, String folderName) throws IOException{
		logger.debug("IN");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFileName)); 
		// Compress the files 
		for (Iterator iterator = fileNames.iterator(); iterator.hasNext();) {
			String fileName = (String) iterator.next();

			FileInputStream in = new FileInputStream(fileName); 

			// The name to Inssert: remove the parameter folder
			int lastIndex=folderName.length();
			String fileToInsert=fileName.substring(lastIndex+1);

			logger.debug("Adding to zip entry "+fileToInsert);
			ZipEntry zipEntry=new ZipEntry(fileToInsert);

			// Add ZIP entry to output stream. 
			out.putNextEntry(zipEntry); 

			// Transfer bytes from the file to the ZIP file 
			int len; 
			while ((len = in.read(buf)) > 0) 
			{ 
				out.write(buf, 0, len); 
			} 
			// Complete the entry 
			out.closeEntry(); 
			in.close(); 
		} 
		// Complete the ZIP file 
		out.close(); 
		logger.debug("OUT");
	}

	/**
	 * Handle a download request of an importation zip file. Reads the file, sends it as
	 * an http response attachment.
	 */
	private void manageDownloadZipFile(HttpServletRequest request, HttpServletResponse response, String folderName, String exportFileName) {
		logger.debug("IN");
		try {
			String importBasePath = ImportUtilities.getImportTempFolderPath();
			String folderPath = importBasePath + "/" + folderName;
			String fileExtension = "zip";
			manageDownload(exportFileName, fileExtension, folderName, response, false);
		} catch (Exception e) {
			logger.error("Error while downloading importation log file", e);
		} finally {
			logger.debug("OUT");
		}
	}


	private void manageDownload(String fileName, String fileExtension, String folderPath, HttpServletResponse response, boolean deleteFile) {
		logger.debug("IN");
		try {
			File exportedFile = new File(folderPath + "/" + fileName);

			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "." + fileExtension + "\";");
			byte[] exportContent = "".getBytes();
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(exportedFile);
				exportContent = GeneralUtilities.getByteArrayFromInputStream(fis);
			} catch (IOException ioe) {
				logger.error("Cannot get bytes of the download file", ioe);
			}
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\";");		
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
			logger.debug("OUT");
		}
	}

}
