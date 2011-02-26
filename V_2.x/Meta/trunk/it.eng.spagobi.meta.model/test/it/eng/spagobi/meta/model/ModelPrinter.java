/**
 * 
 */
package it.eng.spagobi.meta.model;

import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

/**
 * @author Andrea Gioia (andrea.gioi@eng.it)
 *
 */
public class ModelPrinter {
	public static void print(PhysicalModel model) {
		
	}
	
	public static void print(BusinessModel model) {
		for(int i = 0; i < model.getTables().size(); i++) {
        	BusinessColumnSet t = model.getTables().get(i);
        	System.out.println( i + " " + t.getName() );
        	for(int j = 0; j < t.getColumns().size(); j++) {
        		System.out.println( "  -  " + t.getColumns().get(j).getName());
        	}
        }
		
		 /*
        for(int i = 0; i < businessModel.getRelationships().size(); i++) {
        	BusinessRelationship r = businessModel.getRelationships().get(i);
        	
        	System.out.println( "(" + r.getName() + ") " +r.getSourceTable().getName() + " -> " + r.getDestinationTable().getName() );
        	for(int j = 0; j < r.getSourceColumns().size(); j++) {
        		System.out.println( "  -  " + r.getSourceColumns().get(j).getName() + " -> "+ r.getDestinationColumns().get(j).getName() );
        	}
        }
        */
	}
}
