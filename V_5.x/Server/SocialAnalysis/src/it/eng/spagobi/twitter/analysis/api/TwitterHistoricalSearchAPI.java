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

		logger.debug("Method search(): Start..");

		TwitterAnalysisLauncher twitterLauncher = null;
		TwitterSearchSchedulerPojo twitterScheduler = null;
		TwitterMonitorSchedulerPojo twitterMonitorScheduler = null;
		TwitterSearchPojo twitterSearch = new TwitterSearchPojo();

		String languageCode = null;
		String dbType = "MySQL";

		String typeFrequency = "None";

		// reading the user input
		String searchType = req.getParameter("searchType");
		String keywords = req.getParameter("keywords");
		String links = req.getParameter("links");
		String accounts = req.getParameter("accounts");

		// String documents = req.getParameter("documents");

		// set ready parameters
		twitterSearch.setLanguageCode(languageCode);
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchType(searchType);
		twitterSearch.setKeywords(keywords);
		twitterSearch.setLinks(links);
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

		// String typeRepeatEvery = req.getParameter("typeRepeatEvery");
		String numberRepeat = req.getParameter("numberRepeat");

		if (req.getParameter("isRepeating") != null && numberRepeat != null && numberRepeat.equals("")) {

			logger.debug("Method search(): Search with scheduler");

			Calendar startingDate = GregorianCalendar.getInstance();

			int repeatFrequency = Integer.parseInt(numberRepeat);

			if (repeatFrequency > 0) {

				// TODO: Aggiungere altri tipi di frequency
				typeFrequency = "Day";

				startingDate.add(Calendar.DAY_OF_MONTH, repeatFrequency);

				twitterScheduler = new TwitterSearchSchedulerPojo(startingDate, repeatFrequency, typeFrequency);
			}

		}

		// set type frequency
		twitterSearch.setFrequency(typeFrequency);

		// set search scheduler
		twitterSearch.setTwitterScheduler(twitterScheduler);

		// parameters for monitoring scheduler

		String numberUpTo = req.getParameter("numberUpTo");
		String typeUpTo = req.getParameter("typeUpTo");

		if ((links != null && !links.equals("")) || (accounts != null && !accounts.equals(""))) {
			if (numberUpTo != null && !numberUpTo.equals("") && typeUpTo != null && !typeUpTo.equals("")) {

				Calendar endingDate = GregorianCalendar.getInstance();
				int repeatFrequency = Integer.parseInt(numberUpTo);

				if (typeUpTo.equalsIgnoreCase("Day")) {
					endingDate.add(Calendar.DAY_OF_MONTH, repeatFrequency);
				} else if (typeUpTo.equalsIgnoreCase("Week")) {
					endingDate.add(Calendar.DAY_OF_MONTH, (repeatFrequency) * 7);
				} else if (typeUpTo.equalsIgnoreCase("Month")) {
					endingDate.add(Calendar.DAY_OF_MONTH, (repeatFrequency) * 30);
				}

				twitterMonitorScheduler = new TwitterMonitorSchedulerPojo(endingDate, repeatFrequency, typeUpTo);
			} else {
				// TODO: default upto 1 month
				Calendar endingDate = GregorianCalendar.getInstance();
				int repeatFrequency = Integer.parseInt(numberUpTo);

				endingDate.add(Calendar.DAY_OF_MONTH, (repeatFrequency) * 30);

				twitterMonitorScheduler = new TwitterMonitorSchedulerPojo(endingDate, repeatFrequency, typeUpTo);

			}

		}

		// set monitor scheduler
		twitterSearch.setTwitterMonitorScheduler(twitterMonitorScheduler);

		String numberStartingFrom = req.getParameter("numberStartingFrom");
		String typeStartingFrom = req.getParameter("typeStartingFrom");

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
		twitterSearch.setSearchType("streamingAPI");

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
			resObj.put("msg", "Historical search \"" + searchID + "\" deleted");

		} catch (JSONException e) {
			logger.error("Method delete(): ERROR - " + e);
		}

		logger.debug("Method delete(): End");

		return resObj.toString();
	}

}
