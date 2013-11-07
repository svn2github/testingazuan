/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.jpa;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.jpa.IJpaDataSource;
import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.query.IQueryField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.AbstractStatement;
import it.eng.qbe.statement.graph.bean.Relationship;
import it.eng.spagobi.utilities.StringUtils;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.objects.Couple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Parent;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class JPQLStatement extends AbstractStatement {
	
	protected IJpaDataSource dataSource;
	
	public static transient Logger logger = Logger.getLogger(JPQLStatement.class);
		
	protected JPQLStatement(IDataSource dataSource) {
		super(dataSource);
	}
	
	public JPQLStatement(IDataSource dataSource, Query query) {
		super(dataSource, query);
	}
	
	public void prepare() {
		String queryStr;
		
		// one map of entity aliases for each queries (master query + subqueries)
		// each map is indexed by the query id
		Map<String, Map<String, String>> entityAliasesMaps = new HashMap<String, Map<String, String>>();
		
		queryStr = compose(getQuery(), entityAliasesMaps);	

		if(getParameters() != null) {
			try {
				queryStr = StringUtils.replaceParameters(queryStr.trim(), "P", getParameters());
			} catch (IOException e) {
				throw new SpagoBIRuntimeException("Impossible to set parameters in query", e);
			}
			
		}	
		
		setQueryString(queryStr);
		
	}
	
	/*
	 * internally used to generate the parametric statement string. Shared by the prepare method 
	 * and the buildWhereClause method in order to recursively generate subquery statement 
	 * string to be embedded in the parent query.
	 */
	private String compose(Query query, Map<String, Map<String, String>> entityAliasesMaps) {
		String queryStr = null;
		String selectClause = null;
		String whereClause = null;
		String groupByClause = null;
		String orderByClause = null;
		String fromClause = null;
		String havingClause = null;
//		String viewRelation = null;
		
		Assert.assertNotNull(query, "Input parameter 'query' cannot be null");
		Assert.assertTrue(!query.isEmpty(), "Input query cannot be empty (i.e. with no selected fields)");
				
		// let's start with the query at hand
		entityAliasesMaps.put(query.getId(), new LinkedHashMap<String, String>());
		
//		JPQLBusinessViewUtility viewsUtility = new JPQLBusinessViewUtility(this);
		
		selectClause = JPQLStatementSelectClause.build(this, query, entityAliasesMaps);
		whereClause = JPQLStatementWhereClause.build(this, query, entityAliasesMaps);
		groupByClause = JPQLStatementGroupByClause.build(this, query, entityAliasesMaps);
		orderByClause = JPQLStatementOrderByClause.build(this, query, entityAliasesMaps);
		havingClause = JPQLStatementHavingClause.build(this, query, entityAliasesMaps);
//		viewRelation = viewsUtility.buildViewsRelations(entityAliasesMaps, query, whereClause);
		
		whereClause = JPQLStatementWhereClause.injectAutoJoins(this, whereClause, query, entityAliasesMaps);
		
		fromClause = JPQLStatementFromClause.build(this, query, entityAliasesMaps);
		
		whereClause = JPQLStatementWhereClause.fix(this, whereClause, query, entityAliasesMaps);
		
		queryStr = selectClause    + " " 
				   + fromClause    + " " 
				   + whereClause   + " "
//				   + viewRelation  + " " 
				   + groupByClause + " " 
				   + havingClause  + " " 
				   + orderByClause;
		
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

	
	public static String getNextAlias(Map entityAliasesMaps) {
		int aliasesCount = 0;
		Iterator it = entityAliasesMaps.keySet().iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			Map entityAliases = (Map)entityAliasesMaps.get(key);
			aliasesCount += entityAliases.keySet().size();
		}
		
		return "t_" + aliasesCount;
	}
	
	public Set getSelectedEntities() {
		Set selectedEntities;
		Map<String, Map<String, String>> entityAliasesMaps;
		Iterator entityUniqueNamesIterator;
		String entityUniqueName;
		IModelEntity entity;
		
		
		Assert.assertNotNull( getQuery(), "Input parameter 'query' cannot be null");
		Assert.assertTrue(! getQuery().isEmpty(), "Input query cannot be empty (i.e. with no selected fields)");
		
		selectedEntities = new HashSet();
		
		// one map of entity aliases for each queries (master query + subqueries)
		// each map is indexed by the query id
		entityAliasesMaps = new HashMap<String, Map<String, String>>();
		
		// let's start with the query at hand
		entityAliasesMaps.put( getQuery().getId(), new HashMap<String, String>());
		
		JPQLStatementSelectClause.build(this, getQuery(), entityAliasesMaps);
		JPQLStatementWhereClause.build(this, getQuery(), entityAliasesMaps);
		JPQLStatementGroupByClause.build(this, getQuery(), entityAliasesMaps);
		JPQLStatementOrderByClause.build(this, getQuery(), entityAliasesMaps);
		JPQLStatementFromClause.build(this, getQuery(), entityAliasesMaps);
		
		Map entityAliases = (Map)entityAliasesMaps.get( getQuery().getId());
		entityUniqueNamesIterator = entityAliases.keySet().iterator();
		while(entityUniqueNamesIterator.hasNext()) {
			entityUniqueName = (String)entityUniqueNamesIterator.next();
			entity = getDataSource().getModelStructure().getEntity( entityUniqueName );
			selectedEntities.add(entity);
		}
		return selectedEntities;
	}
	
	
	


	public String getQueryString() {		
		if(super.getQueryString() == null) {
			this.prepare();
		}
		
		return super.getQueryString();
	}
	
	public String getSqlQueryString() {

		JPADataSource ds = ((JPADataSource)getDataSource());
		EntityManager em = ds.getEntityManager();

		JPQL2SQLStatementRewriter translator = new JPQL2SQLStatementRewriter(em);
		return translator.rewrite( getQueryString());

	}


	@Override
	public String getValueBounded(String operandValueToBound, String operandType) {
		JPQLStatementWhereClause clause = new JPQLStatementWhereClause(this);
		return clause.getValueBounded(operandValueToBound, operandType);
	}
	
	
	private String getEntityAlias(IModelField datamartField, Map entityAliases, Map entityAliasesMaps){
		IModelEntity rootEntity;
		
		Couple queryNameAndRoot = datamartField.getQueryName();

		
		if(queryNameAndRoot.getSecond()!=null){
			rootEntity = (IModelEntity)queryNameAndRoot.getSecond(); 	
		}else{
			rootEntity = datamartField.getParent().getRoot(); 	
		}

		String rootEntityAlias = (String)entityAliases.get(rootEntity.getUniqueName());
		if(rootEntityAlias == null) {
			rootEntityAlias = getNextAlias(entityAliasesMaps);
			entityAliases.put(rootEntity.getUniqueName(), rootEntityAlias);
		}
		
		return rootEntityAlias;
	}
	
	public String getFieldAliasNoRoles(IModelField datamartField, Map entityAliases, Map entityAliasesMaps){
		

		
		Couple queryNameAndRoot = datamartField.getQueryName();
		
		String queryName = (String) queryNameAndRoot.getFirst();
		

		String rootEntityAlias = getEntityAlias(datamartField, entityAliases, entityAliasesMaps);


		return rootEntityAlias + "." + queryName;//queryName.substring(0,1).toLowerCase()+queryName.substring(1);
	}
	
	/**
	 * Creates a list of clauses. for each role of the entity create this clause part: role+.+field name.
	 * This function is used to create the joins for the entities 
	 * @param datamartField
	 * @param entityAliases
	 * @param entityAliasesMaps
	 * @param roleName
	 * @return
	 */
	public String getFieldAliasWithRoles(IModelField datamartField, Map entityAliases, Map entityAliasesMaps, String roleName){
		
		IModelEntity rootEntity;
		
		Couple queryNameAndRoot = datamartField.getQueryName();
		
		String queryName = (String) queryNameAndRoot.getFirst();
		
		if(queryNameAndRoot.getSecond()!=null){
			rootEntity = (IModelEntity)queryNameAndRoot.getSecond(); 	
		}else{
			rootEntity = datamartField.getParent().getRoot(); 	
		}
		
		
		Set<String> entityRoleAlias = getQuery().getEntityRoleAlias(rootEntity, getDataSource());
		
		String rootEntityAlias = getEntityAlias(datamartField, entityAliases, entityAliasesMaps);

		//if there is no role for this field
		if(entityRoleAlias!=null){
			rootEntityAlias = buildEntityAliasWithRoles(rootEntity, roleName, rootEntityAlias);
		}

		
		return  rootEntityAlias + "." + queryName.substring(0,1).toLowerCase()+queryName.substring(1);
	}
	
	/**
	 * Creates a list of clauses. for each role of the entity create this clause part: role+.+field name.
	 * This function is used to create the joins for the entities 
	 * @param datamartField
	 * @param entityAliases
	 * @param entityAliasesMaps
	 * @param roleName
	 * @return
	 */
	public List<String> getFieldAliasWithRolesList(IModelField datamartField, Map entityAliases, Map entityAliasesMaps){
		
		List<String> toReturn = new ArrayList<String>();
		
		IModelEntity rootEntity;
		
		Couple queryNameAndRoot = datamartField.getQueryName();
		
		String queryName = (String) queryNameAndRoot.getFirst();
		
		if(queryNameAndRoot.getSecond()!=null){
			rootEntity = (IModelEntity)queryNameAndRoot.getSecond(); 	
		}else{
			rootEntity = datamartField.getParent().getRoot(); 	
		}
		
		
		Map<String, List<String>> roleAliasMap = getQuery().getMapEntityRoleField( getDataSource()).get(rootEntity);
		Set<String> roleAlias = null;
		if(roleAliasMap!=null){
			roleAlias = roleAliasMap.keySet();
		}
		
		String rootEntityAlias = getEntityAlias(datamartField, entityAliases, entityAliasesMaps);
		
		if(roleAlias!=null && roleAlias.size()>1){
			Iterator<String> iter = roleAlias.iterator();
			while(iter.hasNext()){
				String firstRole = iter.next();
				String rootEntityAliasWithRole = buildEntityAliasWithRoles(rootEntity, firstRole, rootEntityAlias);
				toReturn.add(rootEntityAliasWithRole + "." + queryName.substring(0,1).toLowerCase()+queryName.substring(1));
			}
		}else{
			toReturn.add(rootEntityAlias + "." + queryName);//queryName.substring(0,1).toLowerCase()+queryName.substring(1));
		}

		return toReturn;
	}
	
	/**
	 * Check if a select field with the alias exist and if so take it as field to get the query name.
	 * @param datamartField
	 * @param entityAliases
	 * @param entityAliasesMaps
	 * @param alias
	 * @return
	 */
	public String getFieldAliasWithRolesFromAlias(IModelField datamartField, Map entityAliases, Map entityAliasesMaps, String alias){
		Query query = this.getQuery();
		List<ISelectField> fields = query.getSelectFields(true);
		if(alias!=null){
			for(int i=0; i<fields.size();i++){
				ISelectField field = fields.get(i);
				if(field.getAlias().equals(alias) && field.getName().equals(datamartField.getUniqueName())){
					return getFieldAliasWithRoles(datamartField, entityAliases, entityAliasesMaps, field);
				}
			}
		}

		return getFieldAliasNoRoles(datamartField, entityAliases, entityAliasesMaps);
	}
	
	public String getFieldAliasWithRoles(IModelField datamartField, Map entityAliases, Map entityAliasesMaps, IQueryField queryField){
		
		IModelEntity rootEntity;
		
		Couple queryNameAndRoot = datamartField.getQueryName();
		String queryName = (String) queryNameAndRoot.getFirst();
		
		if(queryNameAndRoot.getSecond()!=null){
			rootEntity = (IModelEntity)queryNameAndRoot.getSecond(); 	
		}else{
			rootEntity = datamartField.getParent().getRoot(); 	
		}
		
		//List<List<Relationship>> roleAlias = (List<List<Relationship>>) rootEntity.getProperty(GraphUtilities.roleRelationsProperty);
		Map<String, List<String>> mapRoleField = getQuery().getMapEntityRoleField( getDataSource()).get(rootEntity);
		
		String rootEntityAlias = (String)entityAliases.get(rootEntity.getUniqueName());
		if(rootEntityAlias == null) {
			rootEntityAlias = getNextAlias(entityAliasesMaps);
			entityAliases.put(rootEntity.getUniqueName(), rootEntityAlias);
		}
		
		if(mapRoleField!=null && mapRoleField.keySet().size()>0){
			Iterator<String> iter = mapRoleField.keySet().iterator();
			while (iter.hasNext()) {
				String role = (String) iter.next();
				List<String> fieldsWithRole = mapRoleField.get(role);
				if(fieldsWithRole!=null && fieldsWithRole.contains(queryField.getAlias())){
					rootEntityAlias = buildEntityAliasWithRoles(rootEntity, role, rootEntityAlias);
					break;
				}
			}
		}

			

		return rootEntityAlias + "." + queryName.substring(0,1).toLowerCase()+queryName.substring(1);
	}
	
	public String getEntityAliasWithRoles(IModelEntity rootEntity, Map entityAliases, Map entityAliasesMaps){
		
		String rootEntityAlias = (String)entityAliases.get(rootEntity.getUniqueName());
		if(rootEntityAlias == null) {
			rootEntityAlias = getNextAlias(entityAliasesMaps);
			entityAliases.put(rootEntity.getUniqueName(), rootEntityAlias);
		}
		
		return rootEntityAlias;

	}
	
	
//	public String getEntityAliasWithRoles(IModelEntity rootEntity, Map entityAliases, Map entityAliasesMaps){
//		
//		List<List<Relationship>> roleAlias = (List<List<Relationship>>) rootEntity.getProperty(GraphUtilities.roleRelationsProperty);
//		
//		String rootEntityAlias = (String)entityAliases.get(rootEntity.getUniqueName());
//		if(rootEntityAlias == null) {
//			rootEntityAlias = getNextAlias(entityAliasesMaps);
//			entityAliases.put(rootEntity.getUniqueName(), rootEntityAlias);
//		}
//		
//		if(roleAlias!=null && roleAlias.size()>1){
//
////			for(int j=0; j<roleAlias.size(); j++){
////			List<Relationship> r = roleAlias.get(j);
//				List<Relationship> r = roleAlias.get(new Double((Math.random()*roleAlias.size())%roleAlias.size()).intValue());
//				return buildEntityAliasWithRoles(rootEntity, r, rootEntityAlias);
////			}
//			
//		}else{
//			return rootEntityAlias;
//		}
//	}
	
	public String buildFromEntityAliasWithRoles(IModelEntity me, String rel, String entityAlias){
		String fromClauseElement =  me.getName() + " "+ entityAlias;
		//for(int i=0; i<rel.size(); i++){
			fromClauseElement = fromClauseElement+("_"+rel).replace(" ", "");
		//}
		return fromClauseElement;
	}
	
	
	public String buildEntityAliasWithRoles(IModelEntity me, String role, String entityAlias){
		String fromClauseElement = (entityAlias+"_"+role).replace(" ", "");;
		return fromClauseElement;
	}
	
}
