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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
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

public class TwitterTimelineDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterTimelineDataProcessor.class);

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	private LinkedHashMap<Long, Integer> tweetsMap = new LinkedHashMap<Long, Integer>();
	private LinkedHashMap<Long, Integer> rtsMap = new LinkedHashMap<Long, Integer>();

	private List<Calendar> roundedTimes = new ArrayList<Calendar>();

	List<TwitterTimelinePojo> timelineChartObjs = new ArrayList<TwitterTimelinePojo>();

	private long lowerBound = -1;
	private long upperBound = -1;

	public LinkedHashMap<Long, Integer> getTweetsMap() {
		return tweetsMap;
	}

	public void setTweetsMap(LinkedHashMap<Long, Integer> tweetsMap) {
		this.tweetsMap = tweetsMap;
	}

	public LinkedHashMap<Long, Integer> getRtsMap() {
		return rtsMap;
	}

	public void setRtsMap(LinkedHashMap<Long, Integer> rtsMap) {
		this.rtsMap = rtsMap;
	}

	public List<Calendar> getRoundedTimes() {
		return roundedTimes;
	}

	public void setRoundedTimes(List<Calendar> roundedTimes) {
		this.roundedTimes = roundedTimes;
	}

	public List<TwitterTimelinePojo> getTimelineChartObjs() {
		return timelineChartObjs;
	}

	public void setTimelineChartObjs(List<TwitterTimelinePojo> timelineChartObjs) {
		this.timelineChartObjs = timelineChartObjs;
	}

	public long getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(long lowerBound) {
		this.lowerBound = lowerBound;
	}

	public long getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(long upperBound) {
		this.upperBound = upperBound;
	}

	public List<TwitterTimelinePojo> getTimelineObjs(String searchIDStr, String timeFilter) {

		logger.debug("Method getTimelineObjs(): Start");

		long searchID = Long.parseLong(searchIDStr);

		// initialize structures
		this.initializeTimeline(this.getTweetsTimes(searchIDStr), timeFilter);

		String sqlQuery = "SELECT t.time_created_at, t.is_retweet from twitter_data t where search_id = '" + searchID + "' order by t.time_created_at DESC";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					boolean isRetweet = rs.getBoolean("is_retweet");
					java.sql.Timestamp timeFromDB = rs.getTimestamp("time_created_at");

					Calendar tweetTime = GregorianCalendar.getInstance();

					tweetTime.setTime(timeFromDB);

					tweetTime = roundTime(timeFilter, tweetTime);

					long tweetTimeMills = tweetTime.getTimeInMillis();

					if (this.tweetsMap.containsKey(tweetTimeMills)) {
						int tweets = this.tweetsMap.get(tweetTimeMills);
						tweets++;
						this.tweetsMap.put(tweetTimeMills, tweets);

						if (isRetweet) {
							int retweets = this.rtsMap.get(tweetTimeMills);
							retweets++;
							this.rtsMap.put(tweetTimeMills, retweets);
						}
					}
				}
			}

			for (Map.Entry<Long, Integer> entry : this.tweetsMap.entrySet()) {

				long time = entry.getKey();
				int nTweets = entry.getValue();
				int nRTs = this.rtsMap.get(time);
				TwitterTimelinePojo obj = new TwitterTimelinePojo(time, nTweets, nRTs, lowerBound, upperBound);
				this.timelineChartObjs.add(obj);
			}

		} catch (Exception e) {
			System.out.println("**** connection failed: " + e);
		}

		logger.debug("Method getTimelineObjs(): End");
		return this.timelineChartObjs;

	}

	public List<TwitterTimelinePojo> getTimelineWeeklyRound(String searchIDStr) {

		long searchID = Long.parseLong(searchIDStr);

		List<TwitterTimelinePojo> timelineChartObjs = new ArrayList<TwitterTimelinePojo>();

		LinkedHashMap<Long, Integer> tweetsMap = new LinkedHashMap<Long, Integer>();
		LinkedHashMap<Long, Integer> rtsMap = new LinkedHashMap<Long, Integer>();

		long lowerBound = -1;
		long upperBound = -1;

		boolean resultSetEmpty = true;

		List<Calendar> times = this.getTweetsTimes(searchIDStr);

		if (times != null && times.size() > 0) {
			for (Calendar tempTime : times) {

			}
		}

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

	public JSONObject getFollowersTimelineObjs(String searchIDStr) {

		logger.debug("Method getFollowersTimelineObjs(): Start");

		long searchID = Long.parseLong(searchIDStr);

		JSONObject accountFollowersJSON = new JSONObject();

		String type = getVisualizationType(searchIDStr);

		Calendar minTimeAccount = getMinTimeResourceToMonitor(searchIDStr, "twitter_accounts_to_monitor");
		Calendar maxTimeAccount = getMaxTimeResourceToMonitor(searchIDStr, "twitter_accounts_to_monitor");

		try {

			String sqlAccountQuery = "SELECT account_name from twitter_accounts_to_monitor where search_id = '" + searchID + "' GROUP BY account_name ";

			CachedRowSet accountRS = twitterCache.runQuery(sqlAccountQuery);

			if (accountRS != null) {

				JSONArray results = new JSONArray();

				while (accountRS.next()) {

					String account = accountRS.getString("account_name");

					JSONObject element = new JSONObject();
					element.put("label", account);

					String sqlTimestampQuery = "SELECT timestamp, followers_count from twitter_accounts_to_monitor where search_id = '" + searchID + "' and account_name = '"
							+ account + "' ORDER BY timestamp asc";

					CachedRowSet timestampRS = twitterCache.runQuery(sqlTimestampQuery);

					if (timestampRS != null) {

						JSONArray data = new JSONArray();

						while (timestampRS.next()) {

							java.sql.Timestamp timestamp = timestampRS.getTimestamp("timestamp");
							int followersCount = timestampRS.getInt("followers_count");

							Calendar timestampCalendar = GregorianCalendar.getInstance();
							timestampCalendar.setTime(timestamp);

							if (type != null && !type.equals("")) {
								if (type.equalsIgnoreCase("Hour")) {

									timestampCalendar.set(Calendar.MINUTE, 0);
									timestampCalendar.set(Calendar.SECOND, 0);
									timestampCalendar.set(Calendar.MILLISECOND, 0);

								} else if (type.equalsIgnoreCase("Day")) {

									timestampCalendar.set(Calendar.HOUR_OF_DAY, 0);
									timestampCalendar.set(Calendar.MINUTE, 0);
									timestampCalendar.set(Calendar.SECOND, 0);
									timestampCalendar.set(Calendar.MILLISECOND, 0);

									minTimeAccount.set(Calendar.HOUR_OF_DAY, 0);
									maxTimeAccount.set(Calendar.HOUR_OF_DAY, 0);

								}
							}

							long time = timestampCalendar.getTimeInMillis();

							JSONObject dataElement = new JSONObject();
							dataElement.put("mills", time);
							dataElement.put("followers", followersCount);

							data.put(dataElement);
						}

						element.put("data", data);
					}

					results.put(element);
				}

				accountFollowersJSON.put("results", results);
				accountFollowersJSON.put("lowerBound", minTimeAccount.getTimeInMillis());
				accountFollowersJSON.put("upperBound", maxTimeAccount.getTimeInMillis());
				accountFollowersJSON.put("type", type);

			}

		} catch (Exception e) {
			logger.debug("Method getFollowersTimelineObjs(): Error calculating timeline for search: " + searchID + " - " + e);
		}

		logger.debug("Method getFollowersTimelineObjs(): End");

		// logger.debug(accountFollowersJSON.toString());
		return accountFollowersJSON;

	}

	public JSONObject getBitlyClicksTimelineObjs(String searchIDStr) {

		logger.debug("Method getBitlyClicksTimelineObjs: Start");

		long searchID = Long.parseLong(searchIDStr);

		JSONObject linkClicksJSON = new JSONObject();

		String type = getVisualizationType(searchIDStr);

		Calendar minTimeAccount = getMinTimeResourceToMonitor(searchIDStr, "twitter_links_to_monitor");
		Calendar maxTimeAccount = getMaxTimeResourceToMonitor(searchIDStr, "twitter_links_to_monitor");

		try {

			String sqlLinkQuery = "SELECT link from twitter_links_to_monitor where search_id = '" + searchID + "' GROUP BY link ";

			CachedRowSet linkRS = twitterCache.runQuery(sqlLinkQuery);

			if (linkRS != null) {

				JSONArray results = new JSONArray();

				while (linkRS.next()) {

					String link = linkRS.getString("link");

					JSONObject element = new JSONObject();
					element.put("label", link);

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

							if (type != null && !type.equals("")) {
								if (type.equalsIgnoreCase("Hour")) {

									timestampCalendar.set(Calendar.MINUTE, 0);
									timestampCalendar.set(Calendar.SECOND, 0);
									timestampCalendar.set(Calendar.MILLISECOND, 0);

								} else if (type.equalsIgnoreCase("Day")) {

									timestampCalendar.set(Calendar.HOUR_OF_DAY, 0);
									timestampCalendar.set(Calendar.MINUTE, 0);
									timestampCalendar.set(Calendar.SECOND, 0);
									timestampCalendar.set(Calendar.MILLISECOND, 0);

									minTimeAccount.set(Calendar.HOUR_OF_DAY, 0);
									maxTimeAccount.set(Calendar.HOUR_OF_DAY, 0);

								}
							}

							long time = timestampCalendar.getTimeInMillis();

							JSONObject dataElement = new JSONObject();
							dataElement.put("mills", time);
							dataElement.put("clicks", clicksCount);

							data.put(dataElement);
						}

						element.put("data", data);
					}

					results.put(element);
				}

				linkClicksJSON.put("results", results);
				linkClicksJSON.put("lowerBound", minTimeAccount.getTimeInMillis());
				linkClicksJSON.put("upperBound", maxTimeAccount.getTimeInMillis());
				linkClicksJSON.put("type", type);

			}

		} catch (Exception e) {
			logger.debug("Method getBitlyClicksTimelineObjs: Error calculating timeline for search: " + searchID + " - " + e);
		}

		logger.debug("Method getBitlyClicksTimelineObjs: End");

		// logger.debug(accountFollowersJSON.toString());
		return linkClicksJSON;

	}

	private String getVisualizationType(String searchIDStr) {

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

	private List<Calendar> getTweetsTimes(String searchID) {
		logger.debug("Method getTweetTimes(): Start");

		List<Calendar> timesList = new ArrayList<Calendar>();

		String sqlQuery = "SELECT t.time_created_at from twitter_data t where search_id = '" + searchID + "' order by t.time_created_at DESC";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					// recupero il time del tweet e lo converto ai mills
					java.sql.Timestamp timeFromDB = rs.getTimestamp("time_created_at");

					Calendar tweetTime = GregorianCalendar.getInstance();
					tweetTime.setTime(timeFromDB);

					timesList.add(tweetTime);
				}
			}
		} catch (SQLException e) {
			logger.debug("Method getTweetTimes(): Error " + e.getMessage());
		}

		return timesList;

	}

	private void initializeTimeline(List<Calendar> times, String filter) {

		// clear fields
		this.roundedTimes = new ArrayList<Calendar>();
		this.timelineChartObjs = new ArrayList<TwitterTimelinePojo>();
		this.tweetsMap = new LinkedHashMap<Long, Integer>();
		this.rtsMap = new LinkedHashMap<Long, Integer>();
		this.upperBound = -1;
		this.lowerBound = -1;

		// initialize data

		if (times != null && times.size() > 0) {

			Calendar min = GregorianCalendar.getInstance();
			Calendar max = GregorianCalendar.getInstance();

			min.setTime(times.get(0).getTime());
			max.setTime(times.get(0).getTime());

			for (Calendar tempTime : times) {

				tempTime = roundTime(filter, tempTime);

				// max and min
				if (tempTime.compareTo(min) < 0) {
					min = tempTime;
				}
				if (tempTime.compareTo(max) > 0) {
					max = tempTime;
				}

				// new rounded time list
				this.roundedTimes.add(tempTime);

			}

			if (filter.equalsIgnoreCase("hours")) {

				min.add(Calendar.HOUR_OF_DAY, -1);
				max.add(Calendar.HOUR_OF_DAY, 1);

			} else if (filter.equalsIgnoreCase("days")) {

				min.add(Calendar.DAY_OF_MONTH, -1);
				max.add(Calendar.DAY_OF_MONTH, 1);

			} else if (filter.equalsIgnoreCase("weeks")) {

				min.add(Calendar.WEEK_OF_YEAR, -1);
				max.add(Calendar.WEEK_OF_YEAR, 1);
			}

			else if (filter.equalsIgnoreCase("months")) {

				min.add(Calendar.MONTH, -1);
				max.add(Calendar.MONTH, 1);
			}

			this.lowerBound = min.getTimeInMillis();
			this.upperBound = max.getTimeInMillis();

			while (min.compareTo(max) <= 0) {

				this.tweetsMap.put(min.getTimeInMillis(), 0);
				this.rtsMap.put(min.getTimeInMillis(), 0);

				if (filter.equalsIgnoreCase("hours")) {

					min.add(Calendar.HOUR_OF_DAY, 1);

				} else if (filter.equalsIgnoreCase("days")) {

					min.add(Calendar.DAY_OF_MONTH, 1);

				} else if (filter.equalsIgnoreCase("weeks")) {

					min.add(Calendar.WEEK_OF_YEAR, 1);
				}

				else if (filter.equalsIgnoreCase("months")) {

					min.add(Calendar.MONTH, 1);
				}

			}

		}

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
