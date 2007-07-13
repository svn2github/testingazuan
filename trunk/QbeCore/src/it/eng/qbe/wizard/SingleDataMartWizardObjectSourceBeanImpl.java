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
package it.eng.qbe.wizard;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.IQuery;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SingleDataMartWizardObjectSourceBeanImpl implements ISingleDataMartWizardObject, IQuery {

	
	private int subQueryCounter = 0;
	private ISelectClause selectClause = null;
	private IWhereClause  whereClause = null;
	private IOrderByClause orderByClause = null;
	private IGroupByClause groupByClause = null;
	
	private String finalQuery = null;
	private List entityClasses = null;
	
	private String expertQueryDisplayed = null;
	private String expertQuerySaved = null;
	
	private String owner = null;
	private boolean visibility;
	private boolean distinct;
	
	private boolean useExpertedVersion = false;
	
	private String queryId = null;
	private String description = null;
	
	public static final int DEFAULT_MAX_ROWS_NUM = 5000;
	
	private Map subqueryMap = null;
	private Map mapFieldIdSubQUeryId = null;
	private ISingleDataMartWizardObject selectedSubquery = null;
	private String subqueryErrMsg = "";
	
	public SingleDataMartWizardObjectSourceBeanImpl() {
		super();
		this.entityClasses = new ArrayList();
		this.subqueryMap = new HashMap();
		this.mapFieldIdSubQUeryId = new HashMap();
	
	}
	
	public ISingleDataMartWizardObject getCopy() {
		SingleDataMartWizardObjectSourceBeanImpl wizardObject = new SingleDataMartWizardObjectSourceBeanImpl();
		
		// copy all the query relaed informations
		wizardObject.setSubQueryCounter(subQueryCounter);
		if(selectClause != null) wizardObject.setSelectClause(selectClause.getCopy());
		if(whereClause != null) wizardObject.setWhereClause(whereClause.getCopy());
		if(orderByClause != null) wizardObject.setOrderByClause(orderByClause.getCopy());
		if(groupByClause != null) wizardObject.setGroupByClause(groupByClause.getCopy());
		wizardObject.setFinalQuery(finalQuery);
		List list = new ArrayList();
		for(int i = 0; i < entityClasses.size(); i++) {
			EntityClass entityClass = (EntityClass)entityClasses.get(i);
			list.add(entityClass.getCopy());
		}
		wizardObject.setEntityClasses(list);
		wizardObject.setOwner(owner);
		wizardObject.setVisibility(visibility);
		wizardObject.setDistinct(distinct);
		wizardObject.setUseExpertedVersion(useExpertedVersion);
		wizardObject.setQueryId(queryId);
		wizardObject.setDescription(description);
				
		return wizardObject;
	}
	
	
	
	
	public boolean isEmpty(){
		return (selectClause == null 
				|| selectClause.getSelectFields() == null 
				|| selectClause.getSelectFields().size() == 0);
	}
	
	public ISelectClause getSelectClause() {
		return this.selectClause;
	}
	
	public IWhereClause getWhereClause() {
		return this.whereClause;
	}
	
	public IOrderByClause getOrderByClause() {
		return this.orderByClause;
	}
	
	public IGroupByClause getGroupByClause() {
		
		return this.groupByClause;
		
	}
	
	public void setOrderByClause(IOrderByClause orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	public void setGroupByClause(IGroupByClause groupByClause) {
		this.groupByClause = groupByClause;		
	}

	
	
	public void setWhereClause(IWhereClause aWhereClause) {
		this.whereClause = aWhereClause;
		
	}

	
	public void setSelectClause(ISelectClause aSelectClause) {
			this.selectClause = aSelectClause;
			
		}
	
	public void delSelectClause() {
		this.selectClause = null;
	}
	
	public void delWhereClause() {
		this.whereClause = null;
	}
	
	public void delOrderByClause() {
		this.orderByClause = null;
	}
	
	public void delGroupByClause() {
		this.groupByClause = null;
	}
	
	public String getFinalQuery() {
		return this.finalQuery;
	}
	
	public String getFinalSqlQuery(DataMartModel dm) {
		
		String finalSqlQuery = null;
		Session aSession = null;
		try {
			String finalHQLQuery = getFinalQuery();
			if (finalHQLQuery != null){
				aSession = Utils.getSessionFactory(dm, ApplicationContainer.getInstance()).openSession();
				HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter(aSession);
				finalSqlQuery = queryRewriter.rewrite( getFinalQuery() );
			}
			return finalSqlQuery;
		} finally {
			if ((aSession != null) && (aSession.isOpen()))
				aSession.close();
		}
		
	}
	
	public void setFinalQuery(String query) {
		this.finalQuery = query;
		
	}
	public void addEntityClass(EntityClass ec) {
		this.entityClasses.add(ec);
	}
	public List getEntityClasses() {
		return this.entityClasses;
	}
	
	public boolean containEntityClass(EntityClass parameEc) {
		
		EntityClass ec = null;
		SourceBean sb = null;
		
		for (Iterator it = this.entityClasses.iterator(); it.hasNext();){
			
			ec = (EntityClass)it.next();
			if (ec.getClassName().equalsIgnoreCase(parameEc.getClassName())
			   && ec.getClassAlias().equalsIgnoreCase(ec.getClassAlias())){
				return true;
			}
		}
		return false;
							
	}
	
	

	public void purgeNotReferredEntityClasses() {
		this.entityClasses.clear();
		
		ISelectClause selectClause = getSelectClause();
				
		EntityClass ec = null;
		ISelectField selField = null;
		if (selectClause != null){
			for (int i=0; i < selectClause.getSelectFields().size(); i++){
				selField = (ISelectField)selectClause.getSelectFields().get(i);
				ec = selField.getFieldEntityClass();
				if (!this.containEntityClass(ec)){
					this.addEntityClass(ec);
				}
			}
		}
		
		IWhereClause whereClause = getWhereClause();
		
		ec = null;
		
		IWhereField whereField = null;
		
		if (whereClause != null){
			for (int i=0; i < whereClause.getWhereFields().size(); i++){
				whereField = (IWhereField)whereClause.getWhereFields().get(i);
				ec = whereField.getFieldEntityClassForLeftCondition();
				if (!this.containEntityClass(ec)){
					this.addEntityClass(ec);
				}
				ec = whereField.getFieldEntityClassForRightCondition();
				if ((ec != null)&&(!this.containEntityClass(ec))){
					this.addEntityClass(ec);
				}
				
			}
		}		
		
	}
	
	public void purgeNotReferredEntityClasses(String prefix) {
		this.entityClasses.clear();
		
		ISelectClause selectClause = getSelectClause();
				
		EntityClass ec = null;
		ISelectField selField = null;
		if (selectClause != null){
			for (int i=0; i < selectClause.getSelectFields().size(); i++){
				selField = (ISelectField)selectClause.getSelectFields().get(i);
				ec = selField.getFieldEntityClass();
				if (!this.containEntityClass(ec) && ec.getClassAlias().startsWith(prefix)){
					this.addEntityClass(ec);
				}
			}
		}
		
		IWhereClause whereClause = getWhereClause();
		
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
	public String getQueryId() {
		return queryId;
	}




	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	
	public void composeQuery(IDataMartModel dataMartModel){
		finalQuery = null;
		IStatement statement = dataMartModel.createStatement(this);
		statement.setParameters(dataMartModel.getDataMartProperties());
		finalQuery = statement.getQueryString();
	
	}//public void composeQuery(){




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public void setEntityClasses(List entityClasses) {
		this.entityClasses = entityClasses;
	}
	
	

	public boolean isUseExpertedVersion() {
		return useExpertedVersion;
	}

	public void setUseExpertedVersion(boolean useExpertedVersion) {
		this.useExpertedVersion = useExpertedVersion;
	}




	public String getExpertQueryDisplayed() {
		return expertQueryDisplayed;
	}




	public void setExpertQueryDisplayed(String expertQueryDisplayed) {
		this.expertQueryDisplayed = expertQueryDisplayed;
	}




	public boolean getVisibility() {
		return this.visibility;
	}




	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}




	public String getOwner() {
		return this.owner;
	}




	public void setOwner(String owner) {
		this.owner = owner;
	}




	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	public boolean getDistinct() {
		return distinct;
	}




	public String getExpertQuerySaved() {
		return expertQuerySaved;
	}




	public void setExpertQuerySaved(String expertQuerySaved) {
		this.expertQuerySaved = expertQuerySaved;
	}


	/**
	 * This method extracts the name of select fields (or the alias name if present), from the expert query 
	 * 
	 * @param expertSelectFieldsList
	 * 
	 *  @return the list of the name of select fields (or the alias name if present), from the expert query, 
	 *  		null if the query is null or doesn't contain select fields 
	 */
	public List extractExpertSelectFieldsList() {
		
	String expertQueryTemp = this.getExpertQueryDisplayed();
	String seekForSelect = null;
		
		//non necessario: darebbe errrore nell'esecuzione e quindi è già gestito prima
		if (expertQueryTemp == null) return null;

		seekForSelect = expertQueryTemp.toLowerCase();
		expertQueryTemp = expertQueryTemp.trim();
		
		int checkSelect = seekForSelect.indexOf("select ");
		if (checkSelect==-1) {
			Logger.debug(SingleDataMartWizardObjectSourceBeanImpl.class,"Select fields not found");
			return null;
		}
		
						
		expertQueryTemp = expertQueryTemp.substring(7,expertQueryTemp.indexOf(" from "));
		
		boolean nextSelect = true;
		int commaIndex = -1;
		int asIndex = -1;
		int dotIndex = -1;
		String expertSelectField = null;
		List expertHeaders = new ArrayList(); 
		
		while (nextSelect) {
			commaIndex = expertQueryTemp.indexOf(",");
			if (commaIndex == -1) {
				expertSelectField = expertQueryTemp;
				nextSelect = false;
			} else {
				
				expertSelectField = expertQueryTemp.substring(0,commaIndex);
				expertQueryTemp = expertQueryTemp.substring(commaIndex+1);
			}

			asIndex = expertSelectField.indexOf(" as ");
			if (asIndex != -1){
				
				expertSelectField = expertSelectField.substring(asIndex+4);
				expertSelectField = expertSelectField.trim();
				expertHeaders.add(expertSelectField);
				continue;
				
			} else {
			
				dotIndex = expertSelectField.lastIndexOf(".");
				if (dotIndex != -1){
					
					expertSelectField = expertSelectField.substring(dotIndex+1);
					expertSelectField = expertSelectField.trim();
					expertHeaders.add(expertSelectField);
					continue;
										
				} else {										
					expertSelectField = expertSelectField.trim();
					expertHeaders.add(expertSelectField);
					continue;
				}
				
			}
				
		}
		
		return expertHeaders;
		
	}
	
	public static String QUERY_RESPONSE_SOURCE_BEAN = "QUERY_RESPONSE_SOURCE_BEAN"; 
	
	public SourceBean executeQbeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception {
		
		IStatement statement = dataMartModel.createStatement(this);
		
		String maxRowsForSQLExecution = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");
		int maxResults = DEFAULT_MAX_ROWS_NUM;
		if( (maxRowsForSQLExecution!=null) && !(maxRowsForSQLExecution.trim().length()==0) ){
			maxResults = Integer.valueOf(maxRowsForSQLExecution).intValue();
		}
		statement.setMaxResults(maxResults);
		statement.setParameters(dataMartModel.getDataMartProperties());
		return statement.executeWithPagination(pageNumber, pageSize);
		
		//return executeHqlQuery(dataMartModel, getFinalQuery(), pageNumber, pageSize);
	}
	
	public SourceBean executeExpertQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception {
		return executeSqlQuery(dataMartModel, getExpertQueryDisplayed(), pageNumber, pageSize);
	}
	
	public SourceBean executeSqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception {
		
		
		if (!(query.startsWith("select") || query.startsWith("SELECT"))){  
			throw new Exception("It's not possible change database status with qbe exepert query");
		}
		Session aSession = null;
		
		
		try{
			aSession = Utils.getSessionFactory(dataMartModel, ApplicationContainer.getInstance()).openSession();
			
			String maxRowsForSQLExecution = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");

			int maxSQLResults = DEFAULT_MAX_ROWS_NUM;
			if( (maxRowsForSQLExecution!=null) && !(maxRowsForSQLExecution.trim().length()==0) ){
				maxSQLResults = Integer.valueOf(maxRowsForSQLExecution).intValue();
			}
		
			List result = null;
			boolean hasNextPage = true;
			boolean hasPrevPage = (pageNumber > 0);
		
			int firstRow = pageNumber * pageSize;
			firstRow = firstRow < 0 ? 0 : firstRow;
		
		
			Connection conn = aSession.connection();
			Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stm.setMaxRows(maxSQLResults);
		
			stm.execute(query);
			
			ResultSet rs = stm.getResultSet();
			int pagesNumber = 0;
			int rowsNumber = 0;
			if (rs != null){
				rs.last();
				rowsNumber = rs.getRow();
				pagesNumber = (rowsNumber / pageSize) + ( ((rowsNumber % pageSize) != 0 )? 1: 0 );
				rs.beforeFirst();
			
						
				ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				result = new ArrayList();
				Object[] row = null;
				if(firstRow > 0)  
					rs.absolute(firstRow - 1);
				else 
					rs.beforeFirst();
				int remainingRows = pageSize;
				while(rs.next() && (remainingRows--)>0) {
					row = new Object[numberOfColumns];
					for(int i = 0; i < numberOfColumns; i++) {
						row[i] = rs.getObject(i+1);
					}
					result.add(row);
				}
				hasNextPage = rs.next();
		
			}
			
				
				SourceBean queryResponseSourceBean = new SourceBean(QUERY_RESPONSE_SOURCE_BEAN);
				queryResponseSourceBean.setAttribute("query", query);
				queryResponseSourceBean.setAttribute("list", result);
				queryResponseSourceBean.setAttribute("currentPage", new Integer(pageNumber));
				queryResponseSourceBean.setAttribute("pagesNumber", new Integer(pagesNumber));
				queryResponseSourceBean.setAttribute("hasNextPage", new Boolean(hasNextPage));
				queryResponseSourceBean.setAttribute("hasPreviousPage", new Boolean(hasPrevPage));
				queryResponseSourceBean.setAttribute("overflow", new Boolean(rowsNumber >= maxSQLResults));
		
				return queryResponseSourceBean;
		}finally{
			
			if (aSession != null && aSession.isOpen())
				aSession.close();
		}
	}
	
	private SourceBean executeHqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception {
		Session aSession = null;
		try{		
			aSession = Utils.getSessionFactory(dataMartModel, ApplicationContainer.getInstance()).openSession();
			//dataMartModel.getDataSource().getSessionFactory();
		
			String maxRowsForSQLExecution = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");
			
			int maxSQLResults = DEFAULT_MAX_ROWS_NUM;
			if( (maxRowsForSQLExecution!=null) && !(maxRowsForSQLExecution.trim().length()==0) ){
				maxSQLResults = Integer.valueOf(maxRowsForSQLExecution).intValue();
			}
				
		//Query aQuery = aSession.createQuery(getFinalQuery());
		Query aQuery = aSession.createQuery(query);
		
		String queryStr = aQuery.getQueryString();		
		/*
		queryStr = queryStr.substring(queryStr.indexOf("from"));
		queryStr = "select count(*) " + queryStr;
		Query countQuery = aSession.createQuery(queryStr);
		List countList = countQuery.list();
		Integer count = (Integer)countList.get(0);
		*/
		Query countQuery = aSession.createQuery(queryStr);
		aQuery.setMaxResults(maxSQLResults);
		List countList = countQuery.list();
		
		int rowsNumber = countList.size();
		//int pagesNumber = (rowsNumber / pageSize) + ( ((rowsNumber % pageSize) != 0 )? 1: 0 );
		
		
		
		
		int firstRow = pageNumber * pageSize;
		
		aQuery.setFirstResult(firstRow < 0 ? 0 : firstRow);
		aQuery.setMaxResults(pageSize);
		
		List result = aQuery.list();
			
		boolean hasNextPage = true;
		boolean hasPrevPage = true;
			
		aQuery.setFirstResult(firstRow + pageSize < 0 ? 0 : firstRow + pageSize);
		aQuery.setMaxResults(1);
			
		List secondPage = aQuery.list();
			

		if (secondPage == null || secondPage.size() == 0) hasNextPage = false;
			
		if (pageNumber == 0){
			firstRow = 0;
			hasPrevPage = false;
		}
								
		aSession.close();
			
		SourceBean queryResponseSourceBean = new SourceBean(QUERY_RESPONSE_SOURCE_BEAN);
		queryResponseSourceBean.setAttribute("query", query);
		queryResponseSourceBean.setAttribute("list", result);
		queryResponseSourceBean.setAttribute("currentPage", new Integer(pageNumber));
		//queryResponseSourceBean.setAttribute("pagesNumber", new Integer(pagesNumber));
		queryResponseSourceBean.setAttribute("hasNextPage", new Boolean(hasNextPage));
		queryResponseSourceBean.setAttribute("hasPreviousPage", new Boolean(hasPrevPage));
		queryResponseSourceBean.setAttribute("overflow", new Boolean(rowsNumber >= maxSQLResults));
					
		return queryResponseSourceBean;
		}finally{
			if (aSession != null && aSession.isOpen())
			aSession.close();
		}
	}
	
	public SourceBean executeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception {
		if(isUseExpertedVersion()) return executeExpertQuery(dataMartModel, pageNumber, pageSize);
		return executeQbeQuery(dataMartModel, pageNumber, pageSize);			
	}


	
	
	public boolean isSelectedSubqueryValid() {
		ISingleDataMartWizardObject subquery = (ISingleDataMartWizardObject)getSelectedSubquery();
		return isSubqueryValid(subquery);
	}
	
	public boolean isSubqueryValid(String fieldId) {
		ISingleDataMartWizardObject subquery = getSubQueryOnField(fieldId);
		return isSubqueryValid(subquery);
	}
	
	public boolean isSubqueryValid(ISingleDataMartWizardObject subquery) {
		
		boolean missingCondition = false;
		boolean invalidReference = false;
		subquery.setSubqueryErrMsg(null);
		subquery.setSubqueryErrMsg(null);
		
		if(subquery !=  null) {
			IWhereClause whereClause = subquery.getWhereClause();
			if(whereClause != null) {
				List fields = whereClause.getWhereFields();
				for(int i = 0; i < fields.size(); i++) {
					IWhereField whereField = (IWhereField)fields.get(i);
					// check missing right condition
					String value = whereField.getFieldValue();
					if(value == null || value.trim().equalsIgnoreCase("")) {
						
						missingCondition = true;
					}  
					
					this.getEntityClasses();
					EntityClass ec = whereField.getFieldEntityClassForRightCondition();
					if(ec != null && !this.containEntityClass(ec)) {
						subquery.setSubqueryErrMsg("Subquery contains at least one where condition not properly defined");
						invalidReference = true;
					}
					
					if(invalidReference && missingCondition) break;					
				}
			}
		}
		
		// resolve all invalid references first; missing right values next
		if(invalidReference) {
			subquery.setSubqueryErrMsg("Subquery contains at least one where condition not properly defined (cause: invalid reference to a parent entity)");
		}
		else if(missingCondition) {
			subquery.setSubqueryErrMsg("Subquery contains at least one where condition not properly defined (cause: missing right end value)");
		}
		
		
		return (!invalidReference && !missingCondition);		
	}
	
	public void selectSubquery(String fieldId) {
		ISingleDataMartWizardObject subquery = getSubQueryOnField(fieldId);
		if(subquery !=  null) {
			ISingleDataMartWizardObject selectedSubquery = subquery.getCopy();
			setSelectedSubquery(selectedSubquery);
		} else {
			ISingleDataMartWizardObject selectedSubquery = new SingleDataMartWizardObjectSourceBeanImpl();
			setSelectedSubquery(selectedSubquery);
			mapFieldIdSubQUeryId.put(fieldId, getNewSubQueryId());
		}
	}
	
	public void addSubQueryOnField(String fieldId, ISingleDataMartWizardObject subquery) {		
		this.subqueryMap.put(fieldId, subquery);
	}
	
	/**
	 * @deprecated
	 */
	public void addSubQueryOnField(String fieldId) {
		
		
		this.subqueryMap.put(fieldId, new SingleDataMartWizardObjectSourceBeanImpl());
		this.mapFieldIdSubQUeryId.put(fieldId, getNewSubQueryId());
	}

	public String getNewSubQueryId(){
		subQueryCounter++;
		return "sub"+this.subQueryCounter;
	}


	public ISingleDataMartWizardObject getSubQueryOnField(String fieldId) {		
		return (ISingleDataMartWizardObject)this.subqueryMap.get(fieldId);
	}




	public Map getSubqueries() {
		return subqueryMap;
	}




	public String getSubQueryIdForSubQueryOnField(String fieldId) {
		return (String)mapFieldIdSubQUeryId.get(fieldId);
	}




	public Map getMapFieldIdSubQUeryId() {
		return mapFieldIdSubQUeryId;
	}




	public void setMapFieldIdSubQUeryId(Map mapFieldIdSubQUeryId) {
		this.mapFieldIdSubQUeryId = mapFieldIdSubQUeryId;
	}




	public int getSubQueryCounter() {
		return subQueryCounter;
	}




	public void setSubQueryCounter(int subQueryCounter) {
		this.subQueryCounter = subQueryCounter;
	}




	public Map getSubqueryMap() {
		return subqueryMap;
	}




	public void setSubqueryMap(Map subqueryMap) {
		this.subqueryMap = subqueryMap;
	}


	public ISingleDataMartWizardObject getSelectedSubquery() {
		return selectedSubquery;
	}


	public void setSelectedSubquery(ISingleDataMartWizardObject selectedSubquery) {
		this.selectedSubquery = selectedSubquery;
	}

	public String getSubqueryErrMsg() {
		return subqueryErrMsg;
	}

	public void setSubqueryErrMsg(String subqueryErrMsg) {
		this.subqueryErrMsg = subqueryErrMsg;
	}
	
		
}
