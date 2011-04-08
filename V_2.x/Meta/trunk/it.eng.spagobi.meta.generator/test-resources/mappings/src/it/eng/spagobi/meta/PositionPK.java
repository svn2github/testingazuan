package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the position table.
 * 
 */
@Embeddable
public class PositionPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="position_id")
		private String positionId;
		@Column(name="position_title")
		private String positionTitle;
		@Column(name="pay_type")
		private String payType;
		@Column(name="min_scale")
		private String minScale;
		@Column(name="max_scale")
		private String maxScale;
		@Column(name="management_role")
		private String managementRole;

    public PositionPK() {
    }

public String getPositionId () {
	return this.positionId;
}
public void setPositionId (String positionId) {
	this.positionId = positionId;
}


public String getPositionTitle () {
	return this.positionTitle;
}
public void setPositionTitle (String positionTitle) {
	this.positionTitle = positionTitle;
}


public String getPayType () {
	return this.payType;
}
public void setPayType (String payType) {
	this.payType = payType;
}


public String getMinScale () {
	return this.minScale;
}
public void setMinScale (String minScale) {
	this.minScale = minScale;
}


public String getMaxScale () {
	return this.maxScale;
}
public void setMaxScale (String maxScale) {
	this.maxScale = maxScale;
}


public String getManagementRole () {
	return this.managementRole;
}
public void setManagementRole (String managementRole) {
	this.managementRole = managementRole;
}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PositionPK)) {
			return false;
		}
		PositionPK castOther = (PositionPK)other;
		return 
			( this.positionId.equals(castOther.positionId) ) 
 && ( this.positionTitle.equals(castOther.positionTitle) ) 
 && ( this.payType.equals(castOther.payType) ) 
 && ( this.minScale.equals(castOther.minScale) ) 
 && ( this.maxScale.equals(castOther.maxScale) ) 
 && ( this.managementRole.equals(castOther.managementRole) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.positionId.hashCode() ;
 hash = hash * prime + this.positionTitle.hashCode() ;
 hash = hash * prime + this.payType.hashCode() ;
 hash = hash * prime + this.minScale.hashCode() ;
 hash = hash * prime + this.maxScale.hashCode() ;
 hash = hash * prime + this.managementRole.hashCode() ;

		return hash;
    }
}