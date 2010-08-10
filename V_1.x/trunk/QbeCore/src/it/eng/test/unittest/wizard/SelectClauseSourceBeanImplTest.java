/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.test.unittest.wizard;

import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.SelectClauseSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class SelectClauseSourceBeanImplTest extends TestCase {

	private static ISelectClause clause = null;
	private static ISelectField field1 = null; 
	private static ISelectField field2 = null;
	private static ISelectField field3 = null;
	
	private static ISelectField field = null;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		clause = new SelectClauseSourceBeanImpl();
		
		field1 = new SelectFieldSourceBeanImpl();
		field1.setFieldName("Field1");
		field1.setId("A");
					
		field2 = new SelectFieldSourceBeanImpl();
		field2.setFieldName("Field2");
		field2.setId("B");
		
		field3 = new SelectFieldSourceBeanImpl();
		field3.setFieldName("Field3");
		field3.setId("C");
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.SelectClauseSourceBeanImpl.addSelectField(ISelectField)'
	 */
	public void testAddSelectField() {
		clause.addSelectField(field1);
		
		List fieldsList = clause.getSelectFields();
		
		
		if (fieldsList.size()!=1) fail("The field has not been correctly added");
				
			field = (ISelectField)fieldsList.get(0);
			if (field == null){
				fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		clause.addSelectField(field2);
		
		if (fieldsList.size()!=2) fail("The field has not been correctly added");
		
		field = (ISelectField)fieldsList.get(1);
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.SelectClauseSourceBeanImpl.delSelectField(ISelectField)'
	 */
	public void testDelSelectField() {
		// A
		clause.addSelectField(field1);
		
		clause.delSelectField(field1);
		
		List fieldsList = clause.getSelectFields();
						
		if (fieldsList.size()!=0) fail("The field has not been correctly deleted");
		
		// B
		clause.addSelectField(field1);
		clause.addSelectField(field2);
		
		clause.delSelectField(field2);
		
		fieldsList = clause.getSelectFields();
		
		if (fieldsList.size()!=1) fail("The field has not been correctly deleted");
		
		Iterator iter = fieldsList.iterator();
		
		while(iter.hasNext()){
			
			field = (ISelectField)iter.next();
			
			if (field.getFieldName().equalsIgnoreCase("field2")) {
				fail("The field has not been deleted");
			} 
		}

	}

	/*
	 * Test method for 'it.eng.qbe.wizard.SelectClauseSourceBeanImpl.moveUp(ISelectField)'
	 */
	public void testMoveUp() {
		clause.addSelectField(field1);
		clause.addSelectField(field2);
		clause.addSelectField(field3);
		
		List fieldsList = clause.getSelectFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveUp(field2);
		
		fieldsList = clause.getSelectFields();
		
		if (fieldsList.size()!=3) fail("The field list is no more correct");
		
		field = (ISelectField)fieldsList.get(0);
		
		if (field == null){
			fail("No fields have been correctly moved ");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}
		
		field = (ISelectField)fieldsList.get(1);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		field = (ISelectField)fieldsList.get(2);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field3");
			assertEquals(field.getId(),"C");
		}

	}

	/*
	 * Test method for 'it.eng.qbe.wizard.SelectClauseSourceBeanImpl.moveDown(ISelectField)'
	 */
	public void testMoveDown() {
		clause.addSelectField(field1);
		clause.addSelectField(field2);
		clause.addSelectField(field3);
		
		List fieldsList = clause.getSelectFields();
		
		// notice that the addGroupByField has already been tested
		
		clause.moveDown(field2);
		
		fieldsList = clause.getSelectFields();
		
		if (fieldsList.size()!=3) fail("The field list is no more correct");
		
		field = (ISelectField)fieldsList.get(0);
		
		if (field == null){
			fail("No fields have been correctly moved ");
		} else {
			assertEquals(field.getFieldName(),"Field1");
			assertEquals(field.getId(),"A");
		}
		
		field = (ISelectField)fieldsList.get(1);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field3");
			assertEquals(field.getId(),"C");
		}
		
		field = (ISelectField)fieldsList.get(2);
		
		if (field == null){
			fail("No fields have been added");
		} else {
			assertEquals(field.getFieldName(),"Field2");
			assertEquals(field.getId(),"B");
		}

	}

}
