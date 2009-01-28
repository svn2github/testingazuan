/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.bo;

import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.wizard.EntityClass;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

// TODO: Auto-generated Javadoc
/**
 * The Class Formula.
 * 
 * @author Andrea Gioia
 */
public class Formula {
	
	/** The document. */
	Document document;
	
	/**
	 * Instantiates a new formula.
	 * 
	 * @param in the in
	 * 
	 * @throws DocumentException the document exception
	 */
	public Formula(InputStream in) throws DocumentException {
		SAXReader saxReader = null;
		saxReader = new SAXReader();
		document = saxReader.read(in);
	}
	
	/**
	 * Gets the calculated field.
	 * 
	 * @param entityName the entity name
	 * 
	 * @return the calculated field
	 */
	public CalculatedField getCalculatedField(String entityName) {
		
		CalculatedField calculatedField = null;
		String xPath = null;
		Node node = null;
			
		xPath = "/FORMULAS/FORMULA[@id = '" + entityName + "']";
		node =  document.selectSingleNode(xPath);
		
		if(node != null) {
			calculatedField = new CalculatedField();
			calculatedField.setId(node.valueOf("@id"));
			calculatedField.setEntityName(node.valueOf("@onEntity"));
			calculatedField.setFldLabel(node.valueOf("@resultingFieldName"));
			calculatedField.setScript(node.valueOf("@script"));
			calculatedField.setMappings(node.valueOf("@mappings"));
			calculatedField.setInExport(node.valueOf("@inExport"));
		}
		
		return calculatedField;
	}
	
	/**
	 * Gets the calculated fields.
	 * 
	 * @param entityClasseIterator the entity classe iterator
	 * 
	 * @return the calculated fields
	 */
	public List getCalculatedFields(Iterator entityClasseIterator) {
		List calculateFields = new ArrayList();
		EntityClass ec = null;
		String ecName = null;
		CalculatedField cfield = null;
		while(entityClasseIterator.hasNext()){
			ec =(EntityClass) entityClasseIterator.next();
			ecName = ec.getClassName(); 
			CalculatedField cf = getCalculatedField(ecName);
			if(cf != null) calculateFields.add(cf);
		}
		return calculateFields;
	}
	
	/**
	 * Gets the manual calculated fields for entity.
	 * 
	 * @param entityName the entity name
	 * 
	 * @return the manual calculated fields for entity
	 */
	public List getManualCalculatedFieldsForEntity(String entityName) {
		
		CalculatedField cfield = null;
		String xPath = null;
		Node node = null;
		List calcuatedFields = new ArrayList();
		
		xPath = "/FORMULAS/FORMULA[@onEntity = '" + entityName + "' and @mode='manual']";
			
		List nodeList = document.selectNodes(xPath);
			
		for (Iterator it2 = nodeList.iterator(); it2.hasNext();){
			node = (Node)it2.next();
			cfield = new CalculatedField();
			cfield.setId(node.valueOf("@id"));
			cfield.setEntityName(node.valueOf("@onEntity"));
			cfield.setFldLabel(node.valueOf("@resultingFieldName"));
			cfield.setScript(node.valueOf("@script"));
			cfield.setMappings(node.valueOf("@mappings"));
			cfield.setMappings(node.valueOf("@inExport"));
			calcuatedFields.add(cfield);
		}
		
		return calcuatedFields;
	}
}
