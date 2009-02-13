/**
 * SessionService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.session.stub;

public interface SessionService extends java.rmi.Remote {
    public void openSession(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.AuthenticationException;
    public void openSessionWithToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.AuthenticationException;
    public void closeSession() throws java.rmi.RemoteException;
}
