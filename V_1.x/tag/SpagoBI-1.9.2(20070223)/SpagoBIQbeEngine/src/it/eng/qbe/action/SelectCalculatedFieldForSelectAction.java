
package it.eng.qbe.action;

import it.eng.qbe.javascript.QbeJsTreeNodeId;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SelectClauseSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to put a field in Select  Clause of the object ISingleDataMartWizardObject in session
 */
public class SelectCalculatedFieldForSelectAction extends SelectFieldForSelectAction {
	
	// valid input parameter names
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String CFIELD_ID = "CFIELD_ID";
	public static final String CFIELD_COMPLETE_NAME = "CFIELD_COMPLETE_NAME";

	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {	
				
		String className = (String)request.getAttribute(CLASS_NAME);
		String cFieldId = (String)request.getAttribute(CFIELD_ID);
		String cFieldNameInQuery = (String)request.getAttribute(CFIELD_COMPLETE_NAME);
		
		SessionContainer aSessionContainer = getRequestContainer().getSessionContainer();
		DataMartModel dmModel = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
		
		boolean isSubQueryMode = Utils.isSubQueryModeActive(aSessionContainer);
		String subQueryPrefix = null;
		if (isSubQueryMode){
			String subQueryFieldId = (String)aSessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
			subQueryPrefix = Utils.getMainWizardObject(aSessionContainer).getSubQueryIdForSubQueryOnField(subQueryFieldId);
		}
		
		try {
			CalculatedField cField = Utils.getCalculatedField(cFieldId, dmModel.getJarFile().getParent());
			
			cField.setFldCompleteNameInQuery(cFieldNameInQuery);
			String mappings = cField.getMappings();
			String[] mappingArray = mappings.split(",");
			
			
			Integer positionInteger = new Integer(-1);
			String prefix = null;
			if ( cFieldNameInQuery.indexOf(".") > 0 ){
				prefix = cFieldNameInQuery.substring(0, cFieldNameInQuery.lastIndexOf("."));
			}
			
			
			for ( int i =0; i < mappingArray.length; i++){
				String[] splitMapping = mappingArray[i].split("->");
				String requiredField = splitMapping[0];
				
				String completeRequiredFieldName = requiredField;
				if (prefix != null){
					completeRequiredFieldName = prefix + "." + requiredField;
				}
			
				cField.setClassNameInQuery(className);
				positionInteger = Utils.findPositionOf(getDataMartWizard(), className + "." + completeRequiredFieldName);
				
				SessionFactory sf = Utils.getSessionFactory(dmModel,ApplicationContainer.getInstance());
				
				ClassMetadata cmd = sf.getClassMetadata(cField.getEntityName());
				Type aHibType = cmd.getPropertyType(requiredField);
				
				if (positionInteger.intValue() == -1){
					QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, completeRequiredFieldName);
					if (isSubQueryMode)
						nodeId.setClassPrefix(subQueryPrefix);
					addSelectClause(nodeId.getClassName(), nodeId.getClassAlias(), nodeId.getFieldAlias(), completeRequiredFieldName, className + "." + completeRequiredFieldName,aHibType.getName(), null, null);
				}
			
			}
		
			getDataMartWizard().getSelectClause().addCalculatedField(cField);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		getSession().setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(getRequestContainer().getSessionContainer()));		
	}
		
	
	
}
