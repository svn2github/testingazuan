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

import java.util.Iterator;
import java.util.List;
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

	public void setRowData( byte[] rowData ) {
		super.setRowData(rowData);
		parseRowData();
	}
	
	public void refreshRowData( ) {
		StringBuffer buffer = new StringBuffer();
		Iterator it = properties.keySet().iterator();
		while( it.hasNext() ) {
			String pName = (String)it.next();
			String pValue = properties.getProperty(pName);
			buffer.append(pName + "=" + pValue + ";");
		}
		
		super.setRowData(buffer.toString().getBytes());
	}
	
	public String getSelectedHierarchy() {
		return properties.getProperty("selected_hierachy");
	}
	
	public void setSelectedHierarchyName(String hierarchyName) {
		properties.setProperty("selected_hierachy", hierarchyName);
	}

	public String getSelectedHierarchyLevel() {
		return properties.getProperty("selected_hierarchy_level");		
	}
	
	public void setSelectedLevelName(String levelName) {
		properties.setProperty("selected_hierarchy_level", levelName);		
	}

	public String getSelectedMapName() {
		return properties.getProperty("selected_map");
	}
	
	public void setSelectedMapName(String mapName) {
		properties.setProperty("selected_map", mapName);
	}

	public String getSelectedLayers() {
		return properties.getProperty("selected_layers");
	}
	
	public void setSelectedLayers(String layers) {
		properties.setProperty("selected_layers", layers);
	}
	
	public void setSelectedLayers(List layers) {
		String layersStr = null;
		
		if(layers.size() > 0) layersStr = (String)layers.get(0);
		for(int i = 1; i < layers.size(); i++) {
			layersStr += "," + (String)layers.get(i);
		}
		
		if(layersStr != null){
			setSelectedLayers(layersStr);
		}
	}
	
	public String toString() {
    	String str = "";
    	
    	str += "[";
    	str += "selectedHierachy:" + getSelectedHierarchy() + "; ";
    	str += "selectedLevel:" + getSelectedHierarchyLevel() + "; ";
    	str += "selectedMap:" + getSelectedMapName() + "; ";
    	str += "selectedLayers:" + getSelectedLayers();
    	str += "]";
    	
    	return str;
    }
	
}
