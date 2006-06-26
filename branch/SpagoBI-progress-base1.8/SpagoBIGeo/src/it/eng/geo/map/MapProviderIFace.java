/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.map;

import it.eng.geo.document.XMLDocumentIFace;
import it.eng.spago.base.SourceBean;

/**
 * @author Administrator
 *
 */
public interface MapProviderIFace {
    
    public abstract XMLDocumentIFace getSVGMapDocument(SourceBean mapProviderConfiguration);// throws ServiceImplementationException;

}
