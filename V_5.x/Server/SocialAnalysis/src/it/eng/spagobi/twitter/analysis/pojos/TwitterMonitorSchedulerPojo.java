/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.spagobi.twitter.analysis.pojos;

import java.util.Calendar;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public class TwitterMonitorSchedulerPojo {

	private long searchID;
	private Calendar endingDate;
	private int repeatFrequency;
	private String repeatType;
	private int id;
	private boolean active = false;
	private int upToValue;
	private String upToType;
	private String links;
	private String accounts;
	private String documents;

	public TwitterMonitorSchedulerPojo() {

	}

	public TwitterMonitorSchedulerPojo(Calendar sD, int rF, String rT, boolean active, int upToValue, String upToType) {
		this.endingDate = sD;
		this.repeatFrequency = rF;
		this.repeatType = rT;
		this.active = active;
		this.upToValue = upToValue;
		this.upToType = upToType;
	}

	public long getSearchID() {
		return searchID;
	}

	public void setSearchID(long searchID) {
		this.searchID = searchID;
	}

	public int getRepeatFrequency() {
		return repeatFrequency;
	}

	public void setRepeatFrequency(int repeatFrequency) {
		this.repeatFrequency = repeatFrequency;
	}

	public String getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}

	public Calendar getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Calendar endingDate) {
		this.endingDate = endingDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getUpToValue() {
		return upToValue;
	}

	public void setUpToValue(int upToValue) {
		this.upToValue = upToValue;
	}

	public String getUpToType() {
		return upToType;
	}

	public void setUpToType(String upToType) {
		this.upToType = upToType;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public String getAccounts() {
		return accounts;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
	}

}
