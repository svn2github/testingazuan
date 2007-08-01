/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.datamart.provider;

import java.sql.ResultSet;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.configuration.DatamartProviderConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;

/**
 * Defines methods for the recovering of the datawarehouse data
 */
public interface IDatamartProvider {
    
	/**
     * Executes the query and obtains the data associated to the svg map
     * for the data recovering (see template definition into GeoAction class)
     */
    public abstract Datamart getDatamartObject() throws EMFUserError;
    
    public SourceBean getDataDetails(String filterValue) throws EMFUserError;
    
    public DatamartProviderConfiguration getDatamartProviderConfiguration() ;

	public void setDatamartProviderConfiguration(DatamartProviderConfiguration datamartProviderConfiguration);

}
