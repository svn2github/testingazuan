package it.eng.spagobi.tools.distributionlist.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.tools.datasource.dao.DataSourceDAOHibImpl;
import it.eng.spagobi.tools.distributionlist.bo.DistributionList;
import it.eng.spagobi.tools.distributionlist.bo.Email;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DistributionListDaoImpl extends AbstractHibernateDAO implements IDistributionListDAO {

	static private Logger logger = Logger.getLogger(DistributionListDaoImpl.class);
	
	public void eraseDistributionList(DistributionList distributionList)
			throws EMFUserError {
		// TODO Auto-generated method stub

	}

	public void insertDistributionList(DistributionList distributionList)
			throws EMFUserError {
		// TODO Auto-generated method stub

	}

	public List loadAllDistributionLists() throws EMFUserError {
		
		logger.debug("IN");
		List risultato = new ArrayList();
		//Prima DistribList
		DistributionList dl = new DistributionList();
		dl.setName("Pippo");
		Email email1 = new Email();
		Email email2 = new Email();
		email1.setEmail("a@a.it");
		email1.setUserId("user11");
		email2.setEmail("b@b.it");
		email2.setUserId("user12");
		List emails1 = new ArrayList();
		emails1.add(email1);
		emails1.add(email2);
		dl.setEmails(emails1);
		dl.setId(1);
		dl.setDescr("Descr1");
		//Seconda DistribList
		DistributionList d2 = new DistributionList();
		d2.setName("Pluto");
		Email email3 = new Email();
		Email email4 = new Email();
		email3.setEmail("c@c.it");
		email3.setUserId("user21");
		email4.setEmail("d@d.it");
		email4.setUserId("user22");
		List emails2 = new ArrayList();
		emails2.add(email3);
		emails2.add(email4);
		d2.setEmails(emails2);
		d2.setId(1);
		d2.setDescr("Descr2");
		risultato.add(d2);
		logger.debug("OUT");
		return risultato;
		
	}

	public DistributionList loadDistributionListById(Integer Id)
			throws EMFUserError {

		logger.debug("IN");
		DistributionList dl = new DistributionList();
		dl.setName("Pippo");
		Email email1 = new Email();
		Email email2 = new Email();
		email1.setEmail("a@a.it");
		email1.setUserId("user11");
		email2.setEmail("b@b.it");
		email1.setUserId("user12");
		List emails = new ArrayList();
		emails.add(email1);
		emails.add(email2);
		dl.setEmails(emails);
		dl.setId(Id.intValue());
		dl.setDescr("Descr1");
		logger.debug("OUT");
		return dl;
	}

	public void modifyDistributionList(DistributionList distributionList)
			throws EMFUserError {
		// TODO Auto-generated method stub

	}

}
