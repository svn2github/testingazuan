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
package it.eng.qbe.model;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.newquery.ExpressionNode;
import it.eng.qbe.newquery.GroupByField;
import it.eng.qbe.newquery.OrderByField;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.newquery.SelectField;
import it.eng.qbe.newquery.WhereField;
import it.eng.qbe.utility.StringUtils;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.assertion.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;

/**
 * The Class HQLStatement.
 */
public class XHQLStatement extends XBasicStatement {
	
	public static interface IConditionalOperator {	
		String apply(String leftHandValue, String rightHandValue);
	}
	public static Map conditionalOperators;
	
	static {
		conditionalOperators = new HashMap();
		conditionalOperators.put(WhereField.EQUALS_TO, new IConditionalOperator() {
			public String getName() {return WhereField.EQUALS_TO;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				return leftHandValue + "=" + rightHandValue;
			}
		});
		conditionalOperators.put(WhereField.NOT_EQUALS_TO, new IConditionalOperator() {
			public String getName() {return WhereField.NOT_EQUALS_TO;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				return leftHandValue + "!=" + rightHandValue;
			}
		});
		conditionalOperators.put(WhereField.GREATER_THAN, new IConditionalOperator() {
			public String getName() {return WhereField.GREATER_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				return leftHandValue + ">" + rightHandValue;
			}
		});
		conditionalOperators.put(WhereField.EQUALS_OR_GREATER_THAN, new IConditionalOperator() {
			public String getName() {return WhereField.EQUALS_OR_GREATER_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				return leftHandValue + ">=" + rightHandValue;
			}
		});
		conditionalOperators.put(WhereField.LESS_THAN, new IConditionalOperator() {
			public String getName() {return WhereField.LESS_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				return leftHandValue + "<" + rightHandValue;
			}
		});
		conditionalOperators.put(WhereField.EQUALS_OR_LESS_THAN, new IConditionalOperator() {
			public String getName() {return WhereField.EQUALS_OR_LESS_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				return leftHandValue + "<=" + rightHandValue;
			}
		});
		conditionalOperators.put(WhereField.STARTS_WITH, new IConditionalOperator() {
			public String getName() {return WhereField.STARTS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {	
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = rightHandValue + "%";
				return leftHandValue + " like '" + rightHandValue + "'";
			}
		});
		conditionalOperators.put(WhereField.NOT_STARTS_WITH, new IConditionalOperator() {
			public String getName() {return WhereField.NOT_STARTS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = rightHandValue + "%";
				return leftHandValue + " not like '" + rightHandValue + "'";
			}
		});
		conditionalOperators.put(WhereField.ENDS_WITH, new IConditionalOperator() {
			public String getName() {return WhereField.ENDS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = "%" + rightHandValue;
				return leftHandValue + " like '" + rightHandValue + "'";
			}
		});
		conditionalOperators.put(WhereField.NOT_ENDS_WITH, new IConditionalOperator() {
			public String getName() {return WhereField.NOT_ENDS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = "%" + rightHandValue;
				return leftHandValue + " not like '" + rightHandValue + "'";
			}
		});		
		conditionalOperators.put(WhereField.CONTAINS, new IConditionalOperator() {
			public String getName() {return WhereField.CONTAINS;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertNotNull(rightHandValue, "Operand cannot be null when the operator is " + getName());
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = "%" + rightHandValue + "%";
				return leftHandValue + " like '" + rightHandValue + "'";
			}
		});
		conditionalOperators.put(WhereField.IS_NULL, new IConditionalOperator() {
			public String getName() {return WhereField.IS_NULL;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + " IS NULL";
			}
		});
		conditionalOperators.put(WhereField.NOT_NULL, new IConditionalOperator() {
			public String getName() {return WhereField.NOT_NULL;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + " IS NOT NULL";
			}
		});
		
		conditionalOperators.put(WhereField.BETWEEN, new IConditionalOperator() {
			public String getName() {return WhereField.BETWEEN;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertTrue(rightHandValue.contains(","), "When  BEETWEEN operator is used the operand have to be specified in the following form: minVal,MaxVal");
				String[] bounds = rightHandValue.split(",");
				Assert.assertTrue(bounds.length == 2, "When  BEETWEEN operator is used the operand have to be specified in the following form: minVal,MaxVal");
				return leftHandValue + " BETWEEN " + bounds[0] + " AND " + bounds[1];
			}
		});
		conditionalOperators.put(WhereField.NOT_BETWEEN, new IConditionalOperator() {
			public String getName() {return WhereField.NOT_BETWEEN;}
			public String apply(String leftHandValue, String rightHandValue) {
				Assert.assertTrue(rightHandValue.contains(","), "When  NOT BEETWEEN operator is used the operand have to be specified in the following form: minVal,MaxVal");
				String[] bounds = rightHandValue.split(",");
				Assert.assertTrue(bounds.length == 2, "When  NOT BEETWEEN operator is used the operand have to be specified in the following form: minVal,MaxVal");
				return leftHandValue + " NOT BETWEEN " + bounds[0] + " AND " + bounds[1];
			}
		});
		
		conditionalOperators.put(WhereField.IN, new IConditionalOperator() {
			public String getName() {return WhereField.IN;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + " IN (" +  rightHandValue + ")";
			}
		});
		conditionalOperators.put(WhereField.NOT_IN, new IConditionalOperator() {
			public String getName() {return WhereField.NOT_IN;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + " NOT IN (" +  rightHandValue + ")";
			}
		});
	}
	
	
	protected XHQLStatement(IDataMartModel dataMartModel) {
		super(dataMartModel);
	}
	
	
	protected XHQLStatement(IDataMartModel dataMartModel, Query query) {
		super(dataMartModel, query);
	}
	


	
	private String buildSelectClause(Query query, Map entityAliases) {
		StringBuffer buffer = new StringBuffer();
		List selectFields = query.getSelectFields();
		
		if(selectFields == null ||selectFields.size() == 0) {
			return "";
		}
		
		buffer.append("SELECT");
		
		Iterator it = selectFields.iterator();
		while( it.hasNext() ) {
			SelectField selectField = (SelectField)it.next();
			DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(selectField.getUniqueName());
			DataMartEntity entity = datamartField.getParent().getRoot(); 
			String queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(entity.getUniqueName())) {
				entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
			}
			String entityAlias = (String)entityAliases.get( entity.getUniqueName() );
			String fieldName = entityAlias + "." + queryName;
			buffer.append(" " + selectField.getFunction().apply(fieldName));
			if( it.hasNext() ) {
				buffer.append(",");
			}
		}
		
		return buffer.toString().trim();
	}
	
