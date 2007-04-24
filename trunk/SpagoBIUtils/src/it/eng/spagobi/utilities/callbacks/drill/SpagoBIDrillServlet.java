package it.eng.spagobi.utilities.callbacks.drill;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class SpagoBIDrillServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String spagobiContextUrl = (String) session.getAttribute("spagobicontext");
		String url = spagobiContextUrl + "/servlet/AdapterHTTP?";
		url += "USERNAME=" + username;
		url += "&NEW_SESSION=TRUE";
		url += "&PAGE=DirectExecutionPage";
		url += "&DOCUMENT_LABEL=" + request.getParameter("DOCUMENT_LABEL");
	    String documentParameters = "";
	    Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
	    	String parurlname = (String) parameterNames.nextElement();
	    	if (parurlname.equalsIgnoreCase("DOCUMENT_LABEL")) 
	    		continue;
	    	String parvalue = request.getParameter(parurlname);
	    	documentParameters += "%26" + parurlname + "%3D" + parvalue;
	    }
	    url += "&DOCUMENT_PARAMETERS=" + documentParameters;
		
		try {
			response.sendRedirect(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		/*
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(spagobiContextUrl + "/servlet/AdapterHTTP");
	    httppost.addParameter("USERNAME", username);
	    httppost.addParameter("NEW_SESSION", "TRUE");
	    httppost.addParameter("PAGE", "DirectExecutionPage");
	    httppost.addParameter("DOCUMENT_LABEL", request.getParameter("DOCUMENT_LABEL"));
	    String documentParameters = "";
	    Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
	    	String parurlname = (String) parameterNames.nextElement();
	    	if (parurlname.equalsIgnoreCase("DOCUMENT_LABEL")) 
	    		continue;
	    	String parvalue = request.getParameter(parurlname);
	    	documentParameters += "&" + parurlname + "=" + parvalue;
	    }
	    httppost.addParameter("DOCUMENT_PARAMETERS", documentParameters);
	    // sends request to SpagoBI
	    try {
			int statusCode = client.executeMethod(httppost);
			byte[] responseBytes = httppost.getResponseBody();
		    httppost.releaseConnection();
			response.getOutputStream().write(responseBytes);
			response.getOutputStream().flush();	
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
	}
	
}
