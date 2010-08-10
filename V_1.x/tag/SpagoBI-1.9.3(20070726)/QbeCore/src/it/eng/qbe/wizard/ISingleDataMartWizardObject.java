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

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.spago.base.SourceBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ISingleDataMartWizardObject extends Serializable {
	
	
	public boolean containEntityClass(EntityClass ec);
	
	public void addEntityClass(EntityClass ec);
	
	public List  getEntityClasses();
	
	public String getFinalQuery();
	
	public ISelectClause getSelectClause();
	
	public IOrderByClause getOrderByClause();
	
	public IGroupByClause getGroupByClause() ;
	
	public IWhereClause getWhereClause();
	
	public void setWhereClause (IWhereClause aWhereClause);
	
	public void setSelectClause(ISelectClause aSelectClause);
	
	public void setOrderByClause(IOrderByClause orderByClause);
	
	public void setGroupByClause(IGroupByClause groupByClause);		
	
	public void delSelectClause();
	
	public void delWhereClause();
	
	public void delOrderByClause();
	
	public void delGroupByClause();
	
	public void setFinalQuery(String query);
	
	public String getFinalSqlQuery(DataMartModel dm);
	
	public void purgeNotReferredEntityClasses();
	
	public String getQueryId();
	
	public void setQueryId(String queryId);
	
	public String getDescription();
	
	public void setDescription(String queryId);
	
	public void composeQuery(IDataMartModel datamart);
	
	public String getExpertQueryDisplayed();
	
	public void setExpertQueryDisplayed(String expert);
	
	public String getExpertQuerySaved();
	
	public void setExpertQuerySaved(String expert);
		
	public boolean isUseExpertedVersion();
		
	public void setUseExpertedVersion(boolean useExpertedVersion);
	
	public boolean getVisibility();
	
	public void setVisibility(boolean visibility);
	
	public String getOwner();
	
	public void setOwner(String owner);
	
	public void setDistinct(boolean distinct);
	
	public boolean getDistinct();
	
	public List extractExpertSelectFieldsList(); 
	
	public SourceBean executeExpertQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception;
		
	public SourceBean executeQbeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception;
			
	public SourceBean executeQuery(DataMartModel dataMartModel, int pageNumber, int pageSize) throws Exception;
				
	public SourceBean executeSqlQuery(DataMartModel dataMartModel, String query, int pageNumber, int pageSize) throws Exception;	
	
	public void addSubQueryOnField(String fieldId);
	
	public Map  getSubqueries();
	
	public ISingleDataMartWizardObject getSubQueryOnField(String fieldId);
	
	public String getSubQueryIdForSubQueryOnField(String fieldId);
	
	public void purgeNotReferredEntityClasses(String prefix);
	
	
	public ISingleDataMartWizardObject getSelectedSubquery() ;


	public void setSelectedSubquery(ISingleDataMartWizardObject selectedSubquery) ;
	
	public void selectSubquery(String fieldId);
	
	public void addSubQueryOnField(String fieldId, ISingleDataMartWizardObject subquery);
	
	public ISingleDataMartWizardObject getCopy();
	
	public String getSubqueryErrMsg() ;

	public void setSubqueryErrMsg(String subqueryErrMsg) ;
	
	public boolean isSelectedSubqueryValid() ;
	
	public boolean isSubqueryValid(String fieldId);
	
	public boolean isSubqueryValid(ISingleDataMartWizardObject subquery);
	
}
