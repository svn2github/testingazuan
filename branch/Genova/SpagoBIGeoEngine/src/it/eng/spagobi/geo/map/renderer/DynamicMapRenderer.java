/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.map.renderer;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;
import it.eng.spagobi.geo.datamart.provider.DatamartProviderFactory;
import it.eng.spagobi.geo.datamart.provider.IDatamartProvider;
import it.eng.spagobi.geo.map.provider.MapProviderFactory;
import it.eng.spagobi.geo.map.provider.IMapProvider;
import it.eng.spagobi.geo.map.utils.SVGMapLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
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
	
	public void importScipt(SVGDocument map, String scriptName) {
		Element script = map.createElement("script");
	    script.setAttribute("type", "text/ecmascript");
	    //script.setAttribute("xlink:href", ConfigSingleton.getRootPath() + "/js/" + scriptName);
	    //script.setAttribute("xlink:href", "http://172.21.5.69:8080/SpagoBIGeoEngine/js/" + scriptName);
	    //System.out.println("-->" + (ConfigSingleton.getRootPath() + "/js/" + scriptName) );
	    script.setAttribute("xlink:href", mapRendererConfiguration.getContextPath() + "/js/" + scriptName);
	    Element importsBlock = map.getElementById("imports");
	    importsBlock.appendChild(script);
	    Node lf = map.createTextNode("\n");
	    importsBlock.appendChild(lf);
	}
	
	/**
	 * @see it.eng.spagobi.geo.map.renderer.IMapRenderer#renderMap(MapConfiguration)
	 */
	public File renderMap(IMapProvider mapProvider, IDatamartProvider datamartProvider) throws Exception {
	
		SVGDocument targetMap;
		SVGDocument masterMap;
		Datamart datamart;
		
		masterMap = SVGMapLoader.loadMapAsDocument(getMasterMapFile());
		
		targetMap = mapProvider.getSVGMapDOMDocument();
				
		datamart = (Datamart)datamartProvider.getDatamartObject();
		
		addData(targetMap, datamart);
		margeSVG(targetMap, masterMap, null, "targetMap");
		
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
	    	    	   
	    Element mainMapBlock = masterMap.getElementById("mainMap");
	    mainMapBlock.setAttribute("viewBox", viewBox);
	    
	    
	    importScipt(masterMap, "helper_functions.js");
	    importScipt(masterMap, "timer.js");
	    importScipt(masterMap, "mapApp.js");
	    importScipt(masterMap, "timer.js");
	    importScipt(masterMap, "slider.js");
	    importScipt(masterMap, "button.js");
	    importScipt(masterMap, "Window.js");
	    importScipt(masterMap, "checkbox_and_radiobutton.js");
	    importScipt(masterMap, "navigation.js");
	    importScipt(masterMap, "tabgroup.js");
	    importScipt(masterMap, "barchart.js");
	    
	   
	    
	    Element scriptInit = masterMap.getElementById("init");
	    
	    Node scriptText = scriptInit.getFirstChild();
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
	    
	    Random rand = new Random();
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
	   
	    
	    buffer.append("// LAYERS\n");
	    String[] layerNames = mapRendererConfiguration.getLayerNames();
	    buffer.append("var layer_names = [");	    
	    for(int i = 0; i < layerNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	buffer.append(separtor + "\"" + layerNames[i] + "\"");
	    }
	    buffer.append(", \"grafici\"];\n");
	    
	    buffer.append("var layer_descriptions = [");	    
	    for(int i = 0; i < layerNames.length; i++) {
	    	String separtor = i>0? ",": "";
	    	buffer.append(separtor + "\"" + mapRendererConfiguration.getLayerDescription(layerNames[i]) + "\"");
	    }	    
	    buffer.append(", \"Grafici\"];\n");
	    
	    buffer.append("var target_layer_index = 0;\n");
	    
	    
	   
	    
	    scriptText.setNodeValue(buffer.toString());
		
		
		String tmpDirPath = System.getProperty("java.io.tmpdir");
		File tmpDir = new File(tmpDirPath);
		File tmpMap = File.createTempFile("SpagoBIGeoEngine_", "_tmpMap", tmpDir);
				
		try{
			saveMap(masterMap, tmpMap);
		} catch(Exception e) {
			System.err.println("Errore");
		} finally {
		
		}
		
		//System.out.println(tmpMap);
		
		return tmpMap;
	}
	
	public void saveMap(SVGDocument doc, File ouputFile) throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
	    
	    StreamResult result = new StreamResult(new FileOutputStream(ouputFile));
	    transformer.transform(source, result); 
	}
	
	private void margeSVG(SVGDocument srcMap, SVGDocument dstMap, String srcId, String dstId) {
		SVGElement destMapRoot = dstMap.getRootElement();
		Element dstElement = dstMap.getElementById(dstId);
		
		SVGElement srcMapRoot = srcMap.getRootElement();
		Element srcElement = ( srcId == null? srcMapRoot: srcMap.getElementById(srcId) );
	    
		NodeList nodeList = srcElement.getChildNodes();	    
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node node = (Node)nodeList.item(i);
	    	Node importedNode = dstMap.importNode(node, true);
	    	dstElement.appendChild(importedNode);
	    }
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
	    		System.out.println("\n" + column_id);
	    		Map attributes = (Map)datamart.getAttributeseById(column_id);
	    		if(attributes != null) {
	    			addAttributes(child, attributes);
	    		}
	    	} 
	    }
	}
	
	public void addAttributes(Element e, Map attributes) {
		Iterator it = attributes.keySet().iterator();
		while(it.hasNext()) {
			String attributeName = (String)it.next();
			String attributeValue = (String)attributes.get(attributeName);
			e.setAttribute("attrib:" + attributeName, attributeValue);
			System.out.println(attributeName + ": " + attributeValue);
		}
		//e.setAttribute("fill", "gray");
		e.setAttribute("attrib:nome", e.getAttribute("id"));
	}
	
	private File getMasterMapFile() {
		return new File(ConfigSingleton.getRootPath() + "/maps/spagobigeo.svg");
	}
	

}