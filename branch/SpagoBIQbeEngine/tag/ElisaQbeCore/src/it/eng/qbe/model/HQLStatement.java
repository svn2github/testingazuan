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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.query.ExpressionNode;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.SelectField;
import it.eng.qbe.query.WhereField;
import it.eng.qbe.utility.StringUtils;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

/**
 * The Class HQLStatement.
 */
public class HQLStatement extends BasicStatement {
	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(HQLStatement.class);
	
    
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
	
	
	protected HQLStatement(IDataMartModel dataMartModel) {
		super(dataMartModel);
	}
	
	
	protected HQLStatement(IDataMartModel dataMartModel, Query query) {
		super(dataMartModel, query);
	}
	
	public static final String DISTINCT = "DISTINCT";
	public static final String SELECT = "SELECT";
	public static final String FROM = "FROM";
	
	
	private String getNextAlias(Map entityAliasesMaps) {
		int aliasesCount = 0;
		Iterator it = entityAliasesMaps.keySet().iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			Map entityAliases = (Map)entityAliasesMaps.get(key);
			aliasesCount += entityAliases.keySet().size();
		}
		
		return "t_" + aliasesCount;
	}
	
	private String buildSelectClause(Query query, Map entityAliasesMaps) {
		StringBuffer buffer;
		List selectFields;
		SelectField selectField;
		DataMartEntity rootEntity;
		DataMartField datamartField;
		String queryName;
		String rootEntityAlias;
		String selectClauseElement; // rootEntityAlias.queryName
		Map entityAliases;
		
		logger.debug("IN");
		buffer = new StringBuffer();
		try {
			selectFields = query.getSelectFields();
			if(selectFields == null ||selectFields.size() == 0) {
				return "";
			}
			
			entityAliases = (Map)entityAliasesMaps.get(query.getId());
						
			buffer.append(SELECT);		
			if (query.isDistinctClauseEnabled()) {
				buffer.append(" " + DISTINCT);
			}
			
			Iterator it = selectFields.iterator();
			while( it.hasNext() ) {
				selectField = (SelectField)it.next();
				logger.debug("select field unique name [" + selectField.getUniqueName() + "]");
				
				datamartField = getDataMartModel().getDataMartModelStructure().getField(selectField.getUniqueName());
				queryName = datamartField.getQueryName();
				logger.debug("select field query name [" + queryName + "]");
				
				rootEntity = datamartField.getParent().getRoot(); 		
				logger.debug("select field root entity unique name [" + rootEntity.getUniqueName() + "]");
				
				rootEntityAlias = (String)entityAliases.get(rootEntity.getUniqueName());
				if(rootEntityAlias == null) {
					rootEntityAlias = getNextAlias(entityAliasesMaps);
					entityAliases.put(rootEntity.getUniqueName(), rootEntityAlias);
				}
				logger.debug("select field root entity alias [" + rootEntityAlias + "]");
				
				
				selectClauseElement = rootEntityAlias + "." + queryName;
				logger.debug("select clause element before aggregation [" + selectClauseElement + "]");
				
				selectClauseElement = selectField.getFunction().apply(selectClauseElement);
				logger.debug("select clause element after aggregation [" + selectClauseElement + "]");
				
				buffer.append(" " + selectClauseElement);
				if( it.hasNext() ) {
					buffer.append(",");
				}
				logger.debug("select clause element succesfully added to select clause");
			}
		} finally {
			logger.debug("OUT");
		}
		
		return buffer.toString().trim();
	}
	
	private List getJoinFields(Query query) {
		List joinFields = null;
		Iterator it;
		WhereField whereField;
		
		logger.debug("IN");
		try {
			joinFields = new ArrayList();
			it = query.getWhereFields().iterator();
			while( it.hasNext() ) {
				whereField = (WhereField)it.next();
				if( "LEFT JOIN ON".equalsIgnoreCase( whereField.getOperator() ) 
						|| "RIGHT JOIN ON".equalsIgnoreCase( whereField.getOperator() ) ) {
					joinFields.add(whereField);
					logger.debug("Join condition found on field [" + whereField.getUniqueName() + "]");
				}
				
			}
			logger.debug("Join condition found [" + joinFields.size() + "]");
		} finally {
			logger.debug("OUT");
		}
		
		Assert.assertNotNull(joinFields, "join fields list can be empty but not null");
		
		return joinFields;
	}
	
	/**
	 * @deperecated this has been a text: unfortunately hibernate does not support outer joins so the 
	 * possibilities to select JOIN as operator while defining a filter has been removed by the GUI
	 * As a consequence of that this method is expected to always return an empty string 
	 */
	private String buildJoinClause(Query query, Map entityAliases) {
		StringBuffer buffer;
		
		Iterator joins;
		WhereField joinField;
		WhereField whereField;
		DataMartField datamartField;
		DataMartEntity lentity;
		DataMartEntity rentity;
		String queryName;
		String lentityAlias;
		String rentityAlias;
		String leftHandValue;
		
		logger.debug("IN");
		buffer = new StringBuffer();
		try {
			joins = getJoinFields(query).iterator();
			int n = 0;
			while(joins.hasNext()) {
				joinField = (WhereField)joins.next();				
				logger.debug("join left-field unique name [" + joinField.getUniqueName() +"]");
				
				datamartField = getDataMartModel().getDataMartModelStructure().getField(joinField.getUniqueName());
				queryName = datamartField.getQueryName();
				logger.debug("join left-field query name [" + queryName +"]");
				
				
				lentity = datamartField.getParent().getRoot();				
				if(!entityAliases.containsKey(lentity.getUniqueName())) {
					lentityAlias = "jt_" + entityAliases.keySet().size() + (n++);
				} else {
					lentityAlias = (String)entityAliases.get( lentity.getUniqueName() );	
					entityAliases.remove( lentity.getUniqueName() );
				}			
				
				leftHandValue = lentityAlias + "." + queryName;
				logger.debug("join left-field query element [" + leftHandValue +"]");
				
				String rightHandValue = null;
				logger.debug("join right-field unique name [" + joinField.getOperand().toString() +"]");
				
				datamartField = getDataMartModel().getDataMartModelStructure().getField( joinField.getOperand().toString() );
				queryName = datamartField.getQueryName();
				logger.debug("join right-field query name [" + queryName +"]");
							
				rentity = datamartField.getParent().getRoot(); 
				if(!entityAliases.containsKey(rentity.getUniqueName())) {
					rentityAlias = "jt_" + entityAliases.keySet().size() + (n++);
				} else {
					rentityAlias = (String)entityAliases.get( rentity.getUniqueName() );	
					entityAliases.remove( rentity.getUniqueName() );
				}		
				
				rightHandValue = rentityAlias + "." + queryName;
				logger.debug("join right-field query element [" + rightHandValue +"]");
				
				buffer.append(lentity.getType() + " " + lentityAlias  
								+ " LEFT OUTER JOIN " 
								+ rentity.getType() + " " + rentityAlias  
								+ " ON "
								+ leftHandValue + " = " + rightHandValue);
				
				if(joins.hasNext() || entityAliases.keySet().iterator().hasNext()) {
					buffer.append(", ");
				}
				
			}
		} finally {
			logger.debug("OUT");
		}
		
		return buffer.toString().trim();
	}
	
	private String buildFromClause(Query query, Map entityAliasesMaps) {
		StringBuffer buffer;
		
		
		logger.debug("IN");
		buffer = new StringBuffer();
		try {
			Map entityAliases = (Map)entityAliasesMaps.get(query.getId());
			
			
			if(entityAliases == null || entityAliases.keySet().size() == 0) {
				return "";
			}
			
			buffer.append(" " + FROM + " ");
			
			
			// outer join are not supported by hibernate 
			// so this method is expected to return always an empty string
			buffer.append( buildJoinClause(query, entityAliases) );
			
			Iterator it = entityAliases.keySet().iterator();
			while( it.hasNext() ) {
				String entityUniqueName = (String)it.next();
				logger.debug("entity [" + entityUniqueName +"]");
				
				String entityAlias = (String)entityAliases.get(entityUniqueName);
				logger.debug("entity alias [" + entityAlias +"]");
				
				DataMartEntity datamartEntity =  getDataMartModel().getDataMartModelStructure().getEntity(entityUniqueName);
				String whereClauseElement = datamartEntity.getType() + " " + entityAlias;
				logger.debug("where clause element [" + whereClauseElement +"]");
				
				buffer.append(" " + whereClauseElement);
				if( it.hasNext() ) {
					buffer.append(",");
				}
			}
		} finally {
			logger.debug("OUT");
		}
		
		return buffer.toString().trim();
	}
	
	
	public static final String OPERAND_TYPE_STATIC = "Static Value";
	public static final String OPERAND_TYPE_SUBQUERY = "Subquery";
	public static final String OPERAND_TYPE_FIELD = "Field Content";
	public static final String OPERAND_TYPE_PARENT_FIELD = "Parent Field Content";
	
	private String buildUserProvidedWhereField(WhereField whereField, Query query, Map entityAliasesMaps) {
		
		String whereClauseElement;
		Map targetQueryEntityAliasesMap;
		String leftHandValue;
		String rightHandValue;
		DataMartField datamartField;
		DataMartEntity rootEntity;
		String queryName;
		String rootEntityAlias;
		
		
		logger.debug("IN");
		
		try {
			whereClauseElement = "";
			leftHandValue = "";
			rightHandValue = "";
		
			targetQueryEntityAliasesMap = (Map)entityAliasesMaps.get(query.getId());
			Assert.assertNotNull(targetQueryEntityAliasesMap, "Entity aliasses map for query [" + query.getId() + "] cannot be null in order to execute method [buildUserProvidedWhereField]");
			
			
			// build left-hand value
			logger.debug("processing where element left-hand field value ...");
			
			logger.debug("where left-hand field unique name [" + whereField.getUniqueName() + "]");
			
			datamartField = getDataMartModel().getDataMartModelStructure().getField(whereField.getUniqueName());
			Assert.assertNotNull(datamartField, "DataMart does not cantain a field named [" + whereField.getOperand().toString() + "]");
			queryName = datamartField.getQueryName();
			logger.debug("where left-hand field query name [" + queryName + "]");
			
			rootEntity = datamartField.getParent().getRoot(); 
			logger.debug("where left-hand field root entity unique name [" + rootEntity.getUniqueName() + "]");
			
			if(!targetQueryEntityAliasesMap.containsKey(rootEntity.getUniqueName())) {
				logger.debug("Entity [" + rootEntity.getUniqueName() + "] require a new alias");
				rootEntityAlias = getNextAlias(entityAliasesMaps);
				logger.debug("A new alias has been generated [" + rootEntityAlias + "]");				
				targetQueryEntityAliasesMap.put(rootEntity.getUniqueName(), rootEntityAlias);
			}
			rootEntityAlias = (String)targetQueryEntityAliasesMap.get( rootEntity.getUniqueName() );		
			logger.debug("where left-hand field root entity alias [" + rootEntityAlias + "]");
			
			leftHandValue = rootEntityAlias + "." + queryName;
			logger.debug("where element left-hand field value [" + leftHandValue + "]");
			
			
			// build right-hand value
			logger.debug("processing where element right-hand field value ...");
			
			if(OPERAND_TYPE_FIELD.equalsIgnoreCase( whereField.getOperandType() ) ) {
				logger.debug("where element right-hand field type [" + OPERAND_TYPE_FIELD + "]");
				
				logger.debug("where right-hand field unique name [" + whereField.getOperand().toString() + "]");
				
				datamartField = getDataMartModel().getDataMartModelStructure().getField( whereField.getOperand().toString() );
				Assert.assertNotNull(datamartField, "DataMart does not cantain a field named [" + whereField.getOperand().toString() + "]");
				queryName = datamartField.getQueryName();
				logger.debug("where right-hand field query name [" + queryName + "]");
				
				rootEntity = datamartField.getParent().getRoot(); 
				logger.debug("where right-hand field root entity unique name [" + rootEntity.getUniqueName() + "]");
				
				if(!targetQueryEntityAliasesMap.containsKey(rootEntity.getUniqueName())) {
					logger.debug("Entity [" + rootEntity.getUniqueName() + "] require a new alias");
					rootEntityAlias = getNextAlias(entityAliasesMaps);
					logger.debug("A new alias has been generated [" + rootEntityAlias + "]");				
					targetQueryEntityAliasesMap.put(rootEntity.getUniqueName(), rootEntityAlias);
				}
				rootEntityAlias = (String)targetQueryEntityAliasesMap.get( rootEntity.getUniqueName() );
				logger.debug("where right-hand field root entity alias [" + rootEntityAlias + "]");
				
				rightHandValue = rootEntityAlias + "." + queryName;
				logger.debug("where element right-hand field value [" + rightHandValue + "]");
				
			} else if(OPERAND_TYPE_PARENT_FIELD.equalsIgnoreCase( whereField.getOperandType() ) ) {
				String operand;
				String[] chunks;
				String parentQueryId;
				String fieldName;
				
				logger.debug("where element right-hand field type [" + OPERAND_TYPE_FIELD + "]");
				
				// it comes directly from the client side GUI. It is a composition of the parent query id and filed name, 
				// separated by a space
				operand = whereField.getOperand().toString();
				logger.debug("operand  is equals to [" + operand + "]");
				
				chunks = operand.split(" ");
				Assert.assertTrue(chunks.length >= 2, "Operand [" + chunks.toString() + "]does not contains enougth informations in order to resolve the reference to parent field");
				
				parentQueryId = chunks[0];
				logger.debug("where right-hand field belonging query [" + parentQueryId + "]");
				fieldName = chunks[1];
				logger.debug("where right-hand field unique name [" + fieldName + "]");
	
				datamartField = getDataMartModel().getDataMartModelStructure().getField( fieldName );
				Assert.assertNotNull(datamartField, "DataMart does not cantain a field named [" + fieldName + "]");
				
				queryName = datamartField.getQueryName();
				logger.debug("where right-hand field query name [" + queryName + "]");
				
				rootEntity = datamartField.getParent().getRoot();
				logger.debug("where right-hand field root entity unique name [" + rootEntity.getUniqueName() + "]");
				
				Map parentEntityAliases = (Map)entityAliasesMaps.get(parentQueryId);
				if(parentEntityAliases != null) {
					if(!parentEntityAliases.containsKey(rootEntity.getUniqueName())) {
						Assert.assertUnreachable("Filter [" + whereField.getUniqueName() + "] of subquery [" + query.getId() + "] refers to a non " +
								"existing parent query [" + parentQueryId+ "] entity [" + rootEntity.getUniqueName() + "]");
					}
					rootEntityAlias = (String)parentEntityAliases.get( rootEntity.getUniqueName() );
				} else {
					rootEntityAlias = "unresoved_alias";
					logger.warn("Impossible to get aliases map for parent query [" + parentQueryId +"]. Probably the parent query ha not been compiled yet");					
					logger.warn("Query [" + query.getId() +"] refers entities of its parent query [" + parentQueryId +"] so the generated statement wont be executable until the parent query will be compiled");					
				}
				logger.debug("where right-hand field root entity alias [" + rootEntityAlias + "]");
				
				rightHandValue = rootEntityAlias + "." + queryName;
				logger.debug("where element right-hand field value [" + rightHandValue + "]");
				
			} else if(OPERAND_TYPE_STATIC.equalsIgnoreCase( whereField.getOperandType() ) ) {
				
				logger.debug("where element right-hand field type [" + OPERAND_TYPE_STATIC + "]");
				
				if (whereField.isFree()) {
					// get last value first (the last value edited by the user)
					rightHandValue = whereField.getLastValue();
				} else {
					rightHandValue = whereField.getOperand().toString();
				}
				
				logger.debug("where right-hand field value [" + rightHandValue + "]");
				
				logger.debug("where right-hand field type [" + datamartField.getType() + "]");
				
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
				logger.debug("where element right-hand field value [" + rightHandValue + "]");
			} else if(OPERAND_TYPE_SUBQUERY.equalsIgnoreCase( whereField.getOperandType() ) ) {
				String subqueryId;
				
				logger.debug("where element right-hand field type [" + OPERAND_TYPE_SUBQUERY + "]");
				
				subqueryId = (String)whereField.getOperand();
				logger.debug("Referenced subquery [" + subqueryId + "]");
				
				rightHandValue = "Q{" + subqueryId + "}";
				rightHandValue = "( " + rightHandValue + ")";
				logger.debug("where element right-hand field value [" + rightHandValue + "]");
			} else {
				Assert.assertUnreachable("Unrecognized filter type: " + whereField.getOperandType());
			}		
			
			IConditionalOperator conditionalOperator = null;
			conditionalOperator = (IConditionalOperator)conditionalOperators.get( whereField.getOperator() );
			Assert.assertNotNull(conditionalOperator, "Unsopported operator " + whereField.getOperator() + " used in query definition");
			
			
			whereClauseElement = conditionalOperator.apply(leftHandValue, rightHandValue);
			logger.debug("where element value [" + whereClauseElement + "]");
		} finally {
			logger.debug("OUT");
		}
		
		
		return  whereClauseElement;
	}
	
	private String buildUserProvidedWhereClause(ExpressionNode filterExp, Query query, Map entityAliasesMaps) {
		String str = "";
		
		String type = filterExp.getType();
		if("NODE_OP".equalsIgnoreCase( type )) {
			for(int i = 0; i < filterExp.getChildNodes().size(); i++) {
				ExpressionNode child = (ExpressionNode)filterExp.getChildNodes().get(i);
				String childStr = buildUserProvidedWhereClause(child, query, entityAliasesMaps);
				if("NODE_OP".equalsIgnoreCase( child.getType() )) {
					childStr = "(" + childStr + ")";
				}
				str += (i==0?"": " " + filterExp.getValue());
				str += " " + childStr;
			}
		} else {
			WhereField whereField = query.getWhereFieldByName( filterExp.getValue() );
			str += buildUserProvidedWhereField(whereField, query, entityAliasesMaps);
		}
		
		return str;
	}
	
	private String buildWhereClause(Query query, Map entityAliasesMaps) {
	
		StringBuffer buffer = new StringBuffer();
		
		Map entityAliases = (Map)entityAliasesMaps.get(query.getId());
		
		if( query.getWhereClauseStructure() != null) {
			buffer.append("WHERE ");
			buffer.append( buildUserProvidedWhereClause(query.getWhereClauseStructure(), query, entityAliasesMaps) );
		}
		

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
	
	private String buildGroupByClause(Query query, Map entityAliasesMaps) {
		StringBuffer buffer = new StringBuffer();
		List groupByFields = query.getGroupByFields();
		
		if(groupByFields == null ||groupByFields.size() == 0) {
			return "";
		}
		
		buffer.append("GROUP BY");
		
		Map entityAliases = (Map)entityAliasesMaps.get(query.getId());
		
		Iterator it = groupByFields.iterator();
		while( it.hasNext() ) {
			SelectField groupByField = (SelectField)it.next();
			DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(groupByField.getUniqueName());
			DataMartEntity entity = datamartField.getParent().getRoot(); 
			String queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(entity.getUniqueName())) {
				entityAliases.put(entity.getUniqueName(), getNextAlias(entityAliasesMaps));
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
	
	private String buildOrderByClause(Query query, Map entityAliasesMaps) {
		StringBuffer buffer;
		Iterator it;
		SelectField selectField;
		
		it = getOrderByFields(query).iterator();		
		if(!it.hasNext()) {
			return "";
		}
		
		buffer = new StringBuffer();	
		buffer.append("ORDER BY");
		
		Map entityAliases = (Map)entityAliasesMaps.get(query.getId());
					
		while( it.hasNext() ) {
			selectField = (SelectField)it.next();
			
			Assert.assertTrue(selectField.isOrderByField(), "Field [" + selectField.getUniqueName() +"] is not an orderBy filed");
			
			DataMartField datamartField = getDataMartModel().getDataMartModelStructure().getField(selectField.getUniqueName());
			DataMartEntity entity = datamartField.getParent().getRoot(); 
			String queryName = datamartField.getQueryName();
			if(!entityAliases.containsKey(entity.getUniqueName())) {
				entityAliases.put(entity.getUniqueName(), getNextAlias(entityAliasesMaps));
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
	}
	
	
	public Set getSelectedEntities() {
		Set selectedEntities;
		Map entityAliasesMaps;
		Iterator entityUniqueNamesIterator;
		String entityUniqueName;
		DataMartEntity entity;
		
		
		Assert.assertNotNull(query, "Input parameter 'query' cannot be null");
		Assert.assertTrue(!query.isEmpty(), "Input query cannot be empty (i.e. with no selected fields)");
		
		selectedEntities = new HashSet();
		
		// one map of entity aliases for each queries (master query + subqueries)
		// each map is indexed by the query id
		entityAliasesMaps = new HashMap();
		
		// let's start with the query at hand
		entityAliasesMaps.put(query.getId(), new HashMap());
		
		buildSelectClause(query, entityAliasesMaps);
		buildWhereClause(query, entityAliasesMaps);
		buildGroupByClause(query, entityAliasesMaps);
		buildOrderByClause(query, entityAliasesMaps);
		buildFromClause(query, entityAliasesMaps);
		
		Map entityAliases = (Map)entityAliasesMaps.get(query.getId());
		entityUniqueNamesIterator = entityAliases.keySet().iterator();
		while(entityUniqueNamesIterator.hasNext()) {
			entityUniqueName = (String)entityUniqueNamesIterator.next();
			entity = getDataMartModel().getDataMartModelStructure().getRootEntity( entityUniqueName );
			selectedEntities.add(entity);
		}
		
		return selectedEntities;
	}
	
	/*
	 * internally used to generate the parametric statement string. Shared by the prepare method and the buildWhereClause method in order
	 * to recursively generate subquery statement string to be embedded in the parent query.
	 */
	private String compose(Query query, Map entityAliasesMaps) {
		String queryStr;
		String selectClause;
		String whereClause;
		String groupByClause;
		String orderByClause;
		String fromClause;
		
		Assert.assertNotNull(query, "Input parameter 'query' cannot be null");
		Assert.assertTrue(!query.isEmpty(), "Input query cannot be empty (i.e. with no selected fields)");
				
		// let's start with the query at hand
		entityAliasesMaps.put(query.getId(), new HashMap());
		
		selectClause = buildSelectClause(query, entityAliasesMaps);
		whereClause = buildWhereClause(query, entityAliasesMaps);
		groupByClause = buildGroupByClause(query, entityAliasesMaps);
		orderByClause = buildOrderByClause(query, entityAliasesMaps);
		fromClause = buildFromClause(query, entityAliasesMaps);
		
		queryStr = selectClause + " " + fromClause + " " + whereClause + " " +  groupByClause + " " + orderByClause;
		
		Set subqueryIds;
		try {
			subqueryIds = StringUtils.getParameters(queryStr, "Q");
		} catch (IOException e) {
			throw new SpagoBIRuntimeException("Impossible to set parameters in query", e);
		}
		
		Iterator it = subqueryIds.iterator();
		while(it.hasNext()) {
			String id = (String)it.next();
			Query subquery = query.getSubquery(id);
			
			String subqueryStr = compose(subquery, entityAliasesMaps);
			queryStr = queryStr.replaceAll("Q\\{" + subquery.getId() + "\\}", subqueryStr);
		} 
		
		return queryStr;
	}
	
	public void prepare() {
		String queryStr;
		
		// one map of entity aliases for each queries (master query + subqueries)
		// each map is indexed by the query id
		Map entityAliasesMaps = new HashMap();
		
		queryStr = compose(query, entityAliasesMaps);	
		
		
		
		if(parameters != null) {
			try {
				queryStr = StringUtils.replaceParameters(queryStr.trim(), "P", parameters);
			} catch (IOException e) {
				throw new SpagoBIRuntimeException("Impossible to set parameters in query", e);
			}
			
		}				
		this.queryString = queryStr;			
	}
		
	public String getQueryString() {		
		if(this.queryString == null) {
			this.prepare();
		}
		
		return this.queryString;
	}
	
	public String getSqlQueryString() {	
		String sqlQuery = null;
		Session session = null;
		HqlToSqlQueryRewriter queryRewriter;
		try {
			session = dataMartModel.getDataSource().getSessionFactory().openSession();
			queryRewriter = new HqlToSqlQueryRewriter(session);
			sqlQuery = queryRewriter.rewrite( getQueryString() );
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
		
		return sqlQuery;		
	}	
	
	public SourceBean execute() throws Exception {
		return execute(offset, fetchSize, maxResults);
	}
	
	public SourceBean execute(int offset) throws Exception {
		return execute(offset, fetchSize, maxResults);
	}
	
	public SourceBean execute(int offset, int fetchSize) throws Exception {
		return execute(offset, fetchSize, maxResults);
	}
	
	public SourceBean execute(int offset, int fetchSize, int maxResults) throws Exception {
		return execute(offset, fetchSize, maxResults, isBlocking);
	}
	
	public SourceBean execute(int offset, int fetchSize, int maxResults, boolean isMaxResultsLimitBlocking) throws Exception {
		Session session = null;
		org.hibernate.Query hibernateQuery;
		int resultNumber;
		boolean overflow;
		
		try{		
			session = dataMartModel.getDataSource().getSessionFactory().openSession();
			
			// execute query
			hibernateQuery = session.createQuery( getQueryString() );	
			ScrollableResults scrollableResults = hibernateQuery.scroll();
			scrollableResults.last();
			resultNumber = scrollableResults.getRowNumber();
			resultNumber = resultNumber < 0? 0: resultNumber;
			overflow = (resultNumber >= maxResults);
			
			List result = null;
			
			if (overflow && isMaxResultsLimitBlocking) {
				// does not execute query
				result = new ArrayList();
			} else {
				offset = offset < 0 ? 0 : offset;
				if(maxResults > 0) {
					fetchSize = (fetchSize > 0)? Math.min(fetchSize, maxResults): maxResults;
				}
				
				hibernateQuery.setFirstResult(offset);
				if(fetchSize > 0) hibernateQuery.setMaxResults(fetchSize);			
				result = hibernateQuery.list();
			}	
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
	
	
	public String toString() {
		return this.getQueryString();
	}
}
