package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * This class refers to the position table.
 * 
 */
@Entity
@Table(name="position")
public class Position implements Serializable {

private static final long serialVersionUID = 1L;

public Position() {
}
	
@EmbeddedId
private PositionPK compId=null;
	@Column(name="pay_type")
private String payType=null;
	@Column(name="management_role")
private String managementRole=null;

public PositionPK getCompId () {
	return this.compId;
}

public void setCompId (PositionPK compId) {
	this.compId = compId;
}

public String getPayType () {
	return this.payType;
}
public void setPayType (String payType) {
	this.payType = payType;
}
public String getManagementRole () {
	return this.managementRole;
}
public void setManagementRole (String managementRole) {
	this.managementRole = managementRole;
}

			@OneToMany(mappedBy="positionId", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	private java.util.Set<Employee> brEmployeePositions;


public java.util.Set<Employee> getBrEmployeePositions () {
	return this.brEmployeePositions;
}

public void setBrEmployeePositions (java.util.Set<Employee> brEmployeePositions) {
	this.brEmployeePositions = brEmployeePositions;
} 
}