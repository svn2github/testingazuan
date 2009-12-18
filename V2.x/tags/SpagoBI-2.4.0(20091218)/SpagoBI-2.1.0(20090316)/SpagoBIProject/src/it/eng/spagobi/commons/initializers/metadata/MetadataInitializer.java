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
import it.eng.spagobi.behaviouralmodel.lov.metadata.SbiLov;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiUserFunctionality;
import it.eng.spagobi.engines.config.metadata.SbiEngines;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
			tx = aSession.beginTransaction();
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
			
			hql = "from SbiLov";
			hqlQuery = aSession.createQuery(hql);
			List lovs = hqlQuery.list();
			if (lovs.isEmpty()) {
				logger.info("Lovs table is empty. Starting populating predefined lovs...");
				writeLovs(aSession);
			} else {
				logger.debug("Lovs table is already populated");
			}
			
			hql = "from SbiUserFunctionality";
			hqlQuery = aSession.createQuery(hql);
			List userFunctionalities = hqlQuery.list();
			if (userFunctionalities.isEmpty()) {
				logger.info("User functionality table is empty. Starting populating predefined User functionalities...");
				writeUserFunctionalities(aSession);
			} else {
				logger.debug("User functionality table is already populated");
			}
			
			tx.commit();
			
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
		SourceBean domainsSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/config/domains.xml");
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
			logger.debug("Inserting Domain with valueCd = [" + aDomainSB.getAttribute("valueCd") + "], domainCd = [" + aDomainSB.getAttribute("domainCd") + "] ...");
			aSession.save(aDomain);
		}
		logger.debug("OUT");
	}
	
	private void writeEngines(Session aSession) throws Exception {
		logger.debug("IN");
		SourceBean enginesSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/config/engines.xml");
		if (enginesSB == null) {
			logger.info("Configuration file for predefined engines not found");
			return;
		}
		List enginesList = enginesSB.getAttributeAsList("ENGINE");
		if (enginesList == null || enginesList.isEmpty()) {
			logger.info("No predefined engines available from configuration file");
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
			anEngine.setClassNm((String) anEngineSB.getAttribute("classNm"));
			anEngine.setUseDataSet(new Boolean((String) anEngineSB.getAttribute("useDataSet")));
			anEngine.setUseDataSource(new Boolean((String) anEngineSB.getAttribute("useDataSource")));
			anEngine.setEncrypt(new Short((String) anEngineSB.getAttribute("encrypt")));
			anEngine.setObjUplDir((String) anEngineSB.getAttribute("objUplDir"));
			anEngine.setObjUseDir((String) anEngineSB.getAttribute("objUseDir"));
			anEngine.setSecnUrl((String) anEngineSB.getAttribute("secnUrl"));
			
			String engineTypeCd = (String) anEngineSB.getAttribute("engineTypeCd");
			SbiDomains domainEngineType = findDomain(aSession, engineTypeCd, "ENGINE_TYPE");
			anEngine.setEngineType(domainEngineType);
			
			String biobjTypeCd = (String) anEngineSB.getAttribute("biobjTypeCd");
			SbiDomains domainBiobjectType = findDomain(aSession, biobjTypeCd, "BIOBJ_TYPE");
			anEngine.setBiobjType(domainBiobjectType);

			logger.debug("Inserting Engine with label = [" + anEngineSB.getAttribute("label") + "] ...");
			
			aSession.save(anEngine);
		}
		logger.debug("OUT");
	}
	
	private void writeChecks(Session aSession) throws Exception {
		logger.debug("IN");
		SourceBean checksSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/config/checks.xml");
		if (checksSB == null) {
			logger.info("Configuration file for predefined checks not found");
			return;
		}
		List checksList = checksSB.getAttributeAsList("CHECK");
		if (checksList == null || checksList.isEmpty()) {
			logger.info("No predefined checks available from configuration file");
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
			SbiDomains domainValueType = findDomain(aSession, valueTypeCd, "PRED_CHECK");
			aCheck.setCheckType(domainValueType);
			aCheck.setValueTypeCd(valueTypeCd);
			
			aCheck.setValue1((String) aChecksSB.getAttribute("value1"));
			aCheck.setValue2((String) aChecksSB.getAttribute("value2"));
			
			logger.debug("Inserting Check with label = [" + aChecksSB.getAttribute("label") + "] ...");
			
			aSession.save(aCheck);
		}
		logger.debug("OUT");
	}
	
	private void writeLovs(Session aSession) throws Exception {
		logger.debug("IN");
		SourceBean lovsSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/config/lovs.xml");
		if (lovsSB == null) {
			logger.info("Configuration file for predefined lovs not found");
			return;
		}
		List lovsList = lovsSB.getAttributeAsList("LOV");
		if (lovsList == null || lovsList.isEmpty()) {
			logger.info("No predefined lovs available from configuration file");
			return;
		}
		Iterator it = lovsList.iterator();
		while (it.hasNext()) {
			SourceBean aLovsSB = (SourceBean) it.next();
			SbiLov aLov = new SbiLov();
			aLov.setLabel((String) aLovsSB.getAttribute("label"));
			aLov.setName((String) aLovsSB.getAttribute("name"));
			aLov.setDescr((String) aLovsSB.getAttribute("descr"));
			aLov.setDefaultVal((String) aLovsSB.getAttribute("defaultVal"));
			aLov.setProfileAttr((String) aLovsSB.getAttribute("profileAttr"));
			
			SourceBean lovProviderSB = (SourceBean) aLovsSB.getAttribute("LOV_PROVIDER");
			aLov.setLovProvider(lovProviderSB.getCharacters());
			
			String inputTypeCd = (String) aLovsSB.getAttribute("inputTypeCd");
			SbiDomains domainInputType = findDomain(aSession, inputTypeCd, "INPUT_TYPE");
			aLov.setInputType(domainInputType);
			aLov.setInputTypeCd(inputTypeCd);
			
			logger.debug("Inserting Lov with label = [" + aLovsSB.getAttribute("label") + "] ...");
			
			aSession.save(aLov);
		}
		logger.debug("OUT");
	}
	
	private void writeUserFunctionalities(Session aSession) throws Exception {
		logger.debug("IN");
		SourceBean userFunctionalitiesSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/config/userFunctionalities.xml");
		SourceBean roleTypeUserFunctionalitiesSB = getConfiguration("it/eng/spagobi/commons/initializers/metadata/config/roleTypeUserFunctionalities.xml");
		if (userFunctionalitiesSB == null) {
			throw new Exception("User functionalities configuration file not found!!!");
		}
		if (roleTypeUserFunctionalitiesSB == null) {
			throw new Exception("Role type user functionalities configuration file not found!!!");
		}
		List userFunctionalitiesList = userFunctionalitiesSB.getAttributeAsList("USER_FUNCTIONALITY");
		if (userFunctionalitiesList == null || userFunctionalitiesList.isEmpty()) {
			throw new Exception("No predefined user functionalities found!!!");
		}
		Iterator it = userFunctionalitiesList.iterator();
		while (it.hasNext()) {
			SourceBean aUSerFunctionalitySB = (SourceBean) it.next();
			SbiUserFunctionality aUserFunctionality = new SbiUserFunctionality();
			String userFunctionality = (String) aUSerFunctionalitySB.getAttribute("name");
			aUserFunctionality.setName(userFunctionality);
			aUserFunctionality.setDescription((String) aUSerFunctionalitySB.getAttribute("description"));
			Object roleTypesObject = roleTypeUserFunctionalitiesSB.getFilteredSourceBeanAttribute("ROLE_TYPE_USER_FUNCTIONALITY", "userFunctionality", userFunctionality);
			if (roleTypesObject == null) {
				throw new Exception("No role type found for user functionality [" + userFunctionality + "]!!!");
			}
			StringBuffer roleTypesStrBuffer = new StringBuffer();
			Set roleTypes = new HashSet();
			if (roleTypesObject instanceof SourceBean) {
				SourceBean roleTypeSB = (SourceBean) roleTypesObject;
				String roleTypeCd = (String) roleTypeSB.getAttribute("roleType");
				roleTypesStrBuffer.append(roleTypeCd);
				SbiDomains domainRoleType = findDomain(aSession, roleTypeCd, "ROLE_TYPE");
				roleTypes.add(domainRoleType);
			} else if (roleTypesObject instanceof List) {
				List roleTypesSB = (List) roleTypesObject;
				Iterator roleTypesIt = roleTypesSB.iterator();
				while (roleTypesIt.hasNext()) {
					SourceBean roleTypeSB = (SourceBean) roleTypesIt.next();
					String roleTypeCd = (String) roleTypeSB.getAttribute("roleType");
					roleTypesStrBuffer.append(roleTypeCd);
					if (roleTypesIt.hasNext()) {
						roleTypesStrBuffer.append(";");
					}
					SbiDomains domainRoleType = findDomain(aSession, roleTypeCd, "ROLE_TYPE");
					roleTypes.add(domainRoleType);
				}
			}
			aUserFunctionality.setRoleType(roleTypes);
			
			logger.debug("Inserting UserFunctionality with name = [" + aUSerFunctionalitySB.getAttribute("name") + "] associated to role types [" + roleTypesStrBuffer.toString() + "]...");
			
			aSession.save(aUserFunctionality);
		}
		logger.debug("OUT");
	}
	
	
	private SbiDomains findDomain(Session aSession, String valueCode, String domainCode) {
		logger.debug("IN");
		String hql = "from SbiDomains where valueCd = ? and domainCd = ?";
		Query hqlQuery = aSession.createQuery(hql);
		hqlQuery.setParameter(0, valueCode);
		hqlQuery.setParameter(1, domainCode);
		SbiDomains domain = (SbiDomains) hqlQuery.uniqueResult();
		logger.debug("OUT");
		return domain;
	}
}
