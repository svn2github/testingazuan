package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.dao.hibernate.BIObjectParameterDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BIObjectParameterDAOHibImplDbUnitTest extends DBConnectionTestCase {
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectParameterDAOHibImpl.loadBIObjectParameterForDetail(Integer,
	 * Integer)'.
	 * Loads a BIObjectParameter and verifies if it is correctly loaded.
	 */
	public void testLoadBIObjectParameterForDetail() {
		BIObjectParameterDAOHibImpl biobjparDAO = new BIObjectParameterDAOHibImpl();
		Integer biobjId = new Integer(21);
		Integer parId = new Integer(12);
		BIObjectParameter biobjpar = null;
		try {
			biobjpar = biobjparDAO.loadBIObjectParameterForDetail(biobjId,
					parId);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(biobjId, biobjpar.getBiObjectID());
		assertEquals("Choose position", biobjpar.getLabel());
		assertEquals(new Integer(0), biobjpar.getModifiable());
		assertEquals(new Integer(0), biobjpar.getMultivalue());
		assertEquals("", biobjpar.getParameter().getDescription());
		assertEquals("param_position_title", biobjpar.getParameterUrlName());
		assertEquals(null, biobjpar.getParameterValues());
		assertEquals(parId, biobjpar.getParID());
		assertEquals(null, biobjpar.getParIdOld());
		assertEquals(null, biobjpar.getProg());
		assertEquals(new Integer(0), biobjpar.getRequired());
		assertEquals(new Integer(1), biobjpar.getVisible());
		assertEquals(parId, biobjpar.getParameter().getId());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectParameterDAOHibImpl.modifyBIObjectParameter(BIObjectParameter)'.
	 * Modifies a BIObjectParameter and then verifies if it is modified as
	 * request by reloading the modified BIObjectParameter.
	 */
	public void testModifyBIObjectParameter() {
		BIObjectParameterDAOHibImpl biobjparDAO = new BIObjectParameterDAOHibImpl();
		Integer biobjId = new Integer(21);
		Integer parId = new Integer(12);
		BIObjectParameter biobjparToModify = null;
		BIObjectParameter reloaded = null;
		try {
			biobjparToModify = biobjparDAO.loadBIObjectParameterForDetail(
					biobjId, parId);
			biobjparToModify.setLabel("Modified label");
			biobjparToModify.setModifiable(new Integer(60));
			biobjparToModify.setMultivalue(new Integer(70));
			biobjparToModify.setParameterUrlName("Modified url name");
			List list = new ArrayList();
			list.add("Value 1");
			list.add("Value 2");
			list.add("Value 3");
			list.add("Value 4");
			biobjparToModify.setParameterValues(list);
			biobjparToModify.setParID(new Integer(25));
			biobjparToModify.setParIdOld(parId);
			biobjparToModify.setProg(new Integer(75));
			biobjparToModify.setRequired(new Integer(80));
			biobjparToModify.setVisible(new Integer(90));
			Parameter param = new Parameter();
			param.setId(new Integer(10));
			biobjparToModify.setParameter(param);
			biobjparDAO.modifyBIObjectParameter(biobjparToModify);
			reloaded = biobjparDAO.loadBIObjectParameterForDetail(biobjId,
					biobjparToModify.getParameter().getId());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(biobjparToModify.getBiObjectID(), reloaded.getBiObjectID());
		assertEquals(biobjparToModify.getLabel(), reloaded.getLabel());
		assertEquals(biobjparToModify.getModifiable(), reloaded.getModifiable());
		assertEquals(biobjparToModify.getMultivalue(), reloaded.getMultivalue());
		assertEquals("", reloaded.getParameter().getDescription());
		assertEquals(biobjparToModify.getParameterUrlName(), reloaded
				.getParameterUrlName());
		assertEquals(null, reloaded.getParameterValues());
		assertEquals(biobjparToModify.getParameter().getId(), reloaded
				.getParID());
		assertEquals(null, reloaded.getParIdOld());
		assertEquals(null, reloaded.getProg());
		assertEquals(biobjparToModify.getRequired(), reloaded.getRequired());
		assertEquals(biobjparToModify.getVisible(), reloaded.getVisible());
		assertEquals(biobjparToModify.getParameter().getId(), reloaded
				.getParameter().getId());
		assertEquals(null,reloaded.getParameterValues());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectParameterDAOHibImpl.insertBIObjectParameter(BIObjectParameter)'.
	 * Inserts a new BIObjectParameter and verifies if it is inserted correctly by reloading the same. 
	 */
	public void testInsertBIObjectParameter() {
		BIObjectParameterDAOHibImpl biobjparDAO = new BIObjectParameterDAOHibImpl();
		Integer biobjId = new Integer(21);
		Integer parId = new Integer(8);
		BIObjectParameter biobjparToInsert = new BIObjectParameter();
		biobjparToInsert.setBiObjectID(biobjId);
		biobjparToInsert.setLabel("Label");
		biobjparToInsert.setModifiable(new Integer(60));
		biobjparToInsert.setMultivalue(new Integer(70));
		biobjparToInsert.setParameterUrlName("Parameter url name");
		biobjparToInsert.setParameterValues(new ArrayList());
		biobjparToInsert.setParID(new Integer(2500000));
		biobjparToInsert.setParIdOld(parId);
		biobjparToInsert.setProg(new Integer(75));
		biobjparToInsert.setRequired(new Integer(80));
		biobjparToInsert.setVisible(new Integer(90));
		Parameter param = new Parameter();
		param.setId(parId);
		biobjparToInsert.setParameter(param);
		BIObjectParameter reloaded = null;
		try {
			biobjparDAO.insertBIObjectParameter(biobjparToInsert);
			reloaded = biobjparDAO.loadBIObjectParameterForDetail(
					biobjparToInsert.getBiObjectID(), biobjparToInsert
							.getParameter().getId());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(biobjparToInsert.getBiObjectID(), reloaded.getBiObjectID());
		assertEquals(biobjparToInsert.getLabel(), reloaded.getLabel());
		assertEquals(biobjparToInsert.getModifiable(), reloaded.getModifiable());
		assertEquals(biobjparToInsert.getMultivalue(), reloaded.getMultivalue());
		assertEquals("", reloaded.getParameter().getDescription());
		assertEquals(biobjparToInsert.getParameterUrlName(), reloaded
				.getParameterUrlName());
		assertEquals(null, reloaded.getParameterValues());
		assertEquals(null, reloaded.getParIdOld());
		assertEquals(null, reloaded.getProg());
		assertEquals(biobjparToInsert.getRequired(), reloaded.getRequired());
		assertEquals(biobjparToInsert.getVisible(), reloaded.getVisible());
		assertEquals(biobjparToInsert.getParameter().getId(), reloaded
				.getParID());
		assertEquals(biobjparToInsert.getParameter().getId(), reloaded
				.getParameter().getId());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectParameterDAOHibImpl.eraseBIObjectParameter(BIObjectParameter)'.
	 * Erases a BIObjectParameter and then verifies that it is deleted.
	 */
	public void testEraseBIObjectParameter() {
		BIObjectParameterDAOHibImpl biobjparDAO = new BIObjectParameterDAOHibImpl();
		Integer biobjId = new Integer(21);
		Integer parId = new Integer(11);
		BIObjectParameter biobjparToDelete = new BIObjectParameter();
		biobjparToDelete.setBiObjectID(biobjId);
		Parameter param = new Parameter();
		param.setId(parId);
		biobjparToDelete.setParameter(param);
		List list = null;
		try {
			biobjparDAO.eraseBIObjectParameter(biobjparToDelete);
			list = biobjparDAO.loadBIObjectParametersById(biobjparToDelete
					.getBiObjectID());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		List biobjparIdExpected = new ArrayList();
		biobjparIdExpected.add(new Integer(12));
		biobjparIdExpected.add(new Integer(14));

		List biobjparIdActual = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			BIObjectParameter biobjparam = (BIObjectParameter) it.next();
			biobjparIdActual.add(biobjparam.getParID());
		}

		assertEquals(biobjparIdExpected, biobjparIdActual);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectParameterDAOHibImpl.hasObjParameters(String)'.
	 * Verifies that all Parameters have BIObjectParameters associated.
	 */
	public void testHasObjParameters() {
		BIObjectParameterDAOHibImpl biobjparDAO = new BIObjectParameterDAOHibImpl();
		try {
			assertTrue(biobjparDAO.hasObjParameters("8"));
			assertFalse(biobjparDAO.hasObjParameters("9"));
			assertTrue(biobjparDAO.hasObjParameters("10"));
			assertTrue(biobjparDAO.hasObjParameters("11"));
			assertTrue(biobjparDAO.hasObjParameters("12"));
			assertTrue(biobjparDAO.hasObjParameters("13"));
			assertTrue(biobjparDAO.hasObjParameters("14"));
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectParameterDAOHibImpl.loadBIObjectParametersById(Integer)'.
	 * Loads all BIObjectParameter associated to a BIObject and verifies that the correct list is returned.
	 */
	public void testLoadBIObjectParametersById() {
		BIObjectParameterDAOHibImpl biobjparDAO = new BIObjectParameterDAOHibImpl();
		List list = null;
		Integer id = new Integer(21);
		try {
			list = biobjparDAO.loadBIObjectParametersById(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		List biobjparIdExpected = new ArrayList();
		biobjparIdExpected.add(new Integer(11));
		biobjparIdExpected.add(new Integer(12));
		biobjparIdExpected.add(new Integer(14));

		List biobjparIdActual = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			BIObjectParameter biobjparam = (BIObjectParameter) it.next();
			biobjparIdActual.add(biobjparam.getParID());
		}
		assertEquals(biobjparIdExpected, biobjparIdActual);
	}
}
