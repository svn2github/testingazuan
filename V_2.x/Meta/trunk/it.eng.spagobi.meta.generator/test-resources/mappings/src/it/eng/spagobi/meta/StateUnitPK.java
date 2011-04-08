package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the state_unit table.
 * 
 */
@Embeddable
public class StateUnitPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="ID")
		private String id;
		@Column(name="SALES_UNIT")
		private String salesUnit;
		@Column(name="PROD_FAMILY")
		private String prodFamily;

    public StateUnitPK() {
    }

public String getId () {
	return this.id;
}
public void setId (String id) {
	this.id = id;
}


public String getSalesUnit () {
	return this.salesUnit;
}
public void setSalesUnit (String salesUnit) {
	this.salesUnit = salesUnit;
}


public String getProdFamily () {
	return this.prodFamily;
}
public void setProdFamily (String prodFamily) {
	this.prodFamily = prodFamily;
}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof StateUnitPK)) {
			return false;
		}
		StateUnitPK castOther = (StateUnitPK)other;
		return 
			( this.id.equals(castOther.id) ) 
 && ( this.salesUnit.equals(castOther.salesUnit) ) 
 && ( this.prodFamily.equals(castOther.prodFamily) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.id.hashCode() ;
 hash = hash * prime + this.salesUnit.hashCode() ;
 hash = hash * prime + this.prodFamily.hashCode() ;

		return hash;
    }
}