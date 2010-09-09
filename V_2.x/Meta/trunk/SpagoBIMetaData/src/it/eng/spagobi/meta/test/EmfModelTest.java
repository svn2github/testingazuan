/**
 * 
 */
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;

/**
 * @author agioia
 *
 */
public class EmfModelTest {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PhysicalModelFactory factory = PhysicalModelFactory.eINSTANCE;
		 
		PhysicalModel model = factory.createPhysicalModel();
		model.setName("test-model");
		 
		PhysicalTable table1, table2;
		PhysicalColumn column11, column12, column21;
		 
		 // create table 1
		 table1 = factory.createPhysicalTable();
		 table1.setName("Customer");
		 
		 column11 = factory.createPhysicalColumn();
		 column11.setName("fullname");
		 table1.getColumns().add(column11);
		 
		 column12 = factory.createPhysicalColumn();
		 column12.setName("region");
		 table1.getColumns().add(column12);
		 
		 model.getTables().add(table1);
		 
		// create table 2
		 
		 table2 = factory.createPhysicalTable();
		 table2.setName("Region");
		 
		 column21 = factory.createPhysicalColumn();
		 column21.setName("id");
		 table2.getColumns().add(column21);
		 
		 model.getTables().add(table2);
		
		 
		// create fk
		 
		 PhysicalForeignKey fk = factory.createPhysicalForeignKey();
		 //fk.addRelation(column12, column21);
		 fk.setSourceTable(table1);
		 fk.setDestinationTable(table2);
		 fk.getSourceColumns().add(column12);
		 fk.getDestinationColumns().add(column21);
		 model.getForeignKeys().add(fk);
		 
		 
		 // TEST
		 
		 System.out.println("fk.getModel().getName(): " + fk.getModel().getName());
		 System.out.println("table1.getForeignKeys().size(): " + table1.getForeignKeys().size());
		 System.out.println("table1.getReverseForeignKeys().size(): " + table1.getReverseForeignKeys().size());
		 System.out.println("table2.getForeignKeys().size(): " + table2.getForeignKeys().size());
		 System.out.println("table2.getReverseForeignKeys().size(): " + table2.getReverseForeignKeys().size());
		 System.out.println("column11.getTable().getName(): " + column11.getTable().getName());
		 System.out.println("column12.getTable().getName(): " + column11.getTable().getName());
		 System.out.println("column21.getTable().getName(): " + column21.getTable().getName());

	}
}
