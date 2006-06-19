<%@ page	extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPage"
			contentType="text/html; charset=ISO-8859-1"
			pageEncoding="ISO-8859-1"
			session="true"
			import="it.eng.spago.base.*"
			errorPage="../spago/serviceError.jsp"
%>
<%	
    ResponseContainer responseContainer = ResponseContainerAccess.getResponseContainer(request);
    SourceBean serviceResponse = responseContainer.getServiceResponse();
    RequestContainer requestContainer=RequestContainerAccess.getRequestContainer(request);
    SourceBean serviceRequest = requestContainer.getServiceRequest();
    byte[] rispostaServizio = (byte[]) serviceResponse.getAttribute("risposta_servizio");
    response.reset();
	response.setContentType("image/svg+xml");
	response.setContentLength(rispostaServizio.length);
	response.setHeader("Content-Disposition","inline;filename=report.svg");
	response.setHeader("Expires", "0");
	response.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
	response.setHeader("Cache-Control", "max-age=30");
	response.setHeader("Pragma", "public");
    ServletOutputStream outputStream = response.getOutputStream();
	outputStream.write(rispostaServizio);
	outputStream.flush();
	outputStream.close();
%>