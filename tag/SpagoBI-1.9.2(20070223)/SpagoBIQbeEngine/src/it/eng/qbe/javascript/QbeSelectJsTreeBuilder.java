/**
 * 
 */
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
 * @author Gioia
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
