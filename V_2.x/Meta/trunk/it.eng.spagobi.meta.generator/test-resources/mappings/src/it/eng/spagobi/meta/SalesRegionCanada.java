package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the sales_region_canada table.
 * 
 */
@Entity
@Table(name="sales_region_canada")
public class SalesRegionCanada implements Serializable {

private static final long serialVersionUID = 1L;

public SalesRegionCanada() {
}
	
@EmbeddedId
private SalesRegionCanadaPK compId=null;

public SalesRegionCanadaPK getCompId () {
	return this.compId;
}

public void setCompId (SalesRegionCanadaPK compId) {
	this.compId = compId;
}


}