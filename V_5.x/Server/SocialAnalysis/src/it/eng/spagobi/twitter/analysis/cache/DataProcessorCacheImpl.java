/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.twitter.analysis.cache;

import it.eng.spagobi.twitter.analysis.entities.TwitterAccountToMonitor;
import it.eng.spagobi.twitter.analysis.entities.TwitterData;
import it.eng.spagobi.twitter.analysis.entities.TwitterLinkToMonitor;
import it.eng.spagobi.twitter.analysis.entities.TwitterMonitorScheduler;
import it.eng.spagobi.twitter.analysis.entities.TwitterUser;
import it.eng.spagobi.twitter.analysis.enums.MonitorRepeatTypeEnum;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class DataProcessorCacheImpl implements IDataProcessorCache {

	private static final Logger logger = Logger.getLogger(DataProcessorCacheImpl.class);

	private DaoService daoService;

	public DataProcessorCacheImpl() {

		this.daoService = new DaoService();
	}

	@Override
	public int getTotalTweets(String searchID) {

		logger.debug("Method getTotalTweets(): Start");

		Assert.assertNotNull(this.daoService, "Method getTotalTweets(): Impossible to get total tweets without a valid DaoService");

		String queryHQL = "select tweet.tweetID from TwitterData tweet where tweet.twitterSearch.searchID = ?";

		int result = daoService.countQuery(queryHQL, Long.parseLong(searchID));

		logger.debug("Method getTotalTweets(): End");

		return result;

	}

	@Override
	public int getTotalUsers(String searchID) {

		logger.debug("Method getTotalUsers(): Start");

		Assert.assertNotNull(this.daoService, "Method getTotalUsers(): Impossible to get total users without a valid DaoService");

		String queryHQL = "select count(user.userID) from TwitterUser user, TwitterData tweet where tweet.twitterSearch.searchID = ? and tweet.twitterUser.userID = user.userID group by (user.username)";

		int result = daoService.countQuery(queryHQL, Long.parseLong(searchID));

		logger.debug("Method getTotalUsers(): End");

		return result;

	}

	@Override
	public Calendar getMinTweetDate(String searchID) {

		logger.debug("Method getMinTweetDate(): Start");

		Assert.assertNotNull(this.daoService, "Method getMinTweetDate(): Impossible to get min tweet date without a valid DaoService");

		String query = "select tweet.dateCreatedAt from TwitterData tweet where tweet.twitterSearch.searchID = ? order by tweet.dateCreatedAt ASC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMinTweetDate(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMinTweetDate(): End");
			return null;
		}
	}

	@Override
	public Calendar getMaxTweetDate(String searchID) {

		logger.debug("Method getMaxTweetDate(): Start");

		Assert.assertNotNull(this.daoService, "Method getMinTweetDate(): Impossible to get max tweet date without a valid DaoService");

		String query = "select tweet.dateCreatedAt from TwitterData tweet where tweet.twitterSearch.searchID = ? order by tweet.dateCreatedAt DESC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMaxTweetDate(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMaxTweetDate(): End");
			return null;
		}

	}

	@Override
	public Calendar getMinTweetTime(String searchID) {

		logger.debug("Method getMinTweetTime(): Start");

		Assert.assertNotNull(this.daoService, "Method getMinTweetTime(): Impossible to get min tweet time without a valid DaoService");

		String query = "select tweet.timeCreatedAt from TwitterData tweet where tweet.twitterSearch.searchID = ? order by tweet.timeCreatedAt ASC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMinTweetTime(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMinTweetTime(): End");
			return null;
		}

	}

	@Override
	public Calendar getMaxTweetTime(String searchID) {

		logger.debug("Method getMaxTweetTime(): Start");

		Assert.assertNotNull(this.daoService, "Method getMaxTweetTime(): Impossible to get max tweet time without a valid DaoService");

		String query = "select tweet.timeCreatedAt from TwitterData tweet where tweet.twitterSearch.searchID = ? order by tweet.timeCreatedAt DESC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMaxTweetTime(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMaxTweetTime(): End");
			return null;
		}

	}

	@Override
	public TwitterMonitorScheduler getDocuments(String searchID) {

		logger.debug("Method getDocuments(): Start");

		Assert.assertNotNull(this.daoService, "Method getDocuments(): Impossible to get documents without a valid DaoService");

		String query = "from TwitterMonitorScheduler tms where tms.twitterSearch.searchID = ?";

		TwitterMonitorScheduler result = daoService.singleResultQuery(query, Long.parseLong(searchID));

		logger.debug("Method getDocuments(): End");

		return result;
	}

	@Override
	public List<TwitterUser> getTopInfluencers(String searchID, int maxResults) {

		logger.debug("Method getTopInfluencers(): Start");

		Assert.assertNotNull(this.daoService, "Method getTopInfluencers(): Impossible to get top influencers without a valid DaoService");

		String query = "SELECT DISTINCT new TwitterUser(tu.username, tu.description, tu.profileImgSrc, tu.followersCount) from TwitterUser tu, TwitterData td where tu.userID = td.twitterUser.userID and td.twitterSearch.searchID = ? order by tu.followersCount desc";

		List<TwitterUser> topInfluencers = daoService.listFromLimitedQuery(query, maxResults, Long.parseLong(searchID));

		logger.debug("Method getTopInfluencers(): End");

		return topInfluencers;

	}

	@Override
	public List<String> getUsersLocationCodes(String searchID) {
		logger.debug("Method getUsersLocationCodes(): Start");

		Assert.assertNotNull(this.daoService, "Method getUsersLocationCodes(): Impossible to get location codes without a valid DaoService");

		String query = "SELECT tu.locationCode from TwitterUser tu, TwitterData td where tu.userID = td.twitterUser.userID and td.twitterSearch.searchID = ?";

		List<String> locationCodes = daoService.listFromQuery(query, Long.parseLong(searchID));

		logger.debug("Method getUsersLocationCodes(): End");

		return locationCodes;

	}

	@Override
	public int getTotalRTs(String searchID) {

		logger.debug("Method getTotalRTs(): Start");

		Assert.assertNotNull(this.daoService, "Method getTotalRTs(): Impossible to get total RTs without a valid DaoService");

		String queryHQL = "select tweet.tweetID from TwitterData tweet where tweet.isRetweet = true and tweet.twitterSearch.searchID = ?";

		int result = daoService.countQuery(queryHQL, Long.parseLong(searchID));

		logger.debug("Method getTotalRTs(): End");

		return result;

	}

	@Override
	public int getTotalReplies(String searchID) {

		logger.debug("Method getTotalReplies(): Start");

		Assert.assertNotNull(this.daoService, "Method getTotalReplies(): Impossible to get total replies without a valid DaoService");

		String queryHQL = "select tweet.tweetID from TwitterData tweet where tweet.replyToTweetId != null and tweet.twitterSearch.searchID = ?";

		int result = daoService.countQuery(queryHQL, Long.parseLong(searchID));

		logger.debug("Method getTotalReplies(): End");

		return result;

	}

	@Override
	public List<String> getSources(String searchID) {

		logger.debug("Method getSources(): Start");

		Assert.assertNotNull(this.daoService, "Method getSources(): Impossible to get sources without a valid DaoService");

		String query = "SELECT td.sourceClient from TwitterData td where td.twitterSearch.searchID = ?";

		List<String> sources = daoService.listFromQuery(query, Long.parseLong(searchID));

		logger.debug("Method getSources(): End");

		return sources;

	}

	@Override
	public List<String> getAccounts(String searchID) {

		logger.debug("Method getAccounts(): Start");

		Assert.assertNotNull(this.daoService, "Method getAccounts(): Impossible to get accounts without a valid DaoService");

		String query = "SELECT ta.accountName from TwitterAccountToMonitor ta where ta.twitterSearch.searchID = ? GROUP BY ta.accountName";

		List<String> accounts = daoService.listFromQuery(query, Long.parseLong(searchID));

		logger.debug("Method getAccounts(): End");

		return accounts;

	}

	@Override
	public List<TwitterAccountToMonitor> getAccountsToMonitorInfo(String searchID, String accountName) {

		logger.debug("Method getAccountsToMonitorInfo(): Start");

		Assert.assertNotNull(this.daoService, "Method getAccountsToMonitorInfo(): Impossible to get account info without a valid DaoService");

		String query = "SELECT new TwitterAccountToMonitor(ta.followersCount, ta.timestamp) from TwitterAccountToMonitor ta where ta.twitterSearch.searchID = ? and ta.accountName = ? order by ta.timestamp asc";

		List<TwitterAccountToMonitor> accountInfo = daoService.listFromQuery(query, Long.parseLong(searchID), accountName);

		logger.debug("Method getAccountsToMonitorInfo(): End");
		return accountInfo;

	}

	@Override
	public List<String> getLinks(String searchID) {

		logger.debug("Method getLinks(): Start");

		Assert.assertNotNull(this.daoService, "Method getLinks(): Impossible to get links without a valid DaoService");

		String query = "SELECT tl.link from TwitterLinkToMonitor tl where tl.twitterSearch.searchID = ? GROUP BY tl.link";

		List<String> links = daoService.listFromQuery(query, Long.parseLong(searchID));

		logger.debug("Method getLinks(): End");

		return links;

	}

	@Override
	public List<TwitterLinkToMonitor> getLinksToMonitorInfo(String searchID, String link) {

		logger.debug("Method getLinksToMonitorInfo(): Start");

		Assert.assertNotNull(this.daoService, "Method getLinksToMonitorInfo(): Impossible to get link info without a valid DaoService");

		String query = "SELECT new TwitterLinkToMonitor(tl.longUrl, tl.clicksCount, tl.timestamp) from TwitterLinkToMonitor tl where tl.twitterSearch.searchID = ? and tl.link = ? order by tl.timestamp asc";

		List<TwitterLinkToMonitor> linkInfo = daoService.listFromQuery(query, Long.parseLong(searchID), link);

		logger.debug("Method getLinksToMonitorInfo(): End");
		return linkInfo;

	}

	@Override
	public MonitorRepeatTypeEnum getMonitorRepeationType(String searchID) {

		logger.debug("Method getMonitorRepeationType(): Start");

		Assert.assertNotNull(this.daoService, "Method getMonitorRepeationType(): Impossible to get monitor repetition type without a valid DaoService");

		String query = "select tms.repeatType from TwitterMonitorScheduler tms WHERE tms.twitterSearch.searchID = ?";

		MonitorRepeatTypeEnum repeatType = daoService.singleResultQuery(query, Long.parseLong(searchID));

		logger.debug("Method getMonitorRepeationType(): End");

		return repeatType;

	}

	@Override
	public List<String> getHashtags(String searchID) {

		logger.debug("Method getHashtags(): Start");

		Assert.assertNotNull(this.daoService, "Method getHashtags(): Impossible to get hashtags without a valid DaoService");

		String query = "SELECT td.hashtags from TwitterData td where td.twitterSearch.searchID = ?";

		List<String> hashtags = daoService.listFromQuery(query, Long.parseLong(searchID));

		logger.debug("Method getHashtags(): End");

		return hashtags;

	}

	@Override
	public List<TwitterData> getTimelineTweets(String searchID) {

		logger.debug("Method getTimelineTweets(): Start");

		Assert.assertNotNull(this.daoService, "Method getTimelineTweets(): Impossible to get tweets info for timeline without a valid DaoService");

		String query = "SELECT new TwitterData(td.timeCreatedAt, td.isRetweet) from TwitterData td where td.twitterSearch.searchID = ? order by td.timeCreatedAt asc";

		List<TwitterData> twitterDatas = daoService.listFromQuery(query, Long.parseLong(searchID));

		logger.debug("Method getTimelineTweets(): End");
		return twitterDatas;

	}

	@Override
	public List<TwitterData> getTopTweetsRTsOrder(String searchID, int nProfiles) {

		logger.debug("Method getTopTweetsRTsOrder(): Start");

		Assert.assertNotNull(this.daoService, "Method getTopTweetsRTsOrder(): Impossible to get top tweets info without a valid DaoService");

		String query = "SELECT new TwitterData(td.twitterUser, td.dateCreatedAt, td.hashtags, td.tweetText, td.timeCreatedAt, td.retweetCount) from TwitterData td where td.twitterSearch.searchID = ? order by td.retweetCount DESC";

		List<TwitterData> twitterDatas = daoService.listFromLimitedQuery(query, nProfiles, Long.parseLong(searchID));

		logger.debug("Method getTopTweetsRTsOrder(): End");
		return twitterDatas;
	}

	@Override
	public List<TwitterData> getTopTweetsRecentOrder(String searchID, int nProfiles) {

		logger.debug("Method getTopTweetsRecentOrder(): Start");

		Assert.assertNotNull(this.daoService, "Method getTopTweetsRecentOrder(): Impossible to get top recent tweets info without a valid DaoService");

		String query = "SELECT new TwitterData(td.twitterUser, td.dateCreatedAt, td.hashtags, td.tweetText, td.timeCreatedAt, td.retweetCount)) from TwitterData td where td.twitterSearch.searchID = ? order by td.timeCreatedAt DESC";

		List<TwitterData> twitterDatas = daoService.listFromLimitedQuery(query, nProfiles, Long.parseLong(searchID));

		logger.debug("Method getTopTweetsRecentOrder(): End");
		return twitterDatas;

	}

	@Override
	public Calendar getMinLinksTime(String searchID) {

		logger.debug("Method getMinLinksTime(): Start");

		Assert.assertNotNull(this.daoService, "Method getMinLinksTime(): Impossible to get min links time without a valid DaoService");

		String query = "select links.timestamp from TwitterLinkToMonitor links where links.twitterSearch.searchID = ? order by links.timestamp ASC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMinLinksTime(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMinLinksTime(): End");
			return null;
		}

	}

	@Override
	public Calendar getMaxLinksTime(String searchID) {

		logger.debug("Method getMaxLinksTime(): Start");

		Assert.assertNotNull(this.daoService, "Method getMaxLinksTime(): Impossible to get max links time without a valid DaoService");

		String query = "select links.timestamp from TwitterLinkToMonitor links where links.twitterSearch.searchID = ? order by links.timestamp DESC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMaxLinksTime(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMaxLinksTime(): End");
			return null;
		}

	}

	@Override
	public Calendar getMinAccountsTime(String searchID) {

		logger.debug("Method getMinAccountsTime(): Start");

		Assert.assertNotNull(this.daoService, "Method getMinAccountsTime(): Impossible to get min accounts time without a valid DaoService");

		String query = "select accounts.timestamp from TwitterAccountToMonitor accounts where accounts.twitterSearch.searchID = ? order by accounts.timestamp ASC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMinAccountsTime(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMinAccountsTime(): End");
			return null;
		}

	}

	@Override
	public Calendar getMaxAccountsTime(String searchID) {

		logger.debug("Method getMaxAccountsTime(): Start");

		Assert.assertNotNull(this.daoService, "Method getMaxAccountsTime(): Impossible to get max links time without a valid DaoService");

		String query = "select accounts.timestamp from TwitterAccountToMonitor accounts where accounts.twitterSearch.searchID = ? order by accounts.timestamp DESC";

		List<Calendar> dates = daoService.listFromQuery(query, Long.parseLong(searchID));

		if (dates != null && dates.size() > 0) {

			logger.debug("Method getMaxAccountsTime(): End");
			return dates.get(0);

		} else {

			logger.debug("Method getMaxAccountsTime(): End");
			return null;
		}

	}

}
