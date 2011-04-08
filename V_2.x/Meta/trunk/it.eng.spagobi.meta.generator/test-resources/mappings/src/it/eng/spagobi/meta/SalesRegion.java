package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the sales_region table.
 * 
 */
@Entity
@Table(name="sales_region")
public class SalesRegion implements Serializable {

private static final long serialVersionUID = 1L;

public SalesRegion() {
}
	
	@Id
	@Column(name="region_id")
private String regionId=null;
	@Column(name="sales_state")
private String salesState=null;
	@Column(name="sales_region")
private String salesRegion=null;
	@Column(name="sales_subregion")
private String salesSubregion=null;
	@Column(name="sales_area")
private String salesArea=null;
	@Column(name="region_desc")
private String regionDesc=null;

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

}