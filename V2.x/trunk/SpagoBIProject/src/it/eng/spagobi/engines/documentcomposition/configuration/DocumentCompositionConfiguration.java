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
import java.util.Enumeration;
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
	//constants for convert panel dimensions from percentage into pixel values
	Integer[] percentageValues = {new Integer("100"), new Integer("75"), new Integer("50"), new Integer("25")};
	Integer[] widthPxValues = {new Integer("1000"), new Integer("750"), new Integer("500"), new Integer("250")};
	Integer[] heightPxValues = {new Integer("700"), new Integer("525"), new Integer("350"), new Integer("175")};
	
	public static class Document {
		int numOrder;
		String label;
		String sbiObjLabel;
		String style;
		String namePar;
		String sbiParName;
		String type;
		String defaultValue;
		Properties params;
		//Properties refreshDocLinked;
		
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

		public Properties getParams() {
			return params;
		}
		public void setParams(Properties params) {
			this.params = params;
		}
		public String getStyle() {
			return style;
		}
		public void setStyle(String style) {
			this.style = style;
		}
		public int getNumOrder() {
			return numOrder;
		}
		public void setNumOrder(int numOrder) {
			this.numOrder = numOrder;
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
		List styleList;
		SourceBean styleSB;
		SourceBean documentSB;
		SourceBean refreshSB;
		SourceBean dimensionSB;
		SourceBean parametersSB;
		SourceBean paramSB;
		SourceBean refreshDocLinkedSB;
		
		
		documentList = documentsConfigurationSB.getAttributeAsList("DOCUMENT");
		//loop on documents
		for(int i = 0; i < documentList.size(); i++) {
			
			documentSB = (SourceBean)documentList.get(i);
			document = new Document();	
			//set the number that identify the document within of hash table
			document.setNumOrder(i);
			attributeValue = (String)documentSB.getAttribute("label");
			document.setLabel(attributeValue);			
			attributeValue = (String)documentSB.getAttribute("sbi_obj_label");
			document.setSbiObjLabel(attributeValue);
			
			dimensionSB = (SourceBean)documentSB.getAttribute("STYLE");			
			attributeValue = (String)dimensionSB.getAttribute("style");
			//attributeValue = (String)dimensionSB.getAttribute("class");
			document.setStyle(attributeValue);			
			
			parametersSB = (SourceBean)documentSB.getAttribute("PARAMETERS");	
			paramList = parametersSB.getAttributeAsList("PARAMETER");
			Properties param = new Properties();
			//loop on parameters of single document
			for(int j = 0; j < paramList.size(); j++) {
				paramSB = (SourceBean)paramList.get(j);
				String label = (paramSB.getAttribute("label")==null)?"":(String)paramSB.getAttribute("label");
				param.setProperty("label_param_"+i+"_"+j, label);
				String sbiParLabel = (paramSB.getAttribute("sbi_par_label")==null)?"":(String)paramSB.getAttribute("sbi_par_label");
				param.setProperty("sbi_par_label_param_"+i+"_"+j, sbiParLabel);
				String typePar = (paramSB.getAttribute("type")==null)?"":(String)paramSB.getAttribute("type");
				param.setProperty("type_par_"+i+"_"+j, typePar);
				String defaultValuePar = (paramSB.getAttribute("default_value")==null)?"":(String)paramSB.getAttribute("default_value");
				param.setProperty("default_value_param_"+i+"_"+j, defaultValuePar);
				
				refreshSB = (SourceBean)paramSB.getAttribute("REFRESH");				
				refreshDocList = refreshSB.getAttributeAsList("REFRESH_DOC_LINKED");
				Properties paramRefreshLinked = new Properties();
				//loop on document linked to single parameter
				for(int k = 0; k < refreshDocList.size(); k++) {
					refreshDocLinkedSB = (SourceBean)refreshDocList.get(k);
					String labelDoc = (refreshDocLinkedSB.getAttribute("labelDoc")==null)?"":(String)refreshDocLinkedSB.getAttribute("labelDoc");
					paramRefreshLinked.setProperty("refresh_doc_linked", labelDoc);
					String labelPar = (refreshDocLinkedSB.getAttribute("labelParam")==null)?"":(String)refreshDocLinkedSB.getAttribute("labelParam");
					paramRefreshLinked.setProperty("refresh_par_linked", labelPar);
					String defaultValueLinked = (paramSB.getAttribute("default_value")==null)?"":(String)paramSB.getAttribute("default_value");
					paramRefreshLinked.setProperty("default_value_linked", defaultValueLinked);
					param.setProperty("param_linked_"+i+"_"+j+"_"+k, paramRefreshLinked.toString());
					
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
			int numDocAdded = 0;
			while (numDocAdded < arrDocs.length){
				for(int i=0; i < arrDocs.length; i++){
					Document tmpDoc =(Document) arrDocs[i];
					if (tmpDoc.getNumOrder() == numDocAdded){
						retLabels.add(tmpDoc.getLabel());
						numDocAdded ++;
					}
				}
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
	/*
	public List getParametersForDocument(String docLabel) {
		Collection collDocs = documentsMap.values();
		List retParams = new ArrayList();
		Object[] arrPars = (Object[])collDocs.toArray();
		try{
			for(int i=0; i < arrPars.length; i++){
				Document tmpDoc =(Document) arrPars[i];
				if (tmpDoc.getLabel().equalsIgnoreCase(docLabel))
					retParams.add(tmpDoc.getParams());
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return retParams;

	}
	*/
	
	public HashMap getParametersForDocument(String docLabel) {
		Collection collDocs = documentsMap.values();
		HashMap retParams = new HashMap();
		Object[] arrPars = (Object[])collDocs.toArray();
		try{
			int cont = 0;
			while (cont < arrPars.length){
				//loop on documents
				for(int i=0; i < arrPars.length; i++){
					int totParsLinked = 0;
					Document tmpDoc =(Document) arrPars[i];
					if (tmpDoc.getNumOrder() == cont){
						if (tmpDoc.getLabel().equalsIgnoreCase(docLabel)){
							Properties prop = (Properties)tmpDoc.getParams();
							Enumeration enum =  prop.keys();
							//loop on parameters of single document
							while (enum.hasMoreElements() ){
								String key = (String)enum.nextElement();
								retParams.put(key, (String)prop.get(key));
								if (key.startsWith("param_linked_"+(tmpDoc.getNumOrder())))
									totParsLinked ++;
							}
							retParams.put("num_doc_linked_"+(tmpDoc.getNumOrder()), Integer.valueOf(totParsLinked));
						}
						cont ++;
					}
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return retParams;

	}
	
	/**
	 * Returns a list with all labels of document linked at the document in input
	 * @param docLabel label of document principal
	 * @return a list with all document linked to the principal document
	 * 
	 */
	/*
	public List getDocumentLinked(String docLabel) {
		Collection collDocs = documentsMap.values();
		List retDocs = new ArrayList();
		Object[] arrPars = (Object[])collDocs.toArray();
		try{
			//gets all document linked with any pararmeters
			List paramsDoc = getParametersForDocument(docLabel);
			List tmpKeyLinked = new ArrayList();
			for (int i=0; i< paramsDoc.size(); i++){
				Properties tmpParam = (Properties)paramsDoc.get(i);
				Enumeration enum =  tmpParam.keys();
				while (enum.hasMoreElements() ){
					String key = (String)enum.nextElement();
					if (key.startsWith("param_linked_")){
						//Properties tmpDocRefresh = (Properties)tmpParam.get(key);
						String tmpDocRefresh = (String)tmpParam.get(key);
						String[] tmpRefresh = null;
						if (tmpDocRefresh != null){
							tmpRefresh = tmpDocRefresh.split(",");
						}
						for (int k=0; k < tmpRefresh.length; k++){
							String tmpValue = tmpRefresh[k].replace("{", "");
							tmpValue = tmpValue.replace("}", "");
							if (tmpValue.trim().startsWith("refresh_doc_linked")){
								retDocs.add(tmpValue.substring(tmpValue.indexOf("=")+1));
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println(e);
		}
		return retDocs;

	}
	*/
	
	public HashMap getInfoDocumentLinked(String docLabel) {
		String strParLinked = "";
		HashMap retDocs = new HashMap();
		Document tmpDoc = getDocument(docLabel);
		int numDoc = 0;
		
		if (tmpDoc != null){
			numDoc = tmpDoc.getNumOrder();
			//gets layout informations (width and height) for next settings of ext-panels
			String docStyles = tmpDoc.getStyle();
			if (docStyles != null){
				String[] propValues = docStyles.split(";");
				strParLinked += "STYLE_"  + docLabel + "=";
				for (int i=0; i<propValues.length; i++){
					String key = propValues[i].substring(0, propValues[i].indexOf(":"));
					String value = propValues[i].substring(propValues[i].indexOf(":")+1);
					if (key.equalsIgnoreCase("WIDTH") || key.equalsIgnoreCase("HEIGHT")){
						if (value.endsWith("%")){
							//if the value is defined in percentage, converts it in pixel value
							for (int j=0; j<percentageValues.length; j++){
								int diff = Integer.valueOf(value.substring(0, value.length()-1)).compareTo(percentageValues[j]);
								if (diff == 0){
									if (key.equalsIgnoreCase("WIDTH"))
										value = widthPxValues[j].toString();
									else if (key.equalsIgnoreCase("HEIGHT"))
										value = heightPxValues[j].toString();
									break;
								}
								else if (diff > 0 && j > 0){
									if (key.equalsIgnoreCase("WIDTH"))
										value = widthPxValues[j-1].toString();
									else if (key.equalsIgnoreCase("HEIGHT"))
										value = heightPxValues[j-1].toString();
									break;
								}
							}
						}
						else if (value.endsWith("px"))
							value = value.substring(0, value.length()-2);
						
						strParLinked += key.toUpperCase() + "_" + value + "|";
					}
				}
				strParLinked = strParLinked.substring(0, strParLinked.length()-1);
				strParLinked += ",";
			
			}
				
			try{
				HashMap paramsDoc = getParametersForDocument(docLabel);
				//loop on parameters of document
				int contOutPar = 0;
				for (int i=0; i< paramsDoc.size(); i++){
					String typePar = (paramsDoc.get("type_par_"+(numDoc)+"_"+i)==null)?"":(String)paramsDoc.get("type_par_"+(numDoc)+"_"+i);
					if (typePar != null && typePar.equalsIgnoreCase("OUT")){
						strParLinked += "SBI_LABEL_PAR_MASTER_"+(numDoc)+"_"+contOutPar+"="+(String)paramsDoc.get("sbi_par_label_param_"+(numDoc)+"_"+i) +",";
					
						Integer numDocLinked = (paramsDoc.get("num_doc_linked_"+(numDoc))==null)?new Integer(0):(Integer)paramsDoc.get("num_doc_linked_"+(numDoc));
						strParLinked += "NUM_DOC_LINKED_"+(numDoc)+"_"+contOutPar+"=" + numDocLinked + ",";
						//loop on document linked to parameter
						for (int j=0; j<numDocLinked.intValue(); j++){
							String paramLinked = (paramsDoc.get("param_linked_"+(numDoc)+"_"+i+"_"+j)==null)?"":(String)paramsDoc.get("param_linked_"+(numDoc)+"_"+i+"_"+j);
							String[] params = paramLinked.split(",");
							strParLinked += "NUM_PARAMS_LINKED_"+(numDoc)+"_"+i+"=" + params.length + ",";
							//loop on parameters of document linked
							for (int k=0; k<params.length; k++) {
								String labelDocLinked = params[k];
								labelDocLinked = labelDocLinked.replace("{", "");
								labelDocLinked = labelDocLinked.replace("}", "");
								if (labelDocLinked.trim().startsWith("refresh_doc_linked")){
									//get document linked 
									Document linkedDoc = getDocument(labelDocLinked.substring(labelDocLinked.indexOf("=")+1));
									
									strParLinked += "LABEL_DOC_"+(j)+"=" + linkedDoc.getLabel() + ",";
									strParLinked += "SBI_LABEL_DOC_"+(j)+"=" + linkedDoc.getSbiObjLabel() + "|"+ linkedDoc.getLabel() + ",";
									
									HashMap paramsDocLinked = getParametersForDocument(labelDocLinked.substring(labelDocLinked.indexOf("=")+1));
									int numLinked = linkedDoc.getNumOrder();
									for (int x=0; x< paramsDocLinked.size(); x++){
										String sbiLabelPar = (paramsDocLinked.get("sbi_par_label_param_"+numLinked+"_"+x)==null)?"":(String)paramsDocLinked.get("sbi_par_label_param_"+(numLinked)+"_"+x);
										String labelPar = (paramsDocLinked.get("label_param_"+numLinked+"_"+x)==null)?"":(String)paramsDocLinked.get("label_param_"+(numLinked)+"_"+x);
										if (sbiLabelPar != null && !sbiLabelPar.equals(""))
											strParLinked += "SBI_LABEL_PAR_"+(j)+"="+ sbiLabelPar +"|"+labelPar+",";
									}
									
								}
								if (labelDocLinked.trim().startsWith("refresh_par_linked")){
									strParLinked += "LABEL_PAR_LINKED_"+(j)+"="+labelDocLinked.substring(labelDocLinked.indexOf("=")+1) +",";
								}
							}
						}
						contOutPar ++;
					}
				}
				
			}catch(Exception e){
				System.out.println(e);
			}
		}
		if(strParLinked.endsWith(","))
			strParLinked = strParLinked.substring(0, strParLinked.length()-1);
		if (!strParLinked.equals(""))
			retDocs = getMapFromString(strParLinked);
		
		return retDocs;

	}
	/**
	 * Return a list with all labels of the fields that are linked to the principal document 
	 * @param docLabel the label of principal document
	 * @param docField the label of the principal field 
	 * @return a list with all field labels linked to the document
	 */
	/*
	public List getFieldsLinked(String docLabel, String docField) {
		Collection collDocs = documentsMap.values();
		List retFields = new ArrayList();
		Object[] arrPars = (Object[])collDocs.toArray();
		try{
			//gets all document linked with any pararmeters
			List paramsDoc = getParametersForDocument(docLabel);
			List tmpKeyLinked = new ArrayList();
			for (int i=0; i< paramsDoc.size(); i++){
				Properties tmpParam = (Properties)paramsDoc.get(i);
				Enumeration enum =  tmpParam.keys();
				while (enum.hasMoreElements() ){
					String key = (String)enum.nextElement();
					if (key.startsWith("param_linked_")){
						//Properties tmpDocRefresh = (Properties)tmpParam.get(key);
						String tmpDocRefresh = (String)tmpParam.get(key);
						String[] tmpRefresh = null;
						if (tmpDocRefresh != null){
							tmpRefresh = tmpDocRefresh.split(",");
						}
						boolean isCorrectDoc = false;
						for (int k=0; k < tmpRefresh.length; k++){
							String tmpValue = tmpRefresh[k].replace("{", "");
							tmpValue = tmpValue.replace("}", "");
							if (tmpValue.startsWith("refresh_doc_linked_")){
								String tmpDocLabel = tmpValue.substring(tmpValue.indexOf("=")+1);
								if (tmpDocLabel.equalsIgnoreCase(docLabel)){
									isCorrectDoc = true;
								}
								if (isCorrectDoc && tmpValue.startsWith("refresh_par_linked_")){
									retFields.add(tmpValue.substring(tmpValue.indexOf("=")+1));
								}
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println(e);
		}
		return retFields;
	}
	*/
	public List getDocumentsArray() {
		Collection collDocs = documentsMap.values();
		List retDocs = new ArrayList();
		Object[] arrDocs = (Object[])collDocs.toArray();
		try{
			int numDocAdded = 0;
			while (numDocAdded < arrDocs.length){
				for(int i=0; i < arrDocs.length; i++){
					Document tmpDoc =(Document) arrDocs[i];
					if (tmpDoc.getNumOrder() == numDocAdded){
						retDocs.add(tmpDoc);
						numDocAdded ++;
					}
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return retDocs;

	}
	
	private HashMap getMapFromString(String strToConvert){
		HashMap retHash = new HashMap();
		String[] tmpStr = strToConvert.split(",");
		for (int i=0; i < tmpStr.length; i++){
			String key = tmpStr[i].substring(0, tmpStr[i].indexOf("="));
			String value = tmpStr[i].substring(tmpStr[i].indexOf("=")+1);
			retHash.put(key, value);
		}
		return retHash;
	}

}