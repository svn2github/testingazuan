package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StreamingSchedulerInitializeJob implements Job {

	static final Logger logger = Logger.getLogger(StreamingSchedulerInitializeJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.debug("Initializing Twitter Stream");

		logger.debug("All streams must to be loading = 0");
		twitterCache.stopStreamingSearch();

	}
}
