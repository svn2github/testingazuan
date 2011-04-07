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
	
@EmbeddedId
private TimeByDayPK compId=null;

public TimeByDayPK getCompId () {
	return this.compId;
}

public void setCompId (TimeByDayPK compId) {
	this.compId = compId;
}


}