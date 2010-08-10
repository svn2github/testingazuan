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
package it.eng.spagobi.services.actions;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.managers.ExecutionManager;
import it.eng.spagobi.managers.ExecutionManager.ExecutionInstance;
import it.eng.spagobi.utilities.GeneralUtilities;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class UpdateExecutionManager extends AbstractHttpAction {

	public void service(SourceBean request, SourceBean response) throws Exception {
		this.freezeHttpResponse();
		String executionId = (String) request.getAttribute("spagobi_execution_id");
		String windowName = (String) request.getAttribute("windowName");
		String objIdStr = (String) request.getAttribute("BIOBJECT_ID");
		String executionRole = (String) request.getAttribute("spagobi_execution_role");
		Integer objId = new Integer(objIdStr);
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		List biObjParams = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(obj.getId());
		obj.setBiObjectParameters(biObjParams);
    	Iterator parametersIt = biObjParams.iterator();
    	while (parametersIt.hasNext()) {
    		BIObjectParameter aParameter = (BIObjectParameter) parametersIt.next();
    		String urlName = aParameter.getParameterUrlName();
    		List values = request.getAttributeAsList(urlName);
    		aParameter.setParameterValues(values);
    		if (values != null && values.size() > 1) {
    			aParameter.setMultivalue(new Integer(1));
    		} else {
    			aParameter.setMultivalue(new Integer(0));
    		}
    	}
		String flowId = windowName;
		if (flowId == null || flowId.trim().equals("") || !flowId.startsWith("iframeexec")) {
			flowId = executionId;
		} else {
			flowId = flowId.substring(10);
		}
		ExecutionManager executionManager = ExecutionManager.getInstance();
		executionManager.registerExecution(flowId, executionId, obj, executionRole);
		List list = executionManager.getBIObjectsExecutionFlow(flowId);
		String html = "";
		// get spagobi url
		String spagobiurl = GeneralUtilities.getSpagoBiContextAddress();
		if (!spagobiurl.endsWith("/")) spagobiurl += "/";
		spagobiurl += "servlet/AdapterHTTP";
		for (int i = 0; i < list.size(); i++) {
			ExecutionManager.ExecutionInstance instance = (ExecutionInstance) list.get(i);
			BIObject aBIObject = instance.getBIObject();
			if (i == list.size() - 1) {
				html += "&nbsp;" + aBIObject.getName();
			} else {
				html += "<form style='display:none;' name=\"navigationBarForm" + instance.getExecutionId() + "\"";
		        html += "       id=\"navigationBarForm" + instance.getExecutionId() + "\" method=\"post\"";
		        html += "	      action=\"" + spagobiurl + "\"";
		        html += "	      target=\"iframeexec" + flowId + "\">";
				html += "	<input type=\"hidden\" name=\"NEW_SESSION\" value=\"TRUE\" />";
				html += "	<input type=\"hidden\" name=\"PAGE\" value=\"DirectExecutionPage\" />";
			    html += "   <input type=\"hidden\" name=\"OPERATION\" value=\"RECOVER_EXECUTION_FROM_DRILL_FLOW\" />";
			    html += "   <input type=\"hidden\" name=\"spagobi_flow_id\" value=\"" + flowId + "\" />";
			    html += "   <input type=\"hidden\" name=\"spagobi_execution_id\" value=\"" + instance.getExecutionId() + "\" />";
			    html += "   <input type=\"hidden\" name=\""+LightNavigationManager.LIGHT_NAVIGATOR_DISABLED+"\" value=\"TRUE\" />";
				html += "</form>";
				html += "&nbsp;<a href=\"javascript:document.getElementById(\'navigationBarForm" + instance.getExecutionId() + "\').submit();\">" + aBIObject.getName() + "</a>";
			}
			if (i < list.size() - 1) {
				html += "&nbsp;&gt;";
			}
		}
		//get the http response 
		HttpServletResponse httpResponse = this.getHttpResponse();
		httpResponse.getOutputStream().write(html.getBytes());
		httpResponse.getOutputStream().flush();	
	}

}
