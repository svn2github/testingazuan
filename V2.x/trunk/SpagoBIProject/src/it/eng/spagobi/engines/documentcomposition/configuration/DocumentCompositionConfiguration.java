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
package it.eng.spagobi.engines.documentcomposition.configuration;

import it.eng.spago.base.SourceBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */

/*
 * This class create a configuration object (alias an object with all informations about the configuration template)
 * and create an object in session.
 */
public class DocumentCompositionConfiguration {
	private String templateFile;
	private Map documentsMap;
	
	public static class Document {
		String label;
		String sbiObjLabel;
		String dimensionWidth;
		String dimensionHeight;
		
		String refreshType;
		Properties refreshDocLinked;
		
		String namePar;
		String sbiParName;
		String type;
		String defaultValue;
		Properties parLinked;
		Properties params;
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getSbiObjLabel() {
			return sbiObjLabel;
		}
		public void setSbiObjLabel(String sbiObjLabel) {
			this.sbiObjLabel = sbiObjLabel;
		}
		public String getDimensionWidth() {
			return dimensionWidth;
		}
		public void setDimensionWidth(String dimensionWidth) {
			this.dimensionWidth = dimensionWidth;
		}
		public String getDimensionHeight() {
			return dimensionHeight;
		}
		public void setDimensionHeight(String dimensionHeight) {
			this.dimensionHeight = dimensionHeight;
		}
		public String getRefreshType() {
			return refreshType;
		}
		public void setRefreshType(String refreshType) {
			this.refreshType = refreshType;
		}
		public Properties getRefreshDocLinked() {
			return refreshDocLinked;
		}
		public void setRefreshDocLinked(Properties refreshDocLinked) {
			this.refreshDocLinked = refreshDocLinked;
		}
		public String getNamePar() {
			return namePar;
		}
		public void setNamePar(String namePar) {
			this.namePar = namePar;
		}
		public String getSbiParName() {
			return sbiParName;
		}
		public void setSbiParName(String sbiParName) {
			this.sbiParName = sbiParName;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}
		public Properties getParLinked() {
			return parLinked;
		}
		public void setParLinked(Properties parLinked) {
			this.parLinked = parLinked;
		}
		public Properties getParams() {
			return params;
		}
		public void setParams(Properties params) {
			this.params = params;
		}
	}
	
	public DocumentCompositionConfiguration () {
		documentsMap = new HashMap();
	}

	public DocumentCompositionConfiguration (SourceBean DocumentCompositionConfigurationSB){
		
		SourceBean documentsConfigurationSB;
		templateFile = (String)DocumentCompositionConfigurationSB.getAttribute("template_value");
		documentsMap = new HashMap();
	
		documentsConfigurationSB = (SourceBean)DocumentCompositionConfigurationSB.getAttribute("DOCUMENTS_CONFIGURATION");
	
		initDocuments(documentsConfigurationSB);
	}
	
	public void addDocument(Document document) {
		if(documentsMap == null) documentsMap = new HashMap();
		documentsMap.put(document.getLabel(), document);
	}
	
	public void resetDocuments() {		
		documentsMap = new HashMap();
	}
	
