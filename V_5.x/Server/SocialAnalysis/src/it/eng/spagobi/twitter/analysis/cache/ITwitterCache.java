/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.cache;

import it.eng.spagobi.twitter.analysis.entities.TwitterAccountToMonitor;
import it.eng.spagobi.twitter.analysis.entities.TwitterData;
import it.eng.spagobi.twitter.analysis.entities.TwitterLinkToMonitor;
import it.eng.spagobi.twitter.analysis.entities.TwitterLinkToMonitorCategory;
import it.eng.spagobi.twitter.analysis.entities.TwitterMonitorScheduler;
import it.eng.spagobi.twitter.analysis.entities.TwitterSearch;
import it.eng.spagobi.twitter.analysis.entities.TwitterSearchScheduler;
import it.eng.spagobi.twitter.analysis.entities.TwitterUser;
import it.eng.spagobi.twitter.analysis.enums.SearchTypeEnum;

import java.util.List;

import twitter4j.Status;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public interface ITwitterCache {

	public TwitterSearch createTwitterSearch(TwitterSearch twitterSearch);

	public void updateTwitterSearch(TwitterSearch twitterSearch);

	public List<TwitterSearch> getTwitterSearchList(SearchTypeEnum searchType);

	public void deleteSearch(TwitterSearch twitterSearch);

	public void saveTweet(Status tweet, long searchID) throws Exception;

	public void insertTwitterUser(TwitterUser twitterUser);

	public void insertTweet(TwitterData twitterData, long searchID);

	public boolean isTwitterUserPresent(long userID);

	public boolean isTwitterDataPresent(long searchID, long tweetID);

	public TwitterSearch findTwitterSearch(long searchID);

	public void insertBitlyAnalysis(TwitterLinkToMonitor twitterLinkToMonitor, List<TwitterLinkToMonitorCategory> twitterLinkToMonitorCategoryList,
			long searchID);

	public void insertAccountToMonitor(TwitterAccountToMonitor twitterAccountToMonitor, long searchID);

	public TwitterSearch isPresentEnabledStream();

	public void updateTwitterMonitorScheduler(TwitterMonitorScheduler twitterMonitorScheduler);

	public void updateTwitterSearchScheduler(TwitterSearchScheduler twitterSearchScheduler);

	public void stopAllStreams();

	public List<TwitterSearchScheduler> getAllActiveSearchSchedulers();

	public List<TwitterMonitorScheduler> getAllMonitorSchedulers();

	public TwitterMonitorScheduler getMonitorSchedulerFromSearch(long searchID);

	public TwitterSearchScheduler findTwitterSearchScheduler(long id);

	public TwitterSearch refreshTwitterSearch(TwitterSearch twitterSearch);

}
