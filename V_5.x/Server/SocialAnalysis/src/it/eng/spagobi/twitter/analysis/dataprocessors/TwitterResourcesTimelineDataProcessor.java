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

package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import twitter4j.JSONArray;
import twitter4j.JSONObject;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */

public class TwitterResourcesTimelineDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterResourcesTimelineDataProcessor.class);

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	private Calendar getMaxTimeResourceToMonitor(String searchIDStr, String tableName) {

		logger.debug("Method getMaxTimeResourceToMonitor(): Start");

		long searchID = Long.parseLong(searchIDStr);

		Calendar maxTime = GregorianCalendar.getInstance();

		String sqlQuery = "SELECT timestamp FROM " + tableName + " WHERE search_id = " + searchID + " ORDER BY timestamp DESC";

		try {

			logger.debug("Method getMaxTimeResourceToMonitor(): Retrieving max time for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				if (rs.next()) {
					java.sql.Timestamp timestamp = rs.getTimestamp("timestamp");

					maxTime.setTime(timestamp);
					// maxTime.set(Calendar.HOUR_OF_DAY, 0);
					maxTime.set(Calendar.MINUTE, 0);
					maxTime.set(Calendar.SECOND, 0);
					maxTime.set(Calendar.MILLISECOND, 0);
				}
			}

		} catch (Exception e) {
			logger.debug("Method getMaxTimeResourceToMonitor(): Error retrieving max date for search: " + searchID + " - " + e);
		}

		logger.debug("Method getMaxTimeResourceToMonitor(): End");
		return maxTime;
	}

	private Calendar getMinTimeResourceToMonitor(String searchIDStr, String tableName) {

		logger.debug("Method getMinTimeResourceToMonitor(): Start");

		long searchID = Long.parseLong(searchIDStr);

		Calendar minTime = GregorianCalendar.getInstance();

		String sqlQuery = "SELECT timestamp FROM " + tableName + " WHERE search_id = " + searchID + " ORDER BY timestamp ASC";

		try {

			logger.debug("Method getMinTimeResourceToMonitor(): Retrieving max time for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				if (rs.next()) {
					java.sql.Timestamp timestamp = rs.getTimestamp("timestamp");

					minTime.setTime(timestamp);
					// minTime.set(Calendar.HOUR_OF_DAY, 0);
					minTime.set(Calendar.MINUTE, 0);
					minTime.set(Calendar.SECOND, 0);
					minTime.set(Calendar.MILLISECOND, 0);
				}
			}

		} catch (Exception e) {
			logger.debug("Method getMinTimeResourceToMonitor(): Error retrieving max date for search: " + searchID + " - " + e);
		}

		logger.debug("Method getMinTimeResourceToMonitor(): End");
		return minTime;
	}

	public JSONObject getFollowers(String searchIDStr, String timeFilter) {

		logger.debug("Method getFollowersTimelineObjs(): Start");

		long searchID = Long.parseLong(searchIDStr);

		Calendar minTime = getMinTimeResourceToMonitor(searchIDStr, "twitter_accounts_to_monitor");
		Calendar maxTime = getMaxTimeResourceToMonitor(searchIDStr, "twitter_accounts_to_monitor");

		minTime = roundTime(timeFilter, minTime);
		maxTime = roundTime(timeFilter, maxTime);

		JSONObject accountFollowersJSON = new JSONObject();

		try {

			String sqlAccountQuery = "SELECT account_name from twitter_accounts_to_monitor where search_id = '" + searchID + "' GROUP BY account_name ";

			CachedRowSet accountRS = twitterCache.runQuery(sqlAccountQuery);

			if (accountRS != null) {

				JSONArray results = new JSONArray();

				while (accountRS.next()) {

					String accountName = accountRS.getString("account_name");

					JSONObject element = new JSONObject();
					element.put("label", accountName);

					Calendar min = GregorianCalendar.getInstance();
					Calendar max = GregorianCalendar.getInstance();

					min.setTimeInMillis(minTime.getTimeInMillis());
					max.setTimeInMillis(maxTime.getTimeInMillis());

					LinkedHashMap<Long, Integer> accountFollowers = initializeTimeline(min, max, timeFilter);

					String sqlTimestampQuery = "SELECT timestamp, followers_count from twitter_accounts_to_monitor where search_id = '" + searchID + "' and account_name = '"
							+ accountName + "' ORDER BY timestamp asc";

					CachedRowSet timestampRS = twitterCache.runQuery(sqlTimestampQuery);

					if (timestampRS != null) {

						JSONArray data = new JSONArray();

						while (timestampRS.next()) {

							java.sql.Timestamp timestamp = timestampRS.getTimestamp("timestamp");
							int followersCount = timestampRS.getInt("followers_count");

							Calendar timestampCalendar = GregorianCalendar.getInstance();
							timestampCalendar.setTime(timestamp);

							Calendar roundedTime = roundTime(timeFilter, timestampCalendar);

							long time = roundedTime.getTimeInMillis();

							if (accountFollowers.containsKey(time)) {

								int value = accountFollowers.get(time);

								if (value == -1) {
									// defaultvalue, first add to this key
									value = followersCount;
								} else {
									// value already modified with real values
									value = value + followersCount;
								}

								accountFollowers.put(time, value);
							}

						}

						if (accountFollowers.size() == 1) {

							// manage the situation of only one result
							// useless graph

							Calendar newMinTime = GregorianCalendar.getInstance();
							newMinTime.setTimeInMillis(minTime.getTimeInMillis());

							// actual min followers value
							int lowerFollowers = accountFollowers.get(newMinTime.getTimeInMillis());

							if (timeFilter.equalsIgnoreCase("hours")) {

								newMinTime.add(Calendar.HOUR_OF_DAY, -1);

							} else if (timeFilter.equalsIgnoreCase("days")) {

								newMinTime.add(Calendar.DAY_OF_MONTH, -1);

							} else if (timeFilter.equalsIgnoreCase("weeks")) {

								newMinTime.add(Calendar.WEEK_OF_YEAR, -1);
							}

							else if (timeFilter.equalsIgnoreCase("months")) {

								newMinTime.add(Calendar.MONTH, -1);
							}

							long newLowerMills = newMinTime.getTimeInMillis();
							accountFollowers.put(newLowerMills, lowerFollowers);

						}

						int coverValue = 0;

						for (Map.Entry<Long, Integer> entry : accountFollowers.entrySet()) {

							JSONObject dataElement = new JSONObject();

							long time = entry.getKey();
							int followers = entry.getValue();

							if (followers == -1) {
								// no values for this time, put cover values
								// until next monitored value
								followers = coverValue;
							}

							// mantain the real value to cover next deault
							// values
							coverValue = followers;

							dataElement.put("mills", time);
							dataElement.put("followers", followers);

							data.put(dataElement);
						}

						element.put("data", data);
					}

					results.put(element);
				}

				accountFollowersJSON.put("results", results);
				accountFollowersJSON.put("lowerBound", minTime.getTimeInMillis());

			}

		} catch (Exception e) {
			logger.debug("Method getFollowersTimelineObjs(): Error calculating timeline for search: " + searchID + " - " + e);
		}

		logger.debug("Method getFollowersTimelineObjs(): End");

		logger.debug(accountFollowersJSON.toString());
		return accountFollowersJSON;

	}

	public JSONObject getClicks(String searchIDStr, String timeFilter) {

		logger.debug("Method getClicks(): Start");

		long searchID = Long.parseLong(searchIDStr);

		Calendar minTime = getMinTimeResourceToMonitor(searchIDStr, "twitter_links_to_monitor");
		Calendar maxTime = getMaxTimeResourceToMonitor(searchIDStr, "twitter_links_to_monitor");

		minTime = roundTime(timeFilter, minTime);
		maxTime = roundTime(timeFilter, maxTime);

		JSONObject linkClicksJSON = new JSONObject();

		try {

			String sqlLinkQuery = "SELECT link from twitter_links_to_monitor where search_id = '" + searchID + "' GROUP BY link ";

			CachedRowSet linkRS = twitterCache.runQuery(sqlLinkQuery);

			if (linkRS != null) {

				JSONArray results = new JSONArray();

				while (linkRS.next()) {

					String link = linkRS.getString("link");

					JSONObject element = new JSONObject();
					element.put("label", link);

					Calendar min = GregorianCalendar.getInstance();
					Calendar max = GregorianCalendar.getInstance();

					min.setTimeInMillis(minTime.getTimeInMillis());
					max.setTimeInMillis(maxTime.getTimeInMillis());

					LinkedHashMap<Long, Integer> linkClicks = initializeTimeline(min, max, timeFilter);

					String sqlTimestampQuery = "SELECT timestamp, clicks_count from twitter_links_to_monitor where search_id = '" + searchID + "' and link = '" + link
							+ "' ORDER BY timestamp asc";

					CachedRowSet timestampRS = twitterCache.runQuery(sqlTimestampQuery);

					if (timestampRS != null) {

						JSONArray data = new JSONArray();

						while (timestampRS.next()) {

							java.sql.Timestamp timestamp = timestampRS.getTimestamp("timestamp");
							int clicksCount = timestampRS.getInt("clicks_count");

							Calendar timestampCalendar = GregorianCalendar.getInstance();
							timestampCalendar.setTime(timestamp);

							Calendar roundedTime = roundTime(timeFilter, timestampCalendar);

							long time = roundedTime.getTimeInMillis();

							if (linkClicks.containsKey(time)) {

								int value = linkClicks.get(time);

								if (value == -1) {
									// defaultvalue, first add to this key
									value = clicksCount;
								} else {
									// value already modified with real values
									value = value + clicksCount;
								}

								linkClicks.put(time, value);
							}

						}

						if (linkClicks.size() == 1) {

							// manage the situation of only one result
							// useless graph

							Calendar newMinTime = GregorianCalendar.getInstance();
							newMinTime.setTimeInMillis(minTime.getTimeInMillis());

							// actual min followers value
							int lowerClicks = linkClicks.get(newMinTime.getTimeInMillis());

							if (timeFilter.equalsIgnoreCase("hours")) {

								newMinTime.add(Calendar.HOUR_OF_DAY, -1);

							} else if (timeFilter.equalsIgnoreCase("days")) {

								newMinTime.add(Calendar.DAY_OF_MONTH, -1);

							} else if (timeFilter.equalsIgnoreCase("weeks")) {

								newMinTime.add(Calendar.WEEK_OF_YEAR, -1);
							}

							else if (timeFilter.equalsIgnoreCase("months")) {

								newMinTime.add(Calendar.MONTH, -1);
							}

							long newLowerMills = newMinTime.getTimeInMillis();
							linkClicks.put(newLowerMills, lowerClicks);

						}

						int coverValue = 0;

						for (Map.Entry<Long, Integer> entry : linkClicks.entrySet()) {

							JSONObject dataElement = new JSONObject();

							long time = entry.getKey();
							int clicks = entry.getValue();

							if (clicks == -1) {
								// no values for this time, put cover values
								// until next monitored value
								clicks = coverValue;
							}

							// mantain the real value to cover next deault
							// values
							coverValue = clicks;

							dataElement.put("mills", time);
							dataElement.put("clicks", clicks);

							data.put(dataElement);
						}

						element.put("data", data);
					}

					results.put(element);
				}

				linkClicksJSON.put("results", results);
				linkClicksJSON.put("lowerBound", minTime.getTimeInMillis());

			}

		} catch (Exception e) {
			logger.debug("Method getClicks(): Error calculating timeline for search: " + searchID + " - " + e);
		}

		logger.debug("Method getClicks(): End");

		return linkClicksJSON;

	}

	public String getVisualizationType(String searchIDStr) {

		logger.debug("Method getVisualizationType(): Start");

		long searchID = Long.parseLong(searchIDStr);

		String type = "";

		String sqlQuery = "SELECT repeat_type FROM twitter_monitor_scheduler WHERE search_id = " + searchID;

		try {

			logger.debug("Method getVisualizationType(): Retrieving max time for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				if (rs.next()) {

					type = rs.getString("repeat_type");
				}
			}

		} catch (Exception e) {
			logger.debug("Method getVisualizationType(): Error retrieving monitor scheduler type for search: " + searchID + " - " + e);
		}

		logger.debug("Method getVisualizationType(): End");
		return type.toLowerCase();
	}

	private LinkedHashMap<Long, Integer> initializeTimeline(Calendar minTime, Calendar maxTime, String filter) {

		LinkedHashMap<Long, Integer> baseMap = new LinkedHashMap<Long, Integer>();

		while (minTime.compareTo(maxTime) <= 0) {

			baseMap.put(minTime.getTimeInMillis(), -1);

			if (filter.equalsIgnoreCase("hours")) {

				minTime.add(Calendar.HOUR_OF_DAY, 1);

			} else if (filter.equalsIgnoreCase("days")) {

				minTime.add(Calendar.DAY_OF_MONTH, 1);

			} else if (filter.equalsIgnoreCase("weeks")) {

				minTime.add(Calendar.WEEK_OF_YEAR, 1);
			}

			else if (filter.equalsIgnoreCase("months")) {

				minTime.add(Calendar.MONTH, 1);
			}

		}

		return baseMap;
	}

	private Calendar roundTime(String filter, Calendar tempTime) {
		if (tempTime != null) {
			if (filter.equalsIgnoreCase("hours")) {

				// round for hours
				tempTime.set(Calendar.MINUTE, 0);
				tempTime.set(Calendar.SECOND, 0);
				tempTime.set(Calendar.MILLISECOND, 0);

			} else if (filter.equalsIgnoreCase("days")) {

				// round for days
				tempTime.set(Calendar.HOUR_OF_DAY, 0);
				tempTime.set(Calendar.MINUTE, 0);
				tempTime.set(Calendar.SECOND, 0);
				tempTime.set(Calendar.MILLISECOND, 0);

			} else if (filter.equalsIgnoreCase("weeks")) {

				// round for weeks
				tempTime.set(Calendar.HOUR_OF_DAY, 0);
				tempTime.set(Calendar.MINUTE, 0);
				tempTime.set(Calendar.SECOND, 0);
				tempTime.set(Calendar.MILLISECOND, 0);
				tempTime.set(Calendar.DAY_OF_WEEK, tempTime.getFirstDayOfWeek());

			}

			else if (filter.equalsIgnoreCase("months")) {

				// round for weeks
				tempTime.set(Calendar.HOUR_OF_DAY, 0);
				tempTime.set(Calendar.MINUTE, 0);
				tempTime.set(Calendar.SECOND, 0);
				tempTime.set(Calendar.MILLISECOND, 0);
				tempTime.set(Calendar.DAY_OF_MONTH, 1);

			}

			return tempTime;
		} else {
			return GregorianCalendar.getInstance();
		}
	}

}