	private void initDocuments(SourceBean documentsConfigurationSB) {
		Document document;
		String attributeValue;
		
		List documentList;
		List refreshDocList;
		List paramList;
		List paramLinkedList;
		SourceBean documentSB;
		SourceBean refreshSB;
		SourceBean dimensionSB;
		SourceBean parametersSB;
		SourceBean paramSB;
		SourceBean refreshDocLinkedSB;
		
		
		documentList = documentsConfigurationSB.getAttributeAsList("DOCUMENT");
		for(int i = 0; i < documentList.size(); i++) {
			
			documentSB = (SourceBean)documentList.get(i);
			document = new Document();			
			
			attributeValue = (String)documentSB.getAttribute("label");
			document.setLabel(attributeValue);			
			attributeValue = (String)documentSB.getAttribute("sbi_obj_label");
			document.setSbiObjLabel(attributeValue);
			
			dimensionSB = (SourceBean)documentSB.getAttribute("DIMENSION");			
			attributeValue = (String)dimensionSB.getAttribute("width");
			document.setDimensionWidth(attributeValue);			
			attributeValue = (String)dimensionSB.getAttribute("height");
			document.setDimensionHeight(attributeValue);				
			
			refreshSB = (SourceBean)documentSB.getAttribute("REFRESH");				
			attributeValue = (String)refreshSB.getAttribute("type");
			document.setRefreshType(attributeValue);		
			
			refreshDocList = refreshSB.getAttributeAsList("REFRESH_DOC_LINKED");
			Properties refreshDocLinked = new Properties();
			for(int j = 0; j < refreshDocList.size(); j++) {
				refreshDocLinkedSB = (SourceBean)refreshDocList.get(j);
				String label = (refreshDocLinkedSB.getAttribute("label")==null)?"":(String)refreshDocLinkedSB.getAttribute("label");
				refreshDocLinked.setProperty("refresh_doc_linked_"+i, label);
			}
			document.setRefreshDocLinked(refreshDocLinked);
			
			parametersSB = (SourceBean)documentSB.getAttribute("PARAMETERS");	
			paramList = parametersSB.getAttributeAsList("PARAMETER");
			Properties param = new Properties();
			for(int j = 0; j < paramList.size(); j++) {
				paramSB = (SourceBean)paramList.get(j);
				String label = (paramSB.getAttribute("label")==null)?"":(String)paramSB.getAttribute("label");
				param.setProperty("label_param_"+j, label);
				String sbiParLabel = (paramSB.getAttribute("sbi_par_label")==null)?"":(String)paramSB.getAttribute("sbi_par_label");
				param.setProperty("sbi_par_label_param_"+j, sbiParLabel);
				String defaultValuePar = (paramSB.getAttribute("default_value")==null)?"":(String)paramSB.getAttribute("default_value");
				param.setProperty("default_value_param_"+j, defaultValuePar);
				
				paramLinkedList = parametersSB.getAttributeAsList("PAR_LINKED");
				Properties paramLinked = new Properties();
				for(int k = 0; k < paramLinkedList.size(); k++) {
					String labelLinked = (paramSB.getAttribute("label")==null)?"":(String)paramSB.getAttribute("label");
					paramLinked.setProperty("label_param_linked_"+k, labelLinked);
					String defaultValueLinked = (paramSB.getAttribute("default_value")==null)?"":(String)paramSB.getAttribute("default_value");
					paramLinked.setProperty("default_value_linked_"+k, defaultValueLinked);
					param.setProperty("param_linked_"+k, paramLinked.toString());
				}
				
			}
			document.setParams(param);
			addDocument(document);
		}		
		
	}

	
	public Document getDocument(String documentName) {
		return (Document)documentsMap.get(documentName);
	}
	
	
	public String getLabel(String documentLabel) {
		Document document = getDocument(documentLabel);
		if(document != null) return document.getLabel();

		return null;
	}
	
	public List getLabelsArray() {
		Collection collLabels = documentsMap.values();
		List retLabels = new ArrayList();
		Object[] arrDocs = (Object[])collLabels.toArray();
		try{
			for(int i=0; i < arrDocs.length; i++){
				Document tmpDoc =(Document) arrDocs[i];
				retLabels.add(tmpDoc.getLabel());
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return retLabels;

	}
	
	public List getSbiObjLabelsArray() {
		Collection collLabels = documentsMap.values();
		List retLabels = new ArrayList();
		Object[] arrDocs = (Object[])collLabels.toArray();
		try{
			for(int i=0; i < arrDocs.length; i++){
				Document tmpDoc =(Document) arrDocs[i];
				retLabels.add(tmpDoc.getSbiObjLabel());
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return retLabels;

	}
	
	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}
	
	public List getParametersArray() {
		Collection collDocs = documentsMap.values();
		List retParams = new ArrayList();
		Object[] arrPars = (Object[])collDocs.toArray();
		try{
			for(int i=0; i < arrPars.length; i++){
				Document tmpDoc =(Document) arrPars[i];
				retParams.add(tmpDoc.getParams());
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return retParams;

	}
	
	public List getDocumentsArray() {
		Collection collDocs = documentsMap.values();
		List retDocs = new ArrayList();
		Object[] arrDocs = (Object[])collDocs.toArray();
		try{
			for(int i=0; i < arrDocs.length; i++){
				Document tmpDoc =(Document) arrDocs[i];
				retDocs.add(tmpDoc);
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return retDocs;

	}
	
/*
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
	*/


}