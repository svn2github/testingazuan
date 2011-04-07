package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the product table.
 * 
 */
@Entity
@Table(name="product")
public class ProdottoEstesoProduct implements Serializable {

private static final long serialVersionUID = 1L;

public ProdottoEstesoProduct() {
}
	
@EmbeddedId
private ProdottoEstesoProductPK compId=null;

public ProdottoEstesoProductPK getCompId () {
	return this.compId;
}

public void setCompId (ProdottoEstesoProductPK compId) {
	this.compId = compId;
}


}