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
package it.eng.spagobi.engines.geo.map.renderer;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.engines.geo.Constants;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.dataset.DataSet;
import it.eng.spagobi.engines.geo.dataset.provider.IDatasetProvider;
import it.eng.spagobi.engines.geo.map.provider.IMapProvider;
import it.eng.spagobi.engines.geo.map.renderer.configurator.InteractiveMapRendererConfigurator;
import it.eng.spagobi.engines.geo.map.utils.SVGMapHandler;
import it.eng.spagobi.engines.geo.map.utils.SVGMapLoader;
import it.eng.spagobi.engines.geo.map.utils.SVGMapMerger;
import it.eng.spagobi.engines.geo.map.utils.SVGMapSaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;

// TODO: Auto-generated Javadoc
/**
 * The Class InteractiveMapRenderer.
 * 
 * @author Andrea Gioia
 */
public class InteractiveMapRenderer extends AbstractMapRenderer {

	/** The close link. */
	private boolean closeLink = false;
	
	/** The label producers. */
	private Map labelProducers;
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(InteractiveMapRenderer.class);
	
	
	/**
	 * Instantiates a new interactive map renderer.
	 */
	public InteractiveMapRenderer() {
		super();
		labelProducers = new HashMap();
		labelProducers.put("header-left", new DefaultLabelProducer() );
		labelProducers.put("header-center", new DefaultLabelProducer() );
		labelProducers.put("header-right", new DefaultLabelProducer() );
		labelProducers.put("footer-left", new DefaultLabelProducer() );
		labelProducers.put("footer-center", new DefaultLabelProducer() );
		labelProducers.put("footer-right", new DefaultLabelProducer() );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.renderer.AbstractMapRenderer#init(java.lang.Object)
	 */
	public void init(Object conf) throws GeoEngineException {
		super.init(conf);
		InteractiveMapRendererConfigurator.configure( this, getConf() );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.renderer.AbstractMapRenderer#renderMap(it.eng.spagobi.engines.geo.map.provider.IMapProvider, it.eng.spagobi.engines.geo.dataset.provider.IDatasetProvider)
	 */
	public File renderMap(IMapProvider mapProvider, 
			  IDatasetProvider datamartProvider) throws GeoEngineException {
		return renderMap(mapProvider, datamartProvider, Constants.DSVG);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.renderer.AbstractMapRenderer#renderMap(it.eng.spagobi.engines.geo.map.provider.IMapProvider, it.eng.spagobi.engines.geo.dataset.provider.IDatasetProvider, java.lang.String)
	 */
	public File renderMap(IMapProvider mapProvider, 
						  IDatasetProvider datamartProvider,
						  String outputFormat) throws GeoEngineException {
		
		if(outputFormat.equalsIgnoreCase(Constants.SVG)) {
			return renderSVGMap(mapProvider, datamartProvider);
		}else if(outputFormat.equalsIgnoreCase(Constants.DSVG)) {
			return renderDSVGMap(mapProvider, datamartProvider, false);
		} else if(outputFormat.equalsIgnoreCase(Constants.XDSVG)) {
			return renderDSVGMap(mapProvider, datamartProvider, true);
		} else if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
			return renderSVGMap(mapProvider, datamartProvider);
		}
		
		
		return renderDSVGMap(mapProvider, datamartProvider, true);
	}
	
	/**
	 * Render dsvg map.
	 * 
	 * @param mapProvider the map provider
	 * @param datamartProvider the datamart provider
	 * @param includeScript the include script
	 * 
	 * @return the file
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	private File renderDSVGMap(IMapProvider mapProvider, 
	  						   IDatasetProvider datamartProvider, boolean includeScript) throws GeoEngineException {
		SVGDocument targetMap;
		SVGDocument masterMap = null;
		DataSet dataSet;
		
		dataSet = (DataSet)datamartProvider.getDataSet();
		
		try {
			masterMap = SVGMapLoader.loadMapAsDocument(getMasterMapFile(true));
		} catch (IOException e) {
			logger.error("Impossible to load map from file: " + getMasterMapFile(true));
			String description = "Impossible to load map from file: " + getMasterMapFile(true);
			throw new GeoEngineException("Impossible to render map", description, e);
		}		
		targetMap = mapProvider.getSVGMapDOMDocument();				
				
		addData(targetMap, dataSet);
		addLink(targetMap, dataSet);
		
		SVGMapMerger.margeMap(targetMap, masterMap, null, "targetMap");
		
		if( includeScript ) {
			includeScripts(masterMap);
		} else {
			importScripts(masterMap);
		}
		
		
		
		setMainMapDimension(masterMap, targetMap);
		setMainMapBkgRectDimension(masterMap, targetMap);	   	   
	    
	    Element scriptInit = masterMap.getElementById("init");	    
	    Node scriptText = scriptInit.getFirstChild();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(getMeasuresConfigurationScript(dataSet));
	    String targetLayer = datamartProvider.getSelectedLevel().getFeatureName();
	   
	    
	    buffer.append( getLayersConfigurationScript(targetMap, targetLayer) );    
	    buffer.append( getGUIConfigurationScript() );
	    scriptText.setNodeValue(buffer.toString());
		
		File tmpMap;
		try {
			tmpMap = getTempFile();
		} catch (IOException e) {
			logger.error("Impossible to create a temporary file", e);
			String description = "Impossible to create a temporary file";
			throw new GeoEngineException("Impossible to render map", description, e);
		}				
		try {
			SVGMapSaver.saveMap(masterMap, tmpMap);
		} catch (FileNotFoundException e) {
			logger.error("Impossible to save map on temporary file " + tmpMap, e);
			String str = e.getMessage()!=null?e.getMessage():e.getClass().getName();
			String description = "Impossible to save map on temporary file " + tmpMap + ". Root cause: " + str;
			throw new GeoEngineException("Impossible to render map", description, e);
		} catch (TransformerException e) {
			logger.error("Impossible to save map on temporary file " + tmpMap, e);
			String str = e.getMessage()!=null?e.getMessage():e.getClass().getName();
			String description = "Impossible to save map on temporary file " + tmpMap + ". Root cause: " + str;
			throw new GeoEngineException("Impossible to render map", description, e);
		}

		return tmpMap;
	}
	
	/**
	 * Render svg map.
	 * 
	 * @param mapProvider the map provider
	 * @param datamartProvider the datamart provider
	 * 
	 * @return the file
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	private File renderSVGMap(IMapProvider mapProvider, 
			   IDatasetProvider datamartProvider) throws GeoEngineException {
		
		SVGDocument targetMap;
		SVGDocument masterMap;
		
		DataSet datamart;
		
		datamart = (DataSet)datamartProvider.getDataSet();
		
	
		targetMap = mapProvider.getSVGMapDOMDocument();		
		try {
			masterMap = SVGMapLoader.loadMapAsDocument(getMasterMapFile(false));
		} catch (IOException e) {
			logger.error("Impossible to load map from file: " + getMasterMapFile(true));
			String description = "Impossible to load map from file: " + getMasterMapFile(true);
			throw new GeoEngineException("Impossible to render map", description, e);
		}
		
		decorateMap(masterMap, targetMap, datamart);
		
		SVGMapMerger.margeMap(targetMap, masterMap, null, "targetMap");
		
		setMainMapDimension(masterMap, targetMap);
		setMainMapBkgRectDimension(masterMap, targetMap);	 
		
		File tmpMap;
		try {
			tmpMap = getTempFile();
		} catch (IOException e) {
			logger.error("Impossible to create a temporary file", e);
			String description = "Impossible to create a temporary file";
			throw new GeoEngineException("Impossible to render map", description, e);
		}				
		try {
			SVGMapSaver.saveMap(masterMap, tmpMap);
		} catch (FileNotFoundException e) {
			logger.error("Impossible to save map on temporary file " + tmpMap, e);
			String str = e.getMessage()!=null?e.getMessage():e.getClass().getName();
			String description = "Impossible to save map on temporary file " + tmpMap + ". Root cause: " + str;
			throw new GeoEngineException("Impossible to render map", description, e);
		} catch (TransformerException e) {
			logger.error("Impossible to save map on temporary file " + tmpMap, e);
			String str = e.getMessage()!=null?e.getMessage():e.getClass().getName();
			String description = "Impossible to save map on temporary file " + tmpMap + ". Root cause: " + str;
			throw new GeoEngineException("Impossible to render map", description, e);
		}
		
		return tmpMap;
	}
	
	/**
	 * Decorate map.
	 * 
	 * @param masterMap the master map
	 * @param targetMap the target map
	 * @param datamart the datamart
	 */
	private void decorateMap(SVGDocument masterMap, SVGDocument targetMap, DataSet datamart) {
		
		String[] kpiNames = datamart.getKpiNames();
		int selectedKpiIndex = datamart.getSelectedKpi();
		String selectedKpiName = kpiNames[selectedKpiIndex];
		Measure measure  = getMeasure( selectedKpiName );
		Double lb_value = null;
		Double ub_value = null;
		String lb_color = null;
		String ub_color = null;
		String null_values_color = null;
		String[] trasholdCalculationPercParams = null;
		Integer num_group = null;
		Integer trasholdCalculationUniformParams = null;
		String colorRangeCalculationGradParams = null;
		String[] col_kpi_array = null;
		Double[] trash_kpi_array = null;
		Double[] kpi_ordered_values = null;
		
		
		Set orderedKpiValuesSet = datamart.getOrderedKpiValuesSet( selectedKpiName );
		kpi_ordered_values = (Double[])orderedKpiValuesSet.toArray(new Double[0]);
    	
		
				
		if( measure.getTresholdLb() == null 
    			|| measure.getTresholdLb().trim().equalsIgnoreCase("")
    			|| measure.getTresholdLb().equalsIgnoreCase("none") ) {
			lb_value = null;
    	} else {
    		lb_value = Double.parseDouble( measure.getTresholdLb() );
    	}
		
		if( measure.getTresholdUb() == null 
    			|| measure.getTresholdUb().trim().equalsIgnoreCase("")
    			|| measure.getTresholdUb().equalsIgnoreCase("none") ) {
			ub_value = null;
    	} else {
    		ub_value = Double.parseDouble( measure.getTresholdUb() );
    	}
		
		lb_color = measure.getColurOutboundCol();
    	ub_color = measure.getColurOutboundCol();
    	null_values_color = measure.getColurNullCol();
    	
	    
    	String numGroupAttr = measure.getTresholdCalculatorParameters().getProperty("GROUPS_NUMBER");
    	if( numGroupAttr != null ) {
    		num_group = Integer.parseInt( numGroupAttr );
    		trasholdCalculationUniformParams = num_group;
    	}
    	
    	colorRangeCalculationGradParams = measure.getColurCalculatorParameters().getProperty("BASE_COLOR");
    	

    	
    	
    	//////////////////////////////////////////////////////////////////////////
    	// SetTrashHolds
    	///////////////
    	if(lb_value == null) {
   			lb_value = kpi_ordered_values[0];
   		}
   		if(ub_value == null) {
   			ub_value = kpi_ordered_values[kpi_ordered_values.length-1];
   		}
   		
   		if(lb_value.doubleValue() > ub_value.doubleValue()) {
   			Double t = lb_value;
   			ub_value = lb_value;
   			lb_value = t;
   		}
   		
   		if(ub_value < kpi_ordered_values[0] || lb_value >  kpi_ordered_values[kpi_ordered_values.length-1]) {
   			lb_value = kpi_ordered_values[0];
   			ub_value = kpi_ordered_values[kpi_ordered_values.length-1];
   		}
		
		
		
		if( measure.getTresholdCalculatorType().equalsIgnoreCase("quantile") ) {			
			
			trash_kpi_array = new Double[num_group + 1];
			
			int diff_value_num = 0;	
			int start_index = -1;	
			if(kpi_ordered_values[0] >= lb_value && kpi_ordered_values[kpi_ordered_values.length-1] <= ub_value) {
				diff_value_num = kpi_ordered_values.length;
				start_index = 0;
			} else {
				for(int j = 0; j < kpi_ordered_values.length; j++) {						
		   			if(kpi_ordered_values[j] >= lb_value && kpi_ordered_values[j] <= ub_value) {
		   				start_index = (start_index == -1?j:start_index);
		   				diff_value_num++;
		   			}
	   			}
	   		}
	   		
	   		
			if(diff_value_num < num_group) num_group = diff_value_num;
			int blockSize = (int)Math.floor( diff_value_num / num_group );
		
			trash_kpi_array[0] = lb_value;
			for(int j = 1; j < num_group; j++){
				trash_kpi_array[j] = kpi_ordered_values[start_index + (j*blockSize)];
			}
			trash_kpi_array[num_group] = ub_value;
			
		} else if ( measure.getTresholdCalculatorType().equalsIgnoreCase("perc") ) {
			double range = ub_value - lb_value;
			
			trasholdCalculationPercParams = getTresholdsArray(measure.getColumnId());
			trash_kpi_array = new Double[trasholdCalculationPercParams.length + 1];
			
			trash_kpi_array[0] = lb_value;
			for(int j = 0; j < trasholdCalculationPercParams.length; j++) {
				double groupSize = (range / 100.0) * Double.parseDouble(trasholdCalculationPercParams[j]);
				trash_kpi_array[j+1] = trash_kpi_array[j] + groupSize;
			}
			trash_kpi_array[ trash_kpi_array.length - 1] = ub_value;
			num_group = trash_kpi_array.length - 1;
		} else if ( measure.getTresholdCalculatorType().equalsIgnoreCase("uniform") ) {
			trash_kpi_array = new Double[ trasholdCalculationUniformParams.intValue() + 1 ];
			double perc = 100 / (trasholdCalculationUniformParams.doubleValue());
			trasholdCalculationPercParams = new String[trasholdCalculationUniformParams.intValue() + 1];
			for(int j = 0; j < trasholdCalculationPercParams.length; j++) {
				trasholdCalculationPercParams[j] = "" + perc;
			}
			
			double range = ub_value - lb_value;
			trash_kpi_array[0] = lb_value;
			
			for(int j = 0; j < trash_kpi_array.length-2; j++) {
				double groupSize = (range / 100.0) * Double.parseDouble(trasholdCalculationPercParams[j]);
				trash_kpi_array[j+1] = trash_kpi_array[j] + groupSize;
			}
			trash_kpi_array[ trash_kpi_array.length-1 ] = ub_value;
			num_group = trasholdCalculationPercParams.length - 1;			
		} else if ( measure.getTresholdCalculatorType().equalsIgnoreCase("static") ) {
			String[] trasholdsArray = getTresholdsArray( selectedKpiName );
			trash_kpi_array = new Double[trasholdsArray.length];
	    	for(int j = 0; j < trasholdsArray.length; j++) {
	    		trash_kpi_array[j] = new Double( trasholdsArray[j] );
	    	}
	    } else {
			//setQuantileTrasholds(kpi_names[i]);
		}
	    
	 
		if(measure.getColurCalculatorType().equalsIgnoreCase("static"))  {
			col_kpi_array = getColoursArray( selectedKpiName ); 
		} else if(measure.getColurCalculatorType().equalsIgnoreCase("gradient"))  {
			col_kpi_array = getGradientColourRange(colorRangeCalculationGradParams, num_group);	
		} else {
			col_kpi_array = getGradientColourRange(colorRangeCalculationGradParams, num_group);	
		}
		logger.debug( Arrays.toString( col_kpi_array ) );
		
		
		
		Element targetLayer = targetMap.getElementById(datamart.getTargetFeatureName());
		
		NodeList nodeList = targetLayer.getChildNodes();
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node childNode = (Node)nodeList.item(i);
	    	if(childNode instanceof Element) {
	    		SVGElement child = (SVGElement)childNode;
	    		
	    		String childId = child.getId();
	        	String column_id = childId.replaceAll(datamart.getTargetFeatureName() + "_", "");
	        	Map attributes = (Map)datamart.getAttributeseById(column_id);
	        	
	    		
	        	String targetColor = null;
	        	Double kpyValue = null;
	        	if(attributes != null) {
	    			String kpyValueAttr = (String)attributes.get( selectedKpiName );	
	    			if(kpyValueAttr == null) {
	    				targetColor = null_values_color;
	    			} else {
	    				kpyValue = Double.parseDouble(kpyValueAttr);
	    				
	    				if(kpyValue < lb_value.doubleValue()) {
	    					targetColor = lb_color;
	    				} else if(kpyValue > ub_value.doubleValue()) {
	    					targetColor = ub_color;
	    				} else if(kpyValue == ub_value.doubleValue()) {
	    					targetColor = col_kpi_array[trash_kpi_array.length-2];
	    				} else  {
	    					for (int j = 0; j < trash_kpi_array.length-1; j++) {
	    						if (kpyValue >= trash_kpi_array[j] && kpyValue <  trash_kpi_array[j+1]) {
	    							targetColor = col_kpi_array[j];
	    							break;
	    						} 
	    					}
	    				}
	    			}
	    		}
	        	
	        	if(targetColor != null) {
	        		child.setAttribute("fill", targetColor );
	        	}
	    		
	    	} 
	    }
	    
	    // add label
	    Map values = datamart.getValues();
	    Iterator it = values.keySet().iterator();
	    while(it.hasNext()) {
	    	String id = (String)it.next();
	    	Map kpiValueMap = (Map)values.get(id);
	    	String centroideId = "centroidi_" + datamart.getTargetFeatureName() + "_"  + id;
	    	Element centroide = targetMap.getElementById( centroideId );
			if( centroide != null ) {
				Iterator kpiValueIterator = kpiValueMap.keySet().iterator();
				int line = 0;
				Element labelGroup = null;
				if(kpiValueIterator.hasNext()) labelGroup = masterMap.createElement("g");
				boolean isFirst = true;
				while(kpiValueIterator.hasNext()) {
					String tmpKpiName = (String)kpiValueIterator.next();
					Measure kpi  = getMeasure( tmpKpiName );
					Object o = kpiValueMap.get(tmpKpiName);
					String kpiValue = (String)kpiValueMap.get(tmpKpiName);
					labelGroup.setAttribute("transform", "translate(" + centroide.getAttribute("cx") + "," + centroide.getAttribute("cy")+ ") scale(40)");
					labelGroup.setAttribute("display", "inherit");
					
					Element label = masterMap.createElement("text");
					label.setAttribute("x", "0");
					label.setAttribute("y", "" + ( (line++ )* 16) );
					label.setAttribute("text-anchor", "middle");
					label.setAttribute("font-family", "Arial,Helvetica");
					label.setAttribute("font-size", isFirst? "16px": "14px");
					label.setAttribute("font-style", isFirst? "normal": "italic");
					label.setAttribute("fill", "black");
					isFirst = false;
					
					String kpiValueString = null;
					if(kpi.getPattern() != null) {
						String pattern = kpi.getPattern();
						DecimalFormat df = new DecimalFormat( pattern );
						kpiValueString = kpiValue != null? df.format(Double.parseDouble(kpiValue)): "?";
					} else {
						kpiValueString = kpiValue != null? kpiValue: "?";
					}
					
					if(!kpiValueString.equalsIgnoreCase("?") && kpi.getUnit() != null) {
						String unit = kpi.getUnit();
						kpiValueString = kpiValueString + unit;
					}
					
					
					Node labelText = masterMap.createTextNode(kpiValueString);
					label.appendChild(labelText);
					
					labelGroup.appendChild(label);
				}
				
				if(labelGroup != null) {
					Element valuesLayer = masterMap.getElementById("values");
					valuesLayer.appendChild(labelGroup);
				}				
			}
	    }
	    
	   
	    
	    // add legend
	    Element windowBackground = masterMap.createElement("rect");
	    windowBackground.setAttribute("width", "241");
	    windowBackground.setAttribute("height", "200");
	    windowBackground.setAttribute("fill", "#fffce6");
	    windowBackground.setAttribute("stroke", "dimgray");
	    windowBackground.setAttribute("stroke-width", "1");
	    windowBackground.setAttribute("display", "inherit");
	    
	    Element windowTitleBar = masterMap.createElement("rect");
	    windowTitleBar.setAttribute("width", "241");
	    windowTitleBar.setAttribute("height", "17");
	    windowTitleBar.setAttribute("fill", "steelblue");
	    windowTitleBar.setAttribute("stroke", "dimgray");
	    windowTitleBar.setAttribute("stroke-width", "1.5");
	    windowTitleBar.setAttribute("display", "inherit");
	    
	    Element windowTitle = masterMap.createElement("text");
	    windowTitle.setAttribute("x", "3");
	    windowTitle.setAttribute("y", "14");
	    windowTitle.setAttribute("font-family", "Arial,Helvetica");
	    windowTitle.setAttribute("font-size", "14px");
	    windowTitle.setAttribute("fill", "white");
	    windowTitle.setAttribute("startOffset", "0");	    
	    Node windowTitleText = masterMap.createTextNode("Legenda");
	    windowTitle.appendChild(windowTitleText);
	    
	    Element windowBody = masterMap.createElement("g");
	    for(int i = 0; i < col_kpi_array.length; i++) {
	    	Double lb = trash_kpi_array[i];
	    	Double ub = trash_kpi_array[i + 1];
	    	String color = col_kpi_array[i];
	    	
	    	String lbValueString = null;
	    	String ubValueString = null;
			if(measure.getPattern() != null) {
				String pattern = measure.getPattern();
				DecimalFormat df = new DecimalFormat( pattern );
				lbValueString = lb != null? df.format(lb.doubleValue()): "?";
				ubValueString = ub != null? df.format(ub.doubleValue()): "?";
			} else {
				lbValueString = lb != null? lb.toString(): "?";
				ubValueString = ub != null? ub.toString(): "?";
			}
			
			if(!lb.toString().equalsIgnoreCase("?") && measure.getUnit() != null) {
				String unit = measure.getUnit();
				lbValueString = lbValueString + unit;
			}
			
			if(!ub.toString().equalsIgnoreCase("?") && measure.getUnit() != null) {
				String unit = measure.getUnit();
				ubValueString = ubValueString + unit;
			}
	    	
	    	Element colorBox = masterMap.createElement("rect");
	    	int offset = 35 + (25 * i);
	    	colorBox.setAttribute("x", "30");
	    	colorBox.setAttribute("y", "" + offset);
	    	colorBox.setAttribute("width", "30");
	    	colorBox.setAttribute("height", "20");
	    	colorBox.setAttribute("fill", color);
	    	colorBox.setAttribute("stroke", "dimgray");
	    	
	    	offset = 50 + (25 * i);
	    	Element labelBox = masterMap.createElement("text");
	    	labelBox.setAttribute("x", "70");
	    	labelBox.setAttribute("y", "" + offset);
	    	labelBox.setAttribute("font-family", "Arial,Helvetica");
	    	labelBox.setAttribute("font-size", "14px");
	    	labelBox.setAttribute("fill", "dimgray");
	    	labelBox.setAttribute("startOffset", "0");	    
		    Node labelBoxText = masterMap.createTextNode(lbValueString + " - " + ubValueString);
		    labelBox.appendChild(labelBoxText);
		    
	    	windowBody.appendChild(colorBox);
	    	windowBody.appendChild(labelBox);
	    }
	    
	    Element legend = masterMap.getElementById("legend");
	    legend.appendChild(windowBackground);
	    legend.appendChild(windowTitleBar);
	    legend.appendChild(windowTitle);
	    legend.appendChild(windowBody);
	    
	    // add labels
	    Node labelText;
	    Element label;
	    
	    label = masterMap.getElementById("header-left");	    
	    labelText = masterMap.createTextNode( getLabelProducer("header-left").getLabel() );
	    label.appendChild(labelText);
	    
	    label = masterMap.getElementById("header-center");	    
	    labelText = masterMap.createTextNode( getLabelProducer("header-center").getLabel() );
	    label.appendChild(labelText);
	    
	    label = masterMap.getElementById("header-right");	    
	    labelText = masterMap.createTextNode( getLabelProducer("header-right").getLabel() );
	    label.appendChild(labelText);
	    
	    label = masterMap.getElementById("footer-left");	    
	    labelText = masterMap.createTextNode( getLabelProducer("footer-left").getLabel() );
	    label.appendChild(labelText);
	    
	    label = masterMap.getElementById("footer-center");	    
	    labelText = masterMap.createTextNode( getLabelProducer("footer-center").getLabel() );
	    label.appendChild(labelText);
	    
	    label = masterMap.getElementById("footer-right");	    
	    labelText = masterMap.createTextNode( getLabelProducer("footer-right").getLabel() );
	    label.appendChild(labelText);
	}	
	
	
	
	
	




	/** The Constant R. */
	private static final int R = 0;
	
	/** The Constant G. */
	private static final int G = 1;
	
	/** The Constant B. */
	private static final int B = 2;
	
	/** The Constant BASE_COLOR. */
	private static final String BASE_COLOR = "#";
	
	/**
	 * Gets the gradient colour range.
	 * 
	 * @param base_color the base_color
	 * @param num_group the num_group
	 * 
	 * @return the gradient colour range
	 */
	public String[] getGradientColourRange(String base_color, int num_group) { 
		int[] A = new int[3];
		int[] RGB = new int[3];
		int[] Grad = new int[3];
		int new_rA;
		int new_gA;
		int new_bA;
		String shade;
		
		
		//if(!colurCalculatorType.equalsIgnoreCase("gradient")) return new String[]{"#FF0000","#00FF00","#FF00FF","#0000FF","#F0F0F0"};
    	
 

		A[R] = Integer.parseInt( base_color.substring(1,3), 16 );
		A[G] = Integer.parseInt( base_color.substring(3,5), 16 );
		A[B] = Integer.parseInt( base_color.substring(5), 16 );
		
		
		System.arraycopy(A, 0, RGB, 0, 3);
		Arrays.sort( RGB );
		for(int i = 0; i < A.length; i++) {
			if(A[i] == RGB[2]) {
				Grad[i] =  (240 - A[i]) / (num_group - 1);	
			} else if( A[i] == RGB[1] ) {
				Grad[i] = (230 - A[i]) / (num_group- 1);
			} else {
				Grad[i] = (220 - A[i]) / (num_group- 1);
			}
		}
		
		String[] colorRangeArray = new String[num_group];
		for(int i = 0; i < num_group; i++) {
			new_rA = A[R] + Grad[R] * i;
			new_gA = A[G] + Grad[G] * i;
			new_bA = A[B] + Grad[B] * i;
			String rA = Integer.toHexString(new_rA);
			String gA = Integer.toHexString(new_gA);
			String bA = Integer.toHexString(new_bA);
			shade = "#" 
					+ (rA.length() == 1?"0":"") + rA
					+ (gA.length() == 1?"0":"") + gA
					+ (bA.length() == 1?"0":"") + bA;
			colorRangeArray[i] = shade;
		}
		List colorRangeList = Arrays.asList( colorRangeArray );
		Collections.reverse( colorRangeList );
		colorRangeArray = (String[])colorRangeList.toArray(new String[0]);
	
		
		
		return colorRangeArray;
	}
	
	
	/**
	 * Adds the data.
	 * 
	 * @param map the map
	 * @param datamart the datamart
	 */
	private void addData(SVGDocument map, DataSet datamart) {
		
		Element targetLayer = map.getElementById(datamart.getTargetFeatureName());
		
		NodeList nodeList = targetLayer.getChildNodes();
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node childNode = (Node)nodeList.item(i);
	    	if(childNode instanceof Element) {
	    		SVGElement child = (SVGElement)childNode;
	    		String childId = child.getId();
	    		String column_id = childId.replaceAll(datamart.getTargetFeatureName() + "_", "");
	    		Map attributes = (Map)datamart.getAttributeseById(column_id);
	    		if(attributes != null) {
	    			SVGMapHandler.addAttributes(child, attributes);
	    			child.setAttribute("attrib:nome", child.getAttribute("id"));	    			
	    		}
	    	} 
	    }
	}

	
	/**
	 * Adds the link.
	 * 
	 * @param map the map
	 * @param datamart the datamart
	 */
	private void addLink(SVGDocument map, DataSet datamart) {	
		
		Element targetLayer = map.getElementById(datamart.getTargetFeatureName());		
		NodeList nodeList = targetLayer.getChildNodes();
		Map mapLink = null;
		List lstLink = new ArrayList();
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node childNode= (Node)nodeList.item(i);
	    	try{
		    	if(childNode instanceof Element) {
		    		SVGElement childOrig =(SVGElement)childNode;	    		
		    		String childId = childOrig.getId();
		    		String column_id = childId.replaceAll(datamart.getTargetFeatureName() + "_", "");	    		
		    		Map mapLinks = datamart.getLinks();
		    		String link = (String)mapLinks.get(column_id);
		    		 
		    		if (link != null) {
		    			mapLink = new HashMap();
		    			mapLink.put("column_id",column_id);
		    			mapLink.put("path", childOrig);
		    			mapLink.put("link", link);		    			
		    			lstLink.add(mapLink);		    			
		    		}
		    	}
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}
	    }//for
	    //adds href links	    
	    for (int j=0; j<lstLink.size(); j++){	
	    	Map tmpMap = (Map)lstLink.get(j);
			Element linkElement = map.createElement("a");
			String pippo = (String)tmpMap.get("link");
			linkElement.setAttribute("xlink:href", (String)tmpMap.get("link"));
			linkElement.setAttribute("target", "_parent");
			linkElement.appendChild((Element)tmpMap.get("path"));
	    	targetLayer.appendChild(linkElement);
		    Node lf = map.createTextNode("\n");
		    targetLayer.appendChild(lf);		    
	    }	    
	    //deletes duplicate path
	    boolean isNew = false;
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node childNode= (Node)nodeList.item(i);	
	    	SVGElement childOrig = null;
	    	if(childNode instanceof SVGElement) {
	    		try{
	    			childOrig = (SVGElement)childNode;
	    		}catch(ClassCastException e){
					
	    			logger.debug("DynamicMapRenderer :: addLinK : Element Generic", e);

	    		}
	    		String childId = "";
	    		String column_id = "";
	    		if (childOrig != null){
		    		childId = childOrig.getId();	    		
		    		column_id = childId.replaceAll(datamart.getTargetFeatureName() + "_", "");
	    		}
	    		Iterator it = lstLink.iterator();
	    		isNew = false;
	    		while(it.hasNext()) {
	    			String tmpMapVal = (String)((Map)it.next()).get("column_id");
	    			if (column_id.equals(tmpMapVal)){
	    				isNew = true;
	    				break;
	    			}
		    	}
	    		if (isNew && childOrig != null)
	    			map.removeChild(childOrig);
	    	}
	    }
	    
	}
	
	/**
	 * Include scripts.
	 * 
	 * @param doc the doc
	 */
	private void includeScripts(SVGDocument doc) {
		Element scriptInit = doc.getElementById("included_scripts");	    
	    Node scriptText = scriptInit.getFirstChild();
	    StringBuffer buffer = new StringBuffer();
	    includeScript(buffer, "helper_functions.js");
	    includeScript(buffer, "timer.js");
	    includeScript(buffer, "mapApp.js");
	    includeScript(buffer, "timer.js");
	    includeScript(buffer, "slider.js");
	    includeScript(buffer, "button.js");
	    includeScript(buffer, "Window.js");
	    includeScript(buffer, "checkbox_and_radiobutton.js");
	    includeScript(buffer, "navigation.js");
	    includeScript(buffer, "tabgroup.js");
	    includeScript(buffer, "barchart.js");
	    includeScript(buffer, "colourPicker.js");
	    
	    scriptText.setNodeValue(buffer.toString());
	}
	
	/**
	 * Import scripts.
	 * 
	 * @param doc the doc
	 */
	private void importScripts(SVGDocument doc) {
		importScipt(doc, "helper_functions.js");
	    importScipt(doc, "timer.js");
	    importScipt(doc, "mapApp.js");
	    importScipt(doc, "timer.js");
	    importScipt(doc, "slider.js");
	    importScipt(doc, "button.js");
	    importScipt(doc, "Window.js");
	    importScipt(doc, "checkbox_and_radiobutton.js");
	    importScipt(doc, "navigation.js");
	    importScipt(doc, "tabgroup.js");
	    importScipt(doc, "barchart.js");
	    importScipt(doc, "colourPicker.js");
	}
	
	/**
	 * Include script.
	 * 
	 * @param buffer the buffer
	 * @param scriptName the script name
	 */
	private void includeScript(StringBuffer buffer, String scriptName) {
		//File file = new File("D:/Documenti/Prototipi/Test/exo-portal-1.1.4-SpagoBI-2.0/webapps/SpagoBIGeoEngine/js/lib/svg-widgets/" + scriptName);
		
		try {
			URL scriptUrl = new URL((String)getEnv().get(Constants.ENV_ABSOLUTE_CONTEXT_URL) + "/js/lib/svg-widgets/" + scriptName);
			//URL scriptUrl = new URL("http://localhost:8080/SpagoBIGeoEngine" + "/js/lib/svg-widgets/" + scriptName);
			
			BufferedReader reader = new BufferedReader( new InputStreamReader( scriptUrl.openStream() ) );
			String line = null;
			while ( (line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Import scipt.
	 * 
	 * @param map the map
	 * @param scriptName the script name
	 */
	private void importScipt(SVGDocument map, String scriptName) {
		Element script = map.createElement("script");
	    script.setAttribute("type", "text/ecmascript");
	    script.setAttribute("xlink:href",(String)getEnv().get(Constants.ENV_CONTEXT_URL) + "/js/lib/svg-widgets/" + scriptName);
	    Element importsBlock = map.getElementById("imported_scripts");
	    importsBlock.appendChild(script);
	    Node lf = map.createTextNode("\n");
	    importsBlock.appendChild(lf);
	}
	
	/**
	 * Sets the main map dimension.
	 * 
	 * @param masterMap the master map
	 * @param targetMap the target map
	 */
	public void setMainMapDimension(SVGDocument masterMap, SVGDocument targetMap) {
		String viewBox = targetMap.getRootElement().getAttribute("viewBox");
		Element mainMapBlock = masterMap.getElementById("mainMap");
		mainMapBlock.setAttribute("viewBox", viewBox);
	}
	
	/**
	 * Sets the main map bkg rect dimension.
	 * 
	 * @param masterMap the master map
	 * @param targetMap the target map
	 */
	public void setMainMapBkgRectDimension(SVGDocument masterMap, SVGDocument targetMap) {
		String viewBox = targetMap.getRootElement().getAttribute("viewBox");
		String[] chunks = viewBox.split(" ");
		String x = chunks[0];
		String y = chunks[1];
		String width = chunks[2];
		String height = chunks[3];
		Element mapBackgroundRect = masterMap.getElementById("mapBackgroundRect");
		mapBackgroundRect.setAttribute("x", x);
		mapBackgroundRect.setAttribute("y", y);
		mapBackgroundRect.setAttribute("width", width);
		mapBackgroundRect.setAttribute("height", height);
	}
	
	/**
	 * Gets the measures configuration script.
	 * 
	 * @param datamart the datamart
	 * 
	 * @return the measures configuration script
	 */
	public String getMeasuresConfigurationScript(DataSet datamart) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\n// MEASURES\n");
	    String[] kpiNames = datamart.getKpiNames();
	    
	    // ...kpi_names
	    buffer.append("var kpi_names = [");	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	buffer.append(separtor + "\"" + kpiNames[i] + "\"");
	    }
	    buffer.append("];\n");
	    
	    //	  ...kpi_descriptions
	    buffer.append("kpi_descriptions = [");	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	buffer.append(separtor + "\"" + getMeasure(kpiNames[i]).getDescription() + "\"");
	    }
	    buffer.append("];\n");
	    
	    //	  ...kpi_descriptions
	    buffer.append("kpi_colours = [");	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	String colour = "'" + getMeasure(kpiNames[i]).getColour() + "'";
	    	buffer.append(separtor + colour);
	    }
	    buffer.append("];\n");
	    
	    buffer.append("var kpi_ordered_values = new Array();\n");	
	    for(int i = 0; i < kpiNames.length; i++) {
	    	Set orderedKpiValuesSet = datamart.getOrderedKpiValuesSet(kpiNames[i]);
	    	Iterator it = orderedKpiValuesSet.iterator();
	    	buffer.append("kpi_ordered_values['" + kpiNames[i] + "'] = [");
	    	String separtor = "";
	    	while(it.hasNext()) {
	    		Double value = (Double)it.next();
	    		buffer.append( separtor +  value.toString() );
	    		separtor = ",";    		
	    	}
	    	 buffer.append("];\n");
	    }
	    
	    buffer.append("var num_group = new Array();\n");
	    buffer.append("var lb_values = new Array();\n");
	    buffer.append("var ub_values = new Array();\n");
	    buffer.append("var lb_color = new Array();\n");
	    buffer.append("var ub_color = new Array();\n");
	    buffer.append("var null_values_color = new Array();\n");
	    buffer.append("var trasholdCalculationType = new Array();\n");
	    buffer.append("var trasholdCalculationPercParams = new Array();\n");
	    buffer.append("var trasholdCalculationUniformParams = new Array();\n");
	    buffer.append("var colorRangeCalculationType = new Array();\n");
	    buffer.append("var colorRangeCalculationGradParams = new Array();\n");
	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	Measure measure  = getMeasure(kpiNames[i]);
	    	buffer.append("\n ");
	    	buffer.append("\n// " + kpiNames[i] + "\n");
	    	
	    	if( measure.getTresholdLb() == null 
	    			|| measure.getTresholdLb().trim().equalsIgnoreCase("")
	    			|| measure.getTresholdLb().equalsIgnoreCase("none") ) {
	    		buffer.append("lb_values['"+ kpiNames[i] +"'] = null;\n");
	    	} else {
	    		buffer.append("lb_values['"+ kpiNames[i] +"'] = " + measure.getTresholdLb() + ";\n");
	    	}
	    	
	    	if( measure.getTresholdUb() == null 
	    			|| measure.getTresholdUb().trim().equalsIgnoreCase("")
	    			|| measure.getTresholdUb().equalsIgnoreCase("none") ) {
	    		buffer.append("ub_values['"+ kpiNames[i] +"'] = null;\n");
	    	} else {
	    		buffer.append("ub_values['"+ kpiNames[i] +"'] = " + measure.getTresholdUb() + ";\n");
	    	}
	    	
	    	
	    	buffer.append("lb_color['"+ kpiNames[i] +"'] = '" + measure.getColurOutboundCol() + "';\n");
	    	buffer.append("ub_color['"+ kpiNames[i] +"'] = '" + measure.getColurOutboundCol() + "';\n");
	    	buffer.append("null_values_color['"+ kpiNames[i] +"'] = '" + measure.getColurNullCol() + "';\n");
	    	buffer.append("trasholdCalculationType['"+ kpiNames[i] +"'] = '" + measure.getTresholdCalculatorType() + "';\n");
	    	
	    	if(measure.getTresholdCalculatorType().equalsIgnoreCase("static") || measure.getTresholdCalculatorType().equalsIgnoreCase("perc")) {
		    	String[] values = getTresholdsArray(measure.getColumnId());
		    	if(values != null && values.length > 0) {
			    	buffer.append("trasholdCalculationPercParams['"+ kpiNames[i] +"'] = [");
			    	
			    	buffer.append(values[0]);
			    	for(int j = 1; j < values.length; j++) {
			    		buffer.append("," + values[j]);
			    	}	    	
			    	buffer.append("];\n");
		    	}
	    	} else {
	    		buffer.append("trasholdCalculationPercParams['"+ kpiNames[i] +"'] = " + "null" + ";\n");
	    	}
	    	
	    	String value = measure.getTresholdCalculatorParameters().getProperty("GROUPS_NUMBER");
	    	if(value != null) {
	    		buffer.append("trasholdCalculationUniformParams['"+ kpiNames[i] +"'] = " + value + ";\n");
	    		buffer.append("num_group['"+ kpiNames[i] +"'] = " + value + ";\n");
	    	}
	    	
	    	buffer.append("colorRangeCalculationType['"+ kpiNames[i] +"'] = '" + measure.getColurCalculatorType() + "';\n");
	    	
	    	value = measure.getColurCalculatorParameters().getProperty("BASE_COLOR");
	    	if(value != null) {
	    		buffer.append("colorRangeCalculationGradParams['"+ kpiNames[i] +"'] = '" + value + "';\n");
	    	}
	    }
	    
	    buffer.append("\n ");
	    
	    
	    
	    
	    
	    
	    
	    buffer.append("\nvar selected_kpi_index = " + datamart.getSelectedKpi() + ";\n");
		
	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	buffer.append("var col_" + kpiNames[i] + " = new Array(");
	    	String[] coloursArray = getColoursArray(kpiNames[i]);
	    	for(int j = 0; j < coloursArray.length; j++) {
	    		String separtor = j>0? ",": "";
		    	buffer.append(separtor + "\"" + coloursArray[j] + "\"");
	    	}
	    	buffer.append(");\n");
	    }
	    
	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	Measure measure  = getMeasure(kpiNames[i]);
	    	if(measure.getTresholdCalculatorType().equalsIgnoreCase("static")) {
		    	buffer.append("thresh_" + kpiNames[i] + " = new Array(");
		    	String[] trasholdsArray = getTresholdsArray(kpiNames[i]);
		    	for(int j = 0; j < trasholdsArray.length; j++) {
		    		String separtor = j>0? ",": "";
			    	buffer.append(separtor + trasholdsArray[j]);
			    }
		    	 buffer.append(");\n");
	    	} else {
	    		buffer.append("thresh_" + kpiNames[i] + " = new Array();\n");
	    	}
	    }
	    
	    return buffer.toString();
	}
	
	/**
	 * Gets the layers configuration script.
	 * 
	 * @param doc the doc
	 * @param targetLayer the target layer
	 * 
	 * @return the layers configuration script
	 */
	public String getLayersConfigurationScript(SVGDocument doc, String targetLayer) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("// LAYERS\n");
	    String[] layerNames = getLayerNames();
	   
	    int targetLayerIndex = 0;
	    boolean includeChartLayer = false;
	    boolean includeValuesLayer = false;
	    buffer.append("var layer_names = [");	
	    String separtor = "";
	    for(int i = 0; i < layerNames.length; i++) {	
	    	if(targetLayer.equalsIgnoreCase(layerNames[i])) targetLayerIndex = i;
	    	if(doc.getElementById(layerNames[i]) != null) { 
	    		buffer.append(separtor + "\"" + layerNames[i] + "\"");
	    		separtor = ",";
	    	} else if (layerNames[i].equalsIgnoreCase("grafici")){
	    		buffer.append(separtor + "\"" + layerNames[i] + "\"");
	    		separtor = ",";
	    		includeChartLayer = true;
	    	} else if(layerNames[i].equalsIgnoreCase("valori")) {
	    		buffer.append(separtor + "\"" + layerNames[i] + "\"");
	    		separtor = ",";
	    		includeValuesLayer = true;
	    	}
	    }
	    buffer.append("];\n");
	    
	    buffer.append("var layer_descriptions = [");
	    separtor = "";
	    for(int i = 0; i < layerNames.length; i++) {
	    	if(doc.getElementById(layerNames[i]) != null) { 
	    		buffer.append(separtor + "\"" + getLayer(layerNames[i]).getDescription() + "\"");
	    		separtor = ",";
	    	} else if (layerNames[i].equalsIgnoreCase("grafici")){
	    		buffer.append(separtor + "\"Grafici\"");
	    		separtor = ",";
	    	} else if (layerNames[i].equalsIgnoreCase("valori")){
	    		buffer.append(separtor + "\"Valori\"");
	    		separtor = ",";
	    	}
	    	
	    }	    
	    buffer.append("];\n");
	    
	    buffer.append("var includeChartLayer = " + includeChartLayer + ";\n");
	    buffer.append("var includeValuesLayer = " + includeValuesLayer + ";\n");
	    
	    buffer.append("var target_layer_index = " + targetLayerIndex +";\n");
	    
	    return buffer.toString();
	}
	
	
	/**
	 * Gets the master map file.
	 * 
	 * @param interactiveMasterMap the interactive master map
	 * 
	 * @return the master map file
	 */
	private File getMasterMapFile(boolean interactiveMasterMap) {
		File file = null;
		if(interactiveMasterMap) {
			file =  new File(ConfigSingleton.getRootPath() + "/maps/spagobigeo.svg");
		} else {
			file = new File(ConfigSingleton.getRootPath() + "/maps/export_spagobigeo.svg");
		}
		return file;
	}
	
	/**
	 * Gets the temporary file.
	 * 
	 * @return the temporary file
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File getTempFile() throws IOException {
		String tempDirName = null;
		File tempDir = null;
		File result = null;
		SourceBean geoEngineConf = (SourceBean)ConfigSingleton.getInstance().getAttribute("GEO-ENGINE");
		if(geoEngineConf != null) {
			tempDirName = (String)geoEngineConf.getAttribute("tempDir");
		}
		
		if(tempDirName != null) {
			logger.debug("temp directory path configured: " + tempDirName);
			if(tempDirName.startsWith("./")) {
				logger.debug("temp directory path is relative to working directory: " + System.getProperty("user.dir"));
				tempDir = new File(System.getProperty("user.dir") + "/" + tempDirName);
				logger.debug("temp directory absolute path: " + tempDir);
			} else {
				tempDir = new File(tempDirName);
			}
			result = new File(tempDir, "SpagoBIGeoEngine_" + System.currentTimeMillis() + "_tmpMap.svg");
			boolean isFileCreated = result.createNewFile();
			if(isFileCreated) {
				logger.debug("temp file successfully created: " + result);
			} else {
				logger.error("impossible to create a new temp file: " + result);
			}
		} else {
			logger.debug("temp directory path not configured");
			tempDirName = System.getProperty("java.io.tmpdir");
			logger.debug("System temp directory will be used: " + tempDirName);
			tempDir = new File(tempDirName);
			result = File.createTempFile("SpagoBIGeoEngine_", "_tmpMap.svg", tempDir);
			logger.debug("temp file successfully created: " + result);
		}
		
		
		
		return result;
	}
	
	/**
	 * Gets the gUI configuration script.
	 * 
	 * @return the gUI configuration script
	 */
	public String getGUIConfigurationScript() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("// GUI SETTINGS\n");
		String pVal = null;
		
		pVal =(String)getEnv().get(Constants.ENV_IS_WINDOWS_ACTIVE);
		boolean activeWindow = pVal==null||pVal.equalsIgnoreCase("TRUE");
		buffer.append("var activeWindow=" + (activeWindow?"true":"false")+ ";\n");
		
		pVal =(String)getEnv().get(Constants.ENV_IS_DAFAULT_DRILL_NAV);
		boolean defaultDrillNav = pVal==null||pVal.equalsIgnoreCase("TRUE");
		buffer.append("var defaultDrillNav=" + (defaultDrillNav?"true":"false")+ ";\n");		
		
		return buffer.toString();		
	}
	
	/**
	 * Gets the label producer.
	 * 
	 * @param key the key
	 * 
	 * @return the label producer
	 */
	public LabelProducer getLabelProducer(String key) {
		return (LabelProducer)labelProducers.get(key);
	}

	/**
	 * Gets the label producers.
	 * 
	 * @return the label producers
	 */
	public Map getLabelProducers() {
		return labelProducers;
	}

	/**
	 * Sets the label producers.
	 * 
	 * @param labelProducers the new label producers
	 */
	public void setLabelProducers(Map labelProducers) {
		this.labelProducers = labelProducers;
	}
}