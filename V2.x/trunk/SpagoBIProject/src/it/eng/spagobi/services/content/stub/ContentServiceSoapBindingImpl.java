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

}
