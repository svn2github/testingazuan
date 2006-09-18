/**
 * 
 */
package it.eng.spagobi.utilities.javascript;

import it.eng.qbe.javascript.SelectFieldForConditionURLGenerator;
import it.eng.qbe.javascript.SelectFieldForJoinUrlGenerator;
import it.eng.qbe.javascript.SelectFieldForSelectionURLGenerator;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Gioia
 *
 */
public class QbeJoinWithFatherQueryJsTreeBuilder extends QbeJsTreeBuilder {

	public QbeJoinWithFatherQueryJsTreeBuilder(DataMartModel dataMartModel, ISingleDataMartWizardObject dataMartWizard, HttpServletRequest httpRequest){
		super(dataMartModel, dataMartWizard, httpRequest);
		
	}
	
	public Map getSelectdNodes() {
		Map map = new HashMap();
		return map;
	}
	
	public void addNodes() {
		int rootNode = 0;
		int nodeCounter = 0;
		Collection classNames = null;
		
		classNames = getSelectedClassNames();
				
		for (Iterator it = classNames.iterator(); it.hasNext(); ){
			String className = (String)it.next();
			nodeCounter = addFieldNodes(className, rootNode, nodeCounter, null, new SelectFieldForJoinUrlGenerator(className, qbeUrlGenerator, httpRequest, getClassPrefix()));
		}		
	}
	
	public void addRootNode() {
		addNode("0", "-1", "Join with Parent Query Entities", "", "", dataMartModel.getName(), 
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif"),
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif"),
				"", "", "", "", "");
	}

}
