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
package it.eng.spagobi.geo.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import it.eng.spago.base.SourceBean;

/**
 * @author Andrea Gioia
 *
 */
public class MapRendererConfiguration {
	private String className;
	private Map colourRangeMap;
	private Map trasholdRangeMap;
	private Map kpiColourMap;
	private Map kpiDescriptionMap;
	
	private Map outboundColourMap;
	private Map nullColourMap;
	
	private List layerNames;
	private Map layersAttributes;
	
	private String contextPath;

	public MapRendererConfiguration (SourceBean mapRendererConfigurationSB){
		SourceBean measuresConfigurationSB;
		SourceBean layersConfigurationSB;
		
		className = (String)mapRendererConfigurationSB.getAttribute("class_name");
		
		colourRangeMap = new HashMap();
		trasholdRangeMap = new HashMap();
		kpiColourMap = new HashMap();
		kpiDescriptionMap = new HashMap();
		outboundColourMap = new HashMap();
		nullColourMap = new HashMap();
		layersAttributes = new HashMap();
		
		measuresConfigurationSB = (SourceBean)mapRendererConfigurationSB.getAttribute("MEASURES");
		initMeasures(measuresConfigurationSB);
		layersConfigurationSB = (SourceBean)mapRendererConfigurationSB.getAttribute("LAYERS");
		initLayers(layersConfigurationSB);
	}
	
	private void initMeasures(SourceBean measuresConfigurationSB) {
		List kpis = measuresConfigurationSB.getAttributeAsList("KPI");
		for(int i = 0; i < kpis.size(); i++) {
			SourceBean param;
			
			SourceBean kpiSB = (SourceBean)kpis.get(i);
			String kpi_name = (String)kpiSB.getAttribute("column_id");
			
			String kpi_description = (String)kpiSB.getAttribute("description");
			kpiDescriptionMap.put(kpi_name, kpi_description);
			
			String kpi_colour = (String)kpiSB.getAttribute("colour");
			kpiColourMap.put(kpi_name, kpi_colour);
			
			SourceBean tresholdsSB = (SourceBean)kpiSB.getAttribute("TRESHOLDS");
			param =(SourceBean)tresholdsSB.getFilteredSourceBeanAttribute("PARAM", "name", "range");
			String trasholdsStr = (String)param.getAttribute("value");
			String[] trasholds = trasholdsStr.split(",");
			trasholdRangeMap.put(kpi_name, trasholds);
			
			SourceBean coloursSB = (SourceBean)kpiSB.getAttribute("COLOURS");
			param =(SourceBean)coloursSB.getFilteredSourceBeanAttribute("PARAM", "name", "range");
			String coloursStr = (String)param.getAttribute("value");
			String[] colours = coloursStr.split(",");
			colourRangeMap.put(kpi_name, colours);			
		}		
	}
	
	private void initLayers(SourceBean layersConfigurationSB) {
		List layers = layersConfigurationSB.getAttributeAsList("LAYER");
		Properties attributes;
		layerNames = new ArrayList();
		for(int i = 0; i < layers.size(); i++) {
			SourceBean layerSB = (SourceBean)layers.get(i);
			layerNames.add(layerSB.getAttribute("name"));
			attributes = new Properties();
			String value;
			value = (String)layerSB.getAttribute("description");
			attributes.setProperty("description", value);
			value = (String)layerSB.getAttribute("selected");
			attributes.setProperty("selected", value);
			value = (String)layerSB.getAttribute("default_fill_color");
			attributes.setProperty("default_fill_color", value);
			layersAttributes.put(layerNames.get(i), attributes);
		}
	}
	
	
	
	public String getKpiColour(String kpiName) {
		return (String)kpiColourMap.get(kpiName);
	}
	
	public String getKpiDescription(String kpiName) {
		return (String)kpiDescriptionMap.get(kpiName);
	}
	
	public String getLayerDescription(String layerName) {
		Properties attributes = (Properties)layersAttributes.get(layerName);
		String value = attributes.getProperty("description");
		return value;
	}
	
	public boolean isLayerSelected(String layerName) {
		Properties attributes = (Properties)layersAttributes.get(layerName);
		String value = attributes.getProperty("selected");
		return (value.equalsIgnoreCase("true"));
	}
	
	public String getLayerDefaultFillColor(String layerName) {
		Properties attributes = (Properties)layersAttributes.get(layerName);
		String value = attributes.getProperty("default_fill_color");
		return value;
	}
	
	public void resetLayers() {
		layersAttributes.clear();
		layerNames.clear();
	}
	
	public void addLayer(String name, String description, String selected, String default_fill_color) {
		layerNames.add(name);
		Properties attributes = new Properties();
		String value;
		value = description;
		attributes.setProperty("description", value);
		value = selected;
		attributes.setProperty("selected", value);
		value = default_fill_color;
		attributes.setProperty("default_fill_color", value);
		layersAttributes.put(name, attributes);
	}
	
	public String[] getLayerNames() {
		return (String[])layerNames.toArray(new String[0]);
	}
	
	public String[] getTresholdsArray(String kpi_name) {
		return (String[])trasholdRangeMap.get(kpi_name);
	}
	
	public String[] getColoursArray(String kpi_name) {
		return (String[])colourRangeMap.get(kpi_name);
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
}
