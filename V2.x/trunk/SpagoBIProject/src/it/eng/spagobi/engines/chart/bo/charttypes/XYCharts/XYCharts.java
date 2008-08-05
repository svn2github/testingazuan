package it.eng.spagobi.engines.chart.bo.charttypes.XYCharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.ChartImpl;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.BarCharts;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.chart.utils.StyleLabel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.data.xy.YIntervalDataItem;

/**
 * @author chiarelli
 */
public class XYCharts extends ChartImpl{


	String xrangeMin="";
	String xrangeMax="";
	String yrangeMin="";
	String zrangeMin="";
	String yrangeMax="";
	String zrangeMax="";
	String xLabel="";
	String yLabel="";
	String[] legendLabels =null;
	double[] zvalues = null;
	String colours = "";
	String add_labels = "false";
	
	Map confParameters;
	HashMap colorRangeMap=new HashMap();  // keeps user selected colors// serie position - color
	HashMap RangeLabelMap =new HashMap();

	private static transient Logger logger=Logger.getLogger(BarCharts.class);
	StyleLabel addLabelsStyle;


    private static XYZDataset createDataset() {
        
    	
        double[] xvalues = new double[14 * 60];
        double[] yvalues = new double[14 * 60];        
        double[] zvalues = new double[14 * 60];
        double[][] data = new double[][] {xvalues, yvalues, zvalues};
        
        
        // set the default z-value to zero throughout the data array.
        for (int c = 0; c < 60; c++) {
            for (int r = 8; r < 22; r++) {
                setValue(data, c, r, 0.0);
            }
        }
        
        for (int r = 8; r < 12; r++) {
            for (int c = 12; c < 48; c++) {
                setValue(data, c, r, 1.0);
            }
        }
        for (int r = 12; r < 20; r++) {
            for (int c = 22; c < 42; c++) {
                setValue(data, c, r, 1.0);
            }
        }
        
        setValue(data, 4, 20, 3);
        setValue(data, 10, 20, 3);
        setValue(data, 20, 20, 2);
        setValue(data, 24, 20, 2);
        setValue(data, 34, 20, 2);
       
        DefaultXYZDataset dataset = new DefaultXYZDataset();
       
        dataset.addSeries("Series 1", data);
        return dataset;
    }
    
    private static void setValue(double[][] data, 
            int c, int r, double value) {

data[0][(r - 8) * 60 + c] = c;
data[1][(r - 8) * 60 + c] = r;
data[2][(r - 8) * 60 + c] = value;

}



	/**
	 * Inherited by IChart: calculates chart value.
	 * 
	 * @return the dataset
	 * 
	 * @throws Exception the exception
	 */

	public DatasetMap calculateValue() throws Exception {
		logger.debug("IN");
		
		
		/*
		String res=DataSetAccessFunctions.getDataSetResultFromId(profile, getData(),parametersObject);

		// XYZDataset dataset = createDataset();
		int xMaxValue = (new Integer(xrangeMax)).intValue();
		int xMinValue = (new Integer(xrangeMin)).intValue();
		int yMaxValue = (new Integer(yrangeMax)).intValue();
		int yMinValue = (new Integer(yrangeMin)).intValue();
		
		

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");

		DefaultXYZDataset dataset = new DefaultXYZDataset();
		int rangex = listAtts.size();
		int rangey = listAtts.size();
		
		double[] xvalues = new double[rangey * rangex];
        double[] yvalues = new double[rangey * rangex];        
        double[] zvalues = new double[rangey * rangex];
        
        
        double[][] data = new double[][] {xvalues, yvalues, zvalues};
        
        int xVal = 0;
		int yVal = 0;
		double zVal = 0;
		int count = 0 ;
		boolean first=true;
		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
			SourceBean category = (SourceBean) iterator.next();
			List atts=category.getContainedAttributes();

			String nameP="";
			String value="";
			
			
			for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

				nameP=new String(object.getKey());
				value=new String((String)object.getValue());
				if(nameP.equalsIgnoreCase("x"))
				{
						xVal = new Integer(value);	
				}
				if(nameP.equalsIgnoreCase("y"))
				{
					    yVal = new Integer(value);					
				}
				if(nameP.equalsIgnoreCase("z"))
				{
						zVal = new Double(value);				
				}			    
			   
				
				}
			
			data[0][count] = xVal;
			data[1][count] = yVal;
			data[2][count] = zVal;
			count++;
			  // setValueInData(data, xMaxValue , yMinValue, xVal, yVal, zVal);
			}
			
        dataset.addSeries("Series 1", data);*/
        
        XYZDataset dataset = createDataset();
        
		DatasetMap datasets=new DatasetMap();
		datasets.addDataset("1",dataset);
		logger.debug("OUT");
		return datasets;
	}

