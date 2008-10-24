/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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

import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

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
	
	public SpagoBiDataSource toSpagoBiDataSource(){
		SpagoBiDataSource sbd = new SpagoBiDataSource();
		sbd.setDriver(driver);
		sbd.setHibDialectClass("");
		sbd.setHibDialectName("");
		sbd.setJndiName(jndi);
		sbd.setLabel(label);
		sbd.setPassword(pwd);
		sbd.setUrl(urlConnection);
		sbd.setUser(user);
		return sbd;
	}
	/**
	 * Gets the ds id.
	 * 
	 * @return the ds id
	 */
	public int getDsId() {
		return dsId;
	}
	
	/**
	 * Sets the ds id.
	 * 
	 * @param dsId the new ds id
	 */
	public void setDsId(int dsId) {
		this.dsId = dsId;
	}
	
	/**
	 * Gets the descr.
	 * 
	 * @return the descr
	 */
	public String getDescr() {
		return descr;
	}
	
	/**
	 * Sets the descr.
	 * 
	 * @param descr the new descr
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Sets the label.
	 * 
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Gets the jndi.
	 * 
	 * @return the jndi
	 */
	public String getJndi() {
		return jndi;
	}
	
	/**
	 * Sets the jndi.
	 * 
	 * @param jndi the new jndi
	 */
	public void setJndi(String jndi) {
		this.jndi = jndi;
	}
	
	/**
	 * Gets the url connection.
	 * 
	 * @return the url connection
	 */
	public String getUrlConnection() {
		return urlConnection;
	}
	
	/**
	 * Sets the url connection.
	 * 
	 * @param url_connection the new url connection
	 */
	public void setUrlConnection(String url_connection) {
		this.urlConnection = url_connection;
	}
	
	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 * 
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * Gets the pwd.
	 * 
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}
	
	/**
	 * Sets the pwd.
	 * 
	 * @param pwd the new pwd
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * Gets the driver.
	 * 
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}
	
	/**
	 * Sets the driver.
	 * 
	 * @param driver the new driver
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	/**
	 * Gets the dialect id.
	 * 
	 * @return the dialect id
	 */
	public Integer getDialectId() {
		return dialectId;
	}
	
	/**
	 * Sets the dialect id.
	 * 
	 * @param dialectId the new dialect id
	 */
	public void setDialectId(Integer dialectId) {
		this.dialectId = dialectId;
	}
	
	/**
	 * Gets the engines.
	 * 
	 * @return the engines
	 */
	public Set getEngines() {
		return engines;
	}

	/**
	 * Sets the engines.
	 * 
	 * @param engines the new engines
	 */
	public void setEngines(Set engines) {
		this.engines = engines;
	}
	
	/**
	 * Gets the objects.
	 * 
	 * @return the objects
	 */
	public Set getObjects() {
		return objects;
	}

	/**
	 * Sets the objects.
	 * 
	 * @param objects the new objects
	 */
	public void setObjects(Set objects) {
		this.objects = objects;
	}

}
