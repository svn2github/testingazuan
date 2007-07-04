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

import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.SelectClauseSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.qbe.wizard.WhereClauseSourceBeanImpl;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class SingleDataMartWizardObjectSourceBeanImplTest extends TestCase {

	private static ISingleDataMartWizardObject query = null;
	
	private static EntityClass ec1 = null;
	private static EntityClass ec2 = null;
	
	private static ISelectClause selectClause = null;
	private static ISelectField selectField1 = null;
	private static ISelectField selectField2 = null;
	private static List selectFieldsList = null;
	
	private static IWhereClause whereClause = null;
	private static IWhereField whereField1 = null;
	private static IWhereField whereField2 = null;
	private static List whereFieldsList = null;
	
	
		
	public static void main(String[] args) throws Exception{
		
		SingleDataMartWizardObjectSourceBeanImplTest a = new SingleDataMartWizardObjectSourceBeanImplTest();
		a.setUp();
		a.testPurgeNotReferredEntityClasses();
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		query = new SingleDataMartWizardObjectSourceBeanImpl();
		
		ec1 = new EntityClass();
		ec1.setClassName("EntityClass1");
		ec1.setClassAlias("Entity Class Alias 1");
		
		ec2 = new EntityClass();
		ec2.setClassName("EntityClass2");
		ec2.setClassAlias("Entity Class Alias 2");
		
		selectClause = new SelectClauseSourceBeanImpl();
		selectField1 = new SelectFieldSourceBeanImpl();
		selectField2 = new SelectFieldSourceBeanImpl();
		
		whereClause = new WhereClauseSourceBeanImpl();
		whereField1 = new WhereFieldSourceBeanImpl();
		whereField2 = new WhereFieldSourceBeanImpl();
		
		selectFieldsList = new ArrayList();
		whereFieldsList = new ArrayList();
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl.containEntityClass(EntityClass)'
	 */
	public void testContainEntityClass() {

		query.addEntityClass(ec1);
		
		assertTrue(query.containEntityClass(ec1));
		assertFalse(query.containEntityClass(ec2));
				
	}

	/*
	 * Test method for 'it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl.purgeNotReferredEntityClasses()'
	 */
	public void testPurgeNotReferredEntityClasses() {

		// A
		selectField1.setFieldEntityClass(ec1);
		selectFieldsList.add(selectField1);
		selectClause.setSelectFields(selectFieldsList);
		query.setSelectClause(selectClause);
		
		query.purgeNotReferredEntityClasses();
		
		assertTrue(query.containEntityClass(ec1));
		assertFalse(query.containEntityClass(ec2));
		
		// B
		selectField2.setFieldEntityClass(ec2);
		selectFieldsList.add(selectField2);
		selectClause.setSelectFields(selectFieldsList);
		query.setSelectClause(selectClause);
		
		query.purgeNotReferredEntityClasses();
		
		assertTrue(query.containEntityClass(ec1));
		assertTrue(query.containEntityClass(ec2));
		
		// C
		selectClause.delSelectField(selectField1);
		query.setSelectClause(selectClause);
		
		query.purgeNotReferredEntityClasses();
		
		assertFalse(query.containEntityClass(ec1));
		assertTrue(query.containEntityClass(ec2));
		
		// D
		
		whereField1.setFieldEntityClassForLeftCondition(ec1);
		whereFieldsList.add(whereField1);
		whereClause.setWhereFields(whereFieldsList);
		query.setWhereClause(whereClause);
		
		query.purgeNotReferredEntityClasses();
		
		assertTrue(query.containEntityClass(ec1));
		assertTrue(query.containEntityClass(ec2));
		
		// E 
		
		whereClause.delWhereField(whereField1);
		query.setWhereClause(whereClause);
		
		query.purgeNotReferredEntityClasses();
		
		assertFalse(query.containEntityClass(ec1));
		assertTrue(query.containEntityClass(ec2));
		
		// F
		whereField2.setFieldEntityClassForLeftCondition(ec2);
		whereField2.setFieldEntityClassForRightCondition(ec1);
		whereFieldsList.add(whereField2);
		whereClause.setWhereFields(whereFieldsList);
		query.setWhereClause(whereClause);
			
		query.purgeNotReferredEntityClasses();
		
		assertTrue(query.containEntityClass(ec1));
		assertTrue(query.containEntityClass(ec2));
				
		// G 
		
		whereClause.delWhereField(whereField2);
		query.setWhereClause(whereClause);
		
		query.purgeNotReferredEntityClasses();
		
		assertFalse(query.containEntityClass(ec1));
		assertTrue(query.containEntityClass(ec2));
		
		// H
		
		selectClause.delSelectField(selectField2);
		query.setSelectClause(selectClause);
		
		query.purgeNotReferredEntityClasses();
		
		assertFalse(query.containEntityClass(ec1));
		assertFalse(query.containEntityClass(ec2));
		
		
	}

}
