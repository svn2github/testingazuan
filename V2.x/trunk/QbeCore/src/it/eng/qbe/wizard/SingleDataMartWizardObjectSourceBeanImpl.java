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
package it.eng.qbe.wizard;


import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.query.IQuery;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class SingleDataMartWizardObjectSourceBeanImpl.
 */
public class SingleDataMartWizardObjectSourceBeanImpl implements ISingleDataMartWizardObject {

	
	/** The sub query counter. */
	private int subQueryCounter = 0;

	/** The final query. */
	private String finalQuery = null;
	//private List entityClasses = null;
	
	/** The expert query displayed. */
	private String expertQueryDisplayed = null;
	
	/** The expert query saved. */
	private String expertQuerySaved = null;
	
	/** The owner. */
	private String owner = null;
	
	/** The visibility. */
	private boolean visibility;
	
	/** The use experted version. */
	private boolean useExpertedVersion = false;
	
	//private String queryId = null;
	/** The description. */
	private String description = null;
	
	/** The Constant DEFAULT_MAX_ROWS_NUM. */
	public static final int DEFAULT_MAX_ROWS_NUM = 5000;
	
	//private Map subqueryMap = null;
	//private Map mapFieldIdSubQUeryId = null;
	//private ISingleDataMartWizardObject selectedSubquery = null;
	//private String subqueryErrMsg = "";
	
	
	/** The query. */
	private it.eng.qbe.query.Query query = null;
	
