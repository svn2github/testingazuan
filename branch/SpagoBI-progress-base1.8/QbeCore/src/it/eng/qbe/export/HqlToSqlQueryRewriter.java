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
package it.eng.qbe.export;

import it.eng.qbe.utility.Logger;

import java.util.Collections;
import java.util.HashMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.hql.ast.ASTQueryTranslatorFactory;

/**
 * @author Gioia
 * 
 */
public class HqlToSqlQueryRewriter implements IQueryRewriter {
	
	private Session session;
	
	public HqlToSqlQueryRewriter(Session session) {
		this.session = session;
	}
	
	public String rewrite(String query) {
		String sqlQuery = null;		
		Logger.debug(this.getClass(), "rewrite: HQL query to convert: " + query);		
		System.out.println("HQL query to convert: " + query);
		
		Query hibQuery = session.createQuery(query);
		SessionFactory sessFact = session.getSessionFactory();
		SessionFactoryImplementor imple = (SessionFactoryImplementor) sessFact;
		ASTQueryTranslatorFactory factory = new ASTQueryTranslatorFactory();
		QueryTranslator trans = factory.createQueryTranslator(hibQuery
				.getQueryString(), Collections.EMPTY_MAP, imple);
		trans.compile(new HashMap(), false);
		sqlQuery = trans.getSQLString();
		
		Logger.debug(this.getClass(), "rewrite: generated SQL query: " + sqlQuery);		
		System.out.println("generated SQL query: " + sqlQuery);
		return rewriteWithAlias(query, sqlQuery);
		//return sqlQuery;
	}
	
	private String getSelectClause(String query) {
		String selectClause = query.substring(query.indexOf("select") + "select".length(),
				query.indexOf("from"));
		return selectClause;
	}
	
	public String rewriteWithAlias(String hqlQuery,String sqlQuery) {
		String selectHqlBody = getSelectClause(hqlQuery);
		String[] fields = selectHqlBody.split(",");
		String[] fieldNames = new String[fields.length];
		for(int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i];
			if(fieldNames[i].indexOf("as") != -1) {
				fieldNames[i] = fieldNames[i].substring(fieldNames[i].indexOf("as")+2, fieldNames[i].length());
			} else {
				if(fieldNames[i].lastIndexOf('.') != -1) {
					fieldNames[i] = fieldNames[i].substring(fieldNames[i].lastIndexOf('.') + 1, fieldNames[i].length());
				}
			}
		}
		
		String selectSqlBody = getSelectClause(sqlQuery);
		fields = selectSqlBody.split(",");
		String newSelectBody = "";
		for(int i = 0; i < fields.length; i++) {
			String selectItem = fields[i];
			selectItem = selectItem.substring(0, selectItem.indexOf("as")+2);
			selectItem += " " + fieldNames[i];
			newSelectBody += ((i!=0)?", ":" ") + selectItem;
		}		
		String newQueryWithAlias = "select " + newSelectBody + sqlQuery.substring(sqlQuery.indexOf("from"), sqlQuery.length());
				
		return newQueryWithAlias;
	}

}
