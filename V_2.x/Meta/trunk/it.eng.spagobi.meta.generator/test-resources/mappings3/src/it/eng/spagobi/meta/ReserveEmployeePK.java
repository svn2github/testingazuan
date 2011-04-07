package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the reserve_employee table.
 * 
 */
@Embeddable
public class ReserveEmployeePK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="employee_id")
		private java.lang.String employeeId;
		@Column(name="full_name")
		private java.lang.String fullName;
		@Column(name="first_name")
		private java.lang.String firstName;
		@Column(name="last_name")
		private java.lang.String lastName;
		@Column(name="position_id")
		private java.lang.String positionId;
		@Column(name="position_title")
		private java.lang.String positionTitle;
		@Column(name="store_id")
		private java.lang.String storeId;
		@Column(name="department_id")
		private java.lang.String departmentId;
		@Column(name="birth_date")
		private java.lang.String birthDate;
		@Column(name="hire_date")
		private java.lang.String hireDate;
		@Column(name="end_date")
		private java.lang.String endDate;
		@Column(name="salary")
		private java.lang.String salary;
		@Column(name="supervisor_id")
		private java.lang.String supervisorId;
		@Column(name="education_level")
		private java.lang.String educationLevel;
		@Column(name="marital_status")
		private java.lang.String maritalStatus;
		@Column(name="gender")
		private java.lang.String gender;

    public ReserveEmployeePK() {
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


public String getPositionTitle () {
	return this.positionTitle;
}
public void setPositionTitle (String positionTitle) {
	this.positionTitle = positionTitle;
}


public String getStoreId () {
	return this.storeId;
}
public void setStoreId (String storeId) {
	this.storeId = storeId;
}


public String getDepartmentId () {
	return this.departmentId;
}
public void setDepartmentId (String departmentId) {
	this.departmentId = departmentId;
}


public String getBirthDate () {
	return this.birthDate;
}
public void setBirthDate (String birthDate) {
	this.birthDate = birthDate;
}


public String getHireDate () {
	return this.hireDate;
}
public void setHireDate (String hireDate) {
	this.hireDate = hireDate;
}


public String getEndDate () {
	return this.endDate;
}
public void setEndDate (String endDate) {
	this.endDate = endDate;
}


public String getSalary () {
	return this.salary;
}
public void setSalary (String salary) {
	this.salary = salary;
}


public String getSupervisorId () {
	return this.supervisorId;
}
public void setSupervisorId (String supervisorId) {
	this.supervisorId = supervisorId;
}


public String getEducationLevel () {
	return this.educationLevel;
}
public void setEducationLevel (String educationLevel) {
	this.educationLevel = educationLevel;
}


public String getMaritalStatus () {
	return this.maritalStatus;
}
public void setMaritalStatus (String maritalStatus) {
	this.maritalStatus = maritalStatus;
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
		if (!(other instanceof ReserveEmployeePK)) {
			return false;
		}
		ReserveEmployeePK castOther = (ReserveEmployeePK)other;
		return 
			( this.employeeId.equals(castOther.employeeId) ) 
 && ( this.fullName.equals(castOther.fullName) ) 
 && ( this.firstName.equals(castOther.firstName) ) 
 && ( this.lastName.equals(castOther.lastName) ) 
 && ( this.positionId.equals(castOther.positionId) ) 
 && ( this.positionTitle.equals(castOther.positionTitle) ) 
 && ( this.storeId.equals(castOther.storeId) ) 
 && ( this.departmentId.equals(castOther.departmentId) ) 
 && ( this.birthDate.equals(castOther.birthDate) ) 
 && ( this.hireDate.equals(castOther.hireDate) ) 
 && ( this.endDate.equals(castOther.endDate) ) 
 && ( this.salary.equals(castOther.salary) ) 
 && ( this.supervisorId.equals(castOther.supervisorId) ) 
 && ( this.educationLevel.equals(castOther.educationLevel) ) 
 && ( this.maritalStatus.equals(castOther.maritalStatus) ) 
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
 hash = hash * prime + this.positionTitle.hashCode() ;
 hash = hash * prime + this.storeId.hashCode() ;
 hash = hash * prime + this.departmentId.hashCode() ;
 hash = hash * prime + this.birthDate.hashCode() ;
 hash = hash * prime + this.hireDate.hashCode() ;
 hash = hash * prime + this.endDate.hashCode() ;
 hash = hash * prime + this.salary.hashCode() ;
 hash = hash * prime + this.supervisorId.hashCode() ;
 hash = hash * prime + this.educationLevel.hashCode() ;
 hash = hash * prime + this.maritalStatus.hashCode() ;
 hash = hash * prime + this.gender.hashCode() ;

		return hash;
    }
}