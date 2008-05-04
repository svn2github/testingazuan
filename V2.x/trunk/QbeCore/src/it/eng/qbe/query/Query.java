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
package it.eng.qbe.query;

import it.eng.qbe.bo.Formula;
import it.eng.qbe.datasource.IHibernateDataSource;
import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.spago.base.SourceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;


public class Query implements IQuery {
	
	private String queryId = null;
	
	private ISelectClause selectClause = null;
	private IWhereClause  whereClause = null;
	private IOrderByClause orderByClause = null;
	private IGroupByClause groupByClause = null;
	
	
	
	
	private Map subqueryMap = null;
	private int subQueryCounter = 0;
	private Map mapFieldIdSubQueryId = null;
	private IQuery selectedSubquery = null;
	
	private String subqueryFieldId = null;
	private boolean subqueryModeActive =  false;
	
	
	
	private List entityClasses = null;
	
	private boolean distinct;
	
	private String errMsg = "";
		

	
	
	public Query() {
		super();
		
		entityClasses = new ArrayList();
		subqueryMap = new HashMap();
		mapFieldIdSubQueryId = new HashMap();
		
		setSelectClause( new SelectClause() );
		setWhereClause( new WhereClause() );
		setOrderByClause( new OrderByClause() );
		setGroupByClause( new GroupByClause() );
	}
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	public boolean isEmpty(){
		return getSelectClause().isEmpty();
	}
	
	
		
	/////////////////////////////////////////////////////////////////////////////////////
	// SELECT
	////////////////////////////////////////////////////////////////////////////////////
	
	// ...public
	
	public void deleteSelectClause() {
		getActiveQuery().setSelectClause( new SelectClause() );
	}
	
	public void deleteSelectField(String fieldId) {
		ISelectField selectField = new SelectField();
		selectField.setId(fieldId);		
		
		List selectFields = getActiveQuery().getSelectClause().getSelectFields();		
		ISelectField tmpSelectField = null;
		boolean found = false;
		for (int i=0; i < selectFields.size(); i++){
			tmpSelectField = (ISelectField)selectFields.get(i);
			if (tmpSelectField.getId().equalsIgnoreCase(selectField.getId())){
				found = true;
				break;
			}
		}
		
		if ((tmpSelectField != null) && found){
		
			//Devo rimuovere il campo anche nelle eventuali condizioni di Order By
			
			IOrderByClause orderByClause = getActiveQuery().getOrderByClause();
			if (orderByClause != null){
				List orderByFields = orderByClause.getOrderByFields();
				IOrderByField aOrderGroupByField = null;
				IOrderByField toEliminate = null;
				
				for (int i=0; i < orderByFields.size(); i++){
					aOrderGroupByField = (IOrderByField)orderByFields.get(i);
					if (aOrderGroupByField.getFieldName().equalsIgnoreCase(tmpSelectField.getFieldName())){
						toEliminate = new OrderByField();
						toEliminate.setId(aOrderGroupByField.getId());
					}
				}
				
				if (toEliminate != null){
					orderByClause.deleteOrderByField(toEliminate.getId());
				}
				if (orderByClause.getOrderByFields().size() == 0)
					deleteOrderByClause();
			}
		
		
		
			//Devo rimuovere il campo anche nelle eventuali condizioni di Order By
		
			IGroupByClause groupByClause = getActiveQuery().getGroupByClause();
			if (groupByClause != null){
				List groupByFields = groupByClause.getGroupByFields();
				IGroupByField groupByField = null;
				IGroupByField toEliminate = null;
				for (int i=0; i < groupByFields.size(); i++){
					groupByField = (IGroupByField)groupByFields.get(i);
					if (groupByField.getFieldName().equalsIgnoreCase(tmpSelectField.getFieldName())){
						toEliminate = new GroupByField();
						toEliminate.setId(groupByField.getId());
					}
				}
				
				if (toEliminate != null){
					groupByClause.deleteGroupByField(toEliminate.getId());
				}
				
				if (groupByClause.getGroupByFields().size() == 0)
					deleteGroupByClause();
			}
		
		
			getActiveQuery().getSelectClause().delSelectField(fieldId);
		 }
		
		 String prefix = "a";
		 if (isSubqueryModeActive()){
				prefix = getSubQueryIdForSubQueryOnField( getSubqueryFieldId() );
		 }	
		 getActiveQuery().purgeNotReferredEntityClasses(prefix);
	}
	
