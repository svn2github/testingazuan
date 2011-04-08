package it.eng.spagobi.meta;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This is the primary key class for the salary table.
 * 
 */
@Embeddable
public class SalaryPK implements Serializable {

	private static final long serialVersionUID = 1L;
		@Column(name="pay_date")
		private String payDate;
		@Column(name="employee_id")
		private String employeeId;
		@Column(name="department_id")
		private String departmentId;
		@Column(name="currency_id")
		private String currencyId;
		@Column(name="salary_paid")
		private String salaryPaid;
		@Column(name="overtime_paid")
		private String overtimePaid;
		@Column(name="vacation_accrued")
		private String vacationAccrued;
		@Column(name="vacation_used")
		private String vacationUsed;

    public SalaryPK() {
    }

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


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SalaryPK)) {
			return false;
		}
		SalaryPK castOther = (SalaryPK)other;
		return 
			( this.payDate.equals(castOther.payDate) ) 
 && ( this.employeeId.equals(castOther.employeeId) ) 
 && ( this.departmentId.equals(castOther.departmentId) ) 
 && ( this.currencyId.equals(castOther.currencyId) ) 
 && ( this.salaryPaid.equals(castOther.salaryPaid) ) 
 && ( this.overtimePaid.equals(castOther.overtimePaid) ) 
 && ( this.vacationAccrued.equals(castOther.vacationAccrued) ) 
 && ( this.vacationUsed.equals(castOther.vacationUsed) );

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		 hash = hash * prime + this.payDate.hashCode() ;
 hash = hash * prime + this.employeeId.hashCode() ;
 hash = hash * prime + this.departmentId.hashCode() ;
 hash = hash * prime + this.currencyId.hashCode() ;
 hash = hash * prime + this.salaryPaid.hashCode() ;
 hash = hash * prime + this.overtimePaid.hashCode() ;
 hash = hash * prime + this.vacationAccrued.hashCode() ;
 hash = hash * prime + this.vacationUsed.hashCode() ;

		return hash;
    }
}