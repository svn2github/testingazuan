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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractMapProvider implements MapProviderIFace {

    /**
     * 
     */
    public AbstractMapProvider() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see it.eng.geo.mapproviders.SVGMapProviderIFace#getSVGMap()
     */
    public abstract XMLDocumentIFace getSVGMapDocument(SourceBean mapProviderConfiguration);
    

}
