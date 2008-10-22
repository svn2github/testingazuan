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
package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.dataset.stub.DataSetWsInterfaceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.tools.dataset.bo.DataSet;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * 
 * Proxy of Data Set Service
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it 
 * 
 */
public final class DataSetWsServiceProxy extends AbstractServiceProxy{

    static private Logger logger = Logger.getLogger(DataSetWsServiceProxy.class);

    private URL url = null;

    /**
     * The Constructor.
     * 
     * @param adress the adress
     */
    public DataSetWsServiceProxy(String adress) {
	try {
	    this.url = new URL(adress);
	} catch (Exception e) {
	    logger.error("Service Adress is incorrect", e);
	}
    }

    private DataSetWsServiceProxy() {
    }

    private it.eng.spagobi.services.dataset.stub.DataSetWsInterface lookUp() throws SecurityException {
	try {
	    DataSetWsInterfaceServiceLocator locator = new DataSetWsInterfaceServiceLocator();
	    it.eng.spagobi.services.dataset.stub.DataSetWsInterface service = null;
	    service = locator.getDataSetService(url);
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }

    /**
     * Read data.
     * 
     * @param parameters HashMap input par.
     * @param operation String operation
     * 
     * @return String data
     */
    public DataSet getDataSetByLabel(String label) {
	logger.debug("IN");
	try {
	    return DataSet.createDataSet(lookUp().getDataSetByLabel(readTicket(), userId,label));
	} catch (Exception e) {
	    logger.error("Error during service execution", e);

	} finally {
	    logger.debug("IN");
	}
	return null;
    }
}
