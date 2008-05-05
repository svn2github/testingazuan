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
package it.eng.qbe.javascript;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.urlgenerator.SelectFieldForSelectionURLGenerator;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeSelectJsTreeBuilder.
 */
public class QbeSelectJsTreeBuilder extends QbeJsTreeBuilder {
	
	/**
	 * Instantiates a new qbe select js tree builder.
	 * 
	 * @param dataMartModel the data mart model
	 * @param dataMartWizard the data mart wizard
	 * @param httpRequest the http request
	 */
	public QbeSelectJsTreeBuilder(DataMartModel dataMartModel, ISingleDataMartWizardObject dataMartWizard, HttpServletRequest httpRequest){
		super(dataMartModel, dataMartWizard, httpRequest);
		actionName = "SELECT_FIELD_FOR_SELECT_ACTION";
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.javascript.QbeJsTreeBuilder#getSelectdNodes()
	 */
	public Map getSelectdNodes() {
		Map map = new HashMap();
		
			Iterator it = dataMartWizard.getQuery().getSelectFieldsIterator();
			while(it.hasNext()) {
				ISelectField field = (ISelectField)it.next();
				EntityClass ec = field.getFieldEntityClass();
				QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(field, getClassPrefix());
				map.put(nodeId.getId(), nodeId);
			}
		
		return map;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.javascript.QbeJsTreeBuilder#addNodes()
	 */
	public void addNodes() {
		int rootNode = 0;
		int nodeCounter = 0;
		for (Iterator it = getClassNames().iterator(); it.hasNext(); ){
			String className = (String)it.next();
			nodeCounter = addFieldNodes(className, null, rootNode, nodeCounter, null, new SelectFieldForSelectionURLGenerator(className, qbeUrlGenerator, httpRequest), 1);
		}		
	}
}
