package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.dao.hibernate.CheckDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Iterator;
import java.util.List;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

public class CheckDAOHibImplDbUnitTest extends DBConnectionTestCase {

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.CheckDAOHibImpl.loadAllChecks()'.
	 * Loads all Checks and verifies that the correct list is returned.
	 */
	public void testLoadAllChecks() {
		CheckDAOHibImpl checkDAO= new CheckDAOHibImpl();
		List list=null;
		Check check=null;
		ITable expectedTable=null;
		try {
			list=checkDAO.loadAllChecks();
			assertEquals(list.size(),13);
			expectedTable=dataSet.getTable("sbi_checks");
			Iterator listIt=list.iterator();
			for (int i=0; i<list.size();i++){
				check=(Check) listIt.next();
				assertEquals(check.getCheckId().intValue(),Integer.parseInt((String)expectedTable.getValue(i,"check_id")));	
			}
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} catch (DataSetException e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.CheckDAOHibImpl.loadCheckByID(Integer)'.
	 * Loads a Check by id and verifies that it is correctly loaded.
	 */
	public void testLoadCheckByID() {
		CheckDAOHibImpl checkDAO= new CheckDAOHibImpl();
		Integer id= new Integer(33);
		Check check= null;
		try {
			check = checkDAO.loadCheckByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(check.getCheckId(),id);
		assertEquals(check.getDescription(),"Control if a parameter is a letter string");
		assertEquals(check.getLabel(),"CK-FIX-04");
		assertEquals(check.getValueTypeCd(),"LETTERSTRING");
		assertEquals(check.getValueTypeId(),new Integer("67"));
		assertEquals(check.getName(),"Letter String");
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.CheckDAOHibImpl.eraseCheck(Check)'.
	 * Erase a Check and tries unsuccessfully to reload the same. 
	 */
	public void testEraseCheck() {
		CheckDAOHibImpl checkDAO= new CheckDAOHibImpl();
		Check check=new Check();
		Integer checkId = new Integer(40);
		check.setCheckId(checkId);
		try {
			checkDAO.eraseCheck(check);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		try {
			checkDAO.loadCheckByID(checkId);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
		
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.CheckDAOHibImpl.insertCheck(Check)'.
	 * Inserts a Check and verifies that it is correctly inserted by reloading the same.
	 */
	public void testInsertCheck() {
		CheckDAOHibImpl checkDAO= new CheckDAOHibImpl();
		Check checkToIns=new Check();
		Check checkReloaded=null;
		checkToIns.setDescription("Description");
		checkToIns.setFirstValue("false");
		checkToIns.setLabel("Label");
		checkToIns.setName("Name");
		checkToIns.setSecondValue("true");
		checkToIns.setValueTypeCd("OLAP");
		checkToIns.setValueTypeId(new Integer(41));
		
		try {
			checkDAO.insertCheck(checkToIns);
			List list=checkDAO.loadAllChecks();
			checkReloaded = (Check) list.get(list.size() -1);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(checkToIns.getDescription(),checkReloaded.getDescription());
		assertEquals(checkToIns.getFirstValue(),checkReloaded.getFirstValue());
		assertEquals(checkToIns.getLabel(),checkReloaded.getLabel());
		assertEquals(checkToIns.getName(),checkReloaded.getName());
		assertEquals(checkToIns.getSecondValue(),checkReloaded.getSecondValue());
		assertEquals(checkToIns.getValueTypeCd(),checkReloaded.getValueTypeCd());
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.CheckDAOHibImpl.modifyCheck(Check)'.
	 * Modifies a Check and verifies that it is correctly modified by reloading the same.
	 */
	public void testModifyCheck() {
		CheckDAOHibImpl checkDAO= new CheckDAOHibImpl();
		Integer id= new Integer(33);
		Check checkToModify= null;
		Check checkReloaded=null;
		try {
			checkToModify = checkDAO.loadCheckByID(id);
			checkToModify.setDescription("Modified description");
			checkToModify.setLabel("Modified label");
			checkToModify.setValueTypeCd("NUMERIC");
			checkToModify.setValueTypeId(new Integer(65));
			checkToModify.setName("Modified name");
			checkDAO.modifyCheck(checkToModify);
			checkReloaded = checkDAO.loadCheckByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(checkToModify.getCheckId(),checkReloaded.getCheckId());
		assertEquals(checkToModify.getDescription(),checkReloaded.getDescription());
		assertEquals(checkToModify.getLabel(),checkReloaded.getLabel());
		assertEquals(checkToModify.getValueTypeCd(),checkReloaded.getValueTypeCd());
		assertEquals(checkToModify.getValueTypeId(),checkReloaded.getValueTypeId());
		assertEquals(checkToModify.getName(),checkReloaded.getName());
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.CheckDAOHibImpl.isReferenced(String)'.
	 * Verifies if Checks are referenced as expected.
	 */
	public void testIsReferenced() {
		CheckDAOHibImpl checkDAO= new CheckDAOHibImpl();
		Integer id_30=new Integer(30);
		Integer id_31=new Integer(31);
		Integer id_33=new Integer(33);
		Integer id_35=new Integer(35);
		Integer id_40=new Integer(40);
		
		Integer id_32=new Integer(32);
		Integer id_39=new Integer(39);
		
		try {
			assertFalse(checkDAO.isReferenced(id_30.toString()));
			assertFalse(checkDAO.isReferenced(id_31.toString()));
			assertFalse(checkDAO.isReferenced(id_33.toString()));
			assertFalse(checkDAO.isReferenced(id_35.toString()));
			assertFalse(checkDAO.isReferenced(id_40.toString()));

			assertTrue(checkDAO.isReferenced(id_32.toString()));
			assertTrue(checkDAO.isReferenced(id_39.toString()));
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}	
	}

}
