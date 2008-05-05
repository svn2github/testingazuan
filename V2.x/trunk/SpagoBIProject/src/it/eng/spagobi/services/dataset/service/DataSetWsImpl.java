/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.services.dataset.service;

import it.eng.spagobi.services.dataset.stub.DataSetWsInterface;

import java.rmi.RemoteException;
import java.util.HashMap;



/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetWsImpl implements DataSetWsInterface {


    /* (non-Javadoc)
     * @see it.eng.spagobi.services.dataset.stub.DataSetWsInterface#readData(java.util.HashMap, java.lang.String)
     */
    public String readData(HashMap parameters, String operation) throws RemoteException {

	return "<ROWS><ROW name=\"io\" value=\"5\"/><ROW name=\"tu\" value=\"3\"/><ROW name=\"egli\" value=\"3\"/><ROW name=\"noi\" value=\"1\"/><ROW name=\"voi\" value=\"2\"/><ROW name=\"essi\" value=\"7\"/></ROWS>";
    }

}
