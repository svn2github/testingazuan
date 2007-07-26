/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
/*
 * Created on 4-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.services;

import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.bo.dao.audit.AuditManager;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DashboardServlet extends HttpServlet{
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// AUDIT UPDATE
		Integer auditId = null;
		String auditIdStr = request.getParameter("SPAGOBI_AUDIT_ID");
		if (auditIdStr == null) {
			SpagoBITracer.warning("SpagoBI", getClass().getName(), "service:", "Audit record id not specified! " +
					"No operations will be performed");
		} else {
			SpagoBITracer.debug("SpagoBI", getClass().getName(), "service:", "Audit id = [" + auditIdStr + "]");
			auditId = new Integer(auditIdStr);
		}
		AuditManager auditManager = AuditManager.getInstance();
		if (auditId != null) {
			auditManager.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
					"EXECUTION_STARTED", null, null);
		}
		
		OutputStream out = null;
		DataConnection dataConnection = null;
	 	try{
	 		out = response.getOutputStream();
		 	String dataName = (String)request.getParameter("dataname");
		 	if((dataName!=null) && (!dataName.trim().equals(""))) {
		 		IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
		 		ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(dataName);
                String type = lov.getITypeCd();
                if(!type.equalsIgnoreCase("QUERY") && !type.equalsIgnoreCase("SCRIPT") && !type.equalsIgnoreCase("JAVA_CLASS")) {
                	SpagoBITracer.major("SpagoBI", this.getClass().getName(),
				             "Service", "Dashboard "+type+" lov Not yet Supported");
                	out.write(createErrorMsg(12, "Dashboard  "+type+" lov not yet supported"));
                	out.flush();
                } else {
                	String result = GeneralUtilities.getLovResult(dataName);
                	result = result.replaceAll("&lt;", "<");
                	result = result.replaceAll("&gt;", ">");
                	
                	// if the lov is a query trasform the result to lower case (for flash dashboard)
                	if(type.equalsIgnoreCase("QUERY")){
                		result = result.toLowerCase();
                	}
                	
                	// write the result into response
            		out.write(result.getBytes());
    		 		out.flush();
    		 		
    		 		// AUDIT UPDATE
    				auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
    						"EXECUTION_PERFORMED", null, null);
                }		
		 	} else {
		 		out.write(createErrorMsg(10, "Param dataname not found"));
		 		out.flush();
		 	}
	 	}catch(Exception e){
	 		SpagoBITracer.critical("SpagoBI",getClass().getName(),"service","Exception", e);
	 		// AUDIT UPDATE
	 		auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", e.getMessage(), null);
	 	} finally {
	 		if (dataConnection != null)
				try {
					dataConnection.close();
				} catch (EMFInternalError e) {
                	SpagoBITracer.major("SpagoBI", this.getClass().getName(),
				             "Service", "Error while processing dashboard request", e);
				}
	 	}
	 }
	
	private byte[] createErrorMsg(int code, String message) {
		String response = "<response><error><code>"+code+"</code>" +
				          "<message>"+message+"</message></error></response>";
		return response.getBytes();
	}

}
