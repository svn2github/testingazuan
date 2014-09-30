package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheImpl;
import it.eng.spagobi.twitter.analysis.entities.TwitterSearchScheduler;
import it.eng.spagobi.twitter.analysis.enums.SearchRepeatTypeEnum;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class SearchSchedulersInitializeJob implements Job {

	static final Logger logger = Logger.getLogger(SearchSchedulersInitializeJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheImpl();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobKey key = context.getJobDetail().getKey();

		List<TwitterSearchScheduler> searchSchedulers = twitterCache.getAllActiveSearchSchedulers();

		if (searchSchedulers != null && searchSchedulers.size() > 0) {

			try {
				SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

				Scheduler sched = schedFact.getScheduler();

				sched.start();

				for (TwitterSearchScheduler searchScheduler : searchSchedulers) {
					long searchID = searchScheduler.getTwitterSearch().getSearchID();
					Calendar startingCalendar = searchScheduler.getStartingTime();
					int frequency = searchScheduler.getRepeatFrequency();
					SearchRepeatTypeEnum type = searchScheduler.getRepeatType();

					JobDetail hSearchJob = JobBuilder.newJob(HistoricalSearchJob.class).withIdentity("HSearchJob_" + searchID, "groupHSearch")
							.usingJobData("searchID", searchID).build();

					java.util.Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

					if (type.toString().equalsIgnoreCase(SearchRepeatTypeEnum.Day.toString())) {

						Trigger trigger = TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" + searchID, "groupHSearch").startAt(startingDateJob)
								.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.DAY))
								.build();

						// Tell quartz to schedule the job using our trigger
						sched.scheduleJob(hSearchJob, trigger);

					} else if (type.toString().equalsIgnoreCase(SearchRepeatTypeEnum.Hour.toString())) {

						Trigger trigger = TriggerBuilder
								.newTrigger()
								.withIdentity("HSearchTgr_" + searchID, "groupHSearch")
								.startAt(startingDateJob)
								.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.HOUR))
								.build();

						// Tell quartz to schedule the job using our trigger
						sched.scheduleJob(hSearchJob, trigger);

					}

					logger.debug("Instance " + key + " of HistoricalSearchJob. Executing search #: " + searchID);
				}
			} catch (SchedulerException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

	}

}
