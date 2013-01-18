/**
 * ArtifactServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.artifact.stub;

import it.eng.spagobi.services.artifact.service.ArtifactServiceImpl;

public class ArtifactServiceSoapBindingImpl implements it.eng.spagobi.services.artifact.stub.ArtifactService{
    public javax.activation.DataHandler getArtifactContentByNameAndType(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    		ArtifactServiceImpl service=new ArtifactServiceImpl();
        	return service.getArtifactContentByNameAndType(in0, in1, in2, in3);
    }

    public javax.activation.DataHandler getArtifactContentById(java.lang.String in0, java.lang.String in1, java.lang.Integer in2) throws java.rmi.RemoteException {
    	ArtifactServiceImpl service=new ArtifactServiceImpl();
        return service.getArtifactContentById(in0, in1, in2);
    }
    
    public it.eng.spagobi.services.artifact.bo.SpagoBIArtifact[] getArtifactsByType(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
    	ArtifactServiceImpl service=new ArtifactServiceImpl();
        return service.getArtifactsByType(in0, in1, in2);
    }

}
