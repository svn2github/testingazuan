/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataSet {

    /**
     * return the Metadata of the DataSet
     * @return IDataSetMetaData
     */
    IDataSetMetaData getMetaData();
}
