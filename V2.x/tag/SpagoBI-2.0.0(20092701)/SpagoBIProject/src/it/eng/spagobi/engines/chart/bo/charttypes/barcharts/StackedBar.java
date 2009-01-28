/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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


package it.eng.spagobi.engines.chart.bo.charttypes.barcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyCategoryUrlGenerator;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyStandardCategoryItemLabelGenerator;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.chart.utils.StyleLabel;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

/**   @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */


public class StackedBar extends BarCharts implements ILinkableChart {

	String rootUrl=null;
	String mode="";
	String drillLabel="";
	HashMap drillParameter=null;
	String categoryUrlName="";
	String serieUrlname="";

	boolean cumulative=false;
	HashMap colorMap=null;  // keeps user selected colors
	boolean additionalLabels=false;
	boolean percentageValue=false;
	boolean makePercentage=false;
	HashMap catSerLabels=null;


	private static transient Logger logger=Logger.getLogger(StackedBar.class);





	/**
	 * Override this functions from BarCharts beacuse I want the hidden serie to be the first!
	 * 
	 * @return the dataset
	 * 
	 * @throws Exception the exception
	 */

	public DatasetMap calculateValue() throws Exception {
		logger.debug("IN");
		String res=DataSetAccessFunctions.getDataSetResultFromId(profile, getData(),parametersObject);
		categories=new HashMap();

		double cumulativeValue=0.0;

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");


		// run all categories (one for each row)
		categoriesNumber=0;
		seriesNames=new Vector();
		catGroupNames=new Vector();
		if(filterCatGroups==true){
			catGroups=new HashMap();
		}
		
		//categories.put(new Integer(0), "All Categories");
		boolean first=true;
		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
			SourceBean category = (SourceBean) iterator.next();
			List atts=category.getContainedAttributes();

			HashMap series=new HashMap();
			HashMap additionalValues=new HashMap();
			String catValue="";
			String cat_group_name="";

			String nameP="";
			String value="";

			ArrayList orderSeries=new ArrayList();

			if(first){
				if (name.indexOf("$F{") >= 0){
					setTitleParameter(atts);
				}
				first=false;
			}


			//run all the attributes, to define series!
			int contSer = 0;
			for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

				nameP=new String(object.getKey());
				value=new String((String)object.getValue());
				if(nameP.equalsIgnoreCase("x"))
				{
					catValue=value;
					categoriesNumber=categoriesNumber+1;
					categories.put(new Integer(categoriesNumber),value);

				}
				else if(nameP.equalsIgnoreCase("cat_group")){
					cat_group_name=value;
				}
				else {
					nameP = nameP.toUpperCase();
					if(nameP.startsWith("ADD_")){
						if(additionalLabels){
							String ind=nameP.substring(4);							
							additionalValues.put(ind, value);
						}
					}
					else if (this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization()){
						series.put(nameP, value);
						orderSeries.add(nameP);
						contSer++;
					}
					else if (this.getNumberSerVisualization() == 0){
						series.put(nameP, value);
						orderSeries.add(nameP);
					}

					// for now I make like if addition value is checked he seek for an attribute with name with value+name_serie
				}
			}
			
			// if a category group was found add it
			if(!cat_group_name.equalsIgnoreCase("") && !catValue.equalsIgnoreCase("") && catGroups!=null)
			{	
				catGroups.put(catValue, cat_group_name);
				if(!(catGroupNames.contains(cat_group_name))){
				catGroupNames.add(cat_group_name);}
			}
			

			// if it is cumulative automatically get the vamount value
			if(cumulative){
				dataset.addValue(cumulativeValue, "CUMULATIVE", catValue);
			}

			// if there is an hidden serie put that one first!!! if it is not cumulative
			/*if(serieHidden!=null && !this.cumulative && !serieHidden.equalsIgnoreCase("")){
				String valueS=(String)series.get(serieHidden);
				dataset.addValue(Double.valueOf(valueS).doubleValue(), serieHidden, catValue);
				if(!seriesNames.contains(serieHidden)){
					seriesNames.add(serieHidden);
				}				
			}*/


