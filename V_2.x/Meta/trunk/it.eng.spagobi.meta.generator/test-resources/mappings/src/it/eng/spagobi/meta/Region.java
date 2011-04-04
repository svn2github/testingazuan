package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the region table.
 * 
 */
@Entity
@Table(name="region")
public class Region implements Serializable {

private static final long serialVersionUID = 1L;

public Region() {
}
	
	@Column(name="region_id")
private String regionId=null;
	@Column(name="sales_city")
private String salesCity=null;
	@Column(name="sales_state_province")
private String salesStateProvince=null;
	@Column(name="sales_district")
private String salesDistrict=null;
	@Column(name="sales_region")
private String salesRegion=null;
	@Column(name="sales_country")
private String salesCountry=null;
	@Column(name="sales_district_id")
private String salesDistrictId=null;

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

}