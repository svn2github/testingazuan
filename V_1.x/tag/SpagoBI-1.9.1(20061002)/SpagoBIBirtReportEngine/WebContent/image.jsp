<%--
/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
--%>
<%@ page import="java.io.*,
				 java.util.*,
				 javax.servlet.ServletOutputStream" %>

<%!
	private transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
%>

<%
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

	FileInputStream fis = new FileInputStream(imageFile);
	ServletOutputStream ouputStream = response.getOutputStream();
	byte[] buffer = new byte[1024];
	int len; 
	while ((len = fis.read(buffer)) >= 0) 
		ouputStream.write(buffer, 0, len);
	ouputStream.flush();
	ouputStream.close();
	fis.close();
	imageFile.delete();	
%>
