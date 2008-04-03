package it.eng.spagobi.engines.chart.bo.charttypes.barcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyCategoryUrlGenerator;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

public class LinkableBar extends BarCharts {

	String rootUrl=null;
	String mode="";
	String drillLabel="";
	HashMap drillParameter=null;
	String categoryUrlName="";
	String serieUrlname="";


	private static transient Logger logger=Logger.getLogger(LinkableBar.class);
	
	public Dataset calculateValue() throws SourceBeanException {
		return super.calculateValue();		
	}


	public void configureChart(SourceBean content) {
		// TODO Auto-generated method stub
		super.configureChart(content);
		SourceBean drillSB = (SourceBean)content.getAttribute("CONF.DRILL");
		if(drillSB!=null){
			String lab=(String)drillSB.getAttribute("document");
			if(lab!=null) drillLabel=lab;
			else{
				logger.error("Drill label not found");
			}
			
			List parameters =drillSB.getAttributeAsList("PARAM");
			if(parameters!=null){
			drillParameter=new HashMap();	
			
			for (Iterator iterator = parameters.iterator(); iterator.hasNext();) {
				SourceBean att = (SourceBean) iterator.next();
				String name=(String)att.getAttribute("name");
				String value=(String)att.getAttribute("value");
				if(name.equalsIgnoreCase("categoryurlname")){
					categoryUrlName=value;
				}
				else if(name.equalsIgnoreCase("seriesurlname")){
					serieUrlname=value;
				}
				else{
				drillParameter.put(name, value);
				}
				}
			}
			}
		}
		
		
		
	


	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		super.createChart(chartTitle, dataset);

		CategoryAxis categoryAxis = new CategoryAxis(categoryLabel);
		ValueAxis valueAxis = new NumberAxis(valueLabel);
		org.jfree.chart.renderer.category.BarRenderer renderer = new org.jfree.chart.renderer.category.BarRenderer();

		renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());

		boolean document_composition=false;
		if(mode.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION))document_composition=true;
			
		
		MyCategoryUrlGenerator mycatUrl=new MyCategoryUrlGenerator(rootUrl);
		mycatUrl.setDocument_composition(document_composition);
		mycatUrl.setCategoryUrlLabel(categoryUrlName);
			mycatUrl.setSerieUrlLabel(serieUrlname);
			
			renderer.setItemURLGenerator(mycatUrl);
			
/*		}
		else{
			renderer.setItemURLGenerator(new StandardCategoryURLGenerator(rootUrl));
		}*/

		CategoryPlot plot = new CategoryPlot((CategoryDataset)dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);



		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(color);

		// get a reference to the plot for further customisation...
		//CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		//BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		
		if(currentSerie!=-1 && colorMap!=null){
			Integer c=new Integer(currentSerie);
			if(colorMap.get("color"+c.toString())!=null){
				Color col= (Color)colorMap.get("color"+c);
				renderer.setSeriesPaint(0, col);
			}
		}
		else{
			if(colorMap!=null){

				for (Iterator iterator = colorMap.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					Color col= (Color)colorMap.get(key);
					String keyNum=key.substring(5, key.length());
					int num=Integer.valueOf(keyNum).intValue();
					num=num-1;
					renderer.setSeriesPaint(num, col);
				}
			}
		}
		
		// set up gradient paints for series...
		/*if(colorMap!=null){

			for (Iterator iterator = colorMap.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				Color col= (Color)colorMap.get(key);
				renderer.setSeriesPaint((Integer.valueOf(key).intValue())-1, col);
			}
		}*/

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(
						Math.PI / 6.0));

		return chart;

	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public boolean isLinkable(){
		return true;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getDrillLabel() {
		return drillLabel;
	}


	public void setDrillLabel(String drillLabel) {
		this.drillLabel = drillLabel;
	}


	public HashMap getDrillParameter() {
		return drillParameter;
	}


	public void setDrillParameter(HashMap drillParameter) {
		this.drillParameter = drillParameter;
	}


	public String getCategoryUrlName() {
		return categoryUrlName;
	}


	public void setCategoryUrlName(String categoryUrlName) {
		this.categoryUrlName = categoryUrlName;
	}


	public String getSerieUrlname() {
		return serieUrlname;
	}


	public void setSerieUrlname(String serieUrlname) {
		this.serieUrlname = serieUrlname;
	}

	public String getDocument_Parameters(HashMap drillParameters) {
		String document_parameter="";
		for (Iterator iterator = drillParameters.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			String value=(String)drillParameters.get(name);
			if(name!=null && !name.equals("") && value!=null && !value.equals("")){
				//document_parameter+="%26"+name+"%3D"+value;
				document_parameter+="&"+name+"="+value;
			}
			
		}
		return document_parameter;
	}
	
}
