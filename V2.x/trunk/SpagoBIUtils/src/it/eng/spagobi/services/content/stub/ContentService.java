/**
 * ContentService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.content.stub;

public interface ContentService extends java.rmi.Remote {
    public it.eng.spagobi.services.content.bo.Content readTemplate(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.util.HashMap in3) throws java.rmi.RemoteException;
    public it.eng.spagobi.services.content.bo.Content readSubObjectContent(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String saveSubObject(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, java.lang.String in6) throws java.rmi.RemoteException;
    public java.lang.String saveObjectTemplate(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
    public it.eng.spagobi.services.content.bo.Content downloadAll(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public java.lang.String publishTemplate(java.lang.String in0, java.lang.String in1, java.util.HashMap in2) throws java.rmi.RemoteException;
    public java.lang.String mapCatalogue(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5) throws java.rmi.RemoteException;
    public it.eng.spagobi.services.content.bo.Content readMap(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
}
