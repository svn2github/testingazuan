/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class Record implements IRecord {


    public IField getFieldById(int position) {
    	IField field = (IField)new Field();
    	
    	return field;
    }


    public IField getFieldByName(String name) {
    	IField field = (IField)new Field();
	
	return field;
    }

}
