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
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheImpl;
import it.eng.spagobi.twitter.analysis.entities.TwitterMonitorScheduler;
import it.eng.spagobi.twitter.analysis.entities.TwitterSearch;
import it.eng.spagobi.twitter.analysis.entities.TwitterSearchScheduler;
import it.eng.spagobi.twitter.analysis.enums.MonitorRepeatTypeEnum;
import it.eng.spagobi.twitter.analysis.enums.SearchRepeatTypeEnum;
import it.eng.spagobi.twitter.analysis.enums.SearchTypeEnum;
import it.eng.spagobi.twitter.analysis.scheduler.HistoricalSearchJob;
import it.eng.spagobi.twitter.analysis.scheduler.MonitoringResourcesJob;
import it.eng.spagobi.twitter.analysis.spider.search.TwitterSearchAPISpider;
import it.eng.spagobi.twitter.analysis.spider.streaming.TwitterStreamingAPISpider;
import it.eng.spagobi.twitter.analysis.utilities.AnalysisUtility;
import it.eng.spagobi.twitter.analysis.utilities.TwitterUserInfoUtility;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici (giorgio.federici@eng.it)
 *
 */
public class TwitterAnalysisLauncher {

	static final Logger logger = Logger.getLogger(TwitterAnalysisLauncher.class);

	// TODO: da questa classe posso decidere qualche tipologia di ricerca
	// lanciare e settare alcuni parametri (es: keyword, lingua ecc)

	TwitterSearch twitterSearch;
	private final ITwitterCache cache;
	private String languageCode;
	private Calendar sinceCalendar;

	public TwitterAnalysisLauncher() {
		this.cache = new TwitterCacheImpl();
	}

