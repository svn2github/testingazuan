package it.eng.spagobi.engines.chart.bo.charttypes.piecharts;


import it.eng.spago.base.SourceBean;

import java.awt.Font;
import java.util.List;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class SimplePie extends PieCharts{

	boolean threeD=false; //false is 2D, true is 3D
	boolean isThreedViewConfigured=false;
	boolean percentage=false;
	boolean isPercentageConfigured=false;


	public static final String CHANGE_VIEW_3D_LABEL="Set View Dimension";
	public static final String CHANGE_VIEW_3D_LABEL1="Set 2D";
	public static final String CHANGE_VIEW_3D_LABEL2="Set 3D";


	public static final String CHANGE_VIEW_3D="threeD";

	public static final String CHANGE_VIEW_PERCENTAGE_LABEL="Set Percentage Mode";
	public static final String CHANGE_VIEW_PERCENTAGE_LABEL1="Absolute Values";
	public static final String CHANGE_VIEW_PERCENTAGE_LABEL2="Percentage Values";


	public static final String CHANGE_VIEW_PERCENTAGE="percentage";



	public void configureChart(SourceBean content) {
		// TODO Auto-generated method stub
		super.configureChart(content);
		if(confParameters.get("dimensions")!=null){	
			String orientation=(String)confParameters.get("dimensions");
			if(orientation.equalsIgnoreCase("3D")){
				threeD=true;
				isThreedViewConfigured=true;
			}
			else if(orientation.equalsIgnoreCase("2D")){
				threeD=false;
				isThreedViewConfigured=true;
			}
		}
		if(confParameters.get("values")!=null){	
			String orientation=(String)confParameters.get("values");
			if(orientation.equalsIgnoreCase("percentage")){
				percentage=true;
				isPercentageConfigured=true;
			}
			else if(orientation.equalsIgnoreCase("absolute")){
				percentage=false;
				isPercentageConfigured=true;
			}
		}

		
		
	}

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		super.createChart(chartTitle, dataset);

		JFreeChart chart=null; 

		if(!threeD){
			chart = ChartFactory.createPieChart(
					chartTitle,  
					(PieDataset)dataset,             // data
					true,                // include legend
					true,
					false
			);


			chart.setBackgroundPaint(color);

			TextTitle title = chart.getTitle();
			title.setToolTipText("A title tooltip!");


			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
			// plot.setNoDataMessages("No data available");
			plot.setCircular(false);
			plot.setLabelGap(0.02);
			plot.setNoDataMessage("No data available");

			if(percentage==false){
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} ({1})"));}
			else
			{
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} ({2})"));
			}

		}
		else{
			chart = ChartFactory.createPieChart3D(
					chartTitle,  
					(PieDataset)dataset,             // data
					true,                // include legend
					true,
					false
			);


			chart.setBackgroundPaint(color);

			TextTitle title = chart.getTitle();
			title.setToolTipText("A title tooltip!");


			PiePlot3D plot = (PiePlot3D) chart.getPlot();

			plot.setDarkerSides(true);
			plot.setStartAngle(290);
			plot.setDirection(Rotation.CLOCKWISE);
			plot.setForegroundAlpha(1.0f);
			plot.setDepthFactor(0.2);

			plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
			// plot.setNoDataMessages("No data available");
			plot.setCircular(false);
			plot.setLabelGap(0.02);
			plot.setNoDataMessage("No data available");

			if(percentage==false){
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} ({1})"));}
			else
			{
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} ({2})"));
			}
		}



		return chart;

	}




	public boolean isChangeableView() {
		return true;	
	}





	public void setChangeViewsParameter(String changePar, boolean how) {
		if(changePar.equalsIgnoreCase(CHANGE_VIEW_3D)){
			threeD=how;
			int temp=getWidth();
			setWidth(getHeight());
			setHeight(temp);
		}
		else if(changePar.equalsIgnoreCase(CHANGE_VIEW_PERCENTAGE)){
			percentage =how;
		}


	}

	public boolean getChangeViewParameter(String changePar) {
		boolean ret=false;
		if(changePar.equalsIgnoreCase(CHANGE_VIEW_3D)){
			ret=threeD;
		}
		else if(changePar.equalsIgnoreCase(CHANGE_VIEW_PERCENTAGE)){
			ret=percentage;
		}
		return ret;
	}

	public String getChangeViewParameterLabel(String changePar, int i) {
		String ret="";
		if(changePar.equalsIgnoreCase(CHANGE_VIEW_3D)){
			if(i==0) ret=CHANGE_VIEW_3D_LABEL;
			else if(i==1) ret=CHANGE_VIEW_3D_LABEL1;
			else if(i==2) ret=CHANGE_VIEW_3D_LABEL2;
		}
		else if(changePar.equalsIgnoreCase(CHANGE_VIEW_PERCENTAGE)){
			if(i==0) ret=CHANGE_VIEW_PERCENTAGE_LABEL;
			else if(i==1) ret=CHANGE_VIEW_PERCENTAGE_LABEL1;
			else if(i==2) ret=CHANGE_VIEW_PERCENTAGE_LABEL2;
		}

		return ret;
	}


	public List getPossibleChangePars() {
		List l=new Vector();
		if(!isThreedViewConfigured)	{l.add(CHANGE_VIEW_3D); }
		if(!isPercentageConfigured){ l.add(CHANGE_VIEW_PERCENTAGE); }
		return l;
	}


}
