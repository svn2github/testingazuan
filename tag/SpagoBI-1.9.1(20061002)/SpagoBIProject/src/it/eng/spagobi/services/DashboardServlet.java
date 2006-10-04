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

import groovy.lang.Binding;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.QueryDetail;
import it.eng.spagobi.bo.ScriptDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

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
		OutputStream out = null;
		DataConnection dataConnection = null;
	 	try{
	 		out = response.getOutputStream();
		 	String dataName = (String)request.getParameter("dataname");
		 	if((dataName!=null) && (!dataName.trim().equals(""))) {
		 		IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
		 		ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(dataName);
                String type = lov.getITypeCd();
                if(type.equalsIgnoreCase("QUERY")) {
                	String dataProv = lov.getLovProvider();
                	QueryDetail queryDet = QueryDetail.fromXML(dataProv);
                	String pool = queryDet.getConnectionName();
            		String statement = queryDet.getQueryDefinition();
            		DataConnectionManager dataConnectionManager = DataConnectionManager.getInstance();
        			dataConnection = dataConnectionManager.getConnection(pool);
        			RequestContainer reqcont = RequestContainer.getRequestContainer();
        			SessionContainer sessCont = reqcont.getSessionContainer();
        			SessionContainer permSess = sessCont.getPermanentContainer();
        			IEngUserProfile profile = (IEngUserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
        			statement = GeneralUtilities.substituteProfileAttributesInString(statement, profile);
        			SQLCommand sqlCommand = dataConnection.createSelectCommand(statement);
        			DataResult dataResult = sqlCommand.execute();
                    ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult.getDataObject();
        			SourceBean result = scrollableDataResult.getSourceBean();
            		String resStr = result.toXML(false);
            		resStr = resStr.trim();
            		if(resStr.startsWith("<?")) {
            			resStr = resStr.substring(2);
            			int indFirstTag = resStr.indexOf("<");
            			resStr = resStr.substring(indFirstTag);
            		}
            		resStr = resStr.toLowerCase();
            		out.write(resStr.getBytes());
    		 		out.flush();
                } else if(type.equalsIgnoreCase("SCRIPT")) {
                	Binding bind = GeneralUtilities.fillBinding(new HashMap());
                	String dataProv = lov.getLovProvider();
                	ScriptDetail scriptDet = ScriptDetail.fromXML(dataProv);
                	String result = GeneralUtilities.testScript(scriptDet.getScript(), bind);
                	out.write(result.getBytes());
		 		    out.flush();
                } else {
                	SpagoBITracer.major("SpagoBI", this.getClass().getName(),
    						             "Service", "Dashboard "+type+" lov Not yet Supported");
                	out.write(createErrorMsg(12, "Dashboard  "+type+" lov not yet supported"));
    		 		out.flush();
                }		
		 	} else {
		 		out.write(createErrorMsg(10, "Param dataname not found"));
		 		out.flush();
		 	}
	 	}catch(Exception e){
	 		SpagoBITracer.critical("SpagoBI",getClass().getName(),"service","Exception", e);
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
