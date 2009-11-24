package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.Column;
import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Metadata;

import java.util.Vector;

public class MetadataBO {
	
	public static void setNewMetadata(GEODocument geoDocument, String datasetName) {
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		Metadata metadata = new Metadata();
		metadata.setDataset(datasetName);
		dmProvider.setMetadata(metadata);

		//add columns
		Vector<Column> column= new Vector<Column>();
		metadata.setColumn(column);
	}	
	public static Metadata getMetadata(GEODocument geoDocument){
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		return dmProvider.getMetadata();
	}
	public static boolean geoidColumnExists(GEODocument geoDocument){
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		Metadata metadata = dmProvider.getMetadata();
		if(metadata != null && metadata.getColumn() != null){
			for(int i=0; i<metadata.getColumn().size(); i++){
				Column col = metadata.getColumn().elementAt(i);
				if(col.getType() != null && col.getType().equalsIgnoreCase("geoid")){
					return true;
				}
			}
		}
		return false;
	}
}
