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

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.query.IQuery;
import it.eng.spago.base.SourceBean;

import java.io.Serializable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface ISingleDataMartWizardObject.
 */
public interface ISingleDataMartWizardObject extends Serializable {
	
	
	
	
	/**
	 * Gets the final query.
	 * 
	 * @return the final query
	 */
	public String getFinalQuery();
	
	
	
	/**
	 * Sets the final query.
	 * 
	 * @param query the new final query
	 */
	public void setFinalQuery(String query);
	
	/**
	 * Gets the final sql query.
	 * 
	 * @param dm the dm
	 * 
	 * @return the final sql query
	 */
	public String getFinalSqlQuery(DataMartModel dm);
	

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * Sets the description.
	 * 
	 * @param queryId the new description
	 */
	public void setDescription(String queryId);
	
	/**
	 * Compose query.
	 * 
	 * @param datamart the datamart
	 */
	public void composeQuery(IDataMartModel datamart);
	
	/**
	 * Gets the expert query displayed.
	 * 
	 * @return the expert query displayed
	 */
	public String getExpertQueryDisplayed();
	
	/**
	 * Sets the expert query displayed.
	 * 
	 * @param expert the new expert query displayed
	 */
	public void setExpertQueryDisplayed(String expert);
	
	/**
	 * Gets the expert query saved.
	 * 
	 * @return the expert query saved
	 */
	public String getExpertQuerySaved();
	
	/**
	 * Sets the expert query saved.
	 * 
	 * @param expert the new expert query saved
	 */
	public void setExpertQuerySaved(String expert);
		
	/**
	 * Checks if is use experted version.
	 * 
	 * @return true, if is use experted version
	 */
	public boolean isUseExpertedVersion();
		
	/**
	 * Sets the use experted version.
	 * 
	 * @param useExpertedVersion the new use experted version
	 */
	public void setUseExpertedVersion(boolean useExpertedVersion);
	
	/**
	 * Gets the visibility.
	 * 
	 * @return the visibility
	 */
	public boolean getVisibility();
	
	/**
	 * Sets the visibility.
	 * 
	 * @param visibility the new visibility
	 */
	public void setVisibility(boolean visibility);
	
	/**
	 * Gets the owner.
	 * 
	 * @return the owner
	 */
	public String getOwner();
	
	/**
	 * Sets the owner.
	 * 
	 * @param owner the new owner
	 */
	public void setOwner(String owner);
	
	
	/**
	 * Extract expert select fields list.
	 * 
	 * @return the list
	 */
	public List extractExpertSelectFieldsList(); 
	
	
	/**
	 * Execute expert query.
	 * 
	 * @param dataMartModel the data mart model
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean executeExpertQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception;
		
	/**
	 * Execute qbe query.
	 * 
	 * @param dataMartModel the data mart model
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean executeQbeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception;
			
	/**
	 * Execute query.
	 * 
	 * @param dataMartModel the data mart model
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean executeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception;
				
	/**
	 * Execute sql query.
	 * 
	 * @param dataMartModel the data mart model
	 * @param query the query
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * 
	 * @return the source bean
	 * 
	 * @throws Exception the exception
	 */
	public SourceBean executeSqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception;	
	
	
	//public ISingleDataMartWizardObject getSelectedSubquery() ;


	//public void setSelectedSubquery(ISingleDataMartWizardObject selectedSubquery) ;
	
	//public void selectSubquery(String fieldId);
	
	//public void addSubQueryOnField(String fieldId, ISingleDataMartWizardObject subquery);
	
	//public ISingleDataMartWizardObject getCopy();
	
	
	//public boolean isSelectedSubqueryValid() ;
	
	//public boolean isSubqueryValid(String fieldId);
	
	//public boolean isSubqueryValid(ISingleDataMartWizardObject subquery);
	
	
	
	/**
	 * Gets the query.
	 * 
	 * @return the query
	 */
	IQuery getQuery();
	////////////////////////////
	// IQuery
	///////////////////////////
	
	//boolean containEntityClass(EntityClass ec);	
	//void addEntityClass(EntityClass ec);	
	//List  getEntityClasses();
	//void purgeNotReferredEntityClasses();
	//void purgeNotReferredEntityClasses(String prefix);
			
	//ISelectClause getSelectClause();	
	//IOrderByClause getOrderByClause();	
	//IGroupByClause getGroupByClause();	
	//IWhereClause getWhereClause();
	
	//void setWhereClause (IWhereClause aWhereClause);	
	//void setSelectClause(ISelectClause aSelectClause);	
	//void setOrderByClause(IOrderByClause orderByClause);	
	//void setGroupByClause(IGroupByClause groupByClause);		
	
	//void delSelectClause();	
	//void delWhereClause();	
	//void delOrderByClause();	
	//void delGroupByClause();
			
	//String getQueryId();	
	//void setQueryId(String queryId);
		
	//void setDistinct(boolean distinct);	
	//boolean getDistinct();
	
	//void addSubQueryOnField(String fieldId);	
	//Map  getSubqueries();
	
	//ISingleDataMartWizardObject getSubQueryOnField(String fieldId);	
	//String getSubQueryIdForSubQueryOnField(String fieldId);
	
	//String[] getDuplicatedAliases();
	
	//boolean containsDuplicatedAliases();
	
	//boolean isEmpty();
	
	//boolean areAllEntitiesJoined();
	
	//public String getSubqueryErrMsg();

	//public void setSubqueryErrMsg(String subqueryErrMsg);
	
	/*
	void addSelectField(String className, 
			   String classAlias, 
			   String fieldAlias, 
			   String fieldLabel, 
			   String selectFieldCompleteName,
			   String fldHibType,
			   String fldHibPrec,
			   String fldHibScale);
	
	*/
	
}
