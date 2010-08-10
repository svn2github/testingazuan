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

import java.util.Iterator;

// TODO: Auto-generated Javadoc
/**
 * The Interface IQuery.
 */
public interface IQuery {
	
	/**
	 * Gets the query id.
	 * 
	 * @return the query id
	 */
	String getQueryId();	
	
	/**
	 * Sets the query id.
	 * 
	 * @param queryId the new query id
	 */
	void setQueryId(String queryId);
	
	/////////////////////////////////////////////////////////////////////////////////////
	// SELECT
	////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Delete select clause.
	 */
	void deleteSelectClause();
	
	/**
	 * Delete select field.
	 * 
	 * @param fieldId the field id
	 */
	void deleteSelectField(String fieldId);
	
	/**
	 * Delete calculated field.
	 * 
	 * @param fieldId the field id
	 */
	void deleteCalculatedField(String fieldId) ;
	
	/**
	 * Gets the select fields iterator.
	 * 
	 * @return the select fields iterator
	 */
	Iterator getSelectFieldsIterator();	
	
	/**
	 * Gets the calculated fields iterator.
	 * 
	 * @return the calculated fields iterator
	 */
	Iterator getCalculatedFieldsIterator();	
	
	/**
	 * Adds the select field.
	 * 
	 * @param className the class name
	 * @param fieldName the field name
	 * @param fieldLabel the field label
	 * @param fieldHibType the field hib type
	 * @param fieldHibScale the field hib scale
	 * @param fieldHibPrec the field hib prec
	 * 
	 * @return the i select field
	 */
	ISelectField addSelectField(String className, String fieldName, String fieldLabel,
			String fieldHibType, String fieldHibScale, String fieldHibPrec);
	
	/**
	 * Adds the calculated field.
	 * 
	 * @param fieldId the field id
	 * @param fieldNameInQuery the field name in query
	 * @param classNameInQuery the class name in query
	 * @param formula the formula
	 * @param dataSource the data source
	 * 
	 * @return the calculated field
	 */
	CalculatedField addCalculatedField(String fieldId, 
			   String fieldNameInQuery, String classNameInQuery, 
			   Formula formula,
			   IHibernateDataSource dataSource);
	
	/**
	 * Move down select field.
	 * 
	 * @param fieldId the field id
	 */
	void moveDownSelectField(String fieldId);	
	
	/**
	 * Move up select field.
	 * 
	 * @param fieldId the field id
	 */
	void moveUpSelectField(String fieldId);
	
	/////////////////////////////////////////////////////////////////////////////////////
	// FROM
	////////////////////////////////////////////////////////////////////////////////////
		
	/**
	 * Gets the entity classes iterator.
	 * 
	 * @return the entity classes iterator
	 */
	Iterator  getEntityClassesIterator();
	
	/**
	 * Contain entity class.
	 * 
	 * @param ec the ec
	 * 
	 * @return true, if successful
	 */
	boolean containEntityClass(EntityClass ec);	
	
	/**
	 * Adds the entity class.
	 * 
	 * @param ec the ec
	 */
	void addEntityClass(EntityClass ec);	
	
	/**
	 * Purge not referred entity classes.
	 */
	void purgeNotReferredEntityClasses();
	
	/**
	 * Purge not referred entity classes.
	 * 
	 * @param prefix the prefix
	 */
	void purgeNotReferredEntityClasses(String prefix);
	
	/////////////////////////////////////////////////////////////////////////////////////
	// WHERE
	////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Delete where clause.
	 */
	void deleteWhereClause();	
	
	/**
	 * Delete where field.
	 * 
	 * @param fieldId the field id
	 */
	void deleteWhereField(String fieldId);
	
	/**
	 * Gets the where fields iterator.
	 * 
	 * @return the where fields iterator
	 */
	Iterator getWhereFieldsIterator();
	
	/**
	 * Adds the where field.
	 * 
	 * @param fieldName the field name
	 * @param type the type
	 * 
	 * @return the i where field
	 */
	IWhereField addWhereField(String fieldName, String type);
	
	/**
	 * Move down where field.
	 * 
	 * @param fieldId the field id
	 */
	void moveDownWhereField(String fieldId);
	
	/**
	 * Move up where field.
	 * 
	 * @param fieldId the field id
	 */
	void moveUpWhereField(String fieldId);	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// GROUP BY
	////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Delete group by clause.
	 */
	void deleteGroupByClause();
	
	/**
	 * Delete group by field.
	 * 
	 * @param fieldId the field id
	 */
	void deleteGroupByField(String fieldId);
	
	/**
	 * Adds the group by field.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return the i group by field
	 */
	IGroupByField addGroupByField(String fieldName);
	
