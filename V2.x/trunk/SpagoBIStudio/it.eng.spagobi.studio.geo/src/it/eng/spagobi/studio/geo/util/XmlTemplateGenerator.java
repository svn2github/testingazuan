package it.eng.spagobi.studio.geo.util;

import it.eng.spagobi.studio.geo.editors.model.geo.Column;
import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchy;
import it.eng.spagobi.studio.geo.editors.model.geo.Level;
import it.eng.spagobi.studio.geo.editors.model.geo.MapProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.MapRenderer;
import it.eng.spagobi.studio.geo.editors.model.geo.Metadata;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

public class XmlTemplateGenerator {


	public static void setAlias(XStream xstream){
		xstream.alias("MAP", GEODocument.class);
		
		xstream.aliasField("MAP_PROVIDER", MapProvider.class, "mapProvider"); 
		xstream.useAttributeFor(MapProvider.class, "className");
		xstream.aliasField("class_name", MapProvider.class, "className");
		xstream.useAttributeFor(MapProvider.class, "mapName");
		xstream.aliasField("map_name", MapProvider.class, "mapName");

		xstream.aliasField("DATAMART_PROVIDER", DatamartProvider.class, "datamartProvider"); 
		xstream.useAttributeFor(DatamartProvider.class, "className");
		xstream.aliasField("class_name", DatamartProvider.class, "className");
		xstream.useAttributeFor(DatamartProvider.class, "hierarchy");
		xstream.aliasField("hierarchy", DatamartProvider.class, "hierarchy");
		xstream.useAttributeFor(DatamartProvider.class, "level");
		xstream.aliasField("level", DatamartProvider.class, "level");
		/**figli di datamart provider**/
			xstream.aliasField("METADATA", DatamartProvider.class, "metadata"); 
				xstream.aliasField("COLUMN", Metadata.class, "column");
					xstream.useAttributeFor(Column.class, "type");
					xstream.aliasField("type", Column.class, "type");
					xstream.useAttributeFor(Column.class, "columnId");
					xstream.aliasField("column_id", Column.class, "columnId");
					xstream.useAttributeFor(Column.class, "hierarchy");
					xstream.aliasField("hierarchy", Column.class, "hierarchy");
					xstream.useAttributeFor(Column.class, "level");
					xstream.aliasField("level", Column.class, "level");
					xstream.useAttributeFor(Column.class, "aggFunction");
					xstream.aliasField("agg_func", Column.class, "aggFunction");
			xstream.addImplicitCollection(DatamartProvider.class, "hierarchies", "HIERARCHY", Hierarchy.class);
			xstream.useAttributeFor(Hierarchy.class, "name");
			xstream.aliasField("name", Hierarchy.class, "name");
			xstream.useAttributeFor(Hierarchy.class, "type");
			xstream.aliasField("type", Hierarchy.class, "type");
				xstream.addImplicitCollection(Hierarchy.class, "levels", "LEVEL", Level.class);
					xstream.useAttributeFor(Level.class, "name");
					xstream.aliasField("name", Level.class, "name");
					xstream.useAttributeFor(Level.class, "columnId");
					xstream.aliasField("column_id", Level.class, "columnId");
					xstream.useAttributeFor(Level.class, "columnDesc");
					xstream.aliasField("column_desc", Level.class, "columnDesc");
					xstream.useAttributeFor(Level.class, "featureName");
					xstream.aliasField("feature_name", Level.class, "featureName");
		
		xstream.aliasField("MAP_RENDERER", MapRenderer.class, "mapRenderer");
		xstream.useAttributeFor(MapRenderer.class, "className");
		xstream.aliasField("class_name", MapRenderer.class, "className");
		
		

		}


		public static String transformToXml(Object bean) {


			XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("_", "_");
			XStream xstream = new XStream(new DomDriver("UTF-8", replacer)); 
			xstream.setMode(XStream.NO_REFERENCES);

			setAlias(xstream);	

			String xml = xstream.toXML(bean);
			System.out.println(xml);
			return xml;
		}


		public static GEODocument readXml(IFile file) throws CoreException{
			XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("grfthscv", "_");
			XStream xstream = new XStream(new DomDriver("UTF-8", replacer)); 
			setAlias(xstream);	
			GEODocument objFromXml = (GEODocument)xstream.fromXML(file.getContents());

			return objFromXml;
		}



		public static void main(String[] args) {

		}
	}
