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

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Gioia
 * 
 */
public class GetLayersAction extends GeoAbstractAction {
	
	static int counter = 0;
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);

		String mapName = (String)request.getAttribute("mapName");
		
		MapCatalogueAccessUtils mapCatalogueClient = null;
		mapCatalogueClient = (MapCatalogueAccessUtils)getRequestContainer().getSessionContainer().getAttribute("MAP_CATALOGUE_CLIENT");
		List layers = new ArrayList();
		
		
		
		if(mapCatalogueClient == null) {
			counter++;
			layers.add("LayerA" + counter);
			layers.add("LayerB" + counter);
			layers.add("LayerC" + counter);
		} else {
			layers = mapCatalogueClient.getFeatureNamesInMap(mapName);
		}
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<LAYERS>");
		for(int i = 0; i < layers.size(); i++) {
			String layerName = (String)layers.get(i);
			buffer.append("<LAYER>" + layerName + "</LAYER>");
		}
		buffer.append("<LAYER>" + "grafici" + "</LAYER>");
		buffer.append("<LAYER>" + "valori" + "</LAYER>");
		buffer.append("</LAYERS>");
		
		SourceBean result = new SourceBean("RESPONSE");
		result.setAttribute(SourceBean.fromXMLString(buffer.toString()));
		response.setBean(result);
		response.setName(result.getName());
	}
}
