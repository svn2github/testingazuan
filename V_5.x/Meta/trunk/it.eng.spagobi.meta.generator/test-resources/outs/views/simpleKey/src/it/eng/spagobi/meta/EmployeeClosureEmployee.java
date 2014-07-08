package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the employee table.
 * 
 */
@Entity
@Table(name="employee")
public class EmployeeClosureEmployee implements Serializable {

private static final long serialVersionUID = 1L;

public EmployeeClosureEmployee() {
}
	
@EmbeddedId
private EmployeeClosureEmployeePK compId=null;

public EmployeeClosureEmployeePK getCompId () {
	return this.compId;
}

public void setCompId (EmployeeClosureEmployeePK compId) {
	this.compId = compId;
}


}