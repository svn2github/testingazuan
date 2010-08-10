package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParameterDAOHibImplDbUnitTest extends DBConnectionTestCase {

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl.loadForDetailByParameterID(Integer)'.
	 * Loads a Parameter by id and verifies that it is correctly loaded.
	 */
	public void testLoadForDetailByParameterID() {
		ParameterDAOHibImpl parameterDAO = new ParameterDAOHibImpl();
		Integer parameterID = new Integer(8);
		Parameter parameter = null;
		try {
			parameter = parameterDAO.loadForDetailByParameterID(parameterID);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(parameterID, parameter.getId());
		assertEquals("Parameter for Country Code selection", parameter
				.getDescription());
		assertEquals("COUNTRY_CD", parameter.getLabel());
		assertEquals(new Integer(0), parameter.getLength());
		assertEquals("", parameter.getMask());
		assertEquals("Country Code", parameter.getName());
		assertEquals("STRING", parameter.getType());
		assertEquals(new Integer(49), parameter.getTypeId());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl.loadForExecutionByParameterIDandRoleName(Integer,
	 * String)'.
	 * Loads a check by id and role and verifies that it is correctly
	 * loaded. Then modifies the roles associated to a ParameterUse in order to
	 * let a Parameter have 2 ParameterUses for the same role and verifies that
	 * an exception occurs.
	 */
	public void testLoadForExecutionByParameterIDandRoleName() {
		ParameterDAOHibImpl parameterDAO = new ParameterDAOHibImpl();
		Integer parameterID = new Integer(11);
		String roleName = "/portal/site";
		Parameter parameter = null;
		try {
			parameter = parameterDAO.loadForExecutionByParameterIDandRoleName(
					parameterID, roleName);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(parameterID,parameter.getId());
		assertEquals(0,parameter.getChecks().size());
		assertEquals(new Integer(22),parameter.getModalityValue().getId());
		
		parameterID = new Integer(14);
		roleName = "/SpagoBiDev";
		try {
			parameter = parameterDAO.loadForExecutionByParameterIDandRoleName(
					parameterID, roleName);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(parameterID,parameter.getId());
		List checks = parameter.getChecks();
		assertEquals(2,checks.size());
		List checksId = new ArrayList();
		for (int i = 0; i < checks.size(); i++) checksId.add(((Check) checks.get(i)).getCheckId());
		assertTrue(checksId.contains(new Integer(32)));
		assertTrue(checksId.contains(new Integer(39)));
		assertEquals(new Integer(24),parameter.getModalityValue().getId());
		
		// modifies the roles associated to ParameterUse with id = 14 so the
		// Parameter with id = 14 has two ParameterUse (id = 13 & 14) for the
		// same role SpagoBiDev (id = 14)
		ParameterUseDAOHibImpl paruseDAO = new ParameterUseDAOHibImpl();
		ParameterUse paruse = null;
		try {
			paruse = paruseDAO.loadByUseID(new Integer(14));
			RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
			List roles = new ArrayList();
			Role spagoBiDev = roleDAO.loadByID(new Integer(14));
			roles.add(spagoBiDev);
			paruse.setAssociatedRoles(roles);
			paruseDAO.modifyParameterUse(paruse);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		try {
			parameter = parameterDAO.loadForExecutionByParameterIDandRoleName(
					parameterID, roleName);

			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
		
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl.loadAllParameters()'.
	 * Loads all Parameters and verifies that the correct list is returned.
	 */
	public void testLoadAllParameters() {
		ParameterDAOHibImpl parameterDAO = new ParameterDAOHibImpl();
		List list = null;
		try {
			list = parameterDAO.loadAllParameters();
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		List parametersIdExpected = new ArrayList();
		parametersIdExpected.add(new Integer(8));
		parametersIdExpected.add(new Integer(9));
		parametersIdExpected.add(new Integer(10));
		parametersIdExpected.add(new Integer(11));
		parametersIdExpected.add(new Integer(12));
		parametersIdExpected.add(new Integer(13));
		parametersIdExpected.add(new Integer(14));

		List parametersIdActual = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Parameter param = (Parameter) it.next();
			parametersIdActual.add(param.getId());
		}
		assertTrue(parametersIdExpected.containsAll(parametersIdActual));
		assertTrue(parametersIdActual.containsAll(parametersIdExpected));
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl.modifyParameter(Parameter)'.
	 * Modifies a Parameter and verifies that it is correctly modified by reloading it.
	 */
	public void testModifyParameter() {
		ParameterDAOHibImpl parameterDAO = new ParameterDAOHibImpl();
		Integer id = new Integer(8);
		Parameter parameterToModify = new Parameter();

		Parameter reloaded = null;
		try {
			parameterToModify = parameterDAO.loadForDetailByParameterID(id);
			parameterToModify.setLength(new Integer(6));
			parameterToModify.setDescription("Modified description");
			parameterToModify.setModality("DAVIDE,36");
			parameterToModify.setName("Modified name");
			parameterToModify.setType("Modified type code");
			parameterToModify.setTypeId(new Integer(54));
			parameterDAO.modifyParameter(parameterToModify);
			reloaded = parameterDAO.loadForDetailByParameterID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(parameterToModify.getId(), reloaded.getId());
		assertEquals(parameterToModify.getLength(), reloaded.getLength());
		assertEquals(parameterToModify.getDescription(), reloaded
				.getDescription());
		assertEquals("",reloaded.getModality());
		assertEquals(parameterToModify.getName(), reloaded.getName());
		assertEquals("DAVIDE",reloaded.getType());
		assertEquals(new Integer(36),reloaded.getTypeId());
		assertFalse(parameterToModify == reloaded);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl.insertParameter(Parameter)'.
	 * Inserts a Parameter and verifies that it is correctly inserted by reloading it.
	 */
	public void testInsertParameter() {
		ParameterDAOHibImpl parameterDAO = new ParameterDAOHibImpl();
		Parameter parameterToInsert = new Parameter();
		parameterToInsert.setLength(new Integer(6));
		parameterToInsert.setDescription("Description");
		parameterToInsert.setId(new Integer(7));
		parameterToInsert.setName("Name");
		parameterToInsert.setModality("DAVIDE,36");

		Parameter reloaded = null;
		try {
			parameterDAO.insertParameter(parameterToInsert);
			List list = parameterDAO.loadAllParameters();
			reloaded = (Parameter) list.get(list.size() - 1);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertFalse(parameterToInsert.getId().equals(reloaded.getId()));

		assertEquals(parameterToInsert.getLength(), reloaded.getLength());
		assertEquals(parameterToInsert.getDescription(), reloaded
				.getDescription());
		assertEquals(parameterToInsert.getName(), reloaded.getName());
		assertEquals("",reloaded.getModality());
		assertEquals("DAVIDE",reloaded.getType());
		assertEquals(new Integer(36),reloaded.getTypeId());
		assertFalse(parameterToInsert == reloaded);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl.eraseParameter(Parameter)'.
	 * Erases a Parameter and tries unsuccessfully to reload the same.
	 */
	public void testEraseParameter() {
		ParameterDAOHibImpl parameterDAO = new ParameterDAOHibImpl();

		Parameter parameter = new Parameter();
		parameter.setLength(new Integer(6));
		parameter.setDescription("Description");
		parameter.setName("Name");
		parameter.setModality("DAVIDE,36");
		Integer parId = null;

		List list = null;
		try {
			// inserts a new parameter
			parameterDAO.insertParameter(parameter);
			// load all the parameter
			list = parameterDAO.loadAllParameters();
			// retrieves the parameter just inserted with the correct Id
			parameter = (Parameter) list.get(list.size() - 1);
			parId = parameter.getId();
			// erase the parameter
			parameterDAO.eraseParameter(parameter);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		try {
			parameterDAO.loadForDetailByParameterID(parId);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
	}

}
