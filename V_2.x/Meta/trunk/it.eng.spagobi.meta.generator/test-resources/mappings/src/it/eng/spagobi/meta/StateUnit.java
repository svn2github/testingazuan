package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the state_unit table.
 * 
 */
@Entity
@Table(name="state_unit")
public class StateUnit implements Serializable {

private static final long serialVersionUID = 1L;

public StateUnit() {
}
	
	@Column(name="ID")
private String id=null;
	@Column(name="SALES_UNIT")
private String salesUnit=null;
	@Column(name="PROD_FAMILY")
private String prodFamily=null;

public String getId () {
	return this.id;
}
public void setId (String id) {
	this.id = id;
}
public String getSalesUnit () {
	return this.salesUnit;
}
public void setSalesUnit (String salesUnit) {
	this.salesUnit = salesUnit;
}
public String getProdFamily () {
	return this.prodFamily;
}
public void setProdFamily (String prodFamily) {
	this.prodFamily = prodFamily;
}

}