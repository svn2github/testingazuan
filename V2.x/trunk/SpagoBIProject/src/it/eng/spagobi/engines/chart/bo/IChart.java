package it.eng.spagobi.engines.chart.bo;


import it.eng.spago.base.SourceBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

public interface IChart {


	public JFreeChart createChart(String chartTitle, Dataset dataset);

	public void configureChart(SourceBean content);

	public Dataset calculateValue() throws Exception;

	
	
	
	
	public Dataset filterDataset(Dataset dataset, HashMap categories, int catSelected, int numberCatsVisualization); 	
	public String getName();
	public void setName(String name);
	public int getWidth();
	public void setWidth(int width);
	public int getHeight();
	public void setHeight(int height);
	public String getData();
	public void setData(String data);
	public boolean isChangeableView();
	public void setChangeViewChecked(boolean b);
	public boolean isLinkable();
	public List getPossibleChangePars();
	public void setChangeViewsParameter(String changePar, boolean how);
	public boolean getChangeViewParameter(String changePar);
	public String getChangeViewParameterLabel(String changePar, int i);	
	public boolean isLegend();
	public void setLegend(boolean legend);
	public Map getParametersObject();
	public void setParametersObject(Map paramsObject);
	
		
}