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
package it.eng.spagobi.geo.map.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;

/**
 * @author Andrea Gioia
 */
public class MapMerger {

	private static File mainMap = new File("C:/Documents and Settings/sulis/Desktop/GENOVA/java/test.svg");
	private static File embededMap = new File("C:/Documents and Settings/sulis/Desktop/GENOVA/java/Circoscrizioni.svg");
	private static File outputMap = new File("C:/Documents and Settings/sulis/Desktop/GENOVA/java/out.svg");
	
	
	public List getChildsElement(Element e) {
		List childElements = new ArrayList();
		NodeList nodeList = e.getChildNodes();
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node childNode = (Node)nodeList.item(i);
	    	if(childNode instanceof Element) {
	    		childElements.add(childNode);
	    	} 
	    }
	    return childElements;
	}
	
	public void addAttributes(Element e, Map attributes) {
		Iterator it = attributes.keySet().iterator();
		while(it.hasNext()) {
			String attributeName = (String)it.next();
			String attributeValue = (String)attributes.get(attributeName);
			e.setAttribute("attrib:" + attributeName, attributeValue);
		}
	}
	
	public void addData(Element e, Map data) {
		NodeList nodeList = e.getChildNodes();
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node childNode = (Node)nodeList.item(i);
	    	if(childNode instanceof Element) {
	    		SVGElement child = (SVGElement)childNode;
	    		String childId = child.getId();
	    		Map attributes = (Map)data.get(childId);
	    		addAttributes(child, attributes);
	    	} 
	    }
	}
	
	public Map getData(Element e) {
		Map data = new HashMap();
		NodeList nodeList = e.getChildNodes();
		  for(int i = 0; i < nodeList.getLength(); i++){
		    Node childNode = (Node)nodeList.item(i);
	    	if(childNode instanceof Element) {
	    		SVGElement child = (SVGElement)childNode;
	    		String childId = child.getId();
	    		Map attributes = new HashMap();
	    		Random rand = new Random();
	    		attributes.put("asili", "" + rand.nextInt(10));
	    		attributes.put("docenti", "" + rand.nextInt(100));
	    		attributes.put("iscritti", "" + rand.nextInt(1000));
	    		attributes.put("nome", childId);  	
	    		data.put(childId, attributes);
	    	} 
	    }
	    
	    return data;
	}
	
	
	
	public void saveMap(SVGDocument doc, File ouputFile) throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
	    
	    StreamResult result = new StreamResult(new FileOutputStream(ouputFile));
	    transformer.transform(source, result); 
	}
	
	
	
	public void includeMap(File mainMapFile, 
			File embededMapFile, 
			File ouputFile) throws Exception {
		
		
		String parser = XMLResourceDescriptor.getXMLParserClassName();
	    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
	    String uri = null;
	    
	    uri = embededMapFile.toURI().toURL().toString();
	    SVGDocument emededMap = (SVGDocument)f.createDocument(uri);
	    SVGElement emededMapRoot = emededMap.getRootElement();
	    
	    uri = mainMapFile.toURI().toURL().toString();
	    SVGDocument mainMap = (SVGDocument)f.createDocument(uri);
	    SVGElement mainMapRoot = emededMap.getRootElement();
	    
	    	    
	    Element targetLayer = emededMap.getElementById("circoscrizioni");
	    addData(targetLayer, getData(targetLayer));	  
	    
	    Element targetMap = mainMap.getElementById("targetMap");
	    NodeList nodeList = emededMapRoot.getChildNodes();	    
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node node = (Node)nodeList.item(i);
	    	Node importedNode = mainMap.importNode(node, true);
	    	targetMap.appendChild(importedNode);
	    }
	    
	    String viewBox = emededMapRoot.getAttribute("viewBox");
	    String[] chunks = viewBox.split(" ");
	    String x = chunks[0];
	    String y = chunks[1];
	    String width = chunks[2];
	    String height = chunks[2];
	    Element mapBackgroundRect = mainMap.getElementById("mapBackgroundRect");
	    mapBackgroundRect.setAttribute("x", x);
	    mapBackgroundRect.setAttribute("y", y);
	    mapBackgroundRect.setAttribute("width", width);
	    mapBackgroundRect.setAttribute("height", height);
	    
	    System.out.println(viewBox);
	    System.out.println(mainMapRoot.getTagName());
	   
	    Element mainMapBlock = mainMap.getElementById("mainMap");
	    mainMapBlock.setAttribute("viewBox", viewBox);
	    System.out.println(mainMapBlock.getAttribute("viewBox"));
	    
	    Element scriptInit = mainMap.getElementById("init");
	    Node scriptText = scriptInit.getFirstChild();
	    System.out.println(scriptText.getNodeValue());
	    scriptText.setNodeValue("// MEASURES");
	    
	    saveMap(mainMap, ouputFile);
	}
	
	public static void main(String[] args) throws Exception {
		
		MapMerger mapMerger = new MapMerger();
		mapMerger.includeMap(mainMap, embededMap, outputMap);
		

	}
}
