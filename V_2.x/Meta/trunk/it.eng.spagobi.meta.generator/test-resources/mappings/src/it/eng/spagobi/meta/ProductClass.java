package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the product_class table.
 * 
 */
@Entity
@Table(name="product_class")
public class ProductClass implements Serializable {

private static final long serialVersionUID = 1L;

public ProductClass() {
}
	
	@Column(name="product_class_id")
private String productClassId=null;
	@Column(name="product_subcategory")
private String productSubcategory=null;
	@Column(name="product_category")
private String productCategory=null;
	@Column(name="product_department")
private String productDepartment=null;
	@Column(name="product_family")
private String productFamily=null;

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

}