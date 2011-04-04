package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the product_class table.
 * 
 */
@Entity
@Table(name="product_class")
public class ProdottoEstesoProductClass implements Serializable {

private static final long serialVersionUID = 1L;

public ProdottoEstesoProductClass() {
}
	
	@Column(name="product_category")
private String productCategory=null;
	@Column(name="product_department")
private String productDepartment=null;
	@Column(name="product_family")
private String productFamily=null;

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