/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */

public class TwitterGeneralStatsDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterGeneralStatsDataProcessor.class);

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	public TwitterGeneralStatsDataProcessor() {

	}

	/**
	 * This method counts total tweets for a search
	 *
	 * @param searchID
	 * @return
	 */
	public int totalTweetsCounter(String searchID) {

		logger.debug("Method totalTweetsCounter(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute totalTweetsCounter() without a correct search ID");

		int totalTweets = 0;

		String sqlQuery = "SELECT tweet_id from twitter_data where search_id = '" + searchID + "'";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			while (rs.next()) {

				totalTweets++;
			}

			logger.debug("Method totalTweetsCounter(): End");

			return totalTweets;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method totalTweetsCounter(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

	/**
	 * This method counts total user linked to a search
	 *
	 * @param searchID
	 * @return
	 */
	public int totalUsersCounter(String searchID) {

		logger.debug("Method totalUsersCounter(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute totalUsersCounter() without a correct search ID");

		int totalUsers = 0;

		String sqlQuery = "SELECT DISTINCT tu.user_id from twitter_users tu, twitter_data td where tu.user_id = td.user_id and td.search_id = " + searchID;

		try {
			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			while (rs.next()) {

				totalUsers++;
			}

			logger.debug("Method totalUsersCounter(): End");
			return totalUsers;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method totalTweetsCounter(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

	/**
	 * This method finds the min date for a search
	 *
	 * @param searchIDStr
	 *            : search ID
	 * @return formatted minimum date for this search
	 */
	public String getMinDateSearch(String searchID) {

		logger.debug("Method getMinDataSearch(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getMinDateSearch() without a correct search ID");

		String minDate = "";

		String sqlQuery = "SELECT date_created_at FROM twitter_data WHERE search_id = " + searchID + " ORDER BY date_created_at ASC";

		try {

			logger.debug("Method getMinDataSearch(): Retrieving minimum date for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			if (rs.next()) {
				java.sql.Date tempDate = rs.getDate("date_created_at");

				SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy");
				minDate = simpleDataFormatter.format(tempDate.getTime());
			}

			logger.debug("Method getMinDataSearch(): End");
			return minDate;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method getMinDataSearch(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

	/**
	 * This method finds the max date for a search
	 *
	 * @param searchIDStr
	 *            : search ID
	 * @return formatted maximum date for this search
	 */
	public String getMaxDateSearch(String searchIDStr) {

		logger.debug("Method getMaxDateSearch(): Start");

		long searchID = Long.parseLong(searchIDStr);

		String maxDate = "";

		String sqlQuery = "SELECT date_created_at FROM twitter_data WHERE search_id = " + searchID + " ORDER BY date_created_at DESC";

		try {

			logger.debug("Method getMaxDateSearch(): Retrieving max date for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			if (rs.next()) {

				java.sql.Date tempDate = rs.getDate("date_created_at");

				SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy");
				maxDate = simpleDataFormatter.format(tempDate.getTime());
			}

			logger.debug("Method getMaxDateSearch(): End");
			return maxDate;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method getMaxDateSearch(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

}
