/**
 * DataSetServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.dataset.stub;

public class DataSetServiceSoapBindingSkeleton implements it.eng.spagobi.services.dataset.stub.DataSetWsInterface, org.apache.axis.wsdl.Skeleton {
    private it.eng.spagobi.services.dataset.stub.DataSetWsInterface impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("readData", _params, new javax.xml.namespace.QName("", "readDataReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobidataset", "readData"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("readData") == null) {
            _myOperations.put("readData", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("readData")).add(_oper);
    }

    public DataSetServiceSoapBindingSkeleton() {
        this.impl = new it.eng.spagobi.services.dataset.stub.DataSetServiceSoapBindingImpl();
    }

    public DataSetServiceSoapBindingSkeleton(it.eng.spagobi.services.dataset.stub.DataSetWsInterface impl) {
        this.impl = impl;
    }
    public java.lang.String readData(java.util.HashMap in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.readData(in0, in1);
        return ret;
    }

}
