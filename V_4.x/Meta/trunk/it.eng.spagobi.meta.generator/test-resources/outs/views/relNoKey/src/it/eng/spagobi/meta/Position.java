package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * This class refers to the position table.
 * 
 */
@Entity
@Table(name="position")
public class Position implements Serializable {

private static final long serialVersionUID = 1L;

public Position() {
}
	
@EmbeddedId
private PositionPK compId=null;

public PositionPK getCompId () {
	return this.compId;
}

public void setCompId (PositionPK compId) {
	this.compId = compId;
}


			@OneToMany(mappedBy="positionId", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	private java.util.Set<Employee> brEmployeePositions;


public java.util.Set<Employee> getBrEmployeePositions () {
	return this.brEmployeePositions;
}

public void setBrEmployeePositions (java.util.Set<Employee> brEmployeePositions) {
	this.brEmployeePositions = brEmployeePositions;
} 
}