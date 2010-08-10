/**
 * WSSessionServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.session.stub;

public class WSSessionServiceSoapBindingSkeleton implements it.eng.spagobi.services.session.stub.SessionService, org.apache.axis.wsdl.Skeleton {
    private it.eng.spagobi.services.session.stub.SessionService impl;
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
        };
        _oper = new org.apache.axis.description.OperationDesc("openSession", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "openSession"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("openSession") == null) {
            _myOperations.put("openSession", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("openSession")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("AuthenticationException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobiwssession", "fault"));
        _fault.setClassName("it.eng.spagobi.services.session.exceptions.AuthenticationException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.session.services.spagobi.eng.it", "AuthenticationException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("openSessionWithToken", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "openSessionWithToken"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("openSessionWithToken") == null) {
            _myOperations.put("openSessionWithToken", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("openSessionWithToken")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("AuthenticationException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobiwssession", "fault"));
        _fault.setClassName("it.eng.spagobi.services.session.exceptions.AuthenticationException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.session.services.spagobi.eng.it", "AuthenticationException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("isValidSession", _params, new javax.xml.namespace.QName("", "isValidSessionReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "isValidSession"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("isValidSession") == null) {
            _myOperations.put("isValidSession", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("isValidSession")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("closeSession", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "closeSession"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("closeSession") == null) {
            _myOperations.put("closeSession", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("closeSession")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDocuments", _params, new javax.xml.namespace.QName("", "getDocumentsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobiwssession", "ArrayOf_tns3_Document"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "getDocuments"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDocuments") == null) {
            _myOperations.put("getDocuments", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDocuments")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getCorrectRolesForExecution", _params, new javax.xml.namespace.QName("", "getCorrectRolesForExecutionReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobiwssession", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "getCorrectRolesForExecution"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getCorrectRolesForExecution") == null) {
            _myOperations.put("getCorrectRolesForExecution", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getCorrectRolesForExecution")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NonExecutableDocumentException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobiwssession", "fault"));
        _fault.setClassName("it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.session.services.spagobi.eng.it", "NonExecutableDocumentException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDocumentParameters", _params, new javax.xml.namespace.QName("", "getDocumentParametersReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobiwssession", "ArrayOf_tns3_DocumentParameter"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "getDocumentParameters"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDocumentParameters") == null) {
            _myOperations.put("getDocumentParameters", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDocumentParameters")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NonExecutableDocumentException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobiwssession", "fault"));
        _fault.setClassName("it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.session.services.spagobi.eng.it", "NonExecutableDocumentException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAdmissibleValues", _params, new javax.xml.namespace.QName("", "getAdmissibleValuesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "getAdmissibleValues"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAdmissibleValues") == null) {
            _myOperations.put("getAdmissibleValues", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAdmissibleValues")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NonExecutableDocumentException");
        _fault.setQName(new javax.xml.namespace.QName("urn:spagobiwssession", "fault"));
        _fault.setClassName("it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://exceptions.session.services.spagobi.eng.it", "NonExecutableDocumentException"));
        _oper.addFault(_fault);
    }

    public WSSessionServiceSoapBindingSkeleton() {
        this.impl = new it.eng.spagobi.services.session.stub.WSSessionServiceSoapBindingImpl();
    }

    public WSSessionServiceSoapBindingSkeleton(it.eng.spagobi.services.session.stub.SessionService impl) {
        this.impl = impl;
    }
    public void openSession(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.AuthenticationException
    {
        impl.openSession(in0, in1);
    }

    public void openSessionWithToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.AuthenticationException
    {
        impl.openSessionWithToken(in0, in1);
    }

    public boolean isValidSession() throws java.rmi.RemoteException
    {
        boolean ret = impl.isValidSession();
        return ret;
    }

    public void closeSession() throws java.rmi.RemoteException
    {
        impl.closeSession();
    }

    public it.eng.spagobi.services.session.bo.Document[] getDocuments(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        it.eng.spagobi.services.session.bo.Document[] ret = impl.getDocuments(in0, in1, in2);
        return ret;
    }

    public java.lang.String[] getCorrectRolesForExecution(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException
    {
        java.lang.String[] ret = impl.getCorrectRolesForExecution(in0);
        return ret;
    }

    public it.eng.spagobi.services.session.bo.DocumentParameter[] getDocumentParameters(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException
    {
        it.eng.spagobi.services.session.bo.DocumentParameter[] ret = impl.getDocumentParameters(in0, in1);
        return ret;
    }

    public java.util.HashMap getAdmissibleValues(java.lang.Integer in0, java.lang.String in1) throws java.rmi.RemoteException, it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException
    {
        java.util.HashMap ret = impl.getAdmissibleValues(in0, in1);
        return ret;
    }

}
