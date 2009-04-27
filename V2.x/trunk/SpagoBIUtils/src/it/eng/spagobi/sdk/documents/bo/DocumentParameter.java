/**
 * DocumentParameter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.documents.bo;

public class DocumentParameter  implements java.io.Serializable {
    private it.eng.spagobi.sdk.documents.bo.Constraint[] constraints;

    private java.lang.Integer id;

    private java.lang.String label;

    private java.lang.String urlName;

    public DocumentParameter() {
    }

    public DocumentParameter(
           it.eng.spagobi.sdk.documents.bo.Constraint[] constraints,
           java.lang.Integer id,
           java.lang.String label,
           java.lang.String urlName) {
           this.constraints = constraints;
           this.id = id;
           this.label = label;
           this.urlName = urlName;
    }


    /**
     * Gets the constraints value for this DocumentParameter.
     * 
     * @return constraints
     */
    public it.eng.spagobi.sdk.documents.bo.Constraint[] getConstraints() {
        return constraints;
    }


    /**
     * Sets the constraints value for this DocumentParameter.
     * 
     * @param constraints
     */
    public void setConstraints(it.eng.spagobi.sdk.documents.bo.Constraint[] constraints) {
        this.constraints = constraints;
    }


    /**
     * Gets the id value for this DocumentParameter.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this DocumentParameter.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the label value for this DocumentParameter.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this DocumentParameter.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the urlName value for this DocumentParameter.
     * 
     * @return urlName
     */
    public java.lang.String getUrlName() {
        return urlName;
    }


    /**
     * Sets the urlName value for this DocumentParameter.
     * 
     * @param urlName
     */
    public void setUrlName(java.lang.String urlName) {
        this.urlName = urlName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentParameter)) return false;
        DocumentParameter other = (DocumentParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.constraints==null && other.getConstraints()==null) || 
             (this.constraints!=null &&
              java.util.Arrays.equals(this.constraints, other.getConstraints()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.urlName==null && other.getUrlName()==null) || 
             (this.urlName!=null &&
              this.urlName.equals(other.getUrlName())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getConstraints() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getConstraints());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConstraints(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getUrlName() != null) {
            _hashCode += getUrlName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://bo.documents.sdk.spagobi.eng.it", "DocumentParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("constraints");
        elemField.setXmlName(new javax.xml.namespace.QName("", "constraints"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://bo.documents.sdk.spagobi.eng.it", "Constraint"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "urlName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
