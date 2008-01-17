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

import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;

import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia
 *
 */
public interface IQuery {
	
	void addSelectField(String className, 
			   String classAlias, 
			   String fieldAlias, 
			   String fieldLabel, 
			   String selectFieldCompleteName,
			   String fldHibType,
			   String fldHibPrec,
			   String fldHibScale);
	
	
	
	boolean containEntityClass(EntityClass ec);	
	void addEntityClass(EntityClass ec);	
	List  getEntityClasses();
	void purgeNotReferredEntityClasses();
	void purgeNotReferredEntityClasses(String prefix);
			
	ISelectClause getSelectClause();	
	IOrderByClause getOrderByClause();	
	IGroupByClause getGroupByClause();	
	IWhereClause getWhereClause();
	
	void setWhereClause (IWhereClause aWhereClause);	
	void setSelectClause(ISelectClause aSelectClause);	
	void setOrderByClause(IOrderByClause orderByClause);	
	void setGroupByClause(IGroupByClause groupByClause);		
	
	void delSelectClause();	
	void delWhereClause();	
	void delOrderByClause();	
	void delGroupByClause();
			
	String getQueryId();	
	void setQueryId(String queryId);
		
	void setDistinct(boolean distinct);	
	boolean getDistinct();
	
	void addSubQueryOnField(String fieldId);	
	Map  getSubqueries();
	
	ISingleDataMartWizardObject getSubQueryOnField(String fieldId);	
	String getSubQueryIdForSubQueryOnField(String fieldId);
	
	String[] getDuplicatedAliases();
	
	boolean containsDuplicatedAliases();
	
	boolean isEmpty();
	
	boolean areAllEntitiesJoined();
	
	public String getSubqueryErrMsg();

	public void setSubqueryErrMsg(String subqueryErrMsg);
}
