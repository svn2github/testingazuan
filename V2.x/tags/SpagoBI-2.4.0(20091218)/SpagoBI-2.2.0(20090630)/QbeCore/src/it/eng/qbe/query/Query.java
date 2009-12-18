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


// TODO: Auto-generated Javadoc
/**
 * The Class Query.
 */
public class Query implements IQuery {
	
	/** The query id. */
	private String queryId = null;
	
	/** The select clause. */
	private ISelectClause selectClause = null;
	
	/** The where clause. */
	private IWhereClause  whereClause = null;
	
	/** The order by clause. */
	private IOrderByClause orderByClause = null;
	
	/** The group by clause. */
	private IGroupByClause groupByClause = null;
	
	
	
	
	/** The subquery map. */
	private Map subqueryMap = null;
	
	/** The sub query counter. */
	private int subQueryCounter = 0;
	
	/** The map field id sub query id. */
	private Map mapFieldIdSubQueryId = null;
	
	/** The selected subquery. */
	private IQuery selectedSubquery = null;
	
	/** The subquery field id. */
	private String subqueryFieldId = null;
	
	/** The subquery mode active. */
	private boolean subqueryModeActive =  false;
	
	
	
	/** The entity classes. */
	private List entityClasses = null;
	
	/** The distinct. */
	private boolean distinct;
	
	/** The err msg. */
	private String errMsg = "";
		

	
	
