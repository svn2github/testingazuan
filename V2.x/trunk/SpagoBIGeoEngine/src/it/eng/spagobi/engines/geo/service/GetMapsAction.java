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
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.configuration.MapConfiguration;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;
import it.eng.spagobi.utilities.engines.EngineException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 * 
 */
public class GetMapsAction extends AbstractGeoEngineAction {
	
	static int counter = 0;
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(GetMapsAction.class);
	
    
	public void service(SourceBean request, SourceBean response) throws EngineException  {
		logger.debug("Start processing a new request...");

		String featureName = (String)request.getAttribute("featureName");
		
		super.service(request, response);
		
		MapCatalogueAccessUtils mapCatalogueClient = null;
		mapCatalogueClient = MapConfiguration.getMapCatalogueAccessUtils();;
		List maps = new ArrayList();
		
		if(mapCatalogueClient == null) {
			counter++;
			maps.add("MapA" + counter);
			maps.add("MapB" + counter);
			maps.add("MapC" + counter);
		} else {
			maps = getMapNamesByFeature(featureName);
		}
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<MAPS>");
		for(int i = 0; i < maps.size(); i++) {
			String mapName = (String)maps.get(i);
			buffer.append("<MAP>" + mapName + "</MAP>");
		}
		buffer.append("</MAPS>");
		
		SourceBean result;
		try {
			result = new SourceBean("RESPONSE");
			result.setAttribute(SourceBean.fromXMLString(buffer.toString()));
		} catch (SourceBeanException e) {
			throw new GeoEngineException("Impossible to generate service responce bean in " + GetMapsAction.class, e);
		}
		
		response.setBean(result);
		response.setName(result.getName());
	}
}
