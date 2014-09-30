/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.DataProcessorCacheImpl;
import it.eng.spagobi.twitter.analysis.cache.IDataProcessorCache;
import it.eng.spagobi.twitter.analysis.pojos.TwitterPiePojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterPieSourcePojo;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class TwitterPieDataProcessor {

	private static final Logger logger = Logger.getLogger(TwitterPieDataProcessor.class);

	private final IDataProcessorCache dpCache = new DataProcessorCacheImpl();

	/**
	 * This method creates the tweets type pie chart object for summary.jsp
	 *
	 * @param searchID
	 * @return
	 */
	public TwitterPiePojo getTweetsPieChart(String searchID) {

		logger.debug("Method getTweetsPieChart(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getTweetsPieChart() without a correct search ID");

		int totalTweets = dpCache.getTotalTweets(searchID);
		int totalReplies = dpCache.getTotalReplies(searchID);
		int totalRTs = dpCache.getTotalRTs(searchID);

		int originalTweets = totalTweets - totalRTs - totalReplies;

		TwitterPiePojo statsObj = new TwitterPiePojo(originalTweets, totalReplies, totalRTs);

		logger.debug("Method getTweetsPieChart(): End");
		return statsObj;

	}

	/**
	 * This method creates the sources pie chart for summary.jsp
	 *
	 * @param searchID
	 * @return
	 */
	public List<TwitterPieSourcePojo> getTweetsPieSourceChart(String searchID) {

		logger.debug("Method getTweetsPieSourceChart(): Start");

		Assert.assertNotNull(searchID, "Impossibile execute getTweetsPieSourceChart() without a correct search ID");

		Map<String, Integer> sourceMap = new HashMap<String, Integer>();
		List<TwitterPieSourcePojo> sources = new ArrayList<TwitterPieSourcePojo>();

		List<String> tweetsSources = dpCache.getSources(searchID);

		for (String sourceClient : tweetsSources) {
			String formattedSource = tweetSourceFormatter(sourceClient);

			if (sourceMap.containsKey(formattedSource)) {

				int value = sourceMap.get(formattedSource);
				value++;
				sourceMap.put(formattedSource, value);

			} else {

				sourceMap.put(formattedSource, 1);
			}
		}

		for (Map.Entry<String, Integer> entry : sourceMap.entrySet()) {

			String source = entry.getKey();
			int value = entry.getValue();

			TwitterPieSourcePojo obj = new TwitterPieSourcePojo(source, value);
			sources.add(obj);
		}

		logger.debug("Method getTweetsPieSourceChart(): End");
		return sources;

	}

	/**
	 * This method extracts the source from general html source String
	 *
	 * @param source
	 * @return
	 */
	private String tweetSourceFormatter(String source) {

		String formattedHTMLSource = source.replaceAll("<.*?>", "");
		String formattedTextSourceFirst = formattedHTMLSource.replaceAll("Twitter ", "");
		String formattedTextSourceSecond = formattedTextSourceFirst.replaceAll("for ", "");

		return formattedTextSourceSecond;

	}
}
