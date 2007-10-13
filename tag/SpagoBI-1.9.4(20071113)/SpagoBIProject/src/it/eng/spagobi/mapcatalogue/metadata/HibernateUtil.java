package it.eng.spagobi.mapcatalogue.metadata;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.SpagoBITracer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static Log log = LogFactory.getLog(HibernateUtil.class);

	private static final SessionFactory sessionFactory;
	static {
		try {
			// Create the SessionFactory
			SourceBean fileCfgSb = ((SourceBean)ConfigSingleton.getInstance().getAttribute("GEOENGINE.HIBERNATE-CFGFILE"));
			
			if (fileCfgSb != null){
				String fileCfg = fileCfgSb.getCharacters();
				fileCfg = fileCfg.trim();
				SpagoBITracer.info("HibernateUtil",HibernateUtil.class.getName(), "static declaration ", "Initializing hibernate Session Factory Described by [" + fileCfg +"]");
				Configuration conf = new Configuration();
				conf = conf.configure(fileCfg);
				sessionFactory = conf.buildSessionFactory();
				
				//sessionFactory = new Configuration().configure(fileCfg).buildSessionFactory();
			}else{
				SpagoBITracer.info("HibernateUtil",HibernateUtil.class.getName(), "static declaration ", "Initializing hibernate Session Factory with default configuration [hibernate.cfg.xml]");
				sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			}
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			log.error("Initial SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	

	public static Session currentSession() {
		return sessionFactory.openSession();
	}

	
}

