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
package it.eng.spagobi.tools.dataset.common;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

/**
 * interfaccia utilizzata dai componenti che utilizzano il Data set
 * 
 *    operazioni da fare :
 *    		1) crare un oggetto con una factory
 *    		2) prendere i dati con loadData() / getDataStore()
 * 
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataSetProxy {

    
    
    IDataSet getDataSet(String dsName);
    IDataSet getRemoteDataSet(String dataSetLabel,String user,HttpSession session);

}