			for (Iterator iterator3 = orderSeries.iterator(); iterator3.hasNext();) {
				String nameS = (String) iterator3.next();
				if(!hiddenSeries.contains(nameS)){
					String valueS=((String)series.get(nameS)).equalsIgnoreCase("null")?"0":(String)series.get(nameS);
					dataset.addValue(Double.valueOf(valueS).doubleValue(), nameS, catValue);
					cumulativeValue+=Double.valueOf(valueS).doubleValue();
					if(!seriesNames.contains(nameS)){
						seriesNames.add(nameS);
					}
					// if there is an additional label are 
					if(additionalValues.get(nameS)!=null){
						String val=(String)additionalValues.get(nameS);
						String index=catValue+"-"+nameS;						
						//String totalVal = valueS;
						String totalVal=val;						
						//if (percentageValue) totalVal += "%";
						//totalVal += "\n" + val;
						catSerLabels.put(index, totalVal);
					}

				}

			}
			// Check additional Values for CUmulative
			if(additionalValues.get("CUMULATIVE")!=null){
				String val=(String)additionalValues.get("CUMULATIVE");
				String index=catValue+"-"+"CUMULATIVE";						
				catSerLabels.put(index, val);	
			}



		}
		logger.debug("OUT");
		DatasetMap datasets=new DatasetMap();
		datasets.addDataset("1",dataset);
		return datasets;
	}




	public void configureChart(SourceBean content) {
		logger.debug("IN");
		super.configureChart(content);


		if(confParameters.get("cumulative")!=null){	
			String orientation=(String)confParameters.get("cumulative");
			if(orientation.equalsIgnoreCase("true")){
				cumulative=true;
			}
			else {
				cumulative=false;
			}
		}

		if(confParameters.get("add_labels")!=null){	
			String additional=(String)confParameters.get("add_labels");
			if(additional.equalsIgnoreCase("true")){
				additionalLabels=true;
				catSerLabels=new HashMap();
			}
			else additionalLabels=false;
		}
		else
		{
			additionalLabels=false;
		}

		if(confParameters.get("make_percentage")!=null){	
			String perc=(String)confParameters.get("make_percentage");
			if(perc.equalsIgnoreCase("true")){
				makePercentage=true;
			}
			else makePercentage=false;
		}
		else
		{
			makePercentage=false;
		}
		
		if(confParameters.get("percentage_value")!=null){	
			String perc=(String)confParameters.get("percentage_value");
			if(perc.equalsIgnoreCase("true")){
				percentageValue=true;
			}
			else percentageValue=false;
		}
		else
		{
			percentageValue=false;
		}

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
					String type=(String)att.getAttribute("type");
					String value=(String)att.getAttribute("value");

					if(type!=null && type.equalsIgnoreCase("RELATIVE")){ // Case relative
						if(value.equalsIgnoreCase("serie"))serieUrlname=name;
						if(value.equalsIgnoreCase("category"))categoryUrlName=name;
					}
					else{												// Case absolute
						drillParameter.put(name, value);
					}
				}
			}
		}
		//reading series colors if present
		SourceBean colors = (SourceBean)content.getAttribute("CONF.SERIES_COLORS");
		if(colors!=null){
			colorMap=new HashMap();
			List atts=colors.getContainedAttributes();
			String colorSerie="";
			for (Iterator iterator = atts.iterator(); iterator.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator.next();

				String serieName=new String(object.getKey());
				colorSerie=new String((String)object.getValue());
				Color col=new Color(Integer.decode(colorSerie).intValue());
				if(col!=null){
					colorMap.put(serieName,col); 
				}
			}		

		}
		logger.debug("OUT");	
	}




	/**
	 * Inherited by IChart.
	 * 
	 * @param chartTitle the chart title
	 * @param dataset the dataset
	 * 
	 * @return the j free chart
	 */



	public JFreeChart createChart(DatasetMap datasets) {



		logger.debug("IN");
		CategoryDataset dataset=(CategoryDataset)datasets.getDatasets().get("1");

		logger.debug("Taken Dataset");

		logger.debug("Call Chart Creation");
		JFreeChart chart = ChartFactory.createStackedBarChart(
				name,  // chart title
				categoryLabel,                  // domain axis label
				valueLabel,                     // range axis label
				dataset,                     // data
				PlotOrientation.VERTICAL,    // the plot orientation
				false,                        // legend
				true,                        // tooltips
				false                        // urls
		);
		logger.debug("Chart Created");

		chart.setBackgroundPaint(Color.white);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(color);
		plot.setRangeGridlinePaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);


		logger.debug("set renderer");
		StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setBaseItemLabelsVisible(true);
		if (percentageValue)
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("#,##.#%")));
		else if(makePercentage)
		       renderer.setRenderAsPercentages(true);
		else
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		
		renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());

		boolean document_composition=false;
		if(mode.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION))document_composition=true;

		logger.debug("Calling Url Generation");

		MyCategoryUrlGenerator mycatUrl=null;
		if(rootUrl!=null){
			logger.debug("Set MycatUrl");
			mycatUrl=new MyCategoryUrlGenerator(rootUrl);

			mycatUrl.setDocument_composition(document_composition);
			mycatUrl.setCategoryUrlLabel(categoryUrlName);
			mycatUrl.setSerieUrlLabel(serieUrlname);
		}
		if(mycatUrl!=null)
			renderer.setItemURLGenerator(mycatUrl);

		logger.debug("Text Title");

		TextTitle title =setStyleTitle(name, styleTitle);
		chart.setTitle(title);
		if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}

		logger.debug("Style Labels");

		Color colorSubInvisibleTitle=Color.decode("#FFFFFF");
		StyleLabel styleSubSubTitle=new StyleLabel("Arial",12,colorSubInvisibleTitle);
		TextTitle subsubTitle =setStyleTitle("c", styleSubSubTitle);
		chart.addSubtitle(subsubTitle);
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(color);


		logger.debug("Axis creation");
		// set the range axis to display integers only...
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		if(makePercentage)
			rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
		else
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		rangeAxis.setLabelFont(new Font(styleXaxesLabels.getFontName(), Font.PLAIN, styleXaxesLabels.getSize()));
		rangeAxis.setLabelPaint(styleXaxesLabels.getColor());
		rangeAxis.setTickLabelFont(new Font(styleXaxesLabels.getFontName(), Font.PLAIN, styleXaxesLabels.getSize()));
		rangeAxis.setTickLabelPaint(styleXaxesLabels.getColor());
		renderer.setDrawBarOutline(false);

		logger.debug("Set series color");

		int seriesN=dataset.getRowCount();
		if(colorMap!=null){
			for (int i = 0; i < seriesN; i++) {
				String serieName=(String)dataset.getRowKey(i);
				Color color=(Color)colorMap.get(serieName);
				if(color!=null){
					renderer.setSeriesPaint(i, color);
					renderer.setSeriesItemLabelFont(i, new Font(styleValueLabels.getFontName(), Font.PLAIN, styleValueLabels.getSize()));
				}	
			}
		}

		logger.debug("If cumulative set series paint "+cumulative);

		if(cumulative){
			int row=dataset.getRowIndex("CUMULATIVE");
			if(row!=-1){
				if(color!=null)
					renderer.setSeriesPaint(row, color);
				else
					renderer.setSeriesPaint(row, Color.WHITE);
			}
		}


		MyStandardCategoryItemLabelGenerator generator=null;
		logger.debug("Are there addition labels "+additionalLabels);
		if(additionalLabels){

			generator = new MyStandardCategoryItemLabelGenerator(catSerLabels,"{1}", NumberFormat.getInstance());
			logger.debug("generator set");

			double orient=(-Math.PI / 2.0);
			logger.debug("add labels style");
			if(styleValueLabels.getOrientation()!= null && styleValueLabels.getOrientation().equalsIgnoreCase("horizontal")){
				orient=0.0;
			}
			renderer.setBaseItemLabelFont(new Font(styleValueLabels.getFontName(), Font.PLAIN, styleValueLabels.getSize()));
			renderer.setBaseItemLabelPaint(styleValueLabels.getColor());

			logger.debug("add labels style set");

			renderer.setBaseItemLabelGenerator(generator);
			renderer.setBaseItemLabelsVisible(true);
			//vertical labels 			
			renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER, TextAnchor.CENTER, 
					orient));
			renderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER, TextAnchor.CENTER, 
					orient));

			logger.debug("end of add labels ");


		}

		logger.debug("domain axis");

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(
						Math.PI / 4.0));
		domainAxis.setLabelFont(new Font(styleYaxesLabels.getFontName(), Font.PLAIN, styleYaxesLabels.getSize()));
        domainAxis.setLabelPaint(styleYaxesLabels.getColor());
        domainAxis.setTickLabelFont(new Font(styleYaxesLabels.getFontName(), Font.PLAIN, styleYaxesLabels.getSize()));
        domainAxis.setTickLabelPaint(styleYaxesLabels.getColor());

		if(legend==true){
			
			drawLegend(chart);}
        
		logger.debug("OUT");
		return chart;

	}



	/**
	 * Gets document parameters and return a string in the form &param1=value1&param2=value2 ...
	 * 
	 * @param drillParameters the drill parameters
	 * 
	 * @return the document_ parameters
	 */

	public String getDocument_Parameters(HashMap drillParameters) {
		String document_parameter="";
		if (drillParameters != null){
			for (Iterator iterator = drillParameters.keySet().iterator(); iterator.hasNext();) {
				String name = (String) iterator.next();
				String value=(String)drillParameters.get(name);
				if(name!=null && !name.equals("") && value!=null && !value.equals("")){
					document_parameter+="%26"+name+"%3D"+value;
					//document_parameter+="&"+name+"="+value;
				}

			}
		}
		return document_parameter;
	}



	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#getRootUrl()
	 */
	public String getRootUrl() {
		return rootUrl;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#setRootUrl(java.lang.String)
	 */
	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#isLinkable()
	 */
	public boolean isLinkable(){
		return true;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#getMode()
	 */
	public String getMode() {
		return mode;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#setMode(java.lang.String)
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#getDrillLabel()
	 */
	public String getDrillLabel() {
		return drillLabel;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#setDrillLabel(java.lang.String)
	 */
	public void setDrillLabel(String drillLabel) {
		this.drillLabel = drillLabel;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#getDrillParameter()
	 */
	public HashMap getDrillParameter() {
		return drillParameter;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#setDrillParameter(java.util.HashMap)
	 */
	public void setDrillParameter(HashMap drillParameter) {
		this.drillParameter = drillParameter;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#getCategoryUrlName()
	 */
	public String getCategoryUrlName() {
		return categoryUrlName;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#setCategoryUrlName(java.lang.String)
	 */
	public void setCategoryUrlName(String categoryUrlName) {
		this.categoryUrlName = categoryUrlName;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#getSerieUrlname()
	 */
	public String getSerieUrlname() {
		return serieUrlname;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart#setSerieUrlname(java.lang.String)
	 */
	public void setSerieUrlname(String serieUrlname) {
		this.serieUrlname = serieUrlname;
	}



}
