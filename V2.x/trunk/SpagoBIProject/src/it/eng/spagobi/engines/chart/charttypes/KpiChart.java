package it.eng.spagobi.engines.chart.charttypes;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.ValueDataset;

public class KpiChart {
	
	double lower=0.0;
	double upper=0.0;
	double increment=0.0;
	int minorTickCount=0;
	String name=null;
	
	public JFreeChart createDialChart(String chartTitle, ValueDataset dataset){
		return null;
	}

	public double getLower() {
		return lower;
	}

	public void setLower(double lower) {
		this.lower = lower;
	}

	public double getUpper() {
		return upper;
	}

	public void setUpper(double upper) {
		this.upper = upper;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	}

	public int getMinorTickCount() {
		return minorTickCount;
	}

	public void setMinorTickCount(int minorTickCount) {
		this.minorTickCount = minorTickCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
