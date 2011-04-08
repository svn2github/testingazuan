package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the store table.
 * 
 */
@Entity
@Table(name="store")
public class Store implements Serializable {

private static final long serialVersionUID = 1L;

public Store() {
}
	
@EmbeddedId
private StorePK compId=null;

public StorePK getCompId () {
	return this.compId;
}

public void setCompId (StorePK compId) {
	this.compId = compId;
}


}