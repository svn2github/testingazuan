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
package it.eng.spago.dbaccess.pool;


import it.eng.spago.base.Constants;
import it.eng.spago.dbaccess.ConnectionPoolDescriptor;
import it.eng.spago.dbaccess.pool.ConnectionPoolInterface;
import it.eng.spago.dbaccess.pool.DecriptAlgorithmFactory;
import it.eng.spago.dbaccess.pool.IDecriptAlgorithm;
import it.eng.spago.dbaccess.pool.NativePoolWrapper;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.tracing.TracerSingleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.sql.DataSource;


public class HSQLConnectionPoolFactory {

    public ConnectionPoolInterface createConnectionPool(ConnectionPoolDescriptor connectionPoolDescriptor)
        throws EMFInternalError {
            try {
            	          
                // ottengo la connection string         
                String connectionString = connectionPoolDescriptor.getConnectionPoolParameter("connectionString").getValue();
                // ottengo lo user name
                String userNameParameter = connectionPoolDescriptor.getConnectionPoolParameter("user").getValue();
                // ottengo la password
                String poolName=connectionPoolDescriptor.getConnectionPoolName();
                IDecriptAlgorithm decipherAlgoritm=DecriptAlgorithmFactory.create(poolName);
				String hiddenPassword=connectionPoolDescriptor.getConnectionPoolParameter("userPassword").getValue();
				String userPasswordParameter =null;
                 if(decipherAlgoritm!=null )
                   {userPasswordParameter = decipherAlgoritm.decipher(hiddenPassword); }
                 else { userPasswordParameter =hiddenPassword; }
                // ottengo l'oggetto Class del datasource di HSQL
                Class ocpdsClass = Class.forName("org.hsqldb.jdbc.jdbcDataSource");
                Object ocpdsObject = ocpdsClass.newInstance();
                //
                // Setting URL
                //
                Method method = null;
                Class[] parameterTypesOfMethod = new Class[] {
                    String.class
                };
                Object[] argumentsOfMethod = new Object[] {
                	connectionString
                };
                method = ocpdsClass.getMethod("setDatabase", parameterTypesOfMethod);
                method.invoke(ocpdsObject, argumentsOfMethod);
                //
                // Setting USER
                //
                method = null;
                parameterTypesOfMethod = new Class[] {
                    String.class
                };
                argumentsOfMethod = new Object[] {
                    userNameParameter
                };
                method = ocpdsClass.getMethod("setUser", parameterTypesOfMethod);
                method.invoke(ocpdsObject, argumentsOfMethod);
                //
                // Setting PASSWORD
                //
                method = null;
                parameterTypesOfMethod = new Class[] {
                    String.class
                };
                argumentsOfMethod = new Object[] {
                    userPasswordParameter
                };
                method = ocpdsClass.getMethod("setPassword", parameterTypesOfMethod);
                method.invoke(ocpdsObject, argumentsOfMethod);
                return new NativePoolWrapper((DataSource)ocpdsObject);
            }
            catch (InvocationTargetException ite) {
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                    "HSQLConnectionPoolFactory::createConnectionPool:", ite);
                throw new EMFInternalError(EMFErrorSeverity.ERROR,
                    "HSQLConnectionPoolFactory::createConnectionPoolDataSource::InvocationTargetException" +
                    ite.getMessage());
            }
            catch (IllegalAccessException iae) {
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                    "HSQLConnectionPoolFactory::createConnectionPool:", iae);
                throw new EMFInternalError(EMFErrorSeverity.ERROR,
                    "HSQLConnectionPoolFactory::createConnectionPoolDataSource::IllegalAccessExceptionTarget" +
                    iae.getMessage());
            }
            catch (InstantiationException ie) {
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                    "HSQLConnectionPoolFactory::createConnectionPool:", ie);
                throw new EMFInternalError(EMFErrorSeverity.ERROR,
                    "HSQLConnectionPoolFactory::createConnectionPoolDataSource::InstantiationException" +
                    ie.getMessage());
            }
            catch (NoSuchMethodException ie) {
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                    "HSQLConnectionPoolFactory::createConnectionPool:", ie);
                throw new EMFInternalError(EMFErrorSeverity.ERROR,
                    "HSQLConnectionPoolFactory::createConnectionPoolDataSource::NoSuchMethodException" +
                    ie.getMessage());
            }
            catch (Exception e) {
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                    "HSQLConnectionPoolFactory::createConnectionPool:", e);
                throw new EMFInternalError(EMFErrorSeverity.ERROR,
                    "HSQLConnectionPoolFactory::createConnectionPoolDataSource::Exception" +
                    e.getMessage());
            }
    } 
} 
