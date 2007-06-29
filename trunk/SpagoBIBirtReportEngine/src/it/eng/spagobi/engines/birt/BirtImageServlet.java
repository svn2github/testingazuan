/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class BirtImageServlet extends HttpServlet {

	private transient Logger logger = Logger.getLogger(this.getClass());
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		String imagePath = request.getParameter("imagePath");
		String imageDirectory = getServletContext().getRealPath(imagePath);
		String imageFileName = request.getParameter("imageID");
		if (imageDirectory == null || imageFileName == null) {
			logger.error("Image directory or image file name missing.");
			return;
		}
		String completeImageFileName = "";
		if (imageDirectory.endsWith("/"))
			completeImageFileName = imageDirectory + imageFileName;
		else completeImageFileName = imageDirectory + "/" + imageFileName;

		File imageFile = new File(completeImageFileName);
		
		if (imageDirectory.endsWith("/"))
			imageFile = new File(imageDirectory + imageFileName);
		else imageFile = new File(imageDirectory + "/" + imageFileName);
		
		if (imageFile == null || !imageFile.isFile()) {
			logger.error("File [" + completeImageFileName + "] not found.");
			return;
		}

		FileInputStream fis = null;
		ServletOutputStream ouputStream = null;
		try {
			fis = new FileInputStream(imageFile);
			ouputStream = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int len; 
			while ((len = fis.read(buffer)) >= 0) 
				ouputStream.write(buffer, 0, len);
			imageFile.delete();
		} catch (Exception e) {
			logger.error("Error writing image into servlet output stream", e);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("Error while closing FileInputStream on file " + completeImageFileName, e);
				}
			if (ouputStream != null) {
				try {
					ouputStream.flush();
					ouputStream.close();
				} catch (IOException e) {
					logger.error("Error flushing servlet output stream", e);
				}
			}
		}
	}

}
