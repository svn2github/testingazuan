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
package it.eng.qbe.geo.configuration;

import it.eng.spago.base.SourceBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


// TODO: Auto-generated Javadoc
/**
 * The Class MapRendererConfiguration.
 * 
 * @author Andrea Gioia
 */
public class MapRendererConfiguration {
	
	/** The parent configuration. */
	private MapConfiguration parentConfiguration;
	
	/** The class name. */
	private String className;
	
		
	/** The context path. */
	private String contextPath;
	
	
	/** The measures map. */
	private Map measuresMap;
	
	/** The layers map. */
	private Map layersMap;
	
	/**
	 * The Class Measure.
	 */
	public static class Measure {
		
		/** The column id. */
		String columnId;
		
		/** The description. */
		String description;
		
		/** The agg func. */
		String aggFunc;
		
		/** The colour. */
		String colour;
		
		/** The treshold lb. */
		String tresholdLb;
		
		/** The treshold ub. */
		String tresholdUb;
		
		/** The treshold calculator type. */
		String tresholdCalculatorType;
		
		/** The treshold calculator parameters. */
		Properties tresholdCalculatorParameters;
		
		/** The colur outbound col. */
		String colurOutboundCol;
		
		/** The colur null col. */
		String colurNullCol;
		
		/** The colur calculator type. */
		String colurCalculatorType;
		
		/** The colur calculator parameters. */
		Properties colurCalculatorParameters;
		
		/** The Constant measureBaseColours. */
		private static final String[] measureBaseColours = new String[]{
			"#FF0000", "#FFFF00", "#FF00FF", "#00FFFF", 
			"#FF6600", "#FF0066", "#00FF66", "#0066FF", "#6600FF", "#66FF00",
			"#9900CC"
		};
		
		/** The counter. */
		private static int counter = 0;
		
		/**
		 * Instantiates a new measure.
		 */
		public Measure() {
			this("");
		}
		
		/**
		 * Instantiates a new measure.
		 * 
		 * @param measureName the measure name
		 */
		public Measure(String measureName) {
			super();
			this.setColumnId(measureName);
			this.setDescription(measureName);
			this.setColour("#CCCC66");
			this.setTresholdCalculatorType("quantile");
			this.setTresholdLb("0");
			this.setTresholdUb("none");
			this.setAggFunc("sum");
			this.setTresholdCalculatorParameters(new Properties());
			this.getTresholdCalculatorParameters().setProperty("range", "");
			this.getTresholdCalculatorParameters().setProperty("GROUPS_NUMBER", "5");
			this.setColurCalculatorType("grad");
			this.setColurOutboundCol("#CCCCCC");
			this.setColurNullCol("#FFFFFF");
			this.setColurCalculatorParameters(new Properties());
			this.getColurCalculatorParameters().setProperty("range", "");
			this.getColurCalculatorParameters().setProperty("BASE_COLOR", measureBaseColours[counter]);
			
			counter++;
			if(counter == measureBaseColours.length) counter = 0;
		}
		
		/**
		 * Gets the colour.
		 * 
		 * @return the colour
		 */
		public String getColour() {
			return colour;
		}
		
		/**
		 * Sets the colour.
		 * 
		 * @param colour the new colour
		 */
		public void setColour(String colour) {
			this.colour = colour;
		}
		
		/**
		 * Gets the column id.
		 * 
		 * @return the column id
		 */
		public String getColumnId() {
			return columnId;
		}
		
		/**
		 * Sets the column id.
		 * 
		 * @param columnId the new column id
		 */
		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}
		
		/**
		 * Gets the colur calculator parameters.
		 * 
		 * @return the colur calculator parameters
		 */
		public Properties getColurCalculatorParameters() {
			return colurCalculatorParameters;
		}
		
		/**
		 * Sets the colur calculator parameters.
		 * 
		 * @param colurCalculatorParameters the new colur calculator parameters
		 */
		public void setColurCalculatorParameters(Properties colurCalculatorParameters) {
			this.colurCalculatorParameters = colurCalculatorParameters;
		}
		
