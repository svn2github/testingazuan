/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import java.util.List;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class GetLayersAction.
 * 
 * @author Andrea Gioia
 */
public class GetLayersAction extends AbstractGeoEngineAction {
	
	/** Request parameters. */
	public static final String MAP_NAME = "mapName";
	
	
	/** Default serial version number (just to keep eclipse happy). */
	private static final long serialVersionUID = 1L;
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(GetLayersAction.class);
	
    
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException  {
		
		logger.debug("IN");
		
		String mapName = null;
		List layers = null;
		StringBuffer buffer = null;
		SourceBean resultSB = null;
		
		super.service(request, response);
		
		mapName = getAttributeAsString(MAP_NAME);
		
		try {
			layers = getGeoEngineInstance().getMapProvider().getFeatureNamesInMap(mapName);
			if(layers == null) throw new Exception("The layers list returned by the map provider is NULL");
		} catch (Exception e) {
			logger.error("Impossible to get a list of layers contained into map [" + mapName + "] ");
			throw new EngineException("Impossible to get a list of layers contained into map [" + mapName + "] ", e);
		}
		
		if(layers.size() == 0) {
			logger.warn("The list of layers contained into mapr [" + mapName + "] is empty");
		}
		
		
		buffer = new StringBuffer();
		buffer.append("<LAYERS>");
		for(int i = 0; i < layers.size(); i++) {
			String layerName = (String)layers.get(i);
			buffer.append("<LAYER>" + layerName + "</LAYER>");
		}
		buffer.append("<LAYER>" + "grafici" + "</LAYER>");
		buffer.append("<LAYER>" + "valori" + "</LAYER>");
		buffer.append("</LAYERS>");
		
		logger.debug( "Layers list: " + buffer.toString() );
		
		
		try {
			resultSB = new SourceBean("RESPONSE");
			resultSB.setAttribute(SourceBean.fromXMLString(buffer.toString()));
		} catch (SourceBeanException e) {
			throw new GeoEngineException("Impossible to generate service responce bean in " + GetLayersAction.class, e);
		}
		
		response.setBean(resultSB);
		response.setName(resultSB.getName());
		
		logger.debug( "Generated service response: " + response );
		
		logger.debug("OUT");
	}
}
