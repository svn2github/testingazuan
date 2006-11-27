/**
 * 
 */
package it.eng.spagobi.bo.lov;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;

import java.io.Serializable;

/**
 * @author Gioia
 *
 */
public interface ILovDetail extends Serializable {
	public String toXML ();
	public void loadFromXML (String dataDefinition) throws SourceBeanException;
	public String getLovResult(IEngUserProfile profile) throws Exception;
}
