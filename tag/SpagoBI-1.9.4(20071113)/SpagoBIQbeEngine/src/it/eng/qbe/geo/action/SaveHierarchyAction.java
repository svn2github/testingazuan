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


/**
 * @author Andrea Gioia
 * 
 */
public class SaveHierarchyAction extends GeoAbstractAction {
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		DatamartProviderConfiguration.Hierarchy hierarchy;
		hierarchy = (DatamartProviderConfiguration.Hierarchy)getRequestContainer().getSessionContainer().getAttribute("HIERARCHY");
		
		String hierarchyName = (String)request.getAttribute("hierarchyName");
		hierarchy.setName(hierarchyName);
		
		String levelNamesStr = (String)request.getAttribute("levelNames");
		String[] levelNames = levelNamesStr.split(";");
		String columnNamesStr = (String)request.getAttribute("columnNames");
		String[] columnNames = columnNamesStr.split(";");
		String featureNamesStr = (String)request.getAttribute("featureNames");
		String[] featureNames = featureNamesStr.split(";");
		
		hierarchy.clearLevels();		
		DatamartProviderConfiguration.Hierarchy.Level level;
		for(int i = 0; i < levelNames.length; i++) {
			level = new DatamartProviderConfiguration.Hierarchy.Level();
			level.setName(levelNames[i]);
			level.setColumnId(columnNames[i]);
			level.setFeatureName(featureNames[i]);
			hierarchy.addLevel(level);
		}		
		
		
		mapConfiguration.getDatamartProviderConfiguration().addHieararchy(hierarchy);	
	}
}