	private List getJoinFields(Query query) {
		List joinFields = new ArrayList();
		Iterator it = query.getWhereFields().iterator();
		while( it.hasNext() ) {
			WhereField whereField = (WhereField)it.next();
			if( "LEFT JOIN ON".equalsIgnoreCase( whereField.getOperator() ) 
					|| "RIGHT JOIN ON".equalsIgnoreCase( whereField.getOperator() ) ) {
				joinFields.add(whereField);
			}
			
		}
		return joinFields;
	}
	private String buildFromClause(Map entityAliases) {
		StringBuffer buffer = new StringBuffer();
		
		if(entityAliases == null || entityAliases.keySet().size() == 0) {
			return "";
		}
		
		buffer.append(" FROM ");
		
		Iterator joins = getJoinFields(query).iterator();
		int n = 0;
		while(joins.hasNext()) {
			WhereField joinField = (WhereField)joins.next();
			
			WhereField whereField;
			DataMartField datamartField;
			DataMartEntity lentity;
			DataMartEntity rentity;
			String queryName;
			String lentityAlias;
			String rentityAlias;
			
			
			String leftHandValue = null;
			datamartField = getDataMartModel().getDataMartModelStructure().getField(joinField.getUniqueName());
			lentity = datamartField.getParent().getRoot(); 
			queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(lentity.getUniqueName())) {
				lentityAlias = "t_" + entityAliases.keySet().size() + (n++);
			} else {
				lentityAlias = (String)entityAliases.get( lentity.getUniqueName() );	
				entityAliases.remove( lentity.getUniqueName() );
			}			
			leftHandValue = lentityAlias + "." + queryName;
			
			String rightHandValue = null;
			datamartField = getDataMartModel().getDataMartModelStructure().getField( joinField.getOperand().toString() );
			rentity = datamartField.getParent().getRoot(); 
			queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(rentity.getUniqueName())) {
				rentityAlias = "t_" + entityAliases.keySet().size() + (n++);
			} else {
				rentityAlias = (String)entityAliases.get( rentity.getUniqueName() );	
				entityAliases.remove( rentity.getUniqueName() );
			}		
			rightHandValue = rentityAlias + "." + queryName;
			
