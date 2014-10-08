/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.DataProcessorCacheImpl;
import it.eng.spagobi.twitter.analysis.cache.IDataProcessorCache;
import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheImpl;
import it.eng.spagobi.twitter.analysis.entities.TwitterData;
import it.eng.spagobi.twitter.analysis.entities.TwitterUser;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class UsersNetworkGraphDataProcessor {

	private static final Logger logger = Logger.getLogger(UsersNetworkGraphDataProcessor.class);

	private final IDataProcessorCache dpCache = new DataProcessorCacheImpl();
	private final ITwitterCache twitterCache = new TwitterCacheImpl();

	private JSONArray links = new JSONArray();

	private JSONObject profiles = new JSONObject();

	public void initializeUsersNetworkGraph(String searchID) {

		logger.debug("Method initializeUsersNetworkGraph(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute initializeUsersNetworkGraph() without a correct search ID");

		List<TwitterUser> users = dpCache.getUsersForSearchID(searchID);
		List<TwitterData> tweets = dpCache.getTweetsFromSearchId(searchID);

		try {
			this.profiles = createNodesProfiles(users);
			this.links = createLinksJsonArray(tweets);

			logger.debug("Method initializeUsersNetworkGraph(): End");

		} catch (JSONException e) {
			throw new SpagoBIRuntimeException("Method initializeUsersNetworkGraph(): Impossible to parse a mentions map int a JSON Array - " + e.getMessage());
		}

	}

	private JSONObject createNodesProfiles(List<TwitterUser> users) throws JSONException {

		JSONObject result = new JSONObject();

		if (users != null && users.size() > 0) {
			for (TwitterUser user : users) {

				// userObj.put("name", user.getUsername());
				// userObj.put("fixed", true);
				result.put(user.getUsername(), user.getProfileImgSrc());

			}
		}

		return result;
	}

	private JSONArray createLinksJsonArray(List<TwitterData> tweets) throws JSONException {

		JSONArray jsonArray = new JSONArray();

		if (tweets != null && tweets.size() > 0) {

			for (TwitterData tweet : tweets) {

				if (tweet.getReplyToUserId() != null) {

					TwitterData tweetToReply = twitterCache.isTwitterDataPresent(tweet.getTwitterSearch().getSearchID(),
							Long.parseLong(tweet.getReplyToUserId()));

					if (tweetToReply != null) {

						JSONObject linkObj = new JSONObject();

						linkObj.put("source", tweet.getTwitterUser().getUsername());
						linkObj.put("target", tweet.getReplyToScreenName());
						linkObj.put("type", "reply");

						jsonArray.put(linkObj);
					}

				} else if (tweet.getOriginalRTTweetId() != null) {

					TwitterData originalTweet = twitterCache.isTwitterDataPresent(tweet.getTwitterSearch().getSearchID(),
							Long.parseLong(tweet.getOriginalRTTweetId()));

					if (originalTweet != null) {
						JSONObject linkObj = new JSONObject();

						linkObj.put("source", originalTweet.getTwitterUser().getUsername());
						linkObj.put("target", tweet.getTwitterUser().getUsername());
						linkObj.put("value", "rt");

						jsonArray.put(linkObj);
					}

				}
			}
		}

		return jsonArray;
	}

	public JSONArray getLinks() {
		return links;
	}

	public void setLinks(JSONArray links) {
		this.links = links;
	}

	public JSONObject getProfiles() {
		return profiles;
	}

	public void setProfiles(JSONObject profiles) {
		this.profiles = profiles;
	}

}
