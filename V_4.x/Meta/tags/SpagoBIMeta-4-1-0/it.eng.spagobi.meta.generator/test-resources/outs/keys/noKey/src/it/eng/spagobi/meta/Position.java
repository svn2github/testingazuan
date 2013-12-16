package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


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


}