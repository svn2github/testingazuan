/**
 * 
 */
package it.eng.spagobi.tools.dataset.metadata;


/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class SbiFileDataSet extends SbiDataSet{
    private String fileName=null;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
