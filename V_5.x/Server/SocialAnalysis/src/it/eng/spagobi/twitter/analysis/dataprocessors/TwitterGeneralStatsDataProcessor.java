/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.DataProcessorCacheImpl;
import it.eng.spagobi.twitter.analysis.cache.IDataProcessorCache;
import it.eng.spagobi.twitter.analysis.entities.TwitterUser;
import it.eng.spagobi.utilities.assertion.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */

public class TwitterGeneralStatsDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterGeneralStatsDataProcessor.class);

	private final IDataProcessorCache dpCache = new DataProcessorCacheImpl();

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

		int totalTweets = dpCache.getTotalTweets(searchID);

		logger.debug("Method totalTweetsCounter(): End");

		return totalTweets;

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

		int totalUsers = dpCache.getTotalUsers(searchID);

		logger.debug("Method totalUsersCounter(): End");
		return totalUsers;

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

		SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy");

		Calendar minCalendar = dpCache.getMinTweetDate(searchID);

		if (minCalendar != null) {

			Date minDate = new Date(minCalendar.getTimeInMillis());

			logger.debug("Method getMinDataSearch(): End");
			return simpleDataFormatter.format(minDate);
		}
		{
			logger.debug("Method getMinDataSearch(): End");
			return "N/A";

		}

	}

	/**
	 * This method finds the max date for a search
	 *
	 * @param searchIDStr
	 *            : search ID
	 * @return formatted maximum date for this search
	 */
	public String getMaxDateSearch(String searchID) {

		logger.debug("Method getMaxDateSearch(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getMinDateSearch() without a correct search ID");

		SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy");

		Calendar maxCalendar = dpCache.getMaxTweetDate(searchID);

		if (maxCalendar != null) {

			Date maxDate = new Date(maxCalendar.getTimeInMillis());

			logger.debug("Method getMaxDateSearch(): End");
			return simpleDataFormatter.format(maxDate);
		}
		{
			logger.debug("Method getMaxDateSearch(): End");
			return "N/A";

		}
	}

	public int getReachMetric(String searchID) {

		logger.debug("Method getReachMetric(): Start");

		int reach = 0;
		int followers = 0;

		Assert.assertNotNull(searchID, "Impossibile execute getReachMetric() without a correct search ID");

		List<TwitterUser> users = dpCache.getUsersForSearchID(searchID);

		if (users != null) {
			reach = reach + users.size();

			for (TwitterUser user : users) {
				followers = followers + user.getFollowersCount();
			}
		}

		reach = reach + followers;

		logger.debug("Method getReachMetric(): End");
		return reach;

	}

}
