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

    
    public IDataSetProxy crateLocalDataSetProxy(IEngUserProfile profile) {

    	return new DataSetProxyImpl(profile);
    }

	public IDataSetProxy crateDataSetProxy(IEngUserProfile profile) {
		// TODO Auto-generated method stub
		return new DataSetProxyImpl(profile);
	}
    

}
