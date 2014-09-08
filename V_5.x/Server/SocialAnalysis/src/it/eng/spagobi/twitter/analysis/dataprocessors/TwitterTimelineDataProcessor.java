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
import it.eng.spagobi.twitter.analysis.pojos.TwitterTimelinePojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */

public class TwitterTimelineDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterTimelineDataProcessor.class);

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	public List<TwitterTimelinePojo> getTimelineObjsDaily(String searchIDStr) {

		long searchID = Long.parseLong(searchIDStr);

		List<TwitterTimelinePojo> timelineChartObjs = new ArrayList<TwitterTimelinePojo>();

		LinkedHashMap<Long, Integer> tweetsMap = new LinkedHashMap<Long, Integer>();
		LinkedHashMap<Long, Integer> rtsMap = new LinkedHashMap<Long, Integer>();

		long lowerBound = -1;
		long upperBound = -1;

		boolean resultSetEmpty = true;

		String sqlQuery = "SELECT t.time_created_at, t.is_retweet from twitter_data t where search_id = '" + searchID + "' order by t.time_created_at DESC";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					resultSetEmpty = false;

					// recupero il time del tweet e lo converto ai mills
					java.sql.Timestamp timeFromDB = rs.getTimestamp("time_created_at");
					Calendar tweetTime = GregorianCalendar.getInstance();

					tweetTime.setTime(timeFromDB);

					tweetTime.set(Calendar.SECOND, 0);
					tweetTime.set(Calendar.MILLISECOND, 0);
					tweetTime.set(Calendar.MINUTE, 0);

					if (rs.isFirst()) {

						Calendar timeMax = GregorianCalendar.getInstance();
						Calendar timeMin = GregorianCalendar.getInstance();

						timeMax.setTimeInMillis(tweetTime.getTimeInMillis());
						timeMin.setTimeInMillis(tweetTime.getTimeInMillis());

						timeMax.add(Calendar.DAY_OF_MONTH, 1);
						timeMax.set(Calendar.HOUR_OF_DAY, 0);
						timeMax.set(Calendar.MINUTE, 0);
						timeMax.set(Calendar.SECOND, 0);
						timeMax.set(Calendar.MILLISECOND, 0);

						timeMin.set(Calendar.HOUR_OF_DAY, 0);
						timeMin.set(Calendar.MINUTE, 0);
						timeMin.set(Calendar.SECOND, 0);
						timeMin.set(Calendar.MILLISECOND, 0);

						lowerBound = timeMin.getTimeInMillis();
						upperBound = timeMax.getTimeInMillis();

						while (timeMin.compareTo(timeMax) <= 0) {

							tweetsMap.put(timeMin.getTimeInMillis(), 0);
							rtsMap.put(timeMin.getTimeInMillis(), 0);

							timeMin.add(Calendar.HOUR_OF_DAY, 1);
						}
					}

					long tweetTimeMills = tweetTime.getTimeInMillis();

					// cerco se il tweet è un RT
					boolean isRetweet = rs.getBoolean("is_retweet");

					if (tweetsMap.containsKey(tweetTimeMills)) {
						int tweets = tweetsMap.get(tweetTimeMills);
						tweets++;
						tweetsMap.put(tweetTimeMills, tweets);

						if (isRetweet) {
							int retweets = rtsMap.get(tweetTimeMills);
							retweets++;
							rtsMap.put(tweetTimeMills, retweets);
						}
					}
				}
			}

			if (resultSetEmpty) {
				Calendar timeMax = GregorianCalendar.getInstance();
				Calendar timeMin = GregorianCalendar.getInstance();

				timeMax.add(Calendar.DAY_OF_MONTH, 1);
				timeMax.set(Calendar.HOUR_OF_DAY, 0);
				timeMax.set(Calendar.MINUTE, 0);
				timeMax.set(Calendar.SECOND, 0);
				timeMax.set(Calendar.MILLISECOND, 0);

				timeMin.set(Calendar.HOUR_OF_DAY, 0);
				timeMin.set(Calendar.MINUTE, 0);
				timeMin.set(Calendar.SECOND, 0);
				timeMin.set(Calendar.MILLISECOND, 0);

				lowerBound = timeMin.getTimeInMillis();
				upperBound = timeMax.getTimeInMillis();

				while (timeMin.compareTo(timeMax) <= 0) {

					tweetsMap.put(timeMin.getTimeInMillis(), 0);
					rtsMap.put(timeMin.getTimeInMillis(), 0);

					timeMin.add(Calendar.HOUR_OF_DAY, 1);
				}

			}

			for (Map.Entry<Long, Integer> entry : tweetsMap.entrySet()) {

				long time = entry.getKey();
				int nTweets = entry.getValue();
				int nRTs = rtsMap.get(time);
				TwitterTimelinePojo obj = new TwitterTimelinePojo(time, nTweets, nRTs, lowerBound, upperBound);
				timelineChartObjs.add(obj);
			}

		} catch (Exception e) {
			System.out.println("**** connection failed: " + e);
		}

		return timelineChartObjs;

	}

	public List<TwitterTimelinePojo> getTimelineObjsWeekly(String searchIDStr) {

		long searchID = Long.parseLong(searchIDStr);

		List<TwitterTimelinePojo> timelineChartObjs = new ArrayList<TwitterTimelinePojo>();

		LinkedHashMap<Long, Integer> tweetsMap = new LinkedHashMap<Long, Integer>();
		LinkedHashMap<Long, Integer> rtsMap = new LinkedHashMap<Long, Integer>();

		long lowerBound = -1;
		long upperBound = -1;

		boolean resultSetEmpty = true;

		String sqlQuery = "SELECT t.date_created_at, t.is_retweet from twitter_data t where search_id = '" + searchID + "'  order by t.date_created_at DESC";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					resultSetEmpty = false;

					// recupero il time del tweet e lo converto ai mills
					java.sql.Date timeFromDB = rs.getDate("date_created_at");
					Calendar tweetTime = GregorianCalendar.getInstance();
					tweetTime.setTime(timeFromDB);

					// tweetTime.set(Calendar.SECOND, 0);
					// tweetTime.set(Calendar.MILLISECOND, 0);
					// tweetTime.set(Calendar.MINUTE, 0);
					// tweetTime.set(Calendar.HOUR_OF_DAY, 0);

					if (rs.isFirst()) {

						Calendar timeMax = GregorianCalendar.getInstance();
						Calendar timeMin = GregorianCalendar.getInstance();

						timeMax.setTimeInMillis(tweetTime.getTimeInMillis());
						timeMin.setTimeInMillis(tweetTime.getTimeInMillis());

						timeMax.add(Calendar.DAY_OF_MONTH, 1);
						// timeMax.set(Calendar.HOUR_OF_DAY, 0);
						// timeMax.set(Calendar.MINUTE, 0);
						// timeMax.set(Calendar.SECOND, 0);
						// timeMax.set(Calendar.MILLISECOND, 0);

						timeMin.add(Calendar.DAY_OF_MONTH, -6);
						// timeMin.set(Calendar.HOUR_OF_DAY, 0);
						// timeMin.set(Calendar.MINUTE, 0);
						// timeMin.set(Calendar.SECOND, 0);
						// timeMin.set(Calendar.MILLISECOND, 0);

						lowerBound = timeMin.getTimeInMillis();
						upperBound = timeMax.getTimeInMillis();

						while (timeMin.compareTo(timeMax) <= 0) {

							tweetsMap.put(timeMin.getTimeInMillis(), 0);
							rtsMap.put(timeMin.getTimeInMillis(), 0);

							timeMin.add(Calendar.DAY_OF_MONTH, 1);

						}
					}

					long tweetTimeMills = tweetTime.getTimeInMillis();

					// cerco se il tweet è un RT
					boolean isRetweet = rs.getBoolean("is_retweet");

					if (tweetsMap.containsKey(tweetTimeMills)) {
						int tweets = tweetsMap.get(tweetTimeMills);
						tweets++;
						tweetsMap.put(tweetTimeMills, tweets);

						if (isRetweet) {
							int retweets = rtsMap.get(tweetTimeMills);
							retweets++;
							rtsMap.put(tweetTimeMills, retweets);
						}
					}
				}
			}

			if (resultSetEmpty) {

				Calendar timeMax = GregorianCalendar.getInstance();
				Calendar timeMin = GregorianCalendar.getInstance();

				timeMax.add(Calendar.DAY_OF_MONTH, 1);
				timeMax.set(Calendar.HOUR_OF_DAY, 0);
				timeMax.set(Calendar.MINUTE, 0);
				timeMax.set(Calendar.SECOND, 0);
				timeMax.set(Calendar.MILLISECOND, 0);

				timeMin.add(Calendar.DAY_OF_MONTH, -6);
				timeMin.set(Calendar.HOUR_OF_DAY, 0);
				timeMin.set(Calendar.MINUTE, 0);
				timeMin.set(Calendar.SECOND, 0);
				timeMin.set(Calendar.MILLISECOND, 0);

				lowerBound = timeMin.getTimeInMillis();
				upperBound = timeMax.getTimeInMillis();

				while (timeMin.compareTo(timeMax) <= 0) {

					tweetsMap.put(timeMin.getTimeInMillis(), 0);
					rtsMap.put(timeMin.getTimeInMillis(), 0);

					timeMin.add(Calendar.DAY_OF_MONTH, 1);

				}

			}

			for (Map.Entry<Long, Integer> entry : tweetsMap.entrySet()) {

				long time = entry.getKey();
				int nTweets = entry.getValue();
				int nRTs = rtsMap.get(time);
				TwitterTimelinePojo obj = new TwitterTimelinePojo(time, nTweets, nRTs, lowerBound, upperBound);
				timelineChartObjs.add(obj);
			}

		} catch (Exception e) {
			System.out.println("**** connection failed: " + e);
		}

		return timelineChartObjs;

	}

	public List<TwitterTimelinePojo> getTimelineObjsMonthly(String searchIDStr) {

		long searchID = Long.parseLong(searchIDStr);

		List<TwitterTimelinePojo> timelineChartObjs = new ArrayList<TwitterTimelinePojo>();

		LinkedHashMap<Long, Integer> tweetsMap = new LinkedHashMap<Long, Integer>();
		LinkedHashMap<Long, Integer> rtsMap = new LinkedHashMap<Long, Integer>();

		long lowerBound = -1;
		long upperBound = -1;

		boolean resultSetEmpty = true;

		String sqlQuery = "SELECT t.date_created_at, t.is_retweet from twitter_data t where search_id = '" + searchID + "' order by t.date_created_at DESC";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					resultSetEmpty = false;

					// recupero il time del tweet e lo converto ai mills
					java.sql.Date timeFromDB = rs.getDate("date_created_at");
					Calendar tweetTime = GregorianCalendar.getInstance();
					tweetTime.setTime(timeFromDB);

					// tweetTime.set(Calendar.SECOND, 0);
					// tweetTime.set(Calendar.MILLISECOND, 0);
					// tweetTime.set(Calendar.MINUTE, 0);
					// tweetTime.set(Calendar.HOUR_OF_DAY, 0);

					if (rs.isFirst()) {

						Calendar timeMax = GregorianCalendar.getInstance();
						Calendar timeMin = GregorianCalendar.getInstance();

						timeMax.setTimeInMillis(tweetTime.getTimeInMillis());
						timeMin.setTimeInMillis(tweetTime.getTimeInMillis());

						timeMax.add(Calendar.DAY_OF_MONTH, 1);
						// timeMax.set(Calendar.HOUR_OF_DAY, 0);
						// timeMax.set(Calendar.MINUTE, 0);
						// timeMax.set(Calendar.SECOND, 0);
						// timeMax.set(Calendar.MILLISECOND, 0);

						timeMin.add(Calendar.MONTH, -1);
						// timeMin.set(Calendar.HOUR_OF_DAY, 0);
						// timeMin.set(Calendar.MINUTE, 0);
						// timeMin.set(Calendar.SECOND, 0);
						// timeMin.set(Calendar.MILLISECOND, 0);

						lowerBound = timeMin.getTimeInMillis();
						upperBound = timeMax.getTimeInMillis();

						while (timeMin.compareTo(timeMax) <= 0) {

							tweetsMap.put(timeMin.getTimeInMillis(), 0);
							rtsMap.put(timeMin.getTimeInMillis(), 0);

							timeMin.add(Calendar.DAY_OF_MONTH, 1);

						}
					}

					long tweetTimeMills = tweetTime.getTimeInMillis();

					// cerco se il tweet è un RT
					boolean isRetweet = rs.getBoolean("is_retweet");

					if (tweetsMap.containsKey(tweetTimeMills)) {
						int tweets = tweetsMap.get(tweetTimeMills);
						tweets++;
						tweetsMap.put(tweetTimeMills, tweets);

						if (isRetweet) {
							int retweets = rtsMap.get(tweetTimeMills);
							retweets++;
							rtsMap.put(tweetTimeMills, retweets);
						}
					}
				}
			}

			if (resultSetEmpty) {

				Calendar timeMax = GregorianCalendar.getInstance();
				Calendar timeMin = GregorianCalendar.getInstance();

				timeMax.add(Calendar.DAY_OF_MONTH, 1);
				timeMax.set(Calendar.HOUR_OF_DAY, 0);
				timeMax.set(Calendar.MINUTE, 0);
				timeMax.set(Calendar.SECOND, 0);
				timeMax.set(Calendar.MILLISECOND, 0);

				timeMin.add(Calendar.MONTH, -1);
				timeMin.set(Calendar.HOUR_OF_DAY, 0);
				timeMin.set(Calendar.MINUTE, 0);
				timeMin.set(Calendar.SECOND, 0);
				timeMin.set(Calendar.MILLISECOND, 0);

				lowerBound = timeMin.getTimeInMillis();
				upperBound = timeMax.getTimeInMillis();

				while (timeMin.compareTo(timeMax) <= 0) {

					tweetsMap.put(timeMin.getTimeInMillis(), 0);
					rtsMap.put(timeMin.getTimeInMillis(), 0);

					timeMin.add(Calendar.DAY_OF_MONTH, 1);

				}
			}

			for (Map.Entry<Long, Integer> entry : tweetsMap.entrySet()) {

				long time = entry.getKey();
				int nTweets = entry.getValue();
				int nRTs = rtsMap.get(time);
				TwitterTimelinePojo obj = new TwitterTimelinePojo(time, nTweets, nRTs, lowerBound, upperBound);
				timelineChartObjs.add(obj);
			}

		} catch (Exception e) {
			System.out.println("**** connection failed: " + e);
		}

		return timelineChartObjs;

	}

	public List<TwitterTimelinePojo> getTimelineObjsRangeTime(String searchIDStr) {

		logger.debug("Method getTimelineObjsRange(): Start");

		long searchID = Long.parseLong(searchIDStr);

		List<TwitterTimelinePojo> timelineChartObjs = new ArrayList<TwitterTimelinePojo>();

		LinkedHashMap<Long, Integer> tweetsMap = new LinkedHashMap<Long, Integer>();
		LinkedHashMap<Long, Integer> rtsMap = new LinkedHashMap<Long, Integer>();

		long lowerBound = -1;
		long upperBound = -1;

		String sqlQuery = "SELECT t.time_created_at, t.is_retweet from twitter_data t where search_id = '" + searchID + "'  order by t.time_created_at asc";

		try {

			String rangeDateMin = this.getMinTimeSearch(searchIDStr);
			String rangeDateMax = this.getMaxTimeSearch(searchIDStr);

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			Calendar calDateMin = GregorianCalendar.getInstance();
			Calendar calDateMax = GregorianCalendar.getInstance();

			calDateMin.setTime(sdf.parse(rangeDateMin));
			calDateMax.setTime(sdf.parse(rangeDateMax));

			calDateMin.set(Calendar.MINUTE, 0);
			calDateMin.set(Calendar.SECOND, 0);
			calDateMin.set(Calendar.MILLISECOND, 0);

			calDateMax.set(Calendar.MINUTE, 0);
			calDateMax.set(Calendar.SECOND, 0);
			calDateMax.set(Calendar.MILLISECOND, 0);

			lowerBound = calDateMin.getTimeInMillis();
			upperBound = calDateMax.getTimeInMillis();

			while (calDateMin.compareTo(calDateMax) <= 0) {

				tweetsMap.put(calDateMin.getTimeInMillis(), 0);
				rtsMap.put(calDateMin.getTimeInMillis(), 0);

				calDateMin.add(Calendar.HOUR_OF_DAY, 1);

			}

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					// recupero il time del tweet e lo converto ai mills
					java.sql.Timestamp timeFromDB = rs.getTimestamp("time_created_at");
					Calendar tweetTime = GregorianCalendar.getInstance();
					tweetTime.setTime(timeFromDB);

					tweetTime.set(Calendar.SECOND, 0);
					tweetTime.set(Calendar.MILLISECOND, 0);
					tweetTime.set(Calendar.MINUTE, 0);
					// tweetTime.set(Calendar.HOUR_OF_DAY, 0);

					long tweetTimeMills = tweetTime.getTimeInMillis();

					// cerco se il tweet è un RT
					boolean isRetweet = rs.getBoolean("is_retweet");

					if (tweetsMap.containsKey(tweetTimeMills)) {
						int tweets = tweetsMap.get(tweetTimeMills);
						tweets++;
						tweetsMap.put(tweetTimeMills, tweets);

						if (isRetweet) {
							int retweets = rtsMap.get(tweetTimeMills);
							retweets++;
							rtsMap.put(tweetTimeMills, retweets);
						}
					} else {
						tweetsMap.put(tweetTimeMills, 1);

						if (isRetweet) {
							rtsMap.put(tweetTimeMills, 1);
						}
					}

					if (rs.isFirst()) {
						lowerBound = tweetTimeMills;
					}

					if (rs.isLast()) {
						upperBound = tweetTimeMills;
					}
				}
			}

			for (Map.Entry<Long, Integer> entry : tweetsMap.entrySet()) {

				long time = entry.getKey();
				int nTweets = entry.getValue();
				int nRTs = rtsMap.get(time);
				TwitterTimelinePojo obj = new TwitterTimelinePojo(time, nTweets, nRTs, lowerBound, upperBound);
				timelineChartObjs.add(obj);
			}

		} catch (Exception e) {
			logger.debug("Method getTimelineObjsRange(): Error calculating timeline for search: " + searchID + " - " + e);
		}

		logger.debug("Method getTimelineObjsRange(): End");
		return timelineChartObjs;

	}

	public List<TwitterTimelinePojo> getTimelineObjsRangeDate(String searchIDStr) {

		logger.debug("Method getTimelineObjsRange(): Start");

		long searchID = Long.parseLong(searchIDStr);

		List<TwitterTimelinePojo> timelineChartObjs = new ArrayList<TwitterTimelinePojo>();

		LinkedHashMap<Long, Integer> tweetsMap = new LinkedHashMap<Long, Integer>();
		LinkedHashMap<Long, Integer> rtsMap = new LinkedHashMap<Long, Integer>();

		long lowerBound = -1;
		long upperBound = -1;

		String sqlQuery = "SELECT t.date_created_at, t.is_retweet from twitter_data t where search_id = '" + searchID + "'  order by t.date_created_at asc";

		try {

			String rangeDateMin = this.getMinTimeSearch(searchIDStr);
			String rangeDateMax = this.getMaxTimeSearch(searchIDStr);

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			Calendar calDateMin = GregorianCalendar.getInstance();
			Calendar calDateMax = GregorianCalendar.getInstance();

			calDateMin.setTime(sdf.parse(rangeDateMin));
			calDateMax.setTime(sdf.parse(rangeDateMax));

			calDateMin.set(Calendar.HOUR_OF_DAY, 0);
			calDateMin.set(Calendar.MINUTE, 0);
			calDateMin.set(Calendar.SECOND, 0);
			calDateMin.set(Calendar.MILLISECOND, 0);

			calDateMax.set(Calendar.HOUR_OF_DAY, 0);
			calDateMax.set(Calendar.MINUTE, 0);
			calDateMax.set(Calendar.SECOND, 0);
			calDateMax.set(Calendar.MILLISECOND, 0);

			lowerBound = calDateMin.getTimeInMillis();
			upperBound = calDateMax.getTimeInMillis();

			while (calDateMin.compareTo(calDateMax) <= 0) {

				tweetsMap.put(calDateMin.getTimeInMillis(), 0);
				rtsMap.put(calDateMin.getTimeInMillis(), 0);

				calDateMin.add(Calendar.DAY_OF_MONTH, 1);

			}

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					// recupero il time del tweet e lo converto ai mills
					java.sql.Date timeFromDB = rs.getDate("date_created_at");
					Calendar tweetTime = GregorianCalendar.getInstance();
					tweetTime.setTime(timeFromDB);

					tweetTime.set(Calendar.SECOND, 0);
					tweetTime.set(Calendar.MILLISECOND, 0);
					tweetTime.set(Calendar.MINUTE, 0);
					tweetTime.set(Calendar.HOUR_OF_DAY, 0);

					long tweetTimeMills = tweetTime.getTimeInMillis();

					// cerco se il tweet è un RT
					boolean isRetweet = rs.getBoolean("is_retweet");

					if (tweetsMap.containsKey(tweetTimeMills)) {
						int tweets = tweetsMap.get(tweetTimeMills);
						tweets++;
						tweetsMap.put(tweetTimeMills, tweets);

						if (isRetweet) {
							int retweets = rtsMap.get(tweetTimeMills);
							retweets++;
							rtsMap.put(tweetTimeMills, retweets);
						}
					} else {
						tweetsMap.put(tweetTimeMills, 1);

						if (isRetweet) {
							rtsMap.put(tweetTimeMills, 1);
						}
					}

					if (rs.isFirst()) {
						lowerBound = tweetTimeMills;
					}

					if (rs.isLast()) {
						upperBound = tweetTimeMills;
					}
				}
			}

			for (Map.Entry<Long, Integer> entry : tweetsMap.entrySet()) {

				long time = entry.getKey();
				int nTweets = entry.getValue();
				int nRTs = rtsMap.get(time);
				TwitterTimelinePojo obj = new TwitterTimelinePojo(time, nTweets, nRTs, lowerBound, upperBound);
				timelineChartObjs.add(obj);
			}

		} catch (Exception e) {
			logger.debug("Method getTimelineObjsRange(): Error calculating timeline for search: " + searchID + " - " + e);
		}

		logger.debug("Method getTimelineObjsRange(): End");
		return timelineChartObjs;

	}

	/**
	 * This method finds the min date for a search
	 *
	 * @param searchIDStr
	 *            : search ID
	 * @return formatted minimum date for this search
	 */
	public String getMinDateSearch(String searchIDStr) {

		logger.debug("Method getMinDataSearch(): Start");

		long searchID = Long.parseLong(searchIDStr);

		String minDate = "";

		String sqlQuery = "SELECT date_created_at FROM twitter_data WHERE search_id = " + searchID + " ORDER BY date_created_at ASC";

		try {

			logger.debug("Method getMinDataSearch(): Retrieving minimum date for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				if (rs.next()) {
					java.sql.Date tempDate = rs.getDate("date_created_at");

					SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy");
					minDate = simpleDataFormatter.format(tempDate.getTime());
				}
			}

		} catch (Exception e) {
			logger.debug("Method getMinDataSearch(): Error retrieving minimum date for search: " + searchID + " - " + e);
		}

		logger.debug("Method getMinDataSearch(): End");
		return minDate;
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

			if (rs != null) {

				if (rs.next()) {
					java.sql.Date tempDate = rs.getDate("date_created_at");

					SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy");
					maxDate = simpleDataFormatter.format(tempDate.getTime());
				}
			}

		} catch (Exception e) {
			logger.debug("Method getMaxDateSearch(): Error retrieving max date for search: " + searchID + " - " + e);
		}

		logger.debug("Method getMaxDateSearch(): End");
		return maxDate;
	}

	public String getMinTimeSearch(String searchIDStr) {

		logger.debug("Method getMinTimeSearch(): Start");

		long searchID = Long.parseLong(searchIDStr);

		String minTime = "";

		String sqlQuery = "SELECT time_created_at FROM twitter_data WHERE search_id = " + searchID + " ORDER BY time_created_at ASC";

		try {

			logger.debug("Method getMinTimeSearch(): Retrieving minimum time for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				if (rs.next()) {
					java.sql.Timestamp tempTime = rs.getTimestamp("time_created_at");

					SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					minTime = simpleDataFormatter.format(tempTime.getTime());
				}
			}

		} catch (Exception e) {
			logger.debug("Method getMinTimeSearch(): Error retrieving minimum date for search: " + searchID + " - " + e);
		}

		logger.debug("Method getMinTimeSearch(): End");
		return minTime;
	}

	public String getMaxTimeSearch(String searchIDStr) {

		logger.debug("Method getMaxTimeSearch(): Start");

		long searchID = Long.parseLong(searchIDStr);

		String maxTime = "";

		String sqlQuery = "SELECT time_created_at FROM twitter_data WHERE search_id = " + searchID + " ORDER BY time_created_at DESC";

		try {

			logger.debug("Method getMaxTimeSearch(): Retrieving max time for search: " + searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				if (rs.next()) {
					java.sql.Timestamp tempTime = rs.getTimestamp("time_created_at");

					SimpleDateFormat simpleDataFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					maxTime = simpleDataFormatter.format(tempTime.getTime());
				}
			}

		} catch (Exception e) {
			logger.debug("Method getMaxTimeSearch(): Error retrieving max date for search: " + searchID + " - " + e);
		}

		logger.debug("Method getMaxTimeSearch(): End");
		return maxTime;
	}
}
