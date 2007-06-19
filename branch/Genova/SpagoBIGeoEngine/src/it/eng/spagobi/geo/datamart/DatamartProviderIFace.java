/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.datamart;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;

/**
 * Defines methods for the recovering of the datawarehouse data
 */
public interface DatamartProviderIFace {
    
	/**
     * Executes the query and obtains the data associated to the svg map
     * @param datamartProviderConfiguration SourceBean wich contains the configuration 
     * for the data recovering (see template definition into GeoAction class)
     */
    public abstract DatamartObject getDatamartObject(SourceBean datamartProviderConfiguration) throws EMFUserError;

}
