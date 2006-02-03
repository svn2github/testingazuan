package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParameterUseDAOHibImplDbUnitTest extends DBConnectionTestCase {

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.loadByUseID(Integer)'.
	 * Loads a ParameterUse by id and verifies that it is correctly loaded.
	 */
	public void testLoadByUseID() {
		ParameterUseDAOHibImpl parameteruseDAO = new ParameterUseDAOHibImpl();
		Integer useId = new Integer(13);
		ParameterUse paruse = new ParameterUse();
		try {
			paruse = parameteruseDAO.loadByUseID(useId);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(new Integer(13), paruse.getUseID());
		assertEquals(new Integer(14), paruse.getId());
		assertEquals("Output Format to choose", paruse.getDescription());
		assertEquals(new Integer(24), paruse.getIdLov());
		assertEquals("OUT_FREE", paruse.getLabel());
		assertEquals("Output Format ", paruse.getName());
		assertEquals(2, paruse.getAssociatedChecks().size());
		assertEquals(4, paruse.getAssociatedRoles().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.fillAssociatedChecksForParUse(ParameterUse)'.
	 * Verifies that Checks are correctly associated to ParameterUses.
	 */
	public void testFillAssociatedChecksForParUse() {
		ParameterUseDAOHibImpl parameteruseDAO = new ParameterUseDAOHibImpl();
		Integer useId = new Integer(13);
		ParameterUse paruse = new ParameterUse();
		paruse.setUseID(useId);
		try {
			parameteruseDAO.fillAssociatedChecksForParUse(paruse);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		List associatedChecksIdExpected = new ArrayList();
		associatedChecksIdExpected.add(new Integer(32));
		associatedChecksIdExpected.add(new Integer(39));
		List associatedChecks = paruse.getAssociatedChecks();
		List associatedChecksIdActual = new ArrayList();
		for (Iterator it = associatedChecks.iterator(); it.hasNext();) {
			Check associatedCheck = (Check) it.next();
			associatedChecksIdActual.add(associatedCheck.getCheckId());
		}
		assertTrue(associatedChecksIdExpected.containsAll(associatedChecksIdActual));
		assertTrue(associatedChecksIdActual.containsAll(associatedChecksIdExpected));

		useId = new Integer(18);
		paruse.setUseID(useId);
		try {
			parameteruseDAO.fillAssociatedChecksForParUse(paruse);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		associatedChecksIdExpected = new ArrayList();
		associatedChecks = paruse.getAssociatedChecks();
		associatedChecksIdActual = new ArrayList();
		for (Iterator it = associatedChecks.iterator(); it.hasNext();) {
			Check associatedCheck = (Check) it.next();
			associatedChecksIdActual.add(associatedCheck.getCheckId());
		}
		assertEquals(associatedChecksIdExpected, associatedChecksIdActual);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.fillRolesForParUse(ParameterUse)'
	 * Verifies that Roles are correctly associated to ParameterUses.
	 */
	 public void testFillRolesForParUse() {
		 ParameterUseDAOHibImpl parameteruseDAO=new ParameterUseDAOHibImpl();
		 ParameterUse paruse = new ParameterUse();
		 Integer useId = new Integer(13);
		 paruse.setUseID(useId);
		 try {
			parameteruseDAO.fillRolesForParUse(paruse);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		List roles = paruse.getAssociatedRoles();
		List rolesId = new ArrayList();
		for (int i= 0; i < roles.size(); i++) {
			Role role = (Role) roles.get(i);
			rolesId.add(role.getId());
		}
		List rolesIdExpected = new ArrayList();
		rolesIdExpected.add(new Integer(1));
		rolesIdExpected.add(new Integer(14));
		rolesIdExpected.add(new Integer(15));
		rolesIdExpected.add(new Integer(16));
		assertEquals(rolesIdExpected.size(), roles.size());
		assertTrue(rolesId.containsAll(rolesIdExpected));
	 }
	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.modifyParameterUse(ParameterUse)'.
	 * Modifies a ParameterUse with data loaded by another ParameterUse (but changing
	 * the label) and then verifies if it is modified as request by
	 * reloading the modified ParameterUse.
	 */
	public void testModifyParameterUse() {
		ParameterUseDAOHibImpl parameteruseDAO = new ParameterUseDAOHibImpl();
		ParameterUse paruseLoaded = null;
		ParameterUse paruseToModify = null;
		ParameterUse paruseModified = null;
		Integer useIdToLoad = new Integer(13);
		Integer useIdToModify = new Integer(14);
		try {
			// loads one ParameterUse
			paruseLoaded = parameteruseDAO.loadByUseID(useIdToLoad);
			paruseToModify = paruseLoaded;
			// modifies the useID and lovID of the loaded object
			paruseToModify.setUseID(useIdToModify);
			paruseToModify.setLabel("DEPA_FREE");
			// modifies the ParameterUse with the new useID
			parameteruseDAO.modifyParameterUse(paruseToModify);
			// loads the modified ParameterUse
			paruseModified = parameteruseDAO.loadByUseID(useIdToModify);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(paruseToModify.getId(), paruseModified.getId());
		assertEquals(paruseToModify.getDescription(), paruseModified
				.getDescription());
		assertEquals(paruseToModify.getIdLov(), paruseModified.getIdLov());
		assertEquals(paruseToModify.getLabel(), paruseModified.getLabel());
		assertEquals(paruseToModify.getName(), paruseModified.getName());
		assertEquals(paruseToModify.getAssociatedChecks().size(),
				paruseModified.getAssociatedChecks().size());
		assertEquals(paruseToModify.getAssociatedRoles().size(), paruseModified
				.getAssociatedRoles().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.insertParameterUse(ParameterUse)'.
	 * Inserts a ParameterUse and verifies that it is correctly inserted by reloading it.
	 */
	public void testInsertParameterUse() {
		ParameterUseDAOHibImpl parameteruseDAO = new ParameterUseDAOHibImpl();
		ParameterUse paruseLoaded = null;
		ParameterUse paruseToInsert = null;
		ParameterUse paruseInserted = null;
		Integer useIdToLoad = new Integer(13);
		try {
			// loads one ParameterUse
			paruseLoaded = parameteruseDAO.loadByUseID(useIdToLoad);
			paruseToInsert = paruseLoaded;
			paruseToInsert.setLabel("DEPA_FREE"); // we must
			// change the label or the par_id
			paruseToInsert.setId(new Integer(8)); 
			parameteruseDAO.insertParameterUse(paruseToInsert);
			// loads the inserted ParameterUse
			List list = parameteruseDAO.loadParametersUseByParId(paruseToInsert
					.getId());
			paruseInserted = (ParameterUse) list.get(list.size() - 1);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(paruseToInsert.getId(), paruseInserted.getId());
		assertEquals(paruseToInsert.getDescription(), paruseInserted
				.getDescription());
		assertEquals(paruseToInsert.getIdLov(), paruseInserted.getIdLov());
		assertEquals(paruseToInsert.getLabel(), paruseInserted.getLabel());
		assertEquals(paruseToInsert.getName(), paruseInserted.getName());
		assertEquals(paruseToInsert.getAssociatedChecks().size(),
				paruseInserted.getAssociatedChecks().size());
		assertEquals(paruseToInsert.getAssociatedRoles().size(), paruseInserted
				.getAssociatedRoles().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.eraseParameterUse(ParameterUse)'.
	 * Erases a ParameterUse and then tries unsuccessfully to load the same.
	 */
	public void testEraseParameterUse() {
		ParameterUseDAOHibImpl parameteruseDAO = new ParameterUseDAOHibImpl();
		ParameterUse paruseToDelete = null;
		Integer useIdToDelete = new Integer(13);
		try {
			paruseToDelete = parameteruseDAO.loadByUseID(useIdToDelete);
			parameteruseDAO.eraseParameterUse(paruseToDelete);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		
		try {
			parameteruseDAO.loadByUseID(useIdToDelete);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
		
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.hasParUseModes(String)'.
	 * Verifies that Parameters have ParameterUses as expected.
	 */
	public void testHasParUseModes() {
		ParameterUseDAOHibImpl parameteruseDAO = new ParameterUseDAOHibImpl();
		for (int i = 8; i <= 14; i++)
			try {
				Integer parId = new Integer(i);
				assertTrue(parameteruseDAO.hasParUseModes(parId.toString()));
			} catch (EMFUserError e) {
				e.printStackTrace();
				fail("Unexpected exception occurred!");
			}
		try {
			assertFalse(parameteruseDAO.hasParUseModes("20"));
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl.loadParametersUseByParId(Integer)'.
	 * Loads ParameterUses by the Parameter id and verifies that returned list are correct.
	 */
	public void testLoadParametersUseByParId() {
		ParameterUseDAOHibImpl parameteruseDAO = new ParameterUseDAOHibImpl();
		Integer parId = new Integer(8);
		List list = null;
		try {
			list = parameteruseDAO.loadParametersUseByParId(parId);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		List listUseIdExpected = new ArrayList();
		listUseIdExpected.add(new Integer(18));
		listUseIdExpected.add(new Integer(19));

		List listUseIdActual = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			ParameterUse paruse = (ParameterUse) it.next();
			listUseIdActual.add(paruse.getUseID());
		}
		assertEquals(listUseIdExpected, listUseIdActual);
	}

}
