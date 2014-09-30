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

import java.util.Calendar;
import java.util.List;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public interface IDataProcessorCache {

	public int getTotalTweets(String searchID);

	public int getTotalUsers(String searchID);

	public Calendar getMinTweetDate(String searchID);

	public Calendar getMaxTweetDate(String searchID);

	public Calendar getMinTweetTime(String searchID);

	public Calendar getMaxTweetTime(String searchID);

	public TwitterMonitorScheduler getDocuments(String searchID);

	public List<TwitterUser> getTopInfluencers(String searchID, int maxResults);

	public List<String> getUsersLocationCodes(String searchID);

	public int getTotalRTs(String searchID);

	public int getTotalReplies(String searchID);

	public List<String> getSources(String searchID);

	public List<String> getAccounts(String searchID);

	public List<TwitterAccountToMonitor> getAccountsToMonitorInfo(String searchID, String accountName);

	public List<String> getLinks(String searchID);

	public List<TwitterLinkToMonitor> getLinksToMonitorInfo(String searchID, String link);

	public MonitorRepeatTypeEnum getMonitorRepeationType(String searchID);

	public List<String> getHashtags(String searchID);

	public List<TwitterData> getTimelineTweets(String searchID);

	public List<TwitterData> getTopTweetsRTsOrder(String searchID, int nProfiles);

	public List<TwitterData> getTopTweetsRecentOrder(String searchID, int nProfiles);

}
