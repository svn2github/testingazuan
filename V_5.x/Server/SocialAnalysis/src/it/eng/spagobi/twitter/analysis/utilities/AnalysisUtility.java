/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.utilities;

import it.eng.spagobi.twitter.analysis.entities.TwitterMonitorScheduler;
import it.eng.spagobi.twitter.analysis.enums.UpToTypeEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class AnalysisUtility {

	private static final Logger logger = Logger.getLogger(AnalysisUtility.class);

	public static Calendar setMonitorSchedulerEndingDate(TwitterMonitorScheduler twitterMonitorScheduler) {

		Calendar endingCalendar = GregorianCalendar.getInstance();
		int upToValue = twitterMonitorScheduler.getUpToValue();
		UpToTypeEnum upToType = twitterMonitorScheduler.getUpToType();

		// Calculating the ending date
		if (upToType == UpToTypeEnum.Day) {
			endingCalendar.add(Calendar.DAY_OF_MONTH, upToValue);
		} else if (upToType == UpToTypeEnum.Week) {
			endingCalendar.add(Calendar.DAY_OF_MONTH, (upToValue) * 7);
		} else if (upToType == UpToTypeEnum.Month) {
			endingCalendar.add(Calendar.DAY_OF_MONTH, (upToValue) * 30);
		}

		return endingCalendar;
	}

	/**
	 * Method to geocode tweet user location
	 *
	 * @param location
	 *            : location input inside the user's twitter profile, not always a "location"
	 * @param userTimeZone
	 *            : user time zone inside his twitter profile
	 * @return the short code of user location country
	 * @throws Exception
	 */

	public static final String findCountryCodeFromUserLocation(String location, String userTimeZone) {

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
