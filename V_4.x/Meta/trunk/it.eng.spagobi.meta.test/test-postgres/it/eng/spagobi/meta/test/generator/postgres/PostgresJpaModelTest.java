/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.generator.postgres;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaRelationship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaModel;
import it.eng.spagobi.meta.generator.utils.JavaKeywordsUtils;
import it.eng.spagobi.meta.test.AbstractSpagoBIMetaTest;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.generator.AbstractJpaMappingGenerationTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;


public class PostgresJpaModelTest extends AbstractSpagoBIMetaTest {

	static JpaModel jpaModel;
	static JpaMappingCodeGenerator jpaMappingCodeGenerator;
	
	public void setUp() throws Exception {
		try {
			// if this is the first test on postgres after the execution
			// of tests on an other database force a tearDown to clean
			// and regenerate properly all the static variables contained in
			// parent class AbstractSpagoBIMetaTest
			System.out.println(dbType);
			if(dbType != TestCostants.DatabaseType.POSTGRES){
				doTearDown();
			}
			super.setUp();
			
			if(dbType == null) dbType = TestCostants.DatabaseType.POSTGRES;
						
			if(rootModel == null) {
				setRootModel( TestModelFactory.createModel( dbType ) );
			}
			
			if(jpaModel == null)  {
				jpaModel = new JpaModel(businessModel);
				jpaMappingCodeGenerator = TestGeneratorFactory.createCodeGeneraor();
				generator = jpaMappingCodeGenerator;
			}
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	// @see SPAGOBI-831
	public void testTableClassNames() {
		Map<String, IJpaTable> classNames = new HashMap<String, IJpaTable>();
		Assert.assertEquals(businessModel.getBusinessTables().size(), jpaModel.getTables().size());
		
		for(IJpaTable table : jpaModel.getTables()) {
			String className = table.getClassName();
			
			Assert.assertNotNull(className);
			Assert.assertTrue(className.trim().length() > 0);
			
			char firstChar = className.charAt(0);
			Assert.assertTrue("The name [" + className + "] of the class associated to table [" + table.getName() + "] does not start with a letter", Character.isLetter(firstChar));
			Assert.assertTrue("The name [" + className + "] of the class associated to table [" + table.getName() + "] does not start with an uppercase letter", Character.isUpperCase(firstChar));
			
			Assert.assertTrue("The name [" + className + "] of the class associated to table [" + table.getName() + "] is not a valid java identifier", JavaKeywordsUtils.isValidJavaIdentifier(className));
			
			IJpaTable t = classNames.get(className);
			String tName = t == null? null: t.getName();
			
			Assert.assertFalse("The name [" + className + "] of the class associated to table [" + table.getName() + "] " +
					"is already used by class associated to table [" + tName + "]"
					, classNames.containsKey(className));
			classNames.put(className, table);
			
			if(table.hasCompositeKey()) {
				className = table.getCompositeKeyClassName();
				t = classNames.get(className);
				tName = t == null? null: t.getName();
				Assert.assertFalse("The name [" + className + "] of the composite key class associated to table [" + table.getName() + "] " +
						"is already used by class associated to table [" +tName + "]"
						, classNames.containsKey(className));
				classNames.put(className, table);
			}
			
		}
	}
	
	// @see SPAGOBI-825 & SPAGOBI-831
	public void testColumnPropertyNames() {
		for(IJpaTable table : jpaModel.getTables()) {
			Map<String, String> propertyNames = new HashMap<String, String>();
			String propertyName = null;
			
			for(IJpaColumn column : table.getSimpleColumns(true, true, false)) {
				propertyName = column.getPropertyName();
				
				Assert.assertTrue("The name [" + propertyName + "] of the property associated to column [" + column.getColumnNameDoubleQuoted() + "] in table [" + table.getName() + "] is not a valid java identifier", JavaKeywordsUtils.isValidJavaIdentifier(propertyName));
				
				
				Assert.assertFalse("In table [" + table.getName() + "] the name [" + propertyName + "] " +
						"of the property associated to column [" + column.getColumnNameDoubleQuoted() + "] " +
						"is already used by property associated to column [" + propertyNames.get(propertyName) + "]"
						, propertyNames.containsKey(propertyName));
				propertyNames.put(propertyName, column.getColumnNameDoubleQuoted());
			}
			
			// test composed key property name
			if(table.hasCompositeKey()) {
				propertyName = table.getCompositeKeyPropertyName();
				Assert.assertFalse("In table [" + table.getName() + "] the name [" + propertyName + "] " +
						"of the property associated to the composed primary key " +
						"is already used by property associated to column [" + propertyNames.get(propertyName) + "]"
						, propertyNames.containsKey(propertyName));
				propertyNames.put(propertyName, "COMPOSED PRIMARY KEY");
			}
			
			// @see SPAGOBI-825
			for(IJpaRelationship relationship : table.getRelationships()) {
				if(relationship.getCardinality().equalsIgnoreCase("many-to-one")) {
					propertyName = relationship.getPropertyName();
					Assert.assertFalse("In table [" + table.getName() + "] the name [" + propertyName + "] " +
							"of the property associated to the foreign key [" + relationship.getDescription()+ "] " +
							"is already used by property associated to column [" + propertyNames.get(propertyName) + "]"
							, propertyNames.containsKey(propertyName));
					propertyNames.put(propertyName, "FOREIGN KEY (" + relationship.getDescription() + ")");
				}
				relationship.getPropertyName();
			}			
		}
	}
}
