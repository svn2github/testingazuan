/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

import java.util.List;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IRecord {

    IField getFieldByName(String name);
    IField getFieldById(int position);
    public void appendField(IField f) ;
    public void insertField(int position, IField f) ;
    public List getFields();
	public void setFields(List fields);
    
}
