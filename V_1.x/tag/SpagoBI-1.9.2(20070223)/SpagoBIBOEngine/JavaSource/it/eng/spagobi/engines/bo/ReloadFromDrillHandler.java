package it.eng.spagobi.engines.bo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReloadFromDrillHandler {
	public void handle(HttpServletRequest request, HttpServletResponse response, 
	           ServletContext servletContext) throws ServletException, IOException {
		String jspexecution = "/jsp/viewDocumentHTML.jsp";
		RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
		disp.forward(request, response);
	}
}
