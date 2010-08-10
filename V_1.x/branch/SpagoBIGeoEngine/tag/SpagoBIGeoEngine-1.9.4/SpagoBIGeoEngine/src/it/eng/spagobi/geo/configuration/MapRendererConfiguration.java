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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import it.eng.spago.base.SourceBean;

/**
 * @author Andrea Gioia
 *
 */
public class MapRendererConfiguration {
	private MapConfiguration parentConfiguration;
	
	private String className;
	
		
	private String contextPath;
	
	
	private Map measuresMap;
	private Map layersMap;
	
	public static class Measure {
		String columnId;
		String description;
		String aggFunc;
		String colour;
		String tresholdLb;
		String tresholdUb;
		String tresholdCalculatorType;
		Properties tresholdCalculatorParameters;
		
		String colurOutboundCol;
		String colurNullCol;
		String colurCalculatorType;
		Properties colurCalculatorParameters;
		
		public String getColour() {
			return colour;
		}
		public void setColour(String colour) {
			this.colour = colour;
		}
		public String getColumnId() {
			return columnId;
		}
		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}
		public Properties getColurCalculatorParameters() {
			return colurCalculatorParameters;
		}
		public void setColurCalculatorParameters(Properties colurCalculatorParameters) {
			this.colurCalculatorParameters = colurCalculatorParameters;
		}
		public String getColurCalculatorType() {
			return colurCalculatorType;
		}
		public void setColurCalculatorType(String colurCalculatorType) {
			this.colurCalculatorType = colurCalculatorType;
		}
		public String getColurNullCol() {
			return colurNullCol;
		}
		public void setColurNullCol(String colurNullCol) {
			this.colurNullCol = colurNullCol;
		}
		public String getColurOutboundCol() {
			return colurOutboundCol;
		}
		public void setColurOutboundCol(String colurOutboundCol) {
			this.colurOutboundCol = colurOutboundCol;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Properties getTresholdCalculatorParameters() {
			return tresholdCalculatorParameters;
		}
		public void setTresholdCalculatorParameters(
				Properties tresholdCalculatorParameters) {
			this.tresholdCalculatorParameters = tresholdCalculatorParameters;
		}
		public String getTresholdCalculatorType() {
			return tresholdCalculatorType;
		}
		public void setTresholdCalculatorType(String tresholdCalculatorType) {
			this.tresholdCalculatorType = tresholdCalculatorType;
		}
		public String getTresholdLb() {
			return tresholdLb;
		}
		public void setTresholdLb(String tresholdLb) {
			this.tresholdLb = tresholdLb;
		}
		public String getTresholdUb() {
			return tresholdUb;
		}
		public void setTresholdUb(String tresholdUb) {
			this.tresholdUb = tresholdUb;
		}
		public String getAggFunc() {
			return aggFunc;
		}
		public void setAggFunc(String aggFunc) {
			this.aggFunc = aggFunc;
		} 
	}
	
	public static class Layer {		
		String name;
		String description;
		boolean selected;
		String defaultFillColor;
		
