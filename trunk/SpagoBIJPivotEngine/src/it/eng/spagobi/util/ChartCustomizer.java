/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package it.eng.spagobi.util;

import it.eng.spagobi.bean.AnalysisBean;

import com.tonbeller.jpivot.chart.ChartComponent;

public class ChartCustomizer {

	public static void customizeChart(AnalysisBean analysis, ChartComponent chart) {
		
		chart.setChartHeight(analysis.getChartHeight());
		chart.setChartWidth(analysis.getChartWidth());
		chart.setChartTitle(analysis.getChartTitle());
		chart.setChartType(analysis.getChartType());
		chart.setFontName(analysis.getFontName());
		chart.setFontStyle(analysis.getFontStyle());
		chart.setFontSize(analysis.getFontSize());

		// legend
		chart.setShowLegend(analysis.isShowLegend());
		// if legend is visible, set properties
		if (analysis.isShowLegend() == true) {
			chart.setLegendFontName(analysis.getLegendFontName());
			chart.setLegendFontStyle(analysis.getLegendFontStyle());
			chart.setLegendFontSize(analysis.getLegendFontSize());
			chart.setLegendPosition(analysis.getLegendPosition());
		}

		// slicer
		chart.setShowSlicer(analysis.isShowSlicer());
		// if slicer is visible, set properties
		if (analysis.isShowSlicer() == true) {
			chart.setSlicerPosition(analysis.getSlicerPosition());
			chart.setSlicerAlignment(analysis.getSlicerAlignment());
			chart.setSlicerFontName(analysis.getSlicerFontName());
			chart.setSlicerFontStyle(analysis.getSlicerFontStyle());
			chart.setSlicerFontSize(analysis.getSlicerFontSize());
		}

		// axes
		chart.setAxisFontName(analysis.getAxisFontName());
		chart.setAxisFontStyle(analysis.getAxisFontStyle());
		chart.setAxisFontSize(analysis.getAxisFontSize());
		chart.setHorizAxisLabel(analysis.getHorizAxisLabel());
		chart.setVertAxisLabel(analysis.getVertAxisLabel());
		chart.setAxisTickFontName(analysis.getAxisTickFontName());
		chart.setAxisTickFontStyle(analysis.getAxisTickFontStyle());
		chart.setAxisTickFontSize(analysis.getAxisTickFontSize());

		chart.setDrillThroughEnabled(analysis.isDrillThroughEnabled());
		chart.setTickLabelRotate(analysis.getTickLabelRotate());

		// set chart visible status
		chart.setVisible(analysis.isShowChart());

		// background color
		chart.setBgColorB(analysis.getBgColorB());
		chart.setBgColorG(analysis.getBgColorG());
		chart.setBgColorR(analysis.getBgColorR());
	

	}
	
}
