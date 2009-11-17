package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.Dataset;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

public class DatasetBO {
	
	public static Dataset setNewDataset(GEODocument geoDocument,
			String datasetQuery) {
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		Dataset dataset =dmProvider.getDataset();
		if(dataset == null){
			dataset = new Dataset();
			dataset.setQuery(datasetQuery);			
			dmProvider.setDataset(dataset);
		}
		if(dataset.getQuery() == null){

			dataset.setQuery(datasetQuery);
		}
		dataset.setQuery(datasetQuery);
		return dataset;
	}	

}
