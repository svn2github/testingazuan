package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.launcher.TwitterAnalysisLauncher;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;

import java.sql.SQLException;

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

		String sqlQuery = "SELECT keywords, type, loading from twitter_search where search_id = '" + searchID + "'";

		try {
			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					String keywords = rs.getString("keywords");
					String type = rs.getString("type");
					// TODO possibile utilizzo per evitare conflitti
					boolean loading = rs.getBoolean("loading");

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

			logger.debug("Instance " + key + " of HistoricalSearchJob. Executing search #: " + searchID);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}

		// logger.debug("Hello Quartz!");

	}

	public void setSearchID(long searchID) {
		this.searchID = searchID;
	}

}
