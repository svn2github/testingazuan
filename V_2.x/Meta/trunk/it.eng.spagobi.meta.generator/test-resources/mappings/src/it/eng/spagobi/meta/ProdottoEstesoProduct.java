package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the product table.
 * 
 */
@Entity
@Table(name="product")
public class ProdottoEstesoProduct implements Serializable {

private static final long serialVersionUID = 1L;

public ProdottoEstesoProduct() {
}
	
	@Column(name="product_id")
private String productId=null;
	@Column(name="brand_name")
private String brandName=null;
	@Column(name="product_name")
private String productName=null;
	@Column(name="gross_weight")
private String grossWeight=null;
	@Column(name="net_weight")
private String netWeight=null;
	@Column(name="recyclable_package")
private String recyclablePackage=null;
	@Column(name="low_fat")
private String lowFat=null;
	@Column(name="units_per_case")
private String unitsPerCase=null;
	@Column(name="cases_per_pallet")
private String casesPerPallet=null;
	@Column(name="shelf_width")
private String shelfWidth=null;
	@Column(name="shelf_height")
private String shelfHeight=null;
	@Column(name="shelf_depth")
private String shelfDepth=null;

public String getProductId () {
	return this.productId;
}
public void setProductId (String productId) {
	this.productId = productId;
}
public String getBrandName () {
	return this.brandName;
}
public void setBrandName (String brandName) {
	this.brandName = brandName;
}
public String getProductName () {
	return this.productName;
}
public void setProductName (String productName) {
	this.productName = productName;
}
public String getGrossWeight () {
	return this.grossWeight;
}
public void setGrossWeight (String grossWeight) {
	this.grossWeight = grossWeight;
}
public String getNetWeight () {
	return this.netWeight;
}
public void setNetWeight (String netWeight) {
	this.netWeight = netWeight;
}
public String getRecyclablePackage () {
	return this.recyclablePackage;
}
public void setRecyclablePackage (String recyclablePackage) {
	this.recyclablePackage = recyclablePackage;
}
public String getLowFat () {
	return this.lowFat;
}
public void setLowFat (String lowFat) {
	this.lowFat = lowFat;
}
public String getUnitsPerCase () {
	return this.unitsPerCase;
}
public void setUnitsPerCase (String unitsPerCase) {
	this.unitsPerCase = unitsPerCase;
}
public String getCasesPerPallet () {
	return this.casesPerPallet;
}
public void setCasesPerPallet (String casesPerPallet) {
	this.casesPerPallet = casesPerPallet;
}
public String getShelfWidth () {
	return this.shelfWidth;
}
public void setShelfWidth (String shelfWidth) {
	this.shelfWidth = shelfWidth;
}
public String getShelfHeight () {
	return this.shelfHeight;
}
public void setShelfHeight (String shelfHeight) {
	this.shelfHeight = shelfHeight;
}
public String getShelfDepth () {
	return this.shelfDepth;
}
public void setShelfDepth (String shelfDepth) {
	this.shelfDepth = shelfDepth;
}

}