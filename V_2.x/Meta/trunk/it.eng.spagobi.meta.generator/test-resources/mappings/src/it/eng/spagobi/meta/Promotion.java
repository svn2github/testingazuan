package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the promotion table.
 * 
 */
@Entity
@Table(name="promotion")
public class Promotion implements Serializable {

private static final long serialVersionUID = 1L;

public Promotion() {
}
	
@EmbeddedId
private PromotionPK compId=null;

public PromotionPK getCompId () {
	return this.compId;
}

public void setCompId (PromotionPK compId) {
	this.compId = compId;
}


}