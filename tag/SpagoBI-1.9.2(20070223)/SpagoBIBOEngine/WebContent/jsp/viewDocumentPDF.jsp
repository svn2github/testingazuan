<%@page import="com.bo.rebean.wi.Report,
                it.eng.spagobi.engines.bo.BOConstants,
                com.bo.rebean.wi.BinaryView,
                com.bo.rebean.wi.OutputFormatType,
                com.bo.wibean.WIServerImpl,
                com.bo.rebean.wi.ReportEngine,
                com.bo.rebean.wi.DocumentInstance,
                com.bo.rebean.wi.Reports"%>
        
<%
	response.setHeader("Content-Type", "application/pdf");
	response.setDateHeader("expires", 0);
	response.setHeader("pragma", "public");
	response.setHeader("Cache-Control", "public");	

	WIServerImpl wiServer =  (WIServerImpl)application.getAttribute(BOConstants.WEBISERVER);
	wiServer.onStartPage(request, response); 
	ReportEngine reportEngine = (ReportEngine)session.getAttribute(BOConstants.REPORTENGINE);
  	String storageToken = (String)session.getAttribute(BOConstants.STORAGETOKEN);
  	DocumentInstance document = reportEngine.getDocumentFromStorageToken(storageToken);
  	int selectedReport = document.getSelectedReport();
  	Reports reports = document.getReports();
  	Report report = reports.getItem(selectedReport);
	
	try{
	    //transform the repot into bytes
	    BinaryView reportBinView = (BinaryView) report.getView(OutputFormatType.PDF);
	    byte[] repBinaryContent = reportBinView.getContent();
	    //visualize the PDF report
	    try{
	      response.setHeader("Content-disposition", "inline; filename=\"" + java.net.URLEncoder.encode(report.getName(), "UTF-8") + ".pdf\"");
	    }catch(java.io.UnsupportedEncodingException e){
	      System.out.println("UnsupportedEncodingException caught");
	    } 
	    response.setContentLength(repBinaryContent.length);
	    ServletOutputStream objServletOutputStream = response.getOutputStream();
	    objServletOutputStream.write(repBinaryContent, 0, repBinaryContent.length);
	    objServletOutputStream.flush();
	    objServletOutputStream.close();
	    wiServer.onEndPage();
	} catch (java.io.IOException IOe){
		System.out.println("IOException caught");
	}
%>