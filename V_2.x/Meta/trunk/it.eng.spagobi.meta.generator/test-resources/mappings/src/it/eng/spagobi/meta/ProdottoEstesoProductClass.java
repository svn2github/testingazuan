package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the product_class table.
 * 
 */
@Entity
@Table(name="product_class")
public class ProdottoEstesoProductClass implements Serializable {

private static final long serialVersionUID = 1L;

public ProdottoEstesoProductClass() {
}
	
@EmbeddedId
private ProdottoEstesoProductClassPK compId=null;

public ProdottoEstesoProductClassPK getCompId () {
	return this.compId;
}

public void setCompId (ProdottoEstesoProductClassPK compId) {
	this.compId = compId;
}


}