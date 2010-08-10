package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.hibernate.EngineDAOHibImpl;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Iterator;
import java.util.List;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

public class EngineDAOHibImplDbUnitTest extends DBConnectionTestCase {

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.EngineDAOHibImpl.loadEngineByID(Integer)'.
	 * Loads an Engine by id and verifies that it is correctly loaded.
	 */
	public void testLoadEngineByID() {
		EngineDAOHibImpl engineDAO = new EngineDAOHibImpl();
		Engine engine = null;
		Integer id=new Integer(9);
		try {
			engine = engineDAO.loadEngineByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(engine.getId(),id);
		assertEquals(engine.getDescription(),"Jasper Report Engine for static reporting in Release Environment");
		assertEquals(engine.getCriptable(),new Integer(0));
		assertEquals(engine.getName(),"Jasper Report Rel");
		assertEquals(engine.getUrl(),"http://web.eng.it:58080/SpagoBIJasperReportEngine/JasperReportServlet");
		assertEquals(engine.getSecondaryUrl(),"");
		assertEquals(engine.getDriverName(),"it.eng.spagobi.drivers.jasperreport.JasperReportDriver");
		assertEquals(engine.getLabel(),"REP-JASP-REL");
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.EngineDAOHibImpl.loadAllEngines()'.
	 * Loads all Engines and verifies that the correct list is returned.
	 */
	public void testLoadAllEngines() {
		EngineDAOHibImpl engineDAO = new EngineDAOHibImpl();
		List loadedlist=null;
		ITable expectedTable=null;
		Engine engine=null;
		try {
			loadedlist=engineDAO.loadAllEngines();
			expectedTable=dataSet.getTable("sbi_engines");
			assertEquals(7,loadedlist.size());
			Iterator loadedlistIt=loadedlist.iterator();
			for (int i=0; i<loadedlist.size();i++){
				engine=(Engine) loadedlistIt.next();
				assertEquals(engine.getId().intValue(),Integer.parseInt((String)expectedTable.getValue(i,"engine_id")));	
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
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.EngineDAOHibImpl.modifyEngine(Engine)'.
	 * Modifies an Engine and verifies that it is correctly modified by reloading it.
	 */
	public void testModifyEngine() {
		EngineDAOHibImpl engineDAO = new EngineDAOHibImpl();
		Engine firstengine = null;
		Engine secondengine = null;
		Integer id=new Integer(9);
		try {
			//loads the engine with the given Id
			firstengine = engineDAO.loadEngineByID(id);
			assertEquals(firstengine.getName(),"Jasper Report Rel");
			//modifies the loaded engine
			firstengine.setName("Modiefied Engine");
			firstengine.setDescription("Modified descritpion");
			firstengine.setDriverName("Driver name");
			firstengine.setLabel("Modified label");
			firstengine.setCriptable(new Integer (50));
			firstengine.setUrl("Modified url");
			engineDAO.modifyEngine(firstengine);
			//loads the modified engine
			secondengine = engineDAO.loadEngineByID(id);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(firstengine.getId(),secondengine.getId());
		assertEquals(firstengine.getDescription(),secondengine.getDescription());
		assertEquals(firstengine.getCriptable(),secondengine.getCriptable());
		assertEquals(firstengine.getName(),secondengine.getName());
		assertEquals(firstengine.getUrl(),secondengine.getUrl());
		assertEquals(firstengine.getSecondaryUrl(),secondengine.getSecondaryUrl());
		assertEquals(firstengine.getDriverName(),secondengine.getDriverName());
		assertEquals(firstengine.getLabel(),secondengine.getLabel());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.EngineDAOHibImpl.insertEngine(Engine)'.
	 * Inserts an Engine and verifies that it is correctly inserted by reloading it.
	 */
	public void testInsertEngine() {
		
		EngineDAOHibImpl engineDAO = new EngineDAOHibImpl();
		
		Engine enginefirst=new Engine();
		enginefirst.setDescription("Description");
		enginefirst.setCriptable(new Integer(10));
		enginefirst.setName("Inserted Engine");
		enginefirst.setUrl("Url");
		enginefirst.setSecondaryUrl("Secondary Url");
		enginefirst.setDriverName("Driver Name");
		enginefirst.setLabel("Label");
		
		Engine enginesecond=null;
		
		try {
			engineDAO.insertEngine(enginefirst);
			List list=engineDAO.loadAllEngines();
			enginesecond = (Engine) list.get(list.size() -1);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		assertEquals(enginefirst.getDescription(),enginesecond.getDescription());
		assertEquals(enginefirst.getCriptable(),enginesecond.getCriptable());
		assertEquals(enginefirst.getName(),enginesecond.getName());
		assertEquals(enginefirst.getUrl(),enginesecond.getUrl());
		assertEquals(enginefirst.getSecondaryUrl(),enginesecond.getSecondaryUrl());
		assertEquals(enginefirst.getDriverName(),enginesecond.getDriverName());
		assertEquals(enginefirst.getLabel(),enginesecond.getLabel());

	}
		

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.EngineDAOHibImpl.eraseEngine(Engine)'.
	 * Erases an Engine and then tries unsuccessfully to load the same.
	 */
	public void testEraseEngine() {	

		EngineDAOHibImpl engineDAO = new EngineDAOHibImpl();
		
		Engine engine = new Engine();
		//Id of an existing engine
		Integer engineId = new Integer(10);
		engine.setId(engineId);
		
		try {
			engineDAO.eraseEngine(engine);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		try {
			engineDAO.loadEngineByID(engineId);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.EngineDAOHibImpl.hasBIObjAssociated(String)'.
	 * Verifies that Engines have BIObjects associated as expected.
	 */
	public void testHasBIObjAssociated() {
		EngineDAOHibImpl engineDAO = new EngineDAOHibImpl();
		
		Integer id_7=new Integer(7);
		Integer id_10=new Integer(10);
		Integer id_11=new Integer(11);
		Integer id_12=new Integer(12);
		Integer id_13=new Integer(13);
		
		Integer id_8=new Integer(8);
		Integer id_9=new Integer(9);
		
		try {
			assertFalse(engineDAO.hasBIObjAssociated(id_7.toString()));
			assertFalse(engineDAO.hasBIObjAssociated(id_10.toString()));
			assertFalse(engineDAO.hasBIObjAssociated(id_11.toString()));
			assertFalse(engineDAO.hasBIObjAssociated(id_12.toString()));
			assertFalse(engineDAO.hasBIObjAssociated(id_13.toString()));
			
			assertTrue(engineDAO.hasBIObjAssociated(id_8.toString()));
			assertTrue(engineDAO.hasBIObjAssociated(id_9.toString()));
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}
}
