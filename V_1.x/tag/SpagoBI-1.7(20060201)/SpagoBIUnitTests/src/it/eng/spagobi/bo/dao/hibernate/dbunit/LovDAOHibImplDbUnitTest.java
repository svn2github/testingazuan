package it.eng.spagobi.bo.dao.hibernate.dbunit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

public class LovDAOHibImplDbUnitTest extends DBConnectionTestCase {

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl.loadModalitiesValueByID(Integer)'.
	 * Loads a Lov and verifies that it is correctly loaded.
	 */
	public void testLoadModalitiesValueByID() {
		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		Integer id = new Integer(20);
		ModalitiesValue modVal = null;
		try {
			modVal = lovDAO.loadModalitiesValueByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(id, modVal.getId());
		assertEquals("Gender values by predefined list", modVal
				.getDescription());
		assertEquals("FIX_LOV", modVal.getITypeCd());
		assertEquals("39", modVal.getITypeId());
		assertEquals("GENDER_FX_01", modVal.getLabel());
		assertEquals(
				"<LOV><LOV-ELEMENT DESC = \"Male\" VALUE = \"M\"/><LOV-ELEMENT DESC" +
				" = \"Female\" VALUE = \"F\"/></LOV>",
				modVal.getLovProvider());
		assertEquals("Gender by Fixed Value", modVal.getName());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl.modifyModalitiesValue(ModalitiesValue)'.
	 * Modifies a Lov and verifies that it is correctly modified by reloading it.
	 */
	public void testModifyModalitiesValue() {
		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		Integer id = new Integer(20);
		ModalitiesValue modValToModify = null;
		ModalitiesValue reloaded = null;
		try {
			modValToModify = lovDAO.loadModalitiesValueByID(id);
			modValToModify.setDescription("Modified Description");
			modValToModify.setLabel("Modified label");
			modValToModify.setLovProvider("Modified Lov Provider");
			modValToModify.setName("Modified name");
			modValToModify.setITypeCd("DAVIDE");
			modValToModify.setITypeId("55");
			lovDAO.modifyModalitiesValue(modValToModify);
			reloaded = lovDAO.loadModalitiesValueByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(modValToModify.getId(), reloaded.getId());
		assertEquals(modValToModify.getDescription(), reloaded.getDescription());
		assertEquals(modValToModify.getLabel(), reloaded.getLabel());
		assertEquals(modValToModify.getLovProvider(), reloaded.getLovProvider());
		assertEquals(modValToModify.getName(), reloaded.getName());
		assertEquals(modValToModify.getITypeCd(), reloaded.getITypeCd());
		assertEquals(modValToModify.getITypeId(), reloaded.getITypeId());
		assertFalse(modValToModify == reloaded);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl.insertModalitiesValue(ModalitiesValue)'.
	 * Inserts a Lov and verifies that it is correctly inserted by reloading it.
	 */
	public void testInsertModalitiesValue() {
		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		ModalitiesValue lovToInsert = new ModalitiesValue();
		lovToInsert.setDescription("Description");
		lovToInsert.setId(new Integer(7));
		lovToInsert.setName("Name");
		lovToInsert.setITypeCd("DAVIDE");
		lovToInsert.setITypeId("55");
		lovToInsert.setLovProvider("Lov Provider");
		lovToInsert.setLabel("Label");

		ModalitiesValue reloaded = null;
		try {
			lovDAO.insertModalitiesValue(lovToInsert);
			List list = lovDAO.loadAllModalitiesValue();
			reloaded = (ModalitiesValue) list.get(list.size() - 1);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertFalse(lovToInsert.getId().equals(reloaded.getId()));

		assertEquals(lovToInsert.getLabel(), reloaded.getLabel());
		assertEquals(lovToInsert.getDescription(), reloaded.getDescription());
		assertEquals(lovToInsert.getName(), reloaded.getName());
		assertEquals(lovToInsert.getLovProvider(), reloaded.getLovProvider());
		assertEquals(lovToInsert.getITypeCd(), reloaded.getITypeCd());
		assertEquals(lovToInsert.getITypeId(), reloaded.getITypeId());
		assertFalse(lovToInsert == reloaded);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl.eraseModalitiesValue(ModalitiesValue)'.
	 * Erases a Lov and then tries unsuccessfully to load the same.
	 */
	public void testEraseModalitiesValue() {
		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		ModalitiesValue lov = new ModalitiesValue();
		lov.setDescription("Description");
		lov.setName("Name");
		lov.setITypeCd("DAVIDE");
		lov.setITypeId("55");
		lov.setLovProvider("Lov Provider");
		lov.setLabel("Label");
		Integer lovId = null;
		List list = null;
		try {
			// inserts a new lov
			lovDAO.insertModalitiesValue(lov);
			// load all the lovs
			list = lovDAO.loadAllModalitiesValue();
			// retrieves the lov just inserted with the correct Id
			lov = (ModalitiesValue) list.get(list.size() - 1);
			lovId = lov.getId();
			// erase the lov
			lovDAO.eraseModalitiesValue(lov);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		try {
			lovDAO.loadModalitiesValueByID(lovId);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
		
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl.loadAllModalitiesValue()'.
	 * Loads all Lovs and verifies that the correct list is returned.
	 */
	public void testLoadAllModalitiesValue() {
		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		List list = null;
		try {
			list = lovDAO.loadAllModalitiesValue();
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		List lovsIdExpected = new ArrayList();
		for (int i = 18; i <= 29; i++)
			lovsIdExpected.add(new Integer(i));

		List lovsIdActual = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			ModalitiesValue modVal = (ModalitiesValue) it.next();
			lovsIdActual.add(modVal.getId());
		}
		assertEquals(lovsIdExpected, lovsIdActual);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl.loadAllModalitiesValueOrderByCode()'.
	 * Loads all the Lovs and verifies that the returned list is oredered by code.
	 */
	public void testLoadAllModalitiesValueOrderByCode() {
		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		List list = null;

		try {
			list = lovDAO.loadAllModalitiesValueOrderByCode();
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		List lovsCdExpected = new ArrayList();
		for (int i = 1; i <= 7; i++)
			lovsCdExpected.add("FIX_LOV");
		for (int i = 1; i <= 5; i++)
			lovsCdExpected.add("QUERY");

		List lovsCdActual = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			ModalitiesValue modVal = (ModalitiesValue) it.next();
			lovsCdActual.add(modVal.getITypeCd());
		}
		assertEquals(lovsCdExpected, lovsCdActual);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl.hasModalitiesValues(String)'.
	 * Verifies that an existing Lov has Parameters as expected and then inserts
	 * a new Lov and verifies that it has no Parameters.
	 */
	public void testHasModalitiesValues() {
		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		String lovId = "25";
		boolean result = false;
		try {
			result = lovDAO.hasParameters(lovId);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertTrue(result);

		ModalitiesValue lov = new ModalitiesValue();
		lov.setDescription("Description");
		lov.setName("Name");
		lov.setITypeCd("DAVIDE");
		lov.setITypeId("55");
		lov.setLovProvider("Lov Provider");
		lov.setLabel("Label");
		try {
			lovDAO.insertModalitiesValue(lov);
			result = lovDAO.hasParameters(lov.getITypeId());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertFalse(result);
	}
}
