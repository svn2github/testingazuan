package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the employee table.
 * 
 */
@Entity
@Table(name="employee")
public class Employee implements Serializable {

private static final long serialVersionUID = 1L;

public Employee() {
}
	
@EmbeddedId
private EmployeePK compId=null;

public EmployeePK getCompId () {
	return this.compId;
}

public void setCompId (EmployeePK compId) {
	this.compId = compId;
}


	@ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
@JoinColumn( name="position_id", insertable = false, updatable = false)
	private Position positionId;


public Position getPositionId () {
	return this.positionId;
}

public void setPositionId (Position positionId) {
	this.positionId = positionId;
} 
}