package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.geo.Column;
import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Layer;
import it.eng.spagobi.studio.geo.editors.model.geo.Layers;
import it.eng.spagobi.studio.geo.editors.model.geo.MapProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.MapRenderer;
import it.eng.spagobi.studio.geo.editors.model.geo.Metadata;
import it.eng.spagobi.studio.geo.util.XmlTemplateGenerator;

import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class ModelBO {
	public GEODocument createModel(IFile file) throws CoreException{
		GEODocument geoDocument = XmlTemplateGenerator.readXml(file);
		if(geoDocument.getMapProvider()==null){
			geoDocument.setMapProvider(new MapProvider());
		}
		if(geoDocument.getDatamartProvider()==null){
			geoDocument.setDatamartProvider(new DatamartProvider());
		}
		if(geoDocument.getMapRenderer()==null){
			geoDocument.setMapRenderer(new MapRenderer());
		}
		return geoDocument;
	}
	public void cleanGEODocument(GEODocument geoDocumentToSaveOnFile){
		DatamartProvider dmProvider = geoDocumentToSaveOnFile.getDatamartProvider();
		Metadata metadata = dmProvider.getMetadata();
		
		if(metadata != null){
			Vector<Column> colToRemove= new Vector<Column>();
			Vector<Column> columns = metadata.getColumn();
			if(columns != null){

				for(int j=0; j<columns.size(); j++){
					Column col = columns.elementAt(j);
					if(!col.isChoosenForTemplate()){
						//columns.remove(col);
						colToRemove.add(col);
					}
				}
				System.out.println(colToRemove.size());
				columns.removeAll(colToRemove);
				System.out.println("columns left on doc :"+columns.size());
			}
		}
		MapRenderer mapRenderer = geoDocumentToSaveOnFile.getMapRenderer();
		Layers layers = mapRenderer.getLayers();
		if(layers != null){
			Vector<Layer> layToRemove= new Vector<Layer>();
			Vector<Layer> layerVect = layers.getLayer();
			if(layerVect != null){
		
				for(int j=0; j<layerVect.size(); j++){
					Layer layer = layerVect.elementAt(j);
					if(!layer.isChoosenForTemplate()){
						//layerVect.remove(layer);
						layToRemove.add(layer);
					}
				}
				System.out.println(layToRemove.size());
				layerVect.removeAll(layToRemove);
				System.out.println("layers left on doc :"+layerVect.size());
			}
		}
	}
	public void saveModel(GEODocument geoDocument){
		Activator.getDefault().setGeoDocument(geoDocument);
	}

	public GEODocument getModel(){
		return Activator.getDefault().getGeoDocument();
	}

}
