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
 && ( this.positionTitle.equals(castOther.positionTitle) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.positionId.hashCode() ;
 hash = hash * prime + this.positionTitle.hashCode() ;

		return hash;
    }
}