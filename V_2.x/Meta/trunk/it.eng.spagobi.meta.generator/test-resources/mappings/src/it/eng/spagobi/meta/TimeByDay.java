package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the time_by_day table.
 * 
 */
@Entity
@Table(name="time_by_day")
public class TimeByDay implements Serializable {

private static final long serialVersionUID = 1L;

public TimeByDay() {
}
	
	@Column(name="time_id")
private String timeId=null;
	@Column(name="the_date")
private String theDate=null;
	@Column(name="the_day")
private String theDay=null;
	@Column(name="the_month")
private String theMonth=null;
	@Column(name="the_year")
private String theYear=null;
	@Column(name="day_of_month")
private String dayOfMonth=null;
	@Column(name="week_of_year")
private String weekOfYear=null;
	@Column(name="month_of_year")
private String monthOfYear=null;
	@Column(name="quarter")
private String quarter=null;
	@Column(name="fiscal_period")
private String fiscalPeriod=null;

public String getTimeId () {
	return this.timeId;
}
public void setTimeId (String timeId) {
	this.timeId = timeId;
}
public String getTheDate () {
	return this.theDate;
}
public void setTheDate (String theDate) {
	this.theDate = theDate;
}
public String getTheDay () {
	return this.theDay;
}
public void setTheDay (String theDay) {
	this.theDay = theDay;
}
public String getTheMonth () {
	return this.theMonth;
}
public void setTheMonth (String theMonth) {
	this.theMonth = theMonth;
}
public String getTheYear () {
	return this.theYear;
}
public void setTheYear (String theYear) {
	this.theYear = theYear;
}
public String getDayOfMonth () {
	return this.dayOfMonth;
}
public void setDayOfMonth (String dayOfMonth) {
	this.dayOfMonth = dayOfMonth;
}
public String getWeekOfYear () {
	return this.weekOfYear;
}
public void setWeekOfYear (String weekOfYear) {
	this.weekOfYear = weekOfYear;
}
public String getMonthOfYear () {
	return this.monthOfYear;
}
public void setMonthOfYear (String monthOfYear) {
	this.monthOfYear = monthOfYear;
}
public String getQuarter () {
	return this.quarter;
}
public void setQuarter (String quarter) {
	this.quarter = quarter;
}
public String getFiscalPeriod () {
	return this.fiscalPeriod;
}
public void setFiscalPeriod (String fiscalPeriod) {
	this.fiscalPeriod = fiscalPeriod;
}

}