		/**
		 * Utility method called by createDataset().
		 * 
		 * @param data  the data array.
		 * @param c  the column.
		 * @param r  the row.
		 * @param value  the value.
		 */
		private static void setValueInData(double[][] data, int xMaxValue, int yStartValue, int c, int r, double value) {
		    
		    data[0][(r - yStartValue) * xMaxValue + c] = c;
		    data[1][(r - yStartValue) * xMaxValue + c] = r;
		    data[2][(r - yStartValue) * xMaxValue + c] = value;
		    
		}
	

	public void configureChart(SourceBean content) {
		logger.debug("IN");
		super.configureChart(content);
		confParameters = new HashMap();
		SourceBean confSB = (SourceBean)content.getAttribute("CONF");
		SourceBean zrange = (SourceBean)content.getAttribute("CONF.ZRANGES");

		if(confSB==null) return;
		List confAttrsList = confSB.getAttributeAsList("PARAMETER");

		Iterator confAttrsIter = confAttrsList.iterator();
		while(confAttrsIter.hasNext()) {
			SourceBean param = (SourceBean)confAttrsIter.next();
			String nameParam = (String)param.getAttribute("name");
			
			if (nameParam.equals("xrange")){
				xrangeMin = (String)param.getAttribute("value_low");
				xrangeMax = (String)param.getAttribute("value_high");
			}else if (nameParam.equals("yrange")){
				yrangeMin = (String)param.getAttribute("value_low");
				yrangeMax = (String)param.getAttribute("value_high");
			}
			
			String valueParam = (String)param.getAttribute("value");
			confParameters.put(nameParam, valueParam);
		}	
		
		if(zrange==null) return;		
		List ranges = zrange.getAttributeAsList("RANGE");
		int rangesNum = ranges.size();
		legendLabels= new String[rangesNum];
		zvalues = new double[rangesNum-1];
		Iterator rangesIter = ranges.iterator();
		int j = 0;
		while(rangesIter.hasNext()) {
			SourceBean range = (SourceBean)rangesIter.next();
			String nameParam = (String)range.getAttribute("label");
			String colour = "";
			String label = "";
			label = nameParam;
			
			
			colour = (String)range.getAttribute("colour");
			Color col=new Color(Integer.decode(colour).intValue());
			
			
			if (!nameParam.equals("outbound")){
				String low = (String)range.getAttribute("value_low");
				String high = (String)range.getAttribute("value_high");
				String low_high = low+","+high;
				legendLabels[j]=label;
				RangeLabelMap.put(label, low_high);	
				colorRangeMap.put( high,col);
				zvalues[j]=new Double(high);
			}else{
				String val = (String)range.getAttribute("value");
				zrangeMax = val;
			}
			j++;
			
		}	
		

		/*if(confParameters.get("add_labels")!=null){	
			add_labels=(String)confParameters.get("add_labels");
		}
		else
		{
			add_labels = "false";
		}*/

		if(confParameters.get("x_label")!=null){	
			xLabel=(String)confParameters.get("xlabel");
		}
		else
		{
			xLabel="X";
		}
		if(confParameters.get("y_label")!=null){	
			xLabel=(String)confParameters.get("ylabel");
		}
		else
		{
			yLabel="Y";
		}


		// check if there is some info about additional labels style

		SourceBean styleLabelsSB = (SourceBean)content.getAttribute("STYLE_LABELS");
		if(styleLabelsSB!=null){

			String fontS = (String)content.getAttribute("STYLE_LABELS.font");
			String sizeS = (String)content.getAttribute("STYLE_LABELS.size");
			String colorS = (String)content.getAttribute("STYLE_LABELS.color");
			String orientationS = (String)content.getAttribute("STYLE_LABELS.orientation");


			try{
				Color color=Color.decode(colorS);
				int size=Integer.valueOf(sizeS).intValue();
				addLabelsStyle=new StyleLabel(fontS,size,color,orientationS);

			}
			catch (Exception e) {
				logger.error("Wrong style labels settings, use default");
			}

		}

		logger.debug("OUT");
	}



	/**
	 * Gets the conf parameters.
	 * 
	 * @return the conf parameters
	 */
	public Map getConfParameters() {
		return confParameters;
	}

	/**
	 * Sets the conf parameters.
	 * 
	 * @param confParameters the new conf parameters
	 */
	public void setConfParameters(Map confParameters) {
		this.confParameters = confParameters;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#createChart(java.lang.String, org.jfree.data.general.Dataset)
	 */
	public JFreeChart createChart(DatasetMap dataset) {
		// TODO Auto-generated method stub
		return super.createChart(dataset);
	}

}