	public void deleteCalculatedField(String fieldId) {
		getActiveQuery().getSelectClause().deleteCalculatedField(fieldId);
	}
	
	public Iterator getSelectFieldsIterator() {
		return getActiveQuery().getSelectClause().getSelectFields().iterator();
	}
	
	public Iterator getCalculatedFieldsIterator() {
		return getActiveQuery().getSelectClause().getCalcuatedFields().iterator();
	}
	

	public ISelectField addSelectField(String className, String fieldName, String fieldLabel,
			String fieldHibType, String fieldHibScale, String fieldHibPrec) {
		
		ISelectField selectField = null;
		
		String subQueryPrefix = null;
		if (isSubqueryModeActive()){
			subQueryPrefix = getSubQueryIdForSubQueryOnField( getSubqueryFieldId() );
		}
		
		if(className != null && fieldName != null) {		
			
			FNode fNode = new FNode(className, fieldName);
			if (isSubqueryModeActive())
				fNode.setClassPrefix(subQueryPrefix);
			
			
			
			
			selectField = new SelectField();
			selectField.setFieldName(fNode.getFieldAlias());
			selectField.setFieldAlias( stripPointsFromAlias(fieldLabel) );
			selectField.setFieldCompleteName(className + "." + fieldName);
			selectField.setFieldEntityClass(getEntityClass(className, fNode.getClassAlias()));
			selectField.setType(fieldHibType);
			selectField.setScale(new Integer(fieldHibScale));
			selectField.setPrecision(new Integer(fieldHibPrec));
			
			getActiveQuery().getSelectClause().addSelectField(selectField);
		} 
		
		return selectField;
	}
	
	public CalculatedField addCalculatedField(String fieldId, 
								   String fieldNameInQuery, String classNameInQuery, 
								   Formula formula,
								   IHibernateDataSource dataSource) {
		
		CalculatedField calculatedField = formula.getCalculatedField(fieldId);
		calculatedField.setFldCompleteNameInQuery(fieldNameInQuery);
		String mappings = calculatedField.getMappings();
		String[] mappingArray = mappings.split(",");
		
		String subQueryPrefix = null;
		if (isSubqueryModeActive()){
			subQueryPrefix = getSubQueryIdForSubQueryOnField( getSubqueryFieldId() );
		}
		
		Integer positionInteger = new Integer(-1);
		String prefix = null;
		if ( fieldNameInQuery.indexOf(".") > 0 ){
			prefix = fieldNameInQuery.substring(0, fieldNameInQuery.lastIndexOf("."));
		}
		
		
		for ( int i =0; i < mappingArray.length; i++){
			String[] splitMapping = mappingArray[i].split("->");
			String requiredField = splitMapping[0];
			
			String completeRequiredFieldName = requiredField;
			if (prefix != null){
				completeRequiredFieldName = prefix + "." + requiredField;
			}
		
			calculatedField.setClassNameInQuery(classNameInQuery);
			positionInteger =  findPositionOf( classNameInQuery + "." + completeRequiredFieldName );
			
			SessionFactory sf = dataSource.getSessionFactory();
			
			ClassMetadata cmd = sf.getClassMetadata(calculatedField.getEntityName());
			Type aHibType = cmd.getPropertyType(requiredField);
			
			if (positionInteger.intValue() == -1){
				addSelectField(classNameInQuery, completeRequiredFieldName, completeRequiredFieldName,
						aHibType.getName(), null, null);
			}		
		}
		
		getActiveQuery().getSelectClause().addCalculatedField(calculatedField);
		
		return calculatedField;
	}
	