	/**
	 * Instantiates a new query.
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getQueryId()
	 */
	public String getQueryId() {
		return queryId;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#setQueryId(java.lang.String)
	 */
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#isEmpty()
	 */
	public boolean isEmpty(){
		return getSelectClause().isEmpty();
	}
	
	
		
	/////////////////////////////////////////////////////////////////////////////////////
	// SELECT
	////////////////////////////////////////////////////////////////////////////////////
	
	// ...public
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteSelectClause()
	 */
	public void deleteSelectClause() {
		getActiveQuery().setSelectClause( new SelectClause() );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteSelectField(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteCalculatedField(java.lang.String)
	 */
	public void deleteCalculatedField(String fieldId) {
		getActiveQuery().getSelectClause().deleteCalculatedField(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getSelectFieldsIterator()
	 */
	public Iterator getSelectFieldsIterator() {
		return getActiveQuery().getSelectClause().getSelectFields().iterator();
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getCalculatedFieldsIterator()
	 */
	public Iterator getCalculatedFieldsIterator() {
		return getActiveQuery().getSelectClause().getCalcuatedFields().iterator();
	}
	

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#addSelectField(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#addCalculatedField(java.lang.String, java.lang.String, java.lang.String, it.eng.qbe.bo.Formula, it.eng.qbe.datasource.IHibernateDataSource)
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveDownSelectField(java.lang.String)
	 */
	public void moveDownSelectField(String fieldId) {
		getActiveQuery().getSelectClause().moveDown(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveUpSelectField(java.lang.String)
	 */
	public void moveUpSelectField(String fieldId) {
		getActiveQuery().getSelectClause().moveUp(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#setDistinct(boolean)
	 */
	public void setDistinct(boolean distinct) {
		getActiveQuery().distinct = distinct;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getDistinct()
	 */
	public boolean getDistinct() {
		if( isSubqueryModeActive() ) {
			getActiveQuery().getDistinct();
		} 
		
		return distinct;		
	}
	
	// ... private
	
	/**
	 * Gets the select clause.
	 * 
	 * @return the select clause
	 */
	private ISelectClause getSelectClause() {
		return this.selectClause;
	}
	
	/**
	 * Sets the select clause.
	 * 
	 * @param selectClause the new select clause
	 */
	private void setSelectClause(ISelectClause selectClause) {
		this.selectClause = selectClause;
		
	}
	
	/**
	 * Strip points from alias.
	 * 
	 * @param original the original
	 * 
	 * @return the string
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteWhereClause()
	 */
	public void deleteWhereClause() {
		getActiveQuery().setWhereClause( new WhereClause() );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteWhereField(java.lang.String)
	 */
	public void deleteWhereField(String fieldId) {
		IWhereClause whereClause = getActiveQuery().getWhereClause();
		
		
		whereClause.delWhereField(fieldId);
		
						
		String prefix = "a";
		if ( isSubqueryModeActive() ) {
			prefix = getSubQueryIdForSubQueryOnField( getSubqueryFieldId() );
		}	
		 getActiveQuery().purgeNotReferredEntityClasses(prefix);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#addWhereField(java.lang.String, java.lang.String)
	 */
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
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getWhereFieldsIterator()
	 */
	public Iterator getWhereFieldsIterator() {
		return getActiveQuery().getWhereClause().getWhereFields().iterator();
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveDownWhereField(java.lang.String)
	 */
	public void moveDownWhereField(String fieldId) {
		getActiveQuery().getWhereClause().moveDown(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveUpWhereField(java.lang.String)
	 */
	public void moveUpWhereField(String fieldId) {
		getActiveQuery().getWhereClause().moveUp(fieldId);
	}
	
	// ...private
	
	/**
	 * Gets the where clause.
	 * 
	 * @return the where clause
	 */
	private IWhereClause getWhereClause() {
		return whereClause;
	}
	
	/**
	 * Sets the where clause.
	 * 
	 * @param whereClause the new where clause
	 */
	private void setWhereClause(IWhereClause whereClause) {
		this.whereClause = whereClause;
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// ORDER BY
	////////////////////////////////////////////////////////////////////////////////////
	
	// public
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteOrderByClause()
	 */
	public void deleteOrderByClause() {
		this.orderByClause = null;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteOrderByField(java.lang.String)
	 */
	public void deleteOrderByField(String fieldId) {
		getActiveQuery().getOrderByClause().deleteOrderByField(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#addOrderByField(java.lang.String)
	 */
	public IOrderByField addOrderByField(String fieldName) {
		
		IOrderByField orderByField = new OrderByField();
		orderByField.setFieldName(fieldName);
		getActiveQuery().getOrderByClause().addOrderByField(orderByField);
		
		return orderByField;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getOrderByFieldsIterator()
	 */
	public Iterator getOrderByFieldsIterator() {
		return getActiveQuery().getOrderByClause().getOrderByFields().iterator();
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveDownOrderByField(java.lang.String)
	 */
	public void moveDownOrderByField(String fieldId) {
		getActiveQuery().getOrderByClause().moveDown(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveUpOrderByField(java.lang.String)
	 */
	public void moveUpOrderByField(String fieldId) {
		getActiveQuery().getOrderByClause().moveUp(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#switchAscendingOrderPopertyValue(java.lang.String)
	 */
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
	
	/**
	 * Gets the order by clause.
	 * 
	 * @return the order by clause
	 */
	private IOrderByClause getOrderByClause() {
		return this.orderByClause;
	}	
	
	/**
	 * Sets the order by clause.
	 * 
	 * @param orderByClause the new order by clause
	 */
	private void setOrderByClause(IOrderByClause orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// GROUP BY
	////////////////////////////////////////////////////////////////////////////////////
	
	// public 
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteGroupByClause()
	 */
	public void deleteGroupByClause() {
		this.groupByClause = null;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deleteGroupByField(java.lang.String)
	 */
	public void deleteGroupByField(String fieldId) {
		getActiveQuery().getGroupByClause().deleteGroupByField(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#addGroupByField(java.lang.String)
	 */
	public IGroupByField addGroupByField(String fieldName) {
		
		IGroupByField groupByField = new GroupByField();
		groupByField.setFieldName(fieldName);
		getActiveQuery().getGroupByClause().addGroupByField(groupByField);
		
		return groupByField;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getGroupByFieldsIterator()
	 */
	public Iterator getGroupByFieldsIterator() {
		return getActiveQuery().getGroupByClause().getGroupByFields().iterator();
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveDownGroupByField(java.lang.String)
	 */
	public void moveDownGroupByField(String fieldId) {
		getActiveQuery().getGroupByClause().moveDown(fieldId);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#moveUpGroupByField(java.lang.String)
	 */
	public void moveUpGroupByField(String fieldId) {
		getActiveQuery().getGroupByClause().moveUp(fieldId);
	}
	
	// private
	
	/**
	 * Gets the group by clause.
	 * 
	 * @return the group by clause
	 */
	private IGroupByClause getGroupByClause() {		
		return this.groupByClause;		
	}
	
	/**
	 * Sets the group by clause.
	 * 
	 * @param groupByClause the new group by clause
	 */
	private void setGroupByClause(IGroupByClause groupByClause) {
		this.groupByClause = groupByClause;		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// FROM
	////////////////////////////////////////////////////////////////////////////////////
	
	// public ...	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getEntityClassesIterator()
	 */
	public Iterator getEntityClassesIterator() {
		return getActiveQuery().entityClasses.iterator();
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#addEntityClass(it.eng.qbe.wizard.EntityClass)
	 */
	public void addEntityClass(EntityClass ec) {
		getActiveQuery().entityClasses.add(ec);
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#containEntityClass(it.eng.qbe.wizard.EntityClass)
	 */
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
	
	

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#purgeNotReferredEntityClasses()
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#purgeNotReferredEntityClasses(java.lang.String)
	 */
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
	
	/**
	 * Gets the entity class.
	 * 
	 * @param className the class name
	 * @param classAlias the class alias
	 * 
	 * @return the entity class
	 */
	private EntityClass getEntityClass(String className, String classAlias){
		EntityClass ec = new EntityClass(className, classAlias);
		if (!containEntityClass(ec)){
			addEntityClass(ec);
		}	
		return ec;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	

	
	
	
	
	
	
	
	
	
	
	
	

	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#areAllEntitiesJoined()
	 */
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
	
	/**
	 * Are entitiy joined.
	 * 
	 * @param entity the entity
	 * 
	 * @return true, if successful
	 */
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


	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getDuplicatedAliases()
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#containsDuplicatedAliases()
	 */
	public boolean containsDuplicatedAliases() {
		return (getDuplicatedAliases().length > 0);
	}


	
	/**
	 * Gets the active query.
	 * 
	 * @return the active query
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#isSubqueryModeActive()
	 */
	public boolean isSubqueryModeActive() {
		return subqueryModeActive;
	}	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getSubqueryField()
	 */
	public String getSubqueryField() {
		return subqueryFieldId;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getSubqueryFieldId()
	 */
	public String getSubqueryFieldId() {
		return subqueryFieldId;
	}
		
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getSubquery(java.lang.String)
	 */
	public IQuery getSubquery(String fieldId) {		
		return (IQuery)subqueryMap.get(fieldId);
	}	
	
	/**
	 * Checks if is subquery defined.
	 * 
	 * @param fieldId the field id
	 * 
	 * @return true, if is subquery defined
	 */
	public boolean isSubqueryDefined(String fieldId) {
		return getSubquery(fieldId) != null;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#selectSubquery(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#saveSelectedSubquery()
	 */
	public void saveSelectedSubquery() {
		if( isSubqueryModeActive() ) {
			subqueryMap.put(getSubqueryFieldId(), getSelectedSubquery());
		}
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#deselectSubquery()
	 */
	public void deselectSubquery() {
		setSelectedSubquery(null);
		setSubqueryFieldId(null);
		setSubqueryModeActive(false);
	}	

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getSubQueryIdForSubQueryOnField(java.lang.String)
	 */
	public String getSubQueryIdForSubQueryOnField(String fieldId) {
		return (String)mapFieldIdSubQueryId.get(fieldId);
	}

	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getErrMsg()
	 */
	public String getErrMsg() {
		return errMsg;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getSubqueryErrMsg(java.lang.String)
	 */
	public String getSubqueryErrMsg(String fieldId) {
		if(isSubqueryDefined(fieldId)) {
			IQuery subquery = getSubquery(fieldId);
			if( isSubqueryValid(subquery) == false) {
				return subquery.getErrMsg();
			}
		}		
		
		return null;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#setErrMsg(java.lang.String)
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}	
	
	// ... private
	
	/**
	 * Sets the subquery mode active.
	 * 
	 * @param subqueryModeActive the new subquery mode active
	 */
	private void setSubqueryModeActive(boolean subqueryModeActive) {
		this.subqueryModeActive = subqueryModeActive;
	}	
	
	/**
	 * Gets the selected subquery.
	 * 
	 * @return the selected subquery
	 */
	private IQuery getSelectedSubquery() {
		return selectedSubquery;
	}
	
	/**
	 * Sets the selected subquery.
	 * 
	 * @param selectedSubquery the new selected subquery
	 */
	private void setSelectedSubquery(IQuery selectedSubquery) {
		this.selectedSubquery = selectedSubquery;
	}
	
	/**
	 * Adds the empty subquery.
	 * 
	 * @param fieldId the field id
	 */
	private void addEmptySubquery(String fieldId) {		
		addSubquery(fieldId, new Query());
	}
	
	/**
	 * Adds the subquery.
	 * 
	 * @param fieldId the field id
	 * @param subquery the subquery
	 */
	private void addSubquery(String fieldId, IQuery subquery) {		
		subqueryMap.put(fieldId, subquery);
		mapFieldIdSubQueryId.put(fieldId, getNewSubQueryId());
	}
		
	
	/**
	 * Gets the new sub query id.
	 * 
	 * @return the new sub query id
	 */
	private String getNewSubQueryId(){
		subQueryCounter++;
		return "sub"+this.subQueryCounter;
	}
	
	/**
	 * Gets the subquery map.
	 * 
	 * @return the subquery map
	 */
	private Map getSubqueryMap() {
		return subqueryMap;
	}
	
	/**
	 * Sets the subquery map.
	 * 
	 * @param subqueryMap the new subquery map
	 */
	private void setSubqueryMap(Map subqueryMap) {
		this.subqueryMap = subqueryMap;
	}
	
	/**
	 * Gets the map field id sub q uery id.
	 * 
	 * @return the map field id sub q uery id
	 */
	public Map getMapFieldIdSubQUeryId() {
		return mapFieldIdSubQueryId;
	}

	/**
	 * Sets the map field id sub q uery id.
	 * 
	 * @param mapFieldIdSubQUeryId the new map field id sub q uery id
	 */
	public void setMapFieldIdSubQUeryId(Map mapFieldIdSubQUeryId) {
		this.mapFieldIdSubQueryId = mapFieldIdSubQUeryId;
	}
	
	/**
	 * Sets the subquery field id.
	 * 
	 * @param fieldId the new subquery field id
	 */
	private void setSubqueryFieldId(String fieldId) {
		subqueryFieldId = fieldId;
	}
	
	/**
	 * Gets the sub query counter.
	 * 
	 * @return the sub query counter
	 */
	private int getSubQueryCounter() {
		return subQueryCounter;
	}

	/**
	 * Sets the sub query counter.
	 * 
	 * @param subQueryCounter the new sub query counter
	 */
	private void setSubQueryCounter(int subQueryCounter) {
		this.subQueryCounter = subQueryCounter;
	}
	


	

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#getCopy()
	 */
	public IQuery getCopy() {
		// TO BE IMPLEMENTED
		return this;
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#isSubqueryValid(it.eng.qbe.query.IQuery)
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#isSelectedSubqueryValid()
	 */
	public boolean isSelectedSubqueryValid() {
		IQuery subquery = getSelectedSubquery();
		return isSubqueryValid(subquery);
	}
	
	/**
	 * Checks if is subquery valid.
	 * 
	 * @param fieldId the field id
	 * 
	 * @return true, if is subquery valid
	 */
	public boolean isSubqueryValid(String fieldId) {
		IQuery subquery = getSubquery(fieldId);
		return isSubqueryValid(subquery);
	}
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IQuery#findPositionOf(java.lang.String)
	 */
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
