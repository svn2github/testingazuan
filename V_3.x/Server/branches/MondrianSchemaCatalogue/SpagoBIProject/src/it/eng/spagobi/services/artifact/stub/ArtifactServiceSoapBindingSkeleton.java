/**
 * ArtifactServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.artifact.stub;

public class ArtifactServiceSoapBindingSkeleton implements it.eng.spagobi.services.artifact.stub.ArtifactService, org.apache.axis.wsdl.Skeleton {
    private it.eng.spagobi.services.artifact.stub.ArtifactService impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getArtifactContentByNameAndType", _params, new javax.xml.namespace.QName("", "getArtifactContentByNameAndTypeReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiartifact", "getArtifactContentByNameAndType"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getArtifactContentByNameAndType") == null) {
            _myOperations.put("getArtifactContentByNameAndType", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getArtifactContentByNameAndType")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getArtifactContentById", _params, new javax.xml.namespace.QName("", "getArtifactContentByIdReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobiartifact", "getArtifactContentById"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getArtifactContentById") == null) {
            _myOperations.put("getArtifactContentById", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getArtifactContentById")).add(_oper);
    }

    public ArtifactServiceSoapBindingSkeleton() {
        this.impl = new it.eng.spagobi.services.artifact.stub.ArtifactServiceSoapBindingImpl();
    }

    public ArtifactServiceSoapBindingSkeleton(it.eng.spagobi.services.artifact.stub.ArtifactService impl) {
        this.impl = impl;
    }
    public java.lang.String getArtifactContentByNameAndType(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getArtifactContentByNameAndType(in0, in1, in2, in3);
        return ret;
    }

    public java.lang.String getArtifactContentById(java.lang.String in0, java.lang.String in1, java.lang.Integer in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getArtifactContentById(in0, in1, in2);
        return ret;
    }

}
