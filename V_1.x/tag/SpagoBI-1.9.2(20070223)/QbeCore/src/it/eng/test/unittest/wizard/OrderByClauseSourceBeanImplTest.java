package it.eng.test.unittest.wizard;

import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
import it.eng.qbe.wizard.OrderByClauseSourceBeanImpl;
import it.eng.qbe.wizard.OrderByFieldSourceBeanImpl;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class OrderByClauseSourceBeanImplTest extends TestCase {
	
	private static IOrderByClause clause = null;
	private static IOrderGroupByField field1 = null; 
	private static IOrderGroupByField field2 = null;
	private static IOrderGroupByField field3 = null;
	
	private static IOrderGroupByField field = null;

	protected void setUp() throws Exception {
		super.setUp();
		clause = new OrderByClauseSourceBeanImpl();
		
		field1 = new OrderByFieldSourceBeanImpl();
		field1.setFieldName("Field1");
		field1.setId("A");
					
		field2 = new OrderByFieldSourceBeanImpl();
		field2.setFieldName("Field2");
		field2.setId("B");
		
		field3 = new OrderByFieldSourceBeanImpl();
		field3.setFieldName("Field3");
		field3.setId("C");
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.OrderByClauseSourceBeanImpl.addOrderByField(IOrderGroupByField)'
	 */
	public void testAddOrderByField() {
		clause.addOrderByField(field1);
		
		List fieldsList = clause.getOrderByFields();
		
		
		if (fieldsList.size()!=1) fail("The field has not been correctly added");
				
			field = (IOrderGroupByField)fieldsList.get(0);
			if (field == null){
				fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		clause.addOrderByField(field2);
		
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
	 * Test method for 'it.eng.qbe.wizard.OrderByClauseSourceBeanImpl.delOrderByField(IOrderGroupByField)'
	 */
	public void testDelOrderByField() {
//		 A
		clause.addOrderByField(field1);
		
		clause.delOrderByField(field1);
		
		List fieldsList = clause.getOrderByFields();
						
		if (fieldsList.size()!=0) fail("The field has not been correctly deleted");
		
		// B
		clause.addOrderByField(field1);
		clause.addOrderByField(field2);
		
		clause.delOrderByField(field2);
		
		fieldsList = clause.getOrderByFields();
		
		if (fieldsList.size()!=1) fail("The field has not been correctly deleted");
		
		Iterator iter = fieldsList.iterator();
		
		while(iter.hasNext()){
			
			field = (OrderByFieldSourceBeanImpl)iter.next();
			
			if (field.getFieldName().equalsIgnoreCase("field2")) {
				fail("The field has not been deleted");
			} 
		}
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.OrderByClauseSourceBeanImpl.moveUp(IOrderGroupByField)'
	 */
	public void testMoveUp() {
		clause.addOrderByField(field1);
		clause.addOrderByField(field2);
		clause.addOrderByField(field3);
		
		List fieldsList = clause.getOrderByFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveUp(field2);
		
		fieldsList = clause.getOrderByFields();
		
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
	 * Test method for 'it.eng.qbe.wizard.OrderByClauseSourceBeanImpl.moveDown(IOrderGroupByField)'
	 */
	public void testMoveDown() {
		clause.addOrderByField(field1);
		clause.addOrderByField(field2);
		clause.addOrderByField(field3);
		
		List fieldsList = clause.getOrderByFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveDown(field2);
		
		fieldsList = clause.getOrderByFields();
		
		if (fieldsList.size()!=3) fail("The field list is no more correct");
		
		field = (OrderByFieldSourceBeanImpl)fieldsList.get(0);
		
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
