package it.eng.qbe.action;

import java.util.List;
import java.util.Locale;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.SpagoBIInfo;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractAction;

import org.hibernate.SessionFactory;
import org.jboss.util.ThrowableHandler;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to instantiate the datamart where
 * 
 * working with qbe
 */
public class SelectDataMartAndInitNewWizardAction extends AbstractAction {

	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		RequestContainer requestContainer = getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		session.delAttribute("spagobi");
		
		String user = (String)request.getAttribute("SPAGOBI_USER");
		String spagobiurl = (String)request.getAttribute("SPAGOBI_URL");
		String templatePath = (String)request.getAttribute("SPAGOBI_PATH");
		String country = (String)request.getAttribute("SPAGOBI_COUNTRY");
		String language = (String)request.getAttribute("SPAGOBI_LANGUAGE");
		Locale spagobiLocale = null;
		if(language != null) {
			/*
			if(country == null || country.equalsIgnoreCase("")) {
				if(language.equalsIgnoreCase("it")) country = "IT";
				else if (language.equalsIgnoreCase("en")) country = "US";
			}
			*/
			spagobiLocale = new Locale(language, country);
		}
		if(user != null && spagobiurl != null && templatePath != null) {
			SpagoBIInfo spagoBIInfo = new SpagoBIInfo(templatePath, spagobiurl, user, spagobiLocale);		
			session.setAttribute("spagobi", spagoBIInfo);
		}
		
//		List allJndiDs = Utils.getAllJndiDS();
//		for(int i = 0; i < allJndiDs.size(); i++) {
//			Object o = allJndiDs.get(i);
//			System.out.println("JNDI connection: " + o.toString());
//		}
		
		String dataSourceName = (String)request.getAttribute("JNDI_DS");
		String jndiDataSourceName  = QbeConf.getInstance().getJndiConnectionName(dataSourceName);
		
		String dialect = (String)request.getAttribute("DIALECT");
		
		String dmPath = (String)request.getAttribute("PATH");
		
		DataMartModel dmModel = new DataMartModel(dmPath, jndiDataSourceName, dialect);
		
		ApplicationContainer application = ApplicationContainer.getInstance();
		
		ApplicationContainer.getInstance().setAttribute("CURRENT_THREAD_CONTEXT_LOADER", Thread.currentThread().getContextClassLoader());
		
		dmModel.setName(dmModel.getPath());
		dmModel.setDescription(dmModel.getPath());
		
		
		
		
		SessionFactory sf = dmModel.createSessionFactory();
		Logger.debug(Utils.class, "getSessionFactory: session factory created: " + sf);
	    ApplicationContainer.getInstance().setAttribute(dmModel.getPath(), sf);
		Logger.debug(Utils.class, "getSessionFactory: session factory stored into application context: " + sf);
		
		
		if (getRequestContainer().getSessionContainer().getAttribute("dataMartModel") != null){
			getRequestContainer().getSessionContainer().delAttribute("dataMartModel");
		}
        getRequestContainer().getSessionContainer().setAttribute("dataMartModel", dmModel);
        
        RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		if (aWizardObject != null){
			aSessionContainer.delAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		}
		
		
		String queryId = (String)request.getAttribute("queryId");
		
		DataMartModel dataMart = null;
		if ((queryId == null) || ((queryId != null) && (queryId.equalsIgnoreCase("#")))){
			aWizardObject = new SingleDataMartWizardObjectSourceBeanImpl();
		} else {
			dataMart = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
			aWizardObject = dataMart.getQuery(queryId);
		}
		
		
		
		
		
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
		
		
        
	}
}
