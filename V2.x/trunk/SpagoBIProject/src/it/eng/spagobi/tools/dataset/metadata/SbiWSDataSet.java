/**
 * 
 */
package it.eng.spagobi.tools.dataset.metadata;


/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class SbiWSDataSet extends SbiDataSet{
    private String adress=null;
    private String executorClass=null;
    private String operation=null;
    
    public String getAdress() {
        return adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }
    public String getExecutorClass() {
        return executorClass;
    }
    public void setExecutorClass(String executorClass) {
        this.executorClass = executorClass;
    }
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
    
}
