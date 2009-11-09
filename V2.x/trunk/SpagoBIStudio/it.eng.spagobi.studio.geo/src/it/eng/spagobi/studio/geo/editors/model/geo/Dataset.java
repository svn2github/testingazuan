package it.eng.spagobi.studio.geo.editors.model.geo;

public class Dataset {
	private Datasource datasource;
	private Query query;
	
	public Datasource getDatasource() {
		return datasource;
	}
	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}

}
