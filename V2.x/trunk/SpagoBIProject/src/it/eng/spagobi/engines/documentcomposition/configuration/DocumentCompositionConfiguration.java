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
	
	public static class Document {
		String label;
		String sbiObjLabel;
		String dimensionWidth;
		String dimensionHeight;		
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
		/*
		public Properties getRefreshDocLinked() {
			return refreshDocLinked;
		}
		public void setRefreshDocLinked(Properties refreshDocLinked) {
			this.refreshDocLinked = refreshDocLinked;
		}
		*/
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
			
			parametersSB = (SourceBean)documentSB.getAttribute("PARAMETERS");	
			paramList = parametersSB.getAttributeAsList("PARAMETER");
			Properties param = new Properties();
			for(int j = 0; j < paramList.size(); j++) {
				paramSB = (SourceBean)paramList.get(j);
				String label = (paramSB.getAttribute("label")==null)?"":(String)paramSB.getAttribute("label");
				param.setProperty("label_param_"+j, label);
				String sbiParLabel = (paramSB.getAttribute("sbi_par_label")==null)?"":(String)paramSB.getAttribute("sbi_par_label");
				param.setProperty("sbi_par_label_param_"+j, sbiParLabel);
				String typePar = (paramSB.getAttribute("type")==null)?"":(String)paramSB.getAttribute("type");
				param.setProperty("type_par_"+j, typePar);
				String defaultValuePar = (paramSB.getAttribute("default_value")==null)?"":(String)paramSB.getAttribute("default_value");
				param.setProperty("default_value_param_"+j, defaultValuePar);
				
				refreshSB = (SourceBean)paramSB.getAttribute("REFRESH");				
				refreshDocList = refreshSB.getAttributeAsList("REFRESH_DOC_LINKED");
				Properties paramRefreshLinked = new Properties();
				for(int k = 0; k < refreshDocList.size(); k++) {
					refreshDocLinkedSB = (SourceBean)refreshDocList.get(k);
					String labelDoc = (refreshDocLinkedSB.getAttribute("labelDoc")==null)?"":(String)refreshDocLinkedSB.getAttribute("labelDoc");
					paramRefreshLinked.setProperty("refresh_doc_linked_"+i, labelDoc);
					String labelPar = (refreshDocLinkedSB.getAttribute("labelParam")==null)?"":(String)refreshDocLinkedSB.getAttribute("labelParam");
					paramRefreshLinked.setProperty("refresh_par_linked_"+i, labelPar);
					String defaultValueLinked = (paramSB.getAttribute("default_value")==null)?"":(String)paramSB.getAttribute("default_value");
					paramRefreshLinked.setProperty("default_value_linked_"+k, defaultValueLinked);
					param.setProperty("param_linked_"+j+"_"+k, paramRefreshLinked.toString());
					
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
	
	public Document getDocumentFromObjLabel(String objLabel) {
		Document document = getDocument(objLabel);
		if(document != null) return document;

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
							if (tmpValue.startsWith("refresh_doc_linked_")){
								retDocs.add(tmpValue.substring(tmpValue.indexOf("=")+1));
							}
						}
						System.out.println(tmpDocRefresh);
					}
				}
			}
			
		}catch(Exception e){
			System.out.println(e);
		}
		return retDocs;

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

}