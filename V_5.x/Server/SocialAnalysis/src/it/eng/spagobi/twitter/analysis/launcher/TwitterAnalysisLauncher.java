/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.spagobi.twitter.analysis.launcher;

import it.eng.spagobi.bitly.analysis.utilities.BitlyCounterClicksUtility;
import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMonitorSchedulerPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchSchedulerPojo;
import it.eng.spagobi.twitter.analysis.scheduler.HistoricalSearchJob;
import it.eng.spagobi.twitter.analysis.scheduler.MonitoringResourcesJob;
import it.eng.spagobi.twitter.analysis.spider.search.TwitterSearchAPISpider;
import it.eng.spagobi.twitter.analysis.spider.streaming.TwitterStreamingAPISpider;
import it.eng.spagobi.twitter.analysis.utilities.TwitterUserInfoUtility;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import twitter4j.Query.ResultType;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public class TwitterAnalysisLauncher {

	static final Logger logger = Logger.getLogger(TwitterAnalysisLauncher.class);

	// TODO: da questa classe posso decidere qualche tipologia di ricerca
	// lanciare e settare alcuni parametri (es: keyword, lingua ecc)

	TwitterSearchPojo twitterSearch;
	private final ITwitterCache cache;

	public TwitterAnalysisLauncher(TwitterSearchPojo twitterSearchPojo) {

		this.twitterSearch = twitterSearchPojo;

		// initialize the cache with the db type inserted
		this.cache = initCache(this.twitterSearch.getDbType());

	}

	/**
	 * This method is used to initialize the DB for this search
	 *
	 * @param dbType
	 *            : db type
	 * @return the interface with DB methods
	 */
	public ITwitterCache initCache(String dbType) {

		logger.debug("Method initCache: Start");

		ITwitterCache twitterCache = null;

		// factory di cache con specifiche implementazioni
		TwitterCacheFactory twitterCacheFactory = new TwitterCacheFactory();

		if (twitterCacheFactory != null) {

			twitterCache = twitterCacheFactory.getCache(dbType);
		}

		logger.debug("Method initCache: End");

		return twitterCache;
	}

	/**
	 * This method is used to create and execute the historical search for the
	 * first time. First it creates the search into DB, next launches the
	 * twitter search thread and the monitoring resource thread. Only this time
	 * search and monitoring are together. After, if there are schedulers, they
	 * will have different start.
	 *
	 * @return searchID: the ID of the search
	 */
	public long createhistoricalSearch() {

		logger.debug("Method createHistoricalSearch(): Start..");

		// insert new search into DB, without linked tweets and resources
		long searchID = cache.insertTwitterSearch(this.twitterSearch);

		this.twitterSearch.setSearchID(searchID);

		// search correctly inserted
		if (searchID > 0) {

			logger.debug("Method createHistoricalSearch(): New search inserted with ID = " + searchID);

			// historical search is loading, loading = true;
			logger.debug("Method createHistoricalSearch(): Historical Search is loading");
			cache.updateTwitterSearchLoading(twitterSearch.getSearchID(), true);

			// launch historical search linked with twitter SEARCH API
			logger.debug("Method createHistoricalSearch(): Twitter Search Thread starting..");
			startHistoricalSearchThread();

			// manage search scheduler
			if (twitterSearch.getTwitterScheduler() != null) {

				TwitterSearchSchedulerPojo twitterScheduler = twitterSearch.getTwitterScheduler();

				// searchID have to set now
				twitterScheduler.setSearchID(twitterSearch.getSearchID());
				cache.insertTwitterSearchScheduler(twitterScheduler);

				createHistoricalSearchTrigger(twitterScheduler);
			}

			// manage monitor scheduler
			if (twitterSearch.getTwitterMonitorScheduler() != null) {

				TwitterMonitorSchedulerPojo twitterMonitorScheduler = twitterSearch.getTwitterMonitorScheduler();

				// searchID have to set now
				twitterMonitorScheduler.setSearchID(twitterSearch.getSearchID());

				this.twitterSearch.setLinks(twitterMonitorScheduler.getLinks());
				this.twitterSearch.setAccounts(twitterMonitorScheduler.getAccounts());
				this.twitterSearch.setDocuments(twitterMonitorScheduler.getDocuments());

				setMonitorSchedulerEndingDate(twitterMonitorScheduler);

				cache.insertTwitterMonitorScheduler(twitterMonitorScheduler);

				// launch monitoring resources for this search
				logger.debug("Method createHistoricalSearch(): Monitoring resources Threads starting..");
				startMonitoringResourcesThreads();

				if (twitterSearch.getTwitterScheduler() == null) {

					createMonitoringTriggerWithEndingDate(twitterMonitorScheduler);

				} else {

					createMonitoringTriggerWithoutEndingDate(twitterMonitorScheduler);

				}

			}

			return searchID;

		} else {
			logger.debug("Method createHistoricalSearch(): New search not inserted. Failure");

			return -1;
		}

	}

	/**
	 * This method is used to crete the streaming search for the first time.
	 *
	 * @return searchID: the ID of the search
	 */
	public long createStreamingSearch() {

		logger.debug("Method createStreamingSearch(): Start..");

		// insert new search into DB, without linked tweets and resources
		long searchID = cache.insertTwitterSearch(this.twitterSearch);

		this.twitterSearch.setSearchID(searchID);

		if (searchID > 0) {

			logger.debug("Method createStreamingSearch(): New search inserted with ID = " + searchID);

			// // launch monitoring resources for this search
			// logger.debug("Method createStreamingSearch(): Monitoring resources Threads starting..");
			// startMonitoringResourcesThreads();

			// manage monitor scheduler
			if (twitterSearch.getTwitterMonitorScheduler() != null) {

				TwitterMonitorSchedulerPojo twitterMonitorScheduler = twitterSearch.getTwitterMonitorScheduler();

				// searchID have to set now
				twitterMonitorScheduler.setSearchID(twitterSearch.getSearchID());

				setMonitorSchedulerEndingDate(twitterMonitorScheduler);

				cache.insertTwitterMonitorScheduler(twitterMonitorScheduler);

			}

			return searchID;

		} else {
			logger.debug("Method createStreamingSearch(): New search not inserted. Failure");

			return -1;
		}

	}

	/**
	 * Method used by historical search jobs. This particular search already
	 * exist in the DB
	 */
	public void startHistoricalSearch() {

		logger.debug("Method startHistoricalSearch(): Start..");

		// launch historical search linked with twitter SEARCH API
		logger.debug("Method createHistoricalSearch(): Twitter Search Thread starting..");
		startHistoricalSearchThread();

		logger.debug("Method startHistoricalSearch: End");

	}

	/**
	 * This method is used to start the twitter streaming search. When a new
	 * stream starts, you have to close the previous opened stream (in DB
	 * loading = false and clean the API listener) and update their resources
	 * monitoring, unscheduling the active trigger and creating a new monitor
	 * scheduler with the ending date. Then you have to start the new stream and
	 * launch the trigger for the new streaming search
	 */
	public void startStreamingSearch() {

		logger.debug("Method startStreamingSearch(): Start");

		// stopping last active stream
		stopStreamingSearch();

		// initialize for new stream
		TwitterStreamingAPISpider streamingAPI = initializeStreamingAPI();

		logger.debug("Method startStreamingSearch(): Starting the new Stream");
		streamingAPI.collectTweets();

		cache.updateTwitterSearchLoading(twitterSearch.getSearchID(), true);

		// launch monitoring resources for this search

		TwitterMonitorSchedulerPojo twitterMonitor = cache.getTwitterMonitorScheduler(twitterSearch.getSearchID());

		if (twitterMonitor != null) {

			try {
				// search active trigger linked with this search
				SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

				Scheduler scheduler = schedFact.getScheduler();

				// re-schedule resource monitoring for new active stream
				logger.debug("Method startStreamingSearch(): Unscheduling trigger MonitoringTgr_" + twitterSearch.getSearchID());
				scheduler.unscheduleJob(TriggerKey.triggerKey("MonitoringTgr_" + twitterSearch.getSearchID(), "groupMonitoring"));

			} catch (SchedulerException e) {

				logger.debug("Method startStreamingSearch(): Error stopping scheduler for search " + twitterSearch.getSearchID());
			}

			logger.debug("Method startStreamingSearch(): Monitoring resources Threads starting..");

			this.twitterSearch.setLinks(twitterMonitor.getLinks());
			this.twitterSearch.setAccounts(twitterMonitor.getAccounts());
			this.twitterSearch.setDocuments(twitterMonitor.getDocuments());

			// thread to get resources info
			startMonitoringResourcesThreads();

			// schedule next monitoring, no set end time -> streaming search
			// active
			createMonitoringTriggerWithoutEndingDate(twitterMonitor);

		}

		logger.debug("Method startStreamingSearch(): End");

	}

	/**
	 * This method is used to start the twitter streaming search. When a new
	 * stream starts, you have to close the previous opened stream (in DB
	 * loading = false and clean the API listener) and update their resources
	 * monitoring, unscheduling the active trigger
	 * */
	public void stopStreamingSearch() {

		logger.debug("Method stopStreamingSearch(): Start");

		TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

		twitterStream.clearListeners();
		twitterStream.cleanUp();

		// set false for all streaming searches, retrieving the ID of last
		// active search
		long lastActiveSearchID = cache.stopStreamingSearch();

		try {

			// search active trigger linked with this search
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler scheduler = schedFact.getScheduler();

			// there was a stream active, re-schedule his resources monitoring
			if (lastActiveSearchID > 0) {

				logger.debug("Method stopStreamingSearch(): Unscheduling trigger MonitoringTgr_" + lastActiveSearchID);
				scheduler.unscheduleJob(TriggerKey.triggerKey("MonitoringTgr_" + lastActiveSearchID, "groupMonitoring"));

				TwitterMonitorSchedulerPojo twitterMonitor = cache.getTwitterMonitorScheduler(lastActiveSearchID);

				if (twitterMonitor != null) {

					// Calculating the ending date
					setMonitorSchedulerEndingDate(twitterMonitor);

					twitterMonitor.setActive(true);

					cache.updateMonitorScheduler(twitterMonitor);

					logger.debug("Method stopStreamingSearch(): Starting monitor resources for search: " + lastActiveSearchID);

					createMonitoringTriggerWithEndingDate(twitterMonitor);

				}
			}

		} catch (SchedulerException e) {
			logger.debug("Method stopStreamingSearch(): Error stopping scheduler for search " + twitterSearch.getSearchID());
		}
		logger.debug("Method stopStreamingSearch(): End");

	}

	public void deleteSearch() {

		logger.debug("Method deleteSearch(): Start");

		if (twitterSearch.getSearchType().equals("streamingAPI") && twitterSearch.isLoading()) {

			TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

			twitterStream.clearListeners();
			twitterStream.cleanUp();
		}

		this.cache.deleteSearch(twitterSearch);

		logger.debug("Method deleteSearch(): End");

	}

	public void stopSearchScheduler() {

		logger.debug("Method stopSearchScheduler(): Start");

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler scheduler = schedFact.getScheduler();

			logger.debug("Method stopSearchScheduler(): Unscheduling trigger HSearchTgr_" + twitterSearch.getSearchID());
			scheduler.unscheduleJob(TriggerKey.triggerKey("HSearchTgr_" + twitterSearch.getSearchID(), "groupHSearch"));

			logger.debug("Method stopSearchScheduler(): Unscheduling trigger MonitoringTgr_" + twitterSearch.getSearchID());
			scheduler.unscheduleJob(TriggerKey.triggerKey("MonitoringTgr_" + twitterSearch.getSearchID(), "groupMonitoring"));

			TwitterMonitorSchedulerPojo twitterMonitor = this.cache.stopSearchScheduler(twitterSearch);

			if (twitterMonitor != null) {

				setMonitorSchedulerEndingDate(twitterMonitor);

				twitterMonitor.setActive(true);

				cache.updateMonitorScheduler(twitterMonitor);

				logger.debug("Method stopSearchScheduler(): Starting monitor resources for search: " + twitterSearch.getSearchID());

				createMonitoringTriggerWithEndingDate(twitterMonitor);
			}
		}

		catch (SchedulerException e) {
			logger.debug("Method stopSearchScheduler(): Error stopping scheduler for search " + twitterSearch.getSearchID());
		}

		logger.debug("Method deleteSearch(): End");

	}

	public void removeFailedSearch() {

		logger.debug("Method removeFailedSearch(): Start");

		this.cache.deleteSearch(twitterSearch);

		logger.debug("Method removeFailedSearch(): End");

	}

	private TwitterSearchAPISpider initializeSearchAPI() {

		TwitterSearchAPISpider twitterSearchAPI = new TwitterSearchAPISpider();

		twitterSearchAPI.setCache((this.cache));
		twitterSearchAPI.setLanguage(this.twitterSearch.getLanguageCode());
		twitterSearchAPI.setResultType(ResultType.recent);

		String[] keywordsArr = twitterSearch.getKeywords().split(",");
		String textQuery = "";

		// TODO: 5 as default, query too complex

		for (int i = 0; (i < keywordsArr.length) && (i < 5); i++) {
			if (i == 0) {
				textQuery = keywordsArr[i].trim();
			} else {
				textQuery = textQuery + " OR " + keywordsArr[i].trim();
			}
		}

		twitterSearchAPI.setQuery(textQuery);
		twitterSearchAPI.setSearchID(this.twitterSearch.getSearchID());
		twitterSearchAPI.setSinceDate(this.twitterSearch.getSinceDate());

		return twitterSearchAPI;

	}

	private TwitterStreamingAPISpider initializeStreamingAPI() {

		TwitterStreamingAPISpider twitterStreamingAPI = new TwitterStreamingAPISpider();

		twitterStreamingAPI.setSearchID(twitterSearch.getSearchID());
		twitterStreamingAPI.setCache(cache);

		String[] keywordsArr = twitterSearch.getKeywords().split(" ");
		twitterStreamingAPI.setTrack(keywordsArr);

		if (twitterSearch.getLanguageCode() != null && !twitterSearch.getLanguageCode().equals("")) {

			String[] languageCodeArr = twitterSearch.getLanguageCode().split(" ");
			twitterStreamingAPI.setLanguage(languageCodeArr);

		} else {
			twitterStreamingAPI.setLanguage(null);
		}

		return twitterStreamingAPI;

	}

	private void startHistoricalSearchThread() {

		Thread twitterSearchThread = new Thread() {
			@Override
			public void run() {

				if (twitterSearch.getSearchType().equalsIgnoreCase("SearchAPI")) {

					// initialize SearchAPI
					logger.debug("Method createHistoricalSearch(): Initializing Historical Search");
					TwitterSearchAPISpider searchAPI = initializeSearchAPI();

					searchAPI.collectTweets();

					// historical search completed, loading = false;
					logger.debug("Method createHistoricalSearch(): Historical Search completed");
					cache.updateTwitterSearchLoading(twitterSearch.getSearchID(), false);

				}

			}
		};

		twitterSearchThread.start();
	}

	private void startMonitoringResourcesThreads() {

		if (twitterSearch.getLinks() != null && !twitterSearch.getLinks().equals("")) {

			Thread bitlyAnalysisThread = new Thread() {
				@Override
				public void run() {

					BitlyCounterClicksUtility bitlyUtil = new BitlyCounterClicksUtility(twitterSearch.getLinks(), twitterSearch.getSearchID());

					bitlyUtil.startBitlyAnalysis();

				}
			};

			bitlyAnalysisThread.start();
		}

		if (twitterSearch.getAccounts() != null && !twitterSearch.getAccounts().equals("")) {

			Thread accountAnalysisThread = new Thread() {

				String accounts = twitterSearch.getAccounts();

				@Override
				public void run() {

					TwitterUserInfoUtility userUtil = new TwitterUserInfoUtility(twitterSearch.getSearchID());

					accounts = accounts.trim().replaceAll("@", "");
					String[] accountArr = accounts.split(",");

					for (int i = 0; i < accountArr.length; i++) {
						String account = accountArr[i].trim();
						userUtil.saveFollowersCount(account);
					}

				}
			};

			accountAnalysisThread.start();
		}

		if (twitterSearch.getDocuments() != null && !twitterSearch.getDocuments().equals("")) {

			Thread documentAnalysisThread = new Thread() {

				String documents = twitterSearch.getDocuments();

				@Override
				public void run() {

					// TwitterUserInfoUtility userUtil = new
					// TwitterUserInfoUtility(twitterSearch.getSearchID());
					//
					// accounts = accounts.trim().replaceAll("@", "");
					// String[] accountArr = accounts.split(",");
					//
					// for (int i = 0; i < accountArr.length; i++) {
					// String account = accountArr[i].trim();
					// userUtil.saveFollowersCount(account);
					// }

				}
			};

			documentAnalysisThread.start();
		}

	}

	private void createHistoricalSearchTrigger(TwitterSearchSchedulerPojo tScheduler) {

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();

			long searchID = this.twitterSearch.getSearchID();

			JobDetail hSearchJob = JobBuilder.newJob(HistoricalSearchJob.class).withIdentity("HSearchJob_" + searchID, "groupHSearch")
					.usingJobData("searchID", searchID).build();

			// scheduler parameters

			Calendar startingCalendar = tScheduler.getStartingDate();
			Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

			int frequency = tScheduler.getRepeatFrequency();
			String type = tScheduler.getRepeatType();

			if (type.equalsIgnoreCase("day")) {

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" + searchID, "groupHSearch").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.DAY)).build();

				// Tell quartz to schedule the job using our trigger
				sched.scheduleJob(hSearchJob, trigger);

			} else if (type.equalsIgnoreCase("hour")) {

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" + searchID, "groupHSearch").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.HOUR))
						.build();

				// Tell quartz to schedule the job using our trigger
				sched.scheduleJob(hSearchJob, trigger);

			}

		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void createMonitoringTriggerWithoutEndingDate(TwitterMonitorSchedulerPojo twitterMonitor) {

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();

			long searchID = twitterMonitor.getSearchID();

			// Job details
			JobDetail hSearchJob = JobBuilder.newJob(MonitoringResourcesJob.class).withIdentity("MonitoringJob_" + searchID, "groupMonitoring")
					.usingJobData("searchID", searchID).build();

			int repeatFrequency = twitterMonitor.getRepeatFrequency();
			String repeatType = twitterMonitor.getRepeatType();

			Calendar startingCalendar = GregorianCalendar.getInstance();

			if (repeatType.equalsIgnoreCase("Day")) {

				startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

				Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("MonitoringTgr_" + searchID, "groupMonitoring").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.DAY))
						.build();

				sched.scheduleJob(hSearchJob, trigger);

			} else if (repeatType.equalsIgnoreCase("Hour")) {

				startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

				Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("MonitoringTgr_" + searchID, "groupMonitoring").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.HOUR))
						.build();

				sched.scheduleJob(hSearchJob, trigger);

			}

		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private void createMonitoringTriggerWithEndingDate(TwitterMonitorSchedulerPojo twitterMonitor) {

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();

			long searchID = twitterMonitor.getSearchID();

			// Job details
			JobDetail hSearchJob = JobBuilder.newJob(MonitoringResourcesJob.class).withIdentity("MonitoringJob_" + searchID, "groupMonitoring")
					.usingJobData("searchID", searchID).build();

			int repeatFrequency = twitterMonitor.getRepeatFrequency();
			String repeatType = twitterMonitor.getRepeatType();

			Calendar startingCalendar = GregorianCalendar.getInstance();

			if (repeatType.equalsIgnoreCase("Day")) {

				startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

				if (twitterMonitor.getEndingDate().compareTo(startingCalendar) > 0) {

					Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

					Date endingDateJob = new java.util.Date(twitterMonitor.getEndingDate().getTimeInMillis());

					Trigger trigger = TriggerBuilder
							.newTrigger()
							.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
							.startAt(startingDateJob)
							.endAt(endingDateJob)
							.withSchedule(
									CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.DAY))
							.build();

					sched.scheduleJob(hSearchJob, trigger);
				}

			} else if (repeatType.equalsIgnoreCase("Hour")) {

				startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

				if (twitterMonitor.getEndingDate().compareTo(startingCalendar) > 0) {

					Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

					Date endingDateJob = new java.util.Date(twitterMonitor.getEndingDate().getTimeInMillis());

					Trigger trigger = TriggerBuilder
							.newTrigger()
							.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
							.startAt(startingDateJob)
							.endAt(endingDateJob)
							.withSchedule(
									CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.HOUR))
							.build();

					sched.scheduleJob(hSearchJob, trigger);
				}
			}

		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private void setMonitorSchedulerEndingDate(TwitterMonitorSchedulerPojo twitterMonitorScheduler) {
		Calendar endingCalendar = GregorianCalendar.getInstance();
		int upToValue = twitterMonitorScheduler.getUpToValue();
		String upToType = twitterMonitorScheduler.getUpToType();

		// Calculating the ending date
		if (upToType.equalsIgnoreCase("Day")) {
			endingCalendar.add(Calendar.DAY_OF_MONTH, upToValue);
		} else if (upToType.equalsIgnoreCase("Week")) {
			endingCalendar.add(Calendar.DAY_OF_MONTH, (upToValue) * 7);
		} else if (upToType.equalsIgnoreCase("Month")) {
			endingCalendar.add(Calendar.DAY_OF_MONTH, (upToValue) * 30);
		}

		twitterMonitorScheduler.setEndingDate(endingCalendar);
	}
}
