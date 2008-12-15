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
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;

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
	
}
