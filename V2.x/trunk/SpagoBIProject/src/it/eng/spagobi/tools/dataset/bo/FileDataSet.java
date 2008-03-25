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
    
    
    public FileDataSet(){
    	super();
    }
    
    public FileDataSet(DataSet a) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
	}

	public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
