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
package it.eng.spagobi.tools.dataset.common.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class GroupByQueryTransformer extends AbstractQueryTransformer{
	
	List groupByColumnNames;
	List groupByColumnAlias;
	List aggregateColumnName;
	List aggregateFunctions;
	List aggregateColumnAlias;
	
	public GroupByQueryTransformer() {
		this(null);
		
	}
	
	public GroupByQueryTransformer(IQueryTransformer previousTransformer) {
		super(previousTransformer);
		groupByColumnNames = new ArrayList();
		groupByColumnAlias = new ArrayList();
		aggregateColumnName = new ArrayList();
		aggregateFunctions = new ArrayList();
		aggregateColumnAlias = new ArrayList();
	}
	
	public Object execTransformation(Object statement) {
	
		String transformedStatment = null;
		String alias;
		
		String subQueryAlias = "t" + System.currentTimeMillis();
    	
		alias = (String)(groupByColumnAlias.get(0) == null?groupByColumnNames.get(0): groupByColumnAlias.get(0));
    	transformedStatment = "SELECT " + subQueryAlias + "." + groupByColumnNames.get(0) + " AS " + groupByColumnNames.get(0);
    	
    	for(int i = 0; i < aggregateColumnName.size(); i++) {
    		
    		String aggFunc = (String)aggregateFunctions.get(i);
    		aggFunc = aggFunc.trim().toUpperCase();
    		alias = (String)(aggregateColumnAlias.get(i) == null?aggregateColumnName.get(i): aggregateColumnAlias.get(i));
    		
    		transformedStatment +=  ", " + aggFunc + "(" + subQueryAlias + "." + aggregateColumnName.get(i) + ") AS " + alias;
    	}
    	transformedStatment += " \nFROM ( " + statement + ") " + subQueryAlias;
    	transformedStatment += " \nGROUP BY " + subQueryAlias + "." + groupByColumnNames.get(0);
    	
		return transformedStatment;
	}
	
	public void addGrouByColumn(String columnName) {
		addGrouByColumn(columnName, null);
	}
	public void addGrouByColumn(String columnName, String columnAlias) {
		groupByColumnNames.add(columnName);
		groupByColumnAlias.add(columnAlias);
	}
	
	public void addAggregateColumn(String columnName, String aggregationFunction) {
		this.addAggregateColumn(columnName, aggregationFunction, null);
	}
			
	public void addAggregateColumn(String columnName, String aggregationFunction, String columnAlias) {
		aggregateColumnName.add(columnName);
		aggregateFunctions.add(aggregationFunction);
		aggregateColumnAlias.add(columnAlias);
	}
	
	
}
