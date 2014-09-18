/**

SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/

/**
 * @author Giorgio Federici (giorgio.federici@eng.it)
 *
 */

package it.eng.spagobi.twitter.analysis.api;

import it.eng.spagobi.twitter.analysis.dataprocessors.TwitterSearchDataProcessor;
import it.eng.spagobi.twitter.analysis.launcher.TwitterAnalysisLauncher;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMonitorSchedulerPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchSchedulerPojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import twitter4j.JSONException;
import twitter4j.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Path("/historicalSearch")
public class TwitterHistoricalSearchAPI {

	static final Logger logger = Logger.getLogger(TwitterHistoricalSearchAPI.class);

	// Save a new Twitter Search
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String search(@Context HttpServletRequest req) {

		logger.debug("Method search(): Start");

		TwitterAnalysisLauncher twitterLauncher = null;
		TwitterSearchSchedulerPojo twitterScheduler = null;
		TwitterMonitorSchedulerPojo twitterMonitorScheduler = null;
		TwitterSearchPojo twitterSearch = new TwitterSearchPojo();

		String languageCode = null;
		String dbType = "MySQL";

		String repeatType = "None";

		// reading the user input
		String searchType = req.getParameter("searchType");
		String keywords = req.getParameter("keywords");
		String links = req.getParameter("links");
		String accounts = req.getParameter("accounts");
		String documents = req.getParameter("documents");

		// String documents = req.getParameter("documents");

		// set ready parameters
		twitterSearch.setLanguageCode(languageCode);
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchType(searchType);
		twitterSearch.setKeywords(keywords);

		// set in the monitoring
		// twitterSearch.setLinks(links);

		twitterSearch.setAccounts(accounts);

		// if user is not specifying the label, create it with the keywords
		String label = req.getParameter("label");

		if (label == null || label.trim().equals("")) {

			logger.debug("Method search(): Blank label. Creation from keywords");

			String[] keywordsArr = keywords.split(",");
			for (int i = 0; i < keywordsArr.length; i++) {

				String tempKeyword = keywordsArr[i].trim();

				if (i == keywordsArr.length - 1) {
					label = label + tempKeyword;
				} else {
					label = label + tempKeyword + "_";
				}
			}
		}

		// set search label
		twitterSearch.setLabel(label);

		String repeatTypeField = req.getParameter("repeatType");
		String numberRepeat = req.getParameter("numberRepeat");

		if (req.getParameter("isRepeating") != null && numberRepeat != null && !numberRepeat.equals("") && repeatTypeField != null
				&& !repeatTypeField.equals("")) {

			logger.debug("Method search(): Search with scheduler");

			Calendar startingDate = GregorianCalendar.getInstance();

			startingDate.set(Calendar.MINUTE, 0);
			startingDate.set(Calendar.SECOND, 0);
			startingDate.set(Calendar.MILLISECOND, 0);

			int repeatFrequency = Integer.parseInt(numberRepeat);

			repeatType = repeatTypeField;

			if (repeatFrequency > 0) {

				if (repeatType.equals("Day")) {
					startingDate.add(Calendar.DAY_OF_MONTH, repeatFrequency);

				} else if (repeatType.equals("Hour")) {
					startingDate.add(Calendar.HOUR_OF_DAY, repeatFrequency);
				}

				twitterScheduler = new TwitterSearchSchedulerPojo(startingDate, repeatFrequency, repeatType);
			}

		}

		// set type frequency
		twitterSearch.setFrequency(repeatType);

		// set search scheduler
		twitterSearch.setTwitterScheduler(twitterScheduler);

		// now we take the decision abount the monitor scheduler. Check if there
		// resources to monitor..
		if ((links != null && !links.equals("")) || (accounts != null && !accounts.equals("")) || (documents != null && !documents.equals(""))) {

			String numberUpTo = req.getParameter("numberUpTo");
			String typeUpTo = req.getParameter("typeUpTo");

			String monitorFrequencyValue = req.getParameter("monitorFrequencyValue");
			String monitorFrequencyType = req.getParameter("monitorFrequencyType");

			if (monitorFrequencyValidation(monitorFrequencyValue, monitorFrequencyType) && monitorUpToValidation(numberUpTo, typeUpTo)) {

				twitterMonitorScheduler = new TwitterMonitorSchedulerPojo();

				twitterMonitorScheduler.setRepeatFrequency(Integer.parseInt(monitorFrequencyValue));
				twitterMonitorScheduler.setRepeatType(monitorFrequencyType);

				twitterMonitorScheduler.setUpToValue(Integer.parseInt(numberUpTo));
				twitterMonitorScheduler.setUpToType(typeUpTo);

				twitterMonitorScheduler.setActive(true);

				twitterMonitorScheduler.setAccounts(accounts);
				twitterMonitorScheduler.setLinks(links);
				twitterMonitorScheduler.setDocuments(documents);

			}

		}

		// set monitor scheduler
		twitterSearch.setTwitterMonitorScheduler(twitterMonitorScheduler);

		String numberStartingFrom = req.getParameter("numberStartingFrom");

		if (req.getParameter("isStartingFrom") != null && numberStartingFrom != null && !numberStartingFrom.equals("")) {

			logger.debug("Method search(): Search with a starting date");

			// Calendar for today
			Calendar actualDate = GregorianCalendar.getInstance();

			// Manage starting time with the user's input
			Calendar sinceDate = actualDate;

			// Type of starting from input: Hour or Day TODO: hours filter
			// if (typeStartingFrom.equals("Hour")) {
			// startingFromDate.add(Calendar.HOUR_OF_DAY,
			// -Integer.parseInt(numberStartingFrom));
			// }

			sinceDate.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(numberStartingFrom));

			// if isRound is checked, set time to previous hour
			// if (req.getParameter("isRound") != null) {
			// startingFromDate.set(Calendar.MINUTE, 0);
			// startingFromDate.set(Calendar.SECOND, 0);
			// startingFromDate.set(Calendar.MILLISECOND, 0);
			// }

			twitterSearch.setSinceDate(sinceDate);

			twitterLauncher = new TwitterAnalysisLauncher(twitterSearch);

		} else {

			logger.debug("Method search(): Search without dates (except API limits)");

			twitterLauncher = new TwitterAnalysisLauncher(twitterSearch);
		}

