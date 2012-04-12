package it.eng.spagobi.meta.test.generator.mysql;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaRelationship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaModel;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaTable;
import it.eng.spagobi.meta.generator.utils.JavaKeywordsUtils;
import it.eng.spagobi.meta.initializer.descriptor.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestGeneratorFactory;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.generator.AbstractJpaMappingGenerationTest;
import it.eng.spagobi.meta.test.utils.ModelManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;


public class MySqlJpaModelTest extends AbstractJpaMappingGenerationTest {

	static JpaModel jpaModel;
	static JpaMappingCodeGenerator jpaMappingCodeGenerator;
	
	public void setUp() throws Exception {
		super.setUp();
		try {
			if(dbType == null) dbType = TestCostants.DatabaseType.MYSQL;
			
			if(rootModel == null) {
				rootModel = TestModelFactory.createModel( dbType );
				if(rootModel != null && rootModel.getPhysicalModels() != null && rootModel.getPhysicalModels().size() > 0) {
					physicalModel = rootModel.getPhysicalModels().get(0);
				}
				if(rootModel != null && rootModel.getBusinessModels() != null && rootModel.getBusinessModels().size() > 0) {
					businessModel = rootModel.getBusinessModels().get(0);
				}
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