	/**
	 * Instantiates a new single data mart wizard object source bean impl.
	 */
	public SingleDataMartWizardObjectSourceBeanImpl() {
		super();
		//this.entityClasses = new ArrayList();
		//this.subqueryMap = new HashMap();
		//this.mapFieldIdSubQUeryId = new HashMap();
		query = new it.eng.qbe.query.Query();
	
	}
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	public ISingleDataMartWizardObject getCopy() {
		SingleDataMartWizardObjectSourceBeanImpl wizardObject = new SingleDataMartWizardObjectSourceBeanImpl();
		
		// copy all the query relaed informations
		//query.setSubQueryCounter(subQueryCounter);
		//if(selectClause != null) wizardObject.setSelectClause(selectClause.getCopy());
		//if(whereClause != null) wizardObject.setWhereClause(whereClause.getCopy());
		//if(orderByClause != null) wizardObject.setOrderByClause(orderByClause.getCopy());
		//if(groupByClause != null) wizardObject.setGroupByClause(groupByClause.getCopy());
		wizardObject.setFinalQuery(finalQuery);
		List list = new ArrayList();
		/*
		for(int i = 0; i < entityClasses.size(); i++) {
			EntityClass entityClass = (EntityClass)entityClasses.get(i);
			list.add(entityClass.getCopy());
		}
		*/
		//wizardObject.setEntityClasses(list);
		wizardObject.setOwner(owner);
		wizardObject.setVisibility(visibility);
		//wizardObject.setDistinct(distinct);
		wizardObject.setUseExpertedVersion(useExpertedVersion);
		//.setQueryId(queryId);
		wizardObject.setDescription(description);
				
		return wizardObject;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getFinalQuery()
	 */
	public String getFinalQuery() {
		return this.finalQuery;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getFinalSqlQuery(it.eng.qbe.model.DataMartModel)
	 */
	public String getFinalSqlQuery(DataMartModel dm) {
		
		String finalSqlQuery = null;
		Session aSession = null;
		try {
			String finalHQLQuery = getFinalQuery();
			if (finalHQLQuery != null){
				aSession = dm.getDataSource().getSessionFactory().openSession();
				HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter(aSession);
				finalSqlQuery = queryRewriter.rewrite( getFinalQuery() );
			}
			return finalSqlQuery;
		} finally {
			if ((aSession != null) && (aSession.isOpen()))
				aSession.close();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#setFinalQuery(java.lang.String)
	 */
	public void setFinalQuery(String query) {
		this.finalQuery = query;
		
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#composeQuery(it.eng.qbe.model.IDataMartModel)
	 */
	public void composeQuery(IDataMartModel dataMartModel){
		finalQuery = null;
		IStatement statement = dataMartModel.createStatement(query);
		statement.setParameters(dataMartModel.getDataMartProperties());
		finalQuery = statement.getQueryString();	
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getDescription()
	 */
	public String getDescription() {
		return description;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}



/*
	public void setEntityClasses(List entityClasses) {
		this.entityClasses = entityClasses;
	}
	*/
	
	

	/* (non-Javadoc)
 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#isUseExpertedVersion()
 */
public boolean isUseExpertedVersion() {
		return useExpertedVersion;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#setUseExpertedVersion(boolean)
	 */
	public void setUseExpertedVersion(boolean useExpertedVersion) {
		this.useExpertedVersion = useExpertedVersion;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getExpertQueryDisplayed()
	 */
	public String getExpertQueryDisplayed() {
		return expertQueryDisplayed;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#setExpertQueryDisplayed(java.lang.String)
	 */
	public void setExpertQueryDisplayed(String expertQueryDisplayed) {
		this.expertQueryDisplayed = expertQueryDisplayed;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getVisibility()
	 */
	public boolean getVisibility() {
		return this.visibility;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#setVisibility(boolean)
	 */
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getOwner()
	 */
	public String getOwner() {
		return this.owner;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#setOwner(java.lang.String)
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}







	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getExpertQuerySaved()
	 */
	public String getExpertQuerySaved() {
		return expertQuerySaved;
	}




	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#setExpertQuerySaved(java.lang.String)
	 */
	public void setExpertQuerySaved(String expertQuerySaved) {
		this.expertQuerySaved = expertQuerySaved;
	}


	/**
	 * This method extracts the name of select fields (or the alias name if present), from the expert query.
	 * 
	 * @return the list of the name of select fields (or the alias name if present), from the expert query,
	 * null if the query is null or doesn't contain select fields
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
	
	/** The QUER y_ respons e_ sourc e_ bean. */
	public static String QUERY_RESPONSE_SOURCE_BEAN = "QUERY_RESPONSE_SOURCE_BEAN"; 
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#executeQbeQuery(it.eng.qbe.model.DataMartModel, int, int)
	 */
	public SourceBean executeQbeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception {
		
		IStatement statement = dataMartModel.createStatement(query);
		
		String maxRowsForSQLExecution = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");
		int maxResults = DEFAULT_MAX_ROWS_NUM;
		if( (maxRowsForSQLExecution!=null) && !(maxRowsForSQLExecution.trim().length()==0) ){
			maxResults = Integer.valueOf(maxRowsForSQLExecution).intValue();
		}
		statement.setMaxResults(maxResults);
		statement.setParameters(dataMartModel.getDataMartProperties());
		return statement.executeWithPagination(pageNumber, pageSize);
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#executeExpertQuery(it.eng.qbe.model.DataMartModel, int, int)
	 */
	public SourceBean executeExpertQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception {
		return executeSqlQuery(dataMartModel, getExpertQueryDisplayed(), pageNumber, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#executeSqlQuery(it.eng.qbe.model.DataMartModel, java.lang.String, int, int)
	 */
	public SourceBean executeSqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception {
		
		
		if (!(query.startsWith("select") || query.startsWith("SELECT"))){  
			throw new Exception("It's not possible change database status with qbe exepert query");
		}
		Session aSession = null;
		
		
		try{
			aSession = dataMartModel.getDataSource().getSessionFactory().openSession();
			
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
	
	/*
	private SourceBean executeHqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception {
		Session aSession = null;
		try{		
			aSession = dataMartModel.getDataSource().getSessionFactory().openSession();
			//dataMartModel.getDataSource().getSessionFactory();
		
			String maxRowsForSQLExecution = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");
			
			int maxSQLResults = DEFAULT_MAX_ROWS_NUM;
			if( (maxRowsForSQLExecution!=null) && !(maxRowsForSQLExecution.trim().length()==0) ){
				maxSQLResults = Integer.valueOf(maxRowsForSQLExecution).intValue();
			}
				
		Query aQuery = aSession.createQuery(query);
		
		String queryStr = aQuery.getQueryString();		
		
		Query countQuery = aSession.createQuery(queryStr);
		aQuery.setMaxResults(maxSQLResults);
		List countList = countQuery.list();
		
		int rowsNumber = countList.size();
	
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
	*/
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#executeQuery(it.eng.qbe.model.DataMartModel, int, int)
	 */
	public SourceBean executeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception {
		if(isUseExpertedVersion()) return executeExpertQuery(dataMartModel, pageNumber, pageSize);
		return executeQbeQuery(dataMartModel, pageNumber, pageSize);			
	}

	
	
	
	/*
	
	public boolean isSelectedSubqueryValid() {
		ISingleDataMartWizardObject subquery = getSelectedSubquery();
		return isSubqueryValid(subquery);
	}
	
	public boolean isSubqueryValid(String fieldId) {
		ISingleDataMartWizardObject subquery = getSubQueryOnField(fieldId);
		return isSubqueryValid(subquery);
	}
	*/
	
	/*
	public boolean isSubqueryValid(ISingleDataMartWizardObject subquery) {
		
		boolean missingCondition = false;
		boolean invalidReference = false;
		subquery.getQuery().setSubqueryErrMsg(null);
		subquery.getQuery().setSubqueryErrMsg(null);
		
		if(subquery !=  null) {
			IWhereClause whereClause = subquery.getQuery().getWhereClause();
			if(whereClause != null) {
				List fields = whereClause.getWhereFields();
				for(int i = 0; i < fields.size(); i++) {
					IWhereField whereField = (IWhereField)fields.get(i);
					// check missing right condition
					String value = whereField.getFieldValue();
					if(value == null || value.trim().equalsIgnoreCase("")) {
						
						missingCondition = true;
					}  
					
					this.query.getEntityClasses();
					EntityClass ec = whereField.getFieldEntityClassForRightCondition();
					if(ec != null && !query.containEntityClass(ec)) {
						subquery.getQuery().setSubqueryErrMsg("Subquery contains at least one where condition not properly defined");
						invalidReference = true;
					}
					
					if(invalidReference && missingCondition) break;					
				}
			}
		}
		
		// resolve all invalid references first; missing right values next
		if(invalidReference) {
			subquery.getQuery().setSubqueryErrMsg("Subquery contains at least one where condition not properly defined (cause: invalid reference to a parent entity)");
		}
		else if(missingCondition) {
			subquery.getQuery().setSubqueryErrMsg("Subquery contains at least one where condition not properly defined (cause: missing right end value)");
		}
		
		
		return (!invalidReference && !missingCondition);		
	}
	*/
	
	/*
	public void selectSubquery(String fieldId) {
		ISingleDataMartWizardObject subquery = getSubQueryOnField(fieldId);
		if(subquery !=  null) {
			ISingleDataMartWizardObject selectedSubquery = (SingleDataMartWizardObjectSourceBeanImpl)subquery.getCopy();
			setSelectedSubquery(selectedSubquery);
		} else {
			ISingleDataMartWizardObject selectedSubquery = new SingleDataMartWizardObjectSourceBeanImpl();
			setSelectedSubquery(selectedSubquery);
			mapFieldIdSubQUeryId.put(fieldId, getNewSubQueryId());
		}
	}
	*/
	
	
	

	


	




	




	

/*
	public ISingleDataMartWizardObject getSelectedSubquery() {
		return query.getSelectedSubquery();
	}


	public void setSelectedSubquery(ISingleDataMartWizardObject selectedSubquery) {
		query.setSelectedSubquery(selectedSubquery);
	}
	*/

	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.wizard.ISingleDataMartWizardObject#getQuery()
	 */
	public IQuery getQuery() {
		return query;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// DEPRECATED
	//////////////////////////////////////////////////////////////////////////////////////////////
	/*
	public String getSubqueryErrMsg() {
		return query.getSubqueryErrMsg();
	}

	public void setSubqueryErrMsg(String subqueryErrMsg) {
		query.setSubqueryErrMsg(subqueryErrMsg);
	}
	
	public boolean isEmpty(){
		return query.isEmpty();
	}
	*/
	/*
	public ISelectClause getSelectClause() {
		return query.getSelectClause();
	}
	*/
	
	/*
	public IWhereClause getWhereClause() {
		return query.getWhereClause();
	}
	*/
	
	/*
	public IOrderByClause getOrderByClause() {
		return query.getOrderByClause();
	}
	
	public IGroupByClause getGroupByClause() {
		
		return query.getGroupByClause();
		
	}
	
	public void setOrderByClause(IOrderByClause orderByClause) {
		query.setOrderByClause(orderByClause);
	}
	
	public void setGroupByClause(IGroupByClause groupByClause) {
		query.setGroupByClause(groupByClause)	;	
	}
	*/
	
	/*
	public void setWhereClause(IWhereClause whereClause) {
		query.setWhereClause(whereClause);
		
	}

	
	public void setSelectClause(ISelectClause aSelectClause) {
		query.setSelectClause(aSelectClause);
	}
	
	
	public void delSelectClause() {
		query.delSelectClause();
	}
	
	public void delWhereClause() {
		query.delWhereClause();
	}
	*/
	
	/*
	public void delOrderByClause() {
		query.delOrderByClause();
	}
	
	public void delGroupByClause() {
		query.delGroupByClause();
	}
	*/

	/*
	public String getQueryId() {
		return query.getQueryId();
	}

	public void setQueryId(String queryId) {
		query.setQueryId(queryId);
	}
	
	public void addSubQueryOnField(String fieldId, ISingleDataMartWizardObject subquery) {		
		query.addSubQueryOnField(fieldId, subquery);
	}
	
	public boolean areAllEntitiesJoined() {
		
		return query.areAllEntitiesJoined();
	}
	
	public boolean areEntitiyJoined(EntityClass targetEntity) {
		return query.areEntitiyJoined(targetEntity);
	}
	
	public Map getSubqueries() {
		return query.getSubqueries();
	}

	public String getSubQueryIdForSubQueryOnField(String fieldId) {
		return query.getSubQueryIdForSubQueryOnField(fieldId);
	}
	
	public Map getMapFieldIdSubQUeryId() {
		return query.getMapFieldIdSubQUeryId();
	}




	public void setMapFieldIdSubQUeryId(Map mapFieldIdSubQUeryId) {
		query.setMapFieldIdSubQUeryId(mapFieldIdSubQUeryId);
	}




	public int getSubQueryCounter() {
		return query.getSubQueryCounter();
	}




	public void setSubQueryCounter(int subQueryCounter) {
		query.setSubQueryCounter(subQueryCounter);
	}

	public Map getSubqueryMap() {
		return query.getSubqueryMap();
	}


	public void setSubqueryMap(Map subqueryMap) {
		query.setSubqueryMap(subqueryMap);
	}
	*/
	
	/*
	public void addEntityClass(EntityClass ec) {
		query.addEntityClass(ec);
	}
	
	public List getEntityClasses() {
		return query.getEntityClasses();
	}
	*/
	
	/*
	public boolean containEntityClass(EntityClass parameEc) {
		
		return query.containEntityClass(parameEc);
							
	}
	*/
	
	
	/*
	public void purgeNotReferredEntityClasses() {
		query.purgeNotReferredEntityClasses();
	}
	*/
	
	/*
	public void purgeNotReferredEntityClasses(String prefix) {
		query.purgeNotReferredEntityClasses(prefix);
	}
	*/
	
	/*
	public void addSubQueryOnField(String fieldId) {
		query.addSubQueryOnField(fieldId);
	}
	*/
	
	
	/*
	public String[] getDuplicatedAliases() {
		return query.getDuplicatedAliases();
	}
	
	public boolean containsDuplicatedAliases() {
		return query.containsDuplicatedAliases();
	}
	
	public ISingleDataMartWizardObject getSubQueryOnField(String fieldId) {		
		return query.getSubQueryOnField(fieldId);
	}
	
	public String getNewSubQueryId(){
		return query.getNewSubQueryId();
	}	
	*/
	
	/*
	public void addSelectField(String className, 
			   String classAlias, 
			   String fieldAlias, 
			   String fieldLabel, 
			   String selectFieldCompleteName,
			   String fldHibType,
			   String fldHibPrec,
			   String fldHibScale){
		query.addSelectField(className, classAlias, fieldAlias, fieldLabel, selectFieldCompleteName, 
				fldHibType, fldHibPrec, fldHibScale);
	}
	*/
}
