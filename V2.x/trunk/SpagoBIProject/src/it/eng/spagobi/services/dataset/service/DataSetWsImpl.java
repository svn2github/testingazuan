/**
 * 
 */
package it.eng.spagobi.services.dataset.service;

import it.eng.spagobi.services.dataset.stub.DataSetWsInterface;

import java.rmi.RemoteException;
import java.util.HashMap;



/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetWsImpl implements DataSetWsInterface {


    /**
     * 
     */
    public String readData(HashMap parameters, String operation) throws RemoteException {

	return "<ROWS><ROW name=\"io\" value=\"5\"/><ROW name=\"tu\" value=\"3\"/><ROW name=\"egli\" value=\"3\"/><ROW name=\"noi\" value=\"1\"/><ROW name=\"voi\" value=\"2\"/><ROW name=\"essi\" value=\"7\"/></ROWS>";
    }

}
