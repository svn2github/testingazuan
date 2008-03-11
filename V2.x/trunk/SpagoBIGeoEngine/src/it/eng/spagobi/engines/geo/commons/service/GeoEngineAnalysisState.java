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
package it.eng.spagobi.engines.geo.commons.service;

import java.util.Properties;

import it.eng.spagobi.utilities.engines.EngineAnalysisState;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class GeoEngineAnalysisState extends EngineAnalysisState {
	private Properties properties;
	
	public GeoEngineAnalysisState( byte[] rowData ) {
		super( rowData );
		parseRowData();
	}
	
	private void  parseRowData() {
		properties = new Properties();
		
		String str = null;
		String[] chuncks = null;
		
		if(getRowData() == null) return;
		
		str = new String( getRowData() );
		chuncks = str.split(";");
		for(int i = 0; i < chuncks.length; i++) {
			String[] propChunk = chuncks[i].split("=");
			String pName = propChunk[0];
			String pValue = propChunk[1];
			properties.setProperty(pName, pValue);
		}
	}

	public String getSelectedHierarchy() {
		return properties.getProperty("selected_hierachy");
	}

	public String getSelectedHierarchyLevel() {
		return properties.getProperty("selected_hierarchy_level");		
	}

	public String getSelectedMap() {
		return properties.getProperty("selected_map");
	}

	public String getSelectedLayers() {
		return properties.getProperty("selected_layers");
	}
	
}
