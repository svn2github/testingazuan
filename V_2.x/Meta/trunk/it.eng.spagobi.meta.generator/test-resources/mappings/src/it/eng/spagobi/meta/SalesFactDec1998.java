package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the sales_fact_dec_1998 table.
 * 
 */
@Entity
@Table(name="sales_fact_dec_1998")
public class SalesFactDec1998 implements Serializable {

private static final long serialVersionUID = 1L;

public SalesFactDec1998() {
}
	
	@Column(name="product_id")
private String productId=null;
	@Column(name="time_id")
private String timeId=null;
	@Column(name="customer_id")
private String customerId=null;
	@Column(name="promotion_id")
private String promotionId=null;
	@Column(name="store_id")
private String storeId=null;
	@Column(name="store_sales")
private String storeSales=null;
	@Column(name="store_cost")
private String storeCost=null;
	@Column(name="unit_sales")
private String unitSales=null;

public String getProductId () {
	return this.productId;
}
public void setProductId (String productId) {
	this.productId = productId;
}
public String getTimeId () {
	return this.timeId;
}
public void setTimeId (String timeId) {
	this.timeId = timeId;
}
public String getCustomerId () {
	return this.customerId;
}
public void setCustomerId (String customerId) {
	this.customerId = customerId;
}
public String getPromotionId () {
	return this.promotionId;
}
public void setPromotionId (String promotionId) {
	this.promotionId = promotionId;
}
public String getStoreId () {
	return this.storeId;
}
public void setStoreId (String storeId) {
	this.storeId = storeId;
}
public String getStoreSales () {
	return this.storeSales;
}
public void setStoreSales (String storeSales) {
	this.storeSales = storeSales;
}
public String getStoreCost () {
	return this.storeCost;
}
public void setStoreCost (String storeCost) {
	this.storeCost = storeCost;
}
public String getUnitSales () {
	return this.unitSales;
}
public void setUnitSales (String unitSales) {
	this.unitSales = unitSales;
}

}