		public String getDefaultFillColor() {
			return defaultFillColor;
		}
		public void setDefaultFillColor(String defaultFillColor) {
			this.defaultFillColor = defaultFillColor;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
	
	
	
	
	
	
	
	
	public MapRendererConfiguration (MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
		measuresMap = new HashMap();
		layersMap = new HashMap();
		
	}

	public MapRendererConfiguration (MapConfiguration parentConfiguration, SourceBean mapRendererConfigurationSB){
		
		setParentConfiguration(parentConfiguration);
		
		SourceBean measuresConfigurationSB;
		SourceBean layersConfigurationSB;
		
		className = (String)mapRendererConfigurationSB.getAttribute("class_name");
		
		measuresMap = new HashMap();
		layersMap = new HashMap();
		
		measuresConfigurationSB = (SourceBean)mapRendererConfigurationSB.getAttribute("MEASURES");
		initMeasures(measuresConfigurationSB);
		layersConfigurationSB = (SourceBean)mapRendererConfigurationSB.getAttribute("LAYERS");
		initLayers(layersConfigurationSB);
	}
	
	public void addMeasure(Measure measure) {
		if(measuresMap == null) measuresMap = new HashMap();
		measuresMap.put(measure.getColumnId(), measure);
	}
	
	public void resetMeasures() {		
		measuresMap = new HashMap();
	}
	
	private void initMeasures(SourceBean measuresConfigurationSB) {
		List measureList;
		SourceBean measureSB;
		SourceBean tresholdsSB;
		SourceBean coloursSB;
		List paramList;
		SourceBean paramSB;
		Measure measure;
		String attributeValue;
		
		measureList = measuresConfigurationSB.getAttributeAsList("KPI");
		for(int i = 0; i < measureList.size(); i++) {
			
			measureSB = (SourceBean)measureList.get(i);
			measure = new Measure();			
			
			attributeValue = (String)measureSB.getAttribute("column_id");
			measure.setColumnId(attributeValue);			
			attributeValue = (String)measureSB.getAttribute("description");
			measure.setDescription(attributeValue);
			attributeValue = (String)measureSB.getAttribute("agg_func");
			if(attributeValue == null) attributeValue = "sum";
			measure.setAggFunc(attributeValue);
			attributeValue = (String)measureSB.getAttribute("colour");
			measure.setColour(attributeValue);
			
			tresholdsSB = (SourceBean)measureSB.getAttribute("TRESHOLDS");			
			attributeValue = (String)tresholdsSB.getAttribute("lb_value");
			measure.setTresholdLb(attributeValue);			
			attributeValue = (String)tresholdsSB.getAttribute("ub_value");
			measure.setTresholdUb(attributeValue);			
			attributeValue = (String)tresholdsSB.getAttribute("type");
			measure.setTresholdCalculatorType(attributeValue);				
			
			paramList = tresholdsSB.getAttributeAsList("PARAM");
			Properties tresholdCalculatorParameters = new Properties();
			for(int j = 0; j < paramList.size(); j++) {
				paramSB = (SourceBean)paramList.get(j);
				String pName = (String)paramSB.getAttribute("name");
				String pValue = (String)paramSB.getAttribute("value");
				tresholdCalculatorParameters.setProperty(pName, pValue);
			}
			measure.setTresholdCalculatorParameters(tresholdCalculatorParameters);
			
			
			coloursSB = (SourceBean)measureSB.getAttribute("COLOURS");				
			attributeValue = (String)coloursSB.getAttribute("null_values_color");
			measure.setColurNullCol(attributeValue);			
			attributeValue = (String)coloursSB.getAttribute("outbound_colour");
			measure.setColurOutboundCol(attributeValue);			
			attributeValue = (String)coloursSB.getAttribute("type");
			measure.setColurCalculatorType(attributeValue);
			
			paramList = coloursSB.getAttributeAsList("PARAM");
			Properties colurCalculatorParameters = new Properties();
			for(int j = 0; j < paramList.size(); j++) {
				paramSB = (SourceBean)paramList.get(j);
				String pName = (String)paramSB.getAttribute("name");
				String pValue = (String)paramSB.getAttribute("value");
				colurCalculatorParameters.setProperty(pName, pValue);
			}
			measure.setColurCalculatorParameters(colurCalculatorParameters);
			
			addMeasure(measure);
		}		
	}
	
	private void initLayers(SourceBean layersConfigurationSB) {
		List layers;
		Layer layer;
		Properties attributes;
		String attributeValue;
		
		layers = layersConfigurationSB.getAttributeAsList("LAYER");
		
		for(int i = 0; i < layers.size(); i++) {
			SourceBean layerSB = (SourceBean)layers.get(i);
			
			layer = new Layer();
			
			
			attributeValue = (String)layerSB.getAttribute("name");
			layer.setName(attributeValue);
			attributeValue = (String)layerSB.getAttribute("description");
			layer.setDescription(attributeValue);
			attributeValue = (String)layerSB.getAttribute("selected");
			layer.setSelected(attributeValue.equalsIgnoreCase("true"));
			attributeValue = (String)layerSB.getAttribute("default_fill_color");
			layer.setDefaultFillColor(attributeValue);			
			
			this.addLayer(layer);			
		}
	}
	
	public Measure getMeasure(String measureName) {
		return (Measure)measuresMap.get(measureName);
	}
	
	public Layer getLayer(String layerName) {
		return (Layer)layersMap.get(layerName);
	}
	
	public String getKpiColour(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) return measure.getColour();

		return null;
	}
	
	public String getKpiDescription(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) return measure.getDescription();

		return null;
	}
	
	public String getLayerDescription(String layerName) {
		Layer layer = getLayer(layerName);
		if(layer != null) return layer.getDescription();

		return null;
	}
	
	public boolean isLayerSelected(String layerName) {
		Layer layer = getLayer(layerName);
		if(layer != null) return layer.isSelected();

		return false;
	}
	
	public String getLayerDefaultFillColor(String layerName) {
		Layer layer = getLayer(layerName);
		if(layer != null) return layer.getDefaultFillColor();

		return null;
	}
	
