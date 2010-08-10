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
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Gioia
 * 
 */
public class ModifyHierarchyAction extends GeoAbstractAction {
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		DatamartProviderConfiguration datamartProviderConfiguration;
		String hierarchyName;
		DatamartProviderConfiguration.Hierarchy hierarchy;
		
		datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();		
		hierarchyName = (String)request.getAttribute("hierarchyName");		
		hierarchy = datamartProviderConfiguration.getHierarchy(hierarchyName);	
		if(hierarchy == null) {
			hierarchy = new DatamartProviderConfiguration.Hierarchy("?");
		}		
		
		getRequestContainer().getSessionContainer().setAttribute("HIERARCHY", hierarchy);	
		
		MapCatalogueAccessUtils mapCatalogueClient = null;
		mapCatalogueClient = (MapCatalogueAccessUtils)getRequestContainer().getSessionContainer().getAttribute("MAP_CATALOGUE_CLIENT");
		List features = new ArrayList();
				
		
		if(mapCatalogueClient != null) {
			features  = mapCatalogueClient.getAllFeatureNames();			
		} else {
			features.add("feature1");
			features.add("feature2");
			features.add("feature3");	
		}
		
		response.setAttribute("FEATURES", features);
	}
}
