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
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.JSONArray;
import twitter4j.JSONException;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class UsersNetworkLinkMapDataProcessor {

	private static final Logger logger = Logger.getLogger(UsersNetworkLinkMapDataProcessor.class);

	private final IDataProcessorCache dpCache = new DataProcessorCacheImpl();
	private final ITwitterCache twitterCache = new TwitterCacheImpl();

	private JSONArray links = new JSONArray();
	private JSONArray contriesCodes = new JSONArray();

	public void initializeUsersNetworkLinkMap(String searchID) {

		logger.debug("Method initializeUsersNetworkLinkMap(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute initializeUsersNetworkLinkMap() without a correct search ID");

		List<TwitterData> tweets = dpCache.getTweetsFromSearchId(searchID);
		List<String> codes = dpCache.getDistinctUsersLocationCodes(searchID);

		try {

			this.links = createLinksJsonArray(tweets);
			this.contriesCodes = createCountriesList(codes);

			logger.debug("Method initializeUsersNetworkLinkMap(): End");

		} catch (JSONException e) {
			throw new SpagoBIRuntimeException("Method initializeUsersNetworkLinkMap(): Impossible to parse data into a JSON Array - " + e.getMessage());
		}

	}

	private JSONArray createLinksJsonArray(List<TwitterData> tweets) throws JSONException {

		JSONArray jsonArray = new JSONArray();

		if (tweets != null && tweets.size() > 0) {

			for (TwitterData tweet : tweets) {

				if ((tweet.getTwitterUser().getLocationCode() != null && !tweet.getTwitterUser().getLocationCode().trim().equals(""))) {

					if (tweet.getReplyToUserId() != null) {

						TwitterData tweetToReply = twitterCache.isTwitterDataPresent(tweet.getTwitterSearch().getSearchID(),
								Long.parseLong(tweet.getReplyToUserId()));

						if (tweetToReply != null
								&& (tweetToReply.getTwitterUser().getLocationCode() != null && !tweetToReply.getTwitterUser().getLocationCode().trim()
										.equals(""))) {

							String sourceCountry = tweet.getTwitterUser().getLocationCode();
							String tagetCountry = tweetToReply.getTwitterUser().getLocationCode();

							if (!sourceCountry.equals(tagetCountry)) {

								JSONArray linkArr = new JSONArray();

								linkArr.put(sourceCountry);
								linkArr.put(tagetCountry);

								jsonArray.put(linkArr);
							}

						}

					} else if (tweet.getOriginalRTTweetId() != null) {

						TwitterData originalTweet = twitterCache.isTwitterDataPresent(tweet.getTwitterSearch().getSearchID(),
								Long.parseLong(tweet.getOriginalRTTweetId()));

						if (originalTweet != null
								&& (originalTweet.getTwitterUser().getLocationCode() != null && !originalTweet.getTwitterUser().getLocationCode().trim()
										.equals(""))) {

							String sourceCountry = originalTweet.getTwitterUser().getLocationCode();
							String tagetCountry = tweet.getTwitterUser().getLocationCode();

							if (!sourceCountry.equals(tagetCountry)) {

								JSONArray linkArr = new JSONArray();

								linkArr.put(sourceCountry);
								linkArr.put(tagetCountry);

								jsonArray.put(linkArr);
							}

						}

					}
				}
			}
		}

		return jsonArray;
	}

	private JSONArray createCountriesList(List<String> countries) {
		JSONArray result = new JSONArray();

		if (countries != null && countries.size() > 0) {
			for (String code : countries) {
				if (code != null && !code.trim().equals("")) {
					result.put(code);
				}
			}
		}

		return result;
	}

	private boolean validCountries(String source, String target) {
		if (source != null && !source.trim().equals("") && target != null && !target.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public JSONArray getLinks() {
		return links;
	}

	public void setLinks(JSONArray links) {
		this.links = links;
	}

	public JSONArray getContriesCodes() {
		return contriesCodes;
	}

	public void setContriesCodes(JSONArray contriesCodes) {
		this.contriesCodes = contriesCodes;
	}

}
