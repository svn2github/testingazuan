/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.entities;

import it.eng.spagobi.twitter.analysis.enums.MonitorRepeatTypeEnum;
import it.eng.spagobi.twitter.analysis.enums.UpToTypeEnum;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @author Giorgio Federici (giorgio.federici@eng.it)
 */

@Entity
@Table(name = "twitter_monitor_scheduler")
public class TwitterMonitorScheduler {

	@Id
	@Column(name = "id")
	@NotNull
	@GeneratedValue
	private long id;

	@OneToOne
	@JoinColumn(name = "search_id")
	@NotNull
	private TwitterSearch twitterSearch;

	@Column(name = "starting_time")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private java.util.Calendar startingTime;

	@Column(name = "last_activation_time")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private java.util.Calendar lastActivationTime;

	@Column(name = "ending_time")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private java.util.Calendar endingTime;

	@Column(name = "repeat_frequency")
	@NotNull
	private int repeatFrequency;

	@Column(name = "repeat_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private MonitorRepeatTypeEnum repeatType;

	@Column(name = "active_search", columnDefinition = "boolean", length = 1)
	@NotNull
	private boolean activeSearch = true;

	@Column(name = "up_to_value")
	@NotNull
	private int upToValue;

	@Column(name = "up_to_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private UpToTypeEnum upToType;

	@Column(name = "links")
	@Length(max = 500)
	private String links;

	@Column(name = "accounts")
	@Length(max = 500)
	private String accounts;

	@Column(name = "documents")
	@Length(max = 500)
	private String documents;

	public TwitterMonitorScheduler() {

	}

	public TwitterMonitorScheduler(long id, TwitterSearch twitterSearch, Calendar startingTime, Calendar lastActivationTime, Calendar endingTime,
			int repeatFrequency, MonitorRepeatTypeEnum repeatType, boolean activeSearch, int upToValue, UpToTypeEnum upToType, String links, String accounts,
			String documents) {

		this.id = id;
		this.twitterSearch = twitterSearch;
		this.startingTime = startingTime;
		this.lastActivationTime = lastActivationTime;
		this.endingTime = endingTime;
		this.repeatFrequency = repeatFrequency;
		this.repeatType = repeatType;
		this.activeSearch = activeSearch;
		this.upToValue = upToValue;
		this.upToType = upToType;
		this.links = links;
		this.accounts = accounts;
		this.documents = documents;
	}

	// Entity mini -> TwitterDocumentsDataProcessor
	public TwitterMonitorScheduler(String documents, Calendar starting_time, Calendar last_activation_time) {
		this.documents = documents;
		this.startingTime = startingTime;
		this.lastActivationTime = last_activation_time;
	}

	public TwitterMonitorScheduler(long id, String links, String accounts) {
		this.id = id;
		this.links = links;
		this.accounts = accounts;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TwitterSearch getTwitterSearch() {
		return twitterSearch;
	}

	public void setTwitterSearch(TwitterSearch twitterSearch) {
		this.twitterSearch = twitterSearch;
	}

	public java.util.Calendar getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(java.util.Calendar startingTime) {
		this.startingTime = startingTime;
	}

	public java.util.Calendar getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(java.util.Calendar endingTime) {
		this.endingTime = endingTime;
	}

	public java.util.Calendar getLastActivationTime() {
		return lastActivationTime;
	}

	public void setLastActivationTime(java.util.Calendar lastActivationTime) {
		this.lastActivationTime = lastActivationTime;
	}

	public int getRepeatFrequency() {
		return repeatFrequency;
	}

	public void setRepeatFrequency(int repeatFrequency) {
		this.repeatFrequency = repeatFrequency;
	}

	public MonitorRepeatTypeEnum getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(MonitorRepeatTypeEnum repeatType) {
		this.repeatType = repeatType;
	}

	public boolean isActiveSearch() {
		return activeSearch;
	}

	public void setActiveSearch(boolean activeSearch) {
		this.activeSearch = activeSearch;
	}

	public int getUpToValue() {
		return upToValue;
	}

	public void setUpToValue(int upToValue) {
		this.upToValue = upToValue;
	}

	public UpToTypeEnum getUpToType() {
		return upToType;
	}

	public void setUpToType(UpToTypeEnum upToType) {
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

	@Override
	public String toString() {
		return "TwitterMonitorScheduler [id=" + id + ", twitterSearch=" + twitterSearch + ", startingTime=" + startingTime + ", lastActivationTime="
				+ lastActivationTime + ", endingTime=" + endingTime + ", repeatFrequency=" + repeatFrequency + ", repeatType=" + repeatType + ", activeSearch="
				+ activeSearch + ", upToValue=" + upToValue + ", upToType=" + upToType + ", links=" + links + ", accounts=" + accounts + ", documents="
				+ documents + "]";
	}

}
