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
 * Executes the query and obtains the data associated to the svg map
 */
public abstract class AbstractDatamartProvider implements IDatamartProvider {

	protected DatamartProviderConfiguration datamartProviderConfiguration;
	
	/**
	 * Builds the class
	 */
    public AbstractDatamartProvider(DatamartProviderConfiguration datamartProviderConfiguration) {
        super();
        setDatamartProviderConfiguration(datamartProviderConfiguration);
    }
    
    public AbstractDatamartProvider() {
        super();
    }

    /**
     * Executes the query and obtains the data associated to the svg map
     * @param datamartProviderConfiguration SourceBean wich contains the configuration 
     * for the data recovering (see template definition into GeoAction class)
     */
    public Datamart getDatamartObject() throws EMFUserError  {
        return null;
    }

	public DatamartProviderConfiguration getDatamartProviderConfiguration() {
		return datamartProviderConfiguration;
	}

	public void setDatamartProviderConfiguration(DatamartProviderConfiguration datamartProviderConfiguration) {
		this.datamartProviderConfiguration = datamartProviderConfiguration;
	}
	
	public SourceBean getDataDetails(String filterValue) throws EMFUserError {
		return null;
	}

}
