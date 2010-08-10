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

package it.eng.spagobi.engines.reports.jasperreport;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dispatching.module.AbstractHttpModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JasperReportInternalEngine extends AbstractHttpModule implements InternalEngineIFace {

	public static final String messageBundle = "messages";
	
	/**
	 * Executes the document and populates the response 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError {
		
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	            			"execute", "Start execute method.");
		if (obj == null) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		            			"execute", "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
		try{
			if(ChannelUtilities.isWebRunning()) {			
				SessionContainer sessionCont = null;
				SourceBean serviceReq = requestContainer.getServiceRequest();
				String executionIdentifier =  (String)serviceReq.getAttribute("EXECUTION_IDENTIFIER");
				SessionContainer mainSessCont = requestContainer.getSessionContainer();
				if(executionIdentifier!=null) {
					sessionCont = (SessionContainer)mainSessCont.getAttribute(executionIdentifier);
				} else {
					sessionCont = mainSessCont;
				}
				sessionCont.setAttribute("JR_IE_OBJ_TO_EXEC", obj);
			}
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JasperReportIE");
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					    		"execute", "Cannot exec the Office document", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
	}

	
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		HttpServletRequest httpReq = getHttpRequest();
		HttpSession httpSession = httpReq.getSession();
		OutputStream out = httpResp.getOutputStream();
		
		String task = (String)request.getAttribute("TASK");
		if(task.equalsIgnoreCase("EXEC_REPORT")) {
			
			RequestContainer requestContainer = RequestContainer.getRequestContainer();
			SessionContainer sessCont = null;
			SourceBean serviceReq = requestContainer.getServiceRequest();
			String executionIdentifier =  (String)serviceReq.getAttribute("EXECUTION_IDENTIFIER");
			SessionContainer mainSessCont = requestContainer.getSessionContainer();
			if(executionIdentifier!=null) {
				sessCont = (SessionContainer)mainSessCont.getAttribute(executionIdentifier);
			} else {
				sessCont = mainSessCont;
			}
			
			BIObject biobj = (BIObject)sessCont.getAttribute("JR_IE_OBJ_TO_EXEC");
			//sessCont.delAttribute("JR_IE_OBJ_TO_EXEC");
			Map parameters = new HashMap();
			List biobjPars = biobj.getBiObjectParameters();
			Iterator biobjParIter = biobjPars.iterator();
			while(biobjParIter.hasNext()) {
				BIObjectParameter biobjPar = (BIObjectParameter)biobjParIter.next();
				String biparurl = biobjPar.getParameterUrlName();
				String biparstringvalue = "";
				List biparvals = biobjPar.getParameterValues();
				Iterator iterBiparVals = biparvals.iterator();
				while(iterBiparVals.hasNext()){
					String biparVal = (String)iterBiparVals.next();
					biparstringvalue = biparstringvalue + biparVal + ",";
				}
			    if(biparstringvalue.length()!=0) {
			    	biparstringvalue = biparstringvalue.substring(0, biparstringvalue.length()-1);
			    }
			    parameters.put(biparurl, biparstringvalue);
			}
			parameters.put("biobjectPath", biobj.getPath());
			
			
			String downPdf =  (String)serviceReq.getAttribute("DOWNLOAD_PDF");
			if(downPdf!=null){
				String reportFileName = biobj.getName() + ".pdf";
				httpResp.setHeader("Content-Disposition","attachment; filename=\"" + reportFileName + "\";");
				parameters.put("param_output_format", "PDF");
			}
			
			
			DataConnection dataconn = getConnection(parameters);
			Connection conn = dataconn.getInternalConnection();
			JasperReportRunner jrr = new JasperReportRunner();
			synchronized(this) {
				jrr.runReport(conn, parameters, out, httpReq, httpResp);
			}
			dataconn.close();
			
		} else if(task.equalsIgnoreCase("GET_REPORT_IMAGE")) {

			String mapName = httpReq.getParameter("mapname");
			Map imagesMap = (Map)httpSession.getAttribute(mapName);
			if(imagesMap != null){
				String imageName = httpReq.getParameter("image");
				if (imageName != null) {
					byte[] imageData = (byte[])imagesMap.get(imageName);
					imagesMap.remove(imageName);
					if(imagesMap.isEmpty()){
						httpSession.removeAttribute(mapName);
					}
					httpResp.setContentLength(imageData.length);
					out.write(imageData, 0, imageData.length);
				}
			}
			
		}
		
		out.flush();
		out.close();
	}
	
	
	private DataConnection getConnection(Map pars) {
		DataConnection dataConnection = null;
		try {
			DataConnectionManager dataConnectionManager = DataConnectionManager.getInstance();
			String connectionName = (String)pars.get("connectionName");
			if(connectionName==null) {
				dataConnection = dataConnectionManager.getConnection();
			} else {
				dataConnection = dataConnectionManager.getConnection(connectionName);
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "getConnection", "Error while recovering connection", e);
		}
		return dataConnection;
	}
	
	
	/**
	 * The <code>JasperReportInternalEngine</code> cannot manage subobjects so this method must not be invoked
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer,
			BIObject obj, SourceBean response, Object subObjectInfo)
			throws EMFUserError {
		// it cannot be invoked
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	            			"executeSubObject",  "Cannot exec subobjects.");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "101", messageBundle);
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	            "handleNewDocumentTemplateCreation", "Not implemented: Cannot build document template for jasper report.");
		throw new InvalidOperationRequest();
		
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleDocumentTemplateEdit(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	            			"handleDocumentTemplateEdit", "Not implemented: Cannot build document template for jasper report.");
		throw new InvalidOperationRequest();
	}

	
}
