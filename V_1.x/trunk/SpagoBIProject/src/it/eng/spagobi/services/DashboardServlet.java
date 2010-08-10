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

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.bo.dao.audit.AuditManager;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
		

		
	 	try{
	 		
	 		// get the user profile
 			HttpSession httpSession = request.getSession();
            IEngUserProfile profile = (IEngUserProfile)httpSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);   	
	 		
	 		// get the mode (mode=single --> only one lov to execute, mode=list --> more than one lov to execute)
	 		// if the parameter mode is not present the single mode is the dafult
	 		String mode = (String)request.getParameter("mode");
	 		
	 		String result = "";
	 		
	 		if((mode==null) || !mode.equalsIgnoreCase("list")) {
		 		// ge the lov name
	 			String dataName = (String)request.getParameter("dataname");
	 			// if lov name is not present send an error
	 			if((dataName==null) || dataName.trim().equals("")) {
	 				response.getOutputStream().write(createErrorMsg(10, "Param dataname not found"));
	 				response.getOutputStream().flush();
			 		return;
	 			}
	 			// check if the lov is supported
	 			if(!isLovSupported(dataName, response)) {
	 				return;
	 			}
	 			// get the lov type
	 			String type = getLovType(dataName);
	 			// get the result
	            if(profile == null) {
	            	result = GeneralUtilities.getLovResult(dataName);
	            } else {
	            	result = GeneralUtilities.getLovResult(dataName, profile);
	            }
	            // if the lov is a query trasform the result to lower case (for flash dashboard)
	            if(type.equalsIgnoreCase("QUERY")){
	            	result = result.toLowerCase();
	            }
	            
	 		} else {
	 			Map lovMap =new HashMap();
	 			Enumeration paramNames = request.getParameterNames();
	 			while(paramNames.hasMoreElements()){
	 				String paramKey = (String)paramNames.nextElement();
	 				if(paramKey.startsWith("LovResLogName_")) {
	 					String logicalName = paramKey.substring(14);
	 					String paramValue = (String)request.getParameter(paramKey);
	 					if( !(paramValue==null) && !(paramValue.trim().equals("")) ) {
	 						lovMap.put(logicalName, paramValue);
	 					}
	 				}
	 			}
	 			result = GeneralUtilities.getLovMapResult(lovMap);
	 		}
	 		
	 		
	 		// replace special characters
            result = result.replaceAll("&lt;", "<");
            result = result.replaceAll("&gt;", ">");
            // write the result into response
            response.getOutputStream().write(result.getBytes());
            response.getOutputStream().flush();
    		// AUDIT UPDATE
    		auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
    								"EXECUTION_PERFORMED", null, null);	
		 	
		 	
	 	}catch(Exception e){
	 		SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE,getClass().getName(),"service","Exception", e);
	 		// AUDIT UPDATE
	 		auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", e.getMessage(), null);
	 	} 
	 }
	

	private String getLovType(String lovName) {
		String toReturn = "";
		try{
			IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
			ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(lovName);
		    String type = lov.getITypeCd();
		    toReturn = type;
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
                                "getLovType", "Error while recovering type of lov " + lovName);
		}
	    return toReturn;
	}

	
	private boolean isLovSupported(String lovName, HttpServletResponse response) {
		boolean toReturn = true;
		try {
			IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
	 		ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(lovName);
	        String type = lov.getITypeCd();
	        if(!type.equalsIgnoreCase("QUERY") && !type.equalsIgnoreCase("SCRIPT") && !type.equalsIgnoreCase("JAVA_CLASS")) {
	        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
			                        "isLovSupported", "Dashboard "+type+" lov Not yet Supported");
	        	response.getOutputStream().write(createErrorMsg(12, "Dashboard  "+type+" lov not yet supported"));
	        	response.getOutputStream().flush();
	        	toReturn = false;
	        }
		} catch (Exception e) {
			toReturn = false;
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
                                "isLovSupported", "Error while checkin if lov " + lovName + " is supported");
		}
        return toReturn;
	}
	
	
	private byte[] createErrorMsg(int code, String message) {
		String response = "<response><error><code>"+code+"</code>" +
				          "<message>"+message+"</message></error></response>";
		return response.getBytes();
	}

}
