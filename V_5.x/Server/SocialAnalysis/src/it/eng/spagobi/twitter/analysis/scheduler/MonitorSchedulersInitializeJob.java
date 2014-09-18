package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

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
	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobKey key = context.getJobDetail().getKey();

		try {

			String searchSchedulerSQL = "SELECT search_id, active FROM twitter_search_scheduler";

			List<Integer> activeSearchScheduler = new ArrayList<Integer>();

			CachedRowSet sSchedulerRS = twitterCache.runQuery(searchSchedulerSQL);

			if (sSchedulerRS != null) {
				while (sSchedulerRS.next()) {
					boolean activeScheduler = sSchedulerRS.getBoolean("active");

					if (activeScheduler) {
						activeSearchScheduler.add(sSchedulerRS.getInt("search_id"));
					}
				}
			}

			String sqlQuery = "SELECT * from twitter_monitor_scheduler";

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

				Scheduler sched = schedFact.getScheduler();

				sched.start();

				while (rs.next()) {

					boolean active = rs.getBoolean("active");

					if (active) {

						String searchID = rs.getString("search_id");

						JobDetail hSearchJob = JobBuilder.newJob(MonitoringResourcesJob.class).withIdentity("MonitoringJob_" + searchID, "groupMonitoring")
								.usingJobData("searchID", searchID).build();

						int repeatFrequency = rs.getInt("repeat_frequency");
						String repeatType = rs.getString("repeat_type");

						Calendar startingCalendar = GregorianCalendar.getInstance();

						if (activeSearchScheduler.contains(Integer.parseInt(searchID))) {
							// search scheduler attivo, monitor senza end

							if (repeatType.equalsIgnoreCase("Day")) {

								startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

								Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

								Trigger trigger = TriggerBuilder
										.newTrigger()
										.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
										.startAt(startingDateJob)
										.withSchedule(
												CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency,
														DateBuilder.IntervalUnit.DAY)).build();

								sched.scheduleJob(hSearchJob, trigger);
							} else if (repeatType.equalsIgnoreCase("Hour")) {

								startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

								Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

								Trigger trigger = TriggerBuilder
										.newTrigger()
										.withIdentity("MonitoringTgr_" + searchID, "groupMonitoring")
										.startAt(startingDateJob)
										.withSchedule(
												CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency,
														DateBuilder.IntervalUnit.HOUR)).build();

								sched.scheduleJob(hSearchJob, trigger);

							}

						} else {
							// search scheduler non presente oppure non attivo,
							// monitor con end

							java.sql.Timestamp endingDate = rs.getTimestamp("ending_date");
							java.util.Date endingDateJob = new Date();

							Calendar endingCalendar = GregorianCalendar.getInstance();
							endingCalendar.setTime(endingDate);
							endingDateJob = new java.util.Date(endingCalendar.getTimeInMillis());

							if (repeatType.equalsIgnoreCase("Day")) {

								startingCalendar.add(Calendar.DAY_OF_MONTH, repeatFrequency);

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

								startingCalendar.add(Calendar.HOUR_OF_DAY, repeatFrequency);

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
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

}
