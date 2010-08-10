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
package it.eng.spagobi.engines.geo.map.renderer.configurator;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.map.renderer.AbstractMapRenderer;
import it.eng.spagobi.engines.geo.map.renderer.Layer;
import it.eng.spagobi.engines.geo.map.renderer.Measure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractMapRendererConfigurator.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class AbstractMapRendererConfigurator {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(AbstractMapRendererConfigurator.class);
	
	
	/**
	 * Configure.
	 * 
	 * @param abstractMapRenderer the abstract map renderer
	 * @param conf the conf
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public static void configure(AbstractMapRenderer abstractMapRenderer, Object conf) throws GeoEngineException {
		SourceBean confSB = null;
		
		if(conf instanceof String) {
			try {
				confSB = SourceBean.fromXMLString( (String)conf );
			} catch (SourceBeanException e) {
				logger.error("Impossible to parse configuration block for MapRenderer", e);
				throw new GeoEngineException("Impossible to parse configuration block for MapRenderer", e);
			}
		} else {
			confSB = (SourceBean)conf;
		}
		
		if(confSB != null) {
			SourceBean measuresConfigurationSB = (SourceBean)confSB.getAttribute("MEASURES");
			Map measures = getMeasures(measuresConfigurationSB);
			SourceBean layersConfigurationSB = (SourceBean)confSB.getAttribute("LAYERS");
			Map layers = getLayers(layersConfigurationSB);
			
			abstractMapRenderer.setMeasures( measures );
			abstractMapRenderer.setLayers( layers );
			
		}
	}
	
	/**
	 * Gets the measures.
	 * 
	 * @param measuresConfigurationSB the measures configuration sb
	 * 
	 * @return the measures
	 */
	private static Map getMeasures(SourceBean measuresConfigurationSB) {
		Map measures;
		List measureList;
		SourceBean measureSB;
		SourceBean tresholdsSB;
		SourceBean coloursSB;
		List paramList;
		SourceBean paramSB;
		Measure measure;
		String attributeValue;
		
		
		measures = new HashMap();
		
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
			attributeValue = (String)measureSB.getAttribute("pattern");
			measure.setPattern(attributeValue);
			attributeValue = (String)measureSB.getAttribute("unit");
			measure.setUnit(attributeValue);
			
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
			
			measures.put(measure.getColumnId(), measure);
		}		
		
		return measures;
	}
	
	/**
	 * Gets the layers.
	 * 
	 * @param layersConfigurationSB the layers configuration sb
	 * 
	 * @return the layers
	 */
	private static Map getLayers(SourceBean layersConfigurationSB) {
		Map layers;
		List layerList;
		Layer layer;
		Properties attributes;
		String attributeValue;
		
		layers = new HashMap();
		
		layerList = layersConfigurationSB.getAttributeAsList("LAYER");
		
		for(int i = 0; i < layerList.size(); i++) {
			SourceBean layerSB = (SourceBean)layerList.get(i);
			
			layer = new Layer();
			
			
			attributeValue = (String)layerSB.getAttribute("name");
			layer.setName(attributeValue);
			attributeValue = (String)layerSB.getAttribute("description");
			layer.setDescription(attributeValue);
			attributeValue = (String)layerSB.getAttribute("selected");
			layer.setSelected(attributeValue.equalsIgnoreCase("true"));
			attributeValue = (String)layerSB.getAttribute("default_fill_color");
			layer.setDefaultFillColor(attributeValue);			
			
			layers.put(layer.getName(), layer);			
		}
		
		return layers;
	}
	
}
