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
package it.eng.qbe.geo.action;

import it.eng.qbe.geo.configuration.DatamartProviderConfiguration;
import it.eng.spago.base.SourceBean;

import java.util.List;

/**
 * @author Andrea Gioia
 * 
 */
public class DeleteHierarchyAction extends GeoAbstractAction {
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		//HttpServletResponse httpRresponse = getHttpResponse();
		
		DatamartProviderConfiguration datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();
		
		String hierarchyName = (String)request.getAttribute("hierarchyName");
		datamartProviderConfiguration.deleteHieararchy(hierarchyName);
		
		List hierarchies = datamartProviderConfiguration.getHierarchies();
		DatamartProviderConfiguration.Hierarchy hierarchy;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<HIERARCHIES>");
		for(int i = 0; i < hierarchies.size(); i++) {
			hierarchy = (DatamartProviderConfiguration.Hierarchy)hierarchies.get(i);
			buffer.append("<HIERARCHY>" + hierarchy.getName() + "</HIERARCHY>");
		}
		buffer.append("</HIERARCHIES>");
		
		SourceBean result = new SourceBean("RESPONSE");
		result.setAttribute(SourceBean.fromXMLString(buffer.toString()));
		response.setBean(result);
		response.setName(result.getName());
	}
}
