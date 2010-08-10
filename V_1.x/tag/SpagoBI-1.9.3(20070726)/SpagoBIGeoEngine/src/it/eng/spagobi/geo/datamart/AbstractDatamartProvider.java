/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.datamart;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;

/**
 * Executes the query and obtains the data associated to the svg map
 */
public abstract class AbstractDatamartProvider implements DatamartProviderIFace {

	/**
	 * Builds the class
	 */
    public AbstractDatamartProvider() {
        super();
    }

    /**
     * Executes the query and obtains the data associated to the svg map
     * @param datamartProviderConfiguration SourceBean wich contains the configuration 
     * for the data recovering (see template definition into GeoAction class)
     */
    public DatamartObject getDatamartObject(SourceBean datamartProviderConfiguration) throws EMFUserError  {
        return null;
    }

}
