package it.eng.spagobi.engines.chart;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

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

}