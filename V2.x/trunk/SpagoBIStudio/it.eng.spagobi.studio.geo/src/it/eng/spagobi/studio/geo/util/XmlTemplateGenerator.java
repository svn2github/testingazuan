package it.eng.spagobi.studio.geo.util;

import it.eng.spagobi.studio.geo.editors.model.geo.Colours;
import it.eng.spagobi.studio.geo.editors.model.geo.Column;
import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.Dataset;
import it.eng.spagobi.studio.geo.editors.model.geo.Datasource;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchies;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchy;
import it.eng.spagobi.studio.geo.editors.model.geo.KPI;
import it.eng.spagobi.studio.geo.editors.model.geo.Layer;
import it.eng.spagobi.studio.geo.editors.model.geo.Layers;
import it.eng.spagobi.studio.geo.editors.model.geo.Level;
import it.eng.spagobi.studio.geo.editors.model.geo.Levels;
import it.eng.spagobi.studio.geo.editors.model.geo.MapProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.MapRenderer;
import it.eng.spagobi.studio.geo.editors.model.geo.Measures;
import it.eng.spagobi.studio.geo.editors.model.geo.Metadata;
import it.eng.spagobi.studio.geo.editors.model.geo.Param;
import it.eng.spagobi.studio.geo.editors.model.geo.Tresholds;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

public class XmlTemplateGenerator {


