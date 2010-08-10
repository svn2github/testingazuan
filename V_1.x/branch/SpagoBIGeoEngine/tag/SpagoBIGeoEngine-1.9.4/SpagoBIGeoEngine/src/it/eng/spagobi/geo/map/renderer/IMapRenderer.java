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

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.configuration.MapRendererConfiguration;
import it.eng.spagobi.geo.datamart.provider.IDatamartProvider;
import it.eng.spagobi.geo.map.provider.IMapProvider;

import java.io.File;

/**
 * @author Andrea Gioia
 *
 */
public interface IMapRenderer {
	/**
	 * Gets the original svg map and the datawarehouse data and then transfor the svg map
	 * based on the template configuration and data recovered. The new map is stored in a 
	 * temporary file 
	 * @return the File object associated to the temporary file of the new svg map 
	 * @throws Exception If some errors occur during the elaboration
	 */
	public File renderMap(IMapProvider mapProvider, IDatamartProvider datamartProvider) throws Exception;
	
	public MapRendererConfiguration getMapRendererConfiguration();

	public void setMapRendererConfiguration(MapRendererConfiguration mapRendererConfiguration);
}
