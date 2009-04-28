/**
 * DocumentsServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.documents.stub;

public class DocumentsServiceSoapBindingSkeleton implements it.eng.spagobi.sdk.documents.stub.DocumentsService, org.apache.axis.wsdl.Skeleton {
    private it.eng.spagobi.sdk.documents.stub.DocumentsService impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDocumentsAsList", _params, new javax.xml.namespace.QName("", "getDocumentsAsListReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "ArrayOf_tns2_Document"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "getDocumentsAsList"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDocumentsAsList") == null) {
            _myOperations.put("getDocumentsAsList", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDocumentsAsList")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDocumentsAsTree", _params, new javax.xml.namespace.QName("", "getDocumentsAsTreeReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://bo.documents.sdk.spagobi.eng.it", "Functionality"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "getDocumentsAsTree"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDocumentsAsTree") == null) {
            _myOperations.put("getDocumentsAsTree", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDocumentsAsTree")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getCorrectRolesForExecution", _params, new javax.xml.namespace.QName("", "getCorrectRolesForExecutionReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "getCorrectRolesForExecution"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getCorrectRolesForExecution") == null) {
            _myOperations.put("getCorrectRolesForExecution", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getCorrectRolesForExecution")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NonExecutableDocumentException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "fault"));
        _fault.setClassName("it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.sdk.spagobi.eng.it", "NonExecutableDocumentException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDocumentParameters", _params, new javax.xml.namespace.QName("", "getDocumentParametersReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "ArrayOf_tns2_DocumentParameter"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "getDocumentParameters"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDocumentParameters") == null) {
            _myOperations.put("getDocumentParameters", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDocumentParameters")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NonExecutableDocumentException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "fault"));
        _fault.setClassName("it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.sdk.spagobi.eng.it", "NonExecutableDocumentException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAdmissibleValues", _params, new javax.xml.namespace.QName("", "getAdmissibleValuesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "getAdmissibleValues"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAdmissibleValues") == null) {
            _myOperations.put("getAdmissibleValues", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAdmissibleValues")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NonExecutableDocumentException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "fault"));
        _fault.setClassName("it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.sdk.spagobi.eng.it", "NonExecutableDocumentException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("downloadTemplate", _params, new javax.xml.namespace.QName("", "downloadTemplateReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://bo.documents.sdk.spagobi.eng.it", "Template"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "downloadTemplate"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("downloadTemplate") == null) {
            _myOperations.put("downloadTemplate", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("downloadTemplate")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NotAllowedOperationException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "fault"));
        _fault.setClassName("it.eng.spagobi.sdk.exceptions.NotAllowedOperationException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.sdk.spagobi.eng.it", "NotAllowedOperationException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://bo.documents.sdk.spagobi.eng.it", "Template"), it.eng.spagobi.sdk.documents.bo.Template.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("uploadTemplate", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "uploadTemplate"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("uploadTemplate") == null) {
            _myOperations.put("uploadTemplate", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("uploadTemplate")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NotAllowedOperationException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "fault"));
        _fault.setClassName("it.eng.spagobi.sdk.exceptions.NotAllowedOperationException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.sdk.spagobi.eng.it", "NotAllowedOperationException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://bo.documents.sdk.spagobi.eng.it", "Document"), it.eng.spagobi.sdk.documents.bo.Document.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://bo.documents.sdk.spagobi.eng.it", "Template"), it.eng.spagobi.sdk.documents.bo.Template.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("saveNewDocument", _params, new javax.xml.namespace.QName("", "saveNewDocumentReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "saveNewDocument"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("saveNewDocument") == null) {
            _myOperations.put("saveNewDocument", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("saveNewDocument")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NotAllowedOperationException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobisdkdocuments", "fault"));
        _fault.setClassName("it.eng.spagobi.sdk.exceptions.NotAllowedOperationException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.sdk.spagobi.eng.it", "NotAllowedOperationException"));
        _oper.addFault(_fault);
    }

    public DocumentsServiceSoapBindingSkeleton() {
        this.impl = new it.eng.spagobi.sdk.documents.stub.DocumentsServiceSoapBindingImpl();
    }

    public DocumentsServiceSoapBindingSkeleton(it.eng.spagobi.sdk.documents.stub.DocumentsService impl) {
        this.impl = impl;
    }
    public it.eng.spagobi.sdk.documents.bo.Document[] getDocumentsAsList(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        it.eng.spagobi.sdk.documents.bo.Document[] ret = impl.getDocumentsAsList(in0, in1, in2);
        return ret;
    }

    public it.eng.spagobi.sdk.documents.bo.Functionality getDocumentsAsTree(java.lang.String in0) throws java.rmi.RemoteException
    {
        it.eng.spagobi.sdk.documents.bo.Functionality ret = impl.getDocumentsAsTree(in0);
        return ret;
    }

    public java.lang.String[] getCorrectRolesForExecution(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException
    {
        java.lang.String[] ret = impl.getCorrectRolesForExecution(in0);
        return ret;
    }

    public it.eng.spagobi.sdk.documents.bo.DocumentParameter[] getDocumentParameters(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException
    {
        it.eng.spagobi.sdk.documents.bo.DocumentParameter[] ret = impl.getDocumentParameters(in0, in1);
        return ret;
    }

    public java.util.HashMap getAdmissibleValues(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException
    {
        java.util.HashMap ret = impl.getAdmissibleValues(in0, in1);
        return ret;
    }

    public it.eng.spagobi.sdk.documents.bo.Template downloadTemplate(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException
    {
        it.eng.spagobi.sdk.documents.bo.Template ret = impl.downloadTemplate(in0);
        return ret;
    }

    public void uploadTemplate(java.lang.Integer in0, it.eng.spagobi.sdk.documents.bo.Template in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException
    {
        impl.uploadTemplate(in0, in1);
    }

    public java.lang.Integer saveNewDocument(it.eng.spagobi.sdk.documents.bo.Document in0, it.eng.spagobi.sdk.documents.bo.Template in1, java.lang.Integer in2) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException
    {
        java.lang.Integer ret = impl.saveNewDocument(in0, in1, in2);
        return ret;
    }

}
