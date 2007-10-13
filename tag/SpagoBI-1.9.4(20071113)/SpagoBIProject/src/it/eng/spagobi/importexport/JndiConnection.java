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
 * Bean that describes a SpagoBI jndi database connection
 */
public class JndiConnection extends DBConnection {
	
	private String jndiContextName = "";
	private String jndiName = "";
	
	/**
	 * gets the jndi context name
	 * @return the jndi context name
	 */
	public String getJndiContextName() {
		return jndiContextName;
	}
	/**
	 * Sets the new jndi context name
	 * @param jndiContextName the new jndi context name
	 */
	public void setJndiContextName(String jndiContextName) {
		this.jndiContextName = jndiContextName;
	}
	/**
	 * gets the jndi name
	 * @return the jndi name
	 */
	public String getJndiName() {
		return jndiName;
	}
	/**
	 * Sets the new jndi name
	 * @param jndiName the new jndi name
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

}
