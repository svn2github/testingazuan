/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/

package it.eng.spagobi.services.security.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SpagoBIUserProfile  implements java.io.Serializable {
    private java.util.HashMap attributes;

    private java.lang.String[] functions;

    private java.lang.String[] roles;

    private java.lang.String userId;

    /**
     * Instantiates a new spago bi user profile.
     */
    public SpagoBIUserProfile() {
    }

    /**
     * Instantiates a new spago bi user profile.
     * 
     * @param attributes the attributes
     * @param functions the functions
     * @param roles the roles
     * @param userId the user id
     */
    public SpagoBIUserProfile(
           java.util.HashMap attributes,
           java.lang.String[] functions,
           java.lang.String[] roles,
           java.lang.String userId) {
           this.attributes = attributes;
           this.functions = functions;
           this.roles = roles;
           this.userId = userId;
    }

    /**
     * Read user attribute.
     * 
     * @param key the key
     * 
     * @return the object
     */
    public Object readUserAttribute(String key){
	return attributes.get(key);
	
    }
    
    
    /**
     * Creates the role iterator.
     * 
     * @return the iterator
     */
    public Iterator createRoleIterator() {
	List rolesList=new ArrayList();
	if (roles==null) return null;
	int l=roles.length;
	for (int i=0;i<l;i++){
	    rolesList.add(roles[i]);
	}
        return rolesList.iterator();
    }
    
    /**
     * Creates the functions iterator.
     * 
     * @return the iterator
     */
    public Iterator createFunctionsIterator() {
	List functionsList=new ArrayList();
	if (functions==null) return null;
	int l=functions.length;
	for (int i=0;i<l;i++){
	    functionsList.add(functions[i]);
	}
        return functionsList.iterator();
    }    

    /**
     * Read attributes names.
     * 
     * @return the collection
     */
    public Collection readAttributesNames(){
	return attributes.values();
    }

    /**
     * Gets the attributes value for this SpagoBIUserProfile.
     * 
     * @return attributes
     */
    public java.util.HashMap getAttributes() {
        return attributes;
    }


    /**
     * Sets the attributes value for this SpagoBIUserProfile.
     * 
     * @param attributes the attributes
     */
    public void setAttributes(java.util.HashMap attributes) {
        this.attributes = attributes;
    }


    /**
     * Gets the functions value for this SpagoBIUserProfile.
     * 
     * @return functions
     */
    public java.lang.String[] getFunctions() {
        return functions;
    }


    /**
     * Sets the functions value for this SpagoBIUserProfile.
     * 
     * @param functions the functions
     */
    public void setFunctions(java.lang.String[] functions) {
        this.functions = functions;
    }


    /**
     * Gets the roles value for this SpagoBIUserProfile.
     * 
     * @return roles
     */
    public java.lang.String[] getRoles() {
        return roles;
    }


    /**
     * Sets the roles value for this SpagoBIUserProfile.
     * 
     * @param roles the roles
     */
    public void setRoles(java.lang.String[] roles) {
        this.roles = roles;
    }


    /**
     * Gets the userId value for this SpagoBIUserProfile.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this SpagoBIUserProfile.
     * 
     * @param userId the user id
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    

    private java.lang.Object __equalsCalc = null;
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SpagoBIUserProfile)) return false;
        SpagoBIUserProfile other = (SpagoBIUserProfile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributes==null && other.getAttributes()==null) || 
             (this.attributes!=null &&
              this.attributes.equals(other.getAttributes()))) &&
            ((this.functions==null && other.getFunctions()==null) || 
             (this.functions!=null &&
              java.util.Arrays.equals(this.functions, other.getFunctions()))) &&
            ((this.roles==null && other.getRoles()==null) || 
             (this.roles!=null &&
              java.util.Arrays.equals(this.roles, other.getRoles()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAttributes() != null) {
            _hashCode += getAttributes().hashCode();
        }
        if (getFunctions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFunctions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFunctions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRoles() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRoles());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRoles(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SpagoBIUserProfile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://bo.security.services.spagobi.eng.it", "SpagoBIUserProfile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("functions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "functions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roles");
        elemField.setXmlName(new javax.xml.namespace.QName("", "roles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object.
     * 
     * @return the type desc
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer.
     * 
     * @param mechType the mech type
     * @param _javaType the _java type
     * @param _xmlType the _xml type
     * 
     * @return the serializer
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
     * Get Custom Deserializer.
     * 
     * @param mechType the mech type
     * @param _javaType the _java type
     * @param _xmlType the _xml type
     * 
     * @return the deserializer
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
