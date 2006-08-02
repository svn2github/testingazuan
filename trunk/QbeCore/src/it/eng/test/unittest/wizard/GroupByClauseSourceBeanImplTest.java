package it.eng.test.unittest.wizard;

import it.eng.qbe.wizard.GroupByClauseSourceBeanImpl;
import it.eng.qbe.wizard.GroupByFieldSourceBeanImpl;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderGroupByField;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class GroupByClauseSourceBeanImplTest extends TestCase {

	private static IGroupByClause clause = null;
	private static IOrderGroupByField field1 = null; 
	private static IOrderGroupByField field2 = null;
	private static IOrderGroupByField field3 = null;
	
	private static IOrderGroupByField field = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		clause = new GroupByClauseSourceBeanImpl();
		
		field1 = new GroupByFieldSourceBeanImpl();
		field1.setFieldName("Field1");
		field1.setId("A");
					
		field2 = new GroupByFieldSourceBeanImpl();
		field2.setFieldName("Field2");
		field2.setId("B");
		
		field3 = new GroupByFieldSourceBeanImpl();
		field3.setFieldName("Field3");
		field3.setId("C");
		
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.GroupByClauseSourceBeanImpl.addGroupByField(IOrderGroupByField)'
	 */
	public void testAddGroupByField() {
		
		clause.addGroupByField(field1);
		
		List fieldsList = clause.getGroupByFields();
		
		
		if (fieldsList.size()!=1) fail("The field has not been correctly added");
				
			field = (IOrderGroupByField)fieldsList.get(0);
			if (field == null){
				fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		clause.addGroupByField(field2);
		
		if (fieldsList.size()!=2) fail("The field has not been correctly added");
		
		field = (IOrderGroupByField)fieldsList.get(1);
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}
			

	}

	/*
	 * Test method for 'it.eng.qbe.wizard.GroupByClauseSourceBeanImpl.delGroupByField(IOrderGroupByField)'
	 */
	public void testDelGroupByField() {
		// A
		clause.addGroupByField(field1);
		
		clause.delGroupByField(field1);
		
		List fieldsList = clause.getGroupByFields();
						
		if (fieldsList.size()!=0) fail("The field has not been correctly deleted");
		
		// B
		clause.addGroupByField(field1);
		clause.addGroupByField(field2);
		
		clause.delGroupByField(field2);
		
		fieldsList = clause.getGroupByFields();
		
		if (fieldsList.size()!=1) fail("The field has not been correctly deleted");
		
		Iterator iter = fieldsList.iterator();
		
		while(iter.hasNext()){
			
			field = (GroupByFieldSourceBeanImpl)iter.next();
			
			if (field.getFieldName().equalsIgnoreCase("field2")) {
				fail("The field has not been deleted");
			} 
		}
							
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.GroupByClauseSourceBeanImpl.moveUp(IOrderGroupByField)'
	 */
	public void testMoveUp() {

		clause.addGroupByField(field1);
		clause.addGroupByField(field2);
		clause.addGroupByField(field3);
		
		List fieldsList = clause.getGroupByFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveUp(field2);
		
		fieldsList = clause.getGroupByFields();
		
		if (fieldsList.size()!=3) fail("The field list is no more correct");
		
		field = (IOrderGroupByField)fieldsList.get(0);
		
		if (field == null){
			fail("No fields have been correctly moved ");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}
		
		field = (IOrderGroupByField)fieldsList.get(1);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		field = (IOrderGroupByField)fieldsList.get(2);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field3");
			assertEquals(field.getId(),"C");
		}
		
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.GroupByClauseSourceBeanImpl.moveDown(IOrderGroupByField)'
	 */
	public void testMoveDown() {
		
		clause.addGroupByField(field1);
		clause.addGroupByField(field2);
		clause.addGroupByField(field3);
		
		List fieldsList = clause.getGroupByFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveDown(field2);
		
		fieldsList = clause.getGroupByFields();
		
		if (fieldsList.size()!=3) fail("The field list is no more correct");
		
		field = (IOrderGroupByField)fieldsList.get(0);
		
		if (field == null){
			fail("No fields have been correctly moved ");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		field = (IOrderGroupByField)fieldsList.get(1);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field3");
			assertEquals(field.getId(),"C");
		}
		
		field = (IOrderGroupByField)fieldsList.get(2);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}

	}

}

