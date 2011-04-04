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

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaView;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.JpaProperties;
import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaView implements IJpaView {
	private BusinessView businessView;
	
	private static Logger logger = LoggerFactory.getLogger(JpaViewInnerTable.class);
	
	
	public JpaView(BusinessView businessView) {
		super();
		Assert.assertNotNull("Parameter [businessView] cannot be null", businessView);
		this.businessView = businessView;
	}


	protected BusinessModel getModel(){
		return businessView.getModel();
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.IJpaView#getTables()
	 */
	@Override
	public List<IJpaTable> getTables() {
		List<IJpaTable> innerTables;
		List<PhysicalTable> physiscalTables;
		
		physiscalTables =  businessView.getPhysicalTables();
		
		innerTables = new ArrayList<IJpaTable>();
		for(PhysicalTable physicaltable : physiscalTables) {
			innerTables.add( new JpaViewInnerTable(businessView, physicaltable) );
		}
		return innerTables;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.IJpaView#getColumns(it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaViewInnerTable)
	 */
	@Override
	public List<IJpaColumn> getColumns(JpaViewInnerTable table) {
		List<IJpaColumn> jpaColumns = new ArrayList<IJpaColumn>();
		List<BusinessColumn> businessColumns = businessView.getColumns();
		for (BusinessColumn businessColumn :businessColumns) {
			if(businessColumn.getPhysicalColumn().getTable()== table.getPhysicalTable()) {
				JpaColumn jpaColumn = new JpaColumn(businessColumn);
				JpaViewInnerTable jpaTable = new JpaViewInnerTable(businessView, businessColumn.getPhysicalColumn().getTable());
				jpaColumn.setJpaTable(jpaTable);
				jpaColumns.add(jpaColumn);
			}
		}
		
		return jpaColumns;	
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.IJpaView#getJoinRelationships()
	 */
	@Override
	public List<AbstractJpaRelationship> getJoinRelationships() {
		List<AbstractJpaRelationship> innerJoinRelationship;
		
		innerJoinRelationship = null;
		
		return innerJoinRelationship;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.IJpaView#getPackage()
	 */
	@Override
	public String getPackage() {
		logger.debug("IN");
		String result=null;
		ModelProperty property =  getModel().getProperties().get(JpaProperties.MODEL_PACKAGE);
        //check if property is setted, else get default value
        if (property.getValue() != null){
        	result= property.getValue();
        }
        else {
        	result= property.getPropertyType().getDefaultValue();
        }
        logger.debug("OUT: "+result);  
        return result;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.IJpaView#getClassName()
	 */
	@Override
	public String getClassName() {
		String name;
		
		name = null;
		try {
			name = StringUtils.tableNameToVarName(businessView.getName());
			name = StringUtils.initUpper(name);
		} catch (Throwable t) {
			logger.error("Impossible to get class name", t);
			name = "pippo";
		}
		return name;
	}
	
	
}
