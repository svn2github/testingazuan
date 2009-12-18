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
import it.eng.qbe.query.IGroupByField;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.query.ISelectClause;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.IWhereField;
import it.eng.qbe.query.OrderByField;
import it.eng.qbe.utility.StringUtils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.spago.base.SourceBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class HQLStatement.
 */
public class CopyOfHQLStatement extends BasicStatement {
	
	/**
	 * Instantiates a new hQL statement.
	 * 
	 * @param dataMartModel the data mart model
	 */
	protected CopyOfHQLStatement(IDataMartModel dataMartModel) {
		super(dataMartModel);
	}
	
	/**
	 * Instantiates a new hQL statement.
	 * 
	 * @param dataMartModel the data mart model
	 * @param query the query
	 */
	protected CopyOfHQLStatement(IDataMartModel dataMartModel, IQuery query) {
		super(dataMartModel, query);
	}
	
	/**
	 * Builds the select clause.
	 * 
	 * @param query the query
	 * 
	 * @return the string
	 */
	private String buildSelectClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		
		ISelectClause selectClause;
		List selectClauses;
		ISelectField selectField;
		Iterator it;
		
		boolean afterFirst;
		Map fieldToAlias = new HashMap();
		
		
		buffer.append("select ");		
		if (query.getDistinct()) buffer.append("distinct ");
				
		
		it = query.getSelectFieldsIterator();	
		afterFirst = false;
		while (it.hasNext()) {
			selectField =(ISelectField)it.next();
			if (afterFirst) buffer.append(", ");
			buffer.append(selectField.getFieldName());
				
			if (selectField.getFieldAlias() != null) {
				if(selectField.getFieldNameWithoutOperators().equalsIgnoreCase(selectField.getFieldName())) {
					String alias = selectField.getFieldAlias().replaceAll(" ","_");
					buffer.append(" as "+ alias + " ");
					fieldToAlias.put(selectField.getFieldNameWithoutOperators(), alias);
				}		 			
			}			
			afterFirst = true;
		}	
		