	public static void setAlias(XStream xstream){
		xstream.alias("MAP", GEODocument.class);
		
		xstream.aliasField("MAP_PROVIDER", GEODocument.class, "mapProvider"); 
		xstream.useAttributeFor(MapProvider.class, "className");
		xstream.aliasField("class_name", MapProvider.class, "className");
		xstream.useAttributeFor(MapProvider.class, "mapName");
		xstream.aliasField("map_name", MapProvider.class, "mapName");

		xstream.aliasField("DATAMART_PROVIDER", GEODocument.class, "datamartProvider"); 
		xstream.useAttributeFor(DatamartProvider.class, "className");
		xstream.aliasField("class_name", DatamartProvider.class, "className");
		xstream.useAttributeFor(DatamartProvider.class, "hierarchy");
		xstream.aliasField("hierarchy", DatamartProvider.class, "hierarchy");
		xstream.useAttributeFor(DatamartProvider.class, "level");
		xstream.aliasField("level", DatamartProvider.class, "level");
		/**figli di datamart provider**/
			xstream.aliasField("DATASET", DatamartProvider.class, "dataset"); 
				xstream.aliasField("QUERY", Dataset.class, "query");
				
				xstream.aliasField("DATASOURCE", Dataset.class, "datasource");
					xstream.useAttributeFor(Datasource.class, "type");
					xstream.aliasField("type", Datasource.class, "type");
					xstream.useAttributeFor(Datasource.class, "driver");
					xstream.aliasField("driver", Datasource.class, "driver");
					xstream.useAttributeFor(Datasource.class, "url");
					xstream.aliasField("url", Datasource.class, "url");
					xstream.useAttributeFor(Datasource.class, "user");
					xstream.aliasField("user", Datasource.class, "user");
					xstream.useAttributeFor(Datasource.class, "password");
					xstream.aliasField("password", Datasource.class, "password");
			
			xstream.aliasField("METADATA", DatamartProvider.class, "metadata"); 
			xstream.useAttributeFor(Metadata.class, "dataset");
			xstream.aliasField("dataset", Metadata.class, "dataset");
			xstream.addImplicitCollection(Metadata.class, "column", "COLUMN", Column.class);

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
					xstream.useAttributeFor(Column.class, "choosenForTemplate");
					xstream.aliasField("choosenForTemplate", Column.class, "choosenForTemplate");
					
					xstream.aliasField("HIERARCHIES", DatamartProvider.class, "hierarchies");
					xstream.addImplicitCollection(Hierarchies.class, "hierarchy", "HIERARCHY", Hierarchy.class);

			xstream.useAttributeFor(Hierarchy.class, "name");
			xstream.aliasField("name", Hierarchy.class, "name");
			xstream.useAttributeFor(Hierarchy.class, "type");
			xstream.aliasField("type", Hierarchy.class, "type");
				xstream.aliasField("LEVELS", Hierarchy.class, "levels");
				xstream.addImplicitCollection(Levels.class, "level", "LEVEL", Level.class);

					xstream.useAttributeFor(Level.class, "name");
					xstream.aliasField("name", Level.class, "name");
					xstream.useAttributeFor(Level.class, "columnId");
					xstream.aliasField("column_id", Level.class, "columnId");
					xstream.useAttributeFor(Level.class, "columnDesc");
					xstream.aliasField("column_desc", Level.class, "columnDesc");
					xstream.useAttributeFor(Level.class, "featureName");
					xstream.aliasField("feature_name", Level.class, "featureName");
		
		xstream.aliasField("MAP_RENDERER", GEODocument.class, "mapRenderer");
		xstream.useAttributeFor(MapRenderer.class, "className");
		xstream.aliasField("class_name", MapRenderer.class, "className");
		xstream.aliasField("MEASURES", MapRenderer.class, "measures");
			xstream.addImplicitCollection(Measures.class, "kpi", "KPI", KPI.class);
			xstream.useAttributeFor(KPI.class, "columnId");
			xstream.aliasField("column_id", KPI.class, "columnId");	
			xstream.useAttributeFor(KPI.class, "description");
			xstream.aliasField("description", KPI.class, "description");	
			xstream.useAttributeFor(KPI.class, "aggFunct");
			xstream.aliasField("agg_funct", KPI.class, "aggFunct");	
			xstream.useAttributeFor(KPI.class, "color");
			xstream.aliasField("colour", KPI.class, "color");	
			xstream.aliasField("TRESHOLDS", KPI.class, "tresholds");
				xstream.useAttributeFor(Tresholds.class, "type");
				xstream.aliasField("type", Tresholds.class, "type");
				xstream.useAttributeFor(Tresholds.class, "lbValue");
				xstream.aliasField("lb_value", Tresholds.class, "lbValue");
				xstream.useAttributeFor(Tresholds.class, "ubValue");
				xstream.aliasField("ub_value", Tresholds.class, "ubValue");
				xstream.aliasField("PARAM", Tresholds.class, "param");
					xstream.useAttributeFor(Param.class, "name");
					xstream.aliasField("name", Param.class, "name");
					xstream.useAttributeFor(Param.class, "value");
					xstream.aliasField("value", Param.class, "value");
			
			xstream.aliasField("COLOURS", KPI.class, "colours");
				xstream.useAttributeFor(Colours.class, "type");
				xstream.aliasField("type", Colours.class, "type");
				xstream.useAttributeFor(Colours.class, "outboundColour");
				xstream.aliasField("outbound_colour", Colours.class, "outboundColour");
				xstream.useAttributeFor(Colours.class, "nullValuesColor");
				xstream.aliasField("null_values_color", Colours.class, "nullValuesColor");
				xstream.aliasField("PARAM", Colours.class, "param");
					xstream.useAttributeFor(Param.class, "name");
					xstream.aliasField("name", Param.class, "name");
					xstream.useAttributeFor(Param.class, "value");
					xstream.aliasField("value", Param.class, "value");

		xstream.aliasField("LAYERS", MapRenderer.class, "layers");
		xstream.useAttributeFor(Layers.class, "mapName");
		xstream.aliasField("mapName", Layers.class, "mapName");
		xstream.addImplicitCollection(Layers.class, "layer", "LAYER", Layer.class);
			xstream.useAttributeFor(Layer.class, "name");
			xstream.aliasField("name", Layer.class, "name");
			xstream.useAttributeFor(Layer.class, "defaultFillColour");
			xstream.aliasField("default_fill_color", Layer.class, "defaultFillColour");
			xstream.useAttributeFor(Layer.class, "description");
			xstream.aliasField("description", Layer.class, "description");
			xstream.useAttributeFor(Layer.class, "selected");
			xstream.aliasField("selected", Layer.class, "selected");
			xstream.useAttributeFor(Layer.class, "choosenForTemplate");
			xstream.aliasField("choosenForTemplate", Layer.class, "choosenForTemplate");
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
