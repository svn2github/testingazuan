<%@page import= "com.bo.wibean.*, 
				 com.bo.rebean.wi.*,
				 com.bo.infoview.*,
				 it.eng.spagobi.engines.bo.BOConstants"%>

<%   
	
	response.reset();
	response.setDateHeader("expires", 0);
	response.setContentType("image/gif");
	ReportEngine reportEngine = (ReportEngine)session.getAttribute(BOConstants.REPORTENGINE);
  	//String storageToken = (String)session.getAttribute(BOConstants.STORAGETOKEN);
  	//DocumentInstance document = reportEngine.getDocumentFromStorageToken(storageToken);
	DocumentInstance document = (DocumentInstance)session.getAttribute(BOConstants.BODOCUMENT);
	String strImageName = request.getParameter("image");
	Image objImage = document.getImage(strImageName);
	byte[] abyBinaryContent = objImage.getBinaryContent();
	ServletOutputStream objServletOutputStream = response.getOutputStream();
	response.setContentLength(abyBinaryContent.length);
	objServletOutputStream.write(abyBinaryContent);
	objServletOutputStream.flush();
	objServletOutputStream.close();

%>