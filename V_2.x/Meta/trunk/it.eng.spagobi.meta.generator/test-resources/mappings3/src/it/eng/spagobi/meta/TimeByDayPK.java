package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the time_by_day table.
 * 
 */
@Embeddable
public class TimeByDayPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="time_id")
		private java.lang.String timeId;
		@Column(name="the_date")
		private java.lang.String theDate;
		@Column(name="the_day")
		private java.lang.String theDay;
		@Column(name="the_month")
		private java.lang.String theMonth;
		@Column(name="the_year")
		private java.lang.String theYear;
		@Column(name="day_of_month")
		private java.lang.String dayOfMonth;
		@Column(name="week_of_year")
		private java.lang.String weekOfYear;
		@Column(name="month_of_year")
		private java.lang.String monthOfYear;
		@Column(name="quarter")
		private java.lang.String quarter;
		@Column(name="fiscal_period")
		private java.lang.String fiscalPeriod;

    public TimeByDayPK() {
    }

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


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TimeByDayPK)) {
			return false;
		}
		TimeByDayPK castOther = (TimeByDayPK)other;
		return 
			( this.timeId.equals(castOther.timeId) ) 
 && ( this.theDate.equals(castOther.theDate) ) 
 && ( this.theDay.equals(castOther.theDay) ) 
 && ( this.theMonth.equals(castOther.theMonth) ) 
 && ( this.theYear.equals(castOther.theYear) ) 
 && ( this.dayOfMonth.equals(castOther.dayOfMonth) ) 
 && ( this.weekOfYear.equals(castOther.weekOfYear) ) 
 && ( this.monthOfYear.equals(castOther.monthOfYear) ) 
 && ( this.quarter.equals(castOther.quarter) ) 
 && ( this.fiscalPeriod.equals(castOther.fiscalPeriod) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.timeId.hashCode() ;
 hash = hash * prime + this.theDate.hashCode() ;
 hash = hash * prime + this.theDay.hashCode() ;
 hash = hash * prime + this.theMonth.hashCode() ;
 hash = hash * prime + this.theYear.hashCode() ;
 hash = hash * prime + this.dayOfMonth.hashCode() ;
 hash = hash * prime + this.weekOfYear.hashCode() ;
 hash = hash * prime + this.monthOfYear.hashCode() ;
 hash = hash * prime + this.quarter.hashCode() ;
 hash = hash * prime + this.fiscalPeriod.hashCode() ;

		return hash;
    }
}