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
package it.eng.qbe.javascript;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.urlgenerator.SelectFieldForSelectionURLGenerator;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Gioia
 *
 */
public class QbeSelectJsTreeBuilder extends QbeJsTreeBuilder {
	
	public QbeSelectJsTreeBuilder(DataMartModel dataMartModel, ISingleDataMartWizardObject dataMartWizard, HttpServletRequest httpRequest){
		super(dataMartModel, dataMartWizard, httpRequest);
		actionName = "SELECT_FIELD_FOR_SELECT_ACTION";
	}
	
	public Map getSelectdNodes() {
		Map map = new HashMap();
		ISelectClause aSelectClause = dataMartWizard.getSelectClause();
		if(aSelectClause != null) {
			List fields = aSelectClause.getSelectFields();
			for(int i = 0; i < fields.size(); i++) {
				ISelectField field = (ISelectField)fields.get(i);
				EntityClass ec = field.getFieldEntityClass();
				QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(field, getClassPrefix());
				map.put(nodeId.getId(), nodeId);
			}
		}
		return map;
	}
	
	public void addNodes() {
		int rootNode = 0;
		int nodeCounter = 0;
		for (Iterator it = getClassNames().iterator(); it.hasNext(); ){
			String className = (String)it.next();
			nodeCounter = addFieldNodes(className, null, rootNode, nodeCounter, null, new SelectFieldForSelectionURLGenerator(className, qbeUrlGenerator, httpRequest), 1);
		}		
	}
}