		/**
		 * Gets the colur calculator type.
		 * 
		 * @return the colur calculator type
		 */
		public String getColurCalculatorType() {
			return colurCalculatorType;
		}
		
		/**
		 * Sets the colur calculator type.
		 * 
		 * @param colurCalculatorType the new colur calculator type
		 */
		public void setColurCalculatorType(String colurCalculatorType) {
			this.colurCalculatorType = colurCalculatorType;
		}
		
		/**
		 * Gets the colur null col.
		 * 
		 * @return the colur null col
		 */
		public String getColurNullCol() {
			return colurNullCol;
		}
		
		/**
		 * Sets the colur null col.
		 * 
		 * @param colurNullCol the new colur null col
		 */
		public void setColurNullCol(String colurNullCol) {
			this.colurNullCol = colurNullCol;
		}
		
		/**
		 * Gets the colur outbound col.
		 * 
		 * @return the colur outbound col
		 */
		public String getColurOutboundCol() {
			return colurOutboundCol;
		}
		
		/**
		 * Sets the colur outbound col.
		 * 
		 * @param colurOutboundCol the new colur outbound col
		 */
		public void setColurOutboundCol(String colurOutboundCol) {
			this.colurOutboundCol = colurOutboundCol;
		}
		
		/**
		 * Gets the description.
		 * 
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		
		/**
		 * Sets the description.
		 * 
		 * @param description the new description
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		
		/**
		 * Gets the treshold calculator parameters.
		 * 
		 * @return the treshold calculator parameters
		 */
		public Properties getTresholdCalculatorParameters() {
			return tresholdCalculatorParameters;
		}
		
		/**
		 * Sets the treshold calculator parameters.
		 * 
		 * @param tresholdCalculatorParameters the new treshold calculator parameters
		 */
		public void setTresholdCalculatorParameters(
				Properties tresholdCalculatorParameters) {
			this.tresholdCalculatorParameters = tresholdCalculatorParameters;
		}
		
		/**
		 * Gets the treshold calculator type.
		 * 
		 * @return the treshold calculator type
		 */
		public String getTresholdCalculatorType() {
			return tresholdCalculatorType;
		}
		
		/**
		 * Sets the treshold calculator type.
		 * 
		 * @param tresholdCalculatorType the new treshold calculator type
		 */
		public void setTresholdCalculatorType(String tresholdCalculatorType) {
			this.tresholdCalculatorType = tresholdCalculatorType;
		}
		
		/**
		 * Gets the treshold lb.
		 * 
		 * @return the treshold lb
		 */
		public String getTresholdLb() {
			return tresholdLb;
		}
		
		/**
		 * Sets the treshold lb.
		 * 
		 * @param tresholdLb the new treshold lb
		 */
		public void setTresholdLb(String tresholdLb) {
			this.tresholdLb = tresholdLb;
		}
		
		/**
		 * Gets the treshold ub.
		 * 
		 * @return the treshold ub
		 */
		public String getTresholdUb() {
			return tresholdUb;
		}
		
		/**
		 * Sets the treshold ub.
		 * 
		 * @param tresholdUb the new treshold ub
		 */
		public void setTresholdUb(String tresholdUb) {
			this.tresholdUb = tresholdUb;
		}
		
		/**
		 * Gets the agg func.
		 * 
		 * @return the agg func
		 */
		public String getAggFunc() {
			return aggFunc;
		}
		
