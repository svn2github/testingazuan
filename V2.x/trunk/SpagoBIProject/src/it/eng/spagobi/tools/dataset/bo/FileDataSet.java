/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class FileDataSet extends DataSet {
    private String fileName=null;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
