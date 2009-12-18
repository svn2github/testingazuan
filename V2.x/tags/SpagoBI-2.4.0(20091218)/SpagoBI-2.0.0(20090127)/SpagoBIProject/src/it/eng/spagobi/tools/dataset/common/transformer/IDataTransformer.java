/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.transformer;

import java.util.List;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataTransformer {

	
    List transformData(List records);
    
    List transformData(List records, String pivotColumn,  String pivotRow, String pivotValue);
}
