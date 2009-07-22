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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class Query {
	String id;
	String name;
	String description;
	
	boolean distinctClauseEnabled;
	
	List selectFields;	
	List whereClause;
	
	ExpressionNode whereClauseStructure;
	Map whereFieldMap;
	
	Query parentQuery;
	Map subqueries;
	
	public Query() {
		selectFields = new ArrayList();		
		whereClause = new ArrayList();
		whereFieldMap = new HashMap();
		subqueries  = new HashMap();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public boolean isEmpty() {
		for(int i = 0; i < selectFields.size(); i++) {
			SelectField field = (SelectField)selectFields.get(i);
			if(field.isVisible()) return false;
		}
		return true;
	}
	
	public void addSelectFiled(String fieldUniqueName, String function, String fieldAlias, boolean visible,
			boolean groupByField, String orderType) {
		selectFields.add( new SelectField(fieldUniqueName, function, fieldAlias, visible, groupByField, orderType) );
	}
	
	public void addWhereFiled(String fname, String fdesc, 
			String fieldUniqueName, String operator, 
			Object operand, String operandType, String operandDesc,
			String boperator, boolean isFree, String defaultValue, String lastValue) {
		WhereField whereField = new WhereField(fname, fieldUniqueName, fieldUniqueName, operator, operand, operandType, operandDesc, boperator, isFree, defaultValue, lastValue);
		whereClause.add( whereField );
		whereFieldMap.put("$F{" + fname + "}", whereField);
	}
	
	public WhereField getWhereFieldByName(String fname) {
		return (WhereField)whereFieldMap.get(fname.trim());
	}
	
	public List getSelectFields() {
		return selectFields;
	}
	
	public List getWhereFields() {
		return whereClause;
	}

	public boolean isDistinctClauseEnabled() {
		return distinctClauseEnabled;
	}
	
	public void setDistinctClauseEnabled(boolean distinctClauseEnabled) {
		this.distinctClauseEnabled = distinctClauseEnabled;
	}
	
	public List getOrderByFields() {
		List orderByFields = new ArrayList();
		Iterator it = this.getSelectFields().iterator();
		while( it.hasNext() ) {
			SelectField selectField = (SelectField)it.next();
			if(selectField.isOrderByField()) {
				orderByFields.add(selectField);
			}
		}
		return orderByFields;
	}
	
	
	public List getGroupByFields() {
		List groupByFields = new ArrayList();
		Iterator it = this.getSelectFields().iterator();
		while( it.hasNext() ) {
			SelectField selectField = (SelectField)it.next();
			if(selectField.isGroupByField()) {
				groupByFields.add(selectField);
			}
		}
		return groupByFields;
	}
	
	
	public ExpressionNode getWhereClauseStructure() {
		return whereClauseStructure;
	}

	public void setWhereClauseStructure(ExpressionNode whereClauseStructure) {
		this.whereClauseStructure = whereClauseStructure;
	}

	
	public Query getParentQuery() {
		return parentQuery;
	}

	public void setParentQuery(Query parentQuery) {
		this.parentQuery = parentQuery;
	}
	
	public boolean hasParentQuery() {
		return getParentQuery() != null;
	}
	
	public void addSubquery(Query subquery) {
		subqueries.put(subquery.getId(), subquery);
		subquery.setParentQuery(this);
	}
	
	public Query getSubquery(String id) {
		return (Query)subqueries.get(id);
	}
	
	public Set getSubqueryIds() {
		return new HashSet(subqueries.keySet());
	}
	
	public Query removeSubquery(String id) {
		Query subquery = (Query)subqueries.remove(id);
		if(subquery != null) subquery.setParentQuery(null);
		return subquery;
	}

	
	

}
