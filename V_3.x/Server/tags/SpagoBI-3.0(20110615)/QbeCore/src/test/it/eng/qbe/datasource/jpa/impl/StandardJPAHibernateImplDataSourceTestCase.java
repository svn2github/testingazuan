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
package it.eng.qbe.datasource.jpa.impl;

import it.eng.qbe.datasource.AbstractDataSourceTestCase;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.datasource.jpa.JPADriver;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class StandardJPAHibernateImplDataSourceTestCase extends AbstractDataSourceTestCase {
	
	
	private static final String QBE_FILE = "test-resources/jpa/jpaImpl/hibernate/datamart.jar";
	
	@Override
	protected void setUpDataSource() {
		IDataSourceConfiguration configuration;
		
		modelName = "foodmart"; 
		
		File file = new File(QBE_FILE);
		configuration = new FileDataSourceConfiguration(modelName, file);
		configuration.loadDataSourceProperties().put("connection", connection);
		dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
		
		testEntityUniqueName = "it.eng.spagobi.meta.Customer::Customer";
	}
	
	public void testJpaHibernateImpl() {
		doTests();
	}
	
	public void doTests() {
		super.doTests();
		// add custom tests here
		doTestDataSourceImplementation();
	}
	
	// add Hibernate specific tests here ...
	
	public void doTestDataSourceImplementation() {
		assertTrue(dataSource instanceof JPADataSource);
	}
	
	public void doTestLabelLocalization() {}
	public void doTestTooltipLocalization() {}
}
