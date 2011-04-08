package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the product_class table.
 * 
 */
@Entity
@Table(name="product_class")
public class ProductClass implements Serializable {

private static final long serialVersionUID = 1L;

public ProductClass() {
}
	
@EmbeddedId
private ProductClassPK compId=null;

public ProductClassPK getCompId () {
	return this.compId;
}

public void setCompId (ProductClassPK compId) {
	this.compId = compId;
}


}