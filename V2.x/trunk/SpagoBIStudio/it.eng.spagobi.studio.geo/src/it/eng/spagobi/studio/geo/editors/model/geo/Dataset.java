package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Dataset  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4381699831115967736L;
	private Datasource datasource;
	private String query;
	
	public Datasource getDatasource() {
		return datasource;
	}
	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}


}
