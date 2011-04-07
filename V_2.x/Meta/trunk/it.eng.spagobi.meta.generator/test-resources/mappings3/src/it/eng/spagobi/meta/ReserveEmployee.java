package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the reserve_employee table.
 * 
 */
@Entity
@Table(name="reserve_employee")
public class ReserveEmployee implements Serializable {

private static final long serialVersionUID = 1L;

public ReserveEmployee() {
}
	
@EmbeddedId
private ReserveEmployeePK compId=null;

public ReserveEmployeePK getCompId () {
	return this.compId;
}

public void setCompId (ReserveEmployeePK compId) {
	this.compId = compId;
}


}