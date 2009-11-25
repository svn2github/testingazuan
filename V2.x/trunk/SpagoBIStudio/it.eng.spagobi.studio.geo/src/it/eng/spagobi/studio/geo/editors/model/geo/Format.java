package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Format implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2546260537067030640L;
	private String day;
	private String hour;
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
}
