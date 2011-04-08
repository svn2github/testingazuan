package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the state_unit table.
 * 
 */
@Entity
@Table(name="state_unit")
public class StateUnit implements Serializable {

private static final long serialVersionUID = 1L;

public StateUnit() {
}
	
@EmbeddedId
private StateUnitPK compId=null;

public StateUnitPK getCompId () {
	return this.compId;
}

public void setCompId (StateUnitPK compId) {
	this.compId = compId;
}


}