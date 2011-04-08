package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the sales_region_canada table.
 * 
 */
@Embeddable
public class SalesRegionCanadaPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="region_id")
		private String regionId;
		@Column(name="sales_state")
		private String salesState;
		@Column(name="sales_region")
		private String salesRegion;
		@Column(name="sales_subregion")
		private String salesSubregion;
		@Column(name="sales_area")
		private String salesArea;
		@Column(name="region_desc")
		private String regionDesc;

    public SalesRegionCanadaPK() {
    }

public String getRegionId () {
	return this.regionId;
}
public void setRegionId (String regionId) {
	this.regionId = regionId;
}


public String getSalesState () {
	return this.salesState;
}
public void setSalesState (String salesState) {
	this.salesState = salesState;
}


public String getSalesRegion () {
	return this.salesRegion;
}
public void setSalesRegion (String salesRegion) {
	this.salesRegion = salesRegion;
}


public String getSalesSubregion () {
	return this.salesSubregion;
}
public void setSalesSubregion (String salesSubregion) {
	this.salesSubregion = salesSubregion;
}


public String getSalesArea () {
	return this.salesArea;
}
public void setSalesArea (String salesArea) {
	this.salesArea = salesArea;
}


public String getRegionDesc () {
	return this.regionDesc;
}
public void setRegionDesc (String regionDesc) {
	this.regionDesc = regionDesc;
}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SalesRegionCanadaPK)) {
			return false;
		}
		SalesRegionCanadaPK castOther = (SalesRegionCanadaPK)other;
		return 
			( this.regionId.equals(castOther.regionId) ) 
 && ( this.salesState.equals(castOther.salesState) ) 
 && ( this.salesRegion.equals(castOther.salesRegion) ) 
 && ( this.salesSubregion.equals(castOther.salesSubregion) ) 
 && ( this.salesArea.equals(castOther.salesArea) ) 
 && ( this.regionDesc.equals(castOther.regionDesc) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.regionId.hashCode() ;
 hash = hash * prime + this.salesState.hashCode() ;
 hash = hash * prime + this.salesRegion.hashCode() ;
 hash = hash * prime + this.salesSubregion.hashCode() ;
 hash = hash * prime + this.salesArea.hashCode() ;
 hash = hash * prime + this.regionDesc.hashCode() ;

		return hash;
    }
}