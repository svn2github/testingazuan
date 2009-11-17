package it.eng.spagobi.studio.geo.editors.model.geo;

public class Dataset {
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
