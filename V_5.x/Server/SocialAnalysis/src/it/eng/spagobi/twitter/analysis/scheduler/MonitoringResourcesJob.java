package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.bitly.analysis.utilities.BitlyCounterClicksUtility;
import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.utilities.TwitterUserInfoUtility;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

@DisallowConcurrentExecution
public class MonitoringResourcesJob implements Job {

	static final Logger logger = Logger.getLogger(MonitoringResourcesJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	private long searchID;

	public MonitoringResourcesJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobKey key = context.getJobDetail().getKey();

		String sqlLinksQuery = "SELECT link from twitter_links_to_monitor where search_id = '" + searchID + "'";
		String sqlAccountsQuery = "SELECT account_name from twitter_accounts_to_monitor where search_id = '" + searchID + "'";

		String links = "";
		String accounts = "";

		try {
			CachedRowSet rs = twitterCache.runQuery(sqlLinksQuery);

			if (rs != null) {

				while (rs.next()) {

					String link = rs.getString("link");
					links = links + "," + link;
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}

		BitlyCounterClicksUtility bitlyUtil = new BitlyCounterClicksUtility(links, searchID);

		bitlyUtil.startBitlyAnalysis();

		try {
			TwitterUserInfoUtility userUtil = new TwitterUserInfoUtility(searchID);

			CachedRowSet rs = twitterCache.runQuery(sqlAccountsQuery);

			if (rs != null) {

				while (rs.next()) {

					String account = rs.getString("account_name");
					account = account.replaceAll("@", "");
					userUtil.saveFollowersCount(account);
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	public void setSearchID(long searchID) {
		this.searchID = searchID;
	}

}
