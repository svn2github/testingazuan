<%--
Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
--%>

<%@page import="com.bo.rebean.wi.Report,
                it.eng.spagobi.engines.bo.BOConstants,
                com.bo.rebean.wi.BinaryView,
                com.bo.rebean.wi.OutputFormatType,
                com.bo.wibean.WIServerImpl,
                com.bo.rebean.wi.ReportEngine,
                com.bo.rebean.wi.DocumentInstance,
                com.bo.rebean.wi.Reports"%>
<%@page import="com.bo.wibean.WIPDFView"%>
<%@page import="com.bo.wibean.WISession"%>
<%@page import="com.bo.wibean.WIDocument"%>
        
<%

	//request = WIRequestWrapper.setCharacterEncoding(request, "UTF-8");

	WIServerImpl wiServer =  (WIServerImpl)application.getAttribute(BOConstants.WEBISERVER);
	wiServer.onStartPage(request, response); 
	
	WISession wiSession = (WISession) session.getAttribute(BOConstants.BOSESSION);
	String storageToken = (String)session.getAttribute(BOConstants.STORAGETOKEN);
	
	WIDocument wiDocument = wiSession.getDocumentFromStorageToken(storageToken);
	String reportType = wiDocument.getDocType();
	boolean isRep = reportType.equals("rep");
	boolean isWid = reportType.equals("wid");
	byte[] binaryContent = null;
	
	if (isWid) {
		ReportEngine reportEngine = (ReportEngine)session.getAttribute(BOConstants.REPORTENGINE);
	  	DocumentInstance document = reportEngine.getDocumentFromStorageToken(storageToken);
	  	int selectedReport = document.getSelectedReport();
	  	Reports reports = document.getReports();
	  	Report report = reports.getItem(selectedReport);
	    //transform the repot into bytes
	    BinaryView reportBinView = (BinaryView) report.getView(OutputFormatType.PDF);
	    binaryContent = reportBinView.getContent();
	}
	
	if (isRep) {
		WIPDFView pdfView = wiDocument.getPDFView();
		out.clear();
		binaryContent = pdfView.getContent("");
		
		// Per gli indici:
		//String pdfIndex = request.getParameter("PDFIndex");
		//if (pdfIndex == null || pdfIndex.trim().equals("")) pdfIndex = "";
		//else {
		//	String[] indexes = pdfView.getPDFIndexes();
		//	boolean indexFound = false;
		//	for (int i = 0; i < indexes.length; i++) {
		//		if (indexes[i].equals(pdfIndex)) {
		//			indexFound = true;
		//			break;
		//		}
		//	}
		//	if (!indexFound) {
		//		System.out.println("Index specified in input [" + pdfIndex + "] was not found. Creating PDF for all reports...");
		//		pdfIndex = "";
		//	}
		//}
		//binaryContent = pdfView.getContent(pdfIndex);
		// oppure:
		//String strReportIndex = getNonNullValue(request.getParameter("report"), "0");
		//int iReport = Integer.parseInt(strReportIndex);
		//WIPDFView wiPdfView = wiDocument.getPDFView();
		//String[] astrPdfIndexes = wiPdfView.getPDFIndexes();
		//out.clear();
		//byte[] abyBinaryContent = wiPdfView.getContent(astrPdfIndexes[iReport]);
		
	}
	
	try {
		//displays the PDF report
		//String repName = wiDocument.getName();
	    //response.setHeader("Content-disposition", "inline; filename=\"" + java.net.URLEncoder.encode(repName, "UTF-8") + ".pdf\"");
	    //  response.setHeader("content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(repName, "UTF-8") + ".pdf");
		
	    //response.setHeader("Content-Type", "application/pdf");
		response.setContentType("application/pdf");
		//response.setDateHeader("expires", 0);
		//response.setHeader("pragma", "no-cache");
		//response.setHeader("Cache-Control", "no-cache");
	    response.setContentLength(binaryContent.length);
	    ServletOutputStream objServletOutputStream = response.getOutputStream();
	    objServletOutputStream.write(binaryContent);
	    objServletOutputStream.flush();
	    objServletOutputStream.close();
	    wiServer.onEndPage();
	} catch (java.io.IOException ioe){
		ioe.printStackTrace();
	}
	

%>