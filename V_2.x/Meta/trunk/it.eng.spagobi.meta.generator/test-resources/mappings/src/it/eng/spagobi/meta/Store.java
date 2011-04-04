package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the store table.
 * 
 */
@Entity
@Table(name="store")
public class Store implements Serializable {

private static final long serialVersionUID = 1L;

public Store() {
}
	
	@Column(name="store_id")
private String storeId=null;
	@Column(name="store_type")
private String storeType=null;
	@Column(name="region_id")
private String regionId=null;
	@Column(name="sales_region_id")
private String salesRegionId=null;
	@Column(name="store_name")
private String storeName=null;
	@Column(name="store_number")
private String storeNumber=null;
	@Column(name="store_street_address")
private String storeStreetAddress=null;
	@Column(name="store_city")
private String storeCity=null;
	@Column(name="store_state")
private String storeState=null;
	@Column(name="store_postal_code")
private String storePostalCode=null;
	@Column(name="store_country")
private String storeCountry=null;
	@Column(name="store_manager")
private String storeManager=null;
	@Column(name="store_phone")
private String storePhone=null;
	@Column(name="store_fax")
private String storeFax=null;
	@Column(name="first_opened_date")
private String firstOpenedDate=null;
	@Column(name="last_remodel_date")
private String lastRemodelDate=null;
	@Column(name="store_sqft")
private String storeSqft=null;
	@Column(name="grocery_sqft")
private String grocerySqft=null;
	@Column(name="frozen_sqft")
private String frozenSqft=null;
	@Column(name="meat_sqft")
private String meatSqft=null;
	@Column(name="coffee_bar")
private String coffeeBar=null;
	@Column(name="video_store")
private String videoStore=null;
	@Column(name="salad_bar")
private String saladBar=null;
	@Column(name="prepared_food")
private String preparedFood=null;
	@Column(name="florist")
private String florist=null;

public String getStoreId () {
	return this.storeId;
}
public void setStoreId (String storeId) {
	this.storeId = storeId;
}
public String getStoreType () {
	return this.storeType;
}
public void setStoreType (String storeType) {
	this.storeType = storeType;
}
public String getRegionId () {
	return this.regionId;
}
public void setRegionId (String regionId) {
	this.regionId = regionId;
}
public String getSalesRegionId () {
	return this.salesRegionId;
}
public void setSalesRegionId (String salesRegionId) {
	this.salesRegionId = salesRegionId;
}
public String getStoreName () {
	return this.storeName;
}
public void setStoreName (String storeName) {
	this.storeName = storeName;
}
public String getStoreNumber () {
	return this.storeNumber;
}
public void setStoreNumber (String storeNumber) {
	this.storeNumber = storeNumber;
}
public String getStoreStreetAddress () {
	return this.storeStreetAddress;
}
public void setStoreStreetAddress (String storeStreetAddress) {
	this.storeStreetAddress = storeStreetAddress;
}
public String getStoreCity () {
	return this.storeCity;
}
public void setStoreCity (String storeCity) {
	this.storeCity = storeCity;
}
public String getStoreState () {
	return this.storeState;
}
public void setStoreState (String storeState) {
	this.storeState = storeState;
}
public String getStorePostalCode () {
	return this.storePostalCode;
}
public void setStorePostalCode (String storePostalCode) {
	this.storePostalCode = storePostalCode;
}
public String getStoreCountry () {
	return this.storeCountry;
}
public void setStoreCountry (String storeCountry) {
	this.storeCountry = storeCountry;
}
public String getStoreManager () {
	return this.storeManager;
}
public void setStoreManager (String storeManager) {
	this.storeManager = storeManager;
}
public String getStorePhone () {
	return this.storePhone;
}
public void setStorePhone (String storePhone) {
	this.storePhone = storePhone;
}
public String getStoreFax () {
	return this.storeFax;
}
public void setStoreFax (String storeFax) {
	this.storeFax = storeFax;
}
public String getFirstOpenedDate () {
	return this.firstOpenedDate;
}
public void setFirstOpenedDate (String firstOpenedDate) {
	this.firstOpenedDate = firstOpenedDate;
}
public String getLastRemodelDate () {
	return this.lastRemodelDate;
}
public void setLastRemodelDate (String lastRemodelDate) {
	this.lastRemodelDate = lastRemodelDate;
}
public String getStoreSqft () {
	return this.storeSqft;
}
public void setStoreSqft (String storeSqft) {
	this.storeSqft = storeSqft;
}
public String getGrocerySqft () {
	return this.grocerySqft;
}
public void setGrocerySqft (String grocerySqft) {
	this.grocerySqft = grocerySqft;
}
public String getFrozenSqft () {
	return this.frozenSqft;
}
public void setFrozenSqft (String frozenSqft) {
	this.frozenSqft = frozenSqft;
}
public String getMeatSqft () {
	return this.meatSqft;
}
public void setMeatSqft (String meatSqft) {
	this.meatSqft = meatSqft;
}
public String getCoffeeBar () {
	return this.coffeeBar;
}
public void setCoffeeBar (String coffeeBar) {
	this.coffeeBar = coffeeBar;
}
public String getVideoStore () {
	return this.videoStore;
}
public void setVideoStore (String videoStore) {
	this.videoStore = videoStore;
}
public String getSaladBar () {
	return this.saladBar;
}
public void setSaladBar (String saladBar) {
	this.saladBar = saladBar;
}
public String getPreparedFood () {
	return this.preparedFood;
}
public void setPreparedFood (String preparedFood) {
	this.preparedFood = preparedFood;
}
public String getFlorist () {
	return this.florist;
}
public void setFlorist (String florist) {
	this.florist = florist;
}

}