package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the employee table.
 * 
 */
@Embeddable
public class EmployeePK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="employee_id")
		private String employeeId;
		@Column(name="full_name")
		private String fullName;
		@Column(name="first_name")
		private String firstName;
		@Column(name="last_name")
		private String lastName;
		@Column(name="position_id")
		private String positionId;
		@Column(name="gender")
		private String gender;

    public EmployeePK() {
    }

public String getEmployeeId () {
	return this.employeeId;
}
public void setEmployeeId (String employeeId) {
	this.employeeId = employeeId;
}


public String getFullName () {
	return this.fullName;
}
public void setFullName (String fullName) {
	this.fullName = fullName;
}


public String getFirstName () {
	return this.firstName;
}
public void setFirstName (String firstName) {
	this.firstName = firstName;
}


public String getLastName () {
	return this.lastName;
}
public void setLastName (String lastName) {
	this.lastName = lastName;
}


public String getPositionId () {
	return this.positionId;
}
public void setPositionId (String positionId) {
	this.positionId = positionId;
}


public String getGender () {
	return this.gender;
}
public void setGender (String gender) {
	this.gender = gender;
}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EmployeePK)) {
			return false;
		}
		EmployeePK castOther = (EmployeePK)other;
		return 
			( this.employeeId.equals(castOther.employeeId) ) 
 && ( this.fullName.equals(castOther.fullName) ) 
 && ( this.firstName.equals(castOther.firstName) ) 
 && ( this.lastName.equals(castOther.lastName) ) 
 && ( this.positionId.equals(castOther.positionId) ) 
 && ( this.gender.equals(castOther.gender) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.employeeId.hashCode() ;
 hash = hash * prime + this.fullName.hashCode() ;
 hash = hash * prime + this.firstName.hashCode() ;
 hash = hash * prime + this.lastName.hashCode() ;
 hash = hash * prime + this.positionId.hashCode() ;
 hash = hash * prime + this.gender.hashCode() ;

		return hash;
    }
}