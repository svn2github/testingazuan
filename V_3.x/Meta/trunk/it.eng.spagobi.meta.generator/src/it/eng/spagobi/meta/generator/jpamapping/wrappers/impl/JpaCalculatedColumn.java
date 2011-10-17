/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.generator.jpamapping.wrappers.impl;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaCalculatedColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.JpaProperties;
import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.CalculatedBusinessColumn;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.util.JDBCTypeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaCalculatedColumn implements IJpaCalculatedColumn {
	public static final String CALCULATED_COLUMN_EXPRESSION = "structural.expression";
	public static final String CALCULATED_COLUMN_DATATYPE = "structural.datatype";

	CalculatedBusinessColumn businessCalculatedColumn;
	AbstractJpaTable jpaTable;
	
	private static Logger logger = LoggerFactory.getLogger(JpaCalculatedColumn.class);
	
	/**
	 * 
	 * @param parentTable the jpaTable that contains this column
	 * @param businessColumn the wrapped business column
	 */
	protected JpaCalculatedColumn(AbstractJpaTable parentTable, CalculatedBusinessColumn businessCalculatedColumn) {
		this.jpaTable = parentTable;
		this.businessCalculatedColumn = businessCalculatedColumn;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn#getName()
	 */
	@Override
	public String getName() {
		return businessCalculatedColumn.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn#getDescription()
	 */
	@Override
	public String getDescription() {
		return businessCalculatedColumn.getDescription() != null? businessCalculatedColumn.getDescription(): getName();
	}
	

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.IJpaColumn#getJpaTable()
	 */
	@Override
	public IJpaTable getJpaTable() {
		return jpaTable;
	}


	@Override
	public String getAttribute(String name) {
		ModelProperty property = businessCalculatedColumn.getProperties().get(name);
		return property != null? property.getValue(): "";
	}
	
	@Override
	public String getExpression(){
		ModelProperty property = businessCalculatedColumn.getProperties().get(CALCULATED_COLUMN_EXPRESSION);
		return property != null? property.getValue(): "";
	}
	@Override
	public String getDataType(){
		ModelProperty property = businessCalculatedColumn.getProperties().get(CALCULATED_COLUMN_DATATYPE);
		return property != null? property.getValue(): "";
	}
	
	public List<IJpaColumn> getReferencedColumns(){
		List<IJpaColumn> jpaColumns = new ArrayList<IJpaColumn>();
		List<SimpleBusinessColumn> businessColumns = businessCalculatedColumn.getReferencedColumns();
		if (!businessColumns.isEmpty()){
			for(SimpleBusinessColumn businessColumn : businessColumns ){
				JpaColumn jpaColumn = new JpaColumn(jpaTable, businessColumn);
				jpaColumns.add(jpaColumn);
				System.out.println("Calculated Column "+this.getName()+" references column "+jpaColumn.getUniqueName());
				logger.debug("Calculated Column [{}] references column [{}]", this.getName(), businessColumn.getName());
			}
		}
		return jpaColumns;	
	}

	public String getExpressionWithUniqueNames(){
		String expression = getExpression();
		List<IJpaColumn> jpaColumns = this.getReferencedColumns();
		List<String> operands = new ArrayList<String>();
		
		if (!jpaColumns.isEmpty()){
			//retrieve operands from string
			StringTokenizer stk = new StringTokenizer(expression, "+-|*/()");
			while(stk.hasMoreTokens()){
				String operand = stk.nextToken().trim();
				System.out.println("Found Operand "+operand);
				operands.add(operand);
			}
		}
		
		for (int i = 0; i<operands.size(); i++){
			System.out.println("Replacing "+operands.get(i)+" with "+jpaColumns.get(i).getUniqueName());
			expression = expression.replace(operands.get(i),jpaColumns.get(i).getUniqueName());
		}
		expression = expression.replaceAll("/", ":");
		
		/*
		for (int i = 0; i<operands.size(); i++){
			System.out.println("Replacing "+operands.get(i)+" with "+jpaColumns.get(i).getName());
			expression = expression.replace(operands.get(i),jpaColumns.get(i).getName());
		}*/
		
		
		return expression;
	}
	
}
