package it.eng.spagobi.engines.chart.bo.charttypes.barcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyCategoryUrlGenerator;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyStandardCategoryItemLabelGenerator;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

public class CombinedCategoryBar extends LinkableBar {


	HashMap seriesDraw=null;
	HashMap seriesScale=null;
	HashMap seriesCaptions=null;
	boolean additionalLabels=false;
	HashMap catSerLabels=null;


	//boolean secondAxis=false; this kind of chart must always have second axis
	String secondAxisLabel=null;

	boolean firstAxisLine=false;
	boolean secondAxisLine=false;

	private static transient Logger logger=Logger.getLogger(CombinedCategoryBar.class);





	public DatasetMap calculateValue() throws Exception {
		logger.debug("IN");

		seriesNames=new Vector();
		seriesCaptions=new LinkedHashMap();

		String res=DataSetAccessFunctions.getDataSetResultFromId(profile, getData(),parametersObject);
		categories=new HashMap();

		DatasetMap datasetMap=new DatasetMap();

		SourceBean sbRows=SourceBean.fromXMLString(res);

		List listAtts=sbRows.getAttributeAsList("ROW"); // One row for each category

		categoriesNumber=0;

		// Two datasets, one for first axis named 1, one for second axis named 2
		datasetMap.getDatasets().put("1", new DefaultCategoryDataset());
		datasetMap.getDatasets().put("2", new DefaultCategoryDataset());


		boolean first=true;
		//categories.put(new Integer(0), "All Categories");

		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) { 		// run all categories (one for each row)
			SourceBean category = (SourceBean) iterator.next();
			List atts=category.getContainedAttributes();							// attributes: x is category name, values as serie_name are others, addition values can be added

			HashMap series=new LinkedHashMap();
			HashMap additionalValues=new LinkedHashMap();
			String catValue="";

			String nameP="";
			String value="";

			if(first){
				if (name.indexOf("$F{") >= 0){
					setTitleParameter(atts);
				}
				if (getSubName().indexOf("$F") >= 0){
					setSubTitleParameter(atts);
				}
				first=false;
			}


			//run all the attributes, to define series!
			int numColumn = 0;
			for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
				numColumn ++;
				SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

				nameP=new String(object.getKey());
				value=new String((String)object.getValue());
				if(nameP.equalsIgnoreCase("x"))       								// category name
				{
					catValue=value;
					categoriesNumber=categoriesNumber+1;
					categories.put(new Integer(categoriesNumber),value);
				}
				else {
					if(nameP.startsWith("add_") || nameP.startsWith("ADD_")){       // additional information
						if(additionalLabels){
							String ind=nameP.substring(4);							
							additionalValues.put(ind, value);
						}
					}
					else{
						if(seriesLabelsMap!=null){									// a serie
							String serieLabel = (String)seriesLabelsMap.get(nameP);
							series.put(serieLabel, value);
							seriesCaptions.put(serieLabel, nameP);
						}
						else
							series.put(nameP, value);
					}

					// for now I make like if addition value is checked he seek for an attribute with name with value+name_serie
				}
			}


