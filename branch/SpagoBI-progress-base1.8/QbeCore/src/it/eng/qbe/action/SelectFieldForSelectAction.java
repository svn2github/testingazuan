
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SelectClauseSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.utilities.javascript.QbeJsTreeNodeId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to put a field in Select  Clause of the object ISingleDataMartWizardObject in session
 */
public class SelectFieldForSelectAction extends AbstractAction {
	
	// valid input parameter names
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String FIELD_NAME = "FIELD_NAME";
	public static final String FIELD_LABEL = "FIELD_LABEL";
	
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {	
				
		String className = (String)request.getAttribute(CLASS_NAME);		
		String fieldName = (String)request.getAttribute(FIELD_NAME); 
		String fieldLabel = (String)request.getAttribute(FIELD_LABEL);
		
		
		if(className != null && fieldName != null) {		
			//deleteExistingSelectClauses();
			QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, fieldName);
			addSelectClause(nodeId.getClassName(), nodeId.getClassAlias(), nodeId.getFieldAlias(), fieldLabel);
		}
		else {
			List items = request.getAttributeAsList("selectItem");
			Map selectMap = new HashMap();
			
			for(int i = 0; i < items.size(); i++) {
				String chunks[] = ((String)items.get(i)).split(";");
				className = chunks[0];
				fieldName = chunks[1];
				fieldLabel = chunks[2];	
				
				QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, fieldName);
				String classAlias = nodeId.getClassAlias();
				String fieldAlias = nodeId.getFieldAlias();	
				String completeFieldName = classAlias + "." + fieldName;
				selectMap.put(completeFieldName, (String)items.get(i));
			}
			
			ISelectClause selectClause =  getDataMartWizard().getSelectClause();
			if(selectClause != null){
				List fields = selectClause.getSelectFields();
				//List items = request.getAttributeAsList("selectItem");
				ISelectField selField = null;
				for(int i = 0; i < fields.size(); i++) {
					selField = (ISelectField)fields.get(i);
					if(selectMap.containsKey(selField.getFieldNameWithoutOperators())) {
						items.remove(selectMap.get(selField.getFieldNameWithoutOperators()));
					}
					else {
						selectClause.delSelectField(selField);
					}					
				}
			}
				
			
			//deleteExistingSelectClauses();	
				
			//List list = request.getAttributeAsList("selectItem");
			for(int i = 0; i < items.size(); i++) {
				String chunks[] = ((String)items.get(i)).split(";");
				
				className = chunks[0];
				fieldName = chunks[1];
				fieldLabel = chunks[2];	
				
				QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, fieldName);
				String classAlias = nodeId.getClassAlias();
				String fieldAlias = nodeId.getFieldAlias();				
						
				addSelectClause(className, classAlias, fieldAlias, fieldLabel);				
			}
			
		}
			
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		getSession().setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, getDataMartWizard());		
	}
		
	
	private EntityClass getEntityClass(String className, String classAlias){
		EntityClass ec = new EntityClass(className, classAlias);
		if (!getDataMartWizard().containEntityClass(ec)){
			getDataMartWizard().addEntityClass(ec);
		}	
		return ec;
	}
	
	private void deleteExistingSelectClauses() {
		getDataMartWizard().setSelectClause(null);
	}
	
	private ISelectClause getSelectClause() {
		ISelectClause aSelectClause = getDataMartWizard().getSelectClause();
		if (aSelectClause == null){
			aSelectClause = new SelectClauseSourceBeanImpl();
		}
		return aSelectClause;
	}
	
	private void addSelectClause(String className, String classAlias, String fieldAlias, String fieldLabel) {
		ISelectClause aSelectClause = getSelectClause();		
		ISelectField aSelectField = new SelectFieldSourceBeanImpl();
		aSelectField.setFieldName(fieldAlias);
		aSelectField.setFieldAlias(fieldLabel);
		aSelectField.setFieldEntityClass(getEntityClass(className, classAlias));
		aSelectClause.addSelectField(aSelectField);
		getDataMartWizard().setSelectClause(aSelectClause);
	}
	
	
	
	private SessionContainer getSession() {
		return getRequestContainer().getSessionContainer();
	}
	
	private ISingleDataMartWizardObject getDataMartWizard(){
		return (ISingleDataMartWizardObject)getSession().getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
	}
}
