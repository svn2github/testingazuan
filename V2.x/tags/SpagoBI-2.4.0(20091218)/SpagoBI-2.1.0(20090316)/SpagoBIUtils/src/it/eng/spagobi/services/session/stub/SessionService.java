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
    public boolean isValidSession() throws java.rmi.RemoteException;
    public void closeSession() throws java.rmi.RemoteException;
    public it.eng.spagobi.services.session.bo.Document[] getDocuments(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String[] getCorrectRolesForExecution(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException;
    public it.eng.spagobi.services.session.bo.DocumentParameter[] getDocumentParameters(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException;
    public java.util.HashMap getAdmissibleValues(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException;
}
