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
package it.eng.qbe.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.SelectClauseSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.SourceBean;

/**
 * @author Andrea Gioia
 *
 */
public class Query implements IQuery {
	
	private String queryId = null;
	private Map subqueryMap = null;
	private int subQueryCounter = 0;
	private Map mapFieldIdSubQUeryId = null;
	
	private ISelectClause selectClause = null;
	private IWhereClause  whereClause = null;
	private IOrderByClause orderByClause = null;
	private IGroupByClause groupByClause = null;
	
	private List entityClasses = null;
	
	private boolean distinct;
	
	private String subqueryErrMsg = "";
	
	
	public Query() {
		super();
		this.entityClasses = new ArrayList();
		this.subqueryMap = new HashMap();
		this.mapFieldIdSubQUeryId = new HashMap();
	}
	
	
	public boolean isEmpty(){
		return (selectClause == null 
				|| selectClause.getSelectFields() == null 
				|| selectClause.getSelectFields().size() == 0);
	}
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// SELECT
	////////////////////////////////////////////////////////////////////////////////////
	
	public ISelectClause getSelectClause() {
		return this.selectClause;
	}
	
	public void setSelectClause(ISelectClause selectClause) {
		this.selectClause = selectClause;
		
	}

	public void delSelectClause() {
		this.selectClause = null;
	}
	
	/**
	 * 
	 * @param className  				it.eng.Customer
	 * @param classAlias				ait_enf_Customer
	 * @param fieldAlias				ait_enf_Customer.name
	 * @param fieldLabel				Name
	 * @param selectFieldCompleteName 	it.eng.Customer.name
	 */
	public void addSelectField(String className, 
								   String classAlias, 
								   String fieldAlias, 
								   String fieldLabel, 
								   String selectFieldCompleteName,
								   String fldHibType,
								   String fldHibPrec,
								   String fldHibScale) {
		
		ISelectField selectField = null;
		
		if(getSelectClause() == null) {
			setSelectClause( new SelectClauseSourceBeanImpl() );
		}		
		selectField = new SelectFieldSourceBeanImpl();
		selectField.setFieldName(fieldAlias);
		selectField.setFieldAlias( stripPointsFromAlias(fieldLabel) );
		selectField.setFieldCompleteName(selectFieldCompleteName);
		selectField.setFieldEntityClass(getEntityClass(className, classAlias));
		selectField.setHibType(fldHibType);
		selectField.setScale(fldHibScale);
		selectField.setPrecision(fldHibPrec);
		
		getSelectClause().addSelectField(selectField);
	}
	