			// for each serie
			for (Iterator iterator3 = series.keySet().iterator(); iterator3.hasNext();) {
				String nameS = (String) iterator3.next();
				String labelS = "";
				String valueS=(String)series.get(nameS);
				if(!hiddenSeries.contains(nameS)){
					if(seriesLabelsMap != null && (seriesCaptions != null && seriesCaptions.size()>0)){
						nameS = (String)(seriesCaptions.get(nameS));
						labelS = (String)seriesLabelsMap.get(nameS);
					}
					else
						labelS = nameS;	


					// Fill DATASET: Check if has to be filled dataset 1 or dataset 2
					if(!isHiddenSerie(nameS)){ 
						if(!seriesNames.contains(nameS))
							seriesNames.add(nameS);
						if(seriesScale != null && seriesScale.get(nameS)!=null && ((String)seriesScale.get(nameS)).equalsIgnoreCase("2")){ // 2 axis
							if(!seriesNames.contains(nameS))seriesNames.add(nameS);
							((DefaultCategoryDataset)(datasetMap.getDatasets().get("2"))).addValue(Double.valueOf(valueS).doubleValue(), labelS, catValue);
						}
						else{ 																												// 1 axis	
							if(!seriesNames.contains(nameS))seriesNames.add(nameS);							
							((DefaultCategoryDataset)(datasetMap.getDatasets().get("1"))).addValue(Double.valueOf(valueS).doubleValue(), labelS, catValue);
						}
					}

					// if there is an additional label are 
					if(additionalValues.get(nameS)!=null){
						String val=(String)additionalValues.get(nameS);
						String index=catValue+"-"+nameS;
						catSerLabels.put(index, val);
					}

				}
			}     // close series cycle

		} // Close cycle on SpagoBI Dataset rows

		if (listAtts.size() == 0){
			if (name.indexOf("$F{") >= 0){
				setTitleParameter("");
			}
			if (getSubName().indexOf("$F") >= 0){
				setSubTitleParameter("");
			}
		}
		logger.debug("OUT");

		return datasetMap;


	}






	public void configureChart(SourceBean content) {
		super.configureChart(content);
		logger.debug("IN");


		if(confParameters.get("add_labels")!=null){	
			String additional=(String)confParameters.get("add_labels");
			if(additional.equalsIgnoreCase("true")){
				additionalLabels=true;
				catSerLabels=new LinkedHashMap();
			}
			else additionalLabels=false;
		}
		else
		{
			additionalLabels=false;
		}

		// by default first and second axis uses bar, can have lines by specifing parameters
		// <parameter name="first_axis_shape" value"line"/>
		// <parameter name="second_axis_shape" value"line"/>

		if(confParameters.get("first_axis_shape")!=null && ((String)confParameters.get("first_axis_shape")).equalsIgnoreCase("line")){	
			firstAxisLine=true;
		}

		if(confParameters.get("second_axis_shape")!=null && ((String)confParameters.get("second_axis_shape")).equalsIgnoreCase("line")){	
			secondAxisLine=true;
		}

		if(confParameters.get("second_axis_label")!=null){	
			secondAxisLabel=(String)confParameters.get("second_axis_label");
		}


		// check wich series has to be mapped to the first axis and wich to the second
		SourceBean scales = (SourceBean)content.getAttribute("SERIES_SCALES");

		if(scales==null){
			scales = (SourceBean)content.getAttribute("CONF.SERIES_SCALES");
		}
		seriesScale=new LinkedHashMap(); // Maps serie Name to scale Number (1 or 2)

		if(scales!=null){

			List attsScales=scales.getContainedAttributes();

			String serieName="";
			Integer serieScale=1;
			for (Iterator iterator = attsScales.iterator(); iterator.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator.next();
				serieName=new String(object.getKey());
				try{
					String serieScaleS=(String)object.getValue();
					serieScale=Integer.valueOf(serieScaleS);
				}
				catch (Exception e) {
					logger.error("Not correct numebr scale; setting default 1");
					serieScale=Integer.valueOf(1);
				}

				if(serieScale.equals(2)){
					seriesScale.put(serieName, "2");
				}
				else{
					seriesScale.put(serieName, "1");					
				}
			}		
		}

		logger.debug("OUT");
	}




	public JFreeChart createChart(DatasetMap datasets) {
		logger.debug("IN");

		// recover the datasets
		DefaultCategoryDataset datasetBarFirstAxis=(DefaultCategoryDataset)datasets.getDatasets().get("1");
		DefaultCategoryDataset datasetBarSecondAxis=(DefaultCategoryDataset)datasets.getDatasets().get("2");

		// create the two subplots
		CategoryPlot subPlot1 = new CategoryPlot();
		CategoryPlot subPlot2 = new CategoryPlot();
		CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot();

		subPlot1.setDataset(datasetBarFirstAxis);
		subPlot2.setDataset(datasetBarSecondAxis);
		
		// Range Axis 1
		NumberAxis rangeAxis = new NumberAxis(getValueLabel());
		rangeAxis.setLabelFont(new Font(styleXaxesLabels.getFontName(), Font.PLAIN, styleXaxesLabels.getSize()));
		rangeAxis.setLabelPaint(styleXaxesLabels.getColor());
		rangeAxis.setTickLabelFont(new Font(styleXaxesLabels.getFontName(), Font.PLAIN, styleXaxesLabels.getSize()));
		rangeAxis.setTickLabelPaint(styleXaxesLabels.getColor());
		rangeAxis.setUpperMargin(0.10);
		subPlot1.setRangeAxis(rangeAxis);

		// Range Axis 2
		NumberAxis rangeAxis2 = new NumberAxis(secondAxisLabel);
		rangeAxis2.setLabelFont(new Font(styleXaxesLabels.getFontName(), Font.PLAIN, styleXaxesLabels.getSize()));
		rangeAxis2.setLabelPaint(styleXaxesLabels.getColor());
		rangeAxis2.setTickLabelFont(new Font(styleXaxesLabels.getFontName(), Font.PLAIN, styleXaxesLabels.getSize()));
		rangeAxis2.setTickLabelPaint(styleXaxesLabels.getColor());
		rangeAxis2.setUpperMargin(0.10);
		subPlot2.setRangeAxis(rangeAxis2);

		// Category Axis
		CategoryAxis domainAxis = new CategoryAxis(getCategoryLabel());
		domainAxis.setLabelFont(new Font(styleYaxesLabels.getFontName(), Font.PLAIN, styleYaxesLabels.getSize()));
		domainAxis.setLabelPaint(styleYaxesLabels.getColor());
		domainAxis.setTickLabelFont(new Font(styleYaxesLabels.getFontName(), Font.PLAIN, styleYaxesLabels.getSize()));
		domainAxis.setTickLabelPaint(styleYaxesLabels.getColor());
		domainAxis.setUpperMargin(0.10);
		plot.setDomainAxis(domainAxis);
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);

		// Add subplots to main plot
		plot.add(subPlot1, 1);
		plot.add(subPlot2, 2);

		MyStandardCategoryItemLabelGenerator generator=null;

		// value labels and additional values are mutually exclusive
		if(showValueLabels==true)additionalLabels=false;

		if(additionalLabels){
			generator = new MyStandardCategoryItemLabelGenerator(catSerLabels,"{1}", NumberFormat.getInstance());
		}

