/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.map.renderer;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;
import it.eng.spagobi.geo.datamart.provider.IDatamartProvider;
import it.eng.spagobi.geo.map.provider.IMapProvider;
import it.eng.spagobi.geo.map.utils.SVGMapHandler;
import it.eng.spagobi.geo.map.utils.SVGMapLoader;
import it.eng.spagobi.geo.map.utils.SVGMapMerger;
import it.eng.spagobi.geo.map.utils.SVGMapSaver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;

/**
 * Defines methods which transform and render the map 
 * @author fiscato
 */
public class DynamicMapRenderer extends AbstractMapRenderer {

	private boolean closeLink = false;
	
	public DynamicMapRenderer() {
		super();
	}
	
	/**
	 * @see it.eng.spagobi.geo.map.renderer.IMapRenderer#renderMap(MapConfiguration)
	 */
	public File renderMap(IMapProvider mapProvider, IDatamartProvider datamartProvider) throws Exception {
	
		SVGDocument targetMap;
		SVGDocument masterMap;
		Datamart datamart;
		
		datamart = (Datamart)datamartProvider.getDatamartObject();
		
		masterMap = SVGMapLoader.loadMapAsDocument(getMasterMapFile());		
		targetMap = mapProvider.getSVGMapDOMDocument(datamart);				
				
		addData(targetMap, datamart);
		addLink(targetMap, datamart);
		
		SVGMapMerger.margeMap(targetMap, masterMap, null, "targetMap");
		
		importScripts(masterMap);
		setMainMapDimension(masterMap, targetMap);
		setMainMapBkgRectDimension(masterMap, targetMap);	   	   
	    
	    Element scriptInit = masterMap.getElementById("init");	    
	    Node scriptText = scriptInit.getFirstChild();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(getMeasuresConfigurationScript(datamart));
	    buffer.append(getLayersConfigurationScript(targetMap));    
	    scriptText.setNodeValue(buffer.toString());
		
		File tmpMap = getTempFile();				
		SVGMapSaver.saveMap(masterMap, tmpMap);

		return tmpMap;
	}
	
	
	private void addData(SVGDocument map, Datamart datamart) {
		
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

	
	private void addLink(SVGDocument map, Datamart datamart) {	
		/*
		Element targetLayer = map.getElementById(datamart.getTargetFeatureName());		
		NodeList nodeList = targetLayer.getChildNodes();
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
		    			int toSubStr = childId.indexOf("_"+column_id);
		    			Element mapPathBlock = map.getElementById(childId.substring(0,toSubStr));
		    			Element linkElement = map.createElement("a");
		    			linkElement.setAttribute("xlink:href", link);
		    			lstLink.add(linkElement);
		    			//mapPathBlock.appendChild(linkElement);
		    			//Element mapBlock = map.getElementById(childId);
		    			//mapPathBlock.appendChild(mapBlock);
		    			//Node href_stop = map.createTextNode("<//a>");
		    			//mapPathBlock.appendChild(href_stop);
		    			//map.removeChild(childOrig);
		    		}
		    	}
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    */
	}
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
	}
	
	private void importScipt(SVGDocument map, String scriptName) {
		Element script = map.createElement("script");
	    script.setAttribute("type", "text/ecmascript");
	    script.setAttribute("xlink:href", mapRendererConfiguration.getContextPath() + "/js/" + scriptName);
	    Element importsBlock = map.getElementById("imports");
	    importsBlock.appendChild(script);
	    Node lf = map.createTextNode("\n");
	    importsBlock.appendChild(lf);
	}
	
	public void setMainMapDimension(SVGDocument masterMap, SVGDocument targetMap) {
		String viewBox = targetMap.getRootElement().getAttribute("viewBox");
		Element mainMapBlock = masterMap.getElementById("mainMap");
		mainMapBlock.setAttribute("viewBox", viewBox);
	}
	
	public void setMainMapBkgRectDimension(SVGDocument masterMap, SVGDocument targetMap) {
		String viewBox = targetMap.getRootElement().getAttribute("viewBox");
		String[] chunks = viewBox.split(" ");
		String x = chunks[0];
		String y = chunks[1];
		String width = chunks[2];
		String height = chunks[2];
		Element mapBackgroundRect = masterMap.getElementById("mapBackgroundRect");
		mapBackgroundRect.setAttribute("x", x);
		mapBackgroundRect.setAttribute("y", y);
		mapBackgroundRect.setAttribute("width", width);
		mapBackgroundRect.setAttribute("height", height);
	}
	
	public String getMeasuresConfigurationScript(Datamart datamart) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("// MEASURES\n");
	    String[] kpiNames = datamart.getKpiNames();
	    buffer.append("var kpi_names = [");	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	buffer.append(separtor + "\"" + kpiNames[i] + "\"");
	    }
	    buffer.append("];\n");
	    
	    buffer.append("kpi_descriptions = [");	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	buffer.append(separtor + "\"" + mapRendererConfiguration.getKpiDescription(kpiNames[i]) + "\"");
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
	    
	    
	    buffer.append("kpi_colours = [");	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	buffer.append(separtor + mapRendererConfiguration.getKpiColour(kpiNames[i]));
	    }
	    buffer.append("];\n");
	    
	    
	    
	    buffer.append("var selected_kpi_index = " + datamart.getSelectedKpi() + ";\n");
		
	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	buffer.append("var col_" + kpiNames[i] + " = new Array(");
	    	String[] coloursArray = mapRendererConfiguration.getColoursArray(kpiNames[i]);
	    	for(int j = 0; j < coloursArray.length; j++) {
	    		String separtor = j>0? ",": "";
		    	buffer.append(separtor + "\"" + coloursArray[j] + "\"");
	    	}
	    	buffer.append(");\n");
	    }
	    
	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	buffer.append("thresh_" + kpiNames[i] + " = new Array(");
	    	String[] trasholdsArray = mapRendererConfiguration.getTresholdsArray(kpiNames[i]);
	    	for(int j = 0; j < trasholdsArray.length; j++) {
	    		String separtor = j>0? ",": "";
		    	buffer.append(separtor + trasholdsArray[j]);
		    }
	    	 buffer.append(");\n");
	    }
	    
	    return buffer.toString();
	}
	
	public String getLayersConfigurationScript(SVGDocument doc) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("// LAYERS\n");
	    String[] layerNames = mapRendererConfiguration.getLayerNames();
	    boolean includeChartLayer = false;
	    boolean includeValuesLayer = false;
	    buffer.append("var layer_names = [");	
	    String separtor = "";
	    for(int i = 0; i < layerNames.length; i++) {	    	
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
	    		buffer.append(separtor + "\"" + mapRendererConfiguration.getLayerDescription(layerNames[i]) + "\"");
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
	    
	    buffer.append("var target_layer_index = 0;\n");
	    
	    return buffer.toString();
	}
	
	
	private File getMasterMapFile() {
		return new File(ConfigSingleton.getRootPath() + "/maps/spagobigeo.svg");
	}
	
	public File getTempFile() throws IOException{
		String tmpDirPath = System.getProperty("java.io.tmpdir");
		File tmpDir = new File(tmpDirPath);
		return File.createTempFile("SpagoBIGeoEngine_", "_tmpMap.svg", tmpDir);
	}
	

}