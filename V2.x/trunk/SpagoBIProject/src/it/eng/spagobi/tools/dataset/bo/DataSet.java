/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

import it.eng.spagobi.tools.datasource.bo.DataSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSet {

	private int dsId;
    	private String name=null;
    	private String description=null;
    	private String label=null;
    	private String parameters=null;

    	
    	
	public int getDsId() {
			return dsId;
		}
		public void setDsId(int dsId) {
			this.dsId = dsId;
		}
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
    	
}
