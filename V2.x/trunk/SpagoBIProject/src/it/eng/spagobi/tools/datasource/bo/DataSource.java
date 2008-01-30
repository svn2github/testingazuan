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
package it.eng.spagobi.tools.datasource.bo;

import java.io.Serializable;
import java.util.Set;



/**
 * Defines an <code>DataSource</code> object
 *
 */

public class DataSource implements Serializable {
	

	private int dsId;
	private String descr;
	private String label;
	private String jndi;
	private String urlConnection;
	private String user;
	private String pwd;
	private String driver;
	private Integer dialectId;	
	private Set engines = null;
	private Set objects = null;
	
	public int getDsId() {
		return dsId;
	}
	public void setDsId(int dsId) {
		this.dsId = dsId;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getJndi() {
		return jndi;
	}
	public void setJndi(String jndi) {
		this.jndi = jndi;
	}
	public String getUrlConnection() {
		return urlConnection;
	}
	public void setUrlConnection(String url_connection) {
		this.urlConnection = url_connection;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public Integer getDialectId() {
		return dialectId;
	}
	public void setDialectId(Integer dialectId) {
		this.dialectId = dialectId;
	}
	public Set getEngines() {
		return engines;
	}

	public void setEngines(Set engines) {
		this.engines = engines;
	}
	public Set getObjects() {
		return objects;
	}

	public void setObjects(Set objects) {
		this.objects = objects;
	}

}
