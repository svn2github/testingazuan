package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the sales_fact_1998 table.
 * 
 */
@Entity
@Table(name="sales_fact_1998")
public class SalesFact1998 implements Serializable {

private static final long serialVersionUID = 1L;

public SalesFact1998() {
}
	
@EmbeddedId
private SalesFact1998PK compId=null;

public SalesFact1998PK getCompId () {
	return this.compId;
}

public void setCompId (SalesFact1998PK compId) {
	this.compId = compId;
}


}