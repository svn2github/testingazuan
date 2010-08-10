/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.geo.datasource;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.Constants;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

// TODO: Auto-generated Javadoc
/**
 * The Class DataSource.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class DataSource {
	
	/** The jndi name. */
	private String jndiName;	 
	
	/** The driver. */
	private String driver;  
    
    /** The password. */
    private String password;
    
    /** The url. */
    private String url;
    
    /** The user. */
    private String user;
    
    /** The label. */
    private String label;


    /**
     * Instantiates a new data source.
     * 
     * @param spagoBIDataSource the spago bi data source
     */
    public DataSource(SpagoBiDataSource spagoBIDataSource) {
    	this.driver = spagoBIDataSource.getDriver();
        this.jndiName = spagoBIDataSource.getJndiName();
        this.password = spagoBIDataSource.getPassword();
        this.url = spagoBIDataSource.getUrl();
        this.user = spagoBIDataSource.getUser();
        this.label = spagoBIDataSource.getLabel();
    }
    
    /**
     * Instantiates a new data source.
     * 
     * @param driver the driver
     * @param jndiName the jndi name
     * @param password the password
     * @param url the url
     * @param user the user
     * @param label the label
     */
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


    /**
     * Instantiates a new data source.
     * 
     * @param dataSourceSB the data source sb
     */
    public DataSource(SourceBean dataSourceSB) {
    	
		String type = (String)dataSourceSB.getAttribute(Constants.DATASET_TYPE_ATTRIBUTE);				
		if("connection".equalsIgnoreCase(type)) {
			jndiName = (String)dataSourceSB.getAttribute(Constants.DATASET_NAME_ATTRIBUTE);
			driver = (String)dataSourceSB.getAttribute(Constants.DATASET_DRIVER_ATTRIBUTER);
			password = (String)dataSourceSB.getAttribute(Constants.DATASET_PWD_ATTRIBUTE);
			user = (String)dataSourceSB.getAttribute(Constants.DATASET_USER_ATTRIBUTE);
			url = (String)dataSourceSB.getAttribute(Constants.DATASET_URL_ATTRIBUTE);
		}
	}

	/**
	 * Check is jndi.
	 * 
	 * @return true, if successful
	 */
	public boolean checkIsJndi() {
    	return getJndiName() != null 
    			&& getJndiName().equals("") == false;
    }
    
    /**
     * Gets the connection.
     * 
     * @return the connection
     * 
     * @throws NamingException the naming exception
     * @throws SQLException the SQL exception
     * @throws ClassNotFoundException the class not found exception
     */
    public Connection getConnection() throws NamingException, SQLException, ClassNotFoundException {
    	Connection connection = null;
    	 
    	if( checkIsJndi() ) {
    		connection = getJndiConnection();
    	} else {    		
    		connection = getDirectConnection();
    	}
    	
    	return connection;
    }
    
    
    /**
     * Get the connection from JNDI.
     * 
     * @return Connection to database
     * 
     * @throws NamingException the naming exception
     * @throws SQLException the SQL exception
     */
    private Connection getJndiConnection() throws NamingException, SQLException {
		Connection connection = null;
		
		Context ctx;
		ctx = new InitialContext();
		javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup( getJndiName() );
		connection = ds.getConnection();
		
		return connection;
    }

    /**
     * Get the connection using jdbc.
     * 
     * @return Connection to database
     * 
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException the SQL exception
     */
    private Connection getDirectConnection() throws ClassNotFoundException, SQLException {
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
     * @param driver the driver
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
     * @param jndiName the jndi name
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
     * @param password the password
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
     * @param url the url
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
     * @param user the user
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
     * @param label the label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	String str = "";
    	
    	str += "[";
    	str += "jndiName:" + jndiName + "; ";
    	str += "driver:" + driver + "; ";
    	str += "url:" + url + "; ";
    	str += "password:" + password + "; ";
    	str += "user:" + user + "; ";
    	str += "label:" + label;
    	str += "]";
    	
    	return str;
    }
    
}

