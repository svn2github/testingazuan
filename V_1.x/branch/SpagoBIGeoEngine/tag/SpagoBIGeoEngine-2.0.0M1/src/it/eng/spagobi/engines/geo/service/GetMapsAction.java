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
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;

import java.util.List;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class GetMapsAction.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GetMapsAction extends AbstractGeoEngineAction {
	
	/** Request parameters. */
	public static final String FEATURE_NAME = "featureName";
	
	
	/** Default serial version number (just to keep eclipse happy). */
	private static final long serialVersionUID = 1L;
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(GetMapsAction.class);
	
	public void service(SourceBean request, SourceBean response) {
		
		String featureName = null;
		List maps = null;
		StringBuffer buffer = null;
		SourceBean result = null;
		
		logger.debug("IN");
		
		try {
			super.service(request, response);
			
			featureName = getAttributeAsString(FEATURE_NAME);
					
			try {
				maps = getGeoEngineInstance().getMapProvider().getMapNamesByFeature(featureName);
				if(maps == null) throw new Exception("The map list returned by the map provider is NULL");
			} catch (Exception e) {
				logger.error("Impossible to get a list of map containing layer [" + featureName + "] ");
				throw new SpagoBIEngineException("Impossible to get a list of map containing layer [" + featureName + "] ", e);
			}
			
			if(maps.size() == 0) {
				logger.warn("The list of map containing layer [" + featureName + "] is empty");
			}
			
			buffer = new StringBuffer();
			buffer.append("<MAPS>");
			for(int i = 0; i < maps.size(); i++) {
				String mapName = (String)maps.get(i);
				buffer.append("<MAP>" + mapName + "</MAP>");
			}
			buffer.append("</MAPS>");
			
			logger.debug( "Map list: " + buffer.toString() );
			
			try {
				result = new SourceBean("RESPONSE");
				result.setAttribute(SourceBean.fromXMLString(buffer.toString()));
			} catch (SourceBeanException e) {
				throw new GeoEngineException("Impossible to generate service responce bean in " + GetMapsAction.class, e);
			}
			
			response.setBean(result);
			response.setName(result.getName());
			
			logger.debug( "Generated service response: " + response );
		} catch (Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			// no resources need to be released
		}	
		
		logger.debug("OUT");
	}
}
