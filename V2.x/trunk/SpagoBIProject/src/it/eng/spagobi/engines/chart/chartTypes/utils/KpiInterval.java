package it.eng.spagobi.engines.chart.chartTypes.utils;

import java.awt.Color;

public class KpiInterval {

	private double min;
	private double max;
	private Color color;
	
	
	public KpiInterval() {
		super();
	}

	public KpiInterval(double min, double max, Color color) {
		super();
		this.min = min;
		this.max = max;
		this.color = color;
	}
	
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
