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
package it.eng.spagobi.importexport;


/** 
 * Bean that describes a SpagoBI jdbc database connection
 */
public class JdbcConnection extends DBConnection {
	
	private String driverClassName = "";
	private String connectionString = "";
	
	/**
	 * Gets the connection string 
	 * @return the connection string 
	 */
	public String getConnectionString() {
		return connectionString;
	}
	/**
	 * Set the connection string
	 * @param connectionString the new connection string
	 */
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	/**
	 * Gets the connection driver class name
	 * @return the connection driver class name
	 */
	public String getDriverClassName() {
		return driverClassName;
	}
	/**
	 * Set the connection driver class name
	 * @param driverClassName the new connection driver class name
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	
	
}
