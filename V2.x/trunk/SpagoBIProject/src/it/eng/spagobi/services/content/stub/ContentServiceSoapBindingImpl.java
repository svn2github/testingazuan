/**
 * ContentServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.content.stub;

import it.eng.spagobi.services.content.service.ContentServiceImpl;

public class ContentServiceSoapBindingImpl implements it.eng.spagobi.services.content.stub.ContentService{
    public it.eng.spagobi.services.content.bo.Content readTemplate(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
	ContentServiceImpl service=new ContentServiceImpl();
	return service.readTemplate(in0, in1, in2);
    }
    public it.eng.spagobi.services.content.bo.Content readSubObjectContent(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String saveSubObject(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String saveObjectTemplate(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public it.eng.spagobi.services.content.bo.Content downloadAll(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String publishTemplate(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, java.lang.String in6, java.lang.String in7, java.lang.String in8, java.lang.String in9) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String mapCatalogue(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }
}
