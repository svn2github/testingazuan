package it.eng.spagobi.engines.chart.bo.charttypes;

import java.util.HashMap;

public interface ILinkableChart {

	/**
	 * Gets document parameters and return a string in the form &param1=value1&param2=value2 ... 
	 */
			
	public String getDocument_Parameters(HashMap drillParameters);	
	
	
	public String getRootUrl();
	
	public void setRootUrl(String rootUrl);
	
	public String getMode();
	


	public void setMode(String mode);


	public String getDrillLabel();


	public void setDrillLabel(String drillLabel);

	public HashMap getDrillParameter();

	public void setDrillParameter(HashMap drillParameter);

	public String getCategoryUrlName();

	public void setCategoryUrlName(String categoryUrlName);

	public String getSerieUrlname();

	public void setSerieUrlname(String serieUrlname);

	
	
	
	
	
	
}
