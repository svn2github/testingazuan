/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import it.eng.spagobi.engines.birt.utilities.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

public class BirtEngine {

	private static IReportEngine birtEngine = null;

	private static Properties configProps = new Properties();

	private final static String configFile = "BirtLogConfig.properties";

	protected static Logger logger = Logger.getLogger(BirtEngine.class);
	
	public static synchronized void initBirtConfig() {
		loadEngineProps();
	}

	public static synchronized IReportEngine getBirtEngine(ServletContext sc) {
		if (birtEngine == null) {
			EngineConfig config = new EngineConfig();
			if (configProps != null && !configProps.isEmpty()) {
				String logLevel = configProps.getProperty("logLevel");
				Level level = Level.OFF;
				if ("SEVERE".equalsIgnoreCase(logLevel)) {
					level = Level.SEVERE;
				} else if ("WARNING".equalsIgnoreCase(logLevel)) {
					level = Level.WARNING;
				} else if ("INFO".equalsIgnoreCase(logLevel)) {
					level = Level.INFO;
				} else if ("CONFIG".equalsIgnoreCase(logLevel)) {
					level = Level.CONFIG;
				} else if ("FINE".equalsIgnoreCase(logLevel)) {
					level = Level.FINE;
				} else if ("FINER".equalsIgnoreCase(logLevel)) {
					level = Level.FINER;
				} else if ("FINEST".equalsIgnoreCase(logLevel)) {
					level = Level.FINEST;
				} else if ("OFF".equalsIgnoreCase(logLevel)) {
					level = Level.OFF;
				}

				String logDir = configProps.getProperty("logDirectory");
				logDir = Utils.resolveSystemProperties(logDir);
				logger.debug("Log config: logDirectory = [" + logDir + "]; level = [" + level + "]");
				config.setLogConfig(logDir, level);
			}

			config.setEngineHome("");
			IPlatformContext context = new PlatformServletContext(sc);
			config.setPlatformContext(context);

			try {
				Platform.startup(config);
				logger.debug("Birt Platform started");
			} catch (BirtException e) {
				logger.error("Error during Birt Platform start-up", e);
			}

			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			birtEngine = factory.createReportEngine(config);
			logger.debug("Report engine created");

		}
		return birtEngine;
	}

	public static synchronized void destroyBirtEngine() {
		if (birtEngine == null) {
			return;
		}
		//birtEngine.shutdown();
		Platform.shutdown();
		birtEngine = null;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private static void loadEngineProps() {
		InputStream in = null;
		try {
			// Config File must be in classpath
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			in = cl.getResourceAsStream(configFile);
			configProps.load(in);
		} catch (IOException e) {
			logger.error("Error during configFile properties file [" + configFile + "]", e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Error during closing input stream", e);
				}
		}
	}
	
}
