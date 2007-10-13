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

import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.utility.StringUtils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.OrderByFieldSourceBeanImpl;
import it.eng.spago.base.SourceBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Andrea Gioia
 *
 */
public class HQLStatement extends BasicStatement {
	
	protected HQLStatement(IDataMartModel dataMartModel) {
		super(dataMartModel);
	}
	
	protected HQLStatement(IDataMartModel dataMartModel, IQuery query) {
		super(dataMartModel, query);
	}
	
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
				
		selectClause = query.getSelectClause();
		selectClauses = selectClause.getSelectFields();		 		
		
		it = selectClauses.iterator();	
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
	
	public String buildFromClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		boolean afterFirst;
		
		List entityClasses = query.getEntityClasses();		
		afterFirst = false;
		EntityClass ec = null;
		if (entityClasses != null){
			buffer.append(" from ");
			
			for (Iterator it = entityClasses.iterator(); it.hasNext();){
				ec =(EntityClass)it.next();
		 		if (afterFirst)
		 			buffer.append(", ");
		 		buffer.append(ec.getClassName() + " as " + ec.getClassAlias() + " ");
		 		afterFirst = true;
			}
		}
		
		return buffer.toString();
	}
	
	public String buildWhereClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		boolean afterFirst;
		
		IWhereClause aWhereClause = query.getWhereClause();  
		afterFirst = false;
		if (aWhereClause != null){
		 	buffer.append("where \n");
		 	java.util.List l= aWhereClause.getWhereFields();
		 	Iterator it = l.iterator();
		 	
		 	
		 	IWhereField aWhereField = null;
		 	String newFieldValue = null;
		 	String fieldName = null;
		 	while (it.hasNext()){
		 		aWhereField =(IWhereField)it.next();
		 		fieldName = aWhereField.getFieldName();
		 		
		 		for(int i = 0; i < aWhereField.getLeftBracketsNum(); i++) buffer.append("(");
		 		
		 		buffer.append(fieldName);  
		 		buffer.append(" ");
		 		
		 		if (aWhereField.getFieldOperator().equalsIgnoreCase("start with")){
		 			aWhereField.setFieldOperator("like");
		 			newFieldValue = "";
		 			newFieldValue = aWhereField.getFieldValue()+"%";
		 			aWhereField.setFieldValue(newFieldValue);
		 		}else if (aWhereField.getFieldOperator().equalsIgnoreCase("end with")){
		 			aWhereField.setFieldOperator("like");
		 			newFieldValue = "";
		 			newFieldValue = "%"+ aWhereField.getFieldValue();
		 			aWhereField.setFieldValue(newFieldValue);
		 		}else if (aWhereField.getFieldOperator().equalsIgnoreCase("contains")){
		 			aWhereField.setFieldOperator("like");
		 			newFieldValue = "";
		 			newFieldValue = "%"+ aWhereField.getFieldValue()+"%";
		 			aWhereField.setFieldValue(newFieldValue);
		 		}
		 		buffer.append(aWhereField.getFieldOperator());
		 		buffer.append(" ");
		 		String fValue = aWhereField.getFieldValue();
		 		if (fValue.startsWith("$subquery_") && fValue.endsWith("$")){
		 			int idx1 = fValue.indexOf("$subquery_");
		 			int idx2 = fValue.lastIndexOf("$");
		 			
		 			idx1 += "$subquery_".length();
		 			String subQueryFldId = fValue.substring(idx1, idx2);
		 			ISingleDataMartWizardObject subQueryObject = query.getSubQueryOnField(subQueryFldId);
		 			
		 			subQueryObject.composeQuery(dataMartModel);
		 			buffer.append(" ( ");
		 			buffer.append(subQueryObject.getFinalQuery());
		 			buffer.append(" ) ");
		 		}else{
		 			if ((aWhereField.getFieldEntityClassForRightCondition() == null)&&(aWhereField.getHibernateType().endsWith("StringType")))
		 				buffer.append("'"+ fValue + "'");
		 			else
		 				buffer.append( fValue );
		 		}
		 		
		 		for(int i = 0; i < aWhereField.getRightBracketsNum(); i++) buffer.append(")");
		 		
		 		if (it.hasNext())
		 			buffer.append(" "+aWhereField.getNextBooleanOperator()+" ");
		 		afterFirst = true;
		 	}
		 }
		
		/////////////////////////////////////////////////////////////////////////
		DataMartModelStructure dataMartModelStructure = dataMartModel.getDataMartModelStructure();
		DataMartModelAccessModality dataMartModelAccessModality = dataMartModel.getDataMartModelAccessModality();
		List entityClasses = query.getEntityClasses();		
		Iterator it = entityClasses.iterator();
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
					String fieldName = (String)fieldIterator.next();
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
						String fieldName = (String)fieldIterator.next();
						DataMartField filed = null;
						Iterator subEntityFields = subEntity.getFields().iterator();
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
		
		
		
		return buffer.toString();
	}
	
	public String buildGroupByClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		IGroupByClause aGroupByClause = query.getGroupByClause();  
		boolean afterFirst = false;
		
		if (aGroupByClause != null){
			buffer.append(" group by ");
			java.util.List l= aGroupByClause.getGroupByFields();
		 	Iterator it = l.iterator();
		 
		 	
		 	IOrderGroupByField aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(IOrderGroupByField)it.next();
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
	
	public String buildOrderByClause(IQuery query) {
		StringBuffer buffer = new StringBuffer();
		IOrderByClause aOrderByClause= query.getOrderByClause();  
		boolean afterFirst = false;
		
		if (aOrderByClause != null){
			buffer.append(" order by ");
			java.util.List l= aOrderByClause.getOrderByFields();
		 	Iterator it = l.iterator();
		 
		 	
		 	OrderByFieldSourceBeanImpl aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(OrderByFieldSourceBeanImpl)it.next();
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
	
	public String getQueryString(IQuery query) {		
		try {
			return getQueryString(query, parameters);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getQueryString(IQuery query, Properties parameters) throws IOException {		
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
	
	
	public SourceBean execute(int offset, int fetchSize) throws Exception {
		return execute(query, parameters, offset, fetchSize, maxResults);
	}
	
	public SourceBean execute(int offset) throws Exception {
		return execute(query, parameters, offset, fetchSize, maxResults);
	}

	public SourceBean execute() throws Exception {
		return execute(query, parameters, offset, fetchSize, maxResults);
	}
	
	public SourceBean execute(IQuery query, Properties parameters, int offset, int fetchSize, int maxResults) throws Exception {
		Session session = null;
		try{		
			session = dataMartModel.getDataSource().getSessionFactory().openSession();
			
			String queryString = getQueryString(query, parameters);				
						
			Query hibernateQuery = session.createQuery(queryString);			
			
			// check for overflow
			if(maxResults >= 0) hibernateQuery.setMaxResults(maxResults);
			int resultNumber = hibernateQuery.list().size();
			boolean overflow = (resultNumber == maxResults);
						
			// get query results
			hibernateQuery.setFirstResult(offset < 0 ? 0 : offset);
			if(fetchSize >= 0) hibernateQuery.setMaxResults(fetchSize);			
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
	
	public SourceBean executeWithPagination(int pageNumber, int pageSize) throws Exception {
		return executeWithPagination(query, parameters, pageNumber, pageSize, maxResults);
	}
	
	public SourceBean executeWithPagination(IQuery query, Properties parameters, int pageNumber, int pageSize, int maxResults) throws Exception {
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
