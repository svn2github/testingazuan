/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.jpalo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import it.eng.spagobi.services.content.bo.Content;
import sun.misc.BASE64Decoder;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JPaloEngineTemplate {
	private Content rowTemplate;
	private Document parsedTemplateContent;
	
	private static final BASE64Decoder DECODER = new BASE64Decoder();
	
	public JPaloEngineTemplate(Content template) throws DocumentException, IOException {
		setRowTemplate(template);
		parseTemplateContent();
	}

	protected Content getRowTemplate() {
		return rowTemplate;
	}

	protected void setRowTemplate(Content rowTemplate) {
		this.rowTemplate = rowTemplate;
	}
	
	public byte[] getContentAsByteArray() throws IOException {		
		return DECODER.decodeBuffer( rowTemplate.getContent() );
	}
	
	public InputStream getContentAsInputStream() throws IOException {
		return new ByteArrayInputStream( getContentAsByteArray() );
	}
	
	protected void parseTemplateContent() throws IOException, DocumentException {
		SAXReader reader = new org.dom4j.io.SAXReader();
		parsedTemplateContent = reader.read( getContentAsInputStream() );
	}
	
	public String getDatabaseName() {	
		String result = null;
		Node rootNode = parsedTemplateContent.selectSingleNode("//olap");
	    if (rootNode != null) {
	    	result = rootNode.valueOf("@database");	    	
	    }
		return result;
	}
	
	public String getSchemaName() {		
		String result = null;
		Node rootNode = parsedTemplateContent.selectSingleNode("//olap");
	    if (rootNode != null) {
	    	result = rootNode.valueOf("@schema");	    	
	    }
		return result;
	}
	
	public String getCubeName() {		
		String result = null;
		Node rootNode = parsedTemplateContent.selectSingleNode("//olap");
	    if (rootNode != null) {
	    	result = rootNode.valueOf("@cube");	    	
	    }
		return result;
	}

	public String getTableOnly() {
		String result = null;
		Node rootNode = parsedTemplateContent.selectSingleNode("//olap");
	    if (rootNode != null) {
	    	result = rootNode.valueOf("@tableonly");	    	
	    }
	    if(result == null || result.equalsIgnoreCase("")) result = "false";
		return result;
	}

	public String getEditorOnly() {
		String result = null;
		Node rootNode = parsedTemplateContent.selectSingleNode("//olap");
	    if (rootNode != null) {
	    	result = rootNode.valueOf("@editoronly");	    	
	    }
	    if(result == null || result.equalsIgnoreCase("")) result = "true";
		return result;
	}
	
	
}
