package it.eng.spagobi.workflow;

import it.eng.spago.base.SourceBean;
import it.eng.spago.init.InitializerIFace;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jbpm.JbpmConfiguration;
import org.jbpm.persistence.db.DbPersistenceServiceFactory;
import org.jbpm.svc.Services;


public class JbpmContextInitializer implements InitializerIFace {
	
	/** 
	 * SourceBean that contains the configuration parameters
	 */
	private SourceBean _config = null;

	
	public void init(SourceBean config) {
		JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
		DbPersistenceServiceFactory dbpsf = (DbPersistenceServiceFactory)jbpmConfiguration.getServiceFactory(Services.SERVICENAME_PERSISTENCE);
		try{
			SessionFactory sessionFactHib = dbpsf.getSessionFactory();
			Session sessionHib = sessionFactHib.openSession();
			Query hibQuery = sessionHib.createQuery(" from ProcessDefinition");
			List hibList = hibQuery.list();			
		} catch (HibernateException he) {
			jbpmConfiguration.createSchema();
		} 
	}
	
	public SourceBean getConfig() {
		return _config;
	}

	
}
