package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the position table.
 * 
 */
@Entity
@Table(name="position")
public class EmployeeClosurePosition implements Serializable {

private static final long serialVersionUID = 1L;

public EmployeeClosurePosition() {
}
	
@EmbeddedId
private EmployeeClosurePositionPK compId=null;

public EmployeeClosurePositionPK getCompId () {
	return this.compId;
}

public void setCompId (EmployeeClosurePositionPK compId) {
	this.compId = compId;
}


}