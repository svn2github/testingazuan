/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.pojos.TwitterPiePojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterPieSourcePojo;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class TwitterPieDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterPieDataProcessor.class);

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	/**
	 * This method creates the tweets type pie chart object for summary.jsp
	 *
	 * @param searchID
	 * @return
	 */
	public TwitterPiePojo getTweetsPieChart(String searchID) {

		logger.debug("Method getTweetsPieChart(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getTweetsPieChart() without a correct search ID");

		int totalTweets = 0;
		int totalReplies = 0;
		int totalRTs = 0;

		String sqlQuery = "SELECT is_retweet, reply_to_tweet_id from twitter_data where search_id = '" + searchID + "'";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			while (rs.next()) {

				totalTweets++;

				boolean isRetweet = rs.getBoolean("is_retweet");

				Assert.assertNotNull(isRetweet, "SQL NULL for is_retweet column in results of [ " + sqlQuery + " ] ");

				if (isRetweet) {

					totalRTs++;

				} else {

					String replyToTweetId = rs.getString("reply_to_tweet_id");

					if (replyToTweetId != null) {

						totalReplies++;
					}
				}

			}

			int originalTweets = totalTweets - totalRTs - totalReplies;

			TwitterPiePojo statsObj = new TwitterPiePojo(originalTweets, totalReplies, totalRTs);

			logger.debug("Method getTweetsPieChart(): End");
			return statsObj;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method getTweetsPieChart(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

	/**
	 * This method creates the sources pie chart for summary.jsp
	 *
	 * @param searchID
	 * @return
	 */
	public List<TwitterPieSourcePojo> getTweetsPieSourceChart(String searchID) {

		logger.debug("Method getTweetsPieSourceChart(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getTweetsPieSourceChart() without a correct search ID");

		Map<String, Integer> sourceMap = new HashMap<String, Integer>();
		List<TwitterPieSourcePojo> sources = new ArrayList<TwitterPieSourcePojo>();

		String sqlQuery = "SELECT source_client from twitter_data where search_id = '" + searchID + "'";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			while (rs.next()) {

				String sourceClient = rs.getString("source_client");

				Assert.assertNotNull(sourceClient, "SQL NULL forsource_client column in results of [ " + sqlQuery + " ] ");

				String formattedSource = tweetSourceFormatter(sourceClient);

				if (sourceMap.containsKey(formattedSource)) {

					int value = sourceMap.get(formattedSource);
					value++;
					sourceMap.put(formattedSource, value);

				} else {

					sourceMap.put(formattedSource, 1);
				}
			}

			for (Map.Entry<String, Integer> entry : sourceMap.entrySet()) {

				String source = entry.getKey();
				int value = entry.getValue();

				TwitterPieSourcePojo obj = new TwitterPieSourcePojo(source, value);
				sources.add(obj);
			}

			logger.debug("Method getTweetsPieSourceChart(): End");
			return sources;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method getTweetsPieSourceChart(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		}

	}

	/**
	 * This method extracts the source from general html source String
	 *
	 * @param source
	 * @return
	 */
	private String tweetSourceFormatter(String source) {

		String formattedHTMLSource = source.replaceAll("<.*?>", "");
		String formattedTextSourceFirst = formattedHTMLSource.replaceAll("Twitter ", "");
		String formattedTextSourceSecond = formattedTextSourceFirst.replaceAll("for ", "");

		return formattedTextSourceSecond;

	}
}
