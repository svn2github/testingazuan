package it.eng.spagobi.twitter.analysis.scheduler;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;

import javax.sql.rowset.CachedRowSet;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SearchTriggerRunner {

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	public void createTrigger(long searchID) throws SchedulerException {

		String sqlQuery = "SELECT * from twitter_search_scheduler where search_id = '" + searchID + "'";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {
					java.sql.Date startingDate = rs.getDate("starting_date");
					int repeatFrequency = rs.getInt("repeat_frequency");
					String repeatType = rs.getString("repeat_type");

					JobDetail job = JobBuilder.newJob(HistoricalSearchJob.class).withIdentity("dummyJobName", "group1").build();

					Trigger trigger = TriggerBuilder.newTrigger().withIdentity("dummyTriggerName", "group1")
							.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

					Scheduler scheduler = new StdSchedulerFactory().getScheduler();
					scheduler.start();
					scheduler.scheduleJob(job, trigger);
				}
			}
		} catch (Exception e) {
			System.out.println("**** connection failed: " + e);
		}
	}

}