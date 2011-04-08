package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the store table.
 * 
 */
@Embeddable
public class StorePK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="store_id")
		private String storeId;
		@Column(name="store_type")
		private String storeType;
		@Column(name="region_id")
		private String regionId;
		@Column(name="sales_region_id")
		private String salesRegionId;
		@Column(name="store_name")
		private String storeName;
		@Column(name="store_number")
		private String storeNumber;
		@Column(name="store_street_address")
		private String storeStreetAddress;
		@Column(name="store_city")
		private String storeCity;
		@Column(name="store_state")
		private String storeState;
		@Column(name="store_postal_code")
		private String storePostalCode;
		@Column(name="store_country")
		private String storeCountry;
		@Column(name="store_manager")
		private String storeManager;
		@Column(name="store_phone")
		private String storePhone;
		@Column(name="store_fax")
		private String storeFax;
		@Column(name="first_opened_date")
		private String firstOpenedDate;
		@Column(name="last_remodel_date")
		private String lastRemodelDate;
		@Column(name="store_sqft")
		private String storeSqft;
		@Column(name="grocery_sqft")
		private String grocerySqft;
		@Column(name="frozen_sqft")
		private String frozenSqft;
		@Column(name="meat_sqft")
		private String meatSqft;
		@Column(name="coffee_bar")
		private String coffeeBar;
		@Column(name="video_store")
		private String videoStore;
		@Column(name="salad_bar")
		private String saladBar;
		@Column(name="prepared_food")
		private String preparedFood;
		@Column(name="florist")
		private String florist;

    public StorePK() {
    }

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


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof StorePK)) {
			return false;
		}
		StorePK castOther = (StorePK)other;
		return 
			( this.storeId.equals(castOther.storeId) ) 
 && ( this.storeType.equals(castOther.storeType) ) 
 && ( this.regionId.equals(castOther.regionId) ) 
 && ( this.salesRegionId.equals(castOther.salesRegionId) ) 
 && ( this.storeName.equals(castOther.storeName) ) 
 && ( this.storeNumber.equals(castOther.storeNumber) ) 
 && ( this.storeStreetAddress.equals(castOther.storeStreetAddress) ) 
 && ( this.storeCity.equals(castOther.storeCity) ) 
 && ( this.storeState.equals(castOther.storeState) ) 
 && ( this.storePostalCode.equals(castOther.storePostalCode) ) 
 && ( this.storeCountry.equals(castOther.storeCountry) ) 
 && ( this.storeManager.equals(castOther.storeManager) ) 
 && ( this.storePhone.equals(castOther.storePhone) ) 
 && ( this.storeFax.equals(castOther.storeFax) ) 
 && ( this.firstOpenedDate.equals(castOther.firstOpenedDate) ) 
 && ( this.lastRemodelDate.equals(castOther.lastRemodelDate) ) 
 && ( this.storeSqft.equals(castOther.storeSqft) ) 
 && ( this.grocerySqft.equals(castOther.grocerySqft) ) 
 && ( this.frozenSqft.equals(castOther.frozenSqft) ) 
 && ( this.meatSqft.equals(castOther.meatSqft) ) 
 && ( this.coffeeBar.equals(castOther.coffeeBar) ) 
 && ( this.videoStore.equals(castOther.videoStore) ) 
 && ( this.saladBar.equals(castOther.saladBar) ) 
 && ( this.preparedFood.equals(castOther.preparedFood) ) 
 && ( this.florist.equals(castOther.florist) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.storeId.hashCode() ;
 hash = hash * prime + this.storeType.hashCode() ;
 hash = hash * prime + this.regionId.hashCode() ;
 hash = hash * prime + this.salesRegionId.hashCode() ;
 hash = hash * prime + this.storeName.hashCode() ;
 hash = hash * prime + this.storeNumber.hashCode() ;
 hash = hash * prime + this.storeStreetAddress.hashCode() ;
 hash = hash * prime + this.storeCity.hashCode() ;
 hash = hash * prime + this.storeState.hashCode() ;
 hash = hash * prime + this.storePostalCode.hashCode() ;
 hash = hash * prime + this.storeCountry.hashCode() ;
 hash = hash * prime + this.storeManager.hashCode() ;
 hash = hash * prime + this.storePhone.hashCode() ;
 hash = hash * prime + this.storeFax.hashCode() ;
 hash = hash * prime + this.firstOpenedDate.hashCode() ;
 hash = hash * prime + this.lastRemodelDate.hashCode() ;
 hash = hash * prime + this.storeSqft.hashCode() ;
 hash = hash * prime + this.grocerySqft.hashCode() ;
 hash = hash * prime + this.frozenSqft.hashCode() ;
 hash = hash * prime + this.meatSqft.hashCode() ;
 hash = hash * prime + this.coffeeBar.hashCode() ;
 hash = hash * prime + this.videoStore.hashCode() ;
 hash = hash * prime + this.saladBar.hashCode() ;
 hash = hash * prime + this.preparedFood.hashCode() ;
 hash = hash * prime + this.florist.hashCode() ;

		return hash;
    }
}