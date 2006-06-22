package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.dao.hibernate.DomainDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.List;

public class DomainDAOHibImplDbUnitTest extends DBConnectionTestCase {

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.DomainDAOHibImpl.loadListDomainsByType(String)'.
	 * Loads all Domains by type and verifies that the correct lists are returned.
	 * 
	 */
	public void testLoadListDomainsByType() {
		DomainDAOHibImpl domainDAO=new DomainDAOHibImpl();
		String check="CHECK";
		String pred_check="PRED_CHECK";
		String input_type="INPUT_TYPE";
		String biobj_type="BIOBJ_TYPE";
		String value_type="VALUE_TYPE";
		String par_type="PAR_TYPE";
		String role_type="ROLE_TYPE";
		String funct_type="FUNCT_TYPE";
		String state="STATE";
		
		List listCheck=null;
		List listPred_check=null;
		List listInput_type=null;
		List listBiobj_type=null;
		List listValue_type=null;
		List listPar_type=null;
		List listRole_type=null;
		List listFunct_type=null;
		List listState=null;
		
		try {
			listCheck=domainDAO.loadListDomainsByType(check);
			listPred_check=domainDAO.loadListDomainsByType(pred_check);
			listInput_type=domainDAO.loadListDomainsByType(input_type);
			listBiobj_type=domainDAO.loadListDomainsByType(biobj_type);
			listValue_type=domainDAO.loadListDomainsByType(value_type);
			listPar_type=domainDAO.loadListDomainsByType(par_type);
			listRole_type=domainDAO.loadListDomainsByType(role_type);
			listFunct_type=domainDAO.loadListDomainsByType(funct_type);
			listState=domainDAO.loadListDomainsByType(state);
			
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(listCheck.size(),6);
		assertEquals(listPred_check.size(),7);
		assertEquals(listInput_type.size(),4);
		assertEquals(listBiobj_type.size(),4);
		assertEquals(listValue_type.size(),3);
		assertEquals(listPar_type.size(),3);
		assertEquals(listRole_type.size(),3);
		assertEquals(listFunct_type.size(),1);
		assertEquals(listState.size(),4);
		
		List checkIdsExpected=new ArrayList();
		checkIdsExpected.add(new Integer(58));
		checkIdsExpected.add(new Integer(59));
		checkIdsExpected.add(new Integer(60));
		checkIdsExpected.add(new Integer(61));
		checkIdsExpected.add(new Integer(62));
		checkIdsExpected.add(new Integer(63));
		
		List checkIdsActual=new ArrayList();
		for (int i=0;i<listCheck.size();i++){
			Domain ch=(Domain)listCheck.get(i);
			checkIdsActual.add(ch.getValueId());
		}
		assertTrue(checkIdsExpected.containsAll(checkIdsActual));
		assertTrue(checkIdsActual.containsAll(checkIdsExpected));
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.DomainDAOHibImpl.loadDomainByCodeAndValue(String, String)'.
	 * Loads a Domain by code and value and verifies that the result is as expected.
	 */
	public void testLoadDomainByCodeAndValue() {
		DomainDAOHibImpl domainDAO=new DomainDAOHibImpl();
		String domainCd="INPUT_TYPE";
		String valueCd="QUERY";
		
		Domain domain=null;
		
		try {
			domain=domainDAO.loadDomainByCodeAndValue(domainCd,valueCd);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(36,domain.getValueId().intValue());
		assertEquals("QUERY",domain.getValueCd());
		assertEquals("Query statement",domain.getValueName());
	}
	
}
