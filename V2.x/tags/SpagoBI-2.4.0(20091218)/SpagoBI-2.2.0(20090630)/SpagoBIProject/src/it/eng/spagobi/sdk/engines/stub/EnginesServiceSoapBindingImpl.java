/**
 * EnginesServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.engines.stub;

import it.eng.spagobi.sdk.engines.impl.EnginesServiceImpl;

public class EnginesServiceSoapBindingImpl implements it.eng.spagobi.sdk.engines.stub.EnginesService{
    public it.eng.spagobi.sdk.engines.bo.SDKEngine[] getEngines() throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
        EnginesServiceImpl impl = new EnginesServiceImpl();
    	return impl.getEngines();
    }

    public it.eng.spagobi.sdk.engines.bo.SDKEngine getEngine(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
        EnginesServiceImpl impl = new EnginesServiceImpl();
    	return impl.getEngine(in0);
    }

}
