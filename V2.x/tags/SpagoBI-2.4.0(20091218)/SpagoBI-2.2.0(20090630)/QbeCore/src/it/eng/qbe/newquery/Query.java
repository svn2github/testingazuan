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
package it.eng.qbe.newquery;

import it.eng.qbe.model.structure.DataMartField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class Query {
	String id;
	String aliasPrefix;
	
	List selectFields;	
	List whereClause;
	List orderByClause;
	List groupByClause;	
	
	ExpressionNode whereClauseStructure;
	Map whereFieldMap;
	
	
	
		
	
	public Query() {
		selectFields = new ArrayList();
		
		whereClause = new ArrayList();
		orderByClause = new ArrayList();
		groupByClause = new ArrayList();
		whereFieldMap = new HashMap();
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
			String boperator) {
		WhereField whereField = new WhereField(fname, fieldUniqueName, fieldUniqueName, operator, operand, operandType, operandDesc, boperator);
		whereClause.add( whereField );
		whereFieldMap.put("$F{" + fname + "}", whereField);
	}
	
	public WhereField getWhereFieldByName(String fname) {
		return (WhereField)whereFieldMap.get(fname.trim());
	}
	
	public void addGroupByField(String fieldUniqueName) {
		groupByClause.add( new GroupByField(fieldUniqueName) );
	}
	
	public void addOrderByField(String fieldUniqueName, boolean isAscendingOrder) {
		orderByClause.add( new OrderByField(fieldUniqueName, isAscendingOrder) );
	}
	
	public List getSelectFields() {
		return selectFields;
	}
	
	public List getWhereFields() {
		return whereClause;
	}
	
	public void addGroupByFiled(GroupByField field) {
		groupByClause.add(field);
	}

	public List getOrderByFields() {
		return orderByClause;
	}

	public List getGroupByFields() {
		return groupByClause;
	}

	public ExpressionNode getWhereClauseStructure() {
		return whereClauseStructure;
	}

	public void setWhereClauseStructure(ExpressionNode whereClauseStructure) {
		this.whereClauseStructure = whereClauseStructure;
	}

}
