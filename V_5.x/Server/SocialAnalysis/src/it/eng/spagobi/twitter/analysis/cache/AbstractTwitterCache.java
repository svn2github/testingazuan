/** SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
package it.eng.spagobi.twitter.analysis.cache;

import it.eng.spagobi.bitly.analysis.pojos.BitlyLinkCategoryPojo;
import it.eng.spagobi.bitly.analysis.pojos.BitlyLinkPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterAccountToMonitorPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMessageObject;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMonitorSchedulerPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchSchedulerPojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Status;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public abstract class AbstractTwitterCache implements ITwitterCache {

	private static final Logger logger = Logger.getLogger(AbstractTwitterCache.class);

	private String url;
	private String driver;
	private String userName;
	private String password;
	private String tableName;

	public AbstractTwitterCache(String url, String driver, String userName, String password) {
		this.url = url;
		this.driver = driver;
		this.userName = userName;
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTableName() {
		if (tableName == null) {
			return "twitter_data";
		} else {
			return tableName;
		}
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public abstract Connection openConnection();

	@Override
	public abstract void closeConnection();

	@Override
	public abstract long insertTwitterSearch(TwitterSearchPojo twitterSearch);

	@Override
	public abstract void updateTwitterSearchLoading(long searchID, boolean isLoading);

	/**
	 * This method inserts a new twitter user
	 *
	 * @param twitterMessage
	 *            : object representing all data about a tweet and his author
	 *            (the user)
	 */
	public abstract void insertTwitterUser(TwitterMessageObject twitterMessage);

	/**
	 * This method insert the information about a tweet
	 *
	 * @param twitterMessage
	 *            : object representing a tweet
	 * @param searchID
	 *            : twitter search ID
	 */
	public abstract void insertTweet(TwitterMessageObject twitterMessage, long searchID);

	@Override
	public abstract void insertBitlyAnalysis(BitlyLinkPojo linkPojo, List<BitlyLinkCategoryPojo> linkCategoryPojos, long searchID);

	@Override
	public abstract void insertAccountToMonitor(TwitterAccountToMonitorPojo accountToMonitor);

	@Override
	public abstract CachedRowSet runQuery(String sqlQuery);

	@Override
	public abstract void stopStreamingSearch();

	@Override
	public abstract void insertTwitterSearchScheduler(TwitterSearchSchedulerPojo twitterScheduler);

	@Override
	public abstract void insertTwitterMonitorScheduler(TwitterMonitorSchedulerPojo twitterMonitorScheduler);

	@Override
	public abstract void deleteSearch(TwitterSearchPojo twitterSearch);

	@Override
	public abstract void updateStartingDateSearchScheduler(TwitterSearchSchedulerPojo twitterSearchSchedulerPojo);

	@Override
	public abstract TwitterMonitorSchedulerPojo stopSearchScheduler(TwitterSearchPojo twitterSearch);

	@Override
	public abstract void updateMonitorScheduler(TwitterMonitorSchedulerPojo twitterMonitorSchedulerPojo);

	@Override
	public abstract TwitterMonitorSchedulerPojo getTwitterMonitorScheduler(long searchID);

	@Override
	public final void saveTweet(Status tweet, String keyword, long searchID) throws Exception {

		TwitterMessageObject twitterMessage = new TwitterMessageObject(tweet);

		if (twitterMessage.getUserID() > 0) {
			insertTwitterUser(twitterMessage);
		}

		if (twitterMessage.getUserID() > 0 && twitterMessage.getTweetID() > 0) {
			insertTweet(twitterMessage, searchID);
		}

	}

	/**
	 * Method to geocode tweet user location
	 *
	 * @param location
	 *            : location input inside the user's twitter profile, not always
	 *            a "location"
	 * @param userTimeZone
	 *            : user time zone inside his twitter profile
	 * @return the short code of user location country
	 * @throws Exception
	 */

	public final String findCountryCodeFromUserLocation(String location, String userTimeZone) {

		logger.debug("Method findCountryCodeFromUserLocation(): Start");

		String countryCode = "";

		try {
			String locationEncoded = "";

			if (location != null && !location.equals("")) {
				locationEncoded = URLEncoder.encode(location, "UTF-8");
			} else {
				if (userTimeZone != null && !userTimeZone.equals("")) {
					locationEncoded = URLEncoder.encode(userTimeZone, "UTF-8");
				}
			}

			String urlString = "http://maps.googleapis.com/maps/api/geocode/json?address=" + locationEncoded;

			URL url = new URL(urlString);

			logger.debug("Method findCountryCodeFromUserLocation(): Calling GeoCode API..");

			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String line = null;

			String json = "";
			while ((line = reader.readLine()) != null) {
				json = json + line;
			}

			reader.close();

			JSONObject obj = new JSONObject(json);

			String status = obj.getString("status");

			if (status.equals("OK")) {

				logger.debug("Method findCountryCodeFromUserLocation(): Response OK. Analyzing result..");
				JSONArray resultsArray = obj.getJSONArray("results");

				if (resultsArray != null && resultsArray.length() > 0) {
					JSONObject obj3 = resultsArray.getJSONObject(0);
					JSONArray aComponentsArray = obj3.getJSONArray("address_components");

					if (aComponentsArray != null && aComponentsArray.length() > 0) {
						for (int i = 0; i < aComponentsArray.length(); i++) {

							JSONObject tempObj = aComponentsArray.getJSONObject(i);
							JSONArray types = tempObj.getJSONArray("types");

							if (types != null && types.length() > 0) {
								String firstType = types.getString(0);

								if (firstType.equals("country")) {
									countryCode = tempObj.getString("short_name");
								}
							}
						}
					}
				}
			}
		} catch (java.net.ConnectException e) {
			logger.debug("Method findCountryCodeFromUserLocation(): Error for connection timeout calling GeoCoding" + e.getMessage());
		} catch (IOException e) {
			logger.debug("Method findCountryCodeFromUserLocation(): Error for IO operations GeoCoding" + e.getMessage());
		} catch (JSONException e) {
			logger.debug("Method findCountryCodeFromUserLocation(): Error for JSON parsing after GeoCoding call" + e.getMessage());
		}

		logger.debug("Method findCountryCodeFromUserLocation(): End");
		return countryCode;

	}

}
