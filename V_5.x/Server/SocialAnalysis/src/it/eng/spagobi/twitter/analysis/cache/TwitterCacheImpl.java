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
import it.eng.spagobi.twitter.analysis.pojos.TwitterMessageObject;
import it.eng.spagobi.twitter.analysis.utilities.AnalysisUtility;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.Status;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class TwitterCacheImpl implements ITwitterCache {

	private static final Logger logger = Logger.getLogger(TwitterCacheImpl.class);

	private DaoService daoService;

	public TwitterCacheImpl() {

		this.daoService = new DaoService();
	}

	@Override
	public TwitterSearch createTwitterSearch(TwitterSearch twitterSearch) {

		logger.debug("Method createTwitterSearch(): Start");

		Assert.assertNotNull(this.daoService, "Method createTwitterSearch(): Impossible to create a new [ " + TwitterSearch.class
				+ " ] without a valid DaoService");

		this.daoService.create(twitterSearch);

		logger.debug("Method createTwitterSearch(): End");

		return twitterSearch;

	}

	@Override
	public void updateTwitterSearch(TwitterSearch twitterSearch) {

		logger.debug("Method updateTwitterSearch(): Start");

		Assert.assertNotNull(this.daoService, "Method updateTwitterSearch(): Impossible to update [ " + twitterSearch.getSearchID()
				+ " ] without a valid DaoService");

		this.daoService.update(twitterSearch);

		logger.debug("Method updateTwitterSearch(): End");

	}

	@Override
	public List<TwitterSearch> getTwitterSearchList(SearchTypeEnum searchType) {

		logger.debug("Method getTwitterSearchList(): Start");

		Assert.assertNotNull(this.daoService, "Method getTwitterSearchList(): Impossible to load all searches without a valid DaoService");

		String query = "from TwitterSearch as search where search.type = ? and search.deleted = false";

		List<TwitterSearch> result = daoService.listFromQuery(query, searchType);

		logger.debug("Method getTwitterSearchList(): End");

		return result;

	}

	@Override
	public void deleteSearch(TwitterSearch twitterSearch) {

		logger.debug("Method deleteSearch(): Start");

		Assert.assertNotNull(this.daoService, "Method deleteSearch(): Impossible to delete [ " + twitterSearch.getSearchID() + " ] without a valid DaoService");

		twitterSearch.setTwitterSearchScheduler(null);
		twitterSearch.setTwitterMonitorScheduler(null);
		twitterSearch.setDeleted(true);

		daoService.update(twitterSearch);

		logger.debug("Method deleteSearch(): End");

	}

	@Override
	public void saveTweet(Status tweet, long searchID) throws Exception {

		TwitterMessageObject twitterMessage = new TwitterMessageObject(tweet);

		TwitterUser twitterUser = twitterMessage.getTwitterUser();
		TwitterData twitterData = twitterMessage.getTwitterData();

		TwitterUser twitterUserFromDB = isTwitterUserPresent(twitterMessage.getUserID());

		if (twitterUserFromDB == null) {

			// user not present in DB, create a new twitter user

			String locationCode = AnalysisUtility.findCountryCodeFromUserLocation(twitterUser.getLocation(), twitterUser.getTimeZone());
			twitterUser.setLocationCode(locationCode);

			insertTwitterUser(twitterUser);

		} else {

			// user in DB, update twitter user

			// twitterUserFromDB.setUsername(this.username);
			// twitterUserFromDB.setDescription(this.description);
			// twitterUserFromDB.setFollowersCount(this.followersCount);
			// twitterUserFromDB.setProfileImgSrc(this.profileImgSrc);
			// this.twitterUser.setLocation(this.location);
			// twitterUserFromDB.setLanguageCode(this.userLanguageCode);
			// twitterUserFromDB.setName(this.name);
			// twitterUserFromDB.setTimeZone(this.timeZone);
			// twitterUserFromDB.setTweetsCount(this.tweetsCount);
			// twitterUserFromDB.setVerified(this.isVerified);
			// this.twitterUser.setFollowingCount(this.followingCount);
			// this.twitterUser.setUtcOffset(this.utcOffset);
			// this.twitterUser.setGeoEnabled(this.isGeoEnabled);
			// this.twitterUser.setListedCount(this.listedCount);
			// this.twitterUser.setStartDate(GregorianCalendar.getInstance());
			// this.twitterUser.setEndDate(GregorianCalendar.getInstance());
			//
			//
			// twitterUserFromDB = twitterUser;
			// updateTwitterUser(twitterUserFromDB);

		}

		TwitterData tweetFromDB = isTwitterDataPresent(searchID, twitterMessage.getTweetID());

		if (tweetFromDB == null) {

			insertTweet(twitterData, searchID);
		} else {

		}

	}

	@Override
	public void insertTwitterUser(TwitterUser twitterUser) {

		logger.debug("Method insertTwitterUser(): Start");

		Assert.assertNotNull(this.daoService, "Method insertTwitterUser(): Impossible to insert a new TwitterUser without a valid DaoService");

		this.daoService.create(twitterUser);

	}

	@Override
	public void insertTweet(TwitterData twitterData, long searchID) {

		logger.debug("Method insertTweet(): Start");

		Assert.assertNotNull(this.daoService, "Method insertTweet(): Impossible to insert a new Tweet without a valid DaoService");

		TwitterSearch twitterSearch = new TwitterSearch();
		twitterSearch.setSearchID(searchID);

		twitterData.setTwitterSearch(twitterSearch);

		daoService.create(twitterData);

		logger.debug("Method insertTweet(): End");

	}

	@Override
	public TwitterUser isTwitterUserPresent(long userID) {

		logger.debug("Method isTwitterUserPresent(): Start");

		Assert.assertNotNull(this.daoService, "Method isTwitterUserPresent(): Impossible to get a Twitter User without a valid DaoService");

		TwitterUser twitterUser = (TwitterUser) daoService.find(TwitterUser.class, userID);

		return twitterUser;

	}

	@Override
	public TwitterData isTwitterDataPresent(long searchID, long tweetID) {

		logger.debug("Method isTwitterDataPresent(): Start");

		Assert.assertNotNull(this.daoService, "Method isTwitterUserPresent(): Impossible to get a Twitter Data without a valid DaoService");

		String query = " select tweet.tweetID from TwitterData tweet where tweet.twitterSearch.searchID = ? and tweet.tweetID = ?";

		TwitterData twitterData = daoService.singleResultQuery(query, searchID, tweetID);

		return twitterData;
	}

	@Override
	public TwitterSearch findTwitterSearch(long searchID) {

		logger.debug("Method findTwitterSearch(): Start");

		Assert.assertNotNull(this.daoService, "Method findTwitterSearch(): Impossible to get a Twitter Search without a valid DaoService");

		TwitterSearch twitterSearch = (TwitterSearch) daoService.find(TwitterSearch.class, searchID);

		Assert.assertNotNull(twitterSearch, "Method findTwitterSearch(): Impossible to get a Twitter Search with search_id: " + searchID);

		return twitterSearch;

	}

	@Override
	public void insertBitlyAnalysis(TwitterLinkToMonitor twitterLinkToMonitor, List<TwitterLinkToMonitorCategory> twitterLinkToMonitorCategoryList,
			long searchID) {

		logger.debug("Method insertBitlyAnalysis(): Start");

		Assert.assertNotNull(this.daoService, "Method insertBitlyAnalysis(): Impossible to insert Twitter Links without a valid DaoService");

		TwitterSearch twitterSearch = new TwitterSearch();
		twitterSearch.setSearchID(searchID);

		twitterLinkToMonitor.setTwitterSearch(twitterSearch);

		twitterLinkToMonitor = (TwitterLinkToMonitor) daoService.create(twitterLinkToMonitor);

		for (TwitterLinkToMonitorCategory linkCategory : twitterLinkToMonitorCategoryList) {
			linkCategory.setTwitterLinkToMonitor(twitterLinkToMonitor);

			daoService.create(linkCategory);
		}

		logger.debug("Method insertBitlyAnalysis(): End");
	}

	@Override
	public void insertAccountToMonitor(TwitterAccountToMonitor twitterAccountToMonitor, long searchID) {

		logger.debug("Method insertAccountToMonitor(): Start");

		Assert.assertNotNull(this.daoService, "Method insertAccountToMonitor(): Impossible to insert a Twitter Account without a valid DaoService");

		TwitterSearch twitterSearch = new TwitterSearch();
		twitterSearch.setSearchID(searchID);

		twitterAccountToMonitor.setTwitterSearch(twitterSearch);

		daoService.create(twitterAccountToMonitor);

		logger.debug("Method insertAccountToMonitor(): End");

	}

	@Override
	public TwitterSearch isPresentEnabledStream() {

		logger.debug("Method isPresentEnabledStream(): Start");

		Assert.assertNotNull(this.daoService, "Method isPresentEnabledStream(): Impossible to analyze twitter searches without a valid DaoService");

		String query = "from TwitterSearch search where search.type = ? and search.loading = 1";

		TwitterSearch twitterSearch = daoService.singleResultQuery(query, SearchTypeEnum.STREAMINGAPI);

		return twitterSearch;

	}

	@Override
	public void updateTwitterMonitorScheduler(TwitterMonitorScheduler twitterMonitorScheduler) {

		logger.debug("Method updateTwitterMonitorScheduler(): Start");

		Assert.assertNotNull(this.daoService, "Method updateTwitterMonitorScheduler(): Impossible to update [ " + twitterMonitorScheduler.getId()
				+ " ] without a valid DaoService");

		this.daoService.update(twitterMonitorScheduler);

		logger.debug("Method updateTwitterMonitorScheduler(): End");

	}

	@Override
	public void updateTwitterSearchScheduler(TwitterSearchScheduler twitterSearchScheduler) {

		logger.debug("Method updateTwitterSearchScheduler(): Start");

		Assert.assertNotNull(this.daoService, "Method updateTwitterSearchScheduler(): Impossible to update [ " + twitterSearchScheduler.getId()
				+ " ] without a valid DaoService");

		this.daoService.update(twitterSearchScheduler);

		logger.debug("Method updateTwitterSearchScheduler(): End");
	}

	@Override
	public void stopAllStreams() {

		logger.debug("Method stopAllStreams(): Start");

		Assert.assertNotNull(this.daoService, "Method stopAllStreams(): Impossible to stop enabled Stream without a valid DaoService");

		String query = "update TwitterSearch search set search.loading = 0 where search.type = ?";

		daoService.updateFromQuery(query, SearchTypeEnum.STREAMINGAPI);

		logger.debug("Method stopAllStreams(): End");

	}

	@Override
	public List<TwitterSearchScheduler> getAllActiveSearchSchedulers() {

		logger.debug("Method getAllActiveSearchSchedulers(): Start");

		Assert.assertNotNull(this.daoService, "Method getAllActiveSearchSchedulers(): Impossible to get active search schedulers without a valid DaoService");

		String query = "from TwitterSearchScheduler ts where ts.active = 1 and ts.twitterSearch.deleted = 0";

		List<TwitterSearchScheduler> searchSchedulers = daoService.listFromQuery(query);

		logger.debug("Method getAllActiveSearchSchedulers(): End");
		return searchSchedulers;

	}

	@Override
	public List<TwitterMonitorScheduler> getAllMonitorSchedulers() {

		logger.debug("Method getAllMonitorSchedulers(): Start");

		Assert.assertNotNull(this.daoService, "Method getAllMonitorSchedulers(): Impossible to get monitor schedulers without a valid DaoService");

		String query = "from TwitterMonitorScheduler tms where tms.twitterSearch.deleted = 0";

		List<TwitterMonitorScheduler> monitorSchedulers = daoService.listFromQuery(query);

		logger.debug("Method getAllMonitorSchedulers(): End");
		return monitorSchedulers;

	}

	@Override
	public TwitterMonitorScheduler getMonitorSchedulerFromSearch(long searchID) {

		logger.debug("Method getMonitorSchedulerFromSearch(): Start");

		Assert.assertNotNull(this.daoService, "Method getMonitorSchedulerFromSearch(): Impossible to get monitor scheduler without a valid DaoService");

		String query = "from TwitterMonitorScheduler tms where tms.twitterSearch.searchID = ? and tms.twitterSearch.deleted = 0";

		TwitterMonitorScheduler monitorScheduler = daoService.singleResultQuery(query, searchID);

		logger.debug("Method getMonitorSchedulerFromSearch(): End");
		return monitorScheduler;

	}

	@Override
	public TwitterSearchScheduler findTwitterSearchScheduler(long id) {

		logger.debug("Method findTwitterSearchScheduler(): Start");

		Assert.assertNotNull(this.daoService, "Method findTwitterSearchScheduler(): Impossible to get a Twitter Search without a valid DaoService");

		TwitterSearchScheduler twitterSearchScheduler = (TwitterSearchScheduler) daoService.find(TwitterSearchScheduler.class, id);

		// Assert.assertNotNull(twitterSearch, "Method findTwitterSearchScheduler(): Impossible to get a Twitter Search Scheduler with id: " + searchID);

		return twitterSearchScheduler;

	}

	@Override
	public TwitterSearch refreshTwitterSearch(TwitterSearch twitterSearch) {

		logger.debug("Method refreshTwitterSearc(): Start");

		Assert.assertNotNull(this.daoService, "Method findTwitterSearch(): Impossible to get a Twitter Search without a valid DaoService");

		logger.debug("Method refreshTwitterSearc(): End");
		return daoService.refresh(twitterSearch);

	}

	@Override
	public void updateTwitterUser(TwitterUser twitterUser) {

		logger.debug("Method updateTwitterUser(): Start");

		Assert.assertNotNull(this.daoService, "Method updateTwitterUser(): Impossible to update TwitterUser without a valid DaoService");

		this.daoService.update(twitterUser);

		logger.debug("Method updateTwitterUser(): End");

	}

}
