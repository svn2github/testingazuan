package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the promotion table.
 * 
 */
@Entity
@Table(name="promotion")
public class Promotion implements Serializable {

private static final long serialVersionUID = 1L;

public Promotion() {
}
	
	@Column(name="promotion_id")
private String promotionId=null;
	@Column(name="promotion_district_id")
private String promotionDistrictId=null;
	@Column(name="promotion_name")
private String promotionName=null;
	@Column(name="media_type")
private String mediaType=null;
	@Column(name="cost")
private String cost=null;
	@Column(name="start_date")
private String startDate=null;
	@Column(name="end_date")
private String endDate=null;

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

}