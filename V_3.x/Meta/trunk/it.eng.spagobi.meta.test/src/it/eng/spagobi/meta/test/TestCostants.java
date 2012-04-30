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
package it.eng.spagobi.meta.test;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class TestCostants {
	
	public static File workspaceFolder = new File("D:/Documenti/Sviluppo/workspaces/helios/metadata");
	public static File outputFolder = new File("D:/Documenti/Sviluppo/workspaces/unit-test");
	
	public enum DatabaseType { MYSQL, POSTGRES, ORACLE };
	
	public static boolean enableTestsOnMySql = true;
	public static boolean enableTestsOnPostgres = false;
	public static boolean enableTestsOracle = false;
	
	// =======================================================
	// MYSQL
	// =======================================================
	public static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static String MYSQL_URL = "jdbc:mysql://localhost:3306/meta_test";
	public static String MYSQL_USER = "root";
	public static String MYSQL_PWD = "mysql";
	public static String MYSQL_DEFAULT_CATALOGUE = "meta_test";		
	public static String MYSQL_DEFAULT_SCHEMA = null;	
	public static String MYSQL_DEFAULT_DIALECT = "org.hibernate.dialect.MySQLDialect";
	public static String[] MYSQL_TABLE_NAMES = new String[]{
		"_product_class_"
		, "currency"
		, "currency_view"
		, "customer"
		, "days"
		, "department"
		, "employee"
		, "employee_closure"
		, "inventory_fact_1998"
		, "object"
		, "position"
		, "product"
		, "product_class"
		, "promotion"
		, "region"
		, "reserve_employee"
		, "salary"
		, "sales_fact_1998"
		, "sales_region"
		, "store"
		, "store_ragged"
		, "test_names"
		, "test_types"
		, "time_by_day"
		, "warehouse"
		, "warehouse_class"
	};
	
	public static String[] MYSQL_FILTERED_TABLES_FOR_PMODEL = {
		"_product_class_", "product" , "product_class", "sales_fact_1998" 
		, "currency_view", "department" , "employee", "employee_closure"
		, "object" , "position", "reserve_employee", "salary"
	};
	
	// must be a subset of MYSQL_FILTERED_TABLES_FOR_PMODEL
	public static String[] MYSQL_FILTERED_TABLES_FOR_BMODEL = { 
		"_product_class_", "product" , "product_class", "sales_fact_1998", "object" 
	};

	// =======================================================
	// POSTGRES
	// =======================================================
	public static String POSTGRES_DRIVER = "org.postgresql.Driver";
	public static String POSTGRES_URL = "jdbc:postgresql://localhost:5432/Meta_tesT";
	public static String POSTGRES_USER = "postgres";
	public static String POSTGRES_PWD = "postgres";
	public static String POSTGRES_DEFAULT_CATALOG = "Meta_tesT";	
	public static String POSTGRES_DEFAULT_SCHEMA = "public";	
	public static String POSTGRES_DEFAULT_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
	
	public static String[] POSTGRES_TABLE_NAMES  = new String[]{
		"_Product_ClaSS_"
		, "currency"
		, "cUrReNcY"
		, "customer"
		, "days"
		, "department"
		, "employee"
		, "employee_closure"
		, "inventory_fact_1998"
		//, "object"
		, "position"
		, "product"
		, "product_class"
		, "promotion"
		, "region"
		, "reserve_employee"
		, "salary"
		, "sales_fact_1998"
		, "sales_region"
		, "store"
		, "store_ragged"
		//, "test_names"
		, "time_by_day"
		, "warehouse"
		, "warehouse_class"
	};
	public static final String[] POSTGRES_FILTERED_TABLES_FOR_PMODEL = {
		"_Product_ClaSS_", "product" , "product_class", "sales_fact_1998" 
		, "cUrReNcY", "department" , "employee", "employee_closure"
		, "position", "reserve_employee", "salary", // "object"
	};
	
	public static final String[] POSTGRES_FILTERED_TABLES_FOR_BMODEL = { 
		"_Product_ClaSS_", "product" , "product_class", "sales_fact_1998"// , "object" 
	};
	

	
	// =======================================================
	// ORACLE
	// =======================================================
	public static String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	public static String ORACLE_URL = "jdbc:oracle:thin:@172.27.1.90:1521:REPO";
	public static String ORACLE_USER = "spagobi";
	public static String ORACLE_PWD = "bispago";	
	public static String ORACLE_DEFAULT_CATALOGUE = null;	
	public static String ORACLE_DEFAULT_SCHEMA = "SPAGOBI";	
	public static String ORACLE_DEFAULT_DIALECT = "org.hibernate.dialect.OracleDialect";
	
	public static final String[] ORACLE_FILTERED_TABLES_FOR_PMODEL = MYSQL_FILTERED_TABLES_FOR_PMODEL;
	public static final String[] ORACLE_FILTERED_TABLES_FOR_BMODEL = MYSQL_FILTERED_TABLES_FOR_BMODEL;
	

}
