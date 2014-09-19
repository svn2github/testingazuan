/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.pojos.TwitterInfluencersPojo;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */

public class TwitterInfluencersDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterInfluencersDataProcessor.class);

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	public List<JSONObject> getMostInfluencersJSON(String searchID) {

		logger.debug("Method getMostInfluencersJSON(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getMostInfluencersJSON() without a correct search ID");

		List<TwitterInfluencersPojo> mostInfluencers = new ArrayList<TwitterInfluencersPojo>();
		int influencersCounter = 0;
		int influencersMax = 32;

		List<JSONObject> influencersJSON = new ArrayList<JSONObject>();

		String sqlQuery = "SELECT DISTINCT tu.username, tu.description, tu.profile_image_source, tu.followers_count from twitter_users tu, twitter_data td where tu.user_id = td.user_id and td.search_id = '"
				+ searchID + "' order by followers_count desc";

		try {
			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			while (rs.next()) {

				if (influencersCounter < influencersMax) {
					String username = rs.getString("username");
					String description = rs.getString("description");
					String profileImg = rs.getString("profile_image_source");
					int followers = rs.getInt("followers_count");

					Assert.assertNotNull(username, "SQL NULL for username column in results of [ " + sqlQuery + " ] ");
					Assert.assertNotNull(profileImg, "SQL NULL for profile_image_source column in results of [ " + sqlQuery + " ] ");
					Assert.assertNotNull(followers, "SQL NULL for followers_count column in results of [ " + sqlQuery + " ] ");

					if (description == null) {
						description = "";
					}

					TwitterInfluencersPojo tempObj = new TwitterInfluencersPojo(username, description, profileImg, followers);
					mostInfluencers.add(tempObj);

					influencersCounter++;
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(mostInfluencers);
			JSONArray influencersArr = new JSONArray(jsonString);

			if (influencersArr != null && influencersArr.length() > 0) {
				for (int i = 0; i < influencersArr.length(); i++) {
					influencersJSON.add(influencersArr.getJSONObject(i));
				}
			}

			logger.debug("Method getMostInfluencersJSON(): End");
			return influencersJSON;

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method getMostInfluencersJSON(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		} catch (IOException e) {
			throw new SpagoBIRuntimeException("Method getMostInfluencersJSON(): I/O error converting JSON", e);
		} catch (JSONException e) {
			throw new SpagoBIRuntimeException("Method getMostInfluencersJSON(): Impossible to do a correct JSON mapping", e);
		}

	}
}
