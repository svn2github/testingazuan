package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the region table.
 * 
 */
@Entity
@Table(name="region")
public class Region implements Serializable {

private static final long serialVersionUID = 1L;

public Region() {
}
	
@EmbeddedId
private RegionPK compId=null;

public RegionPK getCompId () {
	return this.compId;
}

public void setCompId (RegionPK compId) {
	this.compId = compId;
}


}