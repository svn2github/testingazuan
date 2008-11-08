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
package it.eng.spagobi.geo.action;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Gioia
 * 
 */
public class GetMapsAction extends AbstractHttpAction {
	
	static int counter = 0;
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		String featureName = (String)request.getAttribute("featureName");
		
		MapCatalogueAccessUtils mapCatalogueClient = null;
		mapCatalogueClient = MapConfiguration.getMapCatalogueAccessUtils();;
		List maps = new ArrayList();
		
		if(mapCatalogueClient == null) {
			counter++;
			maps.add("MapA" + counter);
			maps.add("MapB" + counter);
			maps.add("MapC" + counter);
		} else {
			maps = mapCatalogueClient.getMapNamesByFeature(featureName);
		}
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<MAPS>");
		for(int i = 0; i < maps.size(); i++) {
			String mapName = (String)maps.get(i);
			buffer.append("<MAP>" + mapName + "</MAP>");
		}
		buffer.append("</MAPS>");
		
		SourceBean result = new SourceBean("RESPONSE");
		result.setAttribute(SourceBean.fromXMLString(buffer.toString()));
		response.setBean(result);
		response.setName(result.getName());
	}
}
