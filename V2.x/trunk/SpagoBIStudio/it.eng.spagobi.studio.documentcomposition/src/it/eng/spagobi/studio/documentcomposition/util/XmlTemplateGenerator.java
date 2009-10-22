package it.eng.spagobi.studio.documentcomposition.util;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Refresh;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.RefreshDocLinked;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Style;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.model.Model;

import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

public class XmlTemplateGenerator {


	public static void setAlias(XStream xstream){
		xstream.alias("DOCUMENTS_COMPOSITION", DocumentComposition.class);
		xstream.useAttributeFor(DocumentComposition.class, "templateValue");
		xstream.aliasField("template_value", DocumentComposition.class, "templateValue");


		xstream.aliasField("DOCUMENTS_CONFIGURATION", DocumentComposition.class, "documentsConfiguration"); 
		xstream.useAttributeFor(DocumentsConfiguration.class, "videoWidth");
		xstream.aliasField("video_width", DocumentsConfiguration.class, "videoWidth");        

		xstream.useAttributeFor(DocumentsConfiguration.class, "videoHeight");
		xstream.aliasField("video_height", DocumentsConfiguration.class, "videoHeight");

		xstream.addImplicitCollection(DocumentsConfiguration.class, "documents", "DOCUMENT", Document.class);

		xstream.useAttributeFor(Document.class, "label");
		xstream.aliasField("label", Document.class, "label");

		xstream.useAttributeFor(Document.class, "sbiObjLabel");
		xstream.aliasField("sbi_obj_label", Document.class, "sbiObjLabel");

		xstream.aliasField("STYLE", Document.class, "style");
		xstream.useAttributeFor(Style.class, "style");
		xstream.aliasField("style", Style.class, "style");        

		xstream.aliasField("PARAMETERS", Document.class, "parameters");

		xstream.addImplicitCollection(Parameters.class, "parameter", "PARAMETER", Parameter.class);

		xstream.omitField(Parameter.class, "navigationName");

		xstream.useAttributeFor(Parameter.class, "label");
		xstream.aliasField("label", Parameter.class, "label");

		xstream.useAttributeFor(Parameter.class, "type");
		xstream.aliasField("type", Parameter.class, "type");

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

	}


	public static String transformToXml(Object bean) {


		XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("_", "_");
		XStream xstream = new XStream(new DomDriver("UTF-8", replacer)); 

		setAlias(xstream);	

		String xml = xstream.toXML(bean);
		System.out.println(xml);
		return xml;
	}


	public static Model readXml(IFile file) throws CoreException{
		XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("grfthscv", "_");
		XStream xstream = new XStream(new DomDriver("UTF-8", replacer)); 
		setAlias(xstream);	
		Model objFromXml = (Model)xstream.fromXML(file.getContents());
		return objFromXml;
	}



	public static void main(String[] args) {

		DocumentComposition docComp = new DocumentComposition();

		DocumentsConfiguration documentsConfiguration = new DocumentsConfiguration();

		RefreshDocLinked refreshDocLinked = new RefreshDocLinked();
		refreshDocLinked.setLabelDoc("doc1");
		refreshDocLinked.setLabelParam("i1");


		Vector rv = new Vector();
		rv.add(refreshDocLinked);

		Refresh refresh = new Refresh();
		refresh.setRefreshDocLinked(rv);

		Parameter i1= new Parameter();
		i1.setDefaultVal("");
		i1.setLabel("lab1");		
		i1.setSbiParLabel("sb1");
		i1.setType("IN");
		i1.setRefresh(refresh);

		Parameter i2= new Parameter();
		i2.setDefaultVal("");
		i2.setLabel("lab2");		
		i2.setSbiParLabel("sb2");
		i2.setType("IN");
		i2.setRefresh(refresh);

		Vector p = new Vector();
		p.add(i1);
		p.add(i2);

		Parameters parameters = new Parameters();
		parameters.setParameter(p);

		Style style = new Style();
		style.setStyle("float:left; width:49%;");

		Document doc1 = new Document();
		doc1.setLabel("label doc 1");
		doc1.setSbiObjLabel("sbi doc1 label");
		doc1.setStyle(style);
		doc1.setParameters(parameters);

		Document doc2 = new Document();
		doc2.setLabel("label doc 2");
		doc2.setSbiObjLabel("sbi doc2 label");
		doc2.setStyle(style);

		Document doc3 = new Document();
		doc3.setLabel("label doc 3");
		doc3.setSbiObjLabel("sbi doc3 label");
		doc3.setStyle(style);

		Vector docsVector = new Vector();
		docsVector.add(doc1);
		docsVector.add(doc2);
		docsVector.add(doc3);


		documentsConfiguration.setVideoWidth("1400");
		documentsConfiguration.setVideoHeight("1050");
		documentsConfiguration.setDocuments(docsVector);


		docComp.setTemplateValue("xxx.jsp");
		docComp.setDocumentsConfiguration(documentsConfiguration);



		transformToXml(docComp);
	}
}
