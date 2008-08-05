package it.eng.spagobi.engines.chart.bo.charttypes.XYCharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.StackedBar;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;

import java.awt.Color;
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
    	/*XYZDataset dataset=(XYZDataset)datasets.getDatasets().get("1");
        NumberAxis xAxis = new NumberAxis(xLabel);
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        xAxis.setLabel(xLabel);
        NumberAxis yAxis = new NumberAxis(yLabel);
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setInverted(false);
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        yAxis.setLabel(yLabel);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBlockRenderer renderer = new XYBlockRenderer();
        
        LookupPaintScale paintScale = new LookupPaintScale(zvalues[0], new Double(zrangeMax), Color.black);
        for (int ke=0; ke<(zvalues.length-1) ; ke++){
        	String key =new Integer((new Double(zvalues[ke])).intValue()).toString();
        	Color temp =(Color)colorRangeMap.get(key);
        	paintScale.add(zvalues[ke],temp);
        }       
        renderer.setPaintScale(paintScale);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setForegroundAlpha(0.66f);
        plot.setAxisOffset(new RectangleInsets(20, 20, 20, 20));
        JFreeChart chart = new JFreeChart(name, plot);
        chart.removeLegend();
        chart.setBackgroundPaint(Color.white);
        SymbolAxis scaleAxis = new SymbolAxis(null, legendLabels);
        scaleAxis.setRange(zvalues[0], new Double(zrangeMax));
        scaleAxis.setPlot(new PiePlot());
        scaleAxis.setGridBandsVisible(false);
        PaintScaleLegend psl = new PaintScaleLegend(paintScale, scaleAxis);
        psl.setAxisOffset(20.0);
        psl.setPosition(RectangleEdge.BOTTOM);
        psl.setMargin(new RectangleInsets(20, 20, 20, 20));
        
        chart.addSubtitle(psl);
        return chart;*/
		XYZDataset dataset=(XYZDataset)datasets.getDatasets().get("1");
		NumberAxis xAxis = new NumberAxis("X");
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        NumberAxis yAxis = new NumberAxis("Y");
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setInverted(false);
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        /*double unit = 20.0;
        double unit1 = 15.0;
        double unit2 = 10.0;
        NumberTickUnit c = new NumberTickUnit(unit);
        NumberTickUnit c1 = new NumberTickUnit(unit1);
        NumberTickUnit c2 = new NumberTickUnit(unit2);
        yAxis.setTickUnit(c);
        yAxis.setTickUnit(c1);
        yAxis.setTickUnit(c2);*/
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockWidth(2.0);
        renderer.setBlockHeight(2.0);
        LookupPaintScale paintScale = new LookupPaintScale(0.5, 3.5, 
                Color.black);
        paintScale.add(0.5, Color.green);
        paintScale.add(1.5, Color.orange);
        paintScale.add(2.5, Color.red);        
        renderer.setPaintScale(paintScale);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        //ValueAxis ciao = new ValueAxis("Bau");
        
       
        plot.setDomainCrosshairLockedOnData(false);
        plot.setInsets(new RectangleInsets(10, 20, 10, 10));
        plot.setForegroundAlpha(0.66f);
        plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
        JFreeChart chart = new JFreeChart("Titolo", plot);
     
        chart.removeLegend();
        chart.setBackgroundPaint(Color.white);
        SymbolAxis scaleAxis = new SymbolAxis(null, new String[] {"", "OK", 
                "Uncertain", "Bad","Ancora"});
        scaleAxis.setRange(0.5, 10.5);
        scaleAxis.setPlot(new PiePlot());
        scaleAxis.setGridBandsVisible(false);
        PaintScaleLegend psl = new PaintScaleLegend(paintScale, scaleAxis);
        psl.setAxisOffset(5.0);
        psl.setPosition(RectangleEdge.BOTTOM);
        psl.setMargin(new RectangleInsets(5, 5, 5, 5));
        chart.addSubtitle(psl);
        return chart;
    }    

}
