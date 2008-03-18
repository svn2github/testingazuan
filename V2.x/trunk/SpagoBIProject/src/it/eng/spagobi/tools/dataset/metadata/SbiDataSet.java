/**
 * 
 */
package it.eng.spagobi.tools.dataset.metadata;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class SbiDataSet {
    	private int id;
	private String name=null;
    	private String description=null;
    	private String label=null;
    	private String parameters=null;
    	
	public String getParameters() {
	    return parameters;
	}
	public void setParameters(String parameters) {
	    this.parameters = parameters;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getDescription() {
	    return description;
	}
	public void setDescription(String description) {
	    this.description = description;
	}
	public String getLabel() {
	    return label;
	}
	public void setLabel(String label) {
	    this.label = label;
	}
	public int getId() {
	    return id;
	}
	public void setId(int id) {
	    this.id = id;
	}
    	
}
