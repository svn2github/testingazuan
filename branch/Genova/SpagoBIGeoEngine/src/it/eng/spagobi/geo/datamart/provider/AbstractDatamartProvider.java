/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.datamart.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.datamart.Datamart;

/**
 * Executes the query and obtains the data associated to the svg map
 */
public abstract class AbstractDatamartProvider implements IDatamartProvider {

	protected SourceBean datamartProviderConfiguration;
	
	/**
	 * Builds the class
	 */
    public AbstractDatamartProvider(SourceBean datamartProviderConfiguration) {
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

	public SourceBean getDatamartProviderConfiguration() {
		return datamartProviderConfiguration;
	}

	public void setDatamartProviderConfiguration(
			SourceBean datamartProviderConfiguration) {
		this.datamartProviderConfiguration = datamartProviderConfiguration;
	}

}
