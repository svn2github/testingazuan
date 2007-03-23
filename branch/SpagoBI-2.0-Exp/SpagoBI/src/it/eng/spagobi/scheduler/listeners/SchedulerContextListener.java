package it.eng.spagobi.scheduler.listeners;

import it.eng.spagobi.utilities.SpagoBITracer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		SpagoBITracer.debug("SPAGOBI", this.getClass().getName(), "contextDestroyed", 
				"Entering method ...");
		// shutdown Quartz scheduler
		Scheduler scheduler = null;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			SpagoBITracer.critical("SPAGOBI", this.getClass().getName(), "contextDestroyed", 
					"Error while retrieving scheduler", e);
		}
		if (scheduler != null) {
			try {
				scheduler.shutdown();
			} catch (SchedulerException e) {
				SpagoBITracer.critical("SPAGOBI", this.getClass().getName(), "contextDestroyed", 
						"Error while stopping scheduler", e);
			}
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// does nothing
	}

}