			buffer.append(lentity.getType() + " " + lentityAlias  
							+ " LEFT OUTER JOIN " 
							+ rentity.getType() + " " + rentityAlias  
							+ " ON "
							+ leftHandValue + " = " + rightHandValue);
			
			if(joins.hasNext() || entityAliases.keySet().iterator().hasNext()) {
				buffer.append(", ");
			}
			
		}
		
		Iterator it = entityAliases.keySet().iterator();
		while( it.hasNext() ) {
			String entityUniqueName = (String)it.next();
			String entityAlias = (String)entityAliases.get(entityUniqueName);
			DataMartEntity datamartEntity =  getDataMartModel().getDataMartModelStructure().getEntity(entityUniqueName);
			
			buffer.append(" " + datamartEntity.getType() + " " + entityAlias);
			if( it.hasNext() ) {
				buffer.append(",");
			}
		}
		
		return buffer.toString().trim();
	}
	
	/*
	private List getWhereFields(Query query) {
		List whereFields = new ArrayList();
		Iterator it = query.getWhereFields().iterator();
		while( it.hasNext() ) {
			WhereField whereField = (WhereField)it.next();
			if( "LEFT JOIN ON".equalsIgnoreCase( whereField.getOperator() ) 
					|| "RIGHT JOIN ON".equalsIgnoreCase( whereField.getOperator() ) ) {
				continue;
			}
			whereFields.add(whereField);
		}
		return whereFields;
	}
	*/
	
	private String buildUserProvidedWhereField(WhereField whereField, Query query, Map entityAliases) {
		String leftHandValue = null;
		DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(whereField.getUniqueName());
		DataMartEntity entity = datamartField.getParent().getRoot(); 
		String queryName = datamartField.getQueryName();
		if(!entityAliases.containsKey(entity.getUniqueName())) {
			entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
		}
		String entityAlias = (String)entityAliases.get( entity.getUniqueName() );				
		leftHandValue = entityAlias + "." + queryName;
		
		
		String rightHandValue = null;
		
		if("Field Content".equalsIgnoreCase( whereField.getOperandType() ) ) {
			datamartField = getDataMartModel().getDataMartModelStructure().getField( whereField.getOperand().toString() );
			entity = datamartField.getParent().getRoot(); 
			queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(entity.getUniqueName())) {
				entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
			}
			entityAlias = (String)entityAliases.get( entity.getUniqueName() );
			rightHandValue = entityAlias + "." + queryName;
		} else if("Static Value".equalsIgnoreCase( whereField.getOperandType() ) ) {
			rightHandValue = whereField.getOperand().toString();
			
			if(datamartField.getType().equalsIgnoreCase("String")) {
				if( !( whereField.IN.equalsIgnoreCase( whereField.getOperator() ) 
						|| whereField.IN.equalsIgnoreCase( whereField.getOperator() )
						|| whereField.NOT_IN.equalsIgnoreCase( whereField.getOperator() )
						|| whereField.BETWEEN.equalsIgnoreCase( whereField.getOperator() )
						|| whereField.NOT_BETWEEN.equalsIgnoreCase( whereField.getOperator() ) 
				)) {
					rightHandValue = "'" + rightHandValue + "'";
				} else {
					String[] items = rightHandValue.split(",");
					rightHandValue = "";
					for(int i = 0; i < items.length; i++) {
						rightHandValue += (i==0?"":",") + "'" + items[i] + "'";
					}					
				}
			}
		} else if("Subquery".equalsIgnoreCase( whereField.getOperandType() ) ) {
			Assert.assertUnreachable("Filter of type subquery are not still supported.");
		} else {
			Assert.assertUnreachable("Unrecognized filter type: " + whereField.getOperandType());
		}		
		
		IConditionalOperator conditionalOperator = null;
		conditionalOperator = (IConditionalOperator)conditionalOperators.get( whereField.getOperator() );
		Assert.assertNotNull(conditionalOperator, "Unsopported operator " + whereField.getOperator() + " used in query definition");
		
		
		return conditionalOperator.apply(leftHandValue, rightHandValue) ;
	}
	
	private String buildUserProvidedWhereClause(ExpressionNode filterExp, Query query, Map entityAliases) {
		String str = "";
		
		String type = filterExp.getType();
		if("NODE_OP".equalsIgnoreCase( type )) {
			for(int i = 0; i < filterExp.getChildNodes().size(); i++) {
				ExpressionNode child = (ExpressionNode)filterExp.getChildNodes().get(i);
				String childStr = buildUserProvidedWhereClause(child, query, entityAliases);
				if("NODE_OP".equalsIgnoreCase( child.getType() )) {
					childStr = "(" + childStr + ")";
				}
				str += (i==0?"": " " + filterExp.getValue());
				str += " " + childStr;
			}
		} else {
			WhereField whereField = query.getWhereFieldByName( filterExp.getValue() );
			str += buildUserProvidedWhereField(whereField, query, entityAliases);
		}
		
		return str;
	}
	
	private String buildWhereClause(Query query, Map entityAliases) {
		StringBuffer buffer = new StringBuffer();
		if( query.getWhereClauseStructure() != null) {
			buffer.append("WHERE ");
			buffer.append( buildUserProvidedWhereClause(query.getWhereClauseStructure(), query, entityAliases) );
		}
		
		/*
		List whereFields = getWhereFields(query);
		
		if(whereFields != null && whereFields.size() > 0) {		
			buffer.append("WHERE");
			
			Iterator it = whereFields.iterator();
			while( it.hasNext() ) {
				
				WhereField whereField = (WhereField)it.next();
				
				String leftHandValue = null;
				DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(whereField.getUniqueName());
				DataMartEntity entity = datamartField.getParent().getRoot(); 
				String queryName = datamartField.getQueryName();
				if(!entityAliases.containsKey(entity.getUniqueName())) {
					entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
				}
				String entityAlias = (String)entityAliases.get( entity.getUniqueName() );				
				leftHandValue = entityAlias + "." + queryName;
				
				
				String rightHandValue = null;
				
				if("Field Content".equalsIgnoreCase( whereField.getOperandType() ) ) {
					datamartField = getDataMartModel().getDataMartModelStructure().getField( whereField.getOperand().toString() );
					entity = datamartField.getParent().getRoot(); 
					queryName = datamartField.getQueryName();
					if(!entityAliases.containsKey(entity.getUniqueName())) {
						entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
					}
					entityAlias = (String)entityAliases.get( entity.getUniqueName() );
					rightHandValue = entityAlias + "." + queryName;
				} else if("Static Value".equalsIgnoreCase( whereField.getOperandType() ) ) {
					rightHandValue = whereField.getOperand().toString();
					
					if(datamartField.getType().equalsIgnoreCase("String")) {
						rightHandValue = "'" + rightHandValue + "'";
					}
				} else if("Subquery".equalsIgnoreCase( whereField.getOperandType() ) ) {
					
				} else {
					
				}
				
				
				
				
				IConditionalOperator conditionalOperator = null;
				conditionalOperator = (IConditionalOperator)conditionalOperators.get( whereField.getOperator() );
				Assert.assertNotNull(conditionalOperator, "Unsopported operator " + whereField.getOperator() + " used in query definition");
				
				
				buffer.append(" " + conditionalOperator.apply(leftHandValue, rightHandValue) );
				
				if( it.hasNext() ) {
					buffer.append(" AND");
				}
			}
			
		}
		*/
		
		/////////////////////////////////////////////////////////////////////////
		DataMartModelStructure dataMartModelStructure = dataMartModel.getDataMartModelStructure();
		DataMartModelAccessModality dataMartModelAccessModality = dataMartModel.getDataMartModelAccessModality();
		
		Iterator it = entityAliases.keySet().iterator();
		while(it.hasNext()){
			String entityUniqueName = (String)it.next();
			DataMartEntity entity = dataMartModelStructure.getEntity( entityUniqueName );
			
			// check for condition filter on this entity
			List filters = dataMartModelAccessModality.getEntityFilterConditions(entity.getType());
			for(int i = 0; i < filters.size(); i++) {
				Filter filter = (Filter)filters.get(i);
				Set fields = filter.getFields();
				Properties props = new Properties();
				Iterator fieldIterator = fields.iterator();
				while(fieldIterator.hasNext()) {
					String fieldName = (String)fieldIterator.next();
					String entityAlias = (String)entityAliases.get(entityUniqueName);
					props.put(fieldName, entityAlias + "." + fieldName);
				}
				String filterCondition = null;
				try {
					filterCondition = StringUtils.replaceParameters(filter.getFilterCondition(), "F", props);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(filterCondition != null) {
					if(buffer.toString().length() > 0) {
						buffer.append(" and ");
					} else {
						buffer.append("where ");
					}
					buffer.append(filterCondition + " ");
				}
			}
			
			//	check for condition filter on sub entities
			List subEntities = entity.getAllSubEntities();
			for(int i = 0; i < subEntities.size(); i++) {
				DataMartEntity subEntity = (DataMartEntity)subEntities.get(i);
				filters = dataMartModelAccessModality.getEntityFilterConditions(subEntity.getType());
				for(int j = 0; j < filters.size(); j++) {
					Filter filter = (Filter)filters.get(j);
					Set fields = filter.getFields();
					Properties props = new Properties();
					Iterator fieldIterator = fields.iterator();
					while(fieldIterator.hasNext()) {
						String fieldName = (String)fieldIterator.next();
						DataMartField filed = null;
						Iterator subEntityFields = subEntity.getAllFields().iterator();
						while(subEntityFields.hasNext()) {
							filed = (DataMartField)subEntityFields.next();
							if(filed.getQueryName().endsWith("." + fieldName)) break;
						}
						String entityAlias = (String)entityAliases.get(entityUniqueName);
						props.put(fieldName, entityAlias + "." + filed.getQueryName());
					}
					String filterCondition = null;
					try {
						filterCondition = StringUtils.replaceParameters(filter.getFilterCondition(), "F", props);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if(filterCondition != null) {
						if(buffer.toString().length() > 0) {
							buffer.append(" and ");
						} else {
							buffer.append("where ");
						}
						buffer.append(filterCondition + " ");
					}
				}
			}
			
			
		}
		
		
		return buffer.toString().trim();
	}
	
	private String buildGroupByClause(Query query, Map entityAliases) {
		StringBuffer buffer = new StringBuffer();
		List groupByFields = query.getGroupByFields();
		
		if(groupByFields == null ||groupByFields.size() == 0) {
			return "";
		}
		
		buffer.append("GROUP BY");
		
		Iterator it = groupByFields.iterator();
		while( it.hasNext() ) {
			GroupByField groupByField = (GroupByField)it.next();
			DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(groupByField.getUniqueName());
			DataMartEntity entity = datamartField.getParent().getRoot(); 
			String queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(entity.getUniqueName())) {
				entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
			}
			String entityAlias = (String)entityAliases.get( entity.getUniqueName() );
			String fieldName = entityAlias + "." +queryName;
			buffer.append(" " + fieldName);
			if( it.hasNext() ) {
				buffer.append(",");
			}
		}
		
		return buffer.toString().trim();
	}
	
	// TODO move this into query class
	private List getOrderByFields(Query query) {
		List orderByFields = new ArrayList();
		Iterator it = query.getSelectFields().iterator();
		while( it.hasNext() ) {
			SelectField selectField = (SelectField)it.next();
			if(selectField.isOrderByField()) {
				orderByFields.add(selectField);
			}
		}
		return orderByFields;
	}
	private String buildOrderByClause(Query query, Map entityAliases) {
		StringBuffer buffer;
		Iterator it;
		SelectField selectField;
		
		it = getOrderByFields(query).iterator();		
		if(!it.hasNext()) {
			return "";
		}
		
		buffer = new StringBuffer();	
		buffer.append("ORDER BY");
					
		while( it.hasNext() ) {
			selectField = (SelectField)it.next();
			
			Assert.assertTrue(selectField.isOrderByField(), "Field [" + selectField.getUniqueName() +"] is not an orderBy filed");
			
			DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(selectField.getUniqueName());
			DataMartEntity entity = datamartField.getParent().getRoot(); 
			String queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(entity.getUniqueName())) {
				entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
			}
			String entityAlias = (String)entityAliases.get( entity.getUniqueName() );
			String fieldName = entityAlias + "." + queryName;
			buffer.append(" " + selectField.getFunction().apply(fieldName));
			buffer.append(" " + (selectField.isAscendingOrder()?"ASC": "DESC") );
						
			if( it.hasNext() ) {
				buffer.append(",");
			}
		}
		
		return buffer.toString().trim();
		
		/*
		StringBuffer buffer = new StringBuffer();
		List orderByFields = query.getOrderByFields();
		
		if(orderByFields == null ||orderByFields.size() == 0) {
			return "";
		}
		
		Iterator it = orderByFields.iterator();
		while( it.hasNext() ) {
			OrderByField orderByField = (OrderByField)it.next();
			DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(orderByField.getUniqueName());
			DataMartEntity entity = datamartField.getParent().getRoot(); 
			String queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(entity.getUniqueName())) {
				entityAliases.put(entity.getUniqueName(), "t_" + entityAliases.keySet().size());
			}
			String entityAlias = (String)entityAliases.get( entity.getUniqueName() );
			String fieldName = entityAlias + "." + queryName;
			
			buffer.append(" " + fieldName + " " + (orderByField.isAscendingOrder()?"ASC": "DESC") );
			if( it.hasNext() ) {
				buffer.append(",");
			}
		}
		
		return buffer.toString().trim();
		*/
	}
	
	
	public String getQueryString(Query query) {		
		try {
			return getQueryString(query, parameters);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public String getQueryString(Query query, Map parameters) throws IOException {		
		String queryStr;
		String selectClause;
		String whereClause;
		String groupByClause;
		String orderByClause;
		String fromClause;
		Map entityAliases;
		
		Assert.assertNotNull(query, "Input parameter 'query' cannot be null");
		Assert.assertTrue(!query.isEmpty(), "Input query cannot be empty (i.e. with no selected fields)");
		
		entityAliases = new HashMap();
		
		selectClause = buildSelectClause(query, entityAliases);
		whereClause = buildWhereClause(query, entityAliases);
		groupByClause = buildGroupByClause(query, entityAliases);
		orderByClause = buildOrderByClause(query, entityAliases);
		fromClause = buildFromClause(entityAliases);
		
		queryStr = selectClause + " " + fromClause + " " + whereClause + " " +  groupByClause + " " + orderByClause;
		queryStr = (parameters != null)
						? StringUtils.replaceParameters(queryStr.trim(), "P", parameters)
						: queryStr;
		
		return queryStr;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#execute(int, int)
	 */
	public SourceBean execute(int offset, int fetchSize) throws Exception {
		return execute(query, parameters, offset, fetchSize, maxResults);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#execute(int)
	 */
	public SourceBean execute(int offset) throws Exception {
		return execute(query, parameters, offset, fetchSize, maxResults);
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#execute()
	 */
	public SourceBean execute() throws Exception {
		return execute(query, parameters, offset, fetchSize, maxResults);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#execute(it.eng.qbe.query.IQuery, java.util.Properties, int, int, int)
	 */
	public SourceBean execute(Query query, Map parameters, int offset, int fetchSize, int maxResults) throws Exception {
		Session session = null;
		try{		
			session = dataMartModel.getDataSource().getSessionFactory().openSession();
			
			String queryString = getQueryString(query, parameters);	
			System.out.println("NEW: " + queryString);
				
			org.hibernate.Query hibernateQuery = session.createQuery(queryString);			
			
			// check for overflow
			if(maxResults >= 0) hibernateQuery.setMaxResults(maxResults);
			int resultNumber = hibernateQuery.list().size();
			boolean overflow = (resultNumber == maxResults);
						
			// get query results
			hibernateQuery.setFirstResult(offset < 0 ? 0 : offset);
			//if(fetchSize >= 0 && fetchSize < resultNumber) {
				hibernateQuery.setMaxResults(fetchSize);			
			//}
			List result = hibernateQuery.list();
				
			// build the source bean that holds the resultset				
			SourceBean resultSetSB = new SourceBean("QUERY_RESPONSE_SOURCE_BEAN");
			resultSetSB.setAttribute("query", queryString);
			resultSetSB.setAttribute("offset", new Integer(offset));
			resultSetSB.setAttribute("fetchSize", new Integer(fetchSize));
			resultSetSB.setAttribute("maxResults", new Integer(maxResults));
			resultSetSB.setAttribute("resultNumber", new Integer(resultNumber));
			resultSetSB.setAttribute("overflow", new Boolean(overflow));
			resultSetSB.setAttribute("list", result);
			
			
			session.close();
						
			return resultSetSB;
		}finally {
			if (session != null && session.isOpen())
			session.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#executeWithPagination(int, int, int)
	 */
	public SourceBean executeWithPagination(int offset, int fetchSize, int maxResults) throws Exception {
		return execute(query, parameters, offset, fetchSize, maxResults);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#executeWithPagination(int, int)
	 */
	public SourceBean executeWithPagination(int pageNumber, int pageSize) throws Exception {
		return executeWithPagination(query, parameters, pageNumber, pageSize, maxResults);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#executeWithPagination(it.eng.qbe.query.IQuery, java.util.Properties, int, int, int)
	 */
	public SourceBean executeWithPagination(Query query, Map parameters, int pageNumber, int pageSize, int maxResults) throws Exception {
		SourceBean resultSetSB = execute(query, parameters, pageNumber * pageSize, pageSize, maxResults);
		
		Session session = null;
		try{		
			session = dataMartModel.getDataSource().getSessionFactory().openSession();
			
			Integer resultNumber = (Integer)resultSetSB.getAttribute("resultNumber");
			
			int pagesNumber = 1;
			if(pageSize > 0) {
				pagesNumber = (resultNumber.intValue() / pageSize) + ( ((resultNumber.intValue() % pageSize) != 0)? 1: 0);
			}
			
			boolean hasNextPage = false;
			if(pageSize > 0) hasNextPage = resultNumber.intValue() > ((pageNumber+1) * pageSize);
			
			boolean hasPrevPage = (pageNumber > 0);
			
			 
			
			resultSetSB.setAttribute("currentPage", new Integer(pageNumber));
			resultSetSB.setAttribute("pagesNumber", new Integer(pagesNumber));
			resultSetSB.setAttribute("hasNextPage", new Boolean(hasNextPage));
			resultSetSB.setAttribute("hasPreviousPage", new Boolean(hasPrevPage));			
			
			session.close();
						
			return resultSetSB;
		}finally {
			if (session != null && session.isOpen())
			session.close();
		}
	}
}
