/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.security.IEngUserProfile;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetProxyFactory implements IDataSetProxyFactory {


    public IDataSetProxy crateDataSetProxy(IEngUserProfile profile, String dataSetLabel, HashMap parameters) {

	return new DataSetProxyImpl(profile,dataSetLabel,parameters);
    }

}