	/**
	 * Gets the group by fields iterator.
	 * 
	 * @return the group by fields iterator
	 */
	Iterator getGroupByFieldsIterator();
	
	/**
	 * Move down group by field.
	 * 
	 * @param fieldId the field id
	 */
	void moveDownGroupByField(String fieldId);
	
	/**
	 * Move up group by field.
	 * 
	 * @param fieldId the field id
	 */
	void moveUpGroupByField(String fieldId);
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// ORDER BY
	////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Delete order by clause.
	 */
	void deleteOrderByClause();
	
	/**
	 * Delete order by field.
	 * 
	 * @param fieldId the field id
	 */
	void deleteOrderByField(String fieldId);
	
	/**
	 * Adds the order by field.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return the i order by field
	 */
	IOrderByField addOrderByField(String fieldName);
		
	/**
	 * Gets the order by fields iterator.
	 * 
	 * @return the order by fields iterator
	 */
	Iterator getOrderByFieldsIterator();
	
	/**
	 * Move down order by field.
	 * 
	 * @param fieldId the field id
	 */
	void moveDownOrderByField(String fieldId);	
	
	/**
	 * Move up order by field.
	 * 
	 * @param fieldId the field id
	 */
	void moveUpOrderByField(String fieldId);
	
	/**
	 * Switch ascending order poperty value.
	 * 
	 * @param fieldId the field id
	 */
	void switchAscendingOrderPopertyValue(String fieldId);

	
		
	/**
	 * Sets the distinct.
	 * 
	 * @param distinct the new distinct
	 */
	void setDistinct(boolean distinct);	
	
	/**
	 * Gets the distinct.
	 * 
	 * @return the distinct
	 */
	boolean getDistinct();
	

	/**
	 * Select subquery.
	 * 
	 * @param fieldId the field id
	 */
	void selectSubquery(String fieldId);	
	
	/**
	 * Gets the subquery.
	 * 
	 * @param fieldId the field id
	 * 
	 * @return the subquery
	 */
	IQuery getSubquery(String fieldId);	
	
	/**
	 * Gets the sub query id for sub query on field.
	 * 
	 * @param fieldId the field id
	 * 
	 * @return the sub query id for sub query on field
	 */
	String getSubQueryIdForSubQueryOnField(String fieldId);
	
	/**
	 * Gets the duplicated aliases.
	 * 
	 * @return the duplicated aliases
	 */
	String[] getDuplicatedAliases();	
	
	/**
	 * Contains duplicated aliases.
	 * 
	 * @return true, if successful
	 */
	boolean containsDuplicatedAliases();	
	
	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	boolean isEmpty();	
	
	/**
	 * Are all entities joined.
	 * 
	 * @return true, if successful
	 */
	boolean areAllEntitiesJoined();
	
	
	
	
	/**
	 * Gets the err msg.
	 * 
	 * @return the err msg
	 */
	public String getErrMsg() ;	
	
	/**
	 * Gets the subquery err msg.
	 * 
	 * @param fieldId the field id
	 * 
	 * @return the subquery err msg
	 */
	public String getSubqueryErrMsg(String fieldId) ;
	
	// TODO this method should not be public
	/**
	 * Sets the err msg.
	 * 
	 * @param errMsg the new err msg
	 */
	void setErrMsg(String errMsg);
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	IQuery getCopy();
	
	
	/**
	 * Find position of.
	 * 
	 * @param completeName the complete name
	 * 
	 * @return the integer
	 */
	Integer findPositionOf(String completeName);
	
	/**
	 * Checks if is subquery valid.
	 * 
	 * @param subquery the subquery
	 * 
	 * @return true, if is subquery valid
	 */
	boolean isSubqueryValid(IQuery subquery);
	
	/**
	 * Checks if is selected subquery valid.
	 * 
	 * @return true, if is selected subquery valid
	 */
	boolean isSelectedSubqueryValid() ;
	
	/**
	 * Gets the subquery field.
	 * 
	 * @return the subquery field
	 * 
	 * @deprecated use getSubqueryFieldId
	 */
	String getSubqueryField() ;
	
	/**
	 * Gets the subquery field id.
	 * 
	 * @return the subquery field id
	 */
	String getSubqueryFieldId();
	
	/**
	 * Deselect subquery.
	 */
	void deselectSubquery();

	//void setSubqueryField(String subqueryField);
	/**
	 * Save selected subquery.
	 */
	void saveSelectedSubquery();

	/**
	 * Checks if is subquery mode active.
	 * 
	 * @return true, if is subquery mode active
	 */
	boolean isSubqueryModeActive() ;

	//void setSubqueryModeActive(boolean subqueryModeActive) ;
}
