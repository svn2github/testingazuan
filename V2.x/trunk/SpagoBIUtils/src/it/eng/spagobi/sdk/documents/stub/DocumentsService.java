/**
 * DocumentsService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.documents.stub;

public interface DocumentsService extends java.rmi.Remote {
    public it.eng.spagobi.sdk.documents.bo.Document[] getDocumentsAsList(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public it.eng.spagobi.sdk.documents.bo.Functionality[] getDocumentsAsTree(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String[] getCorrectRolesForExecution(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException;
    public it.eng.spagobi.sdk.documents.bo.DocumentParameter[] getDocumentParameters(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException;
    public java.util.HashMap getAdmissibleValues(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException;
    public it.eng.spagobi.sdk.documents.bo.Template downloadTemplate(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
    public void uploadTemplate(java.lang.Integer in0, it.eng.spagobi.sdk.documents.bo.Template in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
    public java.lang.Integer saveNewDocument(it.eng.spagobi.sdk.documents.bo.Document in0, it.eng.spagobi.sdk.documents.bo.Template in1, java.lang.Integer in2) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
}
