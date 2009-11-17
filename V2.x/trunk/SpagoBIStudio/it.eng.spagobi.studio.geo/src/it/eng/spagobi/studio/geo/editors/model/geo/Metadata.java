package it.eng.spagobi.studio.geo.editors.model.geo;

import java.util.Vector;

public class Metadata {
	private Vector<Column> column;
	private String dataset;

	public Vector<Column> getColumn() {
		return column;
	}

	public void setColumn(Vector<Column> column) {
		this.column = column;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}


}
