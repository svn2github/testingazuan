package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the product table.
 * 
 */
@Entity
@Table(name="product")
public class Product implements Serializable {

private static final long serialVersionUID = 1L;

public Product() {
}
	
@EmbeddedId
private ProductPK compId=null;

public ProductPK getCompId () {
	return this.compId;
}

public void setCompId (ProductPK compId) {
	this.compId = compId;
}


}