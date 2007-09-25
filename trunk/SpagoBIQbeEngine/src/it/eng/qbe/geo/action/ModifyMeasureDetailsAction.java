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

import it.businesslogic.ireport.crosstab.Measure;
import it.eng.qbe.geo.configuration.DatamartProviderConfiguration;
import it.eng.qbe.geo.configuration.MapRendererConfiguration;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Andrea Gioia
 * 
 */
public class ModifyMeasureDetailsAction extends GeoAbstractAction {
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		String measureName = (String)request.getAttribute("measureName");
		
		MapRendererConfiguration mapRendererConfiguration;
		MapRendererConfiguration.Measure measure;
		
		mapRendererConfiguration = mapConfiguration.getMapRendererConfiguration();
		measure = mapRendererConfiguration.getMeasure(measureName);
		if(measure == null) {
			measure = new MapRendererConfiguration.Measure(measureName);			
			mapRendererConfiguration.addMeasure(measure);
		}
		
		getRequestContainer().getSessionContainer().setAttribute("MEASURE", measure);	
	
	}
}
