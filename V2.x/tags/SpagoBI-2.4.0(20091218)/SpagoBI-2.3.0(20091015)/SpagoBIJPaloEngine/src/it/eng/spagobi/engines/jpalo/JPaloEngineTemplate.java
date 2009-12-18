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

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.utilities.StringUtilities;

import sun.misc.BASE64Decoder;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JPaloEngineTemplate {
	
	
	/**
	 * expected template structure:
	 *
	 * <olap database="Palo" 
     *  	 schema="Demo" 
     *		 cube="Sales">
	 * </olap>
	 */
	private SourceBean templateSB;
	
	private static final String DATABASE_ATTRIBUTE_NAME = "database";
	private static final String SCHEMA_ATTRIBUTE_NAME = "schema";
	private static final String CUBE_ATTRIBUTE_NAME = "cube";
	private static final String TABLEONLY_ATTRIBUTE_NAME = "tableonly";
	private static final String EDITORONLY_ATTRIBUTE_NAME = "editoronly";
	
	
	
	
	public JPaloEngineTemplate(SourceBean template) {
		setTemplateSB(template);
	}

	protected SourceBean getTemplateSB() {
		return templateSB;
	}

	protected void setTemplateSB(SourceBean templateSB) {
		this.templateSB = templateSB;
	}

	
	public String getDatabaseName() {	
		return (String)getTemplateSB().getAttribute( DATABASE_ATTRIBUTE_NAME );
	}
	
	public String getSchemaName() {		
		return (String)getTemplateSB().getAttribute( SCHEMA_ATTRIBUTE_NAME );
	}
	
	public String getCubeName() {		
		return (String)getTemplateSB().getAttribute( CUBE_ATTRIBUTE_NAME );
	}

	public String getTableOnly() {
		String result = null;
		
		result = (String)getTemplateSB().getAttribute( TABLEONLY_ATTRIBUTE_NAME );
		if(StringUtilities.isEmpty(result)) {
			result = "false";
		}
		
		return result;
	}

	public String getEditorOnly() {
		String result = null;
		
		result = (String)getTemplateSB().getAttribute( EDITORONLY_ATTRIBUTE_NAME );
		if(StringUtilities.isEmpty(result)) {
			result = "true";
		}
		
		return result;
	}
	
	
}
