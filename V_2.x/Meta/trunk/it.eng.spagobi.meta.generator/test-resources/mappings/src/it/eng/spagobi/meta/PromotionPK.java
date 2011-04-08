package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the promotion table.
 * 
 */
@Embeddable
public class PromotionPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="promotion_id")
		private String promotionId;
		@Column(name="promotion_district_id")
		private String promotionDistrictId;
		@Column(name="promotion_name")
		private String promotionName;
		@Column(name="media_type")
		private String mediaType;
		@Column(name="cost")
		private String cost;
		@Column(name="start_date")
		private String startDate;
		@Column(name="end_date")
		private String endDate;

    public PromotionPK() {
    }

public String getPromotionId () {
	return this.promotionId;
}
public void setPromotionId (String promotionId) {
	this.promotionId = promotionId;
}


public String getPromotionDistrictId () {
	return this.promotionDistrictId;
}
public void setPromotionDistrictId (String promotionDistrictId) {
	this.promotionDistrictId = promotionDistrictId;
}


public String getPromotionName () {
	return this.promotionName;
}
public void setPromotionName (String promotionName) {
	this.promotionName = promotionName;
}


public String getMediaType () {
	return this.mediaType;
}
public void setMediaType (String mediaType) {
	this.mediaType = mediaType;
}


public String getCost () {
	return this.cost;
}
public void setCost (String cost) {
	this.cost = cost;
}


public String getStartDate () {
	return this.startDate;
}
public void setStartDate (String startDate) {
	this.startDate = startDate;
}


public String getEndDate () {
	return this.endDate;
}
public void setEndDate (String endDate) {
	this.endDate = endDate;
}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PromotionPK)) {
			return false;
		}
		PromotionPK castOther = (PromotionPK)other;
		return 
			( this.promotionId.equals(castOther.promotionId) ) 
 && ( this.promotionDistrictId.equals(castOther.promotionDistrictId) ) 
 && ( this.promotionName.equals(castOther.promotionName) ) 
 && ( this.mediaType.equals(castOther.mediaType) ) 
 && ( this.cost.equals(castOther.cost) ) 
 && ( this.startDate.equals(castOther.startDate) ) 
 && ( this.endDate.equals(castOther.endDate) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.promotionId.hashCode() ;
 hash = hash * prime + this.promotionDistrictId.hashCode() ;
 hash = hash * prime + this.promotionName.hashCode() ;
 hash = hash * prime + this.mediaType.hashCode() ;
 hash = hash * prime + this.cost.hashCode() ;
 hash = hash * prime + this.startDate.hashCode() ;
 hash = hash * prime + this.endDate.hashCode() ;

		return hash;
    }
}