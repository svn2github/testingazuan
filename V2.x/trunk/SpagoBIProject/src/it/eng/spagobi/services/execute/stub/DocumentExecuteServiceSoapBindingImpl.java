/**
 * DocumentExecuteServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.execute.stub;

import it.eng.spagobi.services.execute.service.ServiceChartImpl;


public class DocumentExecuteServiceSoapBindingImpl implements it.eng.spagobi.services.execute.stub.DocumentExecuteService{
    public byte[] executeChart(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.util.HashMap in3) throws java.rmi.RemoteException {
	ServiceChartImpl impl=new ServiceChartImpl();
	return impl.executeChart(in0,in1,in2,in3);
    }

}
