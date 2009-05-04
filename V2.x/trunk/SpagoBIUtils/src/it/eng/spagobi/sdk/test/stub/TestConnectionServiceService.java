/**
 * TestConnectionServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.test.stub;

public interface TestConnectionServiceService extends javax.xml.rpc.Service {
    public java.lang.String getTestConnectionServiceAddress();

    public it.eng.spagobi.sdk.test.stub.TestConnectionService getTestConnectionService() throws javax.xml.rpc.ServiceException;

    public it.eng.spagobi.sdk.test.stub.TestConnectionService getTestConnectionService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