//		Create Renderers!
		CategoryItemRenderer renderer1=null;
		CategoryItemRenderer renderer2=null;
		
		if(firstAxisLine==true) renderer1 = new LineAndShapeRenderer(); 
		else renderer1 = new BarRenderer();	

		if(secondAxisLine==true) renderer2 = new LineAndShapeRenderer(); 
		else renderer2 = new BarRenderer();	
		
		subPlot1.setRenderer(renderer1);
		subPlot2.setRenderer(renderer2);


		// Values or addition Labels for first BAR Renderer
		if(showValueLabels){
			renderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer1.setBaseItemLabelsVisible(true);
			renderer1.setBaseItemLabelFont(new Font(styleValueLabels.getFontName(), Font.PLAIN, styleValueLabels.getSize()));
			renderer1.setBaseItemLabelPaint(styleValueLabels.getColor());

			renderer1.setBasePositiveItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

			renderer1.setBaseNegativeItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

		}
		else if(additionalLabels){
			renderer1.setBaseItemLabelGenerator(generator);
			double orient=(-Math.PI / 2.0);
			if(styleValueLabels.getOrientation().equalsIgnoreCase("horizontal")){
				orient=0.0;
			}

			renderer1.setBasePositiveItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 
					orient));
			renderer1.setBaseNegativeItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 
					orient));

		}

		// Values or addition Labels for second BAR Renderer
		if(showValueLabels){
			renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer2.setBaseItemLabelsVisible(true);
			renderer2.setBaseItemLabelFont(new Font(styleValueLabels.getFontName(), Font.PLAIN, styleValueLabels.getSize()));
			renderer2.setBaseItemLabelPaint(styleValueLabels.getColor());

			renderer2.setBasePositiveItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

			renderer2.setBaseNegativeItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

		}
		else if(additionalLabels){
			renderer2.setBaseItemLabelGenerator(generator);
			double orient=(-Math.PI / 2.0);
			if(styleValueLabels.getOrientation().equalsIgnoreCase("horizontal")){
				orient=0.0;
			}

			renderer2.setBasePositiveItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 
					orient));
			renderer2.setBaseNegativeItemLabelPosition(new ItemLabelPosition(
					ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 
					orient));

		}

		// Second Dataset Colors!
		if(colorMap!=null){
			int idx = -1;
			for (Iterator iterator = datasetBarFirstAxis.getRowKeys().iterator(); iterator.hasNext();) {
				idx++;
				String serName = (String) iterator.next();
				String labelName = "";
				int index=-1;

				if (seriesCaptions != null && seriesCaptions.size()>0){
					labelName = serName;
					serName = (String)seriesCaptions.get(serName);
					index=datasetBarFirstAxis.getRowIndex(labelName);
				}
				else
					index=datasetBarFirstAxis.getRowIndex(serName);

				Color color=(Color)colorMap.get(serName);
				if(color!=null){
					renderer1.setSeriesPaint(index, color);
				}	
			}
			for (Iterator iterator = datasetBarSecondAxis.getRowKeys().iterator(); iterator.hasNext();) {
				idx++;
				String serName = (String) iterator.next();
				String labelName = "";
				int index=-1;

				if (seriesCaptions != null && seriesCaptions.size()>0){
					labelName = serName;
					serName = (String)seriesCaptions.get(serName);
					index=datasetBarSecondAxis.getRowIndex(labelName);
				}
				else
					index=datasetBarSecondAxis.getRowIndex(serName);

				Color color=(Color)colorMap.get(serName);
				if(color!=null){
					renderer2.setSeriesPaint(index, color);
					/* test con un renderer
						if (idx > index){
							index = idx+1;
						}

						barRenderer.setSeriesPaint(index, color);*/
				}	
			}				
		}


		//defines url for drill
		boolean document_composition=false;
		if(mode.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION))document_composition=true;

		logger.debug("Calling Url Generation");

//		
//		MyCategoryUrlGenerator mycatUrl=null;
//		if(super.rootUrl!=null){
//			logger.debug("Set MycatUrl");
//			mycatUrl=new MyCategoryUrlGenerator(super.rootUrl);
//
//			mycatUrl.setDocument_composition(document_composition);
//			mycatUrl.setCategoryUrlLabel(super.categoryUrlName);
//			mycatUrl.setSerieUrlLabel(super.serieUrlname);
//		}
//		if(mycatUrl!=null){
//			renderer1.setItemURLGenerator(mycatUrl);
//			renderer2.setItemURLGenerator(mycatUrl);
//		}


		//plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		JFreeChart chart = new JFreeChart(plot);
		TextTitle title = setStyleTitle(name, styleTitle);
		chart.setTitle(title);
		if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}
		chart.setBackgroundPaint(Color.white);

		logger.debug("OUT");

		return chart;



	}



	private boolean isHiddenSerie(String serName){
		boolean res = false;

		for (int i=0; i < hiddenSeries.size(); i++){
			if (((String)hiddenSeries.get(i)).equalsIgnoreCase(serName)){
				res = true;
				break;
			}
		}
		return res;
	}
}
