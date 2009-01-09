package it.eng.spagobi.engines.kpi.bo.charttypes.trendcharts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.ui.VerticalAlignment;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyStandardCategoryItemLabelGenerator;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.BulletGraph;
import it.eng.spagobi.engines.kpi.utils.KpiInterval;
import it.eng.spagobi.engines.kpi.utils.StyleLabel;

public class LineChart extends ChartImpl{
	
	private static transient Logger logger=Logger.getLogger(LineChart.class);
	HashMap categories;
	int categoriesNumber=0;
	DatasetMap datasetMap;
	String res = "";
	
	public LineChart() {
		super();
		datasetMap=new DatasetMap();
		categories=new HashMap();
	}
	
	public DatasetMap calculateValue(String result) throws Exception {

		res = result;
		categories=new HashMap();
		datasetMap=new DatasetMap();

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");


		// run all categories (one for each row)
		categoriesNumber=0;

		datasetMap.getDatasets().put("line", new DefaultCategoryDataset());

		boolean first=true;
		//categories.put(new Integer(0), "All Categories");
		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
			SourceBean category = (SourceBean) iterator.next();
			List atts=category.getContainedAttributes();

			HashMap series=new LinkedHashMap();
			HashMap additionalValues=new LinkedHashMap();
			String catValue="";

			String nameP="";
			String value="";

			//run all the attributes, to define series!
			int numColumn = 0;
			for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
				numColumn ++;
				SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

				nameP=new String(object.getKey());
				value=new String((String)object.getValue());
				if(nameP.equalsIgnoreCase("x"))
				{
					catValue=value;
					categoriesNumber=categoriesNumber+1;
					categories.put(new Integer(categoriesNumber),value);

				}
				else {
							series.put(nameP, value);
					}
			}

			String nameS = "KPI_VALUE";
			String labelS = "kpi Values";
			String valueS=(String)series.get(nameS);
			((DefaultCategoryDataset)(datasetMap.getDatasets().get("line"))).addValue(Double.valueOf(valueS).doubleValue(), labelS, catValue);

		}
		return datasetMap;
	}
	
	
	public JFreeChart createChart(){
		
		logger.debug("IN");
		CategoryPlot plot = new CategoryPlot();

		
		NumberAxis rangeAxis = new NumberAxis("Kpi Values");
		rangeAxis.setLabelFont(new Font("Arial", Font.PLAIN, 12 ));
		Color colorLabel= Color.decode("#000000");
		rangeAxis.setLabelPaint(colorLabel);
		rangeAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 12 ));
		rangeAxis.setTickLabelPaint(colorLabel);
		plot.setRangeAxis(rangeAxis);
		
		CategoryAxis domainAxis = new CategoryAxis("Date");
		domainAxis.setLabelFont(new Font("Arial", Font.PLAIN, 12 ));
        domainAxis.setLabelPaint(colorLabel);
        domainAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 12 ));
        domainAxis.setTickLabelPaint(colorLabel);
		plot.setDomainAxis(domainAxis);

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);


		//I create a line renderer 
		MyStandardCategoryItemLabelGenerator generator=null;

			LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
			lineRenderer.setShapesFilled(true);
			lineRenderer.setBaseItemLabelGenerator(generator);
			lineRenderer.setBaseItemLabelFont(new Font("Arial", Font.PLAIN, 12 ));
			lineRenderer.setBaseItemLabelPaint(colorLabel);
			lineRenderer.setBaseItemLabelsVisible(true);

			DefaultCategoryDataset datasetLine=(DefaultCategoryDataset)datasetMap.getDatasets().get("line");

				for (Iterator iterator = datasetLine.getRowKeys().iterator(); iterator.hasNext();) {
					String serName = (String) iterator.next();
					String labelName = "";
					int index=-1;
					index=datasetLine.getRowIndex(serName);
					
					Color color=Color.decode("#990200");
					lineRenderer.setSeriesPaint(index, color);	
				}

			plot.setDataset(0,datasetLine);
			plot.setRenderer(0,lineRenderer);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.UP_45);
		JFreeChart chart = new JFreeChart(plot);
		TextTitle title=new TextTitle(name,new Font("Arial", Font.BOLD, 20 ),Color.decode("#990200"), RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS);
		chart.setTitle(title);
		TextTitle subTitle =new TextTitle(subName,new Font("Arial", Font.PLAIN, 14 ),Color.decode("#000000"), RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS);
		chart.addSubtitle(subTitle);
		TextTitle subTitle2 =new TextTitle(subName,new Font("Arial", Font.PLAIN, 12 ),Color.decode("#FFFFFF"), RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS);
		chart.addSubtitle(subTitle2);
		
		chart.setBackgroundPaint(Color.white);
		return chart;
	}

}
