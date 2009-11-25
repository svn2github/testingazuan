package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.Column;
import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Metadata;

import java.util.Vector;

public class ColumnBO {
	public static Column setNewColumn(GEODocument geoDocument, 
									String columnId, String datasetName){
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		Metadata metadata =dmProvider.getMetadata();
		if(metadata == null){
			metadata = new Metadata();
			metadata.setDataset(datasetName);
			dmProvider.setMetadata(metadata);
		}
		Vector<Column> columns = metadata.getColumn();
		if(columns== null){
			columns = new Vector<Column>();
			metadata.setColumn(columns);
		}
		Column column = new Column();
		column.setColumnId(columnId);
		
		columns.add(column);
		return column;

	}
	public static Column getColumnByName(GEODocument geoDocument, 
			String columnId){
		Column column = null;
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		Metadata metadata =dmProvider.getMetadata();
		Vector<Column> columns = metadata.getColumn();
		if(columns != null){
			for(int i=0; i<columns.size(); i++){
				if(columns.elementAt(i).getColumnId().equals(columnId)){
					column=columns.elementAt(i);
				}
				
			}
		}
		return column;
	}

}
