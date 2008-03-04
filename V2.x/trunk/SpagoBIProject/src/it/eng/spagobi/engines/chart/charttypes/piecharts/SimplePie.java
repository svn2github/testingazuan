package it.eng.spagobi.engines.chart.charttypes.piecharts;


import it.eng.spago.base.SourceBean;

import java.awt.Font;

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

	boolean changeViewChecked=false; //false is 2D, true is 3D
	public static final String CHANGE_VIEW_LABEL="Set 3D View";
	
	public void configureChart(SourceBean content) {
		// TODO Auto-generated method stub
		super.configureChart(content);
	}

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		super.createChart(chartTitle, dataset);
		
		JFreeChart chart=null; 

		if(!changeViewChecked){
			chart = ChartFactory.createPieChart(
					chartTitle,  
					(PieDataset)dataset,             // data
					true,                // include legend
					true,
					false
			);



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

	public boolean isChangeViewChecked() {
		return changeViewChecked;
	}

	public void setChangeViewChecked(boolean changeViewChecked) {
		this.changeViewChecked = changeViewChecked;
	}

	public boolean isChangeableView() {
		return true;	
	}


}
