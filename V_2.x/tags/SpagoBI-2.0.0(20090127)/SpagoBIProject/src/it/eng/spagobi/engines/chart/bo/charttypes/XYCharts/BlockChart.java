package it.eng.spagobi.engines.chart.bo.charttypes.XYCharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.StackedBar;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;

import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;



/**
 * @author chiarelli
 */
public class BlockChart extends XYCharts {
	
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
	HashMap catSerLabels=null;
	
	private static transient Logger logger=Logger.getLogger(BlockChart.class);

	
    /**
     * Creates a chart for the specified dataset.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart instance.
     */
	public JFreeChart createChart(DatasetMap datasets) {
    	XYZDataset dataset=(XYZDataset)datasets.getDatasets().get("1");
    	//Creates the xAxis with its label and style
        NumberAxis xAxis = new NumberAxis(xLabel);
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        xAxis.setLabel(xLabel);
        if(addLabelsStyle!=null && addLabelsStyle.getFont()!=null){
	        xAxis.setLabelFont(addLabelsStyle.getFont());
	        xAxis.setLabelPaint(addLabelsStyle.getColor());
        }
        //Creates the yAxis with its label and style
        NumberAxis yAxis = new NumberAxis(yLabel);
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setInverted(false);
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        yAxis.setLabel(yLabel);
        if(addLabelsStyle!=null && addLabelsStyle.getFont()!=null){
        	yAxis.setLabelFont(addLabelsStyle.getFont());
        	yAxis.setLabelPaint(addLabelsStyle.getColor());
        }
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        Color outboundCol = new Color(Integer.decode(outboundColor).intValue());
        
        //Sets the graph paint scale and the legend paintscale
        LookupPaintScale paintScale = new LookupPaintScale(zvalues[0], (new Double(zrangeMax)).doubleValue(),outboundCol);
        LookupPaintScale legendPaintScale = new LookupPaintScale(0.5, 0.5+zvalues.length, outboundCol);
        
        for (int ke=0; ke<=(zvalues.length-1) ; ke++){
        	Double key =(new Double(zvalues[ke]));
        	Color temp =(Color)colorRangeMap.get(key);
        	paintScale.add(zvalues[ke],temp);
        	legendPaintScale.add(0.5+ke, temp);
        }     
        //Configures the renderer
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setPaintScale(paintScale);
        double blockHeight =	(new Double(blockH)).doubleValue();
        double blockWidth =	(new Double(blockW)).doubleValue();
        renderer.setBlockWidth(blockWidth);
        renderer.setBlockHeight(blockHeight);
        
        //configures the plot with title, subtitle, axis ecc.
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setForegroundAlpha(0.66f);
        plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
        JFreeChart chart = new JFreeChart(plot);
        TextTitle title =setStyleTitle(name, styleTitle);
        chart.setTitle(title);
        if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}
        chart.removeLegend();
        chart.setBackgroundPaint(Color.white);
        
        //Sets legend labels
        SymbolAxis scaleAxis = new SymbolAxis(null,legendLabels);
        scaleAxis.setRange(0.5, 0.5+zvalues.length);
        scaleAxis.setPlot(new PiePlot());
        scaleAxis.setGridBandsVisible(false);
        scaleAxis.setLabel(zLabel);
        //scaleAxis.setLabelAngle(3.14/2);
        scaleAxis.setLabelFont(addLabelsStyle.getFont());
        scaleAxis.setLabelPaint(addLabelsStyle.getColor());
      
        //draws legend as chart subtitle
        PaintScaleLegend psl = new PaintScaleLegend(legendPaintScale, scaleAxis);
        psl.setAxisOffset(5.0);
        psl.setPosition(RectangleEdge.RIGHT);
        psl.setMargin(new RectangleInsets(5, 5, 5, 5));        
        chart.addSubtitle(psl);
        
        return chart;
    }    

}
