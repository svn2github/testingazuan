/**
 * SessionServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.session.stub;

public interface SessionServiceService extends javax.xml.rpc.Service {
    public java.lang.String getWSSessionServiceAddress();

    public it.eng.spagobi.services.session.stub.SessionService getWSSessionService() throws javax.xml.rpc.ServiceException;

    public it.eng.spagobi.services.session.stub.SessionService getWSSessionService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
