package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheImpl;
import it.eng.spagobi.twitter.analysis.entities.TwitterMonitorScheduler;
import it.eng.spagobi.twitter.analysis.entities.TwitterSearch;
import it.eng.spagobi.twitter.analysis.utilities.AnalysisUtility;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StreamingSchedulerInitializeJob implements Job {

	static final Logger logger = Logger.getLogger(StreamingSchedulerInitializeJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheImpl();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.debug("Initializing Twitter Stream");

		TwitterSearch twitterSearch = twitterCache.isPresentEnabledStream();

		if (twitterSearch != null) {

			TwitterMonitorScheduler twitterMonitor = twitterSearch.getTwitterMonitorScheduler();

			if (twitterMonitor != null) {
				twitterMonitor.setEndingTime(AnalysisUtility.setMonitorSchedulerEndingDate(twitterMonitor));
				twitterMonitor.setActiveSearch(false);

				twitterCache.updateTwitterMonitorScheduler(twitterMonitor);
			}

		}

		logger.debug("All streams must to be loading = 0");
		twitterCache.stopAllStreams();

	}
}
