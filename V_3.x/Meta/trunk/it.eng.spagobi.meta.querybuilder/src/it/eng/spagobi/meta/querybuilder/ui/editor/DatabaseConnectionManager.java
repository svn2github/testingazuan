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
package it.eng.spagobi.meta.querybuilder.ui.editor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DatabaseConnectionManager {
	private static Map<String, String> drivers;
	private static Map<String, String> dialects;
	
	static {
		drivers = new HashMap<String, String>();
		drivers.put("oracle", "oracle.jdbc.OracleDriver");
		drivers.put("postgres", "org.postgresql.Driver");
		drivers.put("ingres", "com.ingres.jdbc.IngresDriver");
		drivers.put("microsoft", "org.hibernate.dialect.SQLServerDialect");
		drivers.put("teradata", "com.teradata.jdbc.TeraDriver");
		dialects.put("mysql", "com.mysql.jdbc.Driver");
		
		dialects = new HashMap<String, String>();
		dialects.put("oracle", "org.hibernate.dialect.OracleDialect");
		dialects.put("postgres", "org.hibernate.dialect.PostgreSQLDialect");
		dialects.put("ingres", "org.hibernate.dialect.IngresDialect");
		dialects.put("microsoft", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dialects.put("teradata", "org.hibernate.dialect.TeradataDialect");
		dialects.put("mysql", "org.hibernate.dialect.MySQLDialect");
	}
	
	public Set getSupportedDatabases() {
		return drivers.keySet();
	}
	
	public boolean isDatabaseSupported(String databaseName) {
		return drivers.keySet().contains(databaseName);
	}
	
	public String getJDBCDriverClassName(String databaseName) {
		return drivers.get(databaseName);
	}
	
	public boolean checkDriverClassAvailability(String databaseName) {
		return false;
	}
	
	public String getHibernateDialectClassName(String databaseName) {
		return dialects.get(databaseName);
	}
}
