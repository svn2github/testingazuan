package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the product_class table.
 * 
 */
@Embeddable
public class ProductClassPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="product_class_id")
		private String productClassId;
		@Column(name="product_subcategory")
		private String productSubcategory;
		@Column(name="product_category")
		private String productCategory;
		@Column(name="product_department")
		private String productDepartment;
		@Column(name="product_family")
		private String productFamily;

    public ProductClassPK() {
    }

public String getProductClassId () {
	return this.productClassId;
}
public void setProductClassId (String productClassId) {
	this.productClassId = productClassId;
}


public String getProductSubcategory () {
	return this.productSubcategory;
}
public void setProductSubcategory (String productSubcategory) {
	this.productSubcategory = productSubcategory;
}


public String getProductCategory () {
	return this.productCategory;
}
public void setProductCategory (String productCategory) {
	this.productCategory = productCategory;
}


public String getProductDepartment () {
	return this.productDepartment;
}
public void setProductDepartment (String productDepartment) {
	this.productDepartment = productDepartment;
}


public String getProductFamily () {
	return this.productFamily;
}
public void setProductFamily (String productFamily) {
	this.productFamily = productFamily;
}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductClassPK)) {
			return false;
		}
		ProductClassPK castOther = (ProductClassPK)other;
		return 
			( this.productClassId.equals(castOther.productClassId) ) 
 && ( this.productSubcategory.equals(castOther.productSubcategory) ) 
 && ( this.productCategory.equals(castOther.productCategory) ) 
 && ( this.productDepartment.equals(castOther.productDepartment) ) 
 && ( this.productFamily.equals(castOther.productFamily) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.productClassId.hashCode() ;
 hash = hash * prime + this.productSubcategory.hashCode() ;
 hash = hash * prime + this.productCategory.hashCode() ;
 hash = hash * prime + this.productDepartment.hashCode() ;
 hash = hash * prime + this.productFamily.hashCode() ;

		return hash;
    }
}