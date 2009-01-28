/**
 * DataSourceServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.datasource.stub;

public interface DataSourceServiceService extends javax.xml.rpc.Service {
    public java.lang.String getDataSourceServiceAddress();

    public it.eng.spagobi.services.datasource.stub.DataSourceService getDataSourceService() throws javax.xml.rpc.ServiceException;

    public it.eng.spagobi.services.datasource.stub.DataSourceService getDataSourceService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
