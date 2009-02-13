/**
 * WSSessionServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.session.stub;

import it.eng.spagobi.services.session.service.SessionServiceImpl;

public class WSSessionServiceSoapBindingImpl implements it.eng.spagobi.services.session.stub.SessionService{
    public void openSession(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.AuthenticationException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	ssImpl.openSession(in0, in1);
    }

    public void openSessionWithToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.AuthenticationException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	ssImpl.openSessionWithToken(in0, in1);
    }

    public void closeSession() throws java.rmi.RemoteException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	ssImpl.closeSession();
    }

}
