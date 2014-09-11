package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

public class SearchSchedulersInitializeJob implements Job {

	static final Logger logger = Logger.getLogger(SearchSchedulersInitializeJob.class);
	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobKey key = context.getJobDetail().getKey();

		String sqlQuery = "SELECT * from twitter_search_scheduler";

		try {
			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

				Scheduler sched = schedFact.getScheduler();

				sched.start();

				while (rs.next()) {

					boolean active = rs.getBoolean("active");

					if (active) {
						String searchID = rs.getString("search_id");
						java.sql.Timestamp startingDate = rs.getTimestamp("starting_date");
						int frequency = rs.getInt("repeat_frequency");
						String type = rs.getString("repeat_type");

						JobDetail hSearchJob = JobBuilder.newJob(HistoricalSearchJob.class).withIdentity("HSearchJob_" + searchID, "groupHSearch")
								.usingJobData("searchID", searchID).build();

						Calendar startingCalendar = GregorianCalendar.getInstance();
						startingCalendar.setTime(startingDate);

						java.util.Date startingDateJob = new java.util.Date(startingCalendar.getTimeInMillis());

						if (type.equalsIgnoreCase("day")) {

							Trigger trigger = TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" + searchID, "groupHSearch").startAt(startingDateJob)
									.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.DAY)).build();

							// Tell quartz to schedule the job using our trigger
							sched.scheduleJob(hSearchJob, trigger);

						} else if (type.equalsIgnoreCase("hour")) {

							Trigger trigger = TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" + searchID, "groupHSearch").startAt(startingDateJob)
									.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(frequency, DateBuilder.IntervalUnit.HOUR)).build();

							// Tell quartz to schedule the job using our trigger
							sched.scheduleJob(hSearchJob, trigger);

						}

						logger.debug("Instance " + key + " of HistoricalSearchJob. Executing search #: " + searchID);
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
