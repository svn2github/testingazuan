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
package it.eng.qbe.action;

import it.eng.qbe.javascript.QbeJsTreeNodeId;
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
	public static final String FIELD_HIBTYPE = "FIELD_HIBTYPE";
	public static final String FIELD_HIBSCALE= "FIELD_HIBSCALE";
	public static final String FIELD_HIBPREC= "FIELD_HIBPREC";

	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {	
				
		String className = (String)request.getAttribute(CLASS_NAME);		
		String fieldName = (String)request.getAttribute(FIELD_NAME); 
		String fieldLabel = (String)request.getAttribute(FIELD_LABEL);
		
		String fieldHibType = (String)request.getAttribute(FIELD_HIBTYPE);
		String fieldHibScale = (String)request.getAttribute(FIELD_HIBSCALE);
		String fieldHibPrec= (String)request.getAttribute(FIELD_HIBPREC);
		
		SessionContainer aSessionContainer = getRequestContainer().getSessionContainer();
		boolean isSubQueryMode = Utils.isSubQueryModeActive(aSessionContainer);
		String subQueryPrefix = null;
		if (isSubQueryMode){
			String subQueryFieldId = (String)aSessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
			subQueryPrefix = Utils.getMainWizardObject(aSessionContainer).getSubQueryIdForSubQueryOnField(subQueryFieldId);
		}
		if(className != null && fieldName != null) {		
			//deleteExistingSelectClauses();
			QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, fieldName);
			if (isSubQueryMode)
				nodeId.setClassPrefix(subQueryPrefix);
			
			addSelectClause(nodeId.getClassName(), 
							nodeId.getClassAlias(), 
							nodeId.getFieldAlias(), 
							fieldLabel, 
							className + "." + fieldName,
							fieldHibType,
							fieldHibScale,
							fieldHibPrec);
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
				if (isSubQueryMode)
					nodeId.setClassPrefix(subQueryPrefix);
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
				
			
			
			for(int i = 0; i < items.size(); i++) {
				String chunks[] = ((String)items.get(i)).split(";");
				
				className = chunks[0];
				fieldName = chunks[1];
				fieldLabel = chunks[2];	
				
				QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, fieldName);
				if (isSubQueryMode)
					nodeId.setClassPrefix(subQueryPrefix);
				String classAlias = nodeId.getClassAlias();
				String fieldAlias = nodeId.getFieldAlias();				
						
				addSelectClause(className, classAlias, fieldAlias, fieldLabel, className + "." + fieldName);				
			}
			
		}
			
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		getSession().setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(getRequestContainer().getSessionContainer()));		
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
	
	protected void addSelectClause(String className, 
								   String classAlias, 
								   String fieldAlias, 
								   String fieldLabel, 
								   String selectFieldCompleteName) {
		ISelectClause aSelectClause = getSelectClause();		
		ISelectField aSelectField = new SelectFieldSourceBeanImpl();
		aSelectField.setFieldName(fieldAlias);
		String newfieldLabel = stripPointsFromAlias(fieldLabel);
		aSelectField.setFieldAlias(newfieldLabel);
		aSelectField.setFieldCompleteName(selectFieldCompleteName);
		aSelectField.setFieldEntityClass(getEntityClass(className, classAlias));
		aSelectClause.addSelectField(aSelectField);
		getDataMartWizard().setSelectClause(aSelectClause);
	}
	
	protected void addSelectClause(String className, 
								   String classAlias, 
								   String fieldAlias, 
								   String fieldLabel, 
								   String selectFieldCompleteName,
								   String fldHibType,
								   String fldHibPrec,
								   String fldHibScale) {
		ISelectClause aSelectClause = getSelectClause();		
		ISelectField aSelectField = new SelectFieldSourceBeanImpl();
		aSelectField.setFieldName(fieldAlias);
		String newfieldLabel = stripPointsFromAlias(fieldLabel);
		aSelectField.setFieldAlias(newfieldLabel);
		aSelectField.setFieldCompleteName(selectFieldCompleteName);
		aSelectField.setFieldEntityClass(getEntityClass(className, classAlias));
		aSelectField.setHibType(fldHibType);
		aSelectField.setScale(fldHibScale);
		aSelectField.setPrecision(fldHibPrec);
		aSelectClause.addSelectField(aSelectField);
		getDataMartWizard().setSelectClause(aSelectClause);
	}
	
	public String stripPointsFromAlias(String original){
		
		if (original.indexOf(".") < 0){
			return original;
		}else{
			StringBuffer newString = new StringBuffer();
			char ch;
			for (int i=0; i < original.length(); i++){
				ch = original.charAt(i);
				if (ch != '.'){
					newString.append(ch);
				}
			}
			return newString.toString();
		}
	}
	
	protected SessionContainer getSession() {
		return getRequestContainer().getSessionContainer();
	}
	
	protected ISingleDataMartWizardObject getDataMartWizard(){
		return  Utils.getWizardObject(getRequestContainer().getSessionContainer());
	}
}
