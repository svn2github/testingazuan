/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.commons.initializers.metadata;

import it.eng.spago.base.SourceBean;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.behaviouralmodel.check.metadata.SbiChecks;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.engines.config.metadata.SbiEngines;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.xml.sax.InputSource;

/**
 * @author Zerbetto (davide.zerbetto@eng.it)
 * 
 * This class initializes SpagoBI metadata repository, if it is empty, with predefined domains, checks, lovs, engines, user functionalities...
**/
public class MetadataInitializer extends AbstractHibernateDAO implements InitializerIFace {

	static private Logger logger = Logger.getLogger(MetadataInitializer.class);
	
	public SourceBean getConfig() {
		return null;
	}

	public void init(SourceBean config) {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = this.getSession();
			String hql = "from SbiDomains";
			Query hqlQuery = aSession.createQuery(hql);
			List domains = hqlQuery.list();
			if (domains.isEmpty()) {
				logger.info("Domains table is empty. Starting populating domains...");
				writeDomains(aSession);
			} else {
				logger.debug("Domains table is already populated");
			}
			
			hql = "from SbiEngines";
			hqlQuery = aSession.createQuery(hql);
			List engines = hqlQuery.list();
			if (engines.isEmpty()) {
				logger.info("Engines table is empty. Starting populating predefined engines...");
				writeEngines(aSession);
			} else {
				logger.debug("Engines table is already populated");
			}
			
			hql = "from SbiChecks";
			hqlQuery = aSession.createQuery(hql);
			List checks = hqlQuery.list();
			if (checks.isEmpty()) {
				logger.info("Checks table is empty. Starting populating predefined checks...");
				writeChecks(aSession);
			} else {
				logger.debug("Checks table is already populated");
			}
			
		} catch (Exception e) {
			logger.error("Error while initializing metadata", e);
			if (tx != null)
				tx.rollback();
		} finally {
			if (aSession != null){
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}

	private SourceBean getConfiguration(String resource) throws Exception {
		logger.debug("IN");
		InputStream is = null;
		SourceBean toReturn = null;
		try {
			Thread curThread = Thread.currentThread();
	        ClassLoader classLoad = curThread.getContextClassLoader();
	        is = classLoad.getResourceAsStream(resource);
	        InputSource source = new InputSource(is);
			toReturn = SourceBean.fromXMLStream(source);
			logger.debug("Configuration successfully read from resource " + resource);
		} catch (Exception e) {
			logger.error("Error while reading configuration from resource " + resource, e);
			throw e;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e);
				}
			logger.debug("OUT");
		}
        return toReturn;
	}
	
	private void writeDomains(Session aSession) throws Exception {
		logger.debug("IN");
		SourceBean domainsSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/domains.xml");
		if (domainsSB == null) {
			throw new Exception("Domains configuration file not found!!!");
		}
		List domainsList = domainsSB.getAttributeAsList("DOMAIN");
		if (domainsList == null || domainsList.isEmpty()) {
			throw new Exception("No predefined domains found!!!");
		}
		Iterator it = domainsList.iterator();
		while (it.hasNext()) {
			SourceBean aDomainSB = (SourceBean) it.next();
			SbiDomains aDomain = new SbiDomains();
			aDomain.setDomainCd((String) aDomainSB.getAttribute("domainCd"));
			aDomain.setDomainNm((String) aDomainSB.getAttribute("domainNm"));
			aDomain.setValueCd((String) aDomainSB.getAttribute("valueCd"));
			aDomain.setValueNm((String) aDomainSB.getAttribute("valueNm"));
			aDomain.setValueDs((String) aDomainSB.getAttribute("valueDs"));
			aSession.save(aDomain);
		}
		logger.debug("OUT");
	}
	
	private void writeEngines(Session aSession) throws Exception {
		logger.debug("IN");
		SourceBean enginesSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/engines.xml");
		if (enginesSB == null) {
			logger.info("Configuration file for predefined engines not found");
			return;
		}
		List enginesList = enginesSB.getAttributeAsList("ENGINE");
		if (enginesList == null || enginesList.isEmpty()) {
			logger.info("No predefined engines avilable from configuration file");
			return;
		}
		Iterator it = enginesList.iterator();
		while (it.hasNext()) {
			SourceBean anEngineSB = (SourceBean) it.next();
			SbiEngines anEngine = new SbiEngines();
			anEngine.setName((String) anEngineSB.getAttribute("name"));
			anEngine.setDescr((String) anEngineSB.getAttribute("descr"));
			anEngine.setMainUrl((String) anEngineSB.getAttribute("mainUrl"));
			anEngine.setDriverNm((String) anEngineSB.getAttribute("driverNm"));
			anEngine.setLabel((String) anEngineSB.getAttribute("label"));
			
			String engineTypeCd = (String) anEngineSB.getAttribute("engineTypeCd");
			String hql = "from SbiDomains where valueCd = ? and domainCd='ENGINE_TYPE'";
			Query hqlQuery = aSession.createQuery(hql);
			hqlQuery.setParameter(1, engineTypeCd);
			SbiDomains domainEngineType = (SbiDomains) hqlQuery.uniqueResult();
			anEngine.setEngineType(domainEngineType);
			anEngine.setClassNm((String) anEngineSB.getAttribute("classNm"));
			
			String biobjTypeCd = (String) anEngineSB.getAttribute("biobjTypeCd");
			hql = "from SbiDomains where valueCd = ? and domainCd='BIOBJ_TYPE'";
			hqlQuery = aSession.createQuery(hql);
			hqlQuery.setParameter(1, biobjTypeCd);
			SbiDomains domainBiobjectType = (SbiDomains) hqlQuery.uniqueResult();
			anEngine.setBiobjType(domainBiobjectType);
			
			anEngine.setUseDataSet(new Boolean((String) anEngineSB.getAttribute("useDataSet")));
			anEngine.setUseDataSource(new Boolean((String) anEngineSB.getAttribute("useDataSource")));
			anEngine.setEncrypt(new Short((String) anEngineSB.getAttribute("encrypt")));
			anEngine.setObjUplDir((String) anEngineSB.getAttribute("objUplDir"));
			anEngine.setObjUseDir((String) anEngineSB.getAttribute("objUseDir"));
			anEngine.setSecnUrl((String) anEngineSB.getAttribute("secnUrl"));
			
			aSession.save(anEngine);
		}
		logger.debug("OUT");
	}
	
	private void writeChecks(Session aSession) throws Exception {
		logger.debug("IN");
		SourceBean checksSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/checks.xml");
		if (checksSB == null) {
			logger.info("Configuration file for predefined checks not found");
			return;
		}
		List checksList = checksSB.getAttributeAsList("CHECK");
		if (checksList == null || checksList.isEmpty()) {
			logger.info("No predefined checks avilable from configuration file");
			return;
		}
		Iterator it = checksList.iterator();
		while (it.hasNext()) {
			SourceBean aChecksSB = (SourceBean) it.next();
			SbiChecks aCheck = new SbiChecks();
			aCheck.setLabel((String) aChecksSB.getAttribute("label"));
			aCheck.setName((String) aChecksSB.getAttribute("name"));
			aCheck.setDescr((String) aChecksSB.getAttribute("descr"));
			
			String valueTypeCd = (String) aChecksSB.getAttribute("valueTypeCd");
			String hql = "from SbiDomains where valueCd = ? and domainCd='PRED_CHECK'";
			Query hqlQuery = aSession.createQuery(hql);
			hqlQuery.setParameter(1, valueTypeCd);
			SbiDomains domainValueTypeCd = (SbiDomains) hqlQuery.uniqueResult();
			aCheck.setCheckType(domainValueTypeCd);
			aCheck.setValue1((String) aChecksSB.getAttribute("value1"));
			aCheck.setValue2((String) aChecksSB.getAttribute("value2"));
			
			aSession.save(aCheck);
		}
		logger.debug("OUT");
	}
	
}
