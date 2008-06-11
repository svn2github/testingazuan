package it.eng.spagobi.engines.chart.bo.charttypes.barcharts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyStandardCategoryItemLabelGenerator;

import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.ui.TextAnchor;

public class OverlaidBarLine extends BarCharts {


	HashMap seriesDraw=null;
	boolean additionalLabels=false;
	HashMap catSerLabels=null;
	boolean useBars=false;
	boolean useLines=false;

	boolean secondAxis=false;
	String secondAxisLabel=null;

	public DatasetMap calculateValue() throws Exception {

		seriesNames=new Vector();
		// I must identify different series

		String res=DataSetAccessFunctions.getDataSetResultFromId(profile, getData(),parametersObject);
		categories=new HashMap();

		//DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		DatasetMap datasetMap=new DatasetMap();

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");


		// run all categories (one for each row)
		categoriesNumber=0;

		// new versione: two datasets one for bar and one for lines
		datasetMap.getDatasets().put("bar", new DefaultCategoryDataset());
		datasetMap.getDatasets().put("line", new DefaultCategoryDataset());


		//categories.put(new Integer(0), "All Categories");
		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
			SourceBean category = (SourceBean) iterator.next();
			List atts=category.getContainedAttributes();

			HashMap series=new HashMap();
			HashMap additionalValues=new HashMap();
			String catValue="";

			String name="";
			String value="";

			//run all the attributes, to define series!
			for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

				name=new String(object.getKey());
				value=new String((String)object.getValue());
				if(name.equalsIgnoreCase("x"))
				{
					catValue=value;
					categoriesNumber=categoriesNumber+1;
					categories.put(new Integer(categoriesNumber),value);


				}
				else {
					if(name.startsWith("add_")){
						if(additionalLabels){
							String ind=name.substring(4);							
							additionalValues.put(ind, value);
						}
					}
					else{
						series.put(name, value);
					}

					// for now I make like if addition value is checked he seek for an attribute with name with value+name_serie
				}
			}


			// for each serie
			for (Iterator iterator3 = series.keySet().iterator(); iterator3.hasNext();) {
				String nameS = (String) iterator3.next();
				String valueS=(String)series.get(nameS);


				// if to draw as a line
				if(seriesDraw.get(nameS)!=null && ((String)seriesDraw.get(nameS)).equalsIgnoreCase("line")){
					if(!seriesNames.contains(nameS))seriesNames.add(nameS);
					((DefaultCategoryDataset)(datasetMap.getDatasets().get("line"))).addValue(Double.valueOf(valueS).doubleValue(), nameS, catValue);
				}
				else{ // if to draw as a bar
					if(!seriesNames.contains(nameS))seriesNames.add(nameS);
					((DefaultCategoryDataset)(datasetMap.getDatasets().get("bar"))).addValue(Double.valueOf(valueS).doubleValue(), nameS, catValue);

				}
				// if there is an additional label are 
				if(additionalValues.get(nameS)!=null){
					String val=(String)additionalValues.get(nameS);
					String index=catValue+"-"+nameS;
					catSerLabels.put(index, val);
				}


			}





		}
		return datasetMap;


	}

	public void configureChart(SourceBean content) {
		super.configureChart(content);

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


		//reading series colors if present
		SourceBean draws = (SourceBean)content.getAttribute("CONF.SERIES_DRAW");
		seriesDraw=new HashMap();
		if(draws!=null){
		
			List atts=draws.getContainedAttributes();

			String serieName="";
			String serieDraw="";
			for (Iterator iterator = atts.iterator(); iterator.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator.next();
				serieName=new String(object.getKey());
				serieDraw=new String((String)object.getValue());

				if(serieDraw.equalsIgnoreCase("line")){
					seriesDraw.put(serieName, "line");
				useLines=true;
				}
				else{
					seriesDraw.put(serieName, "bar");					
					useBars=true;
				}

			}		

		}
		else{
			useBars=true;
		}

		if(confParameters.get("second_axis_label")!=null){	
			secondAxis=true;
			secondAxisLabel=(String)confParameters.get("second_axis_label");
		}




	}

	public JFreeChart createChart(DatasetMap datasets) {


		// create the first renderer...


		CategoryPlot plot = new CategoryPlot();

		plot.setDomainAxis(new CategoryAxis(getCategoryLabel()));
		plot.setRangeAxis(new NumberAxis(getValueLabel()));
		

		

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);

		DefaultCategoryDataset datasetBar=(DefaultCategoryDataset)datasets.getDatasets().get("bar");

		
		//I create one bar renderer and one line


		
		MyStandardCategoryItemLabelGenerator generator=null;
		if(additionalLabels){
			generator = new MyStandardCategoryItemLabelGenerator(catSerLabels,"{1}", NumberFormat.getInstance());}
	
		if(useBars){
		CategoryItemRenderer barRenderer = new BarRenderer();

		
		if(additionalLabels){
			barRenderer.setBaseItemLabelGenerator(generator);
			barRenderer.setBaseItemLabelFont(new Font("Serif", Font.BOLD, 13));
			barRenderer.setBaseItemLabelsVisible(true);
	        barRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
	                ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 
	                -Math.PI / 2.0));
	        barRenderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(
	                ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 
	                -Math.PI / 2.0));	

		}
		
	
		
		if(colorMap!=null){
			for (Iterator iterator = datasetBar.getRowKeys().iterator(); iterator.hasNext();) {
				String serName = (String) iterator.next();
				int index=datasetBar.getRowIndex(serName);
				Color color=(Color)colorMap.get(serName);
				if(color!=null){
					barRenderer.setSeriesPaint(index, color);
				}	
			}
		}
		



		plot.setDataset(1,datasetBar);
		plot.setRenderer(1,barRenderer);

		}
		
if(useLines){
		
		LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
		//lineRenderer.setShapesFilled(false);
		lineRenderer.setShapesFilled(true);
		if(additionalLabels){lineRenderer.setBaseItemLabelGenerator(generator);
		 lineRenderer.setBaseItemLabelFont(new Font("Serif", Font.BOLD, 13));
		lineRenderer.setBaseItemLabelsVisible(true);
		}

		DefaultCategoryDataset datasetLine=(DefaultCategoryDataset)datasets.getDatasets().get("line");



		if(colorMap!=null){
			for (Iterator iterator = datasetLine.getRowKeys().iterator(); iterator.hasNext();) {
				String serName = (String) iterator.next();

				int index=datasetLine.getRowIndex(serName);
				Color color=(Color)colorMap.get(serName);
				if(color!=null){
					lineRenderer.setSeriesPaint(index, color);
				}	
			}
		}
		plot.setDataset(0,datasetLine);
		plot.setRenderer(0,lineRenderer);
}
		

		if(secondAxis){
			plot.setRangeAxis(1,new NumberAxis(secondAxisLabel));
			plot.mapDatasetToRangeAxis(0, 1);
		}



		//plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.UP_45);
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(name);
		return chart;



	}


}
