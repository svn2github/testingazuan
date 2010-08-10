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

/**
 * Created on 22-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spago.dbaccess.pool;

import it.eng.spago.dbaccess.ConnectionPoolDescriptor;
import it.eng.spago.dbaccess.pool.ConnectionPoolInterface;
import it.eng.spago.dbaccess.pool.DecriptAlgorithmFactory;
import it.eng.spago.dbaccess.pool.IDecriptAlgorithm;
import it.eng.spago.dbaccess.pool.NativePoolWrapper;
import it.eng.spago.error.EMFInternalError;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.xerces.parsers.IntegratedParserConfiguration;

/**
 * @author Zoppello
 *
 * Uses dbcp to implement a connection pool
 */
public class DBCPConnectionPoolFactory {
	/**
	 * Creates the connection pool
	 * 
	 * @param connectionPoolDescriptior The descriptor taken at input
	 * @return The Spago's connection pool interface
	 * 
	 */
	private ConnectionPoolDescriptor _connectionPoolDescriptor = null;

	  public ConnectionPoolInterface createConnectionPool(ConnectionPoolDescriptor connectionPoolDescriptor)
      throws EMFInternalError {
	  	String connectionStringParameter = connectionPoolDescriptor.getConnectionPoolParameter("connectionString").getValue();
        String userNameParameter = connectionPoolDescriptor.getConnectionPoolParameter("user").getValue();
        
        /**
         * Eventually decode password
         */
         String poolName=connectionPoolDescriptor.getConnectionPoolName();
         // ottengo il riferimento alla classe che implementa l'algoritmo
         IDecriptAlgorithm decipherAlgoritm=DecriptAlgorithmFactory.create(poolName);
		String hiddenPassword=connectionPoolDescriptor.getConnectionPoolParameter("userPassword").getValue();
		String userPasswordParameter =null;
         if (decipherAlgoritm!=null ){
			userPasswordParameter = decipherAlgoritm.decipher(hiddenPassword);	                 	
         }else {
			userPasswordParameter = hiddenPassword;
         }
        String driverClass = connectionPoolDescriptor.getConnectionPoolParameter("driverClass").getValue();
	  	BasicDataSource ds = new BasicDataSource();
	    ds.setDriverClassName(driverClass);
	    ds.setUsername(userNameParameter);
	  
	    ds.setPassword(userPasswordParameter);
	    ds.setUrl(connectionStringParameter);

        return new NativePoolWrapper((DataSource)ds);
	  }
  
}
