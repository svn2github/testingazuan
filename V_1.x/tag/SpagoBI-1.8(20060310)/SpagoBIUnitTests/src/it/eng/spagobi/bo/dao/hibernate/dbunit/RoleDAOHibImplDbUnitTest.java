package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoleDAOHibImplDbUnitTest extends DBConnectionTestCase {

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.loadByID(Integer)'.
	 * Loads a Role by id and verifies that it is correctly loaded.
	 */
	public void testLoadByID() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		Integer id = new Integer(15);
		Role role = null;
		try {
			role = roleDAO.loadByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(id, role.getId());
		assertEquals(null, role.getCode());
		assertEquals("The spagobi testers group", role.getDescription());
		assertEquals("/SpagoBiTest", role.getName());
		assertEquals("PORTAL", role.getRoleTypeCD());
		assertEquals(new Integer(53), role.getRoleTypeID());

		id = new Integer(18);
		try {
			role = roleDAO.loadByID(id);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100, e.getCode());
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.loadByName(String)'.
	 * Loads a Role by name and verifies that it is correctly loaded.
	 */
	public void testLoadByName() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		String name = "/SpagoBiTest";
		Role role = null;
		try {
			role = roleDAO.loadByName(name);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(new Integer(15), role.getId());
		assertEquals(null, role.getCode());
		assertEquals("The spagobi testers group", role.getDescription());
		assertEquals(name, role.getName());
		assertEquals("PORTAL", role.getRoleTypeCD());
		assertEquals(new Integer(53), role.getRoleTypeID());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.loadAllRoles()'.
	 * Loads all Roles and verifies that the correct list is returned.
	 */
	public void testLoadAllRoles() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		List listRoles = null;
		try {
			listRoles = roleDAO.loadAllRoles();
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		List rolesIdsExpected = new ArrayList();
		rolesIdsExpected.add(new Integer(1));
		rolesIdsExpected.add(new Integer(2));
		rolesIdsExpected.add(new Integer(3));
		rolesIdsExpected.add(new Integer(4));
		rolesIdsExpected.add(new Integer(5));
		rolesIdsExpected.add(new Integer(6));
		rolesIdsExpected.add(new Integer(7));
		rolesIdsExpected.add(new Integer(14));
		rolesIdsExpected.add(new Integer(15));
		rolesIdsExpected.add(new Integer(16));

		List rolesIdsActual = new ArrayList();
		for (int i = 0; i < listRoles.size(); i++) {
			Role rl = (Role) listRoles.get(i);
			rolesIdsActual.add(rl.getId());
		}
		assertEquals(rolesIdsExpected.size(), rolesIdsActual.size());
		assertTrue(rolesIdsExpected.containsAll(rolesIdsActual));
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.insertRole(Role)'.
	 * Inserts a Role and verifies that it is correctly inserted by reloading it.
	 */
	public void testInsertRole() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		Role roleToInsert = new Role();
		roleToInsert.setCode("Code");
		roleToInsert.setDescription("Description");
		roleToInsert.setId(new Integer(9));
		roleToInsert.setName("Name");
		roleToInsert.setRoleTypeCD("Role type code");
		roleToInsert.setRoleTypeID(new Integer(49));

		Role reloaded = null;
		try {
			roleDAO.insertRole(roleToInsert);
			List list = roleDAO.loadAllRoles();
			reloaded = (Role) list.get(list.size() - 1);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertFalse(roleToInsert.getId().equals(reloaded.getId()));
		assertEquals(roleToInsert.getCode(), reloaded.getCode());
		assertEquals(roleToInsert.getDescription(), reloaded.getDescription());
		assertEquals(roleToInsert.getName(), reloaded.getName());
		assertEquals(roleToInsert.getRoleTypeCD(), reloaded.getRoleTypeCD());
		assertEquals(roleToInsert.getRoleTypeID(), reloaded.getRoleTypeID());
		assertFalse(roleToInsert == reloaded);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.eraseRole(Role)'.
	 * Erases a Role and tries unsuccessfully to load the same.
	 */
	public void testEraseRole() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		Role roleToInsert = new Role();
		roleToInsert.setCode("Code");
		roleToInsert.setDescription("New Role");
		roleToInsert.setName("New Role");
		roleToInsert.setRoleTypeCD("FUNCT");
		roleToInsert.setRoleTypeID(new Integer(50));
		Integer id = null;
		List roles = null;
		Role roleToDelete = null;
		try {
			roleDAO.insertRole(roleToInsert);
			roles = roleDAO.loadAllRoles();
			roleToDelete = (Role) roles.get(roles.size()-1);
			id = roleToDelete.getId();
			roleDAO.eraseRole(roleToDelete);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	
		try {
			roleDAO.loadByID(id);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.modifyRole(Role)'.
	 * Modifies a Role and verifies that it is correctly modified by reloading the same.
	 */
	public void testModifyRole() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		Integer id = new Integer(7);
		Role roleToModify = new Role();

		Role reloaded = null;
		try {
			roleToModify = roleDAO.loadByID(id);
			roleToModify.setCode("Modified code");
			roleToModify.setDescription("Modified description");
			roleToModify.setName("Modified name");
			roleToModify.setRoleTypeCD("Modified type code");
			roleToModify.setRoleTypeID(new Integer(36));
			roleDAO.modifyRole(roleToModify);
			reloaded = roleDAO.loadByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(roleToModify.getId(), reloaded.getId());
		assertEquals(roleToModify.getCode(), reloaded.getCode());
		assertEquals(roleToModify.getDescription(), reloaded.getDescription());
		assertEquals(roleToModify.getName(), reloaded.getName());
		assertEquals(roleToModify.getRoleTypeCD(), reloaded.getRoleTypeCD());
		assertEquals(roleToModify.getRoleTypeID(), reloaded.getRoleTypeID());
		assertFalse(roleToModify == reloaded);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.loadAllFreeRolesForInsert(Integer)'.
	 * Verifies that free roles for insert are returned.
	 */
	public void testLoadAllFreeRolesForInsert() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		Integer parId = new Integer(14);
		List list = null;
		try {
			list = roleDAO.loadAllFreeRolesForInsert(parId);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0,list.size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl.loadAllFreeRolesForDetail(Integer)'.
	 * Verifies that free roles for detail are returned.
	 */
	public void testLoadAllFreeRolesForDetail() {
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		Integer parUseId = new Integer(14);
		List list = null;
		try {
			list = roleDAO.loadAllFreeRolesForDetail(parUseId);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		List rolesIdExpected = new ArrayList();
		rolesIdExpected.add(new Integer(2));
		rolesIdExpected.add(new Integer(4));
		rolesIdExpected.add(new Integer(6));
		
		List rolesIdActual = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Role role = (Role) it.next();
			rolesIdActual.add(role.getId());
		}
		assertTrue(rolesIdActual.containsAll(rolesIdExpected));
		assertEquals(rolesIdExpected.size(),rolesIdActual.size());
	}

}
