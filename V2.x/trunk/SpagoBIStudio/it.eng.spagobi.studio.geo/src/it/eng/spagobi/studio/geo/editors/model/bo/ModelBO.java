package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.MapProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.MapRenderer;
import it.eng.spagobi.studio.geo.util.XmlTemplateGenerator;

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

	public void saveModel(GEODocument geoDocument){
		Activator.getDefault().setGeoDocument(geoDocument);
	}

	public GEODocument getModel(){
		return Activator.getDefault().getGeoDocument();
	}
}
