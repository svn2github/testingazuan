package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the product table.
 * 
 */
@Embeddable
public class ProdottoEstesoProductPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="product_class_id")
		private String productClassId;
		@Column(name="product_id")
		private String productId;
		@Column(name="brand_name")
		private String brandName;
		@Column(name="product_name")
		private String productName;
		@Column(name="gross_weight")
		private String grossWeight;
		@Column(name="net_weight")
		private String netWeight;
		@Column(name="recyclable_package")
		private String recyclablePackage;
		@Column(name="low_fat")
		private String lowFat;
		@Column(name="units_per_case")
		private String unitsPerCase;
		@Column(name="cases_per_pallet")
		private String casesPerPallet;
		@Column(name="shelf_width")
		private String shelfWidth;
		@Column(name="shelf_height")
		private String shelfHeight;
		@Column(name="shelf_depth")
		private String shelfDepth;

    public ProdottoEstesoProductPK() {
    }

public String getProductClassId () {
	return this.productClassId;
}
public void setProductClassId (String productClassId) {
	this.productClassId = productClassId;
}


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


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProdottoEstesoProductPK)) {
			return false;
		}
		ProdottoEstesoProductPK castOther = (ProdottoEstesoProductPK)other;
		return 
			( this.productClassId.equals(castOther.productClassId) ) 
 && ( this.productId.equals(castOther.productId) ) 
 && ( this.brandName.equals(castOther.brandName) ) 
 && ( this.productName.equals(castOther.productName) ) 
 && ( this.grossWeight.equals(castOther.grossWeight) ) 
 && ( this.netWeight.equals(castOther.netWeight) ) 
 && ( this.recyclablePackage.equals(castOther.recyclablePackage) ) 
 && ( this.lowFat.equals(castOther.lowFat) ) 
 && ( this.unitsPerCase.equals(castOther.unitsPerCase) ) 
 && ( this.casesPerPallet.equals(castOther.casesPerPallet) ) 
 && ( this.shelfWidth.equals(castOther.shelfWidth) ) 
 && ( this.shelfHeight.equals(castOther.shelfHeight) ) 
 && ( this.shelfDepth.equals(castOther.shelfDepth) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.productClassId.hashCode() ;
 hash = hash * prime + this.productId.hashCode() ;
 hash = hash * prime + this.brandName.hashCode() ;
 hash = hash * prime + this.productName.hashCode() ;
 hash = hash * prime + this.grossWeight.hashCode() ;
 hash = hash * prime + this.netWeight.hashCode() ;
 hash = hash * prime + this.recyclablePackage.hashCode() ;
 hash = hash * prime + this.lowFat.hashCode() ;
 hash = hash * prime + this.unitsPerCase.hashCode() ;
 hash = hash * prime + this.casesPerPallet.hashCode() ;
 hash = hash * prime + this.shelfWidth.hashCode() ;
 hash = hash * prime + this.shelfHeight.hashCode() ;
 hash = hash * prime + this.shelfDepth.hashCode() ;

		return hash;
    }
}