	public TwitterAnalysisLauncher(TwitterSearch twitterSearch) {

		this.twitterSearch = twitterSearch;
		this.cache = new TwitterCacheImpl();

	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public Calendar getSinceCalendar() {
		return sinceCalendar;
	}

	public void setSinceCalendar(Calendar sinceCalendar) {
		this.sinceCalendar = sinceCalendar;
	}

	/**
	 * This method is used to create and execute the historical search for the first time. First it creates the search into DB, next launches the twitter search
	 * thread and the monitoring resource thread. Only this time search and monitoring are together. After, if there are schedulers, they will have different
	 * start.
	 *
	 * @return searchID: the ID of the search
	 */
	public long createhistoricalSearch() {

		logger.debug("Method createHistoricalSearch(): Start..");

		// insert new search into DB, without linked tweets and resources
		this.twitterSearch = cache.createTwitterSearch(this.twitterSearch);

		this.twitterSearch = cache.refreshTwitterSearch(twitterSearch);

		long searchID = this.twitterSearch.getSearchID();

		// search correctly inserted
		if (searchID > 0) {

			logger.debug("Method createHistoricalSearch(): New search inserted with ID = " + searchID);

			// launch historical search linked with twitter SEARCH API
			logger.debug("Method createHistoricalSearch(): Twitter Search Thread starting..");
			startHistoricalSearchThread();

			// manage search scheduler
			if (twitterSearch.getTwitterSearchScheduler() != null) {

				// TwitterSearchScheduler searchScheduler = cache.findTwitterSearchScheduler(twitterSearch.getTwitterSearchScheduler().getId());
				createHistoricalSearchTrigger(twitterSearch.getTwitterSearchScheduler());
			}

			// manage monitor scheduler
			if (twitterSearch.getTwitterMonitorScheduler() != null) {

				boolean activeSearch = twitterSearch.getTwitterMonitorScheduler().isActiveSearch();

				// launch monitoring resources for this search
				logger.debug("Method createHistoricalSearch(): Monitoring resources Threads starting..");
				startMonitoringResourcesThreads();

				if (activeSearch) {

					createMonitoringTriggerWithoutEndingDate(twitterSearch.getTwitterMonitorScheduler());

				} else {

					createMonitoringTriggerWithEndingDate(twitterSearch.getTwitterMonitorScheduler());
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
	public long createEnabledStreamingSearch() {

		logger.debug("Method createEnabledStreamingSearch(): Start..");

		twitterSearch.setLoading(true);

		previousStreamAndMonitorManager();

		// insert new search into DB, without linked tweets and resources

		this.twitterSearch = cache.createTwitterSearch(this.twitterSearch);
		long searchID = this.twitterSearch.getSearchID();

		if (searchID > 0) {

			logger.debug("Method createEnabledStreamingSearch(): New search inserted with ID = " + searchID);

			// initialize for new stream
			TwitterStreamingAPISpider streamingAPI = initializeStreamingAPI();

			logger.debug("Method startStreamingSearch(): Starting the new Stream");
			streamingAPI.collectTweets();

			if (twitterSearch.getTwitterMonitorScheduler() != null) {

				startMonitoringResourcesThreads();

				createMonitoringTriggerWithoutEndingDate(twitterSearch.getTwitterMonitorScheduler());

			}

			return searchID;

		} else {
			logger.debug("Method createEnabledStreamingSearch(): New search not inserted. Failure");

			return -1;
		}

	}

	/**
	 * This method is used to crete the streaming search for the first time.
	 *
	 * @return searchID: the ID of the search
	 */
	public long createDisabledStreamingSearch() {

		logger.debug("Method createDisabledStreamingSearch(): Start..");

		// insert new search into DB, without linked tweets and resources

		this.twitterSearch = cache.createTwitterSearch(this.twitterSearch);
		long searchID = this.twitterSearch.getSearchID();

		if (searchID > 0) {

			logger.debug("Method createDisabledStreamingSearch(): New search inserted with ID = " + searchID);

			if (twitterSearch.getTwitterMonitorScheduler() != null) {

				startMonitoringResourcesThreads();

				createMonitoringTriggerWithEndingDate(twitterSearch.getTwitterMonitorScheduler());

			}

			return searchID;

		} else {
			logger.debug("Method createDisabledStreamingSearch(): New search not inserted. Failure");

			return -1;
		}

	}

	/**
	 * Method used by historical search jobs. This particular search already exist in the DB
	 */
	public void startHistoricalSearch() {

		logger.debug("Method startHistoricalSearch(): Start..");

		// launch historical search linked with twitter SEARCH API
		logger.debug("Method createHistoricalSearch(): Twitter Search Thread starting..");
		startHistoricalSearchThread();

		logger.debug("Method startHistoricalSearch: End");

	}

	/**
	 * This method is used to start the twitter streaming search. When a new stream starts, you have to close the previous opened stream (in DB loading = false
	 * and clean the API listener) and update their resources monitoring, unscheduling the active trigger and creating a new monitor scheduler with the ending
	 * date. Then you have to start the new stream and launch the trigger for the new streaming search
	 */
	public void startStreamingSearch() {

		logger.debug("Method startStreamingSearch(): Start");

		twitterSearch = cache.findTwitterSearch(twitterSearch.getSearchID());

		previousStreamAndMonitorManager();

		// initialize for new stream
		TwitterStreamingAPISpider streamingAPI = initializeStreamingAPI();

		logger.debug("Method startStreamingSearch(): Starting the new Stream");
		streamingAPI.collectTweets();

		twitterSearch.setLoading(true);

		cache.updateTwitterSearch(twitterSearch);

		// launch monitoring resources for this search

		TwitterMonitorScheduler twitterMonitor = twitterSearch.getTwitterMonitorScheduler();

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

			// thread to get resources info
			startMonitoringResourcesThreads();

			// schedule next monitoring, no set end time -> streaming search
			// active
			createMonitoringTriggerWithoutEndingDate(twitterMonitor);

		}

		logger.debug("Method startStreamingSearch(): End");

	}

	/**
	 * This method is used to start the twitter streaming search. When a new stream starts, you have to close the previous opened stream (in DB loading = false
	 * and clean the API listener) and update their resources monitoring, unscheduling the active trigger
	 * */
	public void stopStreamingSearch() {

		logger.debug("Method stopStreamingSearch(): Start");

		twitterSearch = cache.findTwitterSearch(twitterSearch.getSearchID());

		TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

		twitterStream.clearListeners();
		twitterStream.cleanUp();

		twitterSearch.setLoading(false);

		cache.updateTwitterSearch(twitterSearch);

		TwitterMonitorScheduler twitterMonitorScheduler = twitterSearch.getTwitterMonitorScheduler();

		if (twitterMonitorScheduler != null) {

			Calendar endingCalendar = AnalysisUtility.setMonitorSchedulerEndingDate(twitterMonitorScheduler);

			twitterMonitorScheduler.setEndingTime(endingCalendar);

			cache.updateTwitterMonitorScheduler(twitterMonitorScheduler);

			try {

				long lastActiveSearchID = twitterSearch.getSearchID();
				// search active trigger linked with this search
				SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

				Scheduler scheduler = schedFact.getScheduler();

				logger.debug("Method stopStreamingSearch(): Unscheduling trigger MonitoringTgr_" + lastActiveSearchID);
				scheduler.unscheduleJob(TriggerKey.triggerKey("MonitoringTgr_" + lastActiveSearchID, "groupMonitoring"));

				logger.debug("Method stopStreamingSearch(): Starting monitor resources for search: " + lastActiveSearchID);

				createMonitoringTriggerWithEndingDate(twitterMonitorScheduler);

			} catch (SchedulerException e) {
				logger.debug("Method stopStreamingSearch(): Error stopping scheduler for search " + twitterSearch.getSearchID());
			}
		}
		logger.debug("Method stopStreamingSearch(): End");

	}

	public void deleteSearch() {

		logger.debug("Method deleteSearch(): Start");

		this.twitterSearch = this.cache.findTwitterSearch(this.twitterSearch.getSearchID());

		if (twitterSearch.getType() == SearchTypeEnum.STREAMINGAPI && twitterSearch.isLoading()) {

			TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

			twitterStream.clearListeners();
			twitterStream.cleanUp();
		}

		this.cache.deleteSearch(twitterSearch);

		logger.debug("Method deleteSearch(): End");

	}

	public void stopSearchScheduler() {

		logger.debug("Method stopSearchScheduler(): Start");

		twitterSearch = cache.findTwitterSearch(twitterSearch.getSearchID());

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler scheduler = schedFact.getScheduler();

			logger.debug("Method stopSearchScheduler(): Unscheduling trigger HSearchTgr_" + twitterSearch.getSearchID());
			scheduler.unscheduleJob(TriggerKey.triggerKey("HSearchTgr_" + twitterSearch.getSearchID(), "groupHSearch"));

			logger.debug("Method stopSearchScheduler(): Unscheduling trigger MonitoringTgr_" + twitterSearch.getSearchID());
			scheduler.unscheduleJob(TriggerKey.triggerKey("MonitoringTgr_" + twitterSearch.getSearchID(), "groupMonitoring"));

			twitterSearch.getTwitterSearchScheduler().setActive(false);

			cache.updateTwitterSearchScheduler(twitterSearch.getTwitterSearchScheduler());

			TwitterMonitorScheduler twitterMonitor = twitterSearch.getTwitterMonitorScheduler();

			if (twitterMonitor != null) {

				twitterMonitor.setEndingTime(AnalysisUtility.setMonitorSchedulerEndingDate(twitterMonitor));
				twitterMonitor.setActiveSearch(false);

				cache.updateTwitterMonitorScheduler(twitterMonitor);

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

		this.twitterSearch = this.cache.findTwitterSearch(this.twitterSearch.getSearchID());

		this.cache.deleteSearch(twitterSearch);

		logger.debug("Method removeFailedSearch(): End");

	}

	public List<TwitterSearch> getTwitterSearchList(SearchTypeEnum searchType) {
		return cache.getTwitterSearchList(searchType);
	}

	private TwitterSearchAPISpider initializeSearchAPI() {

		TwitterSearchAPISpider twitterSearchAPI = new TwitterSearchAPISpider();

		twitterSearchAPI.setCache((this.cache));
		twitterSearchAPI.setLanguage(this.languageCode);
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
		twitterSearchAPI.setTwitterSearch(this.twitterSearch);
		twitterSearchAPI.setSinceDate(this.sinceCalendar);

		return twitterSearchAPI;

	}

	private TwitterStreamingAPISpider initializeStreamingAPI() {

		TwitterStreamingAPISpider twitterStreamingAPI = new TwitterStreamingAPISpider();

		twitterStreamingAPI.setSearchID(twitterSearch.getSearchID());
		twitterStreamingAPI.setCache(cache);

		String[] keywordsArr = twitterSearch.getKeywords().split(" ");
		twitterStreamingAPI.setTrack(keywordsArr);

		if (this.languageCode != null && !this.languageCode.equals("")) {

			String[] languageCodeArr = this.languageCode.split(" ");
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

				// initialize SearchAPI
				logger.debug("Method createHistoricalSearch(): Initializing Historical Search");
				TwitterSearchAPISpider searchAPI = initializeSearchAPI();

				searchAPI.collectTweets();

				// historical search completed, loading = false;
				logger.debug("Method createHistoricalSearch(): Historical Search completed");

				twitterSearch.setLoading(false);

				cache.updateTwitterSearch(twitterSearch);

			}

		};

		twitterSearchThread.start();
	}

	private void startMonitoringResourcesThreads() {

		if (twitterSearch.getTwitterMonitorScheduler().getLinks() != null && !twitterSearch.getTwitterMonitorScheduler().getLinks().equals("")) {

			Thread bitlyAnalysisThread = new Thread() {
				@Override
				public void run() {

					BitlyCounterClicksUtility bitlyUtil = new BitlyCounterClicksUtility(twitterSearch.getTwitterMonitorScheduler().getLinks(),
							twitterSearch.getSearchID());

					bitlyUtil.startBitlyAnalysis();

				}
			};

			bitlyAnalysisThread.start();
		}

		if (twitterSearch.getTwitterMonitorScheduler().getAccounts() != null && !twitterSearch.getTwitterMonitorScheduler().getAccounts().equals("")) {

			Thread accountAnalysisThread = new Thread() {

				String accounts = twitterSearch.getTwitterMonitorScheduler().getAccounts();

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

		if (twitterSearch.getTwitterMonitorScheduler().getDocuments() != null && !twitterSearch.getTwitterMonitorScheduler().getDocuments().equals("")) {

			Thread documentAnalysisThread = new Thread() {

				String documents = twitterSearch.getTwitterMonitorScheduler().getDocuments();

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

	private void createHistoricalSearchTrigger(TwitterSearchScheduler tsScheduler) {

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();

			long searchID = this.twitterSearch.getSearchID();

			JobDetail hSearchJob = JobBuilder.newJob(HistoricalSearchJob.class).withIdentity("HSearchJob_" + searchID, "groupHSearch")
					.usingJobData("searchID", searchID).build();

			// scheduler parameters

			Calendar startingCalendar = tsScheduler.getStartingTime();
			Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

			int frequency = tsScheduler.getRepeatFrequency();
			SearchRepeatTypeEnum type = tsScheduler.getRepeatType();

			if (type == SearchRepeatTypeEnum.Day) {

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" + searchID, "groupHSearch").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.DAY)).build();

				// Tell quartz to schedule the job using our trigger
				sched.scheduleJob(hSearchJob, trigger);

			} else if (type == SearchRepeatTypeEnum.Hour) {

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" + searchID, "groupHSearch").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.HOUR))
						.build();

				// Tell quartz to schedule the job using our trigger
				sched.scheduleJob(hSearchJob, trigger);

			}

			tsScheduler.setStartingTime(startingCalendar);
			cache.updateTwitterSearchScheduler(tsScheduler);

		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void createMonitoringTriggerWithoutEndingDate(TwitterMonitorScheduler twitterMonitor) {

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();

			long searchID = this.twitterSearch.getSearchID();

			// Job details
			JobDetail hSearchJob = JobBuilder.newJob(MonitoringResourcesJob.class).withIdentity("MonitoringJob_" + searchID, "groupMonitoring")
					.usingJobData("searchID", searchID).build();

			int repeatFrequency = twitterMonitor.getRepeatFrequency();
			MonitorRepeatTypeEnum repeatType = twitterMonitor.getRepeatType();

			Calendar startingCalendar = GregorianCalendar.getInstance();

			startingCalendar.set(Calendar.SECOND, 0);
			startingCalendar.set(Calendar.MILLISECOND, 0);

			if (repeatType == MonitorRepeatTypeEnum.Day) {

				startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

				Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("MonitoringTgr_" + searchID, "groupMonitoring").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.DAY))
						.build();

				sched.scheduleJob(hSearchJob, trigger);

			} else if (repeatType == MonitorRepeatTypeEnum.Hour) {

				startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

				Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("MonitoringTgr_" + searchID, "groupMonitoring").startAt(startingDateJob)
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.HOUR))
						.build();

				sched.scheduleJob(hSearchJob, trigger);

			}

			twitterMonitor.setStartingTime(startingCalendar);
			cache.updateTwitterMonitorScheduler(twitterMonitor);

		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private void createMonitoringTriggerWithEndingDate(TwitterMonitorScheduler twitterMonitor) {

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();

			long searchID = this.twitterSearch.getSearchID();

			// Job details
			JobDetail hSearchJob = JobBuilder.newJob(MonitoringResourcesJob.class).withIdentity("MonitoringJob_" + searchID, "groupMonitoring")
					.usingJobData("searchID", searchID).build();

			int repeatFrequency = twitterMonitor.getRepeatFrequency();
			MonitorRepeatTypeEnum repeatType = twitterMonitor.getRepeatType();

			Calendar startingCalendar = GregorianCalendar.getInstance();

			startingCalendar.set(Calendar.SECOND, 0);
			startingCalendar.set(Calendar.MILLISECOND, 0);

			if (repeatType == MonitorRepeatTypeEnum.Day) {

				startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

				if (twitterMonitor.getEndingTime().compareTo(startingCalendar) > 0) {

					Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

					Date endingDateJob = new java.util.Date(twitterMonitor.getEndingTime().getTimeInMillis());

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

			} else if (repeatType == MonitorRepeatTypeEnum.Hour) {

				startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

				if (twitterMonitor.getEndingTime().compareTo(startingCalendar) > 0) {

					Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

					Date endingDateJob = new java.util.Date(twitterMonitor.getEndingTime().getTimeInMillis());

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

			twitterMonitor.setStartingTime(startingCalendar);
			cache.updateTwitterMonitorScheduler(twitterMonitor);

		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private void previousStreamAndMonitorManager() {
		TwitterSearch previousEnabledTwitterSearch = cache.isPresentEnabledStream();

		if (previousEnabledTwitterSearch != null) {

			TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

			twitterStream.clearListeners();
			twitterStream.cleanUp();

			previousEnabledTwitterSearch.setLoading(false);

			cache.updateTwitterSearch(previousEnabledTwitterSearch);

			TwitterMonitorScheduler previousTwitterMonitorScheduler = previousEnabledTwitterSearch.getTwitterMonitorScheduler();

			if (previousTwitterMonitorScheduler != null) {

				long lastActiveSearchID = previousEnabledTwitterSearch.getSearchID();

				Calendar endingCalendar = AnalysisUtility.setMonitorSchedulerEndingDate(previousTwitterMonitorScheduler);

				previousTwitterMonitorScheduler.setEndingTime(endingCalendar);

				cache.updateTwitterMonitorScheduler(previousTwitterMonitorScheduler);

				try {
					SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

					Scheduler scheduler = schedFact.getScheduler();

					logger.debug("Method stopStreamingSearch(): Unscheduling trigger MonitoringTgr_" + lastActiveSearchID);
					scheduler.unscheduleJob(TriggerKey.triggerKey("MonitoringTgr_" + lastActiveSearchID, "groupMonitoring"));

					logger.debug("Method createEnabledStreamingSearch(): Starting monitor resources for search: " + lastActiveSearchID);

					createMonitoringTriggerWithEndingDate(previousTwitterMonitorScheduler);

				} catch (SchedulerException e) {

					logger.debug("Method createEnabledStreamingSearch(): Error stopping scheduler for search " + lastActiveSearchID);
				}
			}

		}
	}
}
