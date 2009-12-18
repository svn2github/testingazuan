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

    public boolean isValidSession() throws java.rmi.RemoteException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	return ssImpl.isValidSession();
    }

    public void closeSession() throws java.rmi.RemoteException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	ssImpl.closeSession();
    }

    public it.eng.spagobi.services.session.bo.Document[] getDocuments(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	return ssImpl.getDocuments(in0, in1, in2);
    }

    public java.lang.String[] getCorrectRolesForExecution(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	return ssImpl.getCorrectRolesForExecution(in0);
    }

    public it.eng.spagobi.services.session.bo.DocumentParameter[] getDocumentParameters(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	return ssImpl.getDocumentParameters(in0, in1);
    }

    public java.util.HashMap getAdmissibleValues(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException {
    	SessionServiceImpl ssImpl = new SessionServiceImpl();
    	return ssImpl.getAdmissibleValues(in0, in1);
    }

}
