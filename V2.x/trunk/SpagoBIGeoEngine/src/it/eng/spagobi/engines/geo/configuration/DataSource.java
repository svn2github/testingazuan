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
package it.eng.spagobi.engines.geo.configuration;

import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DataSource {
	
	private String jndiName;
	 
	private String driver;  

    private String password;

    private String url;

    private String user;

    private String label;


    public DataSource(SpagoBiDataSource spagoBIDataSource) {
    	this.driver = spagoBIDataSource.getDriver();
        this.jndiName = spagoBIDataSource.getJndiName();
        this.password = spagoBIDataSource.getPassword();
        this.url = spagoBIDataSource.getUrl();
        this.user = spagoBIDataSource.getUser();
        this.label = spagoBIDataSource.getLabel();
    }
    
    public DataSource(
    	   String driver,
    	   String jndiName,
    	   String password,
    	   String url,
    	   String user,
    	   String label) {
           
    	this.driver = driver;
           this.jndiName = jndiName;
           this.password = password;
           this.url = url;
           this.user = user;
           this.label = label;
    }


    public boolean checkIsJndi() {
    	return getJndiName() != null 
    			&& getJndiName().equals("") == false;
    }
    
    public Connection readConnection() throws NamingException, SQLException, ClassNotFoundException {
    	Connection connection = null;
    	 
    	if( checkIsJndi() ) {
    		connection = readJndiConnection();
    	} else {    		
    		connection = readDirectConnection();
    	}
    	
    	return connection;
    }
    
    
    /**
     * Get the connection from JNDI
     * 
     * @param connectionConfig
     *                SourceBean describing data connection
     * @return Connection to database
     * @throws NamingException 
     * @throws SQLException 
     */
    private Connection readJndiConnection() throws NamingException, SQLException {
		Connection connection = null;
		
		Context ctx;
		ctx = new InitialContext();
		javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup( getJndiName() );
		connection = ds.getConnection();
		
		return connection;
    }

    /**
     * Get the connection using jdbc
     * 
     * @param connectionConfig
     *                SpagoBiDataSource describing data connection
     * @return Connection to database
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    private Connection readDirectConnection() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		
		Class.forName( getDriver() );
		connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
		
		return connection;
    }
    
    
    
    
    /**
     * Gets the driver value for this SpagoBiDataSource.
     * 
     * @return driver
     */
    public java.lang.String getDriver() {
        return driver;
    }


    /**
     * Sets the driver value for this SpagoBiDataSource.
     * 
     * @param driver
     */
    public void setDriver(java.lang.String driver) {
        this.driver = driver;
    }


    /**
     * Gets the jndiName value for this SpagoBiDataSource.
     * 
     * @return jndiName
     */
    public java.lang.String getJndiName() {
        return jndiName;
    }


    /**
     * Sets the jndiName value for this SpagoBiDataSource.
     * 
     * @param jndiName
     */
    public void setJndiName(java.lang.String jndiName) {
        this.jndiName = jndiName;
    }


    /**
     * Gets the password value for this SpagoBiDataSource.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this SpagoBiDataSource.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the url value for this SpagoBiDataSource.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this SpagoBiDataSource.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }


    /**
     * Gets the user value for this SpagoBiDataSource.
     * 
     * @return user
     */
    public java.lang.String getUser() {
        return user;
    }


    /**
     * Sets the user value for this SpagoBiDataSource.
     * 
     * @param user
     */
    public void setUser(java.lang.String user) {
        this.user = user;
    }


    /**
     * Gets the label value for this SpagoBiDataSource.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this SpagoBiDataSource.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }

}
