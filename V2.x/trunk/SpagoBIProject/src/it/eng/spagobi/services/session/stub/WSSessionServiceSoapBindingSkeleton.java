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
        _oper = new org.apache.axis.description.OperationDesc("closeSession", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiwssession", "closeSession"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("closeSession") == null) {
            _myOperations.put("closeSession", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("closeSession")).add(_oper);
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

    public void closeSession() throws java.rmi.RemoteException
    {
        impl.closeSession();
    }

}
