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
public class GetHierarchyLevelsAction extends GeoAbstractAction {
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		String hierarchyName = (String)request.getAttribute("hierarchyName");
		
		DatamartProviderConfiguration datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();		
		DatamartProviderConfiguration.Hierarchy hierarchy;
		hierarchy = datamartProviderConfiguration.getHierarchy(hierarchyName);
		List levels = hierarchy.getLevels();
		
		DatamartProviderConfiguration.Hierarchy.Level level;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<RESPONSE>");
		buffer.append("<LEVELS>");
		for(int i = 0; i < levels.size(); i++) {
			level = (DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
			buffer.append("<LEVEL>" + level.getName() + "</LEVEL>");
		}
		buffer.append("</LEVELS>");
		
		buffer.append("<FEATURES>");
		for(int i = 0; i < levels.size(); i++) {
			level = (DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
			buffer.append("<FEATURE>" + level.getFeatureName() + "</FEATURE>");
		}
		buffer.append("</FEATURES>");
		buffer.append("</RESPONSE>");
		
		//SourceBean result = new SourceBean("RESPONSE");
		//result.setAttribute(SourceBean.fromXMLString(buffer.toString()));
		SourceBean result = SourceBean.fromXMLString(buffer.toString());
		
		response.setBean(result);
		response.setName(result.getName());
	}
}
