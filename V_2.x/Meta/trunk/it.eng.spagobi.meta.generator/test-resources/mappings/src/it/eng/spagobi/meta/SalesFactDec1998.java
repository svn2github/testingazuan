package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the sales_fact_dec_1998 table.
 * 
 */
@Entity
@Table(name="sales_fact_dec_1998")
public class SalesFactDec1998 implements Serializable {

private static final long serialVersionUID = 1L;

public SalesFactDec1998() {
}
	
@EmbeddedId
private SalesFactDec1998PK compId=null;

public SalesFactDec1998PK getCompId () {
	return this.compId;
}

public void setCompId (SalesFactDec1998PK compId) {
	this.compId = compId;
}


}