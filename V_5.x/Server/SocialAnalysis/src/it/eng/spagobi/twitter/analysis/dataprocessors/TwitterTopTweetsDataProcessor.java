/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.pojos.TwitterTopTweetsPojo;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */

public class TwitterTopTweetsDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterTopTweetsDataProcessor.class);

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	/**
	 * This method creats the objects to show in top retweets box
	 *
	 * @param searchID
	 * @param nProfiles
	 * @return
	 */
	public List<TwitterTopTweetsPojo> getTopTweetsData(String searchID, int nProfiles) {

		logger.debug("Method getTopTweetsData(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getTopTweetsData() without a correct search ID");

		int counter = 0;

		List<TwitterTopTweetsPojo> topTweetsData = new ArrayList<TwitterTopTweetsPojo>();

		String sqlQuery = "SELECT DISTINCT tu.username, td.date_created_at, tu.profile_image_source, td.hashtags, td.tweet_text, td.time_created_at, td.retweet_count, tu.followers_count from twitter_users tu, twitter_data td where tu.user_id = td.user_id and td.search_id = '"
				+ searchID + "' order by td.retweet_count DESC";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			while (rs.next() && counter < nProfiles) {

				String usernameFromDb = rs.getString("username");
				java.sql.Date tempDate = rs.getDate("date_created_at");
				String createDateFromDb = new SimpleDateFormat("dd MMM").format(tempDate);
				String profileImgSrcFromDB = rs.getString("profile_image_source");
				String hashtagsFromDb = rs.getString("hashtags");
				String tweetTextFromDb = rs.getString("tweet_text");
				java.sql.Timestamp tempTime = rs.getTimestamp("time_created_at");
				int counterRTs = rs.getInt("retweet_count");
				int userFollowersCount = Integer.parseInt(rs.getString("followers_count"));

				Assert.assertNotNull(usernameFromDb, "SQL NULL for is_username column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(tempDate, "SQL NULL for date_created_at column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(profileImgSrcFromDB, "SQL NULL for profile_image_source column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(hashtagsFromDb, "SQL NULL for hashtags column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(tweetTextFromDb, "SQL NULL for tweet_text column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(tempTime, "SQL NULL for time_created_at column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(counterRTs, "SQL NULL for retweet_count column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(userFollowersCount, "SQL NULL for followers_count column in results of [ " + sqlQuery + " ] ");

				List<String> hashtags = new ArrayList<String>();
				if (!hashtagsFromDb.isEmpty()) {
					hashtagsFromDb = hashtagsFromDb.toLowerCase();
					// hashtagsFromDb = hashtagsFromDb.replaceAll("#", "");
					String[] hashtagsSplitted = hashtagsFromDb.split(" ");
					hashtags.addAll(Arrays.asList(hashtagsSplitted));
				}

				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(tempTime);

				TwitterTopTweetsPojo tempObj = new TwitterTopTweetsPojo(usernameFromDb, createDateFromDb, profileImgSrcFromDB, hashtagsFromDb, tweetTextFromDb,
						hashtags, calendar, userFollowersCount, counterRTs);

				topTweetsData.add(tempObj);

				nProfiles++;
			}

			logger.debug("Method getTopTweetsData(): End");
			return topTweetsData;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method getTopTweetsData(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

	/**
	 * This method creats the objects to show in top recent box
	 *
	 * @param searchID
	 * @param nProfiles
	 * @return
	 */
	public List<TwitterTopTweetsPojo> getTopRecentTweetsData(String searchID, int nProfiles) {

		logger.debug("Method getTopRecentTweetsData(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getTopRecentTweetsData() without a correct search ID");

		int counter = 0;

		List<TwitterTopTweetsPojo> topTweetsData = new ArrayList<TwitterTopTweetsPojo>();

		String sqlQuery = "SELECT DISTINCT tu.username, td.date_created_at, tu.profile_image_source, td.hashtags, td.tweet_text, td.time_created_at, td.retweet_count, tu.followers_count from twitter_users tu, twitter_data td where tu.user_id = td.user_id and td.search_id = '"
				+ searchID + "' order by td.time_created_at DESC";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			while (rs.next() && counter < nProfiles) {

				String usernameFromDb = rs.getString("username");
				java.sql.Date tempDate = rs.getDate("date_created_at");
				String profileImgSrcFromDB = rs.getString("profile_image_source");
				String hashtagsFromDb = rs.getString("hashtags");
				String tweetTextFromDb = rs.getString("tweet_text");
				java.sql.Timestamp tempTime = rs.getTimestamp("time_created_at");
				int counterRTs = rs.getInt("retweet_count");
				int userFollowersCount = Integer.parseInt(rs.getString("followers_count"));

				Assert.assertNotNull(usernameFromDb, "SQL NULL for is_username column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(tempDate, "SQL NULL for date_created_at column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(profileImgSrcFromDB, "SQL NULL for profile_image_source column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(hashtagsFromDb, "SQL NULL for hashtags column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(tweetTextFromDb, "SQL NULL for tweet_text column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(tempTime, "SQL NULL for time_created_at column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(counterRTs, "SQL NULL for retweet_count column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(userFollowersCount, "SQL NULL for followers_count column in results of [ " + sqlQuery + " ] ");

				String createDateFromDb = new SimpleDateFormat("dd MMM").format(tempDate);

				List<String> hashtags = new ArrayList<String>();
				if (!hashtagsFromDb.isEmpty()) {
					hashtagsFromDb = hashtagsFromDb.toLowerCase();
					// hashtagsFromDb = hashtagsFromDb.replaceAll("#", "");
					String[] hashtagsSplitted = hashtagsFromDb.split(" ");
					hashtags.addAll(Arrays.asList(hashtagsSplitted));
				}

				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(tempTime);

				TwitterTopTweetsPojo tempObj = new TwitterTopTweetsPojo(usernameFromDb, createDateFromDb, profileImgSrcFromDB, hashtagsFromDb, tweetTextFromDb,
						hashtags, calendar, userFollowersCount, counterRTs);

				topTweetsData.add(tempObj);

				nProfiles++;
			}

			logger.debug("Method getTopRecentTweetsData(): End");
			return topTweetsData;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method getTopTweetsData(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

}
