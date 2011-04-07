package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the sales_fact_1997 table.
 * 
 */
@Entity
@Table(name="sales_fact_1997")
public class SalesFact1997 implements Serializable {

private static final long serialVersionUID = 1L;

public SalesFact1997() {
}
	
@EmbeddedId
private SalesFact1997PK compId=null;

public SalesFact1997PK getCompId () {
	return this.compId;
}

public void setCompId (SalesFact1997PK compId) {
	this.compId = compId;
}


}