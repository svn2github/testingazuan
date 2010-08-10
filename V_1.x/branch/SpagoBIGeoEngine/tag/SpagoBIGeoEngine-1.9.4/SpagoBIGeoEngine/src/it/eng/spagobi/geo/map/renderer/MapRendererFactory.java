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
package it.eng.spagobi.geo.map.renderer;

import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.configuration.MapRendererConfiguration;
import it.eng.spagobi.geo.map.provider.IMapProvider;

/**
 * @author Andrea Gioia
 *
 */
public class MapRendererFactory {
	/**
	 * Builds and returns class instances of the  IMapRenderer interface 
	 * @param className The class name 
	 * @return an instance of the class which must implement the IMapRenderer interface
	 * 
	 * @throws Exception if the class doesn't exist or it doens't implement the interface
	 */
	public static IMapRenderer getMapRenderer(MapRendererConfiguration mapRendererConfiguration) throws Exception {
		IMapRenderer mapRenderer = null;
		mapRenderer =   (IMapRenderer) Class.forName(mapRendererConfiguration.getClassName()).newInstance();
		mapRenderer.setMapRendererConfiguration(mapRendererConfiguration);		
		return mapRenderer;
	 }
}
