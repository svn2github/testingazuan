package it.eng.spagobi.studio.core.util;

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
	
}
