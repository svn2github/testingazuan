/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;

import it.eng.qbe.model.DataMartModel;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.io.Serializable;
import java.util.List;


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
	
	public void composeQuery();
	
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
	
	
	
}
