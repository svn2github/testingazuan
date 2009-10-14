/**
 * DocumentsServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.documents.stub;

import it.eng.spagobi.sdk.datasets.impl.DataSetsSDKServiceImpl;
import it.eng.spagobi.sdk.documents.impl.DocumentsServiceImpl;

public class DocumentsServiceSoapBindingImpl implements it.eng.spagobi.sdk.documents.stub.DocumentsService{
	public it.eng.spagobi.sdk.documents.bo.SDKDocument[] getDocumentsAsList(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.getDocumentsAsList(in0, in1, in2);    
	}

	public it.eng.spagobi.sdk.documents.bo.SDKFunctionality getDocumentsAsTree(java.lang.String in0) throws java.rmi.RemoteException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.getDocumentsAsTree(in0);
	}

	public java.lang.String[] getCorrectRolesForExecution(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.getCorrectRolesForExecution(in0);
	}

	public it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter[] getDocumentParameters(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.getDocumentParameters(in0,in1);
	}

	public java.util.HashMap getAdmissibleValues(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.getAdmissibleValues(in0,in1);
	}

	public it.eng.spagobi.sdk.documents.bo.SDKTemplate downloadTemplate(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.downloadTemplate(in0);
	}

	public void uploadTemplate(java.lang.Integer in0, it.eng.spagobi.sdk.documents.bo.SDKTemplate in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return;
	}

	public java.lang.Integer saveNewDocument(it.eng.spagobi.sdk.documents.bo.SDKDocument in0, it.eng.spagobi.sdk.documents.bo.SDKTemplate in1, java.lang.Integer in2) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.saveNewDocument(in0, in1, in2);
	}

	public it.eng.spagobi.sdk.documents.bo.SDKExecutedDocumentContent executeDocument(it.eng.spagobi.sdk.documents.bo.SDKDocument in0, it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter[] in1, java.lang.String in2) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
		DocumentsServiceImpl impl = new DocumentsServiceImpl();
		return impl.executeDocument(in0, in1,in2);
	}

}
