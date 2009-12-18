/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datareader;

import java.util.HashMap;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataReader {
    IDataStore read( Object data )throws EMFUserError, EMFInternalError;
}
