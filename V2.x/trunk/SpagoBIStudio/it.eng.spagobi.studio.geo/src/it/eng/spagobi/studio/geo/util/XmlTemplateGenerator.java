package it.eng.spagobi.studio.geo.util;

import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.MapProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.MapRenderer;

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
		
		xstream.aliasField("MAP_RENDERER", MapRenderer.class, "mapRenderer");
		xstream.useAttributeFor(MapRenderer.class, "className");
		xstream.aliasField("class_name", MapRenderer.class, "className");
		/*
		xstream.useAttributeFor(DocumentsConfiguration.class, "videoHeight");
		xstream.aliasField("video_height", DocumentsConfiguration.class, "videoHeight");

		xstream.addImplicitCollection(DocumentsConfiguration.class, "documents", "DOCUMENT", Document.class);

		xstream.useAttributeFor(Document.class, "sbiObjLabel");
		xstream.aliasField("sbi_obj_label", Document.class, "sbiObjLabel");

		try{
			xstream.useAttributeFor(Document.class, "localFileName");
			xstream.aliasField("local_file_name", Document.class, "localFileName");
		}
		catch (Exception e) {
			// if not treated		
			}

			xstream.aliasField("STYLE", Document.class, "style");
			xstream.useAttributeFor(Style.class, "style");
			xstream.useAttributeFor(Style.class, "mode");
			xstream.aliasField("style", Style.class, "style");        

			xstream.aliasField("PARAMETERS", Document.class, "parameters");

			xstream.addImplicitCollection(Parameters.class, "parameter", "PARAMETER", Parameter.class);

			xstream.omitField(Parameter.class, "bo");
			xstream.useAttributeFor(Parameter.class, "navigationName");
			xstream.aliasField("navigationName", Parameter.class, "navigationName");

			xstream.useAttributeFor(Parameter.class, "type");
			xstream.aliasField("type", Parameter.class, "type");
			
			xstream.useAttributeFor(Parameter.class, "id");
			xstream.aliasField("id", Parameter.class, "id");

			xstream.useAttributeFor(Parameter.class, "sbiParLabel");
			xstream.aliasField("sbi_par_label", Parameter.class, "sbiParLabel");

			xstream.useAttributeFor(Parameter.class, "defaultVal");
			xstream.aliasField("default_value", Parameter.class, "defaultVal");

			xstream.aliasField("REFRESH", Parameter.class, "refresh");

			xstream.addImplicitCollection(Refresh.class, "refreshDocLinked", "REFRESH_DOC_LINKED", RefreshDocLinked.class);

			xstream.useAttributeFor(RefreshDocLinked.class, "labelDoc");
			xstream.aliasField("labelDoc", RefreshDocLinked.class, "labelDoc");

			xstream.useAttributeFor(RefreshDocLinked.class, "labelParam");
			xstream.aliasField("labelParam", RefreshDocLinked.class, "labelParam");
			
			xstream.useAttributeFor(RefreshDocLinked.class, "idParam");
			xstream.aliasField("idParam", RefreshDocLinked.class, "idParam");*/

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
