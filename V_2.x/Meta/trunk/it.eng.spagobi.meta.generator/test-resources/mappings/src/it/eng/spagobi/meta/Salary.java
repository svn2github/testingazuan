package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;


/**
 * This class refers to the salary table.
 * 
 */
@Entity
@Table(name="salary")
public class Salary implements Serializable {

private static final long serialVersionUID = 1L;

public Salary() {
}
	
	@Column(name="pay_date")
private String payDate=null;
	@Column(name="employee_id")
private String employeeId=null;
	@Column(name="department_id")
private String departmentId=null;
	@Column(name="currency_id")
private String currencyId=null;
	@Column(name="salary_paid")
private String salaryPaid=null;
	@Column(name="overtime_paid")
private String overtimePaid=null;
	@Column(name="vacation_accrued")
private String vacationAccrued=null;
	@Column(name="vacation_used")
private String vacationUsed=null;

public String getPayDate () {
	return this.payDate;
}
public void setPayDate (String payDate) {
	this.payDate = payDate;
}
public String getEmployeeId () {
	return this.employeeId;
}
public void setEmployeeId (String employeeId) {
	this.employeeId = employeeId;
}
public String getDepartmentId () {
	return this.departmentId;
}
public void setDepartmentId (String departmentId) {
	this.departmentId = departmentId;
}
public String getCurrencyId () {
	return this.currencyId;
}
public void setCurrencyId (String currencyId) {
	this.currencyId = currencyId;
}
public String getSalaryPaid () {
	return this.salaryPaid;
}
public void setSalaryPaid (String salaryPaid) {
	this.salaryPaid = salaryPaid;
}
public String getOvertimePaid () {
	return this.overtimePaid;
}
public void setOvertimePaid (String overtimePaid) {
	this.overtimePaid = overtimePaid;
}
public String getVacationAccrued () {
	return this.vacationAccrued;
}
public void setVacationAccrued (String vacationAccrued) {
	this.vacationAccrued = vacationAccrued;
}
public String getVacationUsed () {
	return this.vacationUsed;
}
public void setVacationUsed (String vacationUsed) {
	this.vacationUsed = vacationUsed;
}

}