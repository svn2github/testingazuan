/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.action;

import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.datasource.HibernateDataSource;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
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
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import org.hibernate.SessionFactory;


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
		String mapCatalogueManagerUrl = (String)request.getAttribute("MAP_CATALOGUE_MANAGER_URL");
		String templatePath = (String)request.getAttribute("SPAGOBI_PATH");
		String country = (String)request.getAttribute("SPAGOBI_COUNTRY");
		String language = (String)request.getAttribute("SPAGOBI_LANGUAGE");
		
		Locale spagobiLocale = null;
		if(language != null) {
			spagobiLocale = new Locale(language, country);
		}
		if(user != null && spagobiurl != null && templatePath != null) {
			SpagoBIInfo spagoBIInfo = new SpagoBIInfo(templatePath, spagobiurl, user, spagobiLocale);		
			session.setAttribute("spagobi", spagoBIInfo);
			MapCatalogueAccessUtils mapCatalogueAccessUtils = new MapCatalogueAccessUtils(mapCatalogueManagerUrl);
			session.setAttribute("MAP_CATALOGUE_CLIENT", mapCatalogueAccessUtils);
			session.setAttribute("MAP_CATALOGUE_CLIENT_URL", mapCatalogueManagerUrl);
		}
		
		
		String dataSourceName = (String)request.getAttribute("JNDI_DS");
		String jndiDataSourceName  = QbeConf.getInstance().getJndiConnectionName(dataSourceName);		
		String dialect = (String)request.getAttribute("DIALECT");		
		String dmPath = (String)request.getAttribute("PATH");
		String propertiesStr = (String)request.getAttribute("DATAMART_PROPERTIES");
		Properties properties = null;
		if(propertiesStr != null) {
			properties = new Properties();
			properties.load(new ByteArrayInputStream(propertiesStr.getBytes()));
		}
		
		String modalityStr = (String)request.getAttribute("MODALITY");
		SourceBean modalitySB = null;
		if(modalityStr != null) modalitySB = SourceBean.fromXMLString(modalityStr);
		
		
		ApplicationContainer applicationContainer = ApplicationContainer.getInstance();
		
		//IDataSource dataSource = new HibernateDataSource(dmPath, jndiDataSourceName, dialect);
		
		IDataSource dataSource = (IDataSource) applicationContainer.getAttribute(dmPath + "_dataSource");
		if (dataSource == null) {
			dataSource = new HibernateDataSource(dmPath, jndiDataSourceName, dialect);
			((HibernateDataSource)dataSource).setName(dataSourceName);
			applicationContainer.setAttribute(dmPath + "_dataSource", dataSource);
		}

		
		
		
		DataMartModel dmModel = new DataMartModel(dataSource);
		if(properties != null) dmModel.setDataMartProperties(properties); 
		if(modalitySB != null) dmModel.setDataMartModelAccessModality(new DataMartModelAccessModality(modalitySB));
		
		
		
		
		ApplicationContainer.getInstance().setAttribute("CURRENT_THREAD_CONTEXT_LOADER", Thread.currentThread().getContextClassLoader());
		
		dmModel.setName(dmPath);
		dmModel.setDescription(dmPath);
		
		
		
		
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
