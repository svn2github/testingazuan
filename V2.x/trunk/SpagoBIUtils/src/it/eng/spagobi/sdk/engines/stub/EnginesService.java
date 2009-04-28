/**
 * EnginesService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.engines.stub;

public interface EnginesService extends java.rmi.Remote {
    public it.eng.spagobi.sdk.engines.bo.SDKEngine[] getEngines() throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
    public it.eng.spagobi.sdk.engines.bo.SDKEngine getEngine(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
}
