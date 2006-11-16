package it.eng.spagobi.bean;

import it.eng.spagobi.utilities.GenericSavingException;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.tonbeller.jpivot.olap.model.OlapModel;
import com.tonbeller.jpivot.olap.navi.MdxQuery;
import com.tonbeller.wcf.controller.RequestContext;

public class TemplateBean implements Serializable {
	
	private String templateName;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public void resetFields (){
		templateName = null;
	}

	public void saveTemplate(RequestContext reqContext){
		Logger logger = Logger.getLogger(this.getClass());
		HttpSession session = reqContext.getSession();
		String catalogUri = (String) session.getAttribute("catalogUri"); 
		String spagoBIBaseUrl = (String) session.getAttribute("spagobiurl");
		String path = (String) session.getAttribute("biobject_path");
		OlapModel olapModel = (OlapModel) session.getAttribute("query01");
		MdxQuery mdxQuery = (MdxQuery) olapModel.getExtension("mdxQuery");
		String query = mdxQuery.getMdxQuery();
//		if (olapModel.getBookmarkState(0) instanceof MondrianMemento) {
//			MondrianMemento olapMem = (MondrianMemento) olapModel.getBookmarkState(0);
//			query = olapMem.getMdxQuery();
//		}
		if (query != null) {
			String xmlString = "<olap>\n";
			xmlString += "	<cube reference='" + catalogUri + "' />\n";
			xmlString += "	<MDXquery>\n";
			xmlString += query;
			xmlString += "	</MDXquery>\n";
			xmlString += "</olap>";
			// controls that the produced String in a valid xml format
			Document document = null;
			try {
				SAXReader reader = new SAXReader();
				byte[] templateContent = xmlString.getBytes();
				ByteArrayInputStream is = new ByteArrayInputStream(templateContent);
				document = reader.read(is);
			} catch (Exception e) {
				logger.error("Error while parsing xml template " + xmlString, e);
				return;
			}
			xmlString = document.asXML();
		    SpagoBIAccessUtils sbiutils = new SpagoBIAccessUtils();
		    try {
		        sbiutils.saveObjectTemplate(spagoBIBaseUrl, path, templateName, xmlString);
		    } catch (GenericSavingException gse) {		
		    	logger.error("Error while saving template", gse);
		    }   
		} else {
			logger.error("Could not retrieve MDX query");
		}
	}
}
