package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;

import java.sql.SQLException;

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

		String sqlQuery = "SELECT * from twitter_monitor_scheduler";

		try {
			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					String searchID = rs.getString("search_id");
					java.sql.Date endingDate = rs.getDate("ending_date");
					int repeatFrequency = rs.getInt("repeat_frequency");
					String repeatType = rs.getString("repeat_type");

					SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

					Scheduler sched = schedFact.getScheduler();

					sched.start();

					JobDetail hSearchJob = JobBuilder.newJob(MonitoringResourcesJob.class).withIdentity("MonitorJob_" + searchID, "groupMonitor")
							.usingJobData("searchID", searchID).build();

					Trigger trigger = TriggerBuilder.newTrigger().withIdentity("MonitorTgr_" + searchID, "groupMonitor").startNow().endAt(endingDate)
							.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency, DateBuilder.IntervalUnit.DAY)).build();

					// Trigger trigger =
					// TriggerBuilder.newTrigger().withIdentity("HSearchTgr_" +
					// searchID, "groupHSearch").startNow()
					// .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(repeatFrequency,
					// DateBuilder.IntervalUnit.SECOND)).build();

					// Tell quartz to schedule the job using our trigger
					sched.scheduleJob(hSearchJob, trigger);

					logger.debug("Instance " + key + " of HistoricalSearchJob. Executing search #: " + searchID);
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} catch (SchedulerException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

}