	public void resetLayers() {
		layersMap = new HashMap();
	}
	
	public void addLayer(Layer layer) {
		if(layersMap ==  null) layersMap = new HashMap();
		layersMap.put(layer.getName(), layer);
	}
	
	public String[] getLayerNames() {
		if(layersMap == null) return null;
		return (String[])layersMap.keySet().toArray(new String[0]);
	}
	
	public String[] getTresholdsArray(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) {
			Properties params = (Properties)measure.getTresholdCalculatorParameters();
			if(params == null) return null;
			String pValue = params.getProperty("range");
			String[] trasholds = pValue.split(",");
			return trasholds;
		}

		return null;
	}
	
	public String[] getColoursArray(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) {
			Properties params = (Properties)measure.getColurCalculatorParameters();
			if(params == null) return null;
			String pValue = params.getProperty("range");
			if(pValue == null) return new String[0];
			String[] colours = pValue.split(",");
			return colours;
		}

		return null;
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

	public MapConfiguration getParentConfiguration() {
		return parentConfiguration;
	}

	public void setParentConfiguration(MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}

	public String toXml() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<MAP_RENDERER ");
		buffer.append("\nclass_name=\"" + getClassName()+ "\" ");
		buffer.append(">\n");
		
		Iterator it = measuresMap.keySet().iterator();
		if(it.hasNext()) {
			Measure measure = (Measure)it.next();
			buffer.append("\n<MEASURES default_kpi=\"" + measure.getColumnId() + "\">\n");
			
			it = measuresMap.keySet().iterator();
			while(it.hasNext()) {
				String measureName = (String)it.next();
				measure = getMeasure(measureName);
				
				String kpiDescription = measure.getDescription();
				String kpiColour = measure.getColour();
				String[] trasholds = getTresholdsArray(measure.getColumnId());
				String trasholdsStr = "";
				for(int i = 0; i < trasholds.length; i++) trasholdsStr += (i==0?"":",") + trasholds[i];
				
				String outboundColour = measure.getColurOutboundCol();
				String nullColour = measure.getColurNullCol();
				String[] colourRange = getColoursArray(measure.getColumnId());
				String colourRangesStr = "";
				for(int i = 0; i < colourRange.length; i++) colourRangesStr += (i==0?"":",") + colourRange[i];
				
				
				buffer.append("\t<KPI ");
				buffer.append("column_id=\"" + measure.getColumnId() + "\" ");
				buffer.append("description=\"" + kpiDescription + "\" ");
				buffer.append("colour=\"" + kpiColour + "\" ");
				buffer.append(">\n");
				
				buffer.append("\t\t<TRESHOLDS ");
				buffer.append("type=\"" + "static" + "\" ");
				buffer.append("lb_value=\"" + "0" + "\" ");
				buffer.append("ub_value=\"" + "none" + "\" ");
				buffer.append(">\n");				
				
				buffer.append("\t\t\t<PARAM ");
				buffer.append("name=\"" + "range" + "\" ");
				buffer.append("value=\"" + trasholdsStr + "\" ");
				buffer.append("/>\n");
				
				buffer.append("\t\t</TRESHOLDS>\n ");
				
				buffer.append("\t\t<COLOURS ");
				buffer.append("type=\"" + "static" + "\" ");
				buffer.append("outbound_colour=\"" + outboundColour + "\" ");
				buffer.append("null_values_color=\"" + nullColour + "\" ");
				buffer.append(">\n");				
				
				buffer.append("\t\t\t<PARAM ");
				buffer.append("name=\"" + "range" + "\" ");
				buffer.append("value=\"" + colourRangesStr + "\" ");
				buffer.append("/>\n");
				
				buffer.append("\t\t</COLOURS>\n ");
				
				buffer.append("\t</KPI>\n ");
			}
			buffer.append("</MEASURES>\n");
		}
		
		
		buffer.append("\n<LAYERS>\n");
		it = layersMap.keySet().iterator();
		while(it.hasNext()) {
			String layerName = (String)it.next();
			Layer layer = (Layer)getLayer(layerName);
			buffer.append("\t<LAYER ");
			buffer.append("name=\"" + layerName + "\" ");
			buffer.append("description=\"" + layer.getDescription() + "\" ");
			buffer.append("selected=\"" + (layer.isSelected()?"true":"false") + "\" ");
			buffer.append("default_fill_color=\"" + layer.getDefaultFillColor() + "\" ");				
			buffer.append("/>\n");	
		}		
		buffer.append("</LAYERS>\n");
		
		
		
		buffer.append("\n</MAP_RENDERER>");
		
		return buffer.toString();
	}
	
}