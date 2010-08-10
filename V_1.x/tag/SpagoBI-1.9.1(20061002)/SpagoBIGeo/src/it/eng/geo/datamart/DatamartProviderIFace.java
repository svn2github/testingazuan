/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.datamart;

import it.eng.spago.base.SourceBean;

/**
 * @author Administrator
 *
 */
public interface DatamartProviderIFace {
    
    public abstract DatamartObject getDatamartObject(SourceBean datamartProviderConfiguration);// throws ServiceImplementationException;

}
