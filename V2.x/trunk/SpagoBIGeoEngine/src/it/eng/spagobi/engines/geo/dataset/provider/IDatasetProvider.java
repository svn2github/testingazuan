/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.dataset.provider;

import java.sql.ResultSet;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.engines.geo.configuration.DatamartProviderConfiguration;
import it.eng.spagobi.engines.geo.datamart.Datamart;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

/**
 * Defines methods for the recovering of the datawarehouse data
 */
public interface IDatasetProvider {
    
    public abstract Datamart getDatamartObject() throws EMFUserError;
    
    public SourceBean getDataDetails(String filterValue) throws EMFUserError;
    
    
    public DatamartProviderConfiguration getDatamartProviderConfiguration() ;

	public void setDatamartProviderConfiguration(DatamartProviderConfiguration datamartProviderConfiguration);

}
