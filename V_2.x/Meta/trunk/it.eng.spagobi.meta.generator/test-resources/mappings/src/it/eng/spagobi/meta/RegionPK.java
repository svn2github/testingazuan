package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the region table.
 * 
 */
@Embeddable
public class RegionPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="region_id")
		private String regionId;
		@Column(name="sales_city")
		private String salesCity;
		@Column(name="sales_state_province")
		private String salesStateProvince;
		@Column(name="sales_district")
		private String salesDistrict;
		@Column(name="sales_region")
		private String salesRegion;
		@Column(name="sales_country")
		private String salesCountry;
		@Column(name="sales_district_id")
		private String salesDistrictId;

    public RegionPK() {
    }

public String getRegionId () {
	return this.regionId;
}
public void setRegionId (String regionId) {
	this.regionId = regionId;
}


public String getSalesCity () {
	return this.salesCity;
}
public void setSalesCity (String salesCity) {
	this.salesCity = salesCity;
}


public String getSalesStateProvince () {
	return this.salesStateProvince;
}
public void setSalesStateProvince (String salesStateProvince) {
	this.salesStateProvince = salesStateProvince;
}


public String getSalesDistrict () {
	return this.salesDistrict;
}
public void setSalesDistrict (String salesDistrict) {
	this.salesDistrict = salesDistrict;
}


public String getSalesRegion () {
	return this.salesRegion;
}
public void setSalesRegion (String salesRegion) {
	this.salesRegion = salesRegion;
}


public String getSalesCountry () {
	return this.salesCountry;
}
public void setSalesCountry (String salesCountry) {
	this.salesCountry = salesCountry;
}


public String getSalesDistrictId () {
	return this.salesDistrictId;
}
public void setSalesDistrictId (String salesDistrictId) {
	this.salesDistrictId = salesDistrictId;
}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RegionPK)) {
			return false;
		}
		RegionPK castOther = (RegionPK)other;
		return 
			( this.regionId.equals(castOther.regionId) ) 
 && ( this.salesCity.equals(castOther.salesCity) ) 
 && ( this.salesStateProvince.equals(castOther.salesStateProvince) ) 
 && ( this.salesDistrict.equals(castOther.salesDistrict) ) 
 && ( this.salesRegion.equals(castOther.salesRegion) ) 
 && ( this.salesCountry.equals(castOther.salesCountry) ) 
 && ( this.salesDistrictId.equals(castOther.salesDistrictId) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.regionId.hashCode() ;
 hash = hash * prime + this.salesCity.hashCode() ;
 hash = hash * prime + this.salesStateProvince.hashCode() ;
 hash = hash * prime + this.salesDistrict.hashCode() ;
 hash = hash * prime + this.salesRegion.hashCode() ;
 hash = hash * prime + this.salesCountry.hashCode() ;
 hash = hash * prime + this.salesDistrictId.hashCode() ;

		return hash;
    }
}