	private String stripPointsFromAlias(String original){
		
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
	
	private EntityClass getEntityClass(String className, String classAlias){
		EntityClass ec = new EntityClass(className, classAlias);
		if (!containEntityClass(ec)){
			addEntityClass(ec);
		}	
		return ec;
	}
	
	
	
	
	
	
	
	
	
	public IWhereClause getWhereClause() {
		return this.whereClause;
	}
	
	public IOrderByClause getOrderByClause() {
		return this.orderByClause;
	}
	
	public IGroupByClause getGroupByClause() {
		
		return this.groupByClause;
		
	}
	
	public void setOrderByClause(IOrderByClause orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	public void setGroupByClause(IGroupByClause groupByClause) {
		this.groupByClause = groupByClause;		
	}

	
	
	public void setWhereClause(IWhereClause whereClause) {
		this.whereClause = whereClause;
		
	}

	
	
	
	public void delWhereClause() {
		this.whereClause = null;
	}
	
	public void delOrderByClause() {
		this.orderByClause = null;
	}
	
	public void delGroupByClause() {
		this.groupByClause = null;
	}
	
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	public boolean getDistinct() {
		return distinct;
	}
	
	public void addSubQueryOnField(String fieldId, ISingleDataMartWizardObject subquery) {		
		this.subqueryMap.put(fieldId, subquery);
	}
	
	
	public void addEntityClass(EntityClass ec) {
		this.entityClasses.add(ec);
	}
	public List getEntityClasses() {
		return this.entityClasses;
	}
	
	public boolean containEntityClass(EntityClass parameEc) {
		
		EntityClass ec = null;
		SourceBean sb = null;
		
		for (Iterator it = this.entityClasses.iterator(); it.hasNext();){
			
			ec = (EntityClass)it.next();
			if (ec.getClassName().equalsIgnoreCase(parameEc.getClassName())
			   && ec.getClassAlias().equalsIgnoreCase(ec.getClassAlias())){
				return true;
			}
		}
		return false;
							
	}
	
	

	public void purgeNotReferredEntityClasses() {
		this.entityClasses.clear();
		
		ISelectClause selectClause = getSelectClause();
				
		EntityClass ec = null;
		ISelectField selField = null;
		if (selectClause != null){
			for (int i=0; i < selectClause.getSelectFields().size(); i++){
				selField = (ISelectField)selectClause.getSelectFields().get(i);
				ec = selField.getFieldEntityClass();
				if (!this.containEntityClass(ec)){
					this.addEntityClass(ec);
				}
			}
		}
		
		IWhereClause whereClause = getWhereClause();
		
		ec = null;
		
		IWhereField whereField = null;
		
		if (whereClause != null){
			for (int i=0; i < whereClause.getWhereFields().size(); i++){
				whereField = (IWhereField)whereClause.getWhereFields().get(i);
				ec = whereField.getFieldEntityClassForLeftCondition();
				if (!this.containEntityClass(ec)){
					this.addEntityClass(ec);
				}
				ec = whereField.getFieldEntityClassForRightCondition();
				if ((ec != null)&&(!this.containEntityClass(ec))){
					this.addEntityClass(ec);
				}
				
			}
		}		
		
	}
	
	public void purgeNotReferredEntityClasses(String prefix) {
		this.entityClasses.clear();
		
		ISelectClause selectClause = getSelectClause();
				
		EntityClass ec = null;
		ISelectField selField = null;
		if (selectClause != null){
			for (int i=0; i < selectClause.getSelectFields().size(); i++){
				selField = (ISelectField)selectClause.getSelectFields().get(i);
				ec = selField.getFieldEntityClass();
				if (!this.containEntityClass(ec) && ec.getClassAlias().startsWith(prefix)){
					this.addEntityClass(ec);
				}
			}
		}
		
		IWhereClause whereClause = getWhereClause();
		
		ec = null;
		
		IWhereField whereField = null;
		
		if (whereClause != null){
			for (int i=0; i < whereClause.getWhereFields().size(); i++){
				whereField = (IWhereField)whereClause.getWhereFields().get(i);
				ec = whereField.getFieldEntityClassForLeftCondition();
				if (!this.containEntityClass(ec)&& ec.getClassAlias().startsWith(prefix)){
					this.addEntityClass(ec);
				}
				ec = whereField.getFieldEntityClassForRightCondition();
				if ((ec != null)&&(!this.containEntityClass(ec)) && ec.getClassAlias().startsWith(prefix)) {
					this.addEntityClass(ec);
				}
				
			}
		}		
		
	}
	
	public boolean areAllEntitiesJoined() {
		
		boolean entitiesJoined = true;
		
		if (getEntityClasses().size() == 1) return true;
		
		
		for (int i =0; i < getEntityClasses().size() && entitiesJoined; i++ ) {
			
			EntityClass entty = (EntityClass)getEntityClasses().get(i);
			entitiesJoined = areEntitiyJoined(entty);
			
		}
		
		return entitiesJoined;
	}
	
	public boolean areEntitiyJoined(EntityClass targetEntity) {
		boolean joinFound = false;
		
		for (int j = 0; j < getEntityClasses().size(); j++ ){
			EntityClass e2 = (EntityClass)getEntityClasses().get(j);
				
			if (targetEntity.getClassName() != e2.getClassName()) {
				
				if (getWhereClause() != null) {
					
					for (int k=0; ((k < getWhereClause().getWhereFields().size()) && !joinFound); k++){
						
						IWhereField wf = (IWhereField)getWhereClause().getWhereFields().get(k);
						
						if (wf.getFieldOperator().equalsIgnoreCase("=")) {
							
							joinFound = (
								(wf.getFieldName().startsWith(targetEntity.getClassAlias()) &&  wf.getFieldValue().startsWith(e2.getClassAlias()))
								||
								(wf.getFieldName().startsWith(e2.getClassAlias())&&  wf.getFieldValue().startsWith(targetEntity.getClassAlias()))
							);
						}
					}
				}
			}
		}
		
		return joinFound;
	}
	
	public Map getSubqueries() {
		return subqueryMap;
	}

	public String getSubQueryIdForSubQueryOnField(String fieldId) {
		return (String)mapFieldIdSubQUeryId.get(fieldId);
	}
	
	public Map getMapFieldIdSubQUeryId() {
		return mapFieldIdSubQUeryId;
	}




	public void setMapFieldIdSubQUeryId(Map mapFieldIdSubQUeryId) {
		this.mapFieldIdSubQUeryId = mapFieldIdSubQUeryId;
	}




	public int getSubQueryCounter() {
		return subQueryCounter;
	}




	public void setSubQueryCounter(int subQueryCounter) {
		this.subQueryCounter = subQueryCounter;
	}

	public Map getSubqueryMap() {
		return subqueryMap;
	}


	public void setSubqueryMap(Map subqueryMap) {
		this.subqueryMap = subqueryMap;
	}


	/**
	 * @deprecated
	 */
	public void addSubQueryOnField(String fieldId) {
		
		
		this.subqueryMap.put(fieldId, new SingleDataMartWizardObjectSourceBeanImpl());
		this.mapFieldIdSubQUeryId.put(fieldId, getNewSubQueryId());
	}


	public String[] getDuplicatedAliases() {
		List fields = selectClause.getSelectFields();
		Map duplicatedFieldMap = new HashMap();
	  	Map map = new HashMap();
		for(int i = 0; i < fields.size(); i++) {
			ISelectField selectField = (ISelectField)fields.get(i);
			String fieldAlias = selectField.getFieldAlias();
	  		if(map.get(fieldAlias) != null) duplicatedFieldMap.put(fieldAlias,fieldAlias);
	  		else map.put(fieldAlias,fieldAlias);
		}
		String[] duplicatedFieldAliasArray = (String[])duplicatedFieldMap.keySet().toArray(new String[0]);
		
		return duplicatedFieldAliasArray;
	}
	
	public boolean containsDuplicatedAliases() {
		return (getDuplicatedAliases().length > 0);
	}


	public ISingleDataMartWizardObject getSubQueryOnField(String fieldId) {		
		return (ISingleDataMartWizardObject)this.subqueryMap.get(fieldId);
	}
	
	public String getNewSubQueryId(){
		subQueryCounter++;
		return "sub"+this.subQueryCounter;
	}
	
	public String getSubqueryErrMsg() {
		return subqueryErrMsg;
	}

	public void setSubqueryErrMsg(String subqueryErrMsg) {
		this.subqueryErrMsg = subqueryErrMsg;
	}
}
