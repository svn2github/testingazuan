package it.eng.spagobi.twitter.analysis.api;

import it.eng.spagobi.twitter.analysis.dataprocessors.TwitterSearchDataProcessor;
import it.eng.spagobi.twitter.analysis.launcher.TwitterAnalysisLauncher;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMonitorSchedulerPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;

import java.util.ArrayList;
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

@Path("/streamingSearch")
public class TwitterStreamingSearchAPI {

	static final Logger logger = Logger.getLogger(TwitterStreamingSearchAPI.class);

	TwitterSearchPojo twitterSearch = new TwitterSearchPojo();
	TwitterMonitorSchedulerPojo twitterMonitorScheduler = null;

	// Save a new Twitter Search
	@Path("/saveSearch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String save(@Context HttpServletRequest req) throws Exception {

		logger.debug("Method save(): Start..");

		// String s = readBody(req);
		// ObjectMapper mapper = new ObjectMapper();
		// JsonNode df = mapper.readValue(s, JsonNode.class);

		// JSONObject jsonObj = new JSONObject();
		// Map<String, String[]> params = req.getParameterMap();
		// for (Map.Entry<String, String[]> entry : params.entrySet()) {
		// String v[] = entry.getValue();
		// Object o = (v.length == 1) ? v[0] : v;
		// jsonObj.put(entry.getKey(), o);
		// }

		String languageCode = null;
		String dbType = "MySQL";

		// reading the user input
		String searchType = req.getParameter("searchType");
		String label = req.getParameter("label");
		String keywords = req.getParameter("keywords");
		String links = req.getParameter("links");
		String accounts = req.getParameter("accounts");

		// set ready parameters
		twitterSearch.setLanguageCode(languageCode);
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchType(searchType);
		twitterSearch.setKeywords(keywords);
		twitterSearch.setLinks(links);
		twitterSearch.setAccounts(accounts);
		twitterSearch.setFrequency("");

		// if user is not specifying the label, create it with the keywords
		if (label == null || label.trim().equals("")) {

			logger.debug("Method save(): Blank label. Creation from keywords");

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

		// parameters for monitoring scheduler

		// now we take the decision abount the monitor scheduler. Check if there
		// resources to monitor..
		if ((links != null && !links.equals("")) || (accounts != null && !accounts.equals(""))) {

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

			}

		}

		// set monitor scheduler
		twitterSearch.setTwitterMonitorScheduler(twitterMonitorScheduler);

		logger.debug("Method save(): Search streaming");

		TwitterAnalysisLauncher twitterLauncher = new TwitterAnalysisLauncher(twitterSearch);

		long searchID = twitterLauncher.createStreamingSearch();

		JSONObject resObj = new JSONObject();

		try {

			if (searchID > 0) {

				resObj.put("success", true);
				resObj.put("msg", "Streaming search \"" + label + "\" inserted (DISABLED");

			} else {

				resObj.put("failure", true);
				resObj.put("msg", "Failure inserting new search ");

			}
		} catch (JSONException e) {
			logger.error("Method save(): ERROR - " + e);
		}

		logger.debug("Method save(): End");

		return resObj.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String start(@Context HttpServletRequest req) throws Exception {

		logger.debug("Method start(): Start..");

		// ObjectMapper mapper = new ObjectMapper();
		// JsonNode df = mapper.readValue(s, JsonNode.class);

		// JSONObject jsonObj = new JSONObject();
		// Map<String, String[]> params = req.getParameterMap();
		// for (Map.Entry<String, String[]> entry : params.entrySet()) {
		// String v[] = entry.getValue();
		// Object o = (v.length == 1) ? v[0] : v;
		// jsonObj.put(entry.getKey(), o);
		// }

		String languageCode = null;
		String dbType = "MySQL";
		//
		// // reading the user input
		String sID = req.getParameter("searchID");

		if (sID != null && !sID.equals("")) {
			long searchID = Long.parseLong(sID);

			String keywords = req.getParameter("keywords");

			twitterSearch.setLanguageCode(languageCode);
			twitterSearch.setDbType(dbType);
			twitterSearch.setKeywords(keywords);
			twitterSearch.setSearchID(searchID);

			TwitterAnalysisLauncher twitterLauncher = new TwitterAnalysisLauncher(twitterSearch);

			twitterLauncher.startStreamingSearch();

		}

		JSONObject resObj = new JSONObject();

		// try {
		//
		// if (searchID > 0) {
		//
		// resObj.put("success", true);
		// resObj.put("msg", "Streaming search \"" + label +
		// "\" inserted (DISABLED");
		//
		// } else {
		//
		// resObj.put("failure", true);
		// resObj.put("msg", "Failure inserting new search ");
		//
		// }
		// } catch (JSONException e) {
		// logger.error("Method save(): ERROR - " + e);
		// }
		//
		// logger.debug("Method save(): End");

		return resObj.toString();
	}

	// Get the list of all Twitter Search
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getListSearch(@Context HttpServletRequest req) {

		List<TwitterSearchPojo> search = new ArrayList<TwitterSearchPojo>();

		search = new TwitterSearchDataProcessor().getTwitterSearchList("streamingAPI");

		Gson gson = new Gson();

		JsonElement element = gson.toJsonTree(search, new TypeToken<List<TwitterSearchPojo>>() {
		}.getType());

		JsonArray jsonArray = element.getAsJsonArray();

		// String s =
		// "{ id: '1000',    label: 'Test1', keywords: 'spagobi, opensource', lastActivation: '01/01/2014', accounts: '@themonkey86', links: 'bitly.link' }";

		return jsonArray.toString();

	}

	// Delete a Twitter Search
	@Path("/deleteSearch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@Context HttpServletRequest req) throws Exception {

		logger.debug("Method delete(): Start..");

		String languageCode = null;
		String dbType = "MySQL";

		// reading the user input
		String searchID = req.getParameter("searchID");
		boolean loading = Boolean.parseBoolean(req.getParameter("loading"));

		// set ready parameters
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchID(Long.parseLong(searchID));
		twitterSearch.setSearchType("streamingAPI");
		twitterSearch.setLoading(loading);

		TwitterAnalysisLauncher twitterAnalysisLauncher = new TwitterAnalysisLauncher(twitterSearch);
		twitterAnalysisLauncher.deleteSearch();

		JSONObject resObj = new JSONObject();

		try {
			resObj.put("success", true);
			resObj.put("msg", "Streaming search \"" + searchID + "\" deleted");

		} catch (JSONException e) {
			logger.error("Method delete(): ERROR - " + e);
		}

		logger.debug("Method delete(): End");

		return resObj.toString();
	}

	// Stop a Twitter Search
	@Path("/stopStreamingSearch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String stopStream(@Context HttpServletRequest req) throws Exception {

		logger.debug("Method stopStream(): Start..");

		String languageCode = null;
		String dbType = "MySQL";

		// reading the user input
		String searchID = req.getParameter("searchID");

		// set ready parameters
		twitterSearch.setDbType(dbType);
		twitterSearch.setSearchID(Long.parseLong(searchID));
		twitterSearch.setSearchType("streamingAPI");

		TwitterAnalysisLauncher twitterAnalysisLauncher = new TwitterAnalysisLauncher(twitterSearch);
		twitterAnalysisLauncher.stopStreamingSearch();

		JSONObject resObj = new JSONObject();

		try {
			resObj.put("success", true);
			resObj.put("msg", "Streaming search \"" + searchID + "\" stopped");

		} catch (JSONException e) {
			logger.error("Method delete(): ERROR - " + e);
		}

		logger.debug("Method delete(): End");

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
