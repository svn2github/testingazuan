/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.sql;

import it.eng.qbe.statement.AbstractQbeDataSet;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 */

public class SQLDataSet extends AbstractQbeDataSet {

	
	private List resultList;
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SQLDataSet.class);
    
	
	public SQLDataSet(SQLStatement statement) {
		super(statement);
	}
	
	
	public void loadData(int offset, int fetchSize, int maxResults) {
		EntityManager entityManager;
		
//		try {
//			entityManager = ((SQLStatement)statement.getDataSource()).getEntityManager();
//			loadDataPersistenceProvider(offset, fetchSize, maxResults, entityManager);
//		} catch (Throwable t) {
//			throw new RuntimeException("Impossible to load data", t);
//		}
	
	}
	
	
	
	private String getParameterKey(String fieldValue) {
		int beginIndex = fieldValue.indexOf("P{");
		int endIndex = fieldValue.indexOf("}");
		if (beginIndex > 0 && endIndex > 0 && endIndex > beginIndex) {
			return fieldValue.substring(beginIndex + 2, endIndex);
		} else {
			return null;
		}
	}

}