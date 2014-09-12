/** SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
package it.eng.spagobi.twitter.analysis.cache;

import it.eng.spagobi.bitly.analysis.pojos.BitlyLinkCategoryPojo;
import it.eng.spagobi.bitly.analysis.pojos.BitlyLinkPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterAccountToMonitorPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMonitorSchedulerPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchSchedulerPojo;

import java.sql.Connection;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import twitter4j.Status;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public interface ITwitterCache {

	// TODO: interfaccia per metodi da implementare su tutte le tipologie di
	// Cache sui diversi db

	/**
	 * This method creates the connection for the selected DB
	 *
	 * @return the connection for the DB
	 */
	public Connection openConnection();

	/**
	 * This method closes the connection for the selected DB
	 */
	public void closeConnection();

	/**
	 * This method inserts a new search in the DB
	 *
	 * @param twitterSearch
	 *            : object representing a generic search
	 * @return the created search ID
	 */
	public long insertTwitterSearch(TwitterSearchPojo twitterSearch);

	/**
	 * This method updates the field loading for the selected search
	 *
	 * @param searchID
	 *            : selected search ID
	 * @param isLoading
	 *            : new value for the field loading of this search
	 */
	public void updateTwitterSearchLoading(long searchID, boolean isLoading);

	/**
	 * This method saves a tweet in the DB
	 *
	 * @param tweet
	 *            : the object representing a tweet
	 * @param keyword
	 *            : the string representing the input field keyword
	 * @param searchID
	 *            : selected search ID
	 * @throws Exception
	 */
	public void saveTweet(Status tweet, String keyword, long searchID) throws Exception;

	/**
	 * This method inserts a bitly link in the DB with all linked analysis
	 *
	 * @param linkPojo
	 *            : object representing a bitly link
	 * @param linkCategoryPojos
	 *            : list of objects for linked analysis
	 * @param searchID
	 *            : selected search ID
	 */
	public void insertBitlyAnalysis(BitlyLinkPojo linkPojo, List<BitlyLinkCategoryPojo> linkCategoryPojos, long searchID);

	/**
	 * This method inserts useful data about a specified account to monitor
	 *
	 * @param accountToMonitor
	 *            : twitter username account to monitor
	 */
	public void insertAccountToMonitor(TwitterAccountToMonitorPojo accountToMonitor);

	/**
	 * Generic method to execute query
	 *
	 * @param sqlQuery
	 *            : sql query
	 * @return a set of cached results
	 */
	public CachedRowSet runQuery(String sqlQuery);

	/**
	 * This method is an update for the search table. All streaming searches are
	 * stopped, before start the new stream
	 */
	public void stopStreamingSearch();

	/**
	 * This method creates a scheduler for a search
	 *
	 * @param twitterScheduler
	 *            : search scheduler with data associated to a specified search
	 */
	public void insertTwitterSearchScheduler(TwitterSearchSchedulerPojo twitterScheduler);

	/**
	 * This method creates a scheduler for monitoring the resources
	 *
	 * @param twitterMonitorScheduler
	 *            : monitor scheduler with data associated to specified
	 *            resources
	 */
	public void insertTwitterMonitorScheduler(TwitterMonitorSchedulerPojo twitterMonitorScheduler);

	// TODO: se possibile lo schema da salvare lo renderei gestibile da un'altra
	// classe (magari Abstract) in modo tale da non ripetere
	// le estrazioni delle informazioni dal singolo tweet su ogni
	// implementazione diversa della cache
	// sulle singole implementazioni della cache dovrebbe cambiare la sintassi
	// della INSERT a seconda del tipo di db

	/**
	 * This method deletes the specified search logically and the linked
	 * schedulers phisically
	 *
	 * @param twitterSearch
	 *            : search object
	 */
	public void deleteSearch(TwitterSearchPojo twitterSearch);

	public void updateStartingDateSearchScheduler(TwitterSearchSchedulerPojo twitterSearchSchedulerPojo);

	public TwitterMonitorSchedulerPojo stopSearchScheduler(TwitterSearchPojo twitterSearch);

	public void updateMonitorScheduler(TwitterMonitorSchedulerPojo twitterMonitorSchedulerPojo);

	public TwitterMonitorSchedulerPojo getTwitterMonitorScheduler(long searchID);

}
