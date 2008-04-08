/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class WSDataSet extends DataSet {
    private String adress=null;
    private String operation=null;
    private String executorClass=null;
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
	public WSDataSet(DataSet a) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
	}
	public WSDataSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getOperation() {
	    return operation;
	}
	public void setOperation(String operation) {
	    this.operation = operation;
	}
	
    
    
}