		/**
		 * Sets the agg func.
		 * 
		 * @param aggFunc the new agg func
		 */
		public void setAggFunc(String aggFunc) {
			this.aggFunc = aggFunc;
		} 
	}
	
	/**
	 * The Class Layer.
	 */
	public static class Layer {		
		
		/** The name. */
		String name;
		
		/** The description. */
		String description;
		
		/** The selected. */
		boolean selected;
		
		/** The default fill color. */
		String defaultFillColor;
		
		/**
		 * Gets the default fill color.
		 * 
		 * @return the default fill color
		 */
		public String getDefaultFillColor() {
			return defaultFillColor;
		}
		
		/**
		 * Sets the default fill color.
		 * 
		 * @param defaultFillColor the new default fill color
		 */
		public void setDefaultFillColor(String defaultFillColor) {
			this.defaultFillColor = defaultFillColor;
		}
		
		/**
		 * Gets the description.
		 * 
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		
		/**
		 * Sets the description.
		 * 
		 * @param description the new description
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the name.
		 * 
		 * @param name the new name
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * Checks if is selected.
		 * 
		 * @return true, if is selected
		 */
		public boolean isSelected() {
			return selected;
		}
		
		/**
		 * Sets the selected.
		 * 
		 * @param selected the new selected
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * Instantiates a new map renderer configuration.
	 * 
	 * @param parentConfiguration the parent configuration
	 */
	public MapRendererConfiguration (MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
		measuresMap = new HashMap();
		layersMap = new HashMap();
		
	}

	/**
	 * Instantiates a new map renderer configuration.
	 * 
	 * @param parentConfiguration the parent configuration
	 * @param mapRendererConfigurationSB the map renderer configuration sb
	 */
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
	
	/**
	 * Adds the measure.
	 * 
	 * @param measure the measure
	 */
	public void addMeasure(Measure measure) {
		if(measuresMap == null) measuresMap = new HashMap();
		measuresMap.put(measure.getColumnId(), measure);
	}
	
	/**
	 * Reset measures.
	 */
	public void resetMeasures() {		
		measuresMap = new HashMap();
	}
	
	/**
	 * Inits the measures.
	 * 
	 * @param measuresConfigurationSB the measures configuration sb
	 */
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
	
	/**
	 * Inits the layers.
	 * 
	 * @param layersConfigurationSB the layers configuration sb
	 */
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
	
	/**
	 * Gets the measure.
	 * 
	 * @param measureName the measure name
	 * 
	 * @return the measure
	 */
	public Measure getMeasure(String measureName) {
		return (Measure)measuresMap.get(measureName);
	}
	
	/**
	 * Gets the layer.
	 * 
	 * @param layerName the layer name
	 * 
	 * @return the layer
	 */
	public Layer getLayer(String layerName) {
		return (Layer)layersMap.get(layerName);
	}
	
	/**
	 * Gets the kpi colour.
	 * 
	 * @param measureName the measure name
	 * 
	 * @return the kpi colour
	 */
	public String getKpiColour(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) return measure.getColour();

		return null;
	}
	
	/**
	 * Gets the kpi description.
	 * 
	 * @param measureName the measure name
	 * 
	 * @return the kpi description
	 */
	public String getKpiDescription(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) return measure.getDescription();

		return null;
	}
	
	/**
	 * Gets the layer description.
	 * 
	 * @param layerName the layer name
	 * 
	 * @return the layer description
	 */
	public String getLayerDescription(String layerName) {
		Layer layer = getLayer(layerName);
		if(layer != null) return layer.getDescription();

		return null;
	}
	
	/**
	 * Checks if is layer selected.
	 * 
	 * @param layerName the layer name
	 * 
	 * @return true, if is layer selected
	 */
	public boolean isLayerSelected(String layerName) {
		Layer layer = getLayer(layerName);
		if(layer != null) return layer.isSelected();

		return false;
	}
	
	/**
	 * Gets the layer default fill color.
	 * 
	 * @param layerName the layer name
	 * 
	 * @return the layer default fill color
	 */
	public String getLayerDefaultFillColor(String layerName) {
		Layer layer = getLayer(layerName);
		if(layer != null) return layer.getDefaultFillColor();

		return null;
	}
	
	/**
	 * Reset layers.
	 */
	public void resetLayers() {
		layersMap = new HashMap();
	}
	
	/**
	 * Adds the layer.
	 * 
	 * @param layer the layer
	 */
	public void addLayer(Layer layer) {
		if(layersMap ==  null) layersMap = new HashMap();
		layersMap.put(layer.getName(), layer);
	}
	
	/**
	 * Gets the layer names.
	 * 
	 * @return the layer names
	 */
	public String[] getLayerNames() {
		if(layersMap == null) return null;
		return (String[])layersMap.keySet().toArray(new String[0]);
	}
	
	/**
	 * Gets the tresholds array.
	 * 
	 * @param measureName the measure name
	 * 
	 * @return the tresholds array
	 */
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
	
	/**
	 * Gets the colours array.
	 * 
	 * @param measureName the measure name
	 * 
	 * @return the colours array
	 */
	public String[] getColoursArray(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) {
			Properties params = (Properties)measure.getColurCalculatorParameters();
			if(params == null) return null;
			String pValue = params.getProperty("range");
			String[] colours = pValue.split(",");
			return colours;
		}

		return null;
	}
	
	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the class name.
	 * 
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Gets the context path.
	 * 
	 * @return the context path
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * Sets the context path.
	 * 
	 * @param contextPath the new context path
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * Gets the parent configuration.
	 * 
	 * @return the parent configuration
	 */
	public MapConfiguration getParentConfiguration() {
		return parentConfiguration;
	}

	/**
	 * Sets the parent configuration.
	 * 
	 * @param parentConfiguration the new parent configuration
	 */
	public void setParentConfiguration(MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}

	/**
	 * To xml.
	 * 
	 * @return the string
	 */
	public String toXml() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<MAP_RENDERER ");
		buffer.append("\nclass_name=\"" + getClassName()+ "\" ");
		buffer.append(">\n");
		
		Iterator it = measuresMap.keySet().iterator();
		if(it.hasNext()) {
			String measureName = (String)it.next();
			Measure measure = getMeasure(measureName);
			buffer.append("\n<MEASURES default_kpi=\"" + measure.getColumnId() + "\">\n");
			
			it = measuresMap.keySet().iterator();
			while(it.hasNext()) {
				measureName = (String)it.next();
				measure = getMeasure(measureName);
												
				
				buffer.append("\t<KPI ");
				buffer.append("column_id=\"" + measure.getColumnId() + "\" ");
				buffer.append("description=\"" + measure.getDescription() + "\" ");
				buffer.append("agg_func=\"" + measure.getAggFunc() + "\" ");
				buffer.append("colour=\"" + measure.getColour() + "\" ");
				buffer.append(">\n");
				
				buffer.append("\t\t<TRESHOLDS ");
				buffer.append("type=\"" + measure.getTresholdCalculatorType() + "\" ");
				buffer.append("lb_value=\"" + measure.getTresholdLb() + "\" ");
				buffer.append("ub_value=\"" + measure.getTresholdUb() + "\" ");
				buffer.append(">\n");				
				
				Iterator paramsIt;
				
				paramsIt = measure.getTresholdCalculatorParameters().keySet().iterator();
				while(paramsIt.hasNext()) {
					String pName = (String)paramsIt.next();
					String pValue = measure.getTresholdCalculatorParameters().getProperty(pName);
					buffer.append("\t\t\t<PARAM ");
					buffer.append("name=\"" + pName + "\" ");
					buffer.append("value=\"" + pValue + "\" ");
					buffer.append("/>\n");					
				}
				
				
				buffer.append("\t\t</TRESHOLDS>\n ");
				
				buffer.append("\t\t<COLOURS ");
				buffer.append("type=\"" + measure.getColurCalculatorType() + "\" ");
				buffer.append("outbound_colour=\"" + measure.getColurOutboundCol() + "\" ");
				buffer.append("null_values_color=\"" + measure.getColurNullCol() + "\" ");
				buffer.append(">\n");				
				
				
				paramsIt = measure.getColurCalculatorParameters().keySet().iterator();
				while(paramsIt.hasNext()) {
					String pName = (String)paramsIt.next();
					String pValue = measure.getColurCalculatorParameters().getProperty(pName);
					buffer.append("\t\t\t<PARAM ");
					buffer.append("name=\"" + pName + "\" ");
					buffer.append("value=\"" + pValue + "\" ");
					buffer.append("/>\n");					
				}
				
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