/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.transformer;

import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMeta;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class PivotingTransformer implements IDataTransformer {
	private static transient Logger logger = Logger.getLogger(PivotingTransformer.class);
    
	/**
	 * Gets the result of dataset and execute a pivot operation for split data on row, column and value specified
	 * into dataset interface.
     * The dataset has a structure like: ROW | SER | VAL | other info
	 */
    public List transformData(List records, String pivotColumn, String pivotRow, String pivotValue){
    	logger.info("IN");
	    
    	List newRecords = new ArrayList();
	    IRecord newRecord = null;
	    String newFName = null;
	    String newFValue = null;
	    
	    Iterator i = records.iterator();
	    boolean sameRow = true;
	    String rowValue = "";
	    newRecord = (IRecord)new Record();

    	while (i.hasNext()){
			IRecord r =(IRecord) i.next();		
			List fields = r.getFields();
			Iterator it = fields.iterator();
			while(it.hasNext()){		
				//gets every fields of the record 
				IField f = (IField)it.next();
				IFieldMeta fMeta = (IFieldMeta)f.getMetadata();
				
				String fieldName = fMeta.getName();
				String fieldValue ="";
				String objType = f.getValue().getClass().getName();
				if(objType.equals("java.lang.String")){
					fieldValue = (String) f.getValue();
				}else{
					SourceBeanAttribute fieldObject =(SourceBeanAttribute) f.getValue();
					fieldValue = fieldObject.getValue().toString();
				}
				//checks if the field is a row, a column or a value specified into configuration and manages them
				if (fieldName.equalsIgnoreCase(pivotRow)){
					if (rowValue.equals("")) rowValue = fieldValue;
					if (!(rowValue.trim()).equalsIgnoreCase(fieldValue.trim())){
						rowValue = fieldValue;
						newRecords.add(newRecord);
						newRecord = (IRecord)new Record();
					}
					if (newRecord.getFieldByName(fieldName) == null)
						newRecord.appendField(f);
				}
				else if (fieldName.equalsIgnoreCase(pivotColumn)){
					newFName = fieldValue;
					IField fv = r.getFieldByName(pivotValue);
					if (fv == null){
						logger.error("Pivot value column '"+ pivotValue +"' not found into dataset. Pivot not applicated!");
						return null;
					}
					SourceBeanAttribute newFObject =(SourceBeanAttribute) fv.getValue();
					newFValue = newFObject.getValue().toString();
					fMeta.setName(newFName);
					IField newf = (IField)new Field(fMeta,newFValue);
					newRecord.appendField(newf);
				}
				else if (fieldName.equalsIgnoreCase(pivotValue)){
					//skip field
				}
				else {
					//if the field isn't into record comes added
					if (newRecord.getFieldByName(fieldName) == null)
						newRecord.appendField(f);
				}
			}	    	
		}
    	//Adds the last record
		if (newRecord != null) newRecords.add(newRecord);
	    
	    logger.info("OUT");
    	return newRecords;
    }
    
    public List transformData(List records){
    	logger.info("IN");
    	logger.debug("Method not implemented");
    	logger.info("OUT");
    	return null;
    }
}
