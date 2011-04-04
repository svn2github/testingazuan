package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the reserve_employee table.
 * 
 */
@Entity
@Table(name="reserve_employee")
public class ReserveEmployee implements Serializable {

private static final long serialVersionUID = 1L;

public ReserveEmployee() {
}
	
	@Column(name="employee_id")
private String employeeId=null;
	@Column(name="full_name")
private String fullName=null;
	@Column(name="first_name")
private String firstName=null;
	@Column(name="last_name")
private String lastName=null;
	@Column(name="position_id")
private String positionId=null;
	@Column(name="position_title")
private String positionTitle=null;
	@Column(name="store_id")
private String storeId=null;
	@Column(name="department_id")
private String departmentId=null;
	@Column(name="birth_date")
private String birthDate=null;
	@Column(name="hire_date")
private String hireDate=null;
	@Column(name="end_date")
private String endDate=null;
	@Column(name="salary")
private String salary=null;
	@Column(name="supervisor_id")
private String supervisorId=null;
	@Column(name="education_level")
private String educationLevel=null;
	@Column(name="marital_status")
private String maritalStatus=null;
	@Column(name="gender")
private String gender=null;

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

}