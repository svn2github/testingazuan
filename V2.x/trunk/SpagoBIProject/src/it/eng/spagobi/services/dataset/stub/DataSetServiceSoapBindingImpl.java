/**
 * DataSetServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.dataset.stub;

import it.eng.spagobi.services.dataset.service.DataSetWsImpl;

public class DataSetServiceSoapBindingImpl implements it.eng.spagobi.services.dataset.stub.DataSetWsInterface{
    public java.lang.String readData(java.util.HashMap in0, java.lang.String in1) throws java.rmi.RemoteException {
	DataSetWsImpl impl=new DataSetWsImpl();
	return impl.readData(in0, in1);
    }

}
