package it.eng.spagobi.twitter.analysis.api;

import it.eng.spagobi.twitter.analysis.dataprocessors.TwitterSearchDataProcessor;
import it.eng.spagobi.twitter.analysis.launcher.TwitterAnalysisLauncher;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		// String label = req.getParameter("label");
		//
		// String links = req.getParameter("links");
		// String accounts = req.getParameter("accounts");
		//
		// // if user is not specifying the label, create it with the keywords
		// if (label == null || label.trim().equals("")) {
		//
		// logger.debug("Method save(): Blank label. Creation from keywords");
		//
		// String[] keywordsArr = keywords.split(",");
		// for (int i = 0; i < keywordsArr.length; i++) {
		//
		// String tempKeyword = keywordsArr[i].trim();
		//
		// if (i == keywordsArr.length - 1) {
		// label = label + tempKeyword;
		// } else {
		// label = label + tempKeyword + "_";
		// }
		// }
		// }
		//
		// logger.debug("Method save(): Search streaming");

		// long searchID = twitterLauncher.saveStreamingSearch();

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

	private String readBody(HttpServletRequest request) {

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];

				int byteRead = -1;

				while ((byteRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, byteRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		return stringBuilder.toString();
	}

	// @GET
	// @Path("/sayHello/{username}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response sayHello(@javax.ws.rs.core.Context HttpServletRequest
	// req,
	// @PathParam("username") String username) {
	//
	// System.out.println("Ciao " + username);
	// return "Hello" + username;
	// }

}
