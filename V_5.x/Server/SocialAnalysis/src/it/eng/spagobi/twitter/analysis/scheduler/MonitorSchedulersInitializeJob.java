package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheImpl;
import it.eng.spagobi.twitter.analysis.entities.TwitterMonitorScheduler;

import java.util.Calendar;
import java.util.Date;
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

public class MonitorSchedulersInitializeJob implements Job {

	static final Logger logger = Logger.getLogger(MonitorSchedulersInitializeJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheImpl();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobKey key = context.getJobDetail().getKey();

		try {

			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();

			List<TwitterMonitorScheduler> monitorSchedulers = twitterCache.getAllMonitorSchedulers();

			for (TwitterMonitorScheduler monitorScheduler : monitorSchedulers) {
				long searchID = monitorScheduler.getTwitterSearch().getSearchID();
				boolean activeSearch = monitorScheduler.isActiveSearch();

				JobDetail hSearchJob = JobBuilder.newJob(MonitoringResourcesJob.class).withIdentity("MonitoringJob_" + searchID, "groupMonitoring")
						.usingJobData("searchID", searchID).build();

				int repeatFrequency = monitorScheduler.getRepeatFrequency();
				String repeatType = monitorScheduler.getRepeatType().toString();

				Calendar startingCalendar = monitorScheduler.getStartingTime();

				if (activeSearch) {

					// search attiva, monitor senza end

					if (repeatType.equalsIgnoreCase("Day")) {

						// startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

						Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

						Trigger trigger = TriggerBuilder
								.newTrigger()
								.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
								.startAt(startingDateJob)
								.withSchedule(
										CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.DAY))
								.build();

						sched.scheduleJob(hSearchJob, trigger);
					} else if (repeatType.equalsIgnoreCase("Hour")) {

						// startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

						Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

						Trigger trigger = TriggerBuilder
								.newTrigger()
								.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
								.startAt(startingDateJob)
								.withSchedule(
										CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.HOUR))
								.build();

						sched.scheduleJob(hSearchJob, trigger);

					}

				} else {
					// search non attiva,
					// monitor con end

					java.util.Date endingDateJob = new Date();

					Calendar endingCalendar = monitorScheduler.getEndingTime();
					endingDateJob = new java.util.Date(endingCalendar.getTimeInMillis());

					if (repeatType.equalsIgnoreCase("Day")) {

						// startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

						if (endingCalendar.compareTo(startingCalendar) > 0) {

							Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

							Trigger trigger = TriggerBuilder
									.newTrigger()
									.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
									.startAt(startingDateJob)
									.endAt(endingDateJob)
									.withSchedule(
											CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency,
													DateBuilder.IntervalUnit.DAY)).build();

							sched.scheduleJob(hSearchJob, trigger);
						}
					} else if (repeatType.equalsIgnoreCase("Hour")) {

						// startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

						if (endingCalendar.compareTo(startingCalendar) > 0) {

							Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

							Trigger trigger = TriggerBuilder
									.newTrigger()
									.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
									.startAt(startingDateJob)
									.endAt(endingDateJob)
									.withSchedule(
											CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency,
													DateBuilder.IntervalUnit.HOUR)).build();

							sched.scheduleJob(hSearchJob, trigger);
						}

					}
				}

				logger.debug("Instance " + key + " of MonitoringJob. Executing on search #: " + searchID);
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
