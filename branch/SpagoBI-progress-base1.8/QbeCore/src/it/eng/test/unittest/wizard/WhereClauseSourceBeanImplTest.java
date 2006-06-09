package it.eng.test.unittest.wizard;

import java.util.Iterator;
import java.util.List;

import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WhereClauseSourceBeanImpl;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;
import junit.framework.TestCase;

public class WhereClauseSourceBeanImplTest extends TestCase {
	
	private static IWhereClause clause = null;
	private static IWhereField field1 = null; 
	private static IWhereField field2 = null;
	private static IWhereField field3 = null;
	
	private static IWhereField field = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		clause = new WhereClauseSourceBeanImpl();
		
		field1 = new WhereFieldSourceBeanImpl();
		field1.setFieldName("Field1");
		field1.setId("A");
					
		field2 = new WhereFieldSourceBeanImpl();
		field2.setFieldName("Field2");
		field2.setId("B");
		
		field3 = new WhereFieldSourceBeanImpl();
		field3.setFieldName("Field3");
		field3.setId("C");
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.WhereClauseSourceBeanImpl.addWhereField(IWhereField)'
	 */
	public void testAddWhereField() {
		clause.addWhereField(field1);
		
		List fieldsList = clause.getWhereFields();
		
		
		if (fieldsList.size()!=1) fail("The field has not been correctly added");
				
			field = (IWhereField)fieldsList.get(0);
			if (field == null){
				fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		clause.addWhereField(field2);
		
		if (fieldsList.size()!=2) fail("The field has not been correctly added");
		
		field = (IWhereField)fieldsList.get(1);
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.WhereClauseSourceBeanImpl.delWhereField(IWhereField)'
	 */
	public void testDelWhereField() {
		// A
		clause.addWhereField(field1);
		
		clause.delWhereField(field1);
		
		List fieldsList = clause.getWhereFields();
						
		if (fieldsList.size()!=0) fail("The field has not been correctly deleted");
		
		// B
		clause.addWhereField(field1);
		clause.addWhereField(field2);
		
		clause.delWhereField(field2);
		
		fieldsList = clause.getWhereFields();
		
		if (fieldsList.size()!=1) fail("The field has not been correctly deleted");
		
		Iterator iter = fieldsList.iterator();
		
		while(iter.hasNext()){
			
			field = (WhereFieldSourceBeanImpl)iter.next();
			
			if (field.getFieldName().equalsIgnoreCase("field2")) {
				fail("The field has not been deleted");
			} 
		}
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.WhereClauseSourceBeanImpl.moveUp(IWhereField)'
	 */
	public void testMoveUp() {
		clause.addWhereField(field1);
		clause.addWhereField(field2);
		clause.addWhereField(field3);
		
		List fieldsList = clause.getWhereFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveUp(field2);
		
		fieldsList = clause.getWhereFields();
		
		if (fieldsList.size()!=3) fail("The field list is no more correct");
		
		field = (IWhereField)fieldsList.get(0);
		
		if (field == null){
			fail("No fields have been correctly moved ");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}
		
		field = (IWhereField)fieldsList.get(1);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		field = (IWhereField)fieldsList.get(2);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field3");
			assertEquals(field.getId(),"C");
		}
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.WhereClauseSourceBeanImpl.moveDown(IWhereField)'
	 */
	public void testMoveDown() {
		clause.addWhereField(field1);
		clause.addWhereField(field2);
		clause.addWhereField(field3);
		
		List fieldsList = clause.getWhereFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveDown(field2);
		
		fieldsList = clause.getWhereFields();
		
		if (fieldsList.size()!=3) fail("The field list is no more correct");
		
		field = (IWhereField)fieldsList.get(0);
		
		if (field == null){
			fail("No fields have been correctly moved ");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		field = (IWhereField)fieldsList.get(1);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field3");
			assertEquals(field.getId(),"C");
		}
		
		field = (IWhereField)fieldsList.get(2);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}
	}

}