	public void moveDownSelectField(String fieldId) {
		getActiveQuery().getSelectClause().moveDown(fieldId);
	}
	
	public void moveUpSelectField(String fieldId) {
		getActiveQuery().getSelectClause().moveUp(fieldId);
	}
	
	public void setDistinct(boolean distinct) {
		getActiveQuery().distinct = distinct;
	}
	
	public boolean getDistinct() {
		if( isSubqueryModeActive() ) {
			getActiveQuery().getDistinct();
		} 
		
		return distinct;		
	}
	
	// ... private
	
	private ISelectClause getSelectClause() {
		return this.selectClause;
	}
	
	private void setSelectClause(ISelectClause selectClause) {
		this.selectClause = selectClause;
		
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
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// WHERE
	////////////////////////////////////////////////////////////////////////////////////
	
	// ...public
	
	public void deleteWhereClause() {
		getActiveQuery().setWhereClause( new WhereClause() );
	}
	
	public void deleteWhereField(String fieldId) {
		IWhereClause whereClause = getActiveQuery().getWhereClause();
		
		
		whereClause.delWhereField(fieldId);
		
						
		String prefix = "a";
		if ( isSubqueryModeActive() ) {
			prefix = getSubQueryIdForSubQueryOnField( getSubqueryFieldId() );
		}	
		 getActiveQuery().purgeNotReferredEntityClasses(prefix);
	}
	
	public IWhereField addWhereField(String fieldName, String type) {
			
		IWhereField whereField = new WhereField();
		whereField.setFieldName(fieldName);
		whereField.setFieldOperator("=");
		whereField.setFieldValue("");
		whereField.setType(type);
		whereField.setNextBooleanOperator("AND");
				
		getActiveQuery().getWhereClause().addWhereField(whereField);
		
		return whereField;
	}
	
	
	public Iterator getWhereFieldsIterator() {
		return getActiveQuery().getWhereClause().getWhereFields().iterator();
	}
	
	public void moveDownWhereField(String fieldId) {
		getActiveQuery().getWhereClause().moveDown(fieldId);
	}
	
	public void moveUpWhereField(String fieldId) {
		getActiveQuery().getWhereClause().moveUp(fieldId);
	}
	
	// ...private
	
	private IWhereClause getWhereClause() {
		return whereClause;
	}
	
	private void setWhereClause(IWhereClause whereClause) {
		this.whereClause = whereClause;
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// ORDER BY
	////////////////////////////////////////////////////////////////////////////////////
	
	// public
	
	public void deleteOrderByClause() {
		this.orderByClause = null;
	}
	
	public void deleteOrderByField(String fieldId) {
		getActiveQuery().getOrderByClause().deleteOrderByField(fieldId);
	}
	
	public IOrderByField addOrderByField(String fieldName) {
		
		IOrderByField orderByField = new OrderByField();
		orderByField.setFieldName(fieldName);
		getActiveQuery().getOrderByClause().addOrderByField(orderByField);
		
		return orderByField;
	}
	
	public Iterator getOrderByFieldsIterator() {
		return getActiveQuery().getOrderByClause().getOrderByFields().iterator();
	}
	
	public void moveDownOrderByField(String fieldId) {
		getActiveQuery().getOrderByClause().moveDown(fieldId);
	}
	
	public void moveUpOrderByField(String fieldId) {
		getActiveQuery().getOrderByClause().moveUp(fieldId);
	}
	
	public void switchAscendingOrderPopertyValue(String fieldId) {
		List fields = getActiveQuery().getOrderByClause().getOrderByFields();
		for(int i = 0; i < fields.size(); i++) {
			IOrderByField field = (IOrderByField)fields.get(i);
			if(field.getId().equalsIgnoreCase(fieldId)) {
				field.setAscendingOrder(!field.isAscendingOrder());
			}
		}
	}
	
	// private
	
	private IOrderByClause getOrderByClause() {
		return this.orderByClause;
	}	
	
	private void setOrderByClause(IOrderByClause orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// GROUP BY
	////////////////////////////////////////////////////////////////////////////////////
	
	// public 
	
	public void deleteGroupByClause() {
		this.groupByClause = null;
	}
	
	public void deleteGroupByField(String fieldId) {
		getActiveQuery().getGroupByClause().deleteGroupByField(fieldId);
	}
	
	public IGroupByField addGroupByField(String fieldName) {
		
		IGroupByField groupByField = new GroupByField();
		groupByField.setFieldName(fieldName);
		getActiveQuery().getGroupByClause().addGroupByField(groupByField);
		
		return groupByField;
	}
	
	public Iterator getGroupByFieldsIterator() {
		return getActiveQuery().getGroupByClause().getGroupByFields().iterator();
	}
	
	public void moveDownGroupByField(String fieldId) {
		getActiveQuery().getGroupByClause().moveDown(fieldId);
	}
	
	public void moveUpGroupByField(String fieldId) {
		getActiveQuery().getGroupByClause().moveUp(fieldId);
	}
	
	// private
	
	private IGroupByClause getGroupByClause() {		
		return this.groupByClause;		
	}
	
	private void setGroupByClause(IGroupByClause groupByClause) {
		this.groupByClause = groupByClause;		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// FROM
	////////////////////////////////////////////////////////////////////////////////////
	
	// public ...	
	
	public Iterator getEntityClassesIterator() {
		return getActiveQuery().entityClasses.iterator();
	}
	
	
	public void addEntityClass(EntityClass ec) {
		getActiveQuery().entityClasses.add(ec);
	}
	
	
	public boolean containEntityClass(EntityClass parameEc) {
		
		EntityClass ec = null;
		SourceBean sb = null;
		
		for (Iterator it = getActiveQuery().entityClasses.iterator(); it.hasNext();){
			
			ec = (EntityClass)it.next();
			if (ec.getClassName().equalsIgnoreCase(parameEc.getClassName())
			   && ec.getClassAlias().equalsIgnoreCase(ec.getClassAlias())){
				return true;
			}
		}
		return false;
							
	}
	
	

	public void purgeNotReferredEntityClasses() {
		getActiveQuery().entityClasses.clear();
		
		ISelectClause selectClause = getActiveQuery().getSelectClause();
				
		EntityClass ec = null;
		ISelectField selField = null;
		if (selectClause != null){
			for (int i=0; i < selectClause.getSelectFields().size(); i++){
				selField = (ISelectField)selectClause.getSelectFields().get(i);
				ec = selField.getFieldEntityClass();
				if (!containEntityClass(ec)){
					addEntityClass(ec);
				}
			}
		}
		
		IWhereClause whereClause = getActiveQuery().getWhereClause();
		
		ec = null;
		
		IWhereField whereField = null;
		
		if (whereClause != null){
			for (int i=0; i < whereClause.getWhereFields().size(); i++){
				whereField = (IWhereField)whereClause.getWhereFields().get(i);
				ec = whereField.getFieldEntityClassForLeftCondition();
				if (!containEntityClass(ec)){
					addEntityClass(ec);
				}
				ec = whereField.getFieldEntityClassForRightCondition();
				if ((ec != null)&&(!containEntityClass(ec))){
					addEntityClass(ec);
				}
				
			}
		}		
		
	}
	
	public void purgeNotReferredEntityClasses(String prefix) {
		this.entityClasses.clear();
		
		ISelectClause selectClause = getActiveQuery().getSelectClause();
				
		EntityClass ec = null;
		ISelectField selField = null;
		if (selectClause != null){
			for (int i=0; i < selectClause.getSelectFields().size(); i++){
				selField = (ISelectField)selectClause.getSelectFields().get(i);
				ec = selField.getFieldEntityClass();
				if (!containEntityClass(ec) && ec.getClassAlias().startsWith(prefix)){
					addEntityClass(ec);
				}
			}
		}
		
		IWhereClause whereClause = getActiveQuery().getWhereClause();
		
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
	
	private EntityClass getEntityClass(String className, String classAlias){
		EntityClass ec = new EntityClass(className, classAlias);
		if (!containEntityClass(ec)){
			addEntityClass(ec);
		}	
		return ec;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	

	
	
	
	
	
	
	
	
	
	
	
	

	
	public boolean areAllEntitiesJoined() {
		
		boolean entitiesJoined = true;
		
		if (getActiveQuery().entityClasses.size() == 1) return true;
		
		
		Iterator it = this.getEntityClassesIterator();
		while (it.hasNext() ){
			
			EntityClass entty = (EntityClass)it.next();
			entitiesJoined = areEntitiyJoined(entty);
			
		}
		
		return entitiesJoined;
	}
	
	public boolean areEntitiyJoined(EntityClass entity) {
		boolean joinFound = false;
		
		Iterator it = this.getEntityClassesIterator();
		while (it.hasNext() ){
			EntityClass targetEntity = (EntityClass)it.next();
				
			if (entity.getClassName() != targetEntity.getClassName()) {
				
				if (getWhereClause() != null) {
					
					for (int k=0; ((k < getWhereClause().getWhereFields().size()) && !joinFound); k++){
						
						IWhereField wf = (IWhereField)getWhereClause().getWhereFields().get(k);
						
						if (wf.getFieldOperator().equalsIgnoreCase("=")) {
							
							joinFound = (
								(wf.getFieldName().startsWith(entity.getClassAlias()) &&  wf.getFieldValue().startsWith(targetEntity.getClassAlias()))
								||
								(wf.getFieldName().startsWith(targetEntity.getClassAlias())&&  wf.getFieldValue().startsWith(entity.getClassAlias()))
							);
						}
					}
				}
			}
		}
		
		return joinFound;
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


	
	private Query getActiveQuery() {
		
		IQuery activeQuery = null;
		if( isSubqueryModeActive() ) {
			activeQuery = getSelectedSubquery();
		} else {
			activeQuery = this;
		}
		
		return (Query)activeQuery;
	}	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// SUBQUERY
	////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isSubqueryModeActive() {
		return subqueryModeActive;
	}	
	
	public String getSubqueryField() {
		return subqueryFieldId;
	}
	
	public String getSubqueryFieldId() {
		return subqueryFieldId;
	}
		
	public IQuery getSubquery(String fieldId) {		
		return (IQuery)subqueryMap.get(fieldId);
	}	
	
	public boolean isSubqueryDefined(String fieldId) {
		return getSubquery(fieldId) != null;
	}
	
	public void selectSubquery(String fieldId) {		
		if(!isSubqueryDefined(fieldId)) {
			addEmptySubquery(fieldId);
		}
		
		// All modifications are done on a copy of the selected subquery
		// It is possible to save them at anytime or just undo all.
		setSelectedSubquery( getSubquery(fieldId).getCopy() );
		setSubqueryFieldId(fieldId);
		setSubqueryModeActive(true);		
	}	
	
	public void saveSelectedSubquery() {
		if( isSubqueryModeActive() ) {
			subqueryMap.put(getSubqueryFieldId(), getSelectedSubquery());
		}
	}
	
	public void deselectSubquery() {
		setSelectedSubquery(null);
		setSubqueryFieldId(null);
		setSubqueryModeActive(false);
	}	

	public String getSubQueryIdForSubQueryOnField(String fieldId) {
		return (String)mapFieldIdSubQueryId.get(fieldId);
	}

	
	public String getErrMsg() {
		return errMsg;
	}
	
	public String getSubqueryErrMsg(String fieldId) {
		if(isSubqueryDefined(fieldId)) {
			IQuery subquery = getSubquery(fieldId);
			if( isSubqueryValid(subquery) == false) {
				return subquery.getErrMsg();
			}
		}		
		
		return null;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}	
	
	// ... private
	
	private void setSubqueryModeActive(boolean subqueryModeActive) {
		this.subqueryModeActive = subqueryModeActive;
	}	
	
	private IQuery getSelectedSubquery() {
		return selectedSubquery;
	}
	
	private void setSelectedSubquery(IQuery selectedSubquery) {
		this.selectedSubquery = selectedSubquery;
	}
	
	private void addEmptySubquery(String fieldId) {		
		addSubquery(fieldId, new Query());
	}
	
	private void addSubquery(String fieldId, IQuery subquery) {		
		subqueryMap.put(fieldId, subquery);
		mapFieldIdSubQueryId.put(fieldId, getNewSubQueryId());
	}
		
	
	private String getNewSubQueryId(){
		subQueryCounter++;
		return "sub"+this.subQueryCounter;
	}
	
	private Map getSubqueryMap() {
		return subqueryMap;
	}
	
	private void setSubqueryMap(Map subqueryMap) {
		this.subqueryMap = subqueryMap;
	}
	
	public Map getMapFieldIdSubQUeryId() {
		return mapFieldIdSubQueryId;
	}

	public void setMapFieldIdSubQUeryId(Map mapFieldIdSubQUeryId) {
		this.mapFieldIdSubQueryId = mapFieldIdSubQUeryId;
	}
	
	private void setSubqueryFieldId(String fieldId) {
		subqueryFieldId = fieldId;
	}
	
	private int getSubQueryCounter() {
		return subQueryCounter;
	}

	private void setSubQueryCounter(int subQueryCounter) {
		this.subQueryCounter = subQueryCounter;
	}
	


	

	public IQuery getCopy() {
		// TO BE IMPLEMENTED
		return this;
	}

	
	
	public boolean isSubqueryValid(IQuery subquery) {
		
		boolean missingCondition = false;
		boolean invalidReference = false;
		subquery.setErrMsg(null);
		
		if(subquery !=  null) {
			Iterator it = subquery.getWhereFieldsIterator();
			while(it.hasNext()) {
				IWhereField whereField = (IWhereField)it.next();
				// check missing right condition				
				String value = whereField.getFieldValue();
				if(value == null || value.trim().equalsIgnoreCase("")) {
					missingCondition = true;
				}  
					
				EntityClass ec = whereField.getFieldEntityClassForRightCondition();
				if(ec != null && !containEntityClass(ec)) {
					subquery.setErrMsg("Subquery contains at least one where condition not properly defined");
					invalidReference = true;
				}
					
					if(invalidReference && missingCondition) break;					
			}
		}
		
		// resolve all invalid references first; missing right values next
		if(invalidReference) {
			subquery.setErrMsg("Subquery contains at least one where condition not properly defined (cause: invalid reference to a parent entity)");
		}
		else if(missingCondition) {
			subquery.setErrMsg("Subquery contains at least one where condition not properly defined (cause: missing right end value)");
		}
		
		
		return (!invalidReference && !missingCondition);		
	}
	
	public boolean isSelectedSubqueryValid() {
		IQuery subquery = getSelectedSubquery();
		return isSubqueryValid(subquery);
	}
	
	public boolean isSubqueryValid(String fieldId) {
		IQuery subquery = getSubquery(fieldId);
		return isSubqueryValid(subquery);
	}
	
	
	
	
	
	
	
	public Integer findPositionOf(String completeName){
		if (getSelectClause() != null){
			List l = getSelectClause().getSelectFields();
			ISelectField selField = null;
			String selFieldcompleteName = null;
			for (int i=0; i < l.size(); i++){
				selField = (ISelectField)l.get(i);
				selFieldcompleteName = selField.getFieldCompleteName();
				if ((selFieldcompleteName != null) && (selFieldcompleteName.equals(completeName))){
					return new Integer(i);
				}
			}
		}
		
		return new Integer(-1);
	}

	
}
