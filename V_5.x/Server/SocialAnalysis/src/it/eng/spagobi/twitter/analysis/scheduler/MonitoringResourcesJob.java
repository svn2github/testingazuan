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

@DisallowConcurrentExecution
public class MonitoringResourcesJob implements Job {

	static final Logger logger = Logger.getLogger(MonitoringResourcesJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	private long searchID;

	public MonitoringResourcesJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// JobKey key = context.getJobDetail().getKey();

		String sqlResourcesQuery = "SELECT links, accounts from twitter_monitor_scheduler where search_id = '" + searchID + "'";

		String links = "";
		String accounts = "";

		try {
			CachedRowSet rs = twitterCache.runQuery(sqlResourcesQuery);

			if (rs != null && rs.next()) {

				links = rs.getString("links");
				accounts = rs.getString("accounts");

				BitlyCounterClicksUtility bitlyUtil = new BitlyCounterClicksUtility(links, searchID);
				bitlyUtil.startBitlyAnalysis();

				TwitterUserInfoUtility userUtil = new TwitterUserInfoUtility(searchID);

				accounts = accounts.replaceAll("@", "");
				String[] accountsArr = accounts.split(",");

				if (accountsArr != null && accountsArr.length > 0) {
					for (int i = 0; i < accountsArr.length; i++) {
						String account = accountsArr[i].trim();
						userUtil.saveFollowersCount(account);
					}
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
