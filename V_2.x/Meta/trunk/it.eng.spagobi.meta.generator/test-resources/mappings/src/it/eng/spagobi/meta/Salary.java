package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the salary table.
 * 
 */
@Entity
@Table(name="salary")
public class Salary implements Serializable {

private static final long serialVersionUID = 1L;

public Salary() {
}
	
@EmbeddedId
private SalaryPK compId=null;

public SalaryPK getCompId () {
	return this.compId;
}

public void setCompId (SalaryPK compId) {
	this.compId = compId;
}


}