		return buffer.toString();
	}
	
	/**
	 * Builds the from clause.
	 * 
	 * @param query the query
	 * 
	 * @return the string
	 */
	public String buildFromClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		boolean afterFirst;
		
		afterFirst = false;
		EntityClass ec = null;
		buffer.append(" from ");
			
		for (Iterator it = query.getEntityClassesIterator(); it.hasNext();){
				ec =(EntityClass)it.next();
		 		if (afterFirst)
		 			buffer.append(", ");
		 		buffer.append(ec.getClassName() + " as " + ec.getClassAlias() + " ");
		 		afterFirst = true;
		}
		
		
		return buffer.toString();
	}
	
	
	
	/** The Constant EQUALS_TO. */
	public static final String EQUALS_TO = "EQUALS TO";
	
	/** The Constant NOT_EQUALS_TO. */
	public static final String NOT_EQUALS_TO = "NOT EQUALS TO";
	
	/** The Constant GREATER_THAN. */
	public static final String GREATER_THAN = "GREATER THAN";
	
	/** The Constant EQUALS_OR_GREATER_THAN. */
	public static final String EQUALS_OR_GREATER_THAN = "EQUALS OR GREATER THAN";
	
	/** The Constant LESS_THAN. */
	public static final String LESS_THAN = "LESS THAN";
	
	/** The Constant EQUALS_OR_LESS_THAN. */
	public static final String EQUALS_OR_LESS_THAN = "EQUALS OR LESS THAN";
	
	/** The Constant STARTS_WITH. */
	public static final String STARTS_WITH = "STARTS WITH";
	
	/** The Constant NOT_STARTS_WITH. */
	public static final String NOT_STARTS_WITH = "NOT STARTS WITH";
	
	/** The Constant ENDS_WITH. */
	public static final String ENDS_WITH = "ENDS WITH";
	
	/** The Constant NOT_ENDS_WITH. */
	public static final String NOT_ENDS_WITH = "NOT ENDS WITH";
	
	/** The Constant NOT_NULL. */
	public static final String NOT_NULL = "NOT NULL";
	
	/** The Constant IS_NULL. */
	public static final String IS_NULL = "IS NULL";
	
	/** The Constant CONTAINS. */
	public static final String CONTAINS = "CONTAINS";
	
	/** The Constant NOT_CONTAINS. */
	public static final String NOT_CONTAINS = "NOT CONTAINS";
	
	
	/**
	 * Builds the where clause.
	 * 
	 * @param query the query
	 * 
	 * @return the string
	 */
	public String buildWhereClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		Iterator fieldIterator = null;
		IWhereField whereField = null;
		String fieldName = null;
		String fieldOperator = null;
		String fieldValue = null;
		boolean isBinaryOperator = true;
		boolean afterFirst;
		
		
		afterFirst = false;
		
		fieldIterator = query.getWhereFieldsIterator();
		
		if(fieldIterator.hasNext()) {
			buffer.append("where \n");
		}
		
		while ( fieldIterator.hasNext() ) {
		 		whereField =(IWhereField)fieldIterator.next();
		 		
		 		fieldName = whereField.getFieldName();
		 		fieldValue = whereField.getFieldValue();
		 		
		 		for(int i = 0; i < whereField.getLeftBracketsNum(); i++) buffer.append("(");
		 		
		 		buffer.append(fieldName);  
		 		buffer.append(" ");
		 		
		 		
		 		if (EQUALS_TO.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator = "=";
		 		} else if (NOT_EQUALS_TO.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator = "!=";
		 		} else if (GREATER_THAN.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator = ">";
		 		} else if (EQUALS_OR_GREATER_THAN.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator = ">=";
		 		} else if (LESS_THAN.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator = "<";
		 		} else if (EQUALS_OR_LESS_THAN.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator = "<=";
		 		} else if (STARTS_WITH.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator =  "LIKE";
		 			fieldValue = fieldValue + "%";
		 		} else if (NOT_STARTS_WITH.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator =  "NOT LIKE";
		 			fieldValue = fieldValue + "%";
		 		} else if (ENDS_WITH.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator =  "LIKE";
		 			fieldValue = "%" + fieldValue;
		 		}else if (NOT_ENDS_WITH.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator =  "NOT LIKE";
		 			fieldValue = "%" + fieldValue;
		 		} else if (CONTAINS.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator =  "LIKE";
		 			fieldValue = "%" + fieldValue + "%";
		 		} else if (NOT_CONTAINS.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator =  "NOT LIKE";
		 			fieldValue = "%" + fieldValue + "%";
		 		} else if (IS_NULL.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			fieldOperator =  "IS NULL";
		 			isBinaryOperator = false;
		 		} else if (NOT_NULL.equalsIgnoreCase( whereField.getFieldOperator() )) {
		 			isBinaryOperator = false;
		 			fieldOperator =  "IS NOT NULL";
		 		} else {
		 			
		 		}
		 		
		 	
		 		
		 		buffer.append( fieldOperator );
		 		buffer.append(" ");
		 		
		 		
		 		
		 		
		 		
		 		String fValue = whereField.getFieldValue();		 		
		 		if (fValue.startsWith("$subquery_") && fValue.endsWith("$")){
		 			int idx1 = fValue.indexOf("$subquery_");
		 			int idx2 = fValue.lastIndexOf("$");
		 			
		 			idx1 += "$subquery_".length();
		 			String subQueryFldId = fValue.substring(idx1, idx2);
		 			
		 			//////// TEST //////////////////////////////////////////////////////
		 			IQuery subQueryObject = query.getSubquery(subQueryFldId);		 			
		 			IStatement statement = dataMartModel.createStatement(subQueryObject);
		 			buffer.append(" ( ");
		 			buffer.append(statement.getQueryString());
		 			buffer.append(" ) ");
		 			/////////REPLACE WITH ///////////////////////////////////////////////
		 			/*
		 			query.selectSubquery(subQueryFldId);
		 			IStatement statement = dataMartModel.createStatement(query);
		 			buffer.append(" ( ");
		 			buffer.append(statement.getQueryString());
		 			buffer.append(" ) ");
		 			 */		 			
		 		}else{
		 			if(isBinaryOperator) {			
			 			if (whereField.getFieldEntityClassForRightCondition() == null
			 					&& (whereField.getType().endsWith("StringType") 
			 							|| whereField.getType().equalsIgnoreCase("string")) ) {
			 				buffer.append("'"+ fieldValue + "'");
			 			}else {
			 				buffer.append( fieldValue );
			 			}
		 			}
		 		}
		 		
		 		for(int i = 0; i < whereField.getRightBracketsNum(); i++) buffer.append(")");
		 		
		 		if (fieldIterator.hasNext())
		 			buffer.append(" "+whereField.getNextBooleanOperator()+" ");
		 		afterFirst = true;
		 }
		
		
		
		
		
		
		
		
		
		
		
		
		
		/////////////////////////////////////////////////////////////////////////
		/*
		DataMartModelStructure dataMartModelStructure = dataMartModel.getDataMartModelStructure();
		DataMartModelAccessModality dataMartModelAccessModality = dataMartModel.getDataMartModelAccessModality();
		
		it = query.getEntityClassesItertor();
		while(it.hasNext()){
			EntityClass entityClass = (EntityClass)it.next();
			String entityName = entityClass.getClassName();
			DataMartEntity entity = dataMartModelStructure.getEntity(entityName);
			
			// check for condition filter on this entity
			List filters = dataMartModelAccessModality.getEntityFilterConditions(entity.getName());
			for(int i = 0; i < filters.size(); i++) {
				Filter filter = (Filter)filters.get(i);
				Set fields = filter.getFields();
				Properties props = new Properties();
				Iterator fieldIterator = fields.iterator();
				while(fieldIterator.hasNext()) {
					fieldName = (String)fieldIterator.next();
					props.put(fieldName, entityClass.getClassAlias() + "." + fieldName);
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
				filters = dataMartModelAccessModality.getEntityFilterConditions(subEntity.getName());
				for(int j = 0; j < filters.size(); j++) {
					Filter filter = (Filter)filters.get(j);
					Set fields = filter.getFields();
					Properties props = new Properties();
					Iterator fieldIterator = fields.iterator();
					while(fieldIterator.hasNext()) {
						fieldName = (String)fieldIterator.next();
						DataMartField filed = null;
						Iterator subEntityFields = subEntity.getAllFields().iterator();
						while(subEntityFields.hasNext()) {
							filed = (DataMartField)subEntityFields.next();
							if(filed.getName().endsWith("." + fieldName)) break;
						}
						props.put(fieldName, entityClass.getClassAlias() + "." + filed.getName());
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
		*/
		
		
		return buffer.toString();
	}
	
	/**
	 * Builds the group by clause.
	 * 
	 * @param query the query
	 * 
	 * @return the string
	 */
	public String buildGroupByClause(IQuery query) {
		StringBuffer buffer = new StringBuffer(); 
		boolean afterFirst = false;
		
		if(query.getGroupByFieldsIterator().hasNext()) {
			buffer.append(" group by ");
		 	Iterator it = query.getGroupByFieldsIterator();
		 
		 	
		 	IGroupByField aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(IGroupByField)it.next();
		 		if (afterFirst)
		 			buffer.append(", ");
		 		
//		 		if(fieldToAlias.containsKey(aOrderGroupByField.getFieldName()))
//		 			finalQuery.append(fieldToAlias.get(aOrderGroupByField.getFieldName()) + " ");
//		 		else
		 			buffer.append(aOrderGroupByField.getFieldName() + " ");
		 		
		 		afterFirst = true;
		 	}
		
		}
		return buffer.toString();
	}
	
	/**
	 * Builds the order by clause.
	 * 
	 * @param query the query
	 * 
	 * @return the string
	 */
	public String buildOrderByClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		
		boolean afterFirst = false;
		
		if (query.getOrderByFieldsIterator().hasNext()){
			buffer.append(" order by ");
		Iterator it = query.getOrderByFieldsIterator();
		 
		 	
		 	OrderByField aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(OrderByField)it.next();
		 		if (afterFirst)
		 			buffer.append(", ");
	 			buffer.append(aOrderGroupByField.getFieldName() + " ");
	 			buffer.append(aOrderGroupByField.isAscendingOrder()? "asc": "desc");	 		
	 			buffer.append(" ");
	 			afterFirst = true;
		 	}
		}
		
		return buffer.toString();
	}
	
	/**
	 * Gets the query string.
	 * 
	 * @param query the query
	 * 
	 * @return the query string
	 */
	public String getQueryString(IQuery query) {		
		try {
			return getQueryString(query, parameters);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IStatement#getQueryString(it.eng.qbe.query.IQuery, java.util.Properties)
	 */
	public String getQueryString(IQuery query, Map parameters) throws IOException {		
		StringBuffer buffer; 
		
		if(query.isEmpty()) return null;
		
		buffer = new StringBuffer();		
		buffer.append(buildSelectClause(query));
		buffer.append(buildFromClause(query));
		buffer.append(buildWhereClause(query));
		buffer.append(buildGroupByClause(query));
		buffer.append(buildOrderByClause(query));		
		
		return StringUtils.replaceParameters(buffer.toString().trim(), "P", parameters);
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
	public SourceBean execute(IQuery query, Map parameters, int offset, int fetchSize, int maxResults) throws Exception {
		Session session = null;
		try{		
			session = dataMartModel.getDataSource().getSessionFactory().openSession();
			
			String queryString = getQueryString(query, parameters);	
			
			HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter(dataMartModel.getDataSource().getSessionFactory().openSession());
			String sqlQuery = queryRewriter.rewrite( queryString );
						
			Query hibernateQuery = session.createQuery(queryString);			
			
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
	public SourceBean executeWithPagination(IQuery query, Map parameters, int pageNumber, int pageSize, int maxResults) throws Exception {
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
