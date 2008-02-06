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
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface IQuery {
	
	String getQueryId();	
	void setQueryId(String queryId);
	
	/////////////////////////////////////////////////////////////////////////////////////
	// SELECT
	////////////////////////////////////////////////////////////////////////////////////
	
	void deleteSelectClause();
	void deleteSelectField(String fieldId);
	void deleteCalculatedField(String fieldId) ;
	
	Iterator getSelectFieldsIterator();	
	Iterator getCalculatedFieldsIterator();	
	
	ISelectField addSelectField(String className, String fieldName, String fieldLabel,
			String fieldHibType, String fieldHibScale, String fieldHibPrec);
	
	CalculatedField addCalculatedField(String fieldId, 
			   String fieldNameInQuery, String classNameInQuery, 
			   Formula formula,
			   IHibernateDataSource dataSource);
	
	void moveDownSelectField(String fieldId);	
	void moveUpSelectField(String fieldId);
	
	/////////////////////////////////////////////////////////////////////////////////////
	// FROM
	////////////////////////////////////////////////////////////////////////////////////
		
	Iterator  getEntityClassesIterator();
	boolean containEntityClass(EntityClass ec);	
	void addEntityClass(EntityClass ec);	
	
	void purgeNotReferredEntityClasses();
	void purgeNotReferredEntityClasses(String prefix);
	
	/////////////////////////////////////////////////////////////////////////////////////
	// WHERE
	////////////////////////////////////////////////////////////////////////////////////
	
	void deleteWhereClause();	
	void deleteWhereField(String fieldId);
	
	Iterator getWhereFieldsIterator();
	
	IWhereField addWhereField(String fieldName, String type);
	
	void moveDownWhereField(String fieldId);
	void moveUpWhereField(String fieldId);	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// GROUP BY
	////////////////////////////////////////////////////////////////////////////////////

	void deleteGroupByClause();
	void deleteGroupByField(String fieldId);
	
	IGroupByField addGroupByField(String fieldName);
	
	Iterator getGroupByFieldsIterator();
	
	void moveDownGroupByField(String fieldId);
	void moveUpGroupByField(String fieldId);
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	// ORDER BY
	////////////////////////////////////////////////////////////////////////////////////
	
	void deleteOrderByClause();
	void deleteOrderByField(String fieldId);
	
	IOrderByField addOrderByField(String fieldName);
		
	Iterator getOrderByFieldsIterator();
	
	void moveDownOrderByField(String fieldId);	
	void moveUpOrderByField(String fieldId);
	
	void switchAscendingOrderPopertyValue(String fieldId);

	
		
	void setDistinct(boolean distinct);	
	boolean getDistinct();
	

	void selectSubquery(String fieldId);	
	IQuery getSubquery(String fieldId);	
	String getSubQueryIdForSubQueryOnField(String fieldId);
	
	String[] getDuplicatedAliases();	
	boolean containsDuplicatedAliases();	
	boolean isEmpty();	
	boolean areAllEntitiesJoined();
	
	
	
	
	public String getErrMsg() ;	
	public String getSubqueryErrMsg(String fieldId) ;
	
	// TODO this method should not be public
	void setErrMsg(String errMsg);
	
	IQuery getCopy();
	
	
	Integer findPositionOf(String completeName);
	
	boolean isSubqueryValid(IQuery subquery);
	boolean isSelectedSubqueryValid() ;
	
	/**
	 * 
	 * @deprecated use getSubqueryFieldId
	 */
	String getSubqueryField() ;
	String getSubqueryFieldId();
	void deselectSubquery();

	//void setSubqueryField(String subqueryField);
	void saveSelectedSubquery();

	boolean isSubqueryModeActive() ;

	//void setSubqueryModeActive(boolean subqueryModeActive) ;
}
