/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.map.renderer;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.configuration.MapRendererConfiguration.Measure;
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
import java.util.HashMap;
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
	    String targetLayer = datamartProvider.getDatamartProviderConfiguration().getHierarchyLevel();
	    targetLayer = datamartProvider.getDatamartProviderConfiguration().getSelectedLevel().getFeatureName();
	    buffer.append(getLayersConfigurationScript(targetMap, targetLayer));    
	    
	    
	    
	    
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
	    	if(childNode instanceof Element) {
	    		try{
	    			childOrig = (SVGElement)childNode;
	    		}catch(ClassCastException e){
					TracerSingleton.log(Constants.LOG_NAME, 
	            			TracerSingleton.DEBUG, 
	            			"DynamicMapRenderer :: addLinK : Element Generic", e);

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
		String height = chunks[3];
		Element mapBackgroundRect = masterMap.getElementById("mapBackgroundRect");
		mapBackgroundRect.setAttribute("x", x);
		mapBackgroundRect.setAttribute("y", y);
		mapBackgroundRect.setAttribute("width", width);
		mapBackgroundRect.setAttribute("height", height);
	}
	
	public String getMeasuresConfigurationScript(Datamart datamart) {
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
	    	buffer.append(separtor + "\"" + mapRendererConfiguration.getKpiDescription(kpiNames[i]) + "\"");
	    }
	    buffer.append("];\n");
	    
	    //	  ...kpi_descriptions
	    buffer.append("kpi_colours = [");	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	String colour = "'" + mapRendererConfiguration.getKpiColour(kpiNames[i]) + "'";
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
	    	Measure measure  = mapRendererConfiguration.getMeasure(kpiNames[i]);
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
	    	
	    	if(measure.getTresholdCalculatorType().equalsIgnoreCase("static")) {
		    	String[] values = mapRendererConfiguration.getTresholdsArray(measure.getColumnId());
		    	if(values != null && values.length < 0) {
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
	    	String[] coloursArray = mapRendererConfiguration.getColoursArray(kpiNames[i]);
	    	for(int j = 0; j < coloursArray.length; j++) {
	    		String separtor = j>0? ",": "";
		    	buffer.append(separtor + "\"" + coloursArray[j] + "\"");
	    	}
	    	buffer.append(");\n");
	    }
	    
	    
	    for(int i = 0; i < kpiNames.length; i++) {
	    	Measure measure  = mapRendererConfiguration.getMeasure(kpiNames[i]);
	    	if(measure.getTresholdCalculatorType().equalsIgnoreCase("static")) {
		    	buffer.append("thresh_" + kpiNames[i] + " = new Array(");
		    	String[] trasholdsArray = mapRendererConfiguration.getTresholdsArray(kpiNames[i]);
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
	
	public String getLayersConfigurationScript(SVGDocument doc, String targetLayer) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("// LAYERS\n");
	    String[] layerNames = mapRendererConfiguration.getLayerNames();
	   
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
	    
	    buffer.append("var target_layer_index = " + targetLayerIndex +";\n");
	    
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