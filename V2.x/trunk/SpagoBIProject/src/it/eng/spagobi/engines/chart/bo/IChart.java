package it.eng.spagobi.engines.chart.bo;


import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

public interface IChart {


	public JFreeChart createChart(String chartTitle, Dataset dataset);


	public void configureChart(SourceBean content);

	public String getName();

	public void setName(String name);



	public int getWidth();


	public Dataset calculateValue() throws SourceBeanException;

	public void setWidth(int width);




	public int getHeight();


	public void setHeight(int height);


	public String getDataLov();


	public void setDataLov(String dataLov);

	public boolean isChangeableView();
	public void setChangeViewChecked(boolean b);
	
	public boolean isLinkable();

	public List getPossibleChangePars();
	
	public void setChangeViewsParameter(String changePar, boolean how);
	public boolean getChangeViewParameter(String changePar);
	public String getChangeViewParameterLabel(String changePar, int i);
			
	
}