		long searchID = twitterLauncher.createhistoricalSearch();

		JSONObject resObj = new JSONObject();

		try {

			if (searchID > 0) {

				resObj.put("success", true);
				resObj.put("msg", "Twitter Search \"" + label + "\" inserted. Loading results..");

			} else {

				resObj.put("failure", true);
				resObj.put("msg", "Failure starting new search ");

			}
		} catch (JSONException e) {
			logger.error("Method search(): ERROR - " + e);
		}

		logger.debug("Method search(): End");

		return resObj.toString();
	}

	// Get the list of all Twitter Search
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getListSearch(@Context HttpServletRequest req) {

		List<TwitterSearchPojo> search = new ArrayList<TwitterSearchPojo>();

		search = new TwitterSearchDataProcessor().getTwitterSearchList("searchAPI");
		Gson gson = new Gson();
		JsonElement element = gson.toJsonTree(search, new TypeToken<List<TwitterSearchPojo>>() {
		}.getType());

		JsonArray jsonArray = element.getAsJsonArray();

		return jsonArray.toString();

	}

	// Delete a new Twitter Search
	@Path("/deleteSearch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@Context HttpServletRequest req) throws Exception {

		logger.debug("Method delete(): Start..");

		TwitterSearchPojo twitterSearch = new TwitterSearchPojo();

		String dbType = "MySQL";

		// reading the user input
		String searchID = req.getParameter("searchID");

		// set ready parameters
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchID(Long.parseLong(searchID));
		twitterSearch.setSearchType("searchAPI");

		TwitterAnalysisLauncher twitterAnalysisLauncher = new TwitterAnalysisLauncher(twitterSearch);
		twitterAnalysisLauncher.deleteSearch();

		JSONObject resObj = new JSONObject();

		try {
			resObj.put("success", true);
			resObj.put("msg", "Historical search \"" + searchID + "\" deleted");

		} catch (JSONException e) {
			logger.error("Method delete(): ERROR - " + e);
		}

		logger.debug("Method delete(): End");

		return resObj.toString();
	}

	// Stop the search scheduler and start monitor scheduler
	@Path("/stopSearchScheduler")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String stopSearchScheduler(@Context HttpServletRequest req) throws Exception {

		logger.debug("Method stopSearchScheduler(): Start..");

		TwitterSearchPojo twitterSearch = new TwitterSearchPojo();

		String dbType = "MySQL";

		// reading the user input
		String searchID = req.getParameter("searchID");

		// set ready parameters
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchID(Long.parseLong(searchID));

		TwitterAnalysisLauncher twitterAnalysisLauncher = new TwitterAnalysisLauncher(twitterSearch);
		twitterAnalysisLauncher.stopSearchScheduler();

		JSONObject resObj = new JSONObject();

		try {
			resObj.put("success", true);
			resObj.put("msg", "Historical search scheduler \"" + searchID + "\" stopped");

		} catch (JSONException e) {
			logger.error("Method stopSearchScheduler(): Error trying to stop search scheduler " + searchID + " - " + e.getMessage());
		}

		logger.debug("Method stopSearchScheduler(): End");

		return resObj.toString();
	}

	// Remove a failed search from historic search table
	@Path("/removeFailedSearch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String removeFailedSearch(@Context HttpServletRequest req) throws Exception {

		logger.debug("Method removeFailedSearch(): Start");

		TwitterSearchPojo twitterSearch = new TwitterSearchPojo();

		String dbType = "MySQL";

		// reading the user input
		String searchID = req.getParameter("searchID");

		// set ready parameters
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchID(Long.parseLong(searchID));
		twitterSearch.setSearchType("searchAPI");

		TwitterAnalysisLauncher twitterAnalysisLauncher = new TwitterAnalysisLauncher(twitterSearch);
		twitterAnalysisLauncher.removeFailedSearch();

		JSONObject resObj = new JSONObject();

		try {
			resObj.put("success", true);
			resObj.put("msg", "Historical failed search \"" + searchID + "\" removed");

		} catch (JSONException e) {
			logger.error("Method removeFailedSearch(): Error trying to remove failed search " + searchID + " - " + e.getMessage());
		}

		logger.debug("Method removeFailedSearch(): End");

		return resObj.toString();
	}

	private boolean monitorFrequencyValidation(String value, String type) {
		if (value != null && !value.equals("") && type != null && !value.equals(""))
			return true;
		else
			return false;
	}

	private boolean monitorUpToValidation(String value, String type) {
		if (value != null && !value.equals("") && type != null && !value.equals(""))
			return true;
		else
			return false;
	}
}
