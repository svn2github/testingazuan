package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.bitly.analysis.utilities.BitlyCounterClicksUtility;
import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.launcher.TwitterAnalysisLauncher;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchSchedulerPojo;
import it.eng.spagobi.twitter.analysis.utilities.TwitterUserInfoUtility;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

@DisallowConcurrentExecution
public class HistoricalSearchJob implements Job {

	static final Logger logger = Logger.getLogger(HistoricalSearchJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	private long searchID;

	public HistoricalSearchJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobKey key = context.getJobDetail().getKey();

		String sqlSearchQuery = "SELECT keywords, type, loading from twitter_search where search_id = '" + searchID + "'";

		String sqlLinksQuery = "SELECT link from twitter_link_to_monitor where search_id = '" + searchID + "'";

		String sqlAccountsQuery = "SELECT account from twitter_account_to_monitor where search_id = '" + searchID + "'";

		String sqlSchedulerQuery = "SELECT * from twitter_search_scheduler where search_id = '" + searchID + "'";

		try {
			CachedRowSet searchRS = twitterCache.runQuery(sqlSearchQuery);

			if (searchRS != null) {

				logger.debug("Instance " + key + " of HistoricalSearchJob. Executing search #: " + searchID);

				while (searchRS.next()) {

					String keywords = searchRS.getString("keywords");
					String type = searchRS.getString("type");
					// TODO possibile utilizzo per evitare conflitti
					boolean loading = searchRS.getBoolean("loading");

					TwitterSearchPojo twitterSearch = new TwitterSearchPojo();
					twitterSearch.setKeywords(keywords);
					twitterSearch.setType(type);
					twitterSearch.setDbType("MySQL");
					twitterSearch.setLanguageCode(null);
					twitterSearch.setSearchID(searchID);
					twitterSearch.setSearchType("searchAPI");

					TwitterAnalysisLauncher twitterLauncher = new TwitterAnalysisLauncher(twitterSearch);
					twitterLauncher.startHistoricalSearch();

				}
			}

			CachedRowSet linkRS = twitterCache.runQuery(sqlLinksQuery);

			if (linkRS != null) {

				logger.debug("Instance " + key + " of HistoricalSearchJob. Executing monitoring links on search #: " + searchID);

				while (linkRS.next()) {

					String link = linkRS.getString("link");

					BitlyCounterClicksUtility bitlyUtil = new BitlyCounterClicksUtility();
					bitlyUtil.setSearchID(searchID);

					bitlyUtil.monitorBitlyLink(link);

				}
			}

			CachedRowSet accountRS = twitterCache.runQuery(sqlAccountsQuery);

			if (accountRS != null) {

				logger.debug("Instance " + key + " of HistoricalSearchJob. Executing monitoring accounts on search #: " + searchID);

				while (accountRS.next()) {

					String account = accountRS.getString("account");

					TwitterUserInfoUtility userUtil = new TwitterUserInfoUtility(searchID);
					userUtil.saveFollowersCount(account);
				}
			}

			CachedRowSet schedulerRS = twitterCache.runQuery(sqlSchedulerQuery);

			if (schedulerRS != null) {

				logger.debug("Instance " + key + " of HistoricalSearchJob. Executing updating search scheduler on search #: " + searchID);

				while (schedulerRS.next()) {

					int id = schedulerRS.getInt("id");
					java.sql.Timestamp startingTimestamp = schedulerRS.getTimestamp("starting_date");
					int repeatFrequency = schedulerRS.getInt("repeat_frequency");
					String repeatType = schedulerRS.getString("repeat_type");

					Calendar startingCalendar = GregorianCalendar.getInstance();
					startingCalendar.setTimeInMillis(startingTimestamp.getTime());

					if (repeatType != null) {
						if (repeatType.equals("Day")) {
							startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);
						} else if (repeatType.equals("Hour")) {
							startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);
						}
					}

					TwitterSearchSchedulerPojo twitterSearchScheduler = new TwitterSearchSchedulerPojo();
					twitterSearchScheduler.setSearchID(searchID);
					twitterSearchScheduler.setId(id);
					twitterSearchScheduler.setStartingDate(startingCalendar);

					twitterCache.updateStartingDateSearchScheduler(twitterSearchScheduler);

				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}

		// logger.debug("Hello Quartz!");

	}

	public void setSearchID(long searchID) {
		this.searchID = searchID;
	}

}
