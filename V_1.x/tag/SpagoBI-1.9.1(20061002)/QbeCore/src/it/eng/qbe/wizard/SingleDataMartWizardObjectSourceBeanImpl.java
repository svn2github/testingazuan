/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.DataMartModel;
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
import org.hibernate.Transaction;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SingleDataMartWizardObjectSourceBeanImpl implements ISingleDataMartWizardObject {

	
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
	
	public SingleDataMartWizardObjectSourceBeanImpl() {
		super();
		this.entityClasses = new ArrayList();
		this.subqueryMap = new HashMap();
		this.mapFieldIdSubQUeryId = new HashMap();
	
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
		/*
		String finalSqlQuery = null;
		
		String finalHQLQuery = getFinalQuery();
		if (finalHQLQuery != null){
			HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter(dm.createSessionFactory().openSession());
			finalSqlQuery = queryRewriter.rewrite( getFinalQuery() );
		}
		return finalSqlQuery;
		*/
		
		
		String finalSqlQuery = null;
		
		String finalHQLQuery = getFinalQuery();
		if (finalHQLQuery != null){
			Session aSession = Utils.getSessionFactory(dm, ApplicationContainer.getInstance()).openSession();
			HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter(aSession);
			finalSqlQuery = queryRewriter.rewrite( getFinalQuery() );
		}
		return finalSqlQuery;
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
	

	public String getQueryId() {
		return queryId;
	}




	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	
	public void composeQuery(){
		this.finalQuery = null;
		StringBuffer finalQuery = new StringBuffer();
		ISelectClause aSelectClause = this.getSelectClause();
		boolean afterFirst = false;
		Map fieldToAlias = new HashMap();
		
		
		if (aSelectClause != null){
			
			finalQuery.append("select ");
			java.util.List l= aSelectClause.getSelectFields();
		 	
			// if select clause is empty than the query itself is empty so set finalQuery to null
			if(l == null || l.size() <= 0) {
				return;
			}
			
		 		
		 	Iterator it = l.iterator();
		 
		 	if (this.getDistinct()){
		 		finalQuery.append("distinct ");
		 	}
		 	
		 	ISelectField aSelectField = null;
		 	while (it.hasNext()){
		 		aSelectField =(ISelectField)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
		 		finalQuery.append(aSelectField.getFieldName());
		 		
		 		if (aSelectField.getFieldAlias() != null) {
		 			if(aSelectField.getFieldNameWithoutOperators().equalsIgnoreCase(aSelectField.getFieldName())) {
		 				String alias = aSelectField.getFieldAlias().replaceAll(" ","_");
		 				finalQuery.append(" as "+ alias + " ");
			 			fieldToAlias.put(aSelectField.getFieldNameWithoutOperators(), alias);
		 			}
		 			
		 			
		 		}
		 	
		 		afterFirst = true;
		 	}
			
		} else{
			 return;
		}
		
		List entityClasses = this.getEntityClasses();
		
		
		afterFirst = false;
		EntityClass ec = null;
		if (entityClasses != null){
			finalQuery.append(" from ");
			
			for (Iterator it = entityClasses.iterator(); it.hasNext();){
				ec =(EntityClass)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
		 		finalQuery.append(ec.getClassName() + " as " + ec.getClassAlias() + " ");
		 		afterFirst = true;
			}
		}
		 
	
		IWhereClause aWhereClause = this.getWhereClause();  
		afterFirst = false;
		if (aWhereClause != null){
		 	finalQuery.append("where \n");
		 	java.util.List l= aWhereClause.getWhereFields();
		 	Iterator it = l.iterator();
		 	
		 	
		 	IWhereField aWhereField = null;
		 	String newFieldValue = null;
		 	String fieldName = null;
		 	while (it.hasNext()){
		 		aWhereField =(IWhereField)it.next();
		 		fieldName = aWhereField.getFieldName();
		 		
		 		finalQuery.append(fieldName);  
		 		finalQuery.append(" ");
		 		
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
		 		finalQuery.append(aWhereField.getFieldOperator());
		 		finalQuery.append(" ");
		 		String fValue = aWhereField.getFieldValue();
		 		if (fValue.startsWith("$subquery_") && fValue.endsWith("$")){
		 			int idx1 = fValue.indexOf("$subquery_");
		 			int idx2 = fValue.lastIndexOf("$");
		 			
		 			idx1 += "$subquery_".length();
		 			String subQueryFldId = fValue.substring(idx1, idx2);
		 			ISingleDataMartWizardObject subQueryObject = this.getSubQueryOnField(subQueryFldId);
		 			subQueryObject.composeQuery();
		 			finalQuery.append(" ( ");
		 			finalQuery.append(subQueryObject.getFinalQuery());
		 			finalQuery.append(" ) ");
		 		}else{
		 			if ((aWhereField.getFieldEntityClassForRightCondition() == null)&&(aWhereField.getHibernateType().endsWith("StringType")))
		 				finalQuery.append("'"+ fValue + "'");
		 			else
		 				finalQuery.append( fValue );
		 		}
		 		if (it.hasNext())
		 			finalQuery.append(" "+aWhereField.getNextBooleanOperator()+" ");
		 		afterFirst = true;
		 	}
		 }
		
		IGroupByClause aGroupByClause = this.getGroupByClause();  
		afterFirst = false;
		
		if (aGroupByClause != null){
			finalQuery.append(" group by ");
			java.util.List l= aGroupByClause.getGroupByFields();
		 	Iterator it = l.iterator();
		 
		 	
		 	IOrderGroupByField aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(IOrderGroupByField)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
		 		
//		 		if(fieldToAlias.containsKey(aOrderGroupByField.getFieldName()))
//		 			finalQuery.append(fieldToAlias.get(aOrderGroupByField.getFieldName()) + " ");
//		 		else
		 			finalQuery.append(aOrderGroupByField.getFieldName() + " ");
		 		
		 		afterFirst = true;
		 	}
		}
		
		IOrderByClause aOrderByClause= this.getOrderByClause();  
		afterFirst = false;
		
		if (aOrderByClause != null){
			finalQuery.append(" order by ");
			java.util.List l= aOrderByClause.getOrderByFields();
		 	Iterator it = l.iterator();
		 
		 	
		 	IOrderGroupByField aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(IOrderGroupByField)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
//		 		if(fieldToAlias.containsKey(aOrderGroupByField.getFieldName()))
//		 			finalQuery.append(fieldToAlias.get(aOrderGroupByField.getFieldName()) + " ");
//		 		else
		 			finalQuery.append(aOrderGroupByField.getFieldName() + " ");
		 				 		
		 		afterFirst = true;
		 	}
		}
		
		this.finalQuery = (finalQuery != null ? finalQuery.toString() : null);
		
		this.finalQuery = this.finalQuery.trim();
	
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
		return executeHqlQuery(dataMartModel, getFinalQuery(), pageNumber, pageSize);
	}
	
	public SourceBean executeExpertQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception {
		return executeSqlQuery(dataMartModel, getExpertQueryDisplayed(), pageNumber, pageSize);
	}
	
	public SourceBean executeSqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception {
		
		
		if (!(query.startsWith("select") || query.startsWith("SELECT"))){  
			throw new Exception("It's not possible change database status with qbe exepert query");
		}
		Session aSession = null;
		Transaction tx = null;
		
		try{
			aSession = Utils.getSessionFactory(dataMartModel, ApplicationContainer.getInstance()).openSession();
			tx = aSession.beginTransaction();
			String maxRowsForSQLExecution = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");
		
			int maxSQLResults = DEFAULT_MAX_ROWS_NUM;
			if (maxRowsForSQLExecution != null && maxRowsForSQLExecution.trim().length() == 0){
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
			if (tx != null)
				tx.rollback();
			if (aSession != null && aSession.isOpen())
				aSession.close();
		}
	}
	
	private SourceBean executeHqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception {
		Session aSession = null;
		try{		
			aSession = Utils.getSessionFactory(dataMartModel, ApplicationContainer.getInstance()).openSession();
		
				
		//Query aQuery = aSession.createQuery(getFinalQuery());
		Query aQuery = aSession.createQuery(query);
		
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
		queryResponseSourceBean.setAttribute("hasNextPage", new Boolean(hasNextPage));
		queryResponseSourceBean.setAttribute("hasPreviousPage", new Boolean(hasPrevPage));
					
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
	
		
}
