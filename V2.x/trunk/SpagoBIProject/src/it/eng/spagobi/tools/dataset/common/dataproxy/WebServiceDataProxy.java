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
package it.eng.spagobi.tools.dataset.common.dataproxy;

import java.io.FileInputStream;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.services.content.stub.ContentServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.tools.dataset.wsconnectors.stub.IWsConnector;
import it.eng.spagobi.tools.dataset.wsconnectors.stub.IWsConnectorServiceLocator;
import it.eng.spagobi.tools.dataset.wsconnectors.stub.WSDataSetServiceSoapBindingImpl;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class WebServiceDataProxy implements IDataProxy {

	String address;
	String operation;
	String executorClass;

	private static transient Logger logger = Logger.getLogger(WebServiceDataProxy.class);
	public static final String messageBundle = "web_service_dataset";


	public WebServiceDataProxy() {

	}

	public WebServiceDataProxy(String address, String operation, String executorClass) {
		this.setAddress(address);
		this.setOperation(operation);
		this.setExecutorClass(executorClass);
	}

	public Object load(String statement) throws EMFUserError {
		throw new UnsupportedOperationException("metothd load not yet implemented");
	}

	public Object load() throws EMFUserError {
		String resultXML="";
		IWsConnectorServiceLocator locator = new IWsConnectorServiceLocator();   
		IWsConnector connector=null;
		URL addressToCall=null;
		
		
		try{
			addressToCall=new URL(address);
		}
		catch (Exception e) {
			logger.error("URL non valid.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "11100", messageBundle);
		}
		try {
			connector = locator.getWSDataSetService(addressToCall);

		} catch (ServiceException e) {
			logger.error("Service not avalaible");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "11200", messageBundle);
		}	
		
		try {
			resultXML=connector.readDataSet(address, null, operation);
		} catch (RemoteException e) {
			logger.error("Service not avalaible");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "11200", messageBundle);
		}		

		return resultXML;
		
	}

/*
	private IWsConnector lookUp() throws SecurityException {
		try {
			IWsConnectorServiceLocator locator = new IWsConnectorServiceLocator();   
			IWsConnector connector=null;
			URL addressToCall=null;
			try{
				addressToCall=new URL(address);
			}
			catch (Exception e) {
				//TODO
			}

			connector = locator.getWSDataSetService(addressToCall);	

			return connector;
		} catch (ServiceException e) {
			logger.error("Error during service execution", e);
			throw new SecurityException();
		}
	}*/



	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getExecutorClass() {
		return executorClass;
	}

	public void setExecutorClass(String executorClass) {
		this.executorClass = executorClass;
	}
}
