<%@page import="it.eng.spagobi.engines.officedocument.SpagoBIOfficeDocumentInternalEngine"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.InputStream"%>
<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="it.eng.spagobi.services.content.bo.Content"%>
<%@page import="it.eng.spagobi.services.content.service.ContentServiceImplSupplier"%>
<%@page import="org.apache.log4j.Logger"%>

 <%! private static transient Logger logger=Logger.getLogger(SpagoBIOfficeDocumentInternalEngine.class);%>

<%
	logger.debug("IN");
	String userId = request.getParameter("userId");
	String documentId = request.getParameter("documentId");
	logger.debug("got parameters userId="+userId+" and documentId="+documentId.toString());
	
	ContentServiceImplSupplier c = new ContentServiceImplSupplier();
	Content template = c.readTemplate(userId, documentId);
	String templateFileName = template.getFileName();

	logger.debug("Template Read");

	if(templateFileName==null){
		logger.warn("Template has no name");
		templateFileName="";
	}
	
	response.setHeader("Cache-Control: ",""); // leave blank to avoid IE errors
	response.setHeader("Pragma: ",""); // leave blank to avoid IE errors 
	response.setHeader("content-disposition","attachment; filename="+templateFileName);	
	
	String fileExtension = null;
	int extindex = templateFileName.lastIndexOf(".");
	if (extindex != -1) fileExtension = templateFileName.substring(extindex + 1);
	if (fileExtension != null) {
	 		InputStream is;
	 		Thread t= Thread.currentThread();
	 		ClassLoader cl=t.getContextClassLoader();
	 		is=cl.getResourceAsStream("MIMEtypes-extensions.properties");
	 		Properties props = new Properties();
	 		props.load(is);
	 		String mimetype = props.getProperty(fileExtension);
	 		if (mimetype != null && !mimetype.trim().equals("")) {
	 			response.setContentType(mimetype.trim());
	 			logger.debug("Content type: "+mimetype.trim());
	 			}
	 	}
	else
	{
		logger.warn("no file extension");	
	}

	BASE64Decoder bASE64Decoder = new BASE64Decoder();
	byte[] templateContent = bASE64Decoder.decodeBuffer(template.getContent());
	response.setContentLength(templateContent.length);
	response.getOutputStream().write(templateContent);
	response.getOutputStream().flush();
	logger.debug("");
%>