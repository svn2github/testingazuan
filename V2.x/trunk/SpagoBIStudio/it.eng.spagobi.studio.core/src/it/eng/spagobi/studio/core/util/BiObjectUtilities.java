package it.eng.spagobi.studio.core.util;

import java.util.ArrayList;
import java.util.Iterator;

import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;
import it.eng.spagobi.studio.core.properties.PropertyPage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

public class BiObjectUtilities {


	/**
	 *   TYPE => EXTENSION
	 */


	public final static String DASHBOARD_ENGINE_EXTENSION="sbidash";
	public final static String CHART_ENGINE_EXTENSION="sbichart";
	public final static String JASPER_REPORT_ENGINE_EXTENSION="jrxml";
	public final static String BIRT_REPORT_ENGINE_EXTENSION="rptdesign";



	public static String getTypeFromExtension(String fileName){

		int indexPoint=fileName.indexOf('.');
		if(indexPoint==-1) return null;

		String extension=fileName.substring(indexPoint+1);
		if(extension.equalsIgnoreCase(DASHBOARD_ENGINE_EXTENSION)){
			return "DASH";
		}
		else if(extension.equalsIgnoreCase(CHART_ENGINE_EXTENSION)){
			return "DASH";
		}
		else if(extension.equalsIgnoreCase(JASPER_REPORT_ENGINE_EXTENSION)){
			return "REPORT";
		}
		else if(extension.equalsIgnoreCase("xml")){
			return "REPORT";
		}
		else if(extension.equalsIgnoreCase(BIRT_REPORT_ENGINE_EXTENSION)){
			return "REPORT";
		}
		else return null;


	}

	public static IFile setFileMetaData(IFile newFile, SDKDocument document) throws CoreException{
		System.out.println("ARRIVO QUI");
		if(document.getId()!=null){
			newFile.setPersistentProperty(PropertyPage.DOCUMENT_ID, document.getId().toString());			
		}
		if(document.getLabel()!=null){
			newFile.setPersistentProperty(PropertyPage.DOCUMENT_LABEL, document.getLabel());
		}
		if(document.getName()!=null){
			newFile.setPersistentProperty(PropertyPage.DOCUMENT_NAME, document.getName());
		}
		if(document.getDescription()!=null){
			newFile.setPersistentProperty(PropertyPage.DOCUMENT_DESCRIPTION, document.getDescription());
		}
		if(document.getState()!=null){
			newFile.setPersistentProperty(PropertyPage.DOCUMENT_STATE, document.getState());
		}
		if(document.getType()!=null){
			newFile.setPersistentProperty(PropertyPage.DOCUMENT_TYPE, document.getType());
		}
		if(document.getDataSourceId()!=null){
			newFile.setPersistentProperty(PropertyPage.DATA_SOURCE_ID, (document.getDataSourceId()!=null?document.getDataSourceId().toString(): ""));
		}
		if(document.getDataSetId()!=null){
			newFile.setPersistentProperty(PropertyPage.DATASET_ID, (document.getDataSetId()!=null?document.getDataSetId().toString(): ""));
		}
		if(document.getEngineId()!=null){
			newFile.setPersistentProperty(PropertyPage.ENGINE_ID, (document.getEngineId()!=null?document.getEngineId().toString(): ""));
		}
		return newFile;
	}


	public static IFile setFileParametersMetaData(IFile newFile, SDKDocumentParameter[] parameters) throws CoreException{
		String xml="";
		ArrayList<SDKDocumentParameter> list=new ArrayList<SDKDocumentParameter>();
		for (int i = 0; i < parameters.length; i++) {
			list.add(parameters[i]);
		}

		XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("_", "_");
		XStream xstream = new XStream(new DomDriver("UTF-8", replacer)); 
		SDKDocumentParameters pars=new SDKDocumentParameters(list);
		xstream.alias("SDK_DOCUMENT_PARAMETERS", SDKDocumentParameters.class);
		xstream.alias("PARAMETER", SDKDocumentParameter.class);
		xstream.useAttributeFor(SDKDocumentParameter.class, "id");
		xstream.useAttributeFor(SDKDocumentParameter.class, "label");
		xstream.useAttributeFor(SDKDocumentParameter.class, "type");
		xstream.useAttributeFor(SDKDocumentParameter.class, "urlName");
		xstream.omitField(SDKDocumentParameter.class, "values");		
		xstream.omitField(SDKDocumentParameter.class, "constraints");
		xstream.omitField(SDKDocumentParameter.class, "__hashCodeCalc");

		//        xstream.addImplicitCollection(SDKDocumentParameter[].class, "PARAMETERS", "PARAMETER", SDKDocument.class);
		//		for (int i = 0; i < parameters.length; i++) {
		//			SDKDocumentParameter sdkDocumentParameter = parameters[i];
		//			xstream.alias("PARAMETER", SDKDocumentParameter.class);
		//			xstream.useAttributeFor(SDKDocumentParameter.class, "id");
		//			xstream.useAttributeFor(SDKDocumentParameter.class, "name");
		//			xstream.useAttributeFor(SDKDocumentParameter.class, "type");
		//			xstream.useAttributeFor(SDKDocumentParameter.class, "urlname");
		//			xstream.useAttributeFor(SDKDocumentParameter.class, "typedesc");
		//	        xstream.omitField(SDKDocumentParameter.class, "values");		
		//		}

		xml = xstream.toXML(pars);		
		newFile.setPersistentProperty(PropertyPage.DOCUMENT_PARAMETERS_XML,xml);
		return newFile;
	}


//	public static IFile setFileParametersMetaDataBackup(IFile newFile, SDKDocumentParameter[] parameters) throws CoreException{
//		String xml="<?xml version='1.0' encoding='iso-8859-1'?>";
//		xml+="<PARAMETERS>";
//		for (int i = 0; i < parameters.length; i++) {
//			SDKDocumentParameter sdkDocumentParameter = parameters[i];
//			xml+="<PARAMETER";
//			xml+=" id="+sdkDocumentParameter.getId();
//			xml+=" label="+sdkDocumentParameter.getLabel();
//			xml+=" type="+sdkDocumentParameter.getType();
//			xml+=" urlname="+sdkDocumentParameter.getUrlName();
//			xml+=" typedesc="+sdkDocumentParameter.getTypeDesc();
//			xml+="+/>";
//		}
//
//		xml+="</PARAMETERS>";
//		newFile.setPersistentProperty(PropertyPage.DOCUMENT_PARAMETERS_XML,xml);
//		return newFile;
//
